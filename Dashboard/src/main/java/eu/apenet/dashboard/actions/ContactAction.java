package eu.apenet.dashboard.actions;
	
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.UserService;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author paul
 */

public class ContactAction extends ActionSupport{
	
	private static final long serialVersionUID = -138629601121582616L;
	private Logger log = Logger.getLogger(getClass());
	private String email;
	private String feedbackText;
	public String toList;
	
	private String breadcrumbLinks;
	
	
	/**
	 * <p> Send a email contact. In case of user logged, the system fill out the field automatically and not show Captcha </p>
	 */
	
	public String execute() throws Exception {
		addTo();
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
	
	public void addTo(){
		try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            List<String> dropDownSubject = new ArrayList<String>();
            List<String> dropDownTo = new ArrayList<String>();
            Map<String,String> dropDownTable = new HashMap<String, String>();
            String ruta = "emails/DIR_LIST.xml";
            InputStream path = Thread.currentThread().getContextClassLoader().getResourceAsStream(ruta);
            Document documento = builder.parse(path);
            dropDownTable = readConfigFile(dropDownTable,documento, dropDownSubject, dropDownTo);
            
            Iterator it = dropDownTable.entrySet().iterator();
            while (it.hasNext()) {
	            Map.Entry e = (Map.Entry)it.next();
	            System.out.println(e.getKey() + " " + e.getValue());
            }
     
		}catch(Exception e){
            e.printStackTrace();
        }
	}
	
   public Map<String, String> readConfigFile(Map<String,String> dropDownTable,Node section, List<String> subject, List<String> to ){
	   if(section != null){
			if (section.getNodeName().equals("subject")) {
				System.out.println(section.getNodeName() + ": " + getText(section.getTextContent()));
				subject.add(getText(section.getTextContent()));
			}
			
			if (section.getNodeName().equals("to")) {
				System.out.println(section.getNodeName() + ": " + section.getTextContent());
				to.add(section.getTextContent());
			}
			
			dropDownTable.put(subject.toString(), to.toString());
			
			NodeList field = section.getChildNodes();
			for(int i = 0; i < field.getLength(); i++){
			    Node data = field.item(i);
			    dropDownTable = readConfigFile(dropDownTable,data, subject, to);
			    
			    if(!subject.toString().contains("[]") || !to.toString().contains("[]"))
			    {	    	
			    		
			    }

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
}

