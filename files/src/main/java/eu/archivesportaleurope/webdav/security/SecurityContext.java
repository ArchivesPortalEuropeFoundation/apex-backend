package eu.archivesportaleurope.webdav.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.UserRole;

public final class SecurityContext{
	private static final Logger LOGGER = Logger.getLogger(SecurityContext.class);
	protected static final String VAR_SECURITY_CONTEXT = "var-security-context";
	protected static final String VAR_SECURITY_CONTEXT_WEBDAV = "var-security-context-webdav";
	private String emailAddress;
	private String name;
	private Integer partnerId;
	private Integer countryId = null;
	private String countryName = "UNKNOWN";
	private String countryIsoname;
	private List<Integer> aiIds = new ArrayList<Integer>();
	private String role;
	private SecurityContext parent;
	private String sessionId;
	private boolean webdav;

	protected SecurityContext(User partner, boolean webdav) {
		this.webdav = webdav;
		role = partner.getUserRole().getRole();
		if (!isAdminOrCoordinator()) {
			countryId = partner.getCountryId();
			countryName = partner.getCountry().getCname();
			countryIsoname = partner.getCountry().getIsoname();
			for (ArchivalInstitution ai : partner.getArchivalInstitutions()) {
				aiIds.add(ai.getAiId());
			}
			aiIds = Collections.unmodifiableList(aiIds);
		}
		partnerId = partner.getId();
		emailAddress = partner.getEmailAddress();
		name = partner.getFirstName() + " " + partner.getLastName();
		// used in globalRefresh_interval to store refresh interval in the session
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public List<Integer> getAiIds() {
		return aiIds;
	}


	public boolean isWebdav() {
		return webdav;
	}

	public String getRole() {
		return role;
	}



	public String getEmailAddress() {
		return emailAddress;
	}

	public String getName() {
		return this.name;
	}

	public String getParentName() {
		if (parent != null) {
			return parent.getName();
		}
		return null;
	}
	protected SecurityContext getParent(){
		return parent;
	}
	public boolean isChild() {
		return parent != null;
	}

	public boolean isCountryManager() {
		return UserRole.ROLE_COUNTRY_MANAGER.equals(role);
	}
	public boolean isCountryManagerCoordinator() {
		return UserRole.ROLE_COUNTRY_MANAGER_COORDINATOR.equals(role);
	}

	public boolean isInstitutionManager() {
		return UserRole.ROLE_INSTITUTION_MANAGER.equals(role);
	}

	public boolean isAdmin() {
		return UserRole.ROLE_ADMIN.equals(role);
	}
	public boolean isAdminOrCoordinator() {
		return UserRole.ROLE_ADMIN.equals(role) || UserRole.ROLE_COUNTRY_MANAGER_COORDINATOR.equals(role);
	}

	@Override
	public boolean equals(Object obj) {
		SecurityContext otherSecurityContext = (SecurityContext) obj;
		return this.getPartnerId().equals(otherSecurityContext.getPartnerId());
	}


	public static SecurityContext getWebDavContext(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return ((SecurityContext) session.getAttribute(VAR_SECURITY_CONTEXT_WEBDAV));
	}

	protected void login(HttpServletRequest request) {
		request.getSession().invalidate();
		HttpSession newSession = request.getSession(true);
		if (webdav){
			newSession.setAttribute(VAR_SECURITY_CONTEXT_WEBDAV, this);
		}else {
			newSession.setAttribute(VAR_SECURITY_CONTEXT, this);
		}
		sessionId = newSession.getId();
	}

	@Override
	public String toString() {
		String result = this.getEmailAddress();
		if (parent != null) {
			result += " (" + parent.toString() + ") ";
		}
		if (!isAdminOrCoordinator()){
			result += " - " + countryIsoname  + " ";
			result += " - ";
		}
		return result;
	}

	public String getCountryName() {
		return countryName;
	}

	public String getCountryIsoname() {
		return countryIsoname;
	}

	



	protected String getSessionId() {
		return sessionId;
	}


}
