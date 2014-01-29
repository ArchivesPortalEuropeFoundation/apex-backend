package eu.apenet.dashboard.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import eu.apenet.dashboard.Breadcrumb;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.archivallandscape.ChangeAlIdentifiers;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Changing the identifiers of the archival landscape items
 *
 *  1- Change in DDBB archival_institution entity
 *
 * @author jara
 */

@SuppressWarnings("serial")
public class ChangeAlIdentifiersAction extends ActionSupport implements Preparable {

	private List<ArchivalInstitution> institutionList = new ArrayList<ArchivalInstitution>();	
	private Integer institutionSelected;
	private final Logger log = Logger.getLogger(getClass());
	private Country country = new Country();
	private String identifier;
	private String identifierOld;
	private List<Breadcrumb> breadcrumbRoute;
	
	public List<Breadcrumb> getBreadcrumbRoute(){
		return this.breadcrumbRoute;
	}

	private void buildBreadcrumb() {
		this.breadcrumbRoute = new ArrayList<Breadcrumb>();
		Breadcrumb breadcrumb = new Breadcrumb(null,getText("breadcrumb.section.changeAIidentifier"));
		this.breadcrumbRoute.add(breadcrumb);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<ArchivalInstitution> getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(List<ArchivalInstitution> institutionList) {
		this.institutionList = institutionList;
	}

	public Integer getInstitutionSelected() {
		return institutionSelected;
	}

	public void setInstitutionSelected(Integer institutionSelected) {
		this.institutionSelected = institutionSelected;
	}

	public String getIdentifierOld() {
		return identifierOld;
	}

	public void setIdentifierOld(String identifierOld) {
		this.identifierOld = identifierOld;
	}

	public void validate(){

		log.debug("Validating textfields in changing AL identifiers process...");

		if (this.getIdentifier()!= null)
		{
			if (this.getIdentifier().length() == 0)
				addFieldError("identifier", "Identifier required");
		}
	}

	public String execute()   {
		buildBreadcrumb();
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {

		CountryDAO countryDao = DAOFactory.instance().getCountryDAO();
		this.setCountry(countryDao.findById(SecurityContext.get().getCountryId()));
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();

		this.setInstitutionList(aiDao.getGroupsAndArchivalInstitutionsByCountryId(this.getCountry().getId(),"alorder", true ));
	}

	public String storeIdentifier(){

		String result = null;
		ChangeAlIdentifiers cAlId = new ChangeAlIdentifiers();

		if ((this.getIdentifier()!= null))
		{
			try{
				JpaUtil.beginDatabaseTransaction();
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
				ArchivalInstitution ai = new ArchivalInstitution();

				ai = aiDao.findById(this.getInstitutionSelected());
				this.setIdentifierOld(ai.getInternalAlId());
				if (!(this.getIdentifier().trim().equals(this.getIdentifierOld().trim())))
				{
					//Checking of the unique identifiers
					
					String available = cAlId.checkIdentifierAvailability(this.getInstitutionList(), this.getIdentifier(), ai);
					if (!(available.equals("success")))
					{
						addActionMessage(getText("al.message.changeIdentifier.alreadyUsed"));
						result= INPUT;
					}else if(ArchivalLandscape.isValidIdentifier(this.getIdentifier()))
					{
						String ddbbChanged = cAlId.changeIdentifierinDDBB(ai, this.getIdentifier());
						if (!ddbbChanged.equals("success"))
						{
							addActionMessage(ai.getAiname() + ":   " + getText("al.message.changeIdentifier.error"));
							result = ERROR;
						}
						else
						{
							result = SUCCESS;
							addActionMessage(getText("al.message.changeIdentifier.identifierChanged"));
							JpaUtil.commitDatabaseTransaction();
							JpaUtil.closeDatabaseSession();
						}
					}else{
					   //The identifier doesn't begin with a letter
						addActionMessage(getText("al.message.changeIdentifier.errorIdentifier"));
						result = ERROR;
					}
				}
				else
				{
					addActionMessage(getText("al.message.changeIdentifier.identifierEqual"));
					result = INPUT;
				}

			}catch(Exception e){
				log.error(e.getMessage());
				addActionMessage(getText("al.message.changeIdentifier.error"));
				result = ERROR;
			}finally{
				if (result.equals(ERROR)){
					try{
						log.debug("Rollbacking the changing AL identifiers process");

						JpaUtil.rollbackDatabaseTransaction();
						JpaUtil.closeDatabaseSession();
					}
					catch(Exception e){
						log.error("Error in rollbacking the changing archival landscape identifiers process. Please review manually.");
						log.error(e.getMessage());
						log.error(e.getStackTrace());
					}
				}
			}

		}else
			result = INPUT;

		return result;
	}
}
