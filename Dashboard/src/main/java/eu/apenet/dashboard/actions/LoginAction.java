package eu.apenet.dashboard.actions;

import java.util.Date;

import eu.apenet.dashboard.listener.QueueDaemon;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.SecurityService.LoginResult;
import eu.apenet.dashboard.security.SecurityService.LoginResult.LoginResultType;


public class LoginAction extends AbstractAction {

	private static final long serialVersionUID = -5297030073907772917L;
	private Logger log = Logger.getLogger(getClass());

	private String username;
	private String password;
	private boolean dropOtherSession = false;






	public String execute() throws Exception {
        if (APEnetUtilities.getDashboardConfig().isMaintenanceMode()){
        	this.getServletRequest().setAttribute("maintenanceMode", true);
        } else if (!QueueDaemon.isActive()) {
			this.getServletRequest().setAttribute("queueClosed", true);
		}
		if ((this.getUsername() != null) && (this.getPassword() != null)) {
			LoginResult loginResult = null;
			try {
				loginResult = SecurityService.login(this.getUsername(), this.getPassword(), dropOtherSession);
				if (LoginResultType.LOGGED_IN.equals(loginResult.getType())){
					addActionMessage(getText("success.user.login") + " "
							+ new Date(ServletActionContext.getRequest().getSession().getLastAccessedTime()));
					return SUCCESS;			
				}else if (LoginResultType.ALREADY_IN_USE.equals(loginResult.getType())){
					addActionError(getText("user.already.inuse"));
					return INPUT;
				}else if (LoginResultType.BLOCKED.equals(loginResult.getType())){
					addActionError(getText("user.blocked"));
					return INPUT;
				}else if (LoginResultType.MAINTENANCE_MODE.equals(loginResult.getType())){
					addActionError(getText("user.maintenancemode"));
					return INPUT;
				}else {
                                        log.error("Fail to login with user: "+username);
					this.setUsername("");
					addActionError(getText("emailpassword.invalid"));	
					return INPUT;
				}

			} catch (Exception e) {
				log.error("Unable to login " + e.getMessage(), e);
				addActionError(getText("error.user.login-database"));
				return ERROR;
			}

		} else {
			return INPUT;
		}
	}

	/**
	 * <p>
	 * Validate a user login.
	 * </p>
	 */
	public void validate() {
		if (this.getUsername() != null) {
			if (this.getUsername().length() == 0) {
				addFieldError("username", getText("email.required"));
			}
		}
		if (this.getPassword() != null) {
			if (this.getPassword().length() == 0) {
				addFieldError("password", getText("password.required"));
			}
		}

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public boolean isDropOtherSession() {
		return dropOtherSession;
	}

	public void setDropOtherSession(boolean dropOtherSession) {
		this.dropOtherSession = dropOtherSession;
	}


}
