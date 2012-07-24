package eu.apenet.dashboard.manual;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

import eu.apenet.dashboard.AbstractInstitutionAction;

/**
 * User: Eloy García
 * Date: Oct 19th, 2010
 *
 */


public class WebFormUploadEAGAction extends AbstractInstitutionAction{

	//Constants
	private static final long serialVersionUID = -9062442129295069037L;

    //Attributes
	private Logger log = Logger.getLogger(getClass());
	private String currentAction;	//This variable stores the action from 
									//ReadEAGAction was called. It is useful 
									//for knowing where to come back, because 
									//ReadEAGAction can be called from 
									//different points within the 
									//application
    private String name;
	private String englishName;
	private String id;
	//private String responsiblePersonSurname;
	//private String responsiblePersonName;
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
	//private String fax;
	//private String researchServices;
	//private String history;
	//private String archival;
	//private String rules;
	//private String publicAreas;
    
    //Getters and Setters
	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}

	public String getCurrentAction() {
		return currentAction;
	}
	
	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
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

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	//@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	//public String getResponsiblePersonSurname() {
		//return responsiblePersonSurname;
	//}

	//public void setResponsiblePersonSurname(String responsiblePersonSurname) {
		//this.responsiblePersonSurname = responsiblePersonSurname;
	//}

	//@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	//public String getResponsiblePersonName() {
		//return responsiblePersonName;
	//}

	//public void setResponsiblePersonName(String responsiblePersonName) {
		//this.responsiblePersonName = responsiblePersonName;
	//}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getCityTown() {
		return cityTown;
	}

	public void setCityTown(String cityTown) {
		this.cityTown = cityTown;
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getWebPage() {
		return webPage;
	}

	public void setWebPage(String webPage) {
		this.webPage = webPage;
	}
	
	public void setAccess(String access) {
		
		if (access.equals("1")) {

			this.access = "yes";
		}
		else {

			this.access = "no";
		}
		
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getAccess() {
		return access;
	}

	public void setHandicapped(String handicapped) {

		if (handicapped.equals("1")) {

			this.handicapped = "yes";	
		}
		else {

			this.handicapped = "no";
		}
		
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getHandicapped() {
		return handicapped;
	}

	public void setWorkingPlaces(String workingPlaces) {
		this.workingPlaces = workingPlaces;
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getWorkingPlaces() {
		return workingPlaces;
	}
	
	public void setLibrary(String library) {
		
		if (library.equals("1")) {

			this.library = "yes";	
		}
		else {

			this.library = "no";
		}
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getLibrary() {
		return library;
	}
	
	public void setLaboratory(String laboratory) {
		
		if (laboratory.equals("1")) {

			this.laboratory = "yes";	
		}
		else {

			this.laboratory = "no";
		}	
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getLaboratory() {
		return laboratory;
	}
	
	public void setReproduction(String reproduction) {

		if (reproduction.equals("1")) {

			this.reproduction = "yes";	
		}
		else {

			this.reproduction = "no";
		}	
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getReproduction() {
		return reproduction;
	}
	
	public void setAutomation(String automation) {

		if (automation.equals("1")) {

			this.automation = "yes";	
		}
		else {

			this.automation = "no";
		}	
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getAutomation() {
		return automation;
	}
	
	public void setArchivalHoldings(String archivalHoldings) {
		this.archivalHoldings = archivalHoldings;
	}

	@RequiredStringValidator(message = "This field is mandatory", key = "label.eag.field.mandatory", trim = true)
	public String getArchivalHoldings() {
		return archivalHoldings;
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
	
    //Constructor
    public WebFormUploadEAGAction(){
    }
	
    //Methods
	public String execute() throws Exception{

    	return SUCCESS;
	}
	
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.uploadeag"));
	}
	
	
	//This method retrieve all the mandatory fields from the web form and create 
	//or replace the EAG file for an Archival Institution
	public String uploadEAG() throws Exception {
		APEnetEAGDashboard eag = new APEnetEAGDashboard(this.getAiId(), null);
		eag.setName(this.getName());
		eag.setEnglishName(this.getEnglishName());
		eag.setId(this.getId());
		//eag.setResponsiblePersonSurname(this.getResponsiblePersonSurname());
		//eag.setResponsiblePersonName(this.getResponsiblePersonName());
		eag.setCountry(this.getCountry());
		eag.setCityTown(this.getCityTown());
		eag.setPostalCode(this.getPostalCode());
		eag.setStreet(this.getStreet());
		eag.setTelephone(this.getTelephone());
		eag.setEmailAddress(this.getEmailAddress());
		eag.setWebPage(this.getWebPage());
		eag.setAccess(this.getAccess());
		eag.setHandicapped(this.getHandicapped());
		eag.setWorkingPlaces(this.getWorkingPlaces());
		eag.setLibrary(this.getLibrary());
		eag.setLaboratory(this.getLaboratory());
		eag.setReproduction(this.getReproduction());
		eag.setAutomation(this.getAutomation());
		eag.setArchivalHoldings(this.getArchivalHoldings());
		eag.setRepositorguideInformation(this.repositorguideInformation);
		eag.setRepositorguideResource(this.repositorguideResource);
		eag.setRepositorguideURL(this.repositorguideURL);
		eag.setRepositorguidePossibleHGTitle(this.repositorguidePossibleHGTitle);

        log.info("Preparing to save EAG from form: " + eag.toString());

		String result = eag.saveEAGModified();
		eag = null;

		if (result.equals("error")){
			//Some errors occurred during the EAG creation process
			//The EAG could't be properly created
			addActionMessage(getText("label.eag.creationwitherror"));
			addActionMessage(getText("label.eag.notcreated"));
			return ERROR;
		}
		else if (result.equals("error_eagalreadyuploaded")){
			//The values of the eadid or the autform are in the System
			//The EAG could't be properly created
			addActionMessage(getText("label.eag.creationwitherror"));
			addActionMessage(getText("label.eag.uploadingerror.eadidautformrepeated"));			
			addActionMessage(getText("label.eag.notcreated"));
			return ERROR;
		}
		else if (result.equals("correct_withoutRepositorguideInformation")){
			//Some information in repositorguide tags is empty, so it will be filled with text by default
			addActionMessage(getText("label.eag.repositorguideInformation.empty") + getText("label.ai.hg.information.content.default"));
			addActionMessage(getText("label.eag.eagcorrectlycreated"));
			return SUCCESS;						
		}
		else{
			addActionMessage(getText("label.eag.eagcorrectlycreated"));
			return SUCCESS;			
		}
		
	}
}