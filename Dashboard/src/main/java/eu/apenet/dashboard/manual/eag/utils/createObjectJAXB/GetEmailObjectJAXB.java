package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Email;
import eu.apenet.dpt.utils.eag2012.Repository;

/**
 * Class for fill elements of email element
 */
public class GetEmailObjectJAXB extends AbstractObjectJAXB{
	/**
	 * EAG2012 {@link Eag2012} internal object.
	 */
	protected Eag2012 eag2012;	
	/**
	 * Eag {@link Eag} JAXB object.
	 */
	protected Eag eag;
	/**
	 * repository {@link Repository}.
	 */
	private Repository repository;
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * Method GetEmailJAXB for fill the elements of Email element
	 * @param eag2012 {@link Eag2012} object Eag2012
	 * @param eag {@link Eag} object Eag
	 * @return {@link Eag} object Eag
	 */
	public Eag GetEmailJAXB(Eag2012 eag2012, Eag eag) {
		this.log.debug("Method start: \"GetEmailJAXB\"");
		// TODO Auto-generated method stub
		this.eag2012=eag2012;
		this.eag=eag;		
		main();
		this.log.debug("End method: \"GetEmailJAXB\"");
		return this.eag;
	}

	/**
	 * Method main of class GetEmailObjectJAXB for fill elements of all mail object JAXB
	 */
	private void main(){
		this.log.debug("Method start: \"Main of class GetEmailObjectJAXB\"");
			if (this.eag2012.getEmailValue() != null || this.eag2012.getEmailHref() != null) {
				for (int i = 0; i < this.eag2012.getEmailValue().size(); i++) {
					Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getEmailValue().get(i);
					Map<String, Map<String, List<String>>> tabsHrefMap = (this.eag2012.getEmailHref()!=null && this.eag2012.getEmailHref().size()>i)?this.eag2012.getEmailHref().get(i):null;
					Map<String, Map<String, List<String>>> tabsLangMap = (this.eag2012.getEmailLang()!=null && this.eag2012.getEmailLang().size()>i)?this.eag2012.getEmailLang().get(i):null;
					// Repository
					repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
					Iterator<String> tabsHrefIt = (tabsHrefMap!=null)?tabsHrefMap.keySet().iterator():null;
					Iterator<String> tabsLangIt = (tabsLangMap!=null)?tabsLangMap.keySet().iterator():null;
					while (tabsValueIt.hasNext()) {
						String tabValueKey = tabsValueIt.next();
						String tabHrefKey = (tabsHrefIt!=null && tabsHrefIt.hasNext())?tabsHrefIt.next():null;
						String tabLangKey = (tabsLangIt!=null && tabsLangIt.hasNext())?tabsLangIt.next():null;
						Map<String, List<String>> sectionValueMap = tabsValueMap.get(tabValueKey);
						Map<String, List<String>> sectionHrefMap = (tabsHrefMap!=null)?tabsHrefMap.get(tabHrefKey):null;
						Map<String, List<String>> sectionLangMap = (tabLangKey!=null)?tabsLangMap.get(tabLangKey):null;
						Iterator<String> sectionValueIt = (sectionValueMap!=null)?sectionValueMap.keySet().iterator():null;
						Iterator<String> sectionHrefIt = (sectionHrefMap!=null)?sectionHrefMap.keySet().iterator():null;
						Iterator<String> sectionLangIt = (sectionLangMap!=null)?sectionLangMap.keySet().iterator():null;
						while (sectionValueIt.hasNext()) {
							String sectionValueKey = sectionValueIt.next();
							String sectionHrefKey = (sectionHrefIt!=null && sectionHrefIt.hasNext())?sectionHrefIt.next():null;
							String sectionLangKey = (sectionLangIt!=null && sectionLangIt.hasNext())?sectionLangIt.next():null;
							List<String> emailValueList = sectionValueMap.get(sectionValueKey);
							List<String> emailHrefList = (sectionHrefMap!=null)?sectionHrefMap.get(sectionHrefKey):null;
							List<String> emailLangList = (sectionLangMap!=null)?sectionLangMap.get(sectionLangKey):null;
							createEmail(emailValueList, emailHrefList, emailLangList,sectionValueKey,sectionHrefKey,sectionLangKey);						
					}
				}
			}
		}
		this.log.debug("End method: \"Main of class GetEmailObjectJAXB\"");
	}

	/**
	 * Method to fill email element of repository
	 * @param emailValueList {@link List<String>} emailValueList
	 * @param emailHrefList {@link List<String>} emailHrefList
	 * @param emailLangList {@link List<String>} emailLangList
	 * @param sectionValueKey {@link String} String sectionValueKey
	 * @param sectionHrefKey {@link String} String sectionHrefKey
	 * @param sectionLangKey {@link String} String sectionLangKey
	*/
	private void createEmail(List<String> emailValueList,List<String> emailHrefList,List<String> emailLangList,String sectionValueKey,String sectionHrefKey,String sectionLangKey){
		this.log.debug("Method start: \"createEmail\"");
		// eag/archguide/desc/repositories/repository/email (href & lang)
		for (int k = 0; k < emailValueList.size() || (emailHrefList!=null && k < emailHrefList.size()); k++) {
			if ((emailValueList.get(k) != null
					&& !emailValueList.get(k).isEmpty()) 
					|| (emailHrefList.get(k) != null 
						&& !emailHrefList.get(k).isEmpty())) {
		Email email = new Email();
		email.setContent(emailValueList.get(k));
		email.setHref((emailHrefList!=null && emailHrefList.size()>k)?emailHrefList.get(k):null);
		if (emailLangList != null
				&& emailLangList.size() > k
				&& emailLangList.get(k) != null
				&& !emailLangList.get(k).isEmpty()
				&& !Eag2012.OPTION_NONE.equalsIgnoreCase(emailLangList.get(k))) {
			email.setLang(emailLangList.get(k));
		}
		if (Eag2012.ROOT.equalsIgnoreCase(sectionValueKey)
				&& Eag2012.ROOT.equalsIgnoreCase(sectionHrefKey)) {
			boolean found = false;
			if (!repository.getEmail().isEmpty()) {
				for (int l = 0; !found && l < repository.getEmail().size(); l++) {
					Email emailCheck = repository.getEmail().get(l);
					// Current lang.
					String currentLang = null;
					if (emailCheck.getContent()!=null && emailCheck.getLang() != null
							&& !emailCheck.getLang().isEmpty()) {
						currentLang = emailCheck.getLang();
					} 
					if ((emailCheck.getHref() != null && emailCheck.getHref().equalsIgnoreCase(email.getHref())
							|| (emailCheck.getHref() == null && email.getHref().isEmpty()))
						&& (emailCheck.getContent() != null && emailCheck.getContent().equalsIgnoreCase(email.getContent())
							|| (emailCheck.getContent() == null && email.getContent().isEmpty()))
						&& ((currentLang != null && currentLang.equalsIgnoreCase(email.getLang()))
							|| (currentLang == null && (email.getLang() == null || Eag2012.OPTION_NONE.equalsIgnoreCase(email.getLang()))))) {
						found = true;
					}
				}
			}
			if (!found) {
				repository.getEmail().add(email);
			}
		}		
		// eag/archguide/desc/repositories/repository/services/searchroom
		// eag/archguide/desc/repositories/repository/services/library
		// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
		repository = createServices( repository,true, sectionValueKey, sectionHrefKey, email, null);		
			}			
		}
		this.log.debug("End method: \"createEmail\"");
	}
				
}
