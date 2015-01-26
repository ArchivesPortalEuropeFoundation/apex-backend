package eu.apenet.dashboard.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.DashboardAPEnetException;
import eu.apenet.dashboard.security.SecurityService.LoginResult.LoginResultType;
import eu.apenet.dashboard.security.cipher.BasicDigestPwd;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;

public final class SecurityService {
	private static final Logger LOGGER = Logger.getLogger(SecurityService.class);

	public static LoginResult login(String username, String password, boolean dropOtherSession)
			throws DashboardAPEnetException {
		try {

			// Init factory, dao and digest.
			UserDAO partnerdao = DAOFactory.instance().getUserDAO();

			// Checking credentials and state Active true
			User loginPartner = partnerdao.loginUser(username, BasicDigestPwd.generateDigest(password));
			// create security context;
			SecurityContext context = null;
			LoginResultType type = null;
			if (loginPartner != null) {
				if (loginPartner.isActive()) {
					context = new SecurityContext(loginPartner, false);
					if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()&& !context.isAdmin()) {
						context = null;
						type = LoginResultType.MAINTENANCE_MODE;
						LOGGER.info("User login tried with username: " + username + " when in maintenance mode");
					} else {
						if (dropOtherSession) {
							SecurityContextContainer.dropOtherSessions(context);
						}
						if (dropOtherSession || SecurityContextContainer.checkAvailability(context)) {
							context.login();
							ChangeControl.logOperation(ChangeControl.LOG_IN_OPERATION);
							type = LoginResultType.LOGGED_IN;
						} else {
							context = null;
							type = LoginResultType.ALREADY_IN_USE;
						}
					}
				} else {
					context = null;
					type = LoginResultType.BLOCKED;
				}
			} else {
				type = LoginResultType.INVALID_USERNAME_PASSWORD;
			}
			return new LoginResult(context, type);
		} catch (Exception e) {
			throw new DashboardAPEnetException("Unable to login with username" + username, e);
		}

	}

	public static LoginResult webDavLogin(String username, String password, HttpServletRequest request)
			throws DashboardAPEnetException {
		try {

			// Init factory, dao and digest.
			UserDAO partnerdao = DAOFactory.instance().getUserDAO();

			// Checking credentials and state Active true
			User loginPartner = partnerdao.loginUser(username, BasicDigestPwd.generateDigest(password));
			// create security context;
			SecurityContext context = null;
			LoginResultType type = null;
			if (loginPartner != null) {
				if (loginPartner.isActive()) {
					context = new SecurityContext(loginPartner, true);
					if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()&& !context.isAdminOrCoordinator()) {
						context = null;
						type = LoginResultType.MAINTENANCE_MODE;
						LOGGER.info("User login tried with username: " + username + " when in maintenance mode");
					} else {
						context.login(request);
						type = LoginResultType.LOGGED_IN;
					}
				} else {
					context = null;
					type = LoginResultType.BLOCKED;
				}
			} else {
				type = LoginResultType.INVALID_USERNAME_PASSWORD;
			}
			return new LoginResult(context, type);
		} catch (Exception e) {
			throw new DashboardAPEnetException("Unable to login with username" + username, e);
		}

	}

	public static void logout(boolean logoutToParent) {
		ChangeControl.logOperation(ChangeControl.LOG_OUT_OPERATION);
		SecurityContext securityContext = SecurityContext.get();
		if (securityContext != null) {
			securityContext.logout(logoutToParent);
		}
	}

	public static void deleteSession(String sessionId) {
		if (SecurityContext.get().isAdmin()) {
			SecurityContextContainer.dropSession(sessionId);
		}
	}

	public static LoginResult loginAsCountryManager(Integer partnerId) {
		SecurityContext currentSecurityContext = SecurityContext.get();
		SecurityContext context = null;
		LoginResultType type = null;
		if (currentSecurityContext != null && currentSecurityContext.isAdminOrCoordinator()) {
			User partner = DAOFactory.instance().getUserDAO().findById(partnerId);
			// create security context;

			if (partner != null) {
				context = new SecurityContext(partner, currentSecurityContext);
				if (SecurityContextContainer.checkAvailability(context)) {
					context.login();
					ChangeControl.logOperation(ChangeControl.LOG_IN_OPERATION);
					type = LoginResultType.LOGGED_IN;
				} else {
					context = null;
					type = LoginResultType.ALREADY_IN_USE;
				}

			} else {
				type = LoginResultType.NO_PARTNER;
			}
		} else {
			type = LoginResultType.ACCESS_DENIED;
		}
		return new LoginResult(context, type);

	}

	public static LoginResult loginAsInstitutionManager(Integer selectedAiId) {
		SecurityContext currentSecurityContext = SecurityContext.get();
		SecurityContext context = null;
		LoginResultType type = null;
		if (currentSecurityContext != null
				&& (currentSecurityContext.isAdminOrCoordinator() || currentSecurityContext.isCountryManager())) {
			ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
			ArchivalInstitution ai = aiDao.getArchivalInstitution(selectedAiId);
			User partner = ai.getPartner();
			// create security context;
			context = new SecurityContext(partner, currentSecurityContext);
			if (SecurityContextContainer.checkAvailability(context)) {
				context.login();
				ChangeControl.logOperation(ChangeControl.LOG_IN_OPERATION);
				type = LoginResultType.LOGGED_IN;
			} else {
				context = null;
				type = LoginResultType.ALREADY_IN_USE;
			}
		} else {
			type = LoginResultType.ACCESS_DENIED;
		}
		return new LoginResult(context, type);

	}

	public static void selectArchivalInstitution(Integer aiId) {
		SecurityContext securityContext = SecurityContext.get();
		ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(aiId);
		if (archivalInstitution != null) {
			securityContext.changeArchivalInstitution(archivalInstitution);
		}

	}

	public static User getCurrentPartner() {
		UserDAO partnerDao = DAOFactory.instance().getUserDAO();
		SecurityContext context = SecurityContext.get();
		if (context == null) {
			return null;
		}
		return partnerDao.findById(context.getPartnerId());
	}

	public static class LoginResult {
		public enum LoginResultType {
			LOGGED_IN, ALREADY_IN_USE, INVALID_USERNAME_PASSWORD, ACCESS_DENIED, NO_PARTNER, BLOCKED, MAINTENANCE_MODE
		}

		private SecurityContext context;
		private LoginResultType type;

		protected LoginResult(SecurityContext context, LoginResultType type) {
			this.context = context;
			this.type = type;
		}

		public SecurityContext getContext() {
			return context;
		}

		public LoginResultType getType() {
			return type;
		}

	}

}
