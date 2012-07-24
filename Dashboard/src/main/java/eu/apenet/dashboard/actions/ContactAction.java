package eu.apenet.dashboard.actions;
	
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.persistence.vo.User;

/**
 * @author paul
 */

public class ContactAction extends ActionSupport{
	
	private static final long serialVersionUID = -138629601121582616L;
	private Logger log = Logger.getLogger(getClass());
	private String email;
	private String feedbackText;
	
	private String breadcrumbLinks;
	
	
	/**
	 * <p> Send a email contact. In case of user logged, the system fill out the field automatically and not show Captcha </p>
	 */
	
	public String execute() throws Exception {
		if (this.getEmail() != null && this.getFeedbackText() != null){
			UserService.sendEmailFeedback(this.getEmail(),this.getFeedbackText());
			addActionMessage(getText("success.feedback.contact"));
			return SUCCESS;
		}
		else { 
			User userLogged = SecurityService.getCurrentPartner();
			if (userLogged!=null){
				this.setEmail(userLogged.getEmailAddress());
			}
			return INPUT;}
	}
	
	public void validate() {
		//log.info("validate() method is called");
		if (this.getFeedbackText() != null){
			if (this.getFeedbackText().length() == 0) {
				addFieldError("feedbackText", getText("feedbackText.required"));
			} 
		}
		
		if (this.getEmail() != null){
			if (this.getEmail().length() == 0) {
				addFieldError("email", getText("email.required"));
			} 
		}
	}
	

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public void setBreadcrumbLinks(String breadcrumbLinks) {
		this.breadcrumbLinks = breadcrumbLinks;
	}

	public String getBreadcrumbLinks() {
		return breadcrumbLinks;
	}
}

