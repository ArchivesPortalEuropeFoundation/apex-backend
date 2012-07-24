package eu.apenet.dashboard.actions;
	
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.dashboard.security.UserService;
import eu.apenet.persistence.vo.User;

/**
 * This class is a simple pre-basic forget Pwd Action that must be developed deeper in future.
 * Evolution: 
 * - Added validation of secret_question.
 * - Use of email question instead of user question. DesingTaskForce suggest. 
 * Pending the use of ESAPI. Concretely, the managers had to decide between use only filters or ESAPI api.
 * @author paul
 */

public class ForgetPwdAction extends ActionSupport implements SessionAware{
	
	private static final long serialVersionUID = -2521169541556876858L;
	private Logger log = Logger.getLogger(getClass());
	private String email;
	private String secretQuestion;
	private String secretAnswer;
	private String errorMessage;

	private Map <String, Object> session;
	
    /**
	 * <p> Recover the nick of the user who has forgotten his password. </p>
	 */
	
	public String execute() throws Exception {

		//log.trace("ForgetPwdAction: execute() method is called");
		log.info("ForgetPwdAction: execute() method is called");
		if (this.getEmail()!=null && session.get("forgetIn")!= "true"){
			User pu = UserService.getForgetUserByEmail(this.getEmail());
			this.setEmail(pu.getEmailAddress());
			this.setSecretQuestion(pu.getSecretQuestion());
			session.put("forgetIn","true");
			return INPUT;
		}
		else if (this.getEmail() != null && session.get("forgetIn")== "true"){
			UserService.sendChangePasswordLink(this.getEmail());
			addActionMessage(getText("success.user.forget.pwd"));
			return SUCCESS;
		}
		else {
			session.put("forgetIn","false");
			return INPUT;}
	}
	
	
	public void validate() {
		if (this.getEmail() != null){
			if (this.getEmail().length() == 0) {
				addFieldError("email", getText("email.required"));
			} else {

			if (!UserService.exitsEmailUser(this.getEmail())){
				addFieldError("email", getText("email.notInSystem"));
			} else if (this.getSecretAnswer() != null){
				if (this.getSecretAnswer().length() == 0) {
					addFieldError("secretAnswer", getText("secretAnswer.required"));
				} else {
					if (!UserService.correctSecretAnswerByEmail(this.getEmail(),this.getSecretAnswer())){
						addFieldError("secretAnswer", getText("secretAnswer.notCorrect"));
					}
				}
			}
			
			}

		}
	}

	public String getSecretQuestion() {
		return secretQuestion;
	}


	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}


	public String getSecretAnswer() {
		return secretAnswer;
	}


	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session = session;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}

