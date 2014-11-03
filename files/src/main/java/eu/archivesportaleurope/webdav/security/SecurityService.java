package eu.archivesportaleurope.webdav.security;

import io.milton.http.exceptions.NotAuthorizedException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.User;
import eu.archivesportaleurope.webdav.security.SecurityService.LoginResult.LoginResultType;

public final class SecurityService {
	private static final Logger LOGGER = Logger.getLogger(SecurityService.class);


	public static LoginResult webDavLogin(String username, String password, HttpServletRequest request)
			throws NotAuthorizedException {
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
					context.login(request);
					type = LoginResultType.LOGGED_IN;
				} else {
					context = null;
					type = LoginResultType.BLOCKED;
				}
			} else {
				type = LoginResultType.INVALID_USERNAME_PASSWORD;
			}
			return new LoginResult(context, type);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
			throw new NotAuthorizedException();
		}

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
