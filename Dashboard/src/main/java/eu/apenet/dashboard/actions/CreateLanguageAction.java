package eu.apenet.dashboard.actions;

import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Lang;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
/**
 * @author Jara Alvarez
 * 
 * To create a new language in the system
 */
public class CreateLanguageAction extends ActionSupport {

	private final Logger log = Logger.getLogger(getClass());
	
	private String  englishlanguageName;
	private String  isoLanguageName;
	private String  iso2LanguageName;
	private String  nativeLanguageName;

	public String getEnglishlanguageName() {
		return englishlanguageName;
	}


	public void setEnglishlanguageName(String englishlanguageName) {
		this.englishlanguageName = englishlanguageName;
	}


	public String getIsoLanguageName() {
		return isoLanguageName;
	}


	public void setIsoLanguageName(String isoLanguageName) {
		this.isoLanguageName = isoLanguageName;
	}

	public String getIso2LanguageName() {
		return iso2LanguageName;
	}


	public void setIso2LanguageName(String iso2LanguageName) {
		this.iso2LanguageName = iso2LanguageName;
	}


	public String getNativeLanguageName() {
		return nativeLanguageName;
	}


	public void setNativeLanguageName(String nativeLanguageName) {
		this.nativeLanguageName = nativeLanguageName;
	}
	
	public void validate(){
		
		log.debug("Validating textfields in creating language process...");
		
		if (this.getEnglishlanguageName()!= null)
		{
			if (this.getEnglishlanguageName().length() == 0)
				addFieldError("englishCountryName", getText("createLanguage.englishCountryName"));
		}
		if (this.getIsoLanguageName()!= null)
		{
			if (this.getIsoLanguageName().length() != 3)
				addFieldError("isoCountryName", getText("createLanguage.isoCountryName"));
		}
		
		if (this.getIso2LanguageName() != null)
		{
			if (this.getIso2LanguageName().length() != 2)
				addFieldError("lang", getText("createLanguage.lang"));
		}		
		if (this.getNativeLanguageName()!= null)
		{
			if (this.getNativeLanguageName().length() == 0)
				addFieldError("altCountryName", getText("createLanguage.altCountryName"));
		}
	}

	public String execute() throws Exception{
		return SUCCESS;
	}
	
	public String storeLanguage(){
		String result = null;
		LangDAO langDao = DAOFactory.instance().getLangDAO();	
		
		if ((this.getEnglishlanguageName()!= null) && (this.getIsoLanguageName()!= null) && (this.getIso2LanguageName() != null) && (this.getNativeLanguageName()!= null))
		{		
			try{
				if ((langDao.getLangByIso2Name(this.getIso2LanguageName().trim())== null) && (langDao.getLangByIsoname(this.getIsoLanguageName().trim())== null))
				{							
					Lang language = new Lang();
					
					language.setLname(this.getEnglishlanguageName().toUpperCase().trim());
					language.setIsoname(this.getIsoLanguageName().toUpperCase().trim());
					language.setIso2name(this.getIso2LanguageName().toUpperCase().trim());
					language.setLnativename(this.getNativeLanguageName().toUpperCase().trim());				
					langDao.store(language);
	
					result = SUCCESS;
					addActionMessage(getText("createLanguage.languageStored"));
				}else
				{
					addActionMessage(getText("createLanguage.languageAlreadyStored"));
					result=INPUT;
				}
				
			}catch(Exception e)
			{
				result = ERROR;	
				addActionMessage(getText("createLanguage.languageNotStored"));
			}		
			
		}else
			result = INPUT;
		
		return result;
	}


}	

	

	


