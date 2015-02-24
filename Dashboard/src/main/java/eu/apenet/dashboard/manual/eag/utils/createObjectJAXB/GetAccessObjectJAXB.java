package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.Access;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Restaccess;
import eu.apenet.dpt.utils.eag2012.TermsOfUse;

/**
 * Class for fill elements of access element
 */
public class GetAccessObjectJAXB {
	/**
	 * EAG2012 internal object.
	 */
	protected Eag2012 eag2012;	
	/**
	 * EAG2012  {@link Eag2012} JAXB object.
	 */
	protected Eag eag;
	/**
	 * object {@link Repository} Repository
	 */
	private Repository repository;
	private final Logger log = Logger.getLogger(getClass());

	/**
	 * Class GetAccessJAXB for fill the elements of access element
	 * @param eag2012 object {@link Eag2012} Eag2012
	 * @param eag object {@link Eag} Eag
	 * @return object {@link Eag} Eag
	 */
	public Eag GetAccessJAXB(Eag2012 eag2012, Eag eag) {
		// TODO Auto-generated method stub
		this.eag2012=eag2012;
		this.eag=eag;		
		createQuestion();
		createRestaccess();
		createTermsOfUse();
		return this.eag;
	}

	/**
	 * Method to fill question element 
	 */
	private void createQuestion(){
		this.log.debug("Method start: \"createQuestion\"");
		// eag/archguide/desc/repositories/repository/access/question
			if (this.eag2012.getAccessQuestion() != null) {
				for (int i = 0; i < this.eag2012.getAccessQuestion().size(); i++) {
					Map<String, String> tabsQuestionMap = this.eag2012.getAccessQuestion().get(i);
					 repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					if (repository.getAccess() == null) {
						repository.setAccess(new Access());
					}
					Iterator<String> questionValueIt = tabsQuestionMap.keySet().iterator();
					while (questionValueIt.hasNext()) {
						String key = questionValueIt.next();
						repository.getAccess().setQuestion(tabsQuestionMap.get(key));
					}
				}
			}
			this.log.debug("End method: \"createQuestion\"");
	}

	/**
	 * Method to fill restaccess element 
	 */
	private void createRestaccess(){
		this.log.debug("Method start: \"createRestaccess\"");
		// eag/archguide/desc/repositories/repository/access/restaccess
			if (this.eag2012.getRestaccessValue() != null) {
				for (int i = 0; i < this.eag2012.getRestaccessValue().size(); i++) {
					Map<String, List<String>> tabsValueMap = this.eag2012.getRestaccessValue().get(i);
					Map<String, List<String>> tabsLangMap = null;
					if (this.eag2012.getRestaccessLang() != null && !this.eag2012.getRestaccessLang().isEmpty()) {
						tabsLangMap = this.eag2012.getRestaccessLang().get(i);
					}
					// Repository.
					repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					Iterator<String> accessValueIt = tabsValueMap.keySet().iterator();
					Iterator<String> accessLangIt = null;
					if (tabsLangMap != null) {
						accessLangIt = tabsLangMap.keySet().iterator();
					}
					while (accessValueIt.hasNext()) {
						String accessValueKey = accessValueIt.next();
						// Rest of tabs.
//						if (!accessValueKey.equalsIgnoreCase(Eag2012.TAB_YOUR_INSTITUTION)) {
							List<String> accessValueList = tabsValueMap.get(accessValueKey);
							List<String> accessLangList = null;
							if (accessLangIt != null) {
								accessLangList = tabsLangMap.get(accessLangIt.next());
							}
							for (int j = 0; j < accessValueList.size(); j++) {
								boolean exit = false;
								if (accessValueList.get(j) == null || accessValueList.get(j).isEmpty()) {
									exit = true;
								}
								if(!exit){
									for(int k=0;k<repository.getAccess().getRestaccess().size();k++){
										if(accessValueList.get(j).equalsIgnoreCase(repository.getAccess().getRestaccess().get(k).getContent())){
											exit = true;
										}
									}
									if(!exit){
										if (accessValueList.get(j) != null
												&& !accessValueList.get(j).isEmpty()) {
											Restaccess restaccess = new Restaccess();
											restaccess.setContent(accessValueList.get(j));
											if (accessLangList != null
													&& !Eag2012.OPTION_NONE.equalsIgnoreCase(accessLangList.get(j))) {
												restaccess.setLang(accessLangList.get(j));
											}
											repository.getAccess().getRestaccess().add(restaccess);
										}
									}
								}
							}
//						}
					}
				}
			}
			this.log.debug("End method: \"createRestaccess\"");
	}

	/**
	 * Method to fill termsOfUse element 
	 */
	private void createTermsOfUse(){
		this.log.debug("Method start: \"createTermsOfUse\"");
		// eag/archguide/desc/repositories/repository/access/termsOfUse
			if (this.eag2012.getTermsOfUseValue() != null && !this.eag2012.getTermsOfUseValue().isEmpty()
					&& this.eag2012.getTermsOfUseLang() != null && !this.eag2012.getTermsOfUseLang().isEmpty()
					&& this.eag2012.getTermsOfUseHref() != null && !this.eag2012.getTermsOfUseHref().isEmpty()) {
				for (int i = 0; i < this.eag2012.getTermsOfUseValue().size(); i++) {
					List<String> termsOfUseValueList = this.eag2012.getTermsOfUseValue().get(i);
					List<String> termsOfUseLangList = this.eag2012.getTermsOfUseLang().get(i);
					List<String> termsOfUseHrefList = this.eag2012.getTermsOfUseHref().get(i);
					// Repository.
					repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					for (int j = 0; j < termsOfUseValueList.size(); j++) {
						if ((termsOfUseValueList.get(j) == null || termsOfUseValueList.get(j).isEmpty()) && (termsOfUseHrefList.get(j)==null || termsOfUseHrefList.get(j).isEmpty() )) {
							break;
						}
						TermsOfUse termsOfUse = new TermsOfUse();
						termsOfUse.setContent(termsOfUseValueList.get(j));
						if (!Eag2012.OPTION_NONE.equalsIgnoreCase(termsOfUseLangList.get(j))) {
							termsOfUse.setLang(termsOfUseLangList.get(j));
						}
						termsOfUse.setHref(termsOfUseHrefList.get(j));
						repository.getAccess().getTermsOfUse().add(termsOfUse);
					}
				}
			}
			this.log.debug("End method: \"createTermsOfUse\"");
	}
}
