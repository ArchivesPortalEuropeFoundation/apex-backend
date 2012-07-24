package eu.apenet.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.StrutsResourceBundleSource;
import eu.apenet.commons.infraestructure.APEnetEAG;
import eu.apenet.dashboard.security.SecurityContext;


/**
 * View Archival Landscape Action
 * User: Eloy García
 * Date: May 25, 2011
 * This class is in charge of displaying the  
 * APEnet Archival Landscape structure (only for one country) in order to 
 * allow the user to browse through this structure and even display the EAG 
 * files related to every archival institution
 */
public class ViewALAction extends ActionSupport {

	//Attributes
	
	private static final long serialVersionUID = -846464987187314333L;
	private Logger log = Logger.getLogger(getClass());
	private Integer couId;			// Identifier for the country which belongs to the partner
	private String navTreeLang;		// Language for the archival landscape tree
	//EAG data
	private String date="";
	private String name="";
	private String englishName="";
	private String id;
	private String responsiblePersonSurname="";
	private String responsiblePersonName="";
	private String country="";
	private String cityTown="";
	private String postalCode="";
	private String street="";
	private String telephone="";
	private String emailAddress="";
	private String webPage="";
	private Integer ai_id;
	private String pathEAG="";
	private String buildinginfo="";
	private String fax="";
	private String organization="";
	private String description="";
	private String firstdem=""; //Region data
    private String secondem=""; //Region data
	private String localentity="";
	private String opening="";
	private String openingNum="";
	private String closing="";
	private String access=""; //Access data
	private String restaccess=""; //Access data
	private String accesibility="";
	private String readingRoom="";
	private String repositoryguide="";
	private String repositoryguideLink="";
	private String library="";
	private String restorationlab="";
	private String reproductionser="";
	private String photocopyser="";
	private String microformser="";
	private String photographser="";
	private String digitalser="";
	private String automation="";
	private String autusermanag="";
	private String autdescription="";
	private String indexvoc="";
	private String odautomationP="";
	private String repositorhistP="";
	private String rule="";
	private String firstnameResp="";
	private String surnamesResp="";
	private String chargeResp="";
	private String adminunit="";
	private String building="";
	private String repositorareaNum="";
	private String lengthshelfNum="";
	private String extentNum="";
	private String notes ="";
	private String dateCreated ="";
	private String dateUpdated ="";
	
	private List<Breadcrumb> breadcrumbRoute;
	
	public List<Breadcrumb> getBreadcrumbRoute(){
		return this.breadcrumbRoute;
	}
	
	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getExtentNum() {
		return extentNum;
	}

	public void setExtentNum(String extentNum) {
		this.extentNum = extentNum;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRepositorareaNum() {
		return repositorareaNum;
	}

	public void setRepositorareaNum(String repositorareaNum) {
		this.repositorareaNum = repositorareaNum;
	}

	public String getLengthshelfNum() {
		return lengthshelfNum;
	}

	public void setLengthshelfNum(String lengthshelfNum) {
		this.lengthshelfNum = lengthshelfNum;
	}

	public String getAdminunit() {
		return adminunit;
	}

	public void setAdminunit(String adminunit) {
		this.adminunit = adminunit;
	}

	public String getFirstnameResp() {
		return firstnameResp;
	}

	public void setFirstnameResp(String firstnameResp) {
		this.firstnameResp = firstnameResp;
	}

	public String getSurnamesResp() {
		return surnamesResp;
	}

	public void setSurnamesResp(String surnamesResp) {
		this.surnamesResp = surnamesResp;
	}

	public String getChargeResp() {
		return chargeResp;
	}

	public void setChargeResp(String chargeResp) {
		this.chargeResp = chargeResp;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	
	
	public String getRepositorhistP() {
		return repositorhistP;
	}

	public void setRepositorhistP(String repositorhistP) {
		this.repositorhistP = repositorhistP;
	}

	public String getAutomation() {
		return automation;
	}

	public void setAutomation(String automation) {
		this.automation = automation;
	}

	public String getAutusermanag() {
		return autusermanag;
	}

	public void setAutusermanag(String autusermanag) {
		this.autusermanag = autusermanag;
	}

	public String getAutdescription() {
		return autdescription;
	}

	public void setAutdescription(String autdescription) {
		this.autdescription = autdescription;
	}

	public String getIndexvoc() {
		return indexvoc;
	}

	public void setIndexvoc(String indexvoc) {
		this.indexvoc = indexvoc;
	}

	public String getOdautomationP() {
		return odautomationP;
	}

	public void setOdautomationP(String odautomationP) {
		this.odautomationP = odautomationP;
	}

	public String getPhotocopyser() {
		return photocopyser;
	}

	public void setPhotocopyser(String photocopyser) {
		this.photocopyser = photocopyser;
	}

	public String getMicroformser() {
		return microformser;
	}

	public void setMicroformser(String microformser) {
		this.microformser = microformser;
	}

	public String getPhotographser() {
		return photographser;
	}

	public void setPhotographser(String photographser) {
		this.photographser = photographser;
	}

	public String getDigitalser() {
		return digitalser;
	}

	public void setDigitalser(String digitalser) {
		this.digitalser = digitalser;
	}

	public String getReproductionser() {
		return reproductionser;
	}

	public void setReproductionser(String reproductionser) {
		this.reproductionser = reproductionser;
	}

	public String getRestorationlab() {
		return restorationlab;
	}

	public void setRestorationlab(String restorationlab) {
		this.restorationlab = restorationlab;
	}

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public String getRepositoryguide() {
		return repositoryguide;
	}

	public void setRepositoryguide(String repositoryguide) {
		this.repositoryguide = repositoryguide;
	}

	public String getRepositoryguideLink() {
		return repositoryguideLink;
	}

	public void setRepositoryguideLink(String repositoryguideLink) {
		this.repositoryguideLink = repositoryguideLink;
	}

	public String getReadingRoom() {
		return readingRoom;
	}

	public void setReadingRoom(String readingRoom) {
		this.readingRoom = readingRoom;
	}

	public String getAccesibility() {
		return accesibility;
	}

	public void setAccesibility(String accesibility) {
		this.accesibility = accesibility;
	}

	public String getClosing() {
		return closing;
	}

	public String getRestaccess() {
		return restaccess;
	}

	public void setRestaccess(String restaccess) {
		this.restaccess = restaccess;
	}

	public void setClosing(String closing) {
		this.closing = closing;
	}

	public String getOpening() {
		return opening;
	}

	public void setOpening(String opening) {
		this.opening = opening;
	}

	public String getOpeningNum() {
		return openingNum;
	}

	public void setOpeningNum(String openingNum) {
		this.openingNum = openingNum;
	}

	public String getLocalentity() {
		return localentity;
	}

	public void setLocalentity(String localentity) {
		this.localentity = localentity;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFirstdem() {
		return firstdem;
	}

	public void setFirstdem(String firstdem) {
		this.firstdem = firstdem;
	}

	public String getSecondem() {
		return secondem;
	}

	public void setSecondem(String secondem) {
		this.secondem = secondem;
	}

	public String getPathEAG() {
		return pathEAG;
	}

	public void setPathEAG(String pathEAG) {
		this.pathEAG = pathEAG;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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

	public Integer getAi_id() {
		return ai_id;
	}

	public void setAi_id(Integer ai_id) {
		this.ai_id = ai_id;
	}
	public String getBuildinginfo() {
		return buildinginfo;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public void setBuildinginfo(String buildinginfo) {
		this.buildinginfo = buildinginfo;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	//Getters and Setters
	public void setCouId(Integer couId) {
		this.couId = couId;
	}

	public Integer getCouId() {
		return couId;
	}
	
	public void setNavTreeLang(String navTreeLang) {
		this.navTreeLang = navTreeLang;
	}

	public String getNavTreeLang() {
		return navTreeLang;
	}
	
	
	//Validator
	@Override
	public void validate() {
		super.validate();
		buildBreadcrumb();
	}

	private void buildBreadcrumb() {
		this.breadcrumbRoute = new ArrayList<Breadcrumb>();
		Breadcrumb breadcrumb = new Breadcrumb("index.action",getText("breadcrumb.section.dashboard"));
		this.breadcrumbRoute.add(breadcrumb);
		breadcrumb = new Breadcrumb(null,getText("breadcrumb.section.previewal"));
		this.breadcrumbRoute.add(breadcrumb);
	}
	
	//Constructor
	
	//Methods
	public String execute() throws Exception{

		String result = null;
		
		try {
			
			// Obtaining couId from Partner
			this.couId = SecurityContext.get().getCountryId();
			
			
			result = SUCCESS;
		}
		catch (Exception e){

			log.error("An exception occurred during the previewing of the Archival Landscape for country: " + this.getCouId() + ". Exception:" + e.getMessage());
			result = ERROR;
		}
    	return result;
	}
	
	public String showEAG(){
		APEnetEAG eag = new APEnetEAG(new StrutsResourceBundleSource(), this.ai_id, null);
		
		//Mandatory elements
		//Contact Details
		this.pathEAG = eag.getEagPath();
		if (this.pathEAG.equals("error"))
			this.pathEAG ="";
		this.name = eag.getName();
		if (this.name.equals("error"))
			this.name="";
		this.englishName = eag.getEnglishName();
		if (this.englishName.equals("error"))
			this.englishName= "";
		this.id = eag.getId();
		if (this.id.equals("error"))
			this.id = "";

		this.country = eag.getCountry();
		if (this.country.equals("error"))
			this.country="";
		this.cityTown = eag.getCityTown();
		if (this.cityTown.equals("error"))
			this.cityTown ="";
		this.postalCode = eag.getPostalCode();
		if (this.postalCode.equals("error"))
			this.postalCode="";
		this.street = eag.getStreet();
		if (this.street.equals("error"))
			this.street="";
		this.telephone = eag.getTelephone();
		if (this.telephone.equals("error"))
			this.telephone = "";
		this.emailAddress = eag.getEmailAddress();
		if (this.emailAddress.equals("error"))
			this.emailAddress ="";
		this.webPage = eag.getWebPage();
		if (this.webPage.equals("error"))
			this.webPage="";
		else if (!this.webPage.contains("http"))
		this.webPage = "http://" + this.webPage;
		
		
		this.access = eag.getAccess();
		if (this.access.equals("error"))
			this.access="";
		
		this.accesibility = eag.getAccesibility();
		if (this.accesibility.equals("error"))
			this.accesibility="";
		
		this.readingRoom = eag.getReadingRoom();
		if (this.readingRoom.equals("error"))
			this.readingRoom="";		
		
		this.library = eag.getLibrary();
		if (this.library.equals("error"))
			this.library = "";
		
		this.restorationlab = eag.getRestorationlab();
		if (this.restorationlab.equals("error"))
			this.restorationlab ="";
		
		this.reproductionser = eag.getReproductionser();
		if (this.reproductionser.equals("error"))
			this.reproductionser ="";
		
		this.automation=eag.getAutomation();
		if (this.automation.equals("error"))
			this.automation="";	
		
		this.extentNum=eag.getExtentNum();
		if (this.extentNum.equals("error"))
			this.extentNum="";
		
		this.dateCreated=eag.getDateCreated();
		if (this.dateCreated!=null)
		{
			if (this.dateCreated.equals("error"))
				this.dateCreated="";
		}		
		this.dateUpdated=eag.getDateUpdated();
		if (this.dateUpdated!=null)		{
			
			if (this.dateUpdated.equals("error"))
				this.dateUpdated="";
		}
	
		return SUCCESS;
		
	}

	
}