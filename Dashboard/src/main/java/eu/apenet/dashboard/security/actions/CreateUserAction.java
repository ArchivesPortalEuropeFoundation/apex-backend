package eu.apenet.dashboard.security.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.UserRole;

public class CreateUserAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2768649306123342939L;
	private final Logger log = Logger.getLogger(getClass());
	private String firstName;
	private String lastName;
	private String email;
	private Integer countryId;
	private Integer aiId;
	private List<SelectItem> partners = new ArrayList<SelectItem>();
	private Integer existingPartnerId;

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getAiId() {
		return aiId;
	}

	public void setAiId(Integer aiId) {
		this.aiId = aiId;
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

	public List<SelectItem> getPartners() {
		return partners;
	}

	public void setPartners(List<SelectItem> partners) {
		this.partners = partners;
	}

	public Integer getExistingPartnerId() {
		return existingPartnerId;
	}

	public void setExistingPartnerId(Integer existingPartnerId) {
		this.existingPartnerId = existingPartnerId;
	}

	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		if (aiId != null) {
			addBreadcrumb("institutionManagerManagement.action", getText("usermanagement.institution.title"));
			addBreadcrumb(getText("usermanagement.create.institutionmanager"));
		} else if (countryId != null) {
			addBreadcrumb("userManagement.action", getText("usermanagement.title"));
			addBreadcrumb(getText("usermanagement.create.countrymanager"));
		}

	}

	public void validate() {
		if (existingPartnerId == null) {
			if (this.getFirstName() != null) {
				if (this.getFirstName().length() == 0) {
					addFieldError("firstName", getText("firstname.required"));
				}
			}

			if (this.getLastName() != null) {
				if (this.getLastName().length() == 0) {
					addFieldError("lastName", getText("lastname.required"));
				}
			}

			if (this.getEmail() != null) {
				if (this.getEmail().length() == 0) {
					addFieldError("email", getText("email.required"));
				} else {
					if (UserService.exitsEmailUser(this.getEmail())) {
						addFieldError("email", getText("email.notAvailable"));
					}
					else {
						/*Email validation required to allow to create a new user*/
						String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			               CharSequence inputStr = email;
			               Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
			               Matcher matcher = pattern.matcher(inputStr);
			               /*A new method has been created to add the error message for an invalid email format*/
			               if(!matcher.matches())
			            	   addFieldError("email", getText("email.valid"));				
					}
				}
			}
		}

	}
	

	@Override
	public void prepare() throws Exception {
		if (aiId != null && countryId != null) {
			List<User> partners = DAOFactory.instance().getUserDAO()
					.getPartnersByCountryAndByRoleType(countryId, UserRole.ROLE_INSTITUTION_MANAGER);
			for (User partner : partners) {
				this.partners.add(new SelectItem(partner.getId(), partner.getName() + "(" + partner.getEmailAddress()
						+ ")"));
			}
		}
		super.prepare();
	}

	@Override
	public String input() throws Exception {
		// TODO Auto-generated method stub
		return super.input();
	}

	public String execute() {
		User partner = new User();
		partner.setFirstName(this.getFirstName());
		partner.setLastName(this.getLastName());
		partner.setEmailAddress(this.getEmail());
		if (aiId != null) {
			UserService.createOrAssociateInstitutionManager(partner, aiId, existingPartnerId);
		} else if (countryId != null) {
			UserService.createCountryManager(partner, countryId);
		}
		return SUCCESS;
	}


	public String cancel() {
		return SUCCESS;
	}

}
