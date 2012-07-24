package eu.apenet.dashboard.manual;

import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import eu.apenet.dashboard.AbstractInstitutionAction;

/**
 * User: Eloy García & Paul
 * Date: Oct 19th, 2010
 *
 */

/*This class implements ActionSupport interface and deals with the actions within web form used to create a new EAG from scratch*/
public class ReadEAGAction extends AbstractInstitutionAction {

	//Constants
	private static final long serialVersionUID = 288694302373041585L;
	
    //Attributes
	private Date date;
	private String name;
	private String englishName;
	private String id;
	private String responsiblePersonSurname;
	private String responsiblePersonName;
	private String country;
	private String cityTown;
	private String postalCode;
	private String street;
	private String telephone;
	private String emailAddress;
	private String webPage;
	private String access;
	private String handicapped;
	private String workingPlaces;
	private String library;
	private String laboratory;
	private String reproduction;
	private String automation;
	private String archivalHoldings;
	private List<String> repositorguideInformation;
	private List<String> repositorguideURL;
	private List<String> repositorguideResource;
	private List<String> repositorguidePossibleHGTitle;
	private List<String> localHGSelected;
	//private String fax;
	//private String researchServices;
	//private String history;
	//private String archival;
	//private String rules;
	//private String publicAreas;
	private String currentAction;	//This variable stores the action from 
									//ReadEAGAction was called. It is useful 
									//for knowing where to come back, because 
									//ReadEAGAction can be called from 
									//different points within the 
									//application
    
    //Getters and Setters
	public Date getDate() {
		return date;
	}
	
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.vieweag"));
	}


	
	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResponsiblePersonSurname() {
		return responsiblePersonSurname;
	}

	public void setResponsiblePersonSurname(String responsiblePersonSurname) {
		this.responsiblePersonSurname = responsiblePersonSurname;
	}

	public String getResponsiblePersonName() {
		return responsiblePersonName;
	}

	public void setResponsiblePersonName(String responsiblePersonName) {
		this.responsiblePersonName = responsiblePersonName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCityTown() {
		return cityTown;
	}

	public void setCityTown(String cityTown) {
		this.cityTown = cityTown;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getWebPage() {
		return webPage;
	}

	public void setWebPage(String webPage) {
		this.webPage = webPage;
	}
	

	public void setAccess(String access) {
		this.access = access;
	}

	public String getAccess() {
		
		if (this.access.equals("yes")) {
			return "1";
		}
		else {
			return "2";
		}
		
	}
	
	public void setHandicapped(String handicapped) {
		this.handicapped = handicapped;
	}

	public String getHandicapped() {

		if (this.handicapped.equals("yes")) {
			return "1";
		}
		else {
			return "2";
		}
	}
	
	public void setWorkingPlaces(String workingPlaces) {
		this.workingPlaces = workingPlaces;
	}

	public String getWorkingPlaces() {
		return workingPlaces;
	}
	
	public void setLibrary(String library) {
		this.library = library;
	}

	public String getLibrary() {

		if (this.library.equals("yes")) {
			return "1";
		}
		else {
			return "2";
		}
	}
	
	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}

	public String getLaboratory() {

		if (this.laboratory.equals("yes")) {
			return "1";
		}
		else {
			return "2";
		}
	}
	
	public void setReproduction(String reproduction) {
		this.reproduction = reproduction;
	}

	public String getReproduction() {

		if (this.reproduction.equals("yes")) {
			return "1";
		}
		else {
			return "2";
		}
	}
	
	public void setAutomation(String automation) {
		this.automation = automation;
	}

	public String getAutomation() {

		if (this.automation.equals("yes")) {
			return "1";
		}
		else {
			return "2";
		}
	}
	
	public void setArchivalHoldings(String archivalHoldings) {
		this.archivalHoldings = archivalHoldings;
	}

	public String getArchivalHoldings() {
		return archivalHoldings;
	}
	
	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}

	public String getCurrentAction() {
		return currentAction;
	}
	
	public void setRepositorguideInformation(
			List<String> repositorguideInformation) {
		this.repositorguideInformation = repositorguideInformation;
	}

	public List<String> getRepositorguideInformation() {
		return repositorguideInformation;
	}
	
	public void setRepositorguideURL(List<String> repositorguideURL) {
		this.repositorguideURL = repositorguideURL;
	}

	public List<String> getRepositorguideURL() {
		return repositorguideURL;
	}
	
	public void setRepositorguideResource(List<String> repositorguideResource) {
		this.repositorguideResource = repositorguideResource;
	}

	public List<String> getRepositorguideResource() {
		return repositorguideResource;
	}

	public void setRepositorguidePossibleHGTitle(
			List<String> repositorguidePossibleHGTitle) {
		this.repositorguidePossibleHGTitle = repositorguidePossibleHGTitle;
	}

	public List<String> getRepositorguidePossibleHGTitle() {
		return repositorguidePossibleHGTitle;
	}
	
	public void setLocalHGSelected(List<String> localHGSelected) {
		this.localHGSelected = localHGSelected;
	}

	public List<String> getLocalHGSelected() {
		return localHGSelected;
	}

	
    //Constructor
    public ReadEAGAction(){
    }
	
    //Methods
	public String execute() throws Exception{

		//The ai_id is retrieved from the session
		//This code has been moved to the prepare method
		
		//The action from where ReadEAGAction was invoked is retrieved
		this.setCurrentAction(ServletActionContext.getActionMapping().getName());
		APEnetEAGDashboard eag = new APEnetEAGDashboard(this.getAiId(), null);
		this.name = eag.getName();
		this.englishName = eag.getEnglishName();
		this.id = eag.getId();
		this.responsiblePersonSurname = eag.getResponsiblePersonSurname();
		this.responsiblePersonName = eag.getResponsiblePersonName();
		this.country = eag.getCountry();
		this.cityTown = eag.getCityTown();
		this.postalCode = eag.getPostalCode();
		this.street = eag.getStreet();
		this.telephone = eag.getTelephone();
		this.emailAddress = eag.getEmailAddress();
		this.webPage = eag.getWebPage();
		this.access = eag.getAccess();
		this.handicapped = eag.getHandicapped();
		this.workingPlaces = eag.getWorkingPlaces();
		this.library = eag.getLibrary();
		this.laboratory = eag.getLaboratory();
		this.reproduction = eag.getReproduction();
		this.automation = eag.getAutomation();
		this.archivalHoldings = eag.getArchivalHoldings();
		this.repositorguideInformation = eag.getRepositorguideInformation();
		this.repositorguideURL = eag.getRepositorguideURL();
		this.repositorguideResource = eag.getRepositorguideResource();
		this.repositorguidePossibleHGTitle = eag.getRepositorguidePossibleHGTitle();
		this.localHGSelected = eag.getLocalHGSelected();
				
		eag = null;
		
		return SUCCESS;
	}

}