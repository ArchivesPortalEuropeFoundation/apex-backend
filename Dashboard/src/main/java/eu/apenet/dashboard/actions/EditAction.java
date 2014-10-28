package eu.apenet.dashboard.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.Breadcrumb;
import eu.apenet.dashboard.security.PasswordValidator;
import eu.apenet.dashboard.security.PasswordValidator.ValidationResult;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.dashboard.security.cipher.BasicDigestPwd;
import eu.apenet.persistence.vo.User;

public class EditAction extends AbstractAction {

	private static final long serialVersionUID = -4369675443958545447L;

	private int numberOfCall;
	private String changeAllow;
	private String messageError;
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
	private List<Breadcrumb> breadcrumbRoute;

	@Override
	protected void buildBreadcrumbs() {
		this.breadcrumbRoute = new ArrayList<Breadcrumb>();
		Breadcrumb breadcrumb = new Breadcrumb(null,getText("breadcrumb.section.changeAIname"));
		this.breadcrumbRoute.add(breadcrumb);
	}

	/**
	 * <p>
	 * Edit some user information, and let modify it
	 * </p>
	 */
	public String execute() throws Exception {
		User userToUpdate = new User();
		userToUpdate.setId( SecurityService.getCurrentPartner().getId());
		boolean changePwd = false;
		buildBreadcrumbs();
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

			userToUpdate.setFirstName(this.getFirstName().trim());
			userToUpdate.setLastName(this.getLastName().trim());
			userToUpdate.setEmailAddress(this.getEmail().trim()); //save the email-address without blanks
			userToUpdate.setSecretAnswer(this.getSecretAnswer());
			userToUpdate.setSecretQuestion(this.getSecretQuestion());
			//after editing the user will be logged out and logged in
			try{
				if (changePwd) {
					if (relog(email, this.getNewPassword(),userToUpdate, changePwd)){
						addActionMessage(getText("success.user.edit"));
						return SUCCESS;
					}
					else{
						addActionMessage(getText("oldpassword.notEquals"));
						SecurityContext securityContext = SecurityContext.get();
						if (securityContext!=null && securityContext.isAdmin()) {
							return "error_admin";
						}
						return ERROR;
					}
				} else {
					if (relog(email, this.getConfirmPassword(),userToUpdate, changePwd)){
						addActionMessage(getText("success.user.edit"));
						return SUCCESS;
					}
					else{
						addActionMessage(getText("oldpassword.wrong"));
						SecurityContext securityContext = SecurityContext.get();
						if (securityContext!=null && securityContext.isAdmin()) {
							return "error_admin";
						}
						return ERROR;
					}
				}
			}
			catch (Exception e) {
				log.error("Unable to relog " + e.getMessage(), e);
				//return INPUT;
				return ERROR;
			}
		} else {
			return INPUT;
		}
	//	return INPUT;
	}
	
	private boolean relog(String email, String passw, User userToUpdate, boolean changePwd) throws Exception
	{
		log.trace("relog() method is called");
		Integer aiId = null;
		if (SecurityContext.get().getSelectedInstitution() != null) {
			aiId = SecurityContext.get().getSelectedInstitution().getId();
		}
		try {
			//if the user has changed the password is needed to check usertoupdate password
			if (changePwd){
				//if the password provided by the user matches with the password the system has, it will be update the user data and update the label
				if(userToUpdate.getPassword().compareTo(passw)==0){
					UserService.updateUser(userToUpdate);
					SecurityService.logout("true".equals(parent));
					SecurityService.login(email, passw, true);
					if (aiId != null)
						SecurityService.selectArchivalInstitution(aiId);
					log.trace("relog() method has finished right.");
					return true;
				}
			}
			//if the user has not changed the password is needed to check the current user password
			else{
				//if the password provided by the user matches with the password the system has, it will be update the user data and update the label
				if(SecurityService.getCurrentPartner().getPassword().compareTo(BasicDigestPwd.generateDigest(passw))==0){
					UserService.updateUser(userToUpdate);
					SecurityService.logout("true".equals(parent));
					SecurityService.login(email, passw, true);
					if (aiId != null)
						SecurityService.selectArchivalInstitution(aiId);					
					log.trace("relog() method has finished right.");
					return true;
				}else{
					return false;
				}
			}
		}
		catch (Exception e){
			log.trace("relog() method is called with " + e);
			return false;
		}
		return true;
	}

	public boolean validateChangePwd() {
		boolean result = true;
		this.setMessageError("true");
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
				result = false;
			}
		}

		if (!result) {
			this.setMessageError("false");
			addActionError(getText("password.errorChange"));
		}
		
		return result;
	}
	
	@Override
	public void validate() {
		
		String currentEmail=SecurityService.getCurrentPartner().getEmailAddress(); //the actual e-mail that the partner has

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
			if (this.getEmail().trim().length() == 0) {
				addFieldError("email", getText("email.required"));
			}else if (UserService.exitsEmailUser(this.getEmail().trim()) && !currentEmail.equalsIgnoreCase(this.getEmail().trim())) {
				addFieldError("email", getText("email.alreadyUsed"));	//the e-mail is already in use
			}else{
	            Matcher matcher = APEnetUtilities.EMAIL_PATTERN.matcher(this.getEmail().trim());
	            if(!matcher.matches()){
	                addFieldError("email", getText("email.valid"));  //it's an invalid e-mail
	            }
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
		buildBreadcrumbs();
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
	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}
	public String getMessageError() {
		return messageError;
	}
	
}
