package eu.apenet.dashboard.actions;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.dashboard.security.PasswordValidator;
import eu.apenet.dashboard.security.PasswordValidator.ValidationResult;
import eu.apenet.dashboard.security.UserService;

public class ChangeForgetPwdAction extends ActionSupport {

	private static final long serialVersionUID = 2247851631123321105L;
	private Logger log = Logger.getLogger(getClass());
	private String password;
	private String repassword;
	private String validation_link;

	/**
	 * <p> Change forget pwd. </p>
	 */

	public String execute() throws Exception {
		log.info("ChangeForgetPwdAction: execute() method is called");
		//log.trace("ChangeForgetPwdAction: execute() method is called");
		if (this.getValidation_link() == null){
			//addFieldError("validation_link", getText("validation_link.required"));
			addActionError(getText("validation_link.required"));
			return ERROR;
		} else if (!UserService.exitsValidationLinkBefore(this.getValidation_link())){
			//addFieldError("validation_link", getText("validation_link.incorrect"));
			addActionError(getText("validation_link.incorrect"));
			return ERROR;
		} else {
			if ((this.getPassword() != null && this.getRepassword() != "")) {
				UserService.changePassword(this.getPassword(), this.getValidation_link());
				addActionMessage(getText("success.user.change.pwd"));
				return SUCCESS;
			} else{
				return INPUT;
			}
		}
	}

	public void validate() {
		log.debug("ChangeForgetPwdAction: validate() value of validation_link : "+validation_link);
	
		if (this.getPassword()!= null){
			if (this.getPassword().length() == 0) {
				addFieldError("password", getText("password.required"));
			} else {
				if (!this.getPassword().equals(this.getRepassword())){
					addFieldError("repassword", getText("repassword.notEquals"));
				}
				ValidationResult validationResult = PasswordValidator.validate(this.getPassword());
				if (!validationResult.isValid()){
					if (validationResult.isTooShort()){
						addFieldError("password", getText("password.tooShort"));
					}else {
						addFieldError("password", getText("password.notStrong"));
						
					}
				}
			}
		} 
	}
	


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}
	
	public String getValidation_link() {
		return validation_link;
	}

	public void setValidation_link(String validation_link) {
		this.validation_link = validation_link;
	}
	
}

