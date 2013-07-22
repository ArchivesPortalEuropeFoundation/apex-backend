package eu.apenet.dashboard.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public final class SecurityContextContainer implements HttpSessionListener {
	private static final Logger LOGGER = Logger.getLogger(SecurityContextContainer.class);

	private static Map<Integer, SecurityContext> securityContexts = new HashMap<Integer, SecurityContext>();
	private static Map<String, HttpSession> activeSessions = new HashMap<String, HttpSession>();

	protected static void putSecurityContext(SecurityContext securityContext) {
		synchronized (securityContexts) {
			securityContexts.put(securityContext.getPartnerId(), securityContext);
		}
	}

	protected static void deleteSecurityContext(SecurityContext securityContext) {
		synchronized (securityContexts) {
			if (securityContexts.containsKey(securityContext.getPartnerId())) {
				securityContexts.remove(securityContext.getPartnerId());
			}

		}
	}

	protected static boolean checkAvailability(SecurityContext otherSecurityContext) {
		boolean available = true;
		if (otherSecurityContext != null) {
			synchronized (securityContexts) {
				available = !securityContexts.containsKey(otherSecurityContext.getPartnerId());
			}
		}
		if (!available) {
			LOGGER.info(otherSecurityContext.getEmailAddress() + " is already in use.");
		}

		return available;
	}
	public static boolean checkAvailability(Integer partnerId) {
		boolean available = true;
		if (partnerId != null) {
				available = !securityContexts.containsKey(partnerId);
		}
		return available;
	}
	protected static void dropOtherSessions(SecurityContext securityContext) {
		SecurityContext otherSecurityContext = null;
		synchronized (securityContexts) {
			otherSecurityContext = securityContexts.get(securityContext.getPartnerId());
		}
		if (otherSecurityContext != null){
			dropSession(otherSecurityContext.getSessionId());
		}
	}

	protected static void dropSession(String sessionId) {
		HttpSession session = activeSessions.get(sessionId);
		try {
			session.invalidate();
		} catch (Exception e) {
			LOGGER.error("Something went wrong while invalidating the session", e);
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		synchronized (activeSessions) {
			activeSessions.put(event.getSession().getId(), event.getSession());
		}

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		synchronized (activeSessions) {
			activeSessions.remove(event.getSession().getId());
		}

	}

	public static List<SessionInfo> retrieveSessionInfo() {
		List<SessionInfo> sessionInfos = new ArrayList<SessionInfo>();
		for (HttpSession session : activeSessions.values()) {
			// normal security context
			SecurityContext securityContext = (SecurityContext) session.getAttribute(SecurityContext.VAR_SECURITY_CONTEXT);
			if (securityContext != null){
				sessionInfos.add(new SessionInfo(session.getCreationTime(), session.getLastAccessedTime(),
						securityContext, session.getId()));
			}
			// webdav security context
			securityContext = (SecurityContext) session.getAttribute(SecurityContext.VAR_SECURITY_CONTEXT_WEBDAV);
			if (securityContext != null){
				sessionInfos.add(new SessionInfo(session.getCreationTime(), session.getLastAccessedTime(),
						securityContext, session.getId()));
			}

		}
		return sessionInfos;
	}

	public static class SessionInfo {
		private Date creationTime;
		private Date lastAccessedTime;
		private String emailAddress;
		private String name;
		private String country;
		private String institution;
		private String role;
		private String sessionId;
		private List<String> parentInfo = new ArrayList<String>();
		private boolean webdav;

		private SessionInfo(long loginTime, long lastAccessedTime, SecurityContext securityContext, String sessionId) {
			this.creationTime = new Date(loginTime);
			this.lastAccessedTime = new Date(lastAccessedTime);
			if (securityContext != null) {
				this.country = securityContext.getCountryName();
				this.name = securityContext.getName();
				this.emailAddress = securityContext.getEmailAddress();
				this.role = securityContext.getRole();
				if (securityContext.getSelectedInstitution() != null) {
					this.institution = securityContext.getSelectedInstitution().getName();
				}
				SecurityContext parent = securityContext.getParent();
				while (parent != null){
					parentInfo.add(parent.getName() + " (" + getRole() + ")");
					parent = parent.getParent();
				}
				this.webdav = securityContext.isWebdav();

			}
			this.sessionId = sessionId;
		}

		public Date getCreationTime() {
			return creationTime;
		}

		public Date getLastAccessedTime() {
			return lastAccessedTime;
		}


		public String getEmailAddress() {
			return emailAddress;
		}

		public String getName() {
			return name;
		}

		public String getCountry() {
			return country;
		}

		public String getInstitution() {
			return institution;
		}

		public String getRole() {
			return role;
		}

		public String getSessionId() {
			return sessionId;
		}

		public List<String> getParentInfo() {
			return Collections.unmodifiableList(parentInfo);
		}

		public boolean isWebdav() {
			return webdav;
		}
		

	}

}
