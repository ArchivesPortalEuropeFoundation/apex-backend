package eu.apenet.dashboard.actions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.InsertNodeinLA;
import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.LangDAO;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Lang;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Jara Alvarez
 * 
 * To insert new alternative names for a country in the system
 * 
 * 		//      - Insert registers in couAlternativeNames entities
		//		- To change the AL.xml with a new translation

 */
public class CreateCountryAltNamesAction extends ActionSupport implements Preparable{

	private final Logger log = Logger.getLogger(getClass());
	
	private List<Lang> languagesList = new ArrayList<Lang>();
	private List<Country> countriesList = new ArrayList<Country>();
	private Integer languageSelected;
	private Integer countrySelected;
	private String altCountryName;
	private String pathRepo = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR;
	static Semaphore sem = new Semaphore(1,true) ;
	
	public List<Lang> getLanguagesList() {
		return languagesList;
	}

	public void setLanguagesList(List<Lang> languagesList) {
		this.languagesList = languagesList;
	}
	
	public Integer getLanguageSelected() {
		return languageSelected;
	}

	public void setLanguageSelected(Integer languageSelected) {
		this.languageSelected = languageSelected;
	}
	
	public String getAltCountryName() {
		return altCountryName;
	}

	public void setAltCountryName(String altCountryName) {
		this.altCountryName = altCountryName;
	}

	public List<Country> getCountriesList() {
		return countriesList;
	}

	public void setCountriesList(List<Country> countriesList) {
		this.countriesList = countriesList;
	}

	public Integer getCountrySelected() {
		return countrySelected;
	}

	public void setCountrySelected(Integer countrySelected) {
		this.countrySelected = countrySelected;
	}

	public String getPathRepo() {
		return pathRepo;
	}

	public void setPathRepo(String pathRepo) {
		this.pathRepo = pathRepo;
	}

	public void prepare() throws Exception{		
		log.debug("Filling in the languages list...");
		LangDAO langDao = DAOFactory.instance().getLangDAO();
		this.setLanguagesList(langDao.findAll());
		
		log.debug("Filling in the countries list...");
		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		this.setCountriesList(countryDAO.findAll());
	}
	
	public void validate(){
		
		log.debug("Validating textfields in creating alternative names country process...");
		
		if (this.getAltCountryName()!= null)
		{
			if (this.getAltCountryName().length() == 0)
				addFieldError("altCountryName", getText("CreateCountryAltNames.altCountryName"));
		}
	}

	public String execute() throws Exception{
		
		return SUCCESS;
	}

	public String insertAlternativeCountryNames(){
		
		String result = null;
		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		CouAlternativeNameDAO couAltNamesDAO = DAOFactory.instance().getCouAlternativeNameDAO();
		CouAlternativeName newCouAltName = new CouAlternativeName();
		
			
		if (this.getAltCountryName()!= null)
		{	
			Lang language = new Lang();
			LangDAO langDao = DAOFactory.instance().getLangDAO();
			language = langDao.findById(this.getLanguageSelected());
			Country country = countryDAO.findById(this.getCountrySelected());

			if (couAltNamesDAO.getAltNamebyCountryAndLang(country,language)==null)
			{
				newCouAltName.setCouAnName(this.getAltCountryName());
				newCouAltName.setLang(language);			
				newCouAltName.setCountry(country);
				
				try{
					couAltNamesDAO.store(newCouAltName);
					result = SUCCESS;
					addActionMessage(getText("CreateCountryAltNames.AlternativeNameStored"));
					insertNodeinAL("unittitle", country, this.languageSelected, this.getAltCountryName());				
					
				}catch(Exception e){
					result=ERROR;
					addActionMessage(getText("CreateCountryAltNames.AlternativeNameNotStored"));
				}
			}else
			{
				addActionMessage(getText("createCountryAltNames.AlternativeNameAlreadyStored"));
				result=INPUT;
			}
		}
		else
			result = INPUT;
		
		return result;
	}
	
	public synchronized void insertNodeinAL(String node, Country cou, int language, String altName)
	{
		LangDAO langDao = DAOFactory.instance().getLangDAO();
		String langIsoName = langDao.findById(language).getIsoname();
		
		log.debug("Inserting in the general AL the new country definition " + cou.getCname() + "...");
		new Thread( new InsertNodeinLA(this.sem, node,cou, langIsoName, altName)).start();
	}
}	
