package eu.apenet.dashboard.security.actions;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContextContainer;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.SecurityService.LoginResult;
import eu.apenet.dashboard.security.SecurityService.LoginResult.LoginResultType;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.UserRole;

/**
 * @author bastiaan
 *
 */
public class UserManagementAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7359216603274196641L;

	private Integer partnerId;
	
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("usermanagement.title"));
	}
	@Override
	public String execute(){
		UserDAO partnerDAO = DAOFactory.instance().getUserDAO();
		List<Country> countries = DAOFactory.instance().getCountryDAO().getCountriesOrderByName();
		List<CountryAndManager> countryAndManagers = new ArrayList<CountryAndManager>();
		for (Country country: countries){
			
			List<User> partners = partnerDAO.getPartnersByCountryAndByRoleType(country.getId(), UserRole.ROLE_COUNTRY_MANAGER);
			if (partners.size() > 0){
				User partner = partners.get(0);
				countryAndManagers.add(new CountryAndManager(country, partner, !SecurityContextContainer.checkAvailability(partner.getId())));
			}else {
				countryAndManagers.add(new CountryAndManager(country));
			}
		}
		getServletRequest().setAttribute("countryAndManagers" , countryAndManagers);
		getServletRequest().setAttribute("admins", DAOFactory.instance().getUserDAO().getPartnersByRoleType(UserRole.ROLE_ADMIN));
		return SUCCESS;
	}
	public String disableUser(){
		UserService.disableUser(partnerId);
		return SUCCESS;
	}
	public String enableUser(){
		UserService.enableUser(partnerId);
		return SUCCESS;
	}
	public String deleteCountryManager(){
		UserService.deleteCountryManager(partnerId);
		return SUCCESS;
	}
	public String changeToCountryManager(){
		LoginResult loginResult = SecurityService.loginAsCountryManager(this.partnerId);
		if (LoginResultType.NO_PARTNER.equals(loginResult.getType())){
			addActionMessage(getText("country.no.partner"));
			return INPUT;
		}else if (LoginResultType.ALREADY_IN_USE.equals(loginResult.getType())){
			addActionMessage(getText("user.already.inuse"));
			return INPUT;			
		}else if (LoginResultType.LOGGED_IN.equals(loginResult.getType())){
			return SUCCESS;			
		}else{
			addActionMessage(getText("user.access.denied"));
			return INPUT;			
		}
	}



	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	public static class CountryAndManager {
		private Country country;
		private User countryManager;
		private boolean inuse;
		public CountryAndManager(Country country) {
			super();
			this.country = country;
		}
		public CountryAndManager(Country country, User countryManager, boolean inuse) {
			super();
			this.country = country;
			this.countryManager = countryManager;
			this.inuse = inuse;
		}
		public Country getCountry() {
			return country;
		}
		public User getCountryManager() {
			return countryManager;
		}
		public boolean isInuse() {
			return inuse;
		}

		
	}
}
