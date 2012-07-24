package eu.apenet.dashboard.actions;
	
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.persistence.vo.User;

public class ViewAction extends AbstractAction {
	
	private static final long serialVersionUID = -5957772691331439159L;
	private String firstName;
	private String lastName;
	private String email;
	private String secretQuestion;
	private String secretAnswer;
	
	
	
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.editUserInformation"));
	}
	
    /**
	 * <p> Recover and show the information of the logged user. </p>
	 */
	
	public String execute() throws Exception {
		User userLogged = SecurityService.getCurrentPartner();
		this.setFirstName(userLogged.getFirstName());
		this.setLastName(userLogged.getLastName());
		this.setEmail(userLogged.getEmailAddress());
		this.setSecretAnswer(userLogged.getSecretAnswer());
		this.setSecretQuestion(userLogged.getSecretQuestion());
		return INPUT;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	
}

