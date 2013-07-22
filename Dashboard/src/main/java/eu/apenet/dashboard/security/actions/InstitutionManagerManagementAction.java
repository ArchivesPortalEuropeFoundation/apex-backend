package eu.apenet.dashboard.security.actions;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.SecurityService.LoginResult;
import eu.apenet.dashboard.security.SecurityService.LoginResult.LoginResultType;
import eu.apenet.dashboard.security.UserService;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;

public class InstitutionManagerManagementAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7836284338030276475L;
	private Integer countryId;
	private Integer aiId;
	private Integer partnerId;



	@Override
	public String execute() throws Exception {
		SecurityContext securityContext = SecurityContext.get();
		if  (securityContext.isCountryManager()){
			countryId = securityContext.getCountryId();
		}
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		
		// Get all the institutions belong to this list of partners. 
		// It must take only the institutions that are not group, so the second parameter is false.
		getServletRequest().setAttribute("ais" , aiDao.getArchivalInstitutionsByCountryId(countryId, false));
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
	public String deleteInstitutionManager(){
		UserService.deleteInstitutionManager(aiId, partnerId);
		return SUCCESS;
	}
	public String changeToInstitutionManager(){
		LoginResult loginResult = SecurityService.loginAsInstitutionManager(this.aiId);
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

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

}
