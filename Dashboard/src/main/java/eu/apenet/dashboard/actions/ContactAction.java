package eu.apenet.dashboard.actions;
	
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.dashboard.utils.PropertiesUtil;
import eu.apenet.persistence.vo.User;

/**
 * @author paul
 */

public class ContactAction extends ActionSupport{
	
	private static final long serialVersionUID = -138629601121582616L;
	private Logger log = Logger.getLogger(getClass());
	private String email;
	private String feedbackText;
	public String toList;
	
	private Map<String, String> mails;
	
	private String breadcrumbLinks;

	private String subjectsMenu;

	public void setSubjectsMenu(String subjectsMenu) {
		this.subjectsMenu = subjectsMenu;
	}

	/**
	 * <p> Send a email contact. In case of user logged, the system fill out the field automatically and not show Captcha </p>
	 */
	
	public String execute() throws Exception {
		mails = new LinkedHashMap<String, String>();
		mails.put("ape.emails.troubles", getText("xmlMail.troubles"));
		mails.put("ape.emails.data", getText("xmlMail.data"));
		mails.put("ape.emails.processing", getText("xmlMail.procesing"));
		mails.put("ape.emails.europeana", getText("xmlMail.europeana"));
		mails.put("ape.emails.other", getText("xmlMail.other"));
		mails.put("ape.emails.suggestions", getText("xmlMail.suggestions"));
		if (this.getEmail() != null && this.getFeedbackText() != null){
			String toEmail = PropertiesUtil.get(subjectsMenu);
			UserService.sendEmailFeedback(this.getEmail(),this.getFeedbackText(), toEmail);
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

	public Map<String, String> getMails() {
		return mails;
	}

	public void setMails(Map<String, String> mails) {
		this.mails = mails;
	}
}

