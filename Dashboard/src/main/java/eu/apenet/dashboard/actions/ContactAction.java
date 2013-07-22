package eu.apenet.dashboard.actions;
	
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;

import java.io.File;
import java.io.InputStream;

import javax.servlet.Servlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		addTo();
		if (this.getEmail() != null && this.getFeedbackText() != null){
			UserService.sendEmailFeedback(this.getEmail(),this.getFeedbackText(), this.subjectsMenu);
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
	
	public void addTo(){
		try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Map<String,String> dropDownTable = new HashMap<String,String>();
            String file_path = "emails/email-configuration.xml";
            InputStream path = Thread.currentThread().getContextClassLoader().getResourceAsStream(file_path);
            Document documento = builder.parse(path);
            mails = readConfigFile(dropDownTable,documento);
     
		}catch(Exception e){
            e.printStackTrace();
        }
	}
	
    public Map<String, String> readConfigFile(Map<String,String> dropDownTable,Node section){
	   if(section != null){
			NodeList emails = section.getChildNodes();
			String subject = new String();
			String to = new String();
			for(int i = 0; i < emails.getLength(); i++){
			    Node email = emails.item(i);
			    if(email.hasChildNodes() && !email.getNodeName().equals("subject") && !email.getNodeName().equals("to")){
			    	dropDownTable = readConfigFile(dropDownTable,email);
			    }else{
			    	if (email.getNodeName().equals("subject")) {
						subject=getText(email.getTextContent());
					}
					if (email.getNodeName().equals("to")) {
						to=email.getTextContent();
					}
			    }
			}
			if(section.getNodeName().equals("email")){
				dropDownTable.put(subject, to);
			}
        }
	   return dropDownTable;
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

