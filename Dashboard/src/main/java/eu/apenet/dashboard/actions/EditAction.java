package eu.apenet.dashboard.actions;

import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.PasswordValidator;
import eu.apenet.dashboard.security.PasswordValidator.ValidationResult;
import eu.apenet.dashboard.security.SecurityService.LoginResult;
import eu.apenet.dashboard.security.SecurityService.LoginResult.LoginResultType;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.dashboard.security.cipher.BasicDigestPwd;
import eu.apenet.persistence.vo.User;

public class EditAction extends AbstractAction {

	private static final long serialVersionUID = -4369675443958545447L;

	private int numberOfCall;
	private String changeAllow;

	private Logger log = Logger.getLogger(getClass());
	private String firstName;
	private String lastName;
	private String email;
	private String secretQuestion;
	private String secretAnswer;
	private String currentPassword;
	private String newPassword;
	private String rePassword;
	private String parent;
	public String confirmPassword;

	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.editUserInformation"));
	}

	/**
	 * <p>
	 * Edit some user information, and let modify it
	 * </p>
	 */
	public String execute() throws Exception {
		// checkUserRoleToBuildBreadcrumbs();
		User userToUpdate = new User();
		userToUpdate.setId( SecurityService.getCurrentPartner().getId());
		boolean changePwd = false;

		if (StringUtils.isNotBlank(this.newPassword) || StringUtils.isNotBlank(this.rePassword) || StringUtils.isNotBlank(this.currentPassword)) {
			if (!validateChangePwd()) {
				return INPUT;
			} else {
				userToUpdate.setPassword(this.getNewPassword());
				changePwd = true;
			}
		}
		if ((changePwd == true)
				|| (this.getFirstName() != null && this.getLastName() != null && this
						.getEmail() != null && this.getSecretAnswer() != null && this.getSecretQuestion() != null)
				&& (!this.getFirstName().equalsIgnoreCase(userToUpdate.getFirstName())
						|| !this.getLastName().equalsIgnoreCase(userToUpdate.getLastName()) || !this.getEmail()
						.equalsIgnoreCase(userToUpdate.getEmailAddress())
						|| !this.getSecretAnswer().equalsIgnoreCase(userToUpdate.getSecretAnswer())
								|| !this.getSecretQuestion().equalsIgnoreCase(userToUpdate.getSecretQuestion())
						)) {

			userToUpdate.setFirstName(this.getFirstName());
			userToUpdate.setLastName(this.getLastName());
			userToUpdate.setEmailAddress(this.getEmail());
			userToUpdate.setSecretAnswer(this.getSecretAnswer());
			userToUpdate.setSecretQuestion(this.getSecretQuestion());
			//after editing the user will be logged out and logged in
			try{
				if (relog(email,newPassword,userToUpdate)){
					addActionMessage(getText("success.user.edit"));
					return SUCCESS;
				}
				else{
					addActionMessage(getText("oldpassword.notEquals"));
				}
			}
			catch (Exception e) {
				log.error("Unable to relog " + e.getMessage(), e);
				return INPUT;
			}
		} else {
			return INPUT;
		}
		return INPUT;
	}
	
	private boolean relog(String email, String passw, User userToUpdate) throws Exception
	{
		log.trace("relog() method is called");
		try {
			//if the password provided by the user matches with the password the system has, it will be update the user data and update the label
			if(getConfirmPassword().compareTo(passw)==0){
				UserService.updateUser(userToUpdate);
				SecurityService.logout("true".equals(parent));
				LoginResult loginResult = SecurityService.login(email, passw, true);
				log.trace("relog() method has finished right.");
				return true;
			}
		}
		catch (Exception e){
			log.trace("relog() method is called with " + e);
			return false;
		}
		return false;
	}

	public boolean validateChangePwd() {
		boolean result = true;
		if (StringUtils.isBlank(getCurrentPassword())){
			addFieldError("currentPassword", getText("currentPassword.required"));
			result = false;
		}else {
			String currentPassword = this.getCurrentPassword().trim();
			User userToUpdate = SecurityService.getCurrentPartner();
			if (!(BasicDigestPwd.generateDigest(currentPassword).equals(userToUpdate.getPassword()))) {
				addFieldError("oldPassword", getText("currentPassword.notEquals"));
				result = false;
			}
		}
		if (StringUtils.isBlank(getNewPassword())){
			addFieldError("newPassword", getText("password.required"));
			result = false;			
		}else {
			String newPassword = this.getNewPassword().trim();
			if (rePassword == null || !newPassword.equals(this.getRePassword().trim())) {
				addFieldError("rePassword", getText("reNewpassword.notEquals"));
				result = false;
			}
			ValidationResult validationResult = PasswordValidator.validate(newPassword);
			if (!validationResult.isValid()) {
				if (validationResult.isTooShort()) {
					addFieldError("newPassword", getText("password.tooShort"));
				} else {
					addFieldError("newPassword", getText("password.notStrong"));

				}
				return false;
			}
		}
		return result;
	}

	public void validate() {

		if (this.getFirstName() != null) {
			if (this.getFirstName().length() == 0) {
				addFieldError("lastName", getText("firstname.required"));
			}
		}

		if (this.getLastName() != null) {
			if (this.getLastName().length() == 0) {
				addFieldError("firstName", getText("lastname.required"));
			}
		}

		if (this.getEmail() != null) {
			if (this.getEmail().length() == 0) {
				addFieldError("email", getText("email.required"));
			}

		}
		if (this.getSecretAnswer() != null) {
			if (this.getSecretAnswer().length() == 0) {
				addFieldError("secretAnswer", getText("secretAnswer.required"));
			}

		}	
		if (this.getSecretQuestion() != null) {
			if (this.getSecretQuestion().length() == 0) {
				addFieldError("secretQuestion", getText("secretQuestion.required"));
			}

		}
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getChangeAllow() {
		return changeAllow;
	}

	public void setChangeAllow(String changeAllow) {
		this.changeAllow = changeAllow;
	}

	public void setNumberOfCall(int numberOfCall) {
		this.numberOfCall = numberOfCall;
	}

	public int getNumberOfCall() {
		return numberOfCall;
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

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}
	
	private String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
