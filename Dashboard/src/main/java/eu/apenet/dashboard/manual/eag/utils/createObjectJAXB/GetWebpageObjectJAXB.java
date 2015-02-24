package eu.apenet.dashboard.manual.eag.utils.createObjectJAXB;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.ComputerPlaces;
import eu.apenet.dpt.utils.eag2012.DescriptiveNote;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Exhibition;
import eu.apenet.dpt.utils.eag2012.InternetAccess;
import eu.apenet.dpt.utils.eag2012.OtherServices;
import eu.apenet.dpt.utils.eag2012.P;
import eu.apenet.dpt.utils.eag2012.RecreationalServices;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Reproductionser;
import eu.apenet.dpt.utils.eag2012.Restorationlab;
import eu.apenet.dpt.utils.eag2012.Services;
import eu.apenet.dpt.utils.eag2012.ToursSessions;
import eu.apenet.dpt.utils.eag2012.Webpage;

/**
 * Class for fill elements of webpages element
 */
public class GetWebpageObjectJAXB extends AbstractObjectJAXB{
	/**
	 * EAG2012 internal object.
	 */
	protected Eag2012 eag2012;
	
	/**
	 * EAG2012 JAXB object.
	 */
	protected Eag eag;
	/**
	 * object Repository
	 */
	private Repository repository;
	private final Logger log = Logger.getLogger(getClass());
	boolean exhibitions;
	boolean tours;
	boolean otherServices;	
	private String sectionValueKey;
	private String sectionHrefKey;
	private String sectionLangKey;
	private List<String> webpageValueList;
	private List<String> webpageHrefList;
	private List<String> webpageLangList;

	/**
	 * Method to receives object Eag2012 and Eag for fill webpage element
	 * @param eag2012 {@link Eag2012} object Eag2012
	 * @param eag {@link Eag} object Eag
	 * @return {@link Eag} object Eag
	 */
	public Eag GetWebpageJAXB(Eag2012 eag2012, Eag eag) {
		this.eag2012=eag2012;
		this.eag=eag;	
		exhibitions = false;
		tours = false;
		otherServices = false;	
		boolean exhibitions = false;
		boolean tours = false;
		boolean otherServices = false;
		// eag/archguide/desc/repositories/repository/webpage
		if (this.eag2012.getWebpageValue() != null || this.eag2012.getWebpageHref() != null) {
			for (int i = 0; i < this.eag2012.getWebpageValue().size(); i++) {
				Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getWebpageValue().get(i);
				Map<String, Map<String, List<String>>> tabsHrefMap = (this.eag2012.getWebpageHref()!=null && this.eag2012.getWebpageHref().size()>i)?this.eag2012.getWebpageHref().get(i):null;
				Map<String, Map<String, List<String>>> tabsLangMap = (this.eag2012.getWebpageLang()!=null && this.eag2012.getWebpageLang().size()>i)?this.eag2012.getWebpageLang().get(i):null;
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
					Iterator<String> sectionValueIt = sectionValueMap.keySet().iterator();
					Iterator<String> sectionHrefIt = (sectionHrefMap!=null)?sectionHrefMap.keySet().iterator():null;
					Iterator<String> sectionLangIt = (sectionLangMap!=null)?sectionLangMap.keySet().iterator():null;
					while (sectionValueIt.hasNext()) {
						
					
						sectionValueKey = sectionValueIt.next();
						sectionHrefKey = sectionHrefIt.next();
						sectionLangKey = (sectionLangIt!=null && sectionLangIt.hasNext())?sectionLangIt.next():null;
						webpageValueList = sectionValueMap.get(sectionValueKey);
						webpageHrefList = (sectionHrefMap!=null)?sectionHrefMap.get(sectionHrefKey):null;
						webpageLangList = (sectionLangKey!=null)?sectionLangMap.get(sectionLangKey):null;
						parseWebpage(i);
							}
				}
			}
		}else{
			exhibitionToursAndOtherServicesDescriptiveNotes(exhibitions,tours,otherServices);
		}

			
		return this.eag;
	}

	/**
	 * Method to fill webpage element
	 * @param i {@link int}  i
	 * @param sectionValueKey {@link String} the sectionValueKey
	 * @param sectionHrefKey {@link String} the sectionHrefKey
	 * @param sectionLangKey {@link String} the sectionLangKey
	 * @param webpageValueList {@link List<String>} the webpageValueList
	 * @param webpageHrefList {@link List<String>} the webpageHrefList
	 * @param webpageLangList {@link List<String>} the webpageLangList
	 */
	private void parseWebpage(int i){

		
		for (int k = 0; k < webpageValueList.size() || (webpageHrefList!=null && webpageHrefList.size()>k); k++) {
			if (webpageValueList.get(k) == null || webpageValueList.get(k).isEmpty() && (webpageHrefList.get(k) == null || webpageHrefList.get(k).isEmpty())) {
				exhibitionToursAndOtherServicesDescriptiveNotes(exhibitions,tours,otherServices);
			}else{
				Webpage webpage= new Webpage();
				webpage.setContent(webpageValueList.get(k));
				String href = ((webpageHrefList.get(k)!=null && webpageHrefList.get(k).length()>0 && (webpageHrefList.get(k).toLowerCase().startsWith("http://") || webpageHrefList.get(k).toLowerCase().startsWith("ftp://") || webpageHrefList.get(k).toLowerCase().startsWith("https://") ))?webpageHrefList.get(k):(webpageHrefList.get(k)!=null && !webpageHrefList.get(k).isEmpty())?("http://"+webpageHrefList.get(k)):null);

				if (href != null && !href.isEmpty()) {
					webpage.setHref(href);
				}

				if (webpageLangList != null
						&& webpageLangList.size() > k
						&& webpageLangList.get(k) != null
						&& !webpageLangList.get(k).isEmpty()
						&& !Eag2012.OPTION_NONE.equalsIgnoreCase(webpageLangList.get(k))) {
					webpage.setLang(webpageLangList.get(k));
				}
				if (Eag2012.ROOT.equalsIgnoreCase(sectionValueKey) && 
						(Eag2012.ROOT.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
					boolean found = false;

					if (!repository.getWebpage().isEmpty()) {
						for (int l = 0; !found && l < repository.getWebpage().size(); l++) {
							Webpage web = repository.getWebpage().get(l);
							// Current lang.
							String currentLang = null;
							if (web.getContent()!=null && web.getLang() != null
									&& !web.getLang().isEmpty()) {
								currentLang = web.getLang();
							} 
							if ((web.getHref() != null && web.getHref().equalsIgnoreCase(webpage.getHref())
									|| (web.getHref() == null && (webpage.getHref() == null || webpage.getHref().isEmpty())))
								&& (web.getContent() != null && web.getContent().equalsIgnoreCase(webpage.getContent())
									|| (web.getContent() == null && (webpage.getContent() == null || webpage.getContent().isEmpty())))
								&& ((currentLang != null && currentLang.equalsIgnoreCase(webpage.getLang()))
									|| (currentLang == null && (webpage.getLang() == null || Eag2012.OPTION_NONE.equalsIgnoreCase(webpage.getLang()))))) {
								found = true;
							}
						}
					}

					if (!found) {
						repository.getWebpage().add(webpage);
					}
				}
				repository = createServices( repository,false, sectionValueKey, sectionHrefKey,null, webpage);	

				//TODO: changed implementation for exhibition, tours section and other services
				// eag/archguide/desc/repositories/repository/services/recreationalServices/exhibition
				if (Eag2012.EXHIBITION.equalsIgnoreCase(sectionValueKey) && 
						(Eag2012.EXHIBITION.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
					exhibitions = true;
					createRepositoryRecreationalservices("exhibition",i,k);

					Exhibition exhibition = repository.getServices().getRecreationalServices().getExhibition().get(k);
					getDescriptiveNote(exhibition, k, i);
					exhibition.setWebpage(webpage);
				}

				// eag/archguide/desc/repositories/repository/services/recreationalServices/toursSessions
				if (Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionValueKey) && 
						(Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
					tours = true;
					ToursSessions toursSessions = repository.getServices().getRecreationalServices().getToursSessions().get(k);
					getDescriptiveNote(toursSessions, k, i);
					toursSessions.setWebpage(webpage);
				}

				// eag/archguide/desc/repositories/repository/services/recreationalServices/otherServices
				if (Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionValueKey) && 
						(Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
					otherServices = true;
					createRepositoryRecreationalservices("other",i,k);
					OtherServices otherService = repository.getServices().getRecreationalServices().getOtherServices().get(k);
					getDescriptiveNote(otherService, k, i);
					otherService.setWebpage(webpage);
				}
			}
		}
		if(!exhibitions || !tours || !otherServices){
			exhibitionToursAndOtherServicesDescriptiveNotes(exhibitions,tours,otherServices);
		}
		exhibitions = tours = otherServices = false;
	
	}

	/**
	 * Method to fill Recreationalservices element 
	 * @param i {@linkint} the i
	 * @param k {@link int} the k
	 */
	private void createRepositoryRecreationalservices(String key,int i, int k){
		this.log.debug("Method start: \"createStreetLang\"");
		if (repository.getServices() == null) {
			repository.setServices(new Services());
		}
		if (repository.getServices().getRecreationalServices() == null) {
			repository.getServices().setRecreationalServices(new RecreationalServices());
		}
		if(key.equals("exhibition")){
			
			if (repository.getServices().getRecreationalServices().getExhibition().size() < (k + 1)) {
				int diff = (k + 1) -repository.getServices().getRecreationalServices().getExhibition().size();
				for (int l = 0; l < diff; l++) {
					repository.getServices().getRecreationalServices().getExhibition().add(new Exhibition());
					if ((l + 1) != diff) {
						int size = repository.getServices().getRecreationalServices().getExhibition().size();
						Exhibition exhibition = repository.getServices().getRecreationalServices().getExhibition().get(size-1);
						getDescriptiveNote(exhibition, (size - 1), i);
					}
				}
			}
		}else if(key.equals("tours")){
			if (repository.getServices().getRecreationalServices().getToursSessions().size() < (k + 1)) {
				int diff = (k + 1) -repository.getServices().getRecreationalServices().getToursSessions().size();
				for (int l = 0; l < diff; l++) {
					repository.getServices().getRecreationalServices().getToursSessions().add(new ToursSessions());
					if ((l + 1) != diff) {
						int size = repository.getServices().getRecreationalServices().getToursSessions().size();
						ToursSessions toursSessions = repository.getServices().getRecreationalServices().getToursSessions().get(size-1);
						getDescriptiveNote(toursSessions, (size - 1), i);
					}
				}
			}
		}else if(key.equals("other")){
			if (repository.getServices().getRecreationalServices().getOtherServices().size() < (k + 1)) {
				int diff = (k + 1) -repository.getServices().getRecreationalServices().getOtherServices().size();
				for (int l = 0; l < diff; l++) {
					repository.getServices().getRecreationalServices().getOtherServices().add(new OtherServices());
					if ((l + 1) != diff) {
						int size = repository.getServices().getRecreationalServices().getOtherServices().size();
						OtherServices otherService = repository.getServices().getRecreationalServices().getOtherServices().get(size-1);
						
						getDescriptiveNote(otherService, (size - 1), i);
					}
				}
			}
		}
		this.log.debug("End method: \"createQuestion\"");
	}

	/**
	 * Method to recover the elements "exhibition", "toursSessions" and
	 * "otherServices" for the element "recreationalServices".
	 *
	 * @param exhibitions boolean Indicating if exists exhibitions.
	 * @param tours boolean Indicating if exists tours or sessions.
	 * @param otherServices boolean Indicating if exists other services.
	 */
	private void exhibitionToursAndOtherServicesDescriptiveNotes(boolean exhibitions,boolean tours,boolean otherServices) {
		for(int i=0;i<this.eag.getArchguide().getDesc().getRepositories().getRepository().size();i++){
			Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
			if (this.eag2012.getDescriptiveNotePValue() != null && this.eag2012.getDescriptiveNotePLang() != null) {
				if (this.eag2012.getDescriptiveNotePValue().get(i) != null
						&& this.eag2012.getDescriptiveNotePLang().get(i) != null) {
					Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(i);
					Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(i);
					Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
					Iterator<String> tabsLangIt = tabsLangMap.keySet().iterator();
					while (tabsValueIt.hasNext()) {
						String tabValueKey = tabsValueIt.next();
						String tabLangKey = tabsLangIt.next();

						if (Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabValueKey) && Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabLangKey)) {
							Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
							Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
							Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
							Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
							while (sectionsValueIt.hasNext()) {
								String sectionValueKey = sectionsValueIt.next();
								String sectionLangKey = sectionsLangIt.next();
								List<String> valuesList = sectionsValueMap.get(sectionValueKey);
								for (int k = 0; k < valuesList.size(); k++) {
									if (valuesList.get(k) != null && !valuesList.get(k).isEmpty()) {
										// eag/archguide/desc/repositories/repository/services/recreationalServices/exhibition/descriptiveNote/P
										if (!exhibitions && Eag2012.EXHIBITION.equalsIgnoreCase(sectionValueKey) && Eag2012.EXHIBITION.equalsIgnoreCase(sectionLangKey)) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getRecreationalServices() == null) {
												repository.getServices().setRecreationalServices(new RecreationalServices());
											}
											if (repository.getServices().getRecreationalServices().getExhibition().size() < (k + 1)) {
												int diff = (k + 1) -repository.getServices().getRecreationalServices().getExhibition().size();
												for (int l = 0; l < diff; l++) {
													repository.getServices().getRecreationalServices().getExhibition().add(new Exhibition());
													if ((l + 1) != diff) {
														int size = repository.getServices().getRecreationalServices().getExhibition().size();
														Exhibition exhibition = repository.getServices().getRecreationalServices().getExhibition().get(size-1);
														getDescriptiveNote(exhibition, (size - 1), i);
													}
												}
											}

											Exhibition exhibition = repository.getServices().getRecreationalServices().getExhibition().get(k);
											getDescriptiveNote(exhibition, k, i);
										}
										// eag/archguide/desc/repositories/repository/services/recreationalServices/toursSessions/descriptiveNote/P
										if (!tours && Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionValueKey) && Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionLangKey)) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getRecreationalServices() == null) {
												repository.getServices().setRecreationalServices(new RecreationalServices());
											}
											if (repository.getServices().getRecreationalServices().getToursSessions().size() < (k + 1)) {
												int diff = (k + 1) -repository.getServices().getRecreationalServices().getToursSessions().size();
												for (int l = 0; l < diff; l++) {
													repository.getServices().getRecreationalServices().getToursSessions().add(new ToursSessions());
													if ((l + 1) != diff) {
														int size = repository.getServices().getRecreationalServices().getToursSessions().size();
														ToursSessions toursSessions = repository.getServices().getRecreationalServices().getToursSessions().get(size-1);
														getDescriptiveNote(toursSessions, (size - 1), i);
													}
												}
											}

											ToursSessions toursSessions = repository.getServices().getRecreationalServices().getToursSessions().get(k);
											getDescriptiveNote(toursSessions, k, i);
										}
										// eag/archguide/desc/repositories/repository/services/recreationalServices/otherServices/descriptiveNote/P
										if (!otherServices && Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionValueKey) && Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionLangKey)) {
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getRecreationalServices() == null) {
												repository.getServices().setRecreationalServices(new RecreationalServices());
											}
											if (repository.getServices().getRecreationalServices().getOtherServices().size() < (k + 1)) {
												int diff = (k + 1) -repository.getServices().getRecreationalServices().getOtherServices().size();
												for (int l = 0; l < diff; l++) {
													repository.getServices().getRecreationalServices().getOtherServices().add(new OtherServices());
													if ((l + 1) != diff) {
														int size = repository.getServices().getRecreationalServices().getOtherServices().size();
														OtherServices otherService = repository.getServices().getRecreationalServices().getOtherServices().get(size-1);
														getDescriptiveNote(otherService, (size - 1), i);
													}
												}
											}
	
											OtherServices otherService = repository.getServices().getRecreationalServices().getOtherServices().get(k);
											getDescriptiveNote(otherService, k, i);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Method to recover descriptiveNote.
	 *
	 * @param object The object to add the descriptive note.
	 */
	private void getDescriptiveNote(final Object object, final int index, final int repoIndex) {
		if (this.eag2012.getDescriptiveNotePValue() != null
				&& this.eag2012.getDescriptiveNotePLang() != null) {
			if (this.eag2012.getDescriptiveNotePValue().get(repoIndex) != null
					&& this.eag2012.getDescriptiveNotePLang().get(repoIndex) != null) {
				Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(repoIndex);
				Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(repoIndex);
				Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> tabsLangIt = tabsLangMap.keySet().iterator();
				while (tabsValueIt.hasNext()) {
					String tabValueKey = tabsValueIt.next();
					String tabLangKey = tabsLangIt.next();

					if (Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabValueKey)
							&& Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabLangKey)) {
						Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
						Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
						Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
						Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
						while (sectionsValueIt.hasNext()) {
							String sectionValueKey = sectionsValueIt.next();
							String sectionLangKey = sectionsLangIt.next();
							List<String> valuesList = sectionsValueMap.get(sectionValueKey);
							List<String> langList = sectionsLangMap.get(sectionLangKey);
								if (index>-1 && valuesList!=null && valuesList.size() > index && !(valuesList.get(index) == null || valuesList.get(index).isEmpty())) {
									P p = new P();
									p.setContent(valuesList.get(index));
									if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(index))) {
										p.setLang(langList.get(index));
									}

									// eag/archguide/desc/repositories/repository/services/recreationalServices/exhibition/descriptiveNote/P
									if (Eag2012.EXHIBITION.equalsIgnoreCase(sectionValueKey)
											&& Eag2012.EXHIBITION.equalsIgnoreCase(sectionLangKey)) {
										if (object instanceof Exhibition) {
											Exhibition exhibition = (Exhibition) object;
											if (exhibition.getDescriptiveNote() == null) {
												exhibition.setDescriptiveNote(new DescriptiveNote());
												exhibition.getDescriptiveNote().getP().add(p);
											}
										}
									}
									// eag/archguide/desc/repositories/repository/services/recreationalServices/toursSessions/descriptiveNote/P
									if (Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionValueKey)
											&& Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionLangKey)) {
										if (object instanceof ToursSessions) {
											ToursSessions toursSessions = (ToursSessions) object;
											if (toursSessions.getDescriptiveNote() == null) {
												toursSessions.setDescriptiveNote(new DescriptiveNote());
												toursSessions.getDescriptiveNote().getP().add(p);
											}
										}
									}
									// eag/archguide/desc/repositories/repository/services/recreationalServices/otherServices/descriptiveNote/P
									if (Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionValueKey)
											&& Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionLangKey)) {
										if (object instanceof OtherServices) {
											OtherServices otherServices = (OtherServices) object;
											if (otherServices.getDescriptiveNote() == null) {
												otherServices.setDescriptiveNote(new DescriptiveNote());
												otherServices.getDescriptiveNote().getP().add(p);
											}
										}
									}
								}else if(index==-1){
									for(int x=0;x<valuesList.size();x++){
										P p = new P();
										p.setContent(valuesList.get(x));
										if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(x))) {
											p.setLang(langList.get(x));
										}

										if (p.getContent() != null && !p.getContent().isEmpty()) {
											// eag/archguide/desc/repositories/repository/services/searchroom/computerPlaces/descriptiveNote/P
											if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.SEARCHROOM.equalsIgnoreCase(sectionLangKey)) {
												if (object instanceof ComputerPlaces) {
													ComputerPlaces computerPlaces = (ComputerPlaces) object;
													if (computerPlaces.getDescriptiveNote() == null) {
														computerPlaces.setDescriptiveNote(new DescriptiveNote());
													}
													computerPlaces.getDescriptiveNote().getP().add(p);
												}
											}
											// eag/archguide/desc/repositories/repository/services/internetAccess/descriptiveNote/P
											if (Eag2012.INTERNET_ACCESS.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.INTERNET_ACCESS.equalsIgnoreCase(sectionLangKey)) {
												if (object instanceof InternetAccess) {
														InternetAccess internetAccess = (InternetAccess) object;
														if (internetAccess.getDescriptiveNote() == null) {
															internetAccess.setDescriptiveNote(new DescriptiveNote());
														}
														internetAccess.getDescriptiveNote().getP().add(p);
												}
											}
											// eag/archguide/desc/repositories/repository/services/techservices/restorationlab/descriptiveNote/P
											if (Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionLangKey)) {
												if (object instanceof Restorationlab) {
													Restorationlab restorationlab = (Restorationlab) object;
													if (restorationlab.getDescriptiveNote() == null) {
														restorationlab.setDescriptiveNote(new DescriptiveNote());
													}
													restorationlab.getDescriptiveNote().getP().add(p);
												}
											}
											// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/descriptiveNote/P
											if (Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionLangKey)) {
												if (object instanceof Reproductionser) {
													Reproductionser reproductionser = (Reproductionser) object;
													if (reproductionser.getDescriptiveNote() == null) {
														reproductionser.setDescriptiveNote(new DescriptiveNote());
													}
													reproductionser.getDescriptiveNote().getP().add(p);
												}
											}
										}
									}
								}
							}
					}
				}
			}
		}
	}
	
	
}
