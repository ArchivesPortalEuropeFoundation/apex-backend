package eu.apenet.dashboard.manual.eag.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dpt.utils.eag2012.*;

/**
 * Class for fill EAG2012 JAXB object.
 */
public class CreateEAG2012 {
	/**
	 * EAG2012 internal object.
	 */
	protected Eag2012 eag2012;

	/**
	 * EAG2012 JAXB object.
	 */
	protected Eag eag;

	/**
	 * Constructor.
	 */
	public CreateEAG2012(final Eag2012 eag2012, final Eag eag) {
		super();
		this.eag2012 = eag2012;
		if (eag == null) {
			this.eag = new Eag();
		} else {
			this.eag = eag;
		}
	}

	/**
	 * Input method.
	 *
	 * @return The EAG2012 JAXB object.
	 */
	public Eag fillEAG2012() {
		// Constructs elements.
		constructAllParentElements();
		// Fill "Control" element.
		fillControl();
		// Fill "Archguide" element.
		fillArchguide();
		// Fill "Relations" element.
		fillRelations();

		return this.eag;
	}

	/**
	 * Method to construct all parent elements.
	 */
	private void constructAllParentElements() {
		// Fill EAG attibutes.
		this.eag.setAudience(Eag2012.XML_AUDIENCE);

		// Cosntruct "control".
		if (this.eag.getControl() == null) {
			this.eag.setControl(new Control());
		} else {
			MaintenanceStatus maintenanceStatus = this.eag.getControl().getMaintenanceStatus();
			MaintenanceHistory maintenanceHistory = this.eag.getControl().getMaintenanceHistory();

			this.eag.setControl(new Control());
			this.eag.getControl().setMaintenanceStatus(maintenanceStatus);
			this.eag.getControl().setMaintenanceHistory(maintenanceHistory);
		}

		// Constructs "archguide".
		this.eag.setArchguide(new Archguide());

		// Constructs "relations".
		this.eag.setRelations(new Relations());
	}

	/**
	 * Method to fill "Control" element.
	 */
	private void fillControl() {
		// eag/control/recordId
		if (this.eag.getControl().getRecordId() == null) {
			this.eag.getControl().setRecordId(new RecordId());
		}
		this.eag.getControl().getRecordId().setValue(this.eag2012.getRecordIdValue());
		// eag/control/otherRecordId
		if (this.eag2012.getOtherRecordIdValue() != null) {
			for (int i = 0; i < this.eag2012.getOtherRecordIdValue().size(); i++) {
				if (this.eag2012.getOtherRecordIdValue().get(i) != null
						&& !this.eag2012.getOtherRecordIdValue().get(i).isEmpty()) {
					OtherRecordId otherRecordId = new OtherRecordId();
					otherRecordId.setValue(this.eag2012.getOtherRecordIdValue().get(i));
					otherRecordId.setLocalType(this.eag2012.getOtherRecordIdLocalType().get(i));
					this.eag.getControl().getOtherRecordId().add(otherRecordId);
				}
			}
		}

		// eag/control/maintenanceAgency
		if (this.eag.getControl().getMaintenanceAgency() == null) {
			this.eag.getControl().setMaintenanceAgency(new MaintenanceAgency());
		}

		// eag/control/maintenanceAgency/agencyCode
		if (this.eag.getControl().getMaintenanceAgency().getAgencyCode() == null) {
			this.eag.getControl().getMaintenanceAgency().setAgencyCode(new AgencyCode());
		}
		if (this.eag2012.getAgencyCodeValue() != null && !this.eag2012.getAgencyCodeValue().isEmpty()) {
			this.eag.getControl().getMaintenanceAgency().getAgencyCode().setContent(this.eag2012.getAgencyCodeValue());
		} else {
			this.eag.getControl().getMaintenanceAgency().getAgencyCode().setContent(this.eag2012.getRecordIdValue());
		}

		// eag/control/maintenanceAgency/agencyName
		if (this.eag.getControl().getMaintenanceAgency().getAgencyName() == null) {
			this.eag.getControl().getMaintenanceAgency().setAgencyName(new AgencyName());
		}
		if (this.eag2012.getAgencyNameValue() != null && !this.eag2012.getAgencyNameValue().isEmpty()) {
			this.eag.getControl().getMaintenanceAgency().getAgencyName().setContent(this.eag2012.getAgencyNameValue());
			if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getAgencyNameLang())) {
				this.eag.getControl().getMaintenanceAgency().getAgencyName().setLang(this.eag2012.getAgencyNameLang());
			}
		} else {
			this.eag.getControl().getMaintenanceAgency().getAgencyName().setContent(this.eag2012.getAutformValue().get(0));
			if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getAutformLang().get(0))) {
				this.eag.getControl().getMaintenanceAgency().getAgencyName().setLang(this.eag2012.getAutformLang().get(0));
			}
		}

		// eag/control/maintenanceAgency/otherAgencyCode
		if (this.eag2012.getOtherAgencyCodeValue() != null && !this.eag2012.getOtherAgencyCodeValue().isEmpty()) {
			for (int i = 0; i < this.eag2012.getOtherRecordIdValue().size(); i++) {
				OtherAgencyCode otherAgencyCode = new OtherAgencyCode();
				otherAgencyCode.setContent(this.eag2012.getOtherAgencyCodeValue().get(i));
// TODO:				otherAgencyCode.setLocalType(this.eag2012.getOtherAgencyCodeLocalType().get(i));
				this.eag.getControl().getMaintenanceAgency().getOtherAgencyCode().add(otherAgencyCode);
			}
		}

		// eag/control/maintenanceAgency/descriptiveNote
// TODO:

		// eag/control/maintenanceStatus
		if (this.eag.getControl().getMaintenanceStatus() == null) {
			MaintenanceStatus maintenanceStatus = new MaintenanceStatus();
			maintenanceStatus.setValue(Eag2012.EVENTTYPE_NEW);
			this.eag.getControl().setMaintenanceStatus(maintenanceStatus);
		} else {
			this.eag.getControl().getMaintenanceStatus().setValue(Eag2012.EVENTTYPE_REVISED);
		}

		// eag/control/maintenanceHistory
		boolean exists = true;
		if (this.eag.getControl().getMaintenanceHistory() == null) {
			this.eag.getControl().setMaintenanceHistory(new MaintenanceHistory());
			exists = false;
		}

		// eag/control/maintenanceHistory/maintenanceEvent
		MaintenanceEvent maintenanceEvent = new MaintenanceEvent();

		// eag/control/maintenanceHistory/maintenanceEvent/agent && /agentType
		if (maintenanceEvent.getAgent() == null) {
			maintenanceEvent.setAgent(new Agent());
		}
		if (maintenanceEvent.getAgentType() == null) {
			maintenanceEvent.setAgentType(new AgentType());
		}
		if (this.eag2012.getAgentValue() != null && !this.eag2012.getAgentValue().isEmpty()) {
			maintenanceEvent.getAgent().setContent(this.eag2012.getAgentValue());
			maintenanceEvent.getAgentType().setValue(Eag2012.AGENT_TYPE_HUMAN);
		} else {
			maintenanceEvent.getAgent().setContent(Eag2012.AGENT_AUTOMATICALLY);
			maintenanceEvent.getAgentType().setValue(Eag2012.AGENT_TYPE_MACHINE);
		}
		if (this.eag2012.getAgentLang() != null 
				&& !Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getAgentLang())) {
			maintenanceEvent.getAgent().setLang(this.eag2012.getAgentLang());
		}

		// eag/control/maintenanceHistory/maintenanceEvent/eventDateTime
		if (maintenanceEvent.getEventDateTime() == null) {
			maintenanceEvent.setEventDateTime(new EventDateTime());
		}
		SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern(Eag2012.DATE_FORMAT);
		maintenanceEvent.getEventDateTime().setStandardDateTime(df.format(new GregorianCalendar().getTime()));
		df.applyPattern(Eag2012.DATE_FORMAT_HUMAN);
		maintenanceEvent.getEventDateTime().setContent(df.format(new GregorianCalendar().getTime()));

		// eag/control/maintenanceHistory/maintenanceEvent/eventType
		if (maintenanceEvent.getEventType() == null) {
			maintenanceEvent.setEventType(new EventType());
		}
		if (exists) {
			maintenanceEvent.getEventType().setValue(Eag2012.EVENTTYPE_REVISED);
		} else {
			maintenanceEvent.getEventType().setValue(Eag2012.EVENTTYPE_CREATED);
		}

		// MaintenanceHistory
		this.eag.getControl().getMaintenanceHistory().getMaintenanceEvent().add(maintenanceEvent);

		// eag/control/languageDeclarations
		if (this.eag2012.getLanguageLanguageCode() != null
				&& !this.eag2012.getLanguageLanguageCode().isEmpty()
				&& this.eag2012.getScriptScriptCode() != null
				&& !this.eag2012.getScriptScriptCode().isEmpty()) {
			for (int i = 0; i < this.eag2012.getLanguageLanguageCode().size(); i++) {
				LanguageDeclaration languageDeclaration = new LanguageDeclaration();

				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getLanguageLanguageCode().get(i))) {
					Language language = new Language();
					language.setLanguageCode(this.eag2012.getLanguageLanguageCode().get(i));

					languageDeclaration.setLanguage(language);
				}

				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getScriptScriptCode().get(i))) {
					Script script = new Script();
					script.setScriptCode(this.eag2012.getScriptScriptCode().get(i));

					languageDeclaration.setScript(script);
				}

				if (languageDeclaration.getLanguage() != null
						|| languageDeclaration.getScript() != null) {
					if (this.eag.getControl().getLanguageDeclarations() == null) {
						this.eag.getControl().setLanguageDeclarations(new LanguageDeclarations());
					}
					this.eag.getControl().getLanguageDeclarations().getLanguageDeclaration().add(languageDeclaration);
				}
			}
		}

		// eag/control/conventionDeclaration/citation
		if (this.eag2012.getCitationValue() != null && this.eag2012.getAbbreviationValue() != null) {
			for (int i = 0; i < this.eag2012.getCitationValue().size(); i++) {
				Map<String, List<String>> tabsMap = this.eag2012.getCitationValue().get(i);
				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while(tabsIt.hasNext()) {
					String tabsKey = tabsIt.next();
					List<String> valueList = tabsMap.get(tabsKey);
					if (Eag2012.TAB_CONTROL.equalsIgnoreCase(tabsKey)) {
						for (int j = 0; j < valueList.size(); j++) {
							Citation citation = new Citation();
							citation.setContent(valueList.get(j));

							ConventionDeclaration conventionDeclaration = new ConventionDeclaration();
							conventionDeclaration.getCitation().add(citation);

							// eag/control/conventionDeclaration/abbreviation
							if (this.eag2012.getAbbreviationValue()!=null && this.eag2012.getAbbreviationValue().size()>j && this.eag2012.getAbbreviationValue().get(j) != null && !this.eag2012.getAbbreviationValue().get(j).isEmpty()) {
								Abbreviation abbreviation = new Abbreviation();
								abbreviation.setContent(this.eag2012.getAbbreviationValue().get(j));

								conventionDeclaration.setAbbreviation(abbreviation);
							}
							if(citation.getContent()!=null /*&& !citation.getContent().isEmpty()*/){
								if (!citation.getContent().isEmpty()) {
									this.eag.getControl().getConventionDeclaration().add(conventionDeclaration);
								} else if (conventionDeclaration.getAbbreviation() != null){
									if (!conventionDeclaration.getCitation().isEmpty()) {
										for (int k = 0; k < conventionDeclaration.getCitation().size(); k++) {
											conventionDeclaration.getCitation().remove(k);
										}
									}
									this.eag.getControl().getConventionDeclaration().add(conventionDeclaration);
								}
							}
						}
					}
				}
			}
		}

		// eag/control/localControl/term
		Term term = new Term();
        df.applyPattern(Eag2012.DATE_FORMAT);
        term.setLastDateTimeVerified(df.format(new GregorianCalendar().getTime()));
		term.setScriptCode(Eag2012.CONTROL_SCRIPT_COD);
// TODO:		term.setTransliteration(Eag2012.CONTROL_TRANSLITERATION);
		term.setVocabularySource(Eag2012.CONTROL_VOCABULARY_SOURCE);
		term.setValue(Eag2012.EXTENDED);

		// eag/control/localControl
		LocalControl localControl = new LocalControl();
		localControl.setLocalType(Eag2012.LOCAL_TYPE_CONTROL);
		localControl.getTerm().add(term);

		this.eag.getControl().getLocalControl().add(localControl);

		// eag/control/localTypeDeclaration

		// eag/control/publicationStatus

		// eag/control/sources

	}

	/**
	 * Method to fill "Archguide" element.
	 */
	private void fillArchguide() {
		// Fill "Identity" element.
		if (this.eag.getArchguide().getIdentity() == null) {
			this.eag.getArchguide().setIdentity(new Identity());
		}
		fillIdentity();
		// Fill "Desc" element.
		if (this.eag.getArchguide().getDesc() == null) {
			this.eag.getArchguide().setDesc(new Desc());
		}
		fillDesc();
	}

	/**
	 * Method to fill "Relations" element.
	 */
	private void fillRelations() {
		// eag/relations/resourceRelation/relationEntry
 		if (this.eag2012.getRelationEntryValue() != null) {
			for (int i = 0; i < this.eag2012.getRelationEntryValue().size(); i++) {
				Map<String, List<String>> sectionValueMap = this.eag2012.getRelationEntryValue().get(i);
				Map<String, List<String>> sectionLangMap = this.eag2012.getRelationEntryLang().get(i);
				Iterator<String> sectionValueIt = sectionValueMap.keySet().iterator();
				Iterator<String> sectionLangIt = sectionLangMap.keySet().iterator();
				while (sectionValueIt.hasNext()) {
					String sectionValueKey = sectionValueIt.next();
					String sectionLangKey = (sectionLangIt.hasNext())?sectionLangIt.next():null;
					List<String> valueList = sectionValueMap.get(sectionValueKey);
					List<String> langList = (sectionLangKey!=null)?sectionLangMap.get(sectionLangKey):null;
					if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionValueKey)
							|| Eag2012.TAB_YOUR_INSTITUTION.equalsIgnoreCase(sectionValueKey)) {
						for (int j = 0; j < valueList.size(); j++) {
							ResourceRelation resourceRelation = new ResourceRelation();

							// eag/relations/resourceRelation/resourceRelationType
							if(Eag2012.TAB_YOUR_INSTITUTION.equalsIgnoreCase(sectionValueKey) /*&& (resourceRelation.getResourceRelationType()==null || resourceRelation.getResourceRelationType().isEmpty())*/){
								resourceRelation.setResourceRelationType(Eag2012.OPTION_CREATOR_TEXT); //default text creator, it's mandatory and into tab-your_institution there is not a combobox for it
							}else if (this.eag2012.getResourceRelationResourceRelationType() != null && !this.eag2012.getResourceRelationResourceRelationType().isEmpty()) {
								if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionValueKey)){
									if (Eag2012.OPTION_CREATOR.equalsIgnoreCase(this.eag2012.getResourceRelationResourceRelationType().get(j))) {
										resourceRelation.setResourceRelationType(Eag2012.OPTION_CREATOR_TEXT);
									}
									if (Eag2012.OPTION_SUBJECT.equalsIgnoreCase(this.eag2012.getResourceRelationResourceRelationType().get(j))) {
										resourceRelation.setResourceRelationType(Eag2012.OPTION_SUBJECT_TEXT);
									}
									if (Eag2012.OPTION_OTHER.equalsIgnoreCase(this.eag2012.getResourceRelationResourceRelationType().get(j))) {
										resourceRelation.setResourceRelationType(Eag2012.OPTION_OTHER_TEXT);
									}
									
									// eag/relations/resourceRelation/href
									if (this.eag2012.getResourceRelationHref() != null && this.eag2012.getResourceRelationHref().get(Eag2012.TAB_RELATION)!=null && !this.eag2012.getResourceRelationHref().get(Eag2012.TAB_RELATION).isEmpty()) {
										String href = this.eag2012.getResourceRelationHref().get(Eag2012.TAB_RELATION).get(j);
									//	href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?href:((href!=null && !href.isEmpty())?((href!=null && !href.isEmpty())?("http://"+href):href):null);
										resourceRelation.setHref(href);
										if (resourceRelation.getRelationEntry() == null) {
											resourceRelation.setRelationEntry(new RelationEntry());
										}
									}
								}
							}
							if(Eag2012.TAB_YOUR_INSTITUTION.equalsIgnoreCase(sectionValueKey)){
								if(this.eag2012.getResourceRelationHref() != null && this.eag2012.getResourceRelationHref().get(Eag2012.TAB_YOUR_INSTITUTION)!=null && !this.eag2012.getResourceRelationHref().get(Eag2012.TAB_YOUR_INSTITUTION).isEmpty()){
									String href = this.eag2012.getResourceRelationHref().get(Eag2012.TAB_YOUR_INSTITUTION).get(j);
								//	href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?href:((href!=null && !href.isEmpty())?((href!=null && !href.isEmpty())?("http://"+href):href):null);
									resourceRelation.setHref(href);
									if (resourceRelation.getRelationEntry() == null) {
										resourceRelation.setRelationEntry(new RelationEntry());
									}
								}
							}else if(Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionValueKey)){
								if(this.eag2012.getResourceRelationHref() != null && this.eag2012.getResourceRelationHref().get(Eag2012.RESOURCE_RELATION)!=null && !this.eag2012.getResourceRelationHref().get(Eag2012.RESOURCE_RELATION).isEmpty()){
									String href = this.eag2012.getResourceRelationHref().get(Eag2012.RESOURCE_RELATION).get(j);
								//	href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?((href!=null && !href.isEmpty())?("http://"+href):null):null;
									resourceRelation.setHref(href);
									if (resourceRelation.getRelationEntry() == null) {
										resourceRelation.setRelationEntry(new RelationEntry());
									}
								}
							}

							RelationEntry relationEntry = new RelationEntry();
							relationEntry.setContent(valueList.get(j));
							relationEntry.setLang((langList!=null && langList.size()>j && !langList.get(j).equalsIgnoreCase(Eag2012.OPTION_NONE) )?langList.get(j):null);

							// eag/relations/resourceRelation/descriptiveNote/P
							if (this.eag2012.getDescriptiveNotePValue() != null
									&& this.eag2012.getDescriptiveNotePLang() != null) {
								for (int k = 0; k < this.eag2012.getDescriptiveNotePValue().size(); k++) {
									Map<String, Map<String, List<String>>> tabsPValueMap = this.eag2012.getDescriptiveNotePValue().get(k);
									Map<String, Map<String, List<String>>> tabsPLangMap = this.eag2012.getDescriptiveNotePLang().get(k);
									Iterator<String> tabsPValueIt = tabsPValueMap.keySet().iterator();
									Iterator<String> tabsPLangIt = tabsPLangMap.keySet().iterator();
									while (tabsPValueIt.hasNext()) {
										String tabPValueKey = tabsPValueIt.next();
										String tabPLangKey = tabsPLangIt.next();

										if (Eag2012.TAB_RELATION.equalsIgnoreCase(tabPValueKey)
												&& Eag2012.TAB_RELATION.equalsIgnoreCase(tabPLangKey)) {
											Map<String, List<String>> sectionsPValueMap = tabsPValueMap.get(tabPValueKey);
											Map<String, List<String>> sectionsPLangMap = tabsPLangMap.get(tabPLangKey);
											Iterator<String> sectionsPValueIt = sectionsPValueMap.keySet().iterator();
											Iterator<String> sectionsPLangIt = sectionsPLangMap.keySet().iterator();
											while (sectionsPValueIt.hasNext()) {
												String sectionPValuesKey = sectionsPValueIt.next();
												String sectionPLangKey = sectionsPLangIt.next();
												List<String> valuesPList = sectionsPValueMap.get(sectionPValuesKey);
												List<String> langPList = sectionsPLangMap.get(sectionPLangKey);
												for (int l = 0; l < valuesPList.size(); l++) {
													if (l == j) {
														if (valuesPList.get(l) != null && !valuesPList.get(l).isEmpty()) {
															P p = new P();
															p.setContent(valuesPList.get(l));
															if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langPList.get(l))) {
																p.setLang(langPList.get(l));
															}
	
															if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionPValuesKey)
																	&& Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionPLangKey)) {
																DescriptiveNote descriptiveNote = null;
																if (resourceRelation.getDescriptiveNote() == null) {
																	descriptiveNote = new DescriptiveNote();
																} else {
																	descriptiveNote = resourceRelation.getDescriptiveNote();
																}
																descriptiveNote.getP().add(p);
																resourceRelation.setDescriptiveNote(descriptiveNote);
															}
														}
													}
												}
											}
										}
									}
								}
							}
							if(relationEntry.getContent()!=null && !relationEntry.getContent().isEmpty()){
								resourceRelation.setRelationEntry(relationEntry);
							}
							boolean found = false;
							//if (Eag2012.RESOURCE_RELATION.equalsIgnoreCase(sectionValueKey)){ //TODO: current situation from ticket 543 
								for(int x=0;x<this.eag.getRelations().getResourceRelation().size();x++){
									if(this.eag.getRelations().getResourceRelation().get(x)!=null && this.eag.getRelations().getResourceRelation().get(x).getHref()!=null && this.eag.getRelations().getResourceRelation().get(x).getHref().equalsIgnoreCase(resourceRelation.getHref()) && 
											this.eag.getRelations().getResourceRelation().get(x).getRelationEntry()!=null && (
													(this.eag.getRelations().getResourceRelation().get(x).getRelationEntry().getLang()!=null && 
													this.eag.getRelations().getResourceRelation().get(x).getRelationEntry().getLang().equalsIgnoreCase(relationEntry.getLang()) ) || 
													(this.eag.getRelations().getResourceRelation().get(x).getRelationEntry().getLang()==null && (relationEntry.getLang()==null || relationEntry.getLang().equalsIgnoreCase(Eag2012.OPTION_NONE)) ) )  ){
										found = true;
										if(resourceRelation.getDescriptiveNote()!=null){ //priority for descriptive note
											//TODO: current situation from ticket 543 
											this.eag.getRelations().getResourceRelation().set(x,resourceRelation);
										}
									}
								}
								if(!found){
									//TODO: current situation from ticket 543
									if(resourceRelation.getRelationEntry()!=null || (resourceRelation.getHref()!=null && !resourceRelation.getHref().isEmpty())){
										this.eag.getRelations().getResourceRelation().add(resourceRelation);
									}
								}
							//}
						}
					}

					// eag/relations/eagRelation/relationEntry
					if (Eag2012.INSTITUTION_RELATIONS.equalsIgnoreCase(sectionValueKey)) {
						for (int j = 0; j < valueList.size() && !valueList.get(j).isEmpty(); j++) {
							EagRelation eagRelation = new EagRelation();

							// eag/relations/eagRelation/eagRelationType
							if (this.eag2012.getEagRelationEagRelationType() != null
									&& !this.eag2012.getEagRelationEagRelationType().isEmpty()) {
								if (Eag2012.OPTION_CHILD.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
									eagRelation.setEagRelationType(Eag2012.OPTION_CHILD_TEXT);
								}
								if (Eag2012.OPTION_PARENT.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
									eagRelation.setEagRelationType(Eag2012.OPTION_PARENT_TEXT);
								}
								if (Eag2012.OPTION_EARLIER.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
									eagRelation.setEagRelationType(Eag2012.OPTION_EARLIER_TEXT);
								}
								if (Eag2012.OPTION_LATER.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
									eagRelation.setEagRelationType(Eag2012.OPTION_LATER_TEXT);
								}
								if (Eag2012.OPTION_ASSOCIATIVE.equalsIgnoreCase(this.eag2012.getEagRelationEagRelationType().get(j))) {
									eagRelation.setEagRelationType(Eag2012.OPTION_ASSOCIATIVE_TEXT);
								}

								// eag/relations/eagRelation/href
								if (this.eag2012.getEagRelationHref() != null && !this.eag2012.getEagRelationHref().isEmpty()) {
									if (this.eag2012.getEagRelationHref().size() > valueList.size()) {
										String href = this.eag2012.getEagRelationHref().get(j+1);
									//	href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?href:((href!=null && !href.isEmpty())?((href!=null && !href.isEmpty())?("http://"+href):href):null);
										eagRelation.setHref(href);
									} else {
										String href = this.eag2012.getEagRelationHref().get(j);
								//		href = (href!=null && href.length()>0 && (href.toLowerCase().startsWith("http://") || href.toLowerCase().startsWith("ftp://") || href.toLowerCase().startsWith("https://")) )?href:((href!=null && !href.isEmpty())?((href!=null && !href.isEmpty())?("http://"+href):href):null);
										eagRelation.setHref(href);
									}
								}
							}

							RelationEntry relationEntry = new RelationEntry();
							relationEntry.setContent(valueList.get(j));

							// eag/relations/eagRelation/descriptiveNote/P
							if (this.eag2012.getDescriptiveNotePValue() != null
									&& this.eag2012.getDescriptiveNotePLang() != null) {
								for (int k = 0; k < this.eag2012.getDescriptiveNotePValue().size(); k++) {
									Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(k);
									Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(k);
									Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
									Iterator<String> tabsLangIt = tabsLangMap.keySet().iterator();
									while (tabsValueIt.hasNext()) {
										String tabValueKey = tabsValueIt.next();
										String tabLangKey = tabsLangIt.next();

										if (Eag2012.TAB_RELATION.equalsIgnoreCase(tabValueKey)
												&& Eag2012.TAB_RELATION.equalsIgnoreCase(tabLangKey)) {
											Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
											Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
											Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
											Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
											while (sectionsValueIt.hasNext()) {
												String sectionValuesKey = sectionsValueIt.next();
												sectionLangKey = sectionsLangIt.next();
												List<String> valuesList = sectionsValueMap.get(sectionValuesKey);
												langList = sectionsLangMap.get(sectionLangKey);
												for (int l = 0; l < valuesList.size(); l++) {
													if (l == j) {
														if (valuesList.get(l) != null && !valuesList.get(l).isEmpty()) {
															P p = new P();
															p.setContent(valuesList.get(l));
															if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(l))) {
																p.setLang(langList.get(l));
															}
	
															if (Eag2012.INSTITUTION_RELATIONS.equalsIgnoreCase(sectionValuesKey)
																	&& Eag2012.INSTITUTION_RELATIONS.equalsIgnoreCase(sectionLangKey)) {
																DescriptiveNote descriptiveNote = null;
																if (eagRelation.getDescriptiveNote() == null) {
																	descriptiveNote = new DescriptiveNote();
																} else {
																	descriptiveNote = eagRelation.getDescriptiveNote();
																}
																descriptiveNote.getP().add(p);
																eagRelation.setDescriptiveNote(descriptiveNote);
															}
														}
													}
												}
											}
										}
									}
								}
							}
							eagRelation.getRelationEntry().add(relationEntry);
							this.eag.getRelations().getEagRelation().add(eagRelation);
						}
						if((this.eag.getRelations().getEagRelation().isEmpty() && this.eag.getRelations().getResourceRelation().isEmpty()) ){
							this.eag.setRelations(null);
						}
					}
				}
			}
		}
	}

	/**
	 * Method to fill "Identity" element.
	 */
	private void fillIdentity() {
		// eag/archguide/identity/repositorid/countrycode
		if (this.eag.getArchguide().getIdentity().getRepositorid() == null) {
			this.eag.getArchguide().getIdentity().setRepositorid(new Repositorid());
		}
		this.eag.getArchguide().getIdentity().getRepositorid().setCountrycode(this.eag2012.getRepositoridCountrycode());
		// eag/archguide/identity/repositorid/repositorycode
		this.eag.getArchguide().getIdentity().getRepositorid().setRepositorycode(this.eag2012.getRepositoridRepositorycode());
		// eag/archguide/identity/otherRepositorid
		if (this.eag2012.getOtherRepositorId() != null && !this.eag2012.getOtherRepositorId().isEmpty()) {
			if (this.eag.getArchguide().getIdentity().getOtherRepositorId() == null) {
				this.eag.getArchguide().getIdentity().setOtherRepositorId(new OtherRepositorId());
			}
			this.eag.getArchguide().getIdentity().getOtherRepositorId().setContent(this.eag2012.getOtherRepositorId());
		}
		// eag/archguide/identity/autform
		if (this.eag2012.getAutformValue() != null) {
			for (int i = 0; i < this.eag2012.getAutformValue().size(); i++) {
				Autform autform = new Autform();
				// eag/archguide/identity/autform
				autform.setContent(this.eag2012.getAutformValue().get(i));
				// eag/archguide/identity/autform/lang
				if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getAutformLang().get(i))) {
					autform.setLang(this.eag2012.getAutformLang().get(i));
				}
	
				this.eag.getArchguide().getIdentity().getAutform().add(autform);
			}
		}
		// eag/archguide/identity/parform
		if (this.eag2012.getParformValue() != null
				&& this.eag2012.getParformValue().size() > 0) {
			for (int i = 0; i < this.eag2012.getParformValue().size(); i++) {
				if (this.eag2012.getParformValue().get(i) != null
						&& !this.eag2012.getParformValue().get(i).isEmpty()) {
					Parform parform = new Parform();
					// eag/archguide/identity/parform
					parform.setContent(this.eag2012.getParformValue().get(i));
					// eag/archguide/identity/parform/lang
					if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getParformLang().get(i))) {
						parform.setLang(this.eag2012.getParformLang().get(i));
					}
		
					this.eag.getArchguide().getIdentity().getParform().add(parform);
				}
			}
		}

		// eag/archguide/identity/nonpreform
		if (this.eag2012.getNonpreformValue() != null){
			for (int i = 0; i < this.eag2012.getNonpreformValue().size(); i++) {
				if (this.eag2012.getNonpreformValue().get(i) != null
						&& !this.eag2012.getNonpreformValue().get(i).isEmpty()) {
					Nonpreform nonpreform = new Nonpreform();
					if (!Eag2012.OPTION_NONE.equalsIgnoreCase(this.eag2012.getNonpreformLang().get(i))) {
						nonpreform.setLang(this.eag2012.getNonpreformLang().get(i));
					}
					nonpreform.getContent().add(this.eag2012.getNonpreformValue().get(i));
	
					// eag/archguide/identity/nonpreform/dates
					getNompreformDates(nonpreform, i);
	
					this.eag.getArchguide().getIdentity().getNonpreform().add(nonpreform);
				}
			}
		}
		
		// eag/archguide/identity/repositoryType
		if (this.eag2012.getRepositoryTypeValue() != null) {
			for (int i = 0; i < this.eag2012.getRepositoryTypeValue().size(); i++) {
				if (this.eag2012.getRepositoryTypeValue().get(i) != null
						&& !this.eag2012.getRepositoryTypeValue().get(i).isEmpty()) {
					String[] repositoryTypeList = this.eag2012.getRepositoryTypeValue().get(i).split("_");
					for (int j = 0; j < repositoryTypeList.length; j++) {
						String value = repositoryTypeList[j];

						if (Eag2012.OPTION_NATIONAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_NATIONAL_TEXT;
						} else if (Eag2012.OPTION_REGIONAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_REGIONAL_TEXT;
						} else if (Eag2012.OPTION_COUNTY.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_COUNTY_TEXT;
						} else if (Eag2012.OPTION_MUNICIPAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_MUNICIPAL_TEXT;
						} else if (Eag2012.OPTION_SPECIALISED.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_SPECIALISED_TEXT;
						} else if (Eag2012.OPTION_PRIVATE.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_PRIVATE_TEXT;
						} else if (Eag2012.OPTION_CHURCH.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_CHURCH_TEXT;
						} else if (Eag2012.OPTION_BUSINESS.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_BUSINESS_TEXT;
						} else if (Eag2012.OPTION_UNIVERSITY.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_UNIVERSITY_TEXT;
						} else if (Eag2012.OPTION_MEDIA.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_MEDIA_TEXT;
						} else if (Eag2012.OPTION_POLITICAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_POLITICAL_TEXT;
						} else if (Eag2012.OPTION_CULTURAL.equalsIgnoreCase(value)) {
							value = Eag2012.OPTION_CULTURAL_TEXT;
						}

						RepositoryType repositoryType = new RepositoryType();
						repositoryType.setValue(value);
	
						this.eag.getArchguide().getIdentity().getRepositoryType().add(repositoryType);
					}
				}
			}
		}
	}

	/**
	 * Method to fill "Desc" element.
	 */
	private void fillDesc() {
		// Create repositories element.
		if (this.eag.getArchguide().getDesc().getRepositories() == null) {
			this.eag.getArchguide().getDesc().setRepositories(new Repositories());
		}
		// Create repository elements.
		if (this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			List<Repository> repositoryList = new ArrayList<Repository>();

			// Add institution repository.
			repositoryList.add(new Repository());

			// Add other repositories.
			if (this.eag2012.getRepositoryNameValue() != null) {
				for (int i = 0; i < this.eag2012.getRepositoryNameValue().size(); i++) {
					repositoryList.add(new Repository());
				}
			}

			this.eag.getArchguide().getDesc().getRepositories().setRepository(repositoryList);
		}

		// eag/archguide/identity/nonpreform/dates &&...
		getAllDates();

		// eag/archguide/desc/repositories/repository/repositoryName
		// Main institution.
//		Repository mainRepository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(0);
//		RepositoryName repositoryName = null;
//		if (mainRepository.getRepositoryName() == null) {
//			repositoryName = new RepositoryName();
//		} else {
//			repositoryName = mainRepository.getRepositoryName();
//		}
//		repositoryName.setContent(this.eag2012.getAutformValue().get(0));
//		
//		mainRepository.setRepositoryName(repositoryName);

		// Rest of repositories.
		if (this.eag2012.getRepositoryNameValue() != null) {
			for (int i = 0; i < this.eag2012.getRepositoryNameValue().size(); i++) {
				Repository otherRepositories = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i+1);

				RepositoryName otherRepositoryName = null;
				if (otherRepositories.getRepositoryRole() == null) {
					otherRepositoryName = new RepositoryName();
				} else {
					otherRepositoryName = otherRepositories.getRepositoryName();
				}
				otherRepositoryName.setContent(this.eag2012.getRepositoryNameValue().get(i));

				otherRepositories.setRepositoryName(otherRepositoryName);
			}
		}

		// eag/archguide/desc/repositories/repository/repositoryRole
		// Main institution.
//		RepositoryRole repositoryRole = null;
//		if (mainRepository.getRepositoryRole() == null) {
//			repositoryRole = new RepositoryRole();
//		} else {
//			repositoryRole = mainRepository.getRepositoryRole();
//		}
//		repositoryRole.setValue(Eag2012.OPTION_ROLE_HEADQUARTERS_TEXT);
//
//		mainRepository.setRepositoryRole(repositoryRole);

		// Rest of repositories.
		if (this.eag2012.getRepositoryRoleValue() != null) {
			for (int i = 0; i < this.eag2012.getRepositoryRoleValue().size(); i++) {
				Repository otherRepositories = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i+1);

				RepositoryRole otherRepositoryRole = null;
				if (otherRepositories.getRepositoryRole() == null) {
					otherRepositoryRole = new RepositoryRole();
				} else {
					otherRepositoryRole = otherRepositories.getRepositoryRole();
				}

				if (Eag2012.OPTION_ROLE_BRANCH.equalsIgnoreCase(this.eag2012.getRepositoryRoleValue().get(i))) {
					otherRepositoryRole.setValue(Eag2012.OPTION_ROLE_BRANCH_TEXT);
				} else if (Eag2012.OPTION_ROLE_HEADQUARTERS.equalsIgnoreCase(this.eag2012.getRepositoryRoleValue().get(i))) {
					otherRepositoryRole.setValue(Eag2012.OPTION_ROLE_HEADQUARTERS_TEXT);
				} else if (Eag2012.OPTION_ROLE_INTERIM.equalsIgnoreCase(this.eag2012.getRepositoryRoleValue().get(i))) {
					otherRepositoryRole.setValue(Eag2012.OPTION_ROLE_INTERIM_TEXT);
				}

				otherRepositories.setRepositoryRole(otherRepositoryRole);
			}
		}

		// eag/archguide/desc/repositories/repository/geogarea
		if (this.eag2012.getGeogareaValue() != null) {
			String geogareaValue = this.eag2012.getGeogareaValue();
			Geogarea geogarea = new Geogarea();

			if (Eag2012.OPTION_AFRICA.equalsIgnoreCase(geogareaValue)) {
				geogarea.setValue(Eag2012.OPTION_AFRICA_TEXT);
			} else if (Eag2012.OPTION_ANTARCTICA.equalsIgnoreCase(geogareaValue)) {
				geogarea.setValue(Eag2012.OPTION_ANTARCTICA_TEXT);
			} else if (Eag2012.OPTION_ASIA.equalsIgnoreCase(geogareaValue)) {
				geogarea.setValue(Eag2012.OPTION_ASIA_TEXT);
			} else if (Eag2012.OPTION_AUSTRALIA.equalsIgnoreCase(geogareaValue)) {
				geogarea.setValue(Eag2012.OPTION_AUSTRALIA_TEXT);
			} else if (Eag2012.OPTION_EUROPE.equalsIgnoreCase(geogareaValue)) {
				geogarea.setValue(Eag2012.OPTION_EUROPE_TEXT);
			} else if (Eag2012.OPTION_NORTH_AMERICA.equalsIgnoreCase(geogareaValue)) {
				geogarea.setValue(Eag2012.OPTION_NORTH_AMERICA_TEXT);
			} else if (Eag2012.OPTION_SOUTH_AMERICA.equalsIgnoreCase(geogareaValue)) {
				geogarea.setValue(Eag2012.OPTION_SOUTH_AMERICA_TEXT);
			}

			// For every repository.
			for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i ++) {
				this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i).setGeogarea(geogarea);
			}
		}

		// eag/archguide/desc/repositories/repository/location
		if (this.eag2012.getStreetLang() != null) {
			for (int i =0; i < this.eag2012.getStreetLang().size(); i++) {
				if (this.eag2012.getStreetLang().get(i) != null) {
					// Recover repository.
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

					// Visitor address maps.
					Map<String, List<String>> streetLangMap = this.eag2012.getStreetLang().get(i);
					Map<String, List<String>> latitudeMap = this.eag2012.getLocationLatitude().get(i);
					Map<String, List<String>> longitudeMap = this.eag2012.getLocationLongitude().get(i);
					Map<String, List<String>> countryMap = this.eag2012.getCountryValue().get(i);
					Map<String, List<String>> citiesMap = this.eag2012.getCitiesValue().get(i);
					Map<String, List<String>> streetMap = this.eag2012.getStreetValue().get(i);

					// Visitor address iterators.
					Iterator<String> streetLangIt = streetLangMap.keySet().iterator();
					Iterator<String> latitudeIt = latitudeMap.keySet().iterator();
					Iterator<String> longitudeIt = longitudeMap.keySet().iterator();
					Iterator<String> countryIt = countryMap.keySet().iterator();
					Iterator<String> citiesIt = citiesMap.keySet().iterator();
					Iterator<String> streetIt = streetMap.keySet().iterator();
					while (streetLangIt.hasNext()) {
						// Visitor address keys.
						String streetLangKey = streetLangIt.next();
						String latitudeKey = latitudeIt.next();
						String longitudeKey = longitudeIt.next();
						String countryKey = countryIt.next();
						String citiesKey = citiesIt.next();
						String streetKey = streetIt.next();

						// Rest of tabs.
//						if (!streetLangKey.equalsIgnoreCase(Eag2012.TAB_YOUR_INSTITUTION))  {
							// Visitor address lists.
							List<String> streetLangList = streetLangMap.get(streetLangKey);
							List<String> latitudeList = latitudeMap.get(latitudeKey);
							List<String> longitudeList = longitudeMap.get(longitudeKey);
							List<String> countryList = countryMap.get(countryKey);
							List<String> firstdemList = null;
							if (this.eag2012.getFirstdemValue() != null) {
								firstdemList = this.eag2012.getFirstdemValue().get(i);
							}
							List<String> secondemList = null;
							if (this.eag2012.getSecondemValue() != null) {
								secondemList = this.eag2012.getSecondemValue().get(i);
							}
							List<String> localentityList = null;
							if (this.eag2012.getLocalentityValue() != null) {
								localentityList = this.eag2012.getLocalentityValue().get(i);
							}
							List<String> citiesList = citiesMap.get(citiesKey);
							List<String> streetList = streetMap.get(streetKey);
							for (int j = 0; j < streetLangList.size(); j++) {
								Location location = new Location();
								String language = streetLangList.get(j);
								// eag/archguide/desc/repositories/repository/location/type
								location.setLocalType(Eag2012.VISITORS_ADDRESS);
								if (latitudeList.get(j) != null
										&& !latitudeList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/latitude
									location.setLatitude(latitudeList.get(j));
								}
								if (longitudeList.get(j) != null
										&& !longitudeList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/longitude
									location.setLongitude(longitudeList.get(j));
								}
								// eag/archguide/desc/repositories/repository/location/country
								if (countryList.get(j) != null
										&& !countryList.get(j).isEmpty()) {
									if (location.getCountry() == null) {
										location.setCountry(new Country());
									}
									location.getCountry().setContent(countryList.get(j));
									// eag/archguide/desc/repositories/repository/location/country/lang
									if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
										location.getCountry().setLang(language);
									}
								}
								if (firstdemList != null && firstdemList.size() > j
										&& firstdemList.get(j) != null
										&& !firstdemList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/firstdem
									if (location.getFirstdem() == null) {
										location.setFirstdem(new Firstdem());
									}
									if (firstdemList.size() > j) {
										location.getFirstdem().setContent(firstdemList.get(j));
										// eag/archguide/desc/repositories/repository/location/firstdem/lang
										if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
											location.getFirstdem().setLang(language);
										}
									}
								}
								if (secondemList != null && secondemList.size() > j
										&& secondemList.get(j) != null
										&& !secondemList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/secondem
									if (location.getSecondem() == null) {
										location.setSecondem(new Secondem());
									}
									if (secondemList.size() > j) {
										location.getSecondem().setContent(secondemList.get(j));
										// eag/archguide/desc/repositories/repository/location/secondem/lang
										if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
											location.getSecondem().setLang(language);
										}
									}
								}
								if (citiesList.get(j) != null
										&& !citiesList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/municipalityPostalcode
									if (location.getMunicipalityPostalcode() == null) {
										location.setMunicipalityPostalcode(new MunicipalityPostalcode());
									}
									location.getMunicipalityPostalcode().setContent(citiesList.get(j));
									// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
									if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
										location.getMunicipalityPostalcode().setLang(language);
									}
								}
								if (localentityList != null && localentityList.size() > j
										&& localentityList.get(j) != null
										&& !localentityList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/localentity
									if (location.getLocalentity() == null) {
										location.setLocalentity(new Localentity());
									}
									if (localentityList.size() > j) {
										location.getLocalentity().setContent(localentityList.get(j));
										// eag/archguide/desc/repositories/repository/location/localentity/lang
										if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
											location.getLocalentity().setLang(language);
										}
									}
								}
								if(streetList.get(j) != null
										&& ! streetList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/street
									if (location.getStreet() == null) {
										location.setStreet(new Street());
									}
									location.getStreet().setContent(streetList.get(j));
									// eag/archguide/desc/repositories/repository/location/street/lang
									if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
										location.getStreet().setLang(language);
									}
								}
								//TODO: add looper only for check repeated fields for different contact and your_institution tabs information
								boolean found = false;
								for (int x = 0; !found && x < repository.getLocation().size(); x++) {
									Location target = repository.getLocation().get(x);
									if (target.getStreet() != null && target.getStreet().getContent() != null
											&& target.getStreet().getContent().equalsIgnoreCase(streetList.get(j))
											&& Eag2012.VISITORS_ADDRESS.equalsIgnoreCase(target.getLocalType())
											&& target.getMunicipalityPostalcode()!= null
											&& target.getMunicipalityPostalcode().getContent() != null
											&& target.getMunicipalityPostalcode().getContent().equalsIgnoreCase(citiesList.get(j))
											&& target.getCountry() != null
											&& target.getCountry().getContent() != null
											&& target.getCountry().getContent().equalsIgnoreCase(countryList.get(j))) {
										found = true;
									}
								}
								if (!found
										&& (location.getCountry() != null
										|| location.getFirstdem() != null
										|| location.getLatitude() != null
										|| location.getLocalentity() != null
										|| location.getLongitude() != null
										|| location.getMunicipalityPostalcode() != null
										|| location.getSecondem() != null
										|| location.getStreet() != null)) {
									repository.getLocation().add(location);
								}							
							}
//						}
					}
				}

				if (this.eag2012.getPostalStreetLang().size() > i
						&& this.eag2012.getPostalStreetLang().get(i) != null) {
					// Recover repository.
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

					// Postal address maps.
					Map<String, List<String>> postalStreetLangMap = this.eag2012.getPostalStreetLang().get(i);
					Map<String, List<String>> postalCountryMap = this.eag2012.getCountryValue().get(i);
					Map<String, List<String>> postalCitiesMap = this.eag2012.getMunicipalityPostalcodeValue().get(i);
					Map<String, List<String>> postalStreetMap = this.eag2012.getPostalStreetValue().get(i);

					// Postal address iterators.
					Iterator<String>  postalStreetLangIt = postalStreetLangMap.keySet().iterator();
					Iterator<String>  postalCountryIt = postalCountryMap.keySet().iterator();
					Iterator<String>  postalCitiesIt = postalCitiesMap.keySet().iterator();
					Iterator<String>  postalStreetIt = postalStreetMap.keySet().iterator();
					while (postalStreetLangIt.hasNext()) {
						// Postal address keys.
						String postalStreetLangKey = postalStreetLangIt.next();
						String postalCountryKey = postalCountryIt.next();
						String postalCitiesKey = postalCitiesIt.next();
						String postalStreetKey = postalStreetIt.next();

						// Rest of tabs.
//						if (!postalStreetLangKey.equalsIgnoreCase(Eag2012.TAB_YOUR_INSTITUTION))  {
							// Postal address lists.
							List<String> postalStreetLangList = postalStreetLangMap.get(postalStreetLangKey);
							List<String> postalCountryList = postalCountryMap.get(postalCountryKey);
							List<String> postalCitiesList = postalCitiesMap.get(postalCitiesKey);
							List<String> postalStreetList = postalStreetMap.get(postalStreetKey);
							for (int j = 0; j < postalStreetLangList.size(); j++) {
								Location location = new Location();
								String language = postalStreetLangList.get(j);
								// eag/archguide/desc/repositories/repository/location/type
								location.setLocalType(Eag2012.POSTAL_ADDRESS);
								if (postalCountryList.size() > 0) {
									// eag/archguide/desc/repositories/repository/location/country
									if (location.getCountry() == null) {
										location.setCountry(new Country());
									}
									if (postalCountryList.size() > j) {
										location.getCountry().setContent(postalCountryList.get(j));
									} else {
										location.getCountry().setContent(postalCountryList.get(0));
									}
									// eag/archguide/desc/repositories/repository/location/country/lang
									if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
										location.getCountry().setLang(language);
									}
								}
								if (postalCitiesList.get(j) != null
										&& !postalCitiesList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/municipalityPostalcode
									if (location.getMunicipalityPostalcode() == null) {
										location.setMunicipalityPostalcode(new MunicipalityPostalcode());
									}
									location.getMunicipalityPostalcode().setContent(postalCitiesList.get(j));
									// eag/archguide/desc/repositories/repository/location/municipalityPostalcode/lang
									if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
										location.getMunicipalityPostalcode().setLang(language);
									}
								}
								if (postalStreetList.get(j) != null
										&& !postalStreetList.get(j).isEmpty()) {
									// eag/archguide/desc/repositories/repository/location/street
									if (location.getStreet() == null) {
										location.setStreet(new Street());
									}
									location.getStreet().setContent(postalStreetList.get(j));
									// eag/archguide/desc/repositories/repository/location/street/lang
									if (!Eag2012.OPTION_NONE.equalsIgnoreCase(language)) {
										location.getStreet().setLang(language);
									}
								}
								//TODO: add looper only for check repeated fields for different contact and your_institution tabs information
								boolean found = false;
								for(int x=0;!found && x<repository.getLocation().size();x++){
									Location target = repository.getLocation().get(x);
									if(target !=null && target.getStreet()!=null
											&& target.getStreet().getContent()!=null 
											&& target.getStreet().getContent().equalsIgnoreCase(postalStreetList.get(j)) 
											&& Eag2012.POSTAL_ADDRESS.equalsIgnoreCase(target.getLocalType())
											&& target.getMunicipalityPostalcode()!=null
											&& target.getMunicipalityPostalcode().getContent() != null
											&& target.getMunicipalityPostalcode().getContent().equalsIgnoreCase(postalCitiesList.get(j)) ){
										found = true;
									}
								}
								if(!found
										&& (location.getMunicipalityPostalcode() != null
										|| location.getStreet() != null)){
									repository.getLocation().add(location);
								}
							}
//						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/telephone
		if (this.eag2012.getTelephoneValue() != null) {
			for (int i = 0; i < this.eag2012.getTelephoneValue().size(); i++) {
				Map<String, Map<String, List<String>>> tabsMap = this.eag2012.getTelephoneValue().get(i);
				// Repository.
				Repository repository = (this.eag.getArchguide().getDesc().getRepositories().getRepository().size()>i)?this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i):null;
				if(repository!=null){
					Iterator<String> tabsIt = tabsMap.keySet().iterator();
					while (tabsIt.hasNext()) {
						String tabKey = tabsIt.next();
						Map<String, List<String>> sectionMap = tabsMap.get(tabKey);
						Iterator<String> sectionIt = sectionMap.keySet().iterator();
						while (sectionIt.hasNext()) {
							String sectionKey = sectionIt.next();
							List<String> telephoneList = sectionMap.get(sectionKey);
							for (int k = 0; k < telephoneList.size(); k++) {
								if (telephoneList.get(k) == null
										|| telephoneList.get(k).isEmpty()) {
									break;
								}

								Telephone telephone = new Telephone();
								telephone.setContent(telephoneList.get(k));
								if (Eag2012.ROOT.equalsIgnoreCase(sectionKey)) {
									repository.getTelephone().add(telephone);
								}

								// eag/archguide/desc/repositories/repository/services/searchroom
								if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionKey)) {
									if (repository.getServices() == null) {
										repository.setServices(new Services());
									}
									if (repository.getServices().getSearchroom() == null) {
										repository.getServices().setSearchroom(new Searchroom());
									}
									if (repository.getServices().getSearchroom().getContact() == null) {
										repository.getServices().getSearchroom().setContact(new Contact());
									}
										repository.getServices().getSearchroom().getContact().getTelephone().add(telephone);
								}

								// eag/archguide/desc/repositories/repository/services/library
								if (Eag2012.LIBRARY.equalsIgnoreCase(sectionKey)) {
									if (repository.getServices() == null) {
										repository.setServices(new Services());
									}
									if (repository.getServices().getLibrary() == null) {
										repository.getServices().setLibrary(new Library());
									}
									if (repository.getServices().getLibrary().getContact() == null) {
										repository.getServices().getLibrary().setContact(new Contact());
									}
										repository.getServices().getLibrary().getContact().getTelephone().add(telephone);
								}

								// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
								if (Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionKey)) {
									if (repository.getServices() == null) {
										repository.setServices(new Services());
									}
									if (repository.getServices().getTechservices() == null) {
										repository.getServices().setTechservices(new Techservices());
									}
									if (repository.getServices().getTechservices().getRestorationlab() == null) {
										repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
									}
									if (repository.getServices().getTechservices().getRestorationlab().getContact() == null) {
										repository.getServices().getTechservices().getRestorationlab().setContact(new Contact());
									}
										repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().add(telephone);
								}

								// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
								if (Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionKey)) {
									if (repository.getServices() == null) {
										repository.setServices(new Services());
									}
									if (repository.getServices().getTechservices() == null) {
										repository.getServices().setTechservices(new Techservices());
									}
									if (repository.getServices().getTechservices().getReproductionser() == null) {
										repository.getServices().getTechservices().setReproductionser(new Reproductionser());
									}
									if (repository.getServices().getTechservices().getReproductionser().getContact() == null) {
										repository.getServices().getTechservices().getReproductionser().setContact(new Contact());
									}
										repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().add(telephone);
								}
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/fax
		if (this.eag2012.getFaxValue() != null) {
			for (int i = 0; i < this.eag2012.getFaxValue().size(); i++) {
				Map<String, List<String>> tabsMap = this.eag2012.getFaxValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					if (tabKey == Eag2012.TAB_CONTACT) {
						List<String> faxList = tabsMap.get(tabKey);
						for (int k = 0; k < faxList.size(); k++) {
							if (faxList.get(k) != null
									&& !faxList.get(k).isEmpty()) {
								Fax fax = new Fax();
								fax.setContent(faxList.get(k));
								repository.getFax().add(fax);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/email (href & lang)
		if (this.eag2012.getEmailValue() != null || this.eag2012.getEmailHref() != null) {
			for (int i = 0; i < this.eag2012.getEmailValue().size(); i++) {
				Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getEmailValue().get(i);
				Map<String, Map<String, List<String>>> tabsHrefMap = (this.eag2012.getEmailHref()!=null && this.eag2012.getEmailHref().size()>i)?this.eag2012.getEmailHref().get(i):null;
				Map<String, Map<String, List<String>>> tabsLangMap = (this.eag2012.getEmailLang()!=null && this.eag2012.getEmailLang().size()>i)?this.eag2012.getEmailLang().get(i):null;
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

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
						for (int k = 0; k < emailValueList.size() || (emailHrefList!=null && k < emailHrefList.size()); k++) {
							if ((emailValueList.get(k) == null || emailValueList.get(k).isEmpty()) && (emailHrefList.get(k) == null || emailHrefList.get(k).isEmpty())) {
								break;
							}
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
									repository.getEmail().add(email);
							}

							// eag/archguide/desc/repositories/repository/services/searchroom
							if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionValueKey)
									&& Eag2012.SEARCHROOM.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getSearchroom() == null) {
									repository.getServices().setSearchroom(new Searchroom());
								}
								if (repository.getServices().getSearchroom().getContact() == null) {
									repository.getServices().getSearchroom().setContact(new Contact());
								}
	
								repository.getServices().getSearchroom().getContact().getEmail().add(email);
							}

							// eag/archguide/desc/repositories/repository/services/library
							if (Eag2012.LIBRARY.equalsIgnoreCase(sectionValueKey)
									&& Eag2012.LIBRARY.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getLibrary() == null) {
									repository.getServices().setLibrary(new Library());
								}
								if (repository.getServices().getLibrary().getContact() == null) {
									repository.getServices().getLibrary().setContact(new Contact());
								}
	
								repository.getServices().getLibrary().getContact().getEmail().add(email);
							}

							// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
							if (Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionValueKey)
									&& Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getTechservices() == null) {
									repository.getServices().setTechservices(new Techservices());
								}
								if (repository.getServices().getTechservices().getRestorationlab() == null) {
									repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
								}
								if (repository.getServices().getTechservices().getRestorationlab().getContact() == null) {
									repository.getServices().getTechservices().getRestorationlab().setContact(new Contact());
								}
	
								repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().add(email);
							}

							// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
							if (Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionValueKey)
									&& Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionHrefKey)) {
								if (repository.getServices() == null) {
									repository.setServices(new Services());
								}
								if (repository.getServices().getTechservices() == null) {
									repository.getServices().setTechservices(new Techservices());
								}
								if (repository.getServices().getTechservices().getReproductionser() == null) {
									repository.getServices().getTechservices().setReproductionser(new Reproductionser());
								}
								if (repository.getServices().getTechservices().getReproductionser().getContact() == null) {
									repository.getServices().getTechservices().getReproductionser().setContact(new Contact());
								}
	
								repository.getServices().getTechservices().getReproductionser().getContact().getEmail().add(email);
							}
						}
					}
				}
			}
		}

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
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

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
						String sectionValueKey = sectionValueIt.next();
						String sectionHrefKey = sectionHrefIt.next();
						String sectionLangKey = (sectionLangIt!=null && sectionLangIt.hasNext())?sectionLangIt.next():null;
						List<String> webpageValueList = sectionValueMap.get(sectionValueKey);
						List<String> webpageHrefList = (sectionHrefMap!=null)?sectionHrefMap.get(sectionHrefKey):null;
						List<String> webpageLangList = (sectionLangKey!=null)?sectionLangMap.get(sectionLangKey):null;
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
									repository.getWebpage().add(webpage);
								}

								// eag/archguide/desc/repositories/repository/services/searchroom
								if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionValueKey) && 
										(Eag2012.SEARCHROOM.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
									if (repository.getServices() == null) {
										repository.setServices(new Services());
									}
									if (repository.getServices().getSearchroom() == null) {
										repository.getServices().setSearchroom(new Searchroom());
									}
		
									repository.getServices().getSearchroom().getWebpage().add(webpage);
								}

								// eag/archguide/desc/repositories/repository/services/library
								if (Eag2012.LIBRARY.equalsIgnoreCase(sectionValueKey) && 
										(Eag2012.LIBRARY.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
									if (repository.getServices() == null) {
										repository.setServices(new Services());
									}
									if (repository.getServices().getLibrary() == null) {
										repository.getServices().setLibrary(new Library());
									}
		
									repository.getServices().getLibrary().getWebpage().add(webpage);
								}

								// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
								if (Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionValueKey) && 
										(Eag2012.RESTORATION_LAB.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
									if (repository.getServices() == null) {
										repository.setServices(new Services());
									}
									if (repository.getServices().getTechservices() == null) {
										repository.getServices().setTechservices(new Techservices());
									}
									if (repository.getServices().getTechservices().getRestorationlab() == null) {
										repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
									}
		
									repository.getServices().getTechservices().getRestorationlab().getWebpage().add(webpage);
								}

								// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
								if (Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionValueKey) && 
										(Eag2012.REPRODUCTIONSER.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
									if (repository.getServices() == null) {
										repository.setServices(new Services());
									}
									if (repository.getServices().getTechservices() == null) {
										repository.getServices().setTechservices(new Techservices());
									}
									if (repository.getServices().getTechservices().getReproductionser() == null) {
										repository.getServices().getTechservices().setReproductionser(new Reproductionser());
									}
		
									repository.getServices().getTechservices().getReproductionser().getWebpage().add(webpage);
								}

								//TODO: changed implementation for exhibition, tours section and other services
								// eag/archguide/desc/repositories/repository/services/recreationalServices/exhibition
								if (Eag2012.EXHIBITION.equalsIgnoreCase(sectionValueKey) && 
										(Eag2012.EXHIBITION.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
									exhibitions = true;
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
									exhibition.setWebpage(webpage);
								}

								// eag/archguide/desc/repositories/repository/services/recreationalServices/toursSessions
								if (Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionValueKey) && 
										(Eag2012.TOURS_SESSIONS.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
									tours = true;
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
									toursSessions.setWebpage(webpage);
								}

								// eag/archguide/desc/repositories/repository/services/recreationalServices/otherServices
								if (Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionValueKey) && 
										(Eag2012.OTHER_SERVICES.equalsIgnoreCase(sectionHrefKey) || sectionHrefKey==null )) {
									otherServices = true;
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
									otherService.setWebpage(webpage);
								}
							}
						}
						if(!exhibitions || !tours || !otherServices){
							exhibitionToursAndOtherServicesDescriptiveNotes(exhibitions,tours,otherServices);
						}
						exhibitions = tours = otherServices = false;
					}
				}
			}
		}else{
			exhibitionToursAndOtherServicesDescriptiveNotes(exhibitions,tours,otherServices);
		}

		// eag/archguide/desc/repositories/repository/directions
		if (this.eag2012.getDirectionsValue() != null && this.eag2012.getDirectionsLang() != null
				&& this.eag2012.getCitationHref() != null ) {
			for (int i = 0; i < this.eag2012.getDirectionsValue().size(); i++) {
				List<String> directionsValueList = this.eag2012.getDirectionsValue().get(i);
				List<String> directionsLangList = this.eag2012.getDirectionsLang().get(i);
				Map<String,List<String>> citationHrefMap = this.eag2012.getCitationHref().get(i);

				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> citationHrefIt = citationHrefMap.keySet().iterator();
				while (citationHrefIt.hasNext()) {
					List<String> citationHrefList = citationHrefMap.get(citationHrefIt.next());

					for (int j = 0; j < directionsValueList.size(); j++) {
						Directions directions = new Directions();
						directions.getContent().add(directionsValueList.get(j));
						// eag/archguide/desc/repositories/repository/directions/lang
						if (!Eag2012.OPTION_NONE.equalsIgnoreCase(directionsLangList.get(j))) {
							directions.setLang(directionsLangList.get(j));
						}
						// eag/archguide/desc/repositories/repository/directions/citation/href
						Citation citation = new Citation();
						citation.setHref(citationHrefList.get(j));
	
						if((directionsValueList.get(j)!=null && !directionsValueList.get(j).isEmpty()) || (citation.getHref()!=null && !citation.getHref().isEmpty())){
							directions.getContent().add(citation);
							repository.getDirections().add(directions);
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/repositorhist/descriptiveNote &&
		// eag/archguide/desc/repositories/repository/buildinginfo/building/descriptiveNote &&
		// eag/archguide/desc/repositories/repository/services/searchroom/researchServices/descriptiveNote &&
		// eag/archguide/desc/repositories/repository/services/recreationalServices/refreshment/descriptiveNote &&
		// eag/archguide/desc/repositories/repository/holdings/descriptiveNote
		getAllDescriptiveNote();

		// eag/archguide/desc/repositories/repository/repositorfound/rule &&  repositorsup/rule
		getAllRules();

		// eag/archguide/desc/repositories/repository/adminhierarchy
		if (this.eag2012.getAdminunitValue() != null
				&& this.eag2012.getAdminunitLang() != null) {
			for (int i = 0; i < this.eag2012.getAdminunitValue().size(); i++) {
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				List<String> valuesList = this.eag2012.getAdminunitValue().get(i);
				List<String> langsList = this.eag2012.getAdminunitLang().get(i);
				for (int j = 0; j < valuesList.size(); j++) {
					if (valuesList.get(j) != null && !valuesList.get(j).isEmpty()) {
						if (repository.getAdminhierarchy() == null) {
							repository.setAdminhierarchy(new Adminhierarchy());
						}
	
						Adminunit adminunit = new Adminunit();
						adminunit.setContent(valuesList.get(j));
						if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langsList.get(j))) {
							adminunit.setLang(langsList.get(j));
						}
	
						repository.getAdminhierarchy().getAdminunit().add(adminunit);
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/holdings

		// eag/archguide/desc/repositories/repository/timetable/opening
		if (this.eag2012.getOpeningValue() != null) {
			for (int i = 0; i < this.eag2012.getOpeningValue().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getOpeningValue().get(i);
				Map<String, List<String>> tabsLangMap = null;
				if (this.eag2012.getOpeningLang() != null && !this.eag2012.getOpeningLang().isEmpty()) {
					tabsLangMap = this.eag2012.getOpeningLang().get(i);
				}
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getTimetable() == null) {
					repository.setTimetable(new Timetable());
				}
				Iterator<String> openingValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> openingLangIt = null;
				if (tabsLangMap != null) {
					openingLangIt = tabsLangMap.keySet().iterator();
				}
				while (openingValueIt.hasNext()) {
					String openingValueKey = openingValueIt.next();
					String openingLangKey = (openingLangIt!=null && openingLangIt.hasNext())?openingLangIt.next():null;
					
					List<String> openingValueList = tabsValueMap.get(openingValueKey);
					List<String> openingLangList = null;
					if (openingLangIt != null) {
						openingLangList = (openingLangKey!=null)?tabsLangMap.get(openingLangKey):null;
					}
					for (int j = 0; j < openingValueList.size(); j++) {
						boolean found = false;
						if (!repository.getTimetable().getOpening().isEmpty()) {
							for (int k = 0;!found && k < repository.getTimetable().getOpening().size(); k++) {
								Opening opening = repository.getTimetable().getOpening().get(k);
								if (opening.getContent()!=null && 
										opening.getContent().equalsIgnoreCase(openingValueList.get(j)) && (
										(opening.getLang()!=null && opening.getLang().equalsIgnoreCase(openingLangList.get(j))) || 
										(opening.getLang()==null && (openingLangList.size()<=j || openingLangList.get(j)==null || openingLangList.get(j).equalsIgnoreCase("none")))
										)) {
									found = true;
								}
							}
						}

						if (!found) {
							if (openingValueList.get(j) != null
									&& !openingValueList.get(j).isEmpty()) {
								Opening opening = new Opening();
								opening.setContent(openingValueList.get(j));
								if (openingLangList != null
										&& !Eag2012.OPTION_NONE.equalsIgnoreCase(openingLangList.get(j))) {
									opening.setLang(openingLangList.get(j));
								}
								repository.getTimetable().getOpening().add(opening);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/timetable/closing
		if (this.eag2012.getClosingStandardDate() != null) {
			for (int i = 0; i < this.eag2012.getClosingStandardDate().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getClosingStandardDate().get(i);
				Map<String, List<String>> tabsLangMap = null;
				if (this.eag2012.getClosingLang() != null && !this.eag2012.getClosingLang().isEmpty()) {
					tabsLangMap = this.eag2012.getClosingLang().get(i);
				}
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				if (repository.getTimetable() == null) {
					repository.setTimetable(new Timetable());
				}
				Iterator<String> closingValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> closingLangIt = null;
				if (tabsLangMap != null) {
					closingLangIt = tabsLangMap.keySet().iterator();
				}
				while (closingValueIt.hasNext()) {
					List<String> closingValueList = tabsValueMap.get(closingValueIt.next());
					List<String> closingLangList = null;
					if (closingLangIt != null) {
						closingLangList = tabsLangMap.get(closingLangIt.next());
					}
					for (int j = 0; j < closingValueList.size(); j++) {
						boolean found = false;
						if (!repository.getTimetable().getClosing().isEmpty()) {
							for (int k = 0;!found && k < repository.getTimetable().getClosing().size(); k++) {
								Closing closing = repository.getTimetable().getClosing().get(k);
								if (closing.getContent()!=null && 
										closing.getContent().equalsIgnoreCase(closingValueList.get(j)) && (
											(closing.getLang()!=null && closing.getLang()!=null && closing.getLang().equalsIgnoreCase(closingLangList.get(j)) ) ||
											(closing.getLang()==null && (closingLangList==null || closingLangList.size()<=j || closingLangList.get(j).equalsIgnoreCase("none") ) ) )
										) {
									found = true;
								}
							}
						}

						if (!found) {
							if (closingValueList.get(j) != null
									&& !closingValueList.get(j).isEmpty()) {
								Closing closing = new Closing();
								closing.setContent(closingValueList.get(j));
								if (closingLangList !=  null
										&& !Eag2012.OPTION_NONE.equalsIgnoreCase(closingLangList.get(j))) {
									closing.setLang(closingLangList.get(j));
								}
			
								repository.getTimetable().getClosing().add(closing);
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/access/question
		if (this.eag2012.getAccessQuestion() != null) {
			for (int i = 0; i < this.eag2012.getAccessQuestion().size(); i++) {
				Map<String, String> tabsQuestionMap = this.eag2012.getAccessQuestion().get(i);
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
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

		// eag/archguide/desc/repositories/repository/access/restaccess
		if (this.eag2012.getRestaccessValue() != null) {
			for (int i = 0; i < this.eag2012.getRestaccessValue().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getRestaccessValue().get(i);
				Map<String, List<String>> tabsLangMap = null;
				if (this.eag2012.getRestaccessLang() != null && !this.eag2012.getRestaccessLang().isEmpty()) {
					tabsLangMap = this.eag2012.getRestaccessLang().get(i);
				}
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> accessValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> accessLangIt = null;
				if (tabsLangMap != null) {
					accessLangIt = tabsLangMap.keySet().iterator();
				}
				while (accessValueIt.hasNext()) {
					String accessValueKey = accessValueIt.next();

					// Rest of tabs.
//					if (!accessValueKey.equalsIgnoreCase(Eag2012.TAB_YOUR_INSTITUTION)) {
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
//					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/access/termsOfUse
		if (this.eag2012.getTermsOfUseValue() != null && !this.eag2012.getTermsOfUseValue().isEmpty()
				&& this.eag2012.getTermsOfUseLang() != null && !this.eag2012.getTermsOfUseLang().isEmpty()
				&& this.eag2012.getTermsOfUseHref() != null && !this.eag2012.getTermsOfUseHref().isEmpty()) {
			for (int i = 0; i < this.eag2012.getTermsOfUseValue().size(); i++) {
				List<String> termsOfUseValueList = this.eag2012.getTermsOfUseValue().get(i);
				List<String> termsOfUseLangList = this.eag2012.getTermsOfUseLang().get(i);
				List<String> termsOfUseHrefList = this.eag2012.getTermsOfUseHref().get(i);

				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

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

		// eag/archguide/desc/repositories/repository/accessibility/question
		if (this.eag2012.getAccessibilityQuestion() != null) {
			for (int i = 0; i < this.eag2012.getAccessibilityQuestion().size(); i++) {
				Map<String, String> tabsQuestionMap = this.eag2012.getAccessibilityQuestion().get(i);
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				Iterator<String> questionValueIt = tabsQuestionMap.keySet().iterator();
				while (questionValueIt.hasNext()) {
					String key = questionValueIt.next();

					// Rest of tabs.
					if (!Eag2012.TAB_YOUR_INSTITUTION.equalsIgnoreCase(key)) {
						Accessibility accessibility = new Accessibility();
						accessibility.setQuestion(tabsQuestionMap.get(key));
						repository.getAccessibility().add(accessibility);
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/accessibility
		if (this.eag2012.getAccessibilityValue() != null && this.eag2012.getAccessibilityLang() != null) {
			for (int i = 0; i < this.eag2012.getAccessibilityValue().size(); i++) {
				Map<String, List<String>> tabsValueMap = this.eag2012.getAccessibilityValue().get(i);
				Map<String, List<String>> tabsLangMap = null;
				if (this.eag2012.getAccessibilityLang() != null && !this.eag2012.getAccessibilityLang().isEmpty()) {
					tabsLangMap = this.eag2012.getAccessibilityLang().get(i);
				}
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				Iterator<String> accessibilityValueIt = tabsValueMap.keySet().iterator();
				Iterator<String> accessibilityLangIt = null;
				if (tabsLangMap != null) {
					accessibilityLangIt = tabsLangMap.keySet().iterator();
				}
				while (accessibilityValueIt.hasNext()) {
					String accessibilityValueKey = accessibilityValueIt.next();

					// Rest of tabs.
//					if (!accessibilityValueKey.equalsIgnoreCase(Eag2012.TAB_YOUR_INSTITUTION)) {
						List<String> accessibilityValueList = tabsValueMap.get(accessibilityValueKey);
						List<String> accessibilityLangList = null;
						if (accessibilityLangIt != null) {
							accessibilityLangList = tabsLangMap.get(accessibilityLangIt.next());
						}
						for (int j = 0; j < accessibilityValueList.size(); j++) {
							boolean exit = false;
							if (accessibilityValueList.get(j) == null || accessibilityValueList.get(j).isEmpty()) {
								exit = true;
							}
							if(!exit){
								for(int k=0;k<repository.getAccessibility().size();k++){
									if(accessibilityValueList.get(j).equalsIgnoreCase(repository.getAccessibility().get(k).getContent())){
										exit = true;
									}
								}
								if(!exit){
									List<Accessibility> list = repository.getAccessibility();
									String question = null;
									if(list.size()>0){
										question = repository.getAccessibility().get(0).getQuestion();
										if(list.size()==1 && (list.get(0).getLang()==null || list.get(0).getLang().isEmpty())
												&& (list.get(0).getContent()==null || list.get(0).getContent().isEmpty())){
											list.clear();
										}
									}

									if (accessibilityValueList.get(j) != null
											&& !accessibilityValueList.get(j).isEmpty()) {
										Accessibility accessibility = new Accessibility();
										accessibility.setContent(accessibilityValueList.get(j));
										if (accessibilityLangList != null && !Eag2012.OPTION_NONE.equalsIgnoreCase(accessibilityLangList.get(j))) {
											accessibility.setLang(accessibilityLangList.get(j));
										}
										accessibility.setQuestion(question);
										list.add(accessibility);
										repository.setAccessibility(list);
									}
								}
							}
						}
//					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/searchroom
		getAllNums();

		// eag/archguide/desc/repositories/repository/services/searchroom/photographAllowance
		if (this.eag2012.getPhotographAllowanceValue() != null) {
			for (int i = 0; i < this.eag2012.getPhotographAllowanceValue().size(); i++) {
				String value = this.eag2012.getPhotographAllowanceValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				if (Eag2012.OPTION_NONE.equalsIgnoreCase(value)) {
					break;
				} else if (Eag2012.OPTION_DEPENDING.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_DEPENDING_TEXT;
				} else if (Eag2012.OPTION_WITHOUT.equalsIgnoreCase(value)) {
					value = Eag2012.OPTION_WITHOUT_TEXT;
				}

				if (repository.getServices() == null) {
					repository.setServices(new Services());
				}
				if (repository.getServices().getSearchroom() == null) {
					repository.getServices().setSearchroom(new Searchroom());
				}
				PhotographAllowance photographAllowance = new PhotographAllowance();
				photographAllowance.setValue(value);

				repository.getServices().getSearchroom().setPhotographAllowance(photographAllowance);
			}
		}

		// eag/archguide/desc/repositories/repository/services/searchroom/readersTicket
		if (this.eag2012.getReadersTicketValue() != null && this.eag2012.getReadersTicketHref() != null
				&& this.eag2012.getReadersTicketLang() != null) {
			for (int i = 0; i < this.eag2012.getReadersTicketValue().size(); i++) {
				List<String> valueList = this.eag2012.getReadersTicketValue().get(i);
				List<String> hrefList = this.eag2012.getReadersTicketHref().get(i);
				List<String> langList = this.eag2012.getReadersTicketLang().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				for (int j = 0; j < valueList.size(); j++) {
					if ((valueList.get(j) != null && !valueList.get(j).isEmpty())
							|| (hrefList.get(j) != null && !hrefList.get(j).isEmpty())) {
						if (repository.getServices() == null) {
							repository.setServices(new Services());
						}
						if (repository.getServices().getSearchroom() == null) {
							repository.getServices().setSearchroom(new Searchroom());
						}
	
						ReadersTicket readersTicket = new ReadersTicket();
						readersTicket.setContent(valueList.get(j));

						if (hrefList.get(j) != null && !hrefList.get(j).isEmpty()) {
							readersTicket.setHref(hrefList.get(j));
						}

						if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(j))) {
							readersTicket.setLang(langList.get(j));
						}
	
						repository.getServices().getSearchroom().getReadersTicket().add(readersTicket);
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/searchroom/advancedOrders
		if (this.eag2012.getAdvancedOrdersValue() != null && this.eag2012.getAdvancedOrdersHref() != null
				&& this.eag2012.getAdvancedOrdersLang() != null) {
			for (int i = 0; i < this.eag2012.getAdvancedOrdersValue().size(); i++) {
				List<String> valueList = this.eag2012.getAdvancedOrdersValue().get(i);
				List<String> hrefList = this.eag2012.getAdvancedOrdersHref().get(i);
				List<String> langList = this.eag2012.getAdvancedOrdersLang().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				for (int j = 0; j < valueList.size(); j++) {
					if ((valueList.get(j) != null && !valueList.get(j).isEmpty())
							|| (hrefList.get(j) != null && !hrefList.get(j).isEmpty())) {
						if (repository.getServices() == null) {
							repository.setServices(new Services());
						}
						if (repository.getServices().getSearchroom() == null) {
							repository.getServices().setSearchroom(new Searchroom());
						}
	
						AdvancedOrders advancedOrders = new AdvancedOrders();
						advancedOrders.setContent(valueList.get(j));

						if (hrefList.get(j) != null && !hrefList.get(j).isEmpty()) {
							advancedOrders.setHref(hrefList.get(j));
						}

						if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(j))) {
							advancedOrders.setLang(langList.get(j));
						}
	
						repository.getServices().getSearchroom().getAdvancedOrders().add(advancedOrders);
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/library
		if (this.eag2012.getNumValue() != null) {
			for (int i = 0; i < this.eag2012.getNumValue().size(); i++) {
				Map<String, Map<String, Map<String, List<String>>>> tabsMap = this.eag2012.getNumValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					if (Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabKey)) {
						Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
						Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
						while (sectionsIt.hasNext()) {
							String sectionKey = sectionsIt.next();
							if (Eag2012.LIBRARY.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									// eag/archguide/desc/repositories/repository/services/library/monographicpub
									if (Eag2012.MONOGRAPHIC_PUBLICATION.equalsIgnoreCase(subSectionKey)) {
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
												break;
											}
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getLibrary() == null) {
												repository.getServices().setLibrary(new Library());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(Eag2012.UNIT_BOOK);

											Monographicpub monographicpub = new Monographicpub();
											monographicpub.setNum(num);

											repository.getServices().getLibrary().setMonographicpub(monographicpub);
										}
									}
									// eag/archguide/desc/repositories/repository/services/library/serialpub
									if (Eag2012.SERIAL_PUBLICATION.equalsIgnoreCase(subSectionKey)) {
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
												break;
											}
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getLibrary() == null) {
												repository.getServices().setLibrary(new Library());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(Eag2012.UNIT_TITLE);

											Serialpub serialpub = new Serialpub();
											serialpub.setNum(num);

											repository.getServices().getLibrary().setSerialpub(serialpub);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/library/question
		if (this.eag2012.getLibraryQuestion() != null) {
			for (int i = 0; i < this.eag2012.getLibraryQuestion().size(); i++) {
				if(!this.eag2012.getLibraryQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)){
					// Repository
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					if (repository.getServices() == null) {
						repository.setServices(new Services());
					}
					if (repository.getServices().getLibrary() == null) {
						repository.getServices().setLibrary(new Library());
					}
				
					repository.getServices().getLibrary().setQuestion(this.eag2012.getLibraryQuestion().get(i));
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/internetAccess
		if (this.eag2012.getInternetAccessQuestion() != null) {
			for (int i = 0; i < this.eag2012.getInternetAccessQuestion().size(); i++) {
				if(!this.eag2012.getInternetAccessQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)){
					// Repository
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					if (repository.getServices() == null) {
						repository.setServices(new Services());
					}
					if (repository.getServices().getInternetAccess() == null) {
						repository.getServices().setInternetAccess(new InternetAccess());
					}

					repository.getServices().getInternetAccess().setQuestion(this.eag2012.getInternetAccessQuestion().get(i));
					getDescriptiveNote(repository.getServices().getInternetAccess(), -1, i);
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/techservices/restorationlab
		if (this.eag2012.getRestorationlabQuestion() != null) {
			for (int i = 0; i < this.eag2012.getRestorationlabQuestion().size(); i++) {
				if(!this.eag2012.getRestorationlabQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)){
					// Repository
					Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
					if (repository.getServices() == null) {
						repository.setServices(new Services());
					}
					if (repository.getServices().getTechservices() == null) {
						repository.getServices().setTechservices(new Techservices());
					}
					if (repository.getServices().getTechservices().getRestorationlab() == null) {
						repository.getServices().getTechservices().setRestorationlab(new Restorationlab());
					}

					repository.getServices().getTechservices().getRestorationlab().setQuestion(this.eag2012.getRestorationlabQuestion().get(i));
					getDescriptiveNote(repository.getServices().getTechservices().getRestorationlab(), -1, i);
				}
			}
		}

		// eag/archguide/desc/repositories/repository/services/techservices/reproductionser
		if (this.eag2012.getReproductionserQuestion() != null) {
			for (int i = 0; i < this.eag2012.getReproductionserQuestion().size(); i++) {
				// Repository
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
				
				if(this.eag2012.getReproductionserQuestion().get(i)!=null && 
						(!this.eag2012.getReproductionserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE) || 
						!this.eag2012.getMicroformserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE) || 
						!this.eag2012.getPhotographserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE) || 
						!this.eag2012.getDigitalserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE) || 
						!this.eag2012.getPhotocopyserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)
								)){
					if (repository.getServices() == null) {
						repository.setServices(new Services());
					}
					if (repository.getServices().getTechservices() == null) {
						repository.getServices().setTechservices(new Techservices());
					}
					if (repository.getServices().getTechservices().getReproductionser() == null) {
						repository.getServices().getTechservices().setReproductionser(new Reproductionser());
					}
					// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/question
					if(!this.eag2012.getReproductionserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)){
						repository.getServices().getTechservices().getReproductionser().setQuestion(this.eag2012.getReproductionserQuestion().get(i));
					}
					
					// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/descriptiveNote
					getDescriptiveNote(repository.getServices().getTechservices().getReproductionser(), -1, i);

					// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/microformser
					if (this.eag2012.getMicroformserQuestion() != null && !this.eag2012.getMicroformserQuestion().isEmpty() && !this.eag2012.getMicroformserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
						if (repository.getServices().getTechservices().getReproductionser().getMicroformser() == null) {
							repository.getServices().getTechservices().getReproductionser().setMicroformser(new Microformser());
						}
						repository.getServices().getTechservices().getReproductionser().getMicroformser().setQuestion(this.eag2012.getMicroformserQuestion().get(i));
					}

					// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/photographser
					if (this.eag2012.getPhotographserQuestion() != null && !this.eag2012.getPhotographserQuestion().isEmpty() && !this.eag2012.getPhotographserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
						if (repository.getServices().getTechservices().getReproductionser().getPhotographser() == null) {
							repository.getServices().getTechservices().getReproductionser().setPhotographser(new Photographser());
						}
						repository.getServices().getTechservices().getReproductionser().getPhotographser().setQuestion(this.eag2012.getPhotographserQuestion().get(i));
					}

					// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/digitalser
					if (this.eag2012.getDigitalserQuestion() != null && !this.eag2012.getDigitalserQuestion().isEmpty() && !this.eag2012.getDigitalserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
						if (repository.getServices().getTechservices().getReproductionser().getDigitalser() == null) {
							repository.getServices().getTechservices().getReproductionser().setDigitalser(new Digitalser());
						}
						repository.getServices().getTechservices().getReproductionser().getDigitalser().setQuestion(this.eag2012.getDigitalserQuestion().get(i));
					}

					// eag/archguide/desc/repositories/repository/services/techservices/reproductionser/photocopyser
					if (this.eag2012.getPhotocopyserQuestion() != null && !this.eag2012.getPhotocopyserQuestion().isEmpty() && !this.eag2012.getPhotocopyserQuestion().get(i).equalsIgnoreCase(Eag2012.OPTION_NONE)) {
						if(repository.getServices().getTechservices().getReproductionser()==null){
							repository.getServices().getTechservices().setReproductionser(new Reproductionser());
						}
						if (repository.getServices().getTechservices().getReproductionser().getPhotocopyser() == null) {
							repository.getServices().getTechservices().getReproductionser().setPhotocopyser(new Photocopyser());
						}
						repository.getServices().getTechservices().getReproductionser().getPhotocopyser().setQuestion(this.eag2012.getPhotocopyserQuestion().get(i));
					}
				}
			}
		}

		// eag/archguide/desc/repositories/repository/descriptivenote

	}

	private void exhibitionToursAndOtherServicesDescriptiveNotes(boolean exhibitions,boolean tours,boolean otherServices) {
		for(int i=0;i<this.eag.getArchguide().getDesc().getRepositories().getRepository().size();i++){
			Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
			if (this.eag2012.getDescriptiveNotePValue() != null && this.eag2012.getDescriptiveNotePLang() != null) {
//				for (int x = 0; x < this.eag2012.getDescriptiveNotePValue().size(); x++) {
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
									if (valuesList.get(k) == null || valuesList.get(k).isEmpty()) {
										break;
									}
									// eag/archguide/desc/repositories/repository/services/recreationalServices/exhibition/descriptiveNote/P
									if (!exhibitions && Eag2012.EXHIBITION.equalsIgnoreCase(sectionValueKey) && Eag2012.EXHIBITION.equalsIgnoreCase(sectionLangKey)) {
										if (repository.getServices() == null) {
											repository.setServices(new Services());
										}
										if (repository.getServices().getRecreationalServices() == null) {
											repository.getServices().setRecreationalServices(new RecreationalServices());
										}
										if (repository.getServices().getRecreationalServices().getExhibition().size() < (k + 1)) {
											repository.getServices().getRecreationalServices().getExhibition().add(new Exhibition());
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
											repository.getServices().getRecreationalServices().getToursSessions().add(new ToursSessions());
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
											repository.getServices().getRecreationalServices().getOtherServices().add(new OtherServices());
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

	/**
	 * Method to recover dates.
	 */
	private void getAllDates() {
		if (this.eag2012.getDateStandardDate() != null) {
			for (int i = 0; i < this.eag2012.getDateStandardDate().size(); i++) {
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueMap = this.eag2012.getDateStandardDate().get(i);
				Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
				while (tabsValueIt.hasNext()) {
					String tabValueKey = tabsValueIt.next();
					Map<String, Map<String, List<List<String>>>> sectionsValueMap = tabsValueMap.get(tabValueKey);
					Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
					if (Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueKey)) {
						while (sectionsValueIt.hasNext()) {
							String sectionValueKey = sectionsValueIt.next();
							Map<String, List<List<String>>> subsectionsValueMap = sectionsValueMap.get(sectionValueKey);
							Iterator<String> subsectionsValueIt = subsectionsValueMap.keySet().iterator();
							if (Eag2012.REPOSITORHIST.equalsIgnoreCase(sectionValueKey)) {
								while (subsectionsValueIt.hasNext()) {
									String subsectionValueKey = subsectionsValueIt.next();
									List<List<String>> valuesList = subsectionsValueMap.get(subsectionValueKey);
									for (int j = 0; j < valuesList.size(); j++) {
										List<String> valueList = valuesList.get(j);
										for (int k = 0; k < valuesList.size(); k++) {
											if (valueList.get(k) != null && !valueList.get(k).isEmpty()) {
												Date date = new Date();
												String valueStandardDate = parseDate(valueList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													date.setStandardDate(valueStandardDate);
												}
												date.setContent(valueList.get(k));
		
												if (Eag2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionValueKey)) {
													// eag/archguide/desc/repositories/repository/repositorfuond/date
													if (repository.getRepositorfound() == null) {
														repository.setRepositorfound(new Repositorfound());
													}
													repository.getRepositorfound().setDate(date);
												} else if (Eag2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionValueKey)) {
													// eag/archguide/desc/repositories/repository/repositorsup/date
													if (repository.getRepositorsup() == null) {
														repository.setRepositorsup(new Repositorsup());
													}
													repository.getRepositorsup().setDate(date);
												}
											}
										}
									}
								}
							} else if (Eag2012.HOLDINGS.equalsIgnoreCase(sectionValueKey)) {
								while (subsectionsValueIt.hasNext()) {
									String subsectionValueKey = subsectionsValueIt.next();
									List<List<String>> valuesList = subsectionsValueMap.get(subsectionValueKey);
									for (int j = 0; j < valuesList.size(); j++) {
										List<String> valueList = valuesList.get(j);
										for (int k = 0; k < valueList.size(); k++) {
											if (valueList.get(k) != null && !valueList.get(k).isEmpty()) {
												Date date = new Date();
												String valueStandardDate = parseDate(valueList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													date.setStandardDate(valueStandardDate);
												}
												date.setContent(valueList.get(k));
		
												if (Eag2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueKey)) {
													if (repository.getHoldings() == null) {
														repository.setHoldings(new Holdings());
													}
	
													if (repository.getHoldings().getDate() == null
															&& repository.getHoldings().getDateRange() == null
															&& repository.getHoldings().getDateSet() == null) {
														repository.getHoldings().setDate(date);
													} else {
														DateSet dateSet = null;
														if (repository.getHoldings().getDateSet() == null) {
															dateSet = new DateSet();
														} else {
															dateSet = repository.getHoldings().getDateSet();
														}
	
														// Recover previous single element.
														if (repository.getHoldings().getDate() != null) {
															Date previousDate = repository.getHoldings().getDate();
															repository.getHoldings().setDate(null);
															dateSet.getDateOrDateRange().add(previousDate);
														}
														if (repository.getHoldings().getDateRange() != null) {
															DateRange previousDateRange = repository.getHoldings().getDateRange();
															repository.getHoldings().setDateRange(null);
															dateSet.getDateOrDateRange().add(previousDateRange);
														}
	
														dateSet.getDateOrDateRange().add(date);
	
														repository.getHoldings().setDateSet(dateSet);
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

		if (this.eag2012.getFromDateStandardDate() != null
				&& this.eag2012.getToDateStandardDate() != null) {
			for (int i = 0; i < this.eag2012.getFromDateStandardDate().size(); i++) {
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueFromMap = this.eag2012.getFromDateStandardDate().get(i);
				Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueToMap = this.eag2012.getToDateStandardDate().get(i);
				Iterator<String> tabsValueFromIt = tabsValueFromMap.keySet().iterator();
				Iterator<String> tabsValueToIt = tabsValueToMap.keySet().iterator();
				while (tabsValueFromIt.hasNext()) {
					String tabValueFromKey = tabsValueFromIt.next();
					String tabValueToKey = tabsValueToIt.next();
					Map<String, Map<String, List<List<String>>>> sectionsValueFromMap = tabsValueFromMap.get(tabValueFromKey);
					Map<String, Map<String, List<List<String>>>> sectionsValueToMap = tabsValueToMap.get(tabValueToKey);
					Iterator<String> sectionsValueFromIt = sectionsValueFromMap.keySet().iterator();
					Iterator<String> sectionsValueToIt = sectionsValueToMap.keySet().iterator();
					if (Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueFromKey)
							&& Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueToKey)) {
						while (sectionsValueFromIt.hasNext()) {
							String sectionValueFromKey = sectionsValueFromIt.next();
							String sectionValueToKey = sectionsValueToIt.next();
							Map<String, List<List<String>>> subsectionsValueFromMap = sectionsValueFromMap.get(sectionValueFromKey);
							Map<String, List<List<String>>> subsectionsValueToMap = sectionsValueToMap.get(sectionValueToKey);
							Iterator<String> subsectionsValueFromIt = subsectionsValueFromMap.keySet().iterator();
							Iterator<String> subsectionsValueToIt = subsectionsValueToMap.keySet().iterator();
							while (subsectionsValueFromIt.hasNext()) {
								String subsectionValueFromKey = subsectionsValueFromIt.next();
								String subsectionValueToKey = subsectionsValueToIt.next();
								List<List<String>> valuesFromList = subsectionsValueFromMap.get(subsectionValueFromKey);
								List<List<String>> valuesToList = subsectionsValueToMap.get(subsectionValueToKey);
									if (Eag2012.HOLDINGS.equalsIgnoreCase(sectionValueFromKey)
											&& Eag2012.HOLDINGS.equalsIgnoreCase(sectionValueToKey)) {
									for (int j = 0; j < valuesFromList.size(); j++) {
										List<String> valueFromList = valuesFromList.get(j);
										List<String> valueToList = valuesToList.get(j);
										for (int k = 0; k < valueFromList.size(); k++) {
											if ((valueFromList.get(k) != null && !valueFromList.get(k).isEmpty())
													|| (valueToList.get(k) != null && !valueToList.get(k).isEmpty())){
												FromDate fromDate = new FromDate();
												String valueStandardDate = parseDate(valueFromList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													fromDate.setStandardDate(valueStandardDate);
												}
												fromDate.setContent(valueFromList.get(k));
		
												ToDate toDate = new ToDate();
											    valueStandardDate = parseDate(valueToList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													toDate.setStandardDate(valueStandardDate);
												}
												toDate.setContent(valueToList.get(k));
	
												DateRange dateRange = new DateRange();
												dateRange.setFromDate(fromDate);
												dateRange.setToDate(toDate);
		
												if (Eag2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueFromKey)
														&& Eag2012.HOLDING_SUBSECTION.equalsIgnoreCase(subsectionValueToKey)) {
													if (repository.getHoldings() == null) {
														repository.setHoldings(new Holdings());
													}
	
													if (repository.getHoldings().getDate() == null
															&& repository.getHoldings().getDateRange() == null
															&& repository.getHoldings().getDateSet() == null) {
														repository.getHoldings().setDateRange(dateRange);
													} else {
														DateSet dateSet = null;
														if (repository.getHoldings().getDateSet() == null) {
															dateSet = new DateSet();
														} else {
															dateSet = repository.getHoldings().getDateSet();
														}
	
														// Recover previous single element.
														if (repository.getHoldings().getDate() != null) {
															Date previousDate = repository.getHoldings().getDate();
															repository.getHoldings().setDate(null);
															dateSet.getDateOrDateRange().add(previousDate);
														}
														if (repository.getHoldings().getDateRange() != null) {
															DateRange previousDateRange = repository.getHoldings().getDateRange();
															repository.getHoldings().setDateRange(null);
															dateSet.getDateOrDateRange().add(previousDateRange);
														}
	
														dateSet.getDateOrDateRange().add(dateRange);
	
														repository.getHoldings().setDateSet(dateSet);
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
	}

	private String parseDate(String formatDate) {
		boolean pattern1 = Pattern.matches("\\d{4}", formatDate); //yyyy
		boolean pattern2 = Pattern.matches("\\d{4}[\\-\\./:\\s]\\d{2}", formatDate); //yyyy-MM
		boolean pattern3 = Pattern.matches("\\d{4}[\\-\\./:\\s]\\d{2}[\\-\\./:\\s]\\d{2}", formatDate); //yyyy-MM-dd
		boolean pattern4 = Pattern.matches("\\d{2}[\\-\\./:\\s]\\d{2}[\\-\\./:\\s]\\d{4}", formatDate); //dd-MM-yyyy
		if (pattern4){
			String yearStandardDate = formatDate.substring(6);
			String monthStandardDate = formatDate.substring(2,6);
			String dateStandardDate = formatDate.substring(0,2);
			String reverseString =yearStandardDate+monthStandardDate+dateStandardDate;
	         formatDate = formatDate.replaceAll(formatDate, reverseString);
		}
		if (pattern1){
			return formatDate;
		} else if (pattern2 || pattern3 || pattern4) {
			formatDate = formatDate.replaceAll("[\\./:\\s]", "-");
			return formatDate;
		}

		return null;
	}

	/**
	 * Method to recover descriptiveNote for research services.
	 */
	private void getAllDescriptiveNote() {
		if (this.eag.getArchguide().getDesc().getRepositories().getRepository() != null
				&& !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
			for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				// Recover descriptiveNote for research services.
				if (this.eag2012.getDescriptiveNotePValue() != null
						&& this.eag2012.getDescriptiveNotePLang() != null) {
//					for (int j = 0; j < this.eag2012.getDescriptiveNotePValue().size(); j++) {
					if (this.eag2012.getDescriptiveNotePValue().get(i) != null
							&& this.eag2012.getDescriptiveNotePLang().get(i) != null) {
						Map<String, Map<String, List<String>>> tabsValueMap = this.eag2012.getDescriptiveNotePValue().get(i);
						Map<String, Map<String, List<String>>> tabsLangMap = this.eag2012.getDescriptiveNotePLang().get(i);
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
									for (int k = 0; k < valuesList.size(); k++) {
										if (valuesList.get(k) != null && !valuesList.get(k).isEmpty()) {
											P p = new P();
											p.setContent(valuesList.get(k));
											if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(k))) {
												p.setLang(langList.get(k));
											}
	
											// eag/archguide/desc/repositories/repository/services/searchroom/researchServices/descriptiveNote/P
											if (Eag2012.RESEARCH_SERVICES.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.RESEARCH_SERVICES.equalsIgnoreCase(sectionLangKey)) {
												if (repository.getServices() == null) {
													repository.setServices(new Services());
												}
												if (repository.getServices().getSearchroom() == null) {
													repository.getServices().setSearchroom(new Searchroom());
												}
	
												ResearchServices researchServices = new ResearchServices();
												if (researchServices.getDescriptiveNote() == null) {
													researchServices.setDescriptiveNote(new DescriptiveNote());
												}
	
												researchServices.getDescriptiveNote().getP().add(p);
												repository.getServices().getSearchroom().getResearchServices().add(researchServices);
											} else  if (Eag2012.REFRESHMENT.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.REFRESHMENT.equalsIgnoreCase(sectionLangKey)) {
												// eag/archguide/desc/repositories/repository/services/recreationalSerices/refreshment/descriptiveNote/P
												if (repository.getServices() == null) {
													repository.setServices(new Services());
												}
												if (repository.getServices().getRecreationalServices() == null) {
													repository.getServices().setRecreationalServices(new RecreationalServices());
												}
	
												Refreshment refreshment = null;
												if (repository.getServices().getRecreationalServices().getRefreshment() == null) {
													refreshment = new Refreshment();
												} else {
													refreshment = repository.getServices().getRecreationalServices().getRefreshment();
												}
	
												if (refreshment.getDescriptiveNote() == null) {
													refreshment.setDescriptiveNote(new DescriptiveNote());
												}
	
												refreshment.getDescriptiveNote().getP().add(p);
	
												repository.getServices().getRecreationalServices().setRefreshment(refreshment);
											}
										}
									}
								}
							} else  if (Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabValueKey)
									&& Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabLangKey)) {
								Map<String, List<String>> sectionsValueMap = tabsValueMap.get(tabValueKey);
								Map<String, List<String>> sectionsLangMap = tabsLangMap.get(tabLangKey);
								Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
								Iterator<String> sectionsLangIt = sectionsLangMap.keySet().iterator();
								while (sectionsValueIt.hasNext()) {
									String sectionValueKey = sectionsValueIt.next();
									String sectionLangKey = sectionsLangIt.next();
									List<String> valuesList = sectionsValueMap.get(sectionValueKey);
									List<String> langList = sectionsLangMap.get(sectionLangKey);
									for (int k = 0; k < valuesList.size(); k++) {
										if (valuesList.get(k) != null && !valuesList.get(k).isEmpty()) {
											P p = new P();
											p.setContent(valuesList.get(k));
											if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langList.get(k))) {
												p.setLang(langList.get(k));
											}
	
											// eag/archguide/desc/repositories/repository/repositorhist/descriptiveNote/P
											if (Eag2012.REPOSITORHIST.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.REPOSITORHIST.equalsIgnoreCase(sectionLangKey)) {
												Repositorhist repositorhist = null;
												if (repository.getRepositorhist() == null) {
													repositorhist = new Repositorhist();
												} else {
													repositorhist = repository.getRepositorhist();
												}
												if (repositorhist.getDescriptiveNote() == null) {
													repositorhist.setDescriptiveNote(new DescriptiveNote());
												}
	
												repositorhist.getDescriptiveNote().getP().add(p);
	
												repository.setRepositorhist(repositorhist);
											} else if (Eag2012.BUILDING.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.BUILDING.equalsIgnoreCase(sectionLangKey)) {
												// eag/archguide/desc/repositories/repository/buildinginfo/building/descriptiveNote/P
												Building building = null; 
												if (repository.getBuildinginfo() == null) {
													repository.setBuildinginfo(new Buildinginfo());
													building = new Building();
												} else {
													building = repository.getBuildinginfo().getBuilding();
												}
	
												if (building.getDescriptiveNote() == null) {
													building.setDescriptiveNote(new DescriptiveNote());
												}
	
												building.getDescriptiveNote().getP().add(p);
	
												repository.getBuildinginfo().setBuilding(building);
											} else if (Eag2012.HOLDINGS.equalsIgnoreCase(sectionValueKey)
													&& Eag2012.HOLDINGS.equalsIgnoreCase(sectionLangKey)) {
												// eag/archguide/desc/repositories/repository/holdings/descriptiveNote/P
												Holdings holdings = null;
												if (repository.getHoldings() == null) {
													holdings = new Holdings();
												} else {
													holdings = repository.getHoldings();
												}
	
												if (holdings.getDescriptiveNote() == null) {
													holdings.setDescriptiveNote(new DescriptiveNote());
												}
	
												holdings.getDescriptiveNote().getP().add(p);
	
												repository.setHoldings(holdings);
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

	/**
	 * Method to recover all num elements.
	 */
	private void getAllNums() {
		if (this.eag2012.getNumValue() != null) {
			for (int i = 0; i < this.eag2012.getNumValue().size(); i++) {
				Map<String, Map<String, Map<String, List<String>>>> tabsMap = this.eag2012.getNumValue().get(i);
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Iterator<String> tabsIt = tabsMap.keySet().iterator();
				while (tabsIt.hasNext()) {
					String tabKey = tabsIt.next();
					if (Eag2012.TAB_ACCESS_AND_SERVICES.equalsIgnoreCase(tabKey)) {
						Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
						Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
						while (sectionsIt.hasNext()) {
							String sectionKey = sectionsIt.next();
							if (Eag2012.SEARCHROOM.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									// eag/archguide/desc/repositories/repository/services/searchroom/workPlaces
									if (Eag2012.WORKING_PLACES.equalsIgnoreCase(subSectionKey)) {
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
												break;
											}
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getSearchroom() == null) {
												repository.getServices().setSearchroom(new Searchroom());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(Eag2012.UNIT_SITE);

											WorkPlaces workPlaces = new WorkPlaces();
											workPlaces.setNum(num);

											repository.getServices().getSearchroom().setWorkPlaces(workPlaces);
										}
									} else if (Eag2012.COMPUTER_PLACES.equalsIgnoreCase(subSectionKey)) {
										// eag/archguide/desc/repositories/repository/services/searchroom/ComputerPlaces
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
												break;
											}
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getSearchroom() == null) {
												repository.getServices().setSearchroom(new Searchroom());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(Eag2012.UNIT_SITE);

											ComputerPlaces computerPlaces = new ComputerPlaces();
											computerPlaces.setNum(num);

											// eag/archguide/desc/repositories/repository/services/searchroom/ComputerPlaces/descriptiveNote
											getDescriptiveNote(computerPlaces, -1, i);

											repository.getServices().getSearchroom().setComputerPlaces(computerPlaces);
										}
									} else if (Eag2012.MICROFILM.equalsIgnoreCase(subSectionKey)) {
										// eag/archguide/desc/repositories/repository/services/searchroom/microfilmPlaces
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
												break;
											}
											if (repository.getServices() == null) {
												repository.setServices(new Services());
											}
											if (repository.getServices().getSearchroom() == null) {
												repository.getServices().setSearchroom(new Searchroom());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(Eag2012.UNIT_SITE);

											MicrofilmPlaces microfilmPlaces = new MicrofilmPlaces();
											microfilmPlaces.setNum(num);

											repository.getServices().getSearchroom().setMicrofilmPlaces(microfilmPlaces);
										}
									}
								}
							}
						}
					} else if (Eag2012.TAB_DESCRIPTION.equalsIgnoreCase(tabKey)) {
						Map<String, Map<String, List<String>>> sectionsMap = tabsMap.get(tabKey);
						Iterator<String> sectionsIt = sectionsMap.keySet().iterator();
						while (sectionsIt.hasNext()) {
							String sectionKey = sectionsIt.next();
							if (Eag2012.BUILDING.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									// eag/archguide/desc/repositories/repository/buildinginfo/repositorarea
									if (Eag2012.BUILDING_AREA.equalsIgnoreCase(subSectionKey)) {
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
												break;
											}
											if (repository.getBuildinginfo() == null) {
												repository.setBuildinginfo(new Buildinginfo());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(Eag2012.UNIT_SQUARE_METRE);

											Repositorarea repositorarea = new Repositorarea();
											repositorarea.setNum(num);

											repository.getBuildinginfo().setRepositorarea(repositorarea);
										}
									} else if (Eag2012.BUILDING_LENGTH.equalsIgnoreCase(subSectionKey)) {
										// eag/archguide/desc/repositories/repository/buildinginfo/lengthshelf
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
												break;
											}
											if (repository.getBuildinginfo() == null) {
												repository.setBuildinginfo(new Buildinginfo());
											}

											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(Eag2012.UNIT_LINEAR_METRE);

											Lengthshelf lengthshelf = new Lengthshelf();
											lengthshelf.setNum(num);

											repository.getBuildinginfo().setLengthshelf(lengthshelf);
										}
									}
								}
							} else if (Eag2012.HOLDINGS.equalsIgnoreCase(sectionKey)) {
								Map<String, List<String>> subSectionsMap = sectionsMap.get(sectionKey);
								Iterator<String> subSectionsIt = subSectionsMap.keySet().iterator();
								while (subSectionsIt.hasNext()) {
									String subSectionKey = subSectionsIt.next();
									if (Eag2012.HOLDING_EXTENT.equalsIgnoreCase(subSectionKey)) {
										// eag/archguide/desc/repositories/repository/holdings/extent
										List<String> valueList = subSectionsMap.get(subSectionKey);
										for (int j = 0; j < valueList.size(); j++) {
											if (valueList.get(j) == null || valueList.get(j).isEmpty()) {
												break;
											}
											if (repository.getHoldings() == null) {
												repository.setHoldings(new Holdings());
											}
		
											Num num = new Num();
											num.setContent(valueList.get(j));
											num.setUnit(Eag2012.UNIT_LINEAR_METRE);
		
											Extent extent = new Extent();
											extent.setNum(num);
		
											repository.getHoldings().setExtent(extent);
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
	 * Method to recover all rule elements.
	 */
	private void getAllRules() {
		if (this.eag2012.getRuleValue() != null
				&& this.eag2012.getRuleLang() != null) {
			for (int i = 0; i < this.eag2012.getNumValue().size(); i++) {
				// Repository.
				Repository repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);

				Map<String, List<String>> subsectionValueMap = this.eag2012.getRuleValue().get(i);
				Map<String, List<String>> subsectionLangMap = this.eag2012.getRuleLang().get(i);

				Iterator<String> subsectionValueIt = subsectionValueMap.keySet().iterator();
				Iterator<String> subsectionLangIt = subsectionLangMap.keySet().iterator();
				while (subsectionValueIt.hasNext()) {
					String subsectionValueKey = subsectionValueIt.next();
					String subsectionLangKey = subsectionLangIt.next();
					List<String> valuesList = subsectionValueMap.get(subsectionValueKey);
					List<String> langsList = subsectionLangMap.get(subsectionLangKey);
					for (int j = 0; j < valuesList.size(); j++) {
						if (valuesList.get(j) != null && !valuesList.get(j).isEmpty()) {
							// eag/archguide/desc/repositories/repository/repositorfound/rule
							if (Eag2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionValueKey)
									&& Eag2012.REPOSITOR_FOUND.equalsIgnoreCase(subsectionLangKey)) {
								if (repository.getRepositorfound() == null) {
									repository.setRepositorfound(new Repositorfound());
								}
	
								Rule rule = new Rule();
								rule.setContent(valuesList.get(j));
								if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langsList.get(j))) {
									rule.setLang(langsList.get(j));
								}
	
								repository.getRepositorfound().getRule().add(rule);
							}
							// eag/archguide/desc/repositories/repository/repositorsup/rule
							if (Eag2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionValueKey)
									&& Eag2012.REPOSITOR_SUP.equalsIgnoreCase(subsectionLangKey)) {
								if (repository.getRepositorsup() == null) {
									repository.setRepositorsup(new Repositorsup());
								}
	
								Rule rule = new Rule();
								rule.setContent(valuesList.get(j));
								if (!Eag2012.OPTION_NONE.equalsIgnoreCase(langsList.get(j))) {
									rule.setLang(langsList.get(j));
								}
	
								repository.getRepositorsup().getRule().add(rule);
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
//			for (int i = 0; i < this.eag2012.getDescriptiveNotePValue().size(); i++) {
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
//							for (int j = 0; j < valuesList.size(); j++) {
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
//						}
					}
				}
			}
		}
	}

	/**
	 * Method to recover all dates for the nonpreform passed.
	 */
	private void getNompreformDates(final Nonpreform nonpreform, final int i) {
		if (this.eag2012.getDateStandardDate() != null) {
			// Main institution date.
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueMap = this.eag2012.getDateStandardDate().get(0);
			Iterator<String> tabsValueIt = tabsValueMap.keySet().iterator();
			while (tabsValueIt.hasNext()) {
				String tabValueKey = tabsValueIt.next();
				Map<String, Map<String, List<List<String>>>> sectionsValueMap = tabsValueMap.get(tabValueKey);
				Iterator<String> sectionsValueIt = sectionsValueMap.keySet().iterator();
				if (Eag2012.TAB_IDENTITY.equalsIgnoreCase(tabValueKey)) {
					while (sectionsValueIt.hasNext()) {
						String sectionValueKey = sectionsValueIt.next();
						Map<String, List<List<String>>> subsectionsValueMap = sectionsValueMap.get(sectionValueKey);
						Iterator<String> subsectionsValueIt = subsectionsValueMap.keySet().iterator();
						if (Eag2012.ROOT.equalsIgnoreCase(sectionValueKey)) {
							while (subsectionsValueIt.hasNext()) {
								String subsectionValueKey = subsectionsValueIt.next();
								List<List<String>> valuesList = subsectionsValueMap.get(subsectionValueKey);
								for (int j = 0; j < valuesList.size(); j++) {
									if (j == i) {
										List<String> valueList = valuesList.get(j);
										for (int k = 0; k < valueList.size(); k++) {
											if (valueList.get(k) != null && !valueList.get(k).isEmpty()) {
												Date date = new Date();
												String valueStandardDate = parseDate(valueList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													date.setStandardDate(valueStandardDate);
												}
												date.setContent(valueList.get(k));
	
												if (Eag2012.ROOT_SUBSECTION.equalsIgnoreCase(subsectionValueKey)) {
		
													List<Object> nonpreformObjectList = nonpreform.getContent();
													UseDates useDates = null;
													int index = 0;
													for (int l = 0; l < nonpreformObjectList.size(); l++) {
														if (nonpreformObjectList.get(l) instanceof UseDates) {
															useDates = (UseDates) nonpreformObjectList.get(l);
															index = l;
														}
													}
													boolean emptyUseDates = false;
													if (useDates == null) {
														useDates = new UseDates();
														emptyUseDates = true;
													}
	
													if (emptyUseDates) {
														useDates.setDate(date);
													} else {
														DateSet dateSet = null;
														if (useDates.getDateSet() == null) {
															dateSet = new DateSet();
														} else {
															dateSet = useDates.getDateSet();
														}
	
														// Recover previous single element.
														if (useDates.getDate() != null) {
															Date previousDate = useDates.getDate();
															useDates.setDate(null);
															dateSet.getDateOrDateRange().add(previousDate);
														}
														if (useDates.getDateRange() != null) {
															DateRange previousDateRange = useDates.getDateRange();
															useDates.setDateRange(null);
															dateSet.getDateOrDateRange().add(previousDateRange);
														}
			
														dateSet.getDateOrDateRange().add(date);
														useDates.setDateSet(dateSet);
													}
		
													if (index == 0) {
														nonpreform.getContent().add(useDates);
													} else {
														nonpreform.getContent().set(index, useDates);
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

		if (this.eag2012.getFromDateStandardDate() != null
				&& this.eag2012.getToDateStandardDate() != null) {
			// Main institution dateRange.
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueFromMap = this.eag2012.getFromDateStandardDate().get(0);
			Map<String, Map<String, Map<String, List<List<String>>>>> tabsValueToMap = this.eag2012.getToDateStandardDate().get(0);
			Iterator<String> tabsValueFromIt = tabsValueFromMap.keySet().iterator();
			Iterator<String> tabsValueToIt = tabsValueToMap.keySet().iterator();
			while (tabsValueFromIt.hasNext()) {
				String tabValueFromKey = tabsValueFromIt.next();
				String tabValueToKey = tabsValueToIt.next();
				Map<String, Map<String, List<List<String>>>> sectionsValueFromMap = tabsValueFromMap.get(tabValueFromKey);
				Map<String, Map<String, List<List<String>>>> sectionsValueToMap = tabsValueToMap.get(tabValueToKey);
				Iterator<String> sectionsValueFromIt = sectionsValueFromMap.keySet().iterator();
				Iterator<String> sectionsValueToIt = sectionsValueToMap.keySet().iterator();
				if (Eag2012.TAB_IDENTITY.equalsIgnoreCase(tabValueFromKey)
						&& Eag2012.TAB_IDENTITY.equalsIgnoreCase(tabValueToKey)) {
					while (sectionsValueFromIt.hasNext()) {
						String sectionValueFromKey = sectionsValueFromIt.next();
						String sectionValueToKey = sectionsValueToIt.next();
						Map<String, List<List<String>>> subsectionsValueFromMap = sectionsValueFromMap.get(sectionValueFromKey);
						Map<String, List<List<String>>> subsectionsValueToMap = sectionsValueToMap.get(sectionValueToKey);
						Iterator<String> subsectionsValueFromIt = subsectionsValueFromMap.keySet().iterator();
						Iterator<String> subsectionsValueToIt = subsectionsValueToMap.keySet().iterator();
						while (subsectionsValueFromIt.hasNext()) {
							if (Eag2012.ROOT.equalsIgnoreCase(sectionValueFromKey)
								&& Eag2012.ROOT.equalsIgnoreCase(sectionValueToKey)) {
								String subsectionValueFromKey = subsectionsValueFromIt.next();
								String subsectionValueToKey = subsectionsValueToIt.next();
								List<List<String>> valuesFromList = subsectionsValueFromMap.get(subsectionValueFromKey);
								List<List<String>> valuesToList = subsectionsValueToMap.get(subsectionValueToKey);
								for (int j = 0; j < valuesFromList.size(); j++) {
									if (j == i) {
										List<String> valueFromList = valuesFromList.get(j);
										List<String> valueToList = valuesToList.get(j);
										for (int k = 0; k < valueFromList.size(); k++) {
											if ((valueFromList.get(k) != null && !valueFromList.get(k).isEmpty())
													|| (valueToList.get(k) != null && !valueToList.get(k).isEmpty())){
												FromDate fromDate = new FromDate();
												String valueStandardDate = parseDate(valueFromList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													fromDate.setStandardDate(valueStandardDate);
												}
												fromDate.setContent(valueFromList.get(k));
		
												ToDate toDate = new ToDate();
												valueStandardDate = parseDate(valueToList.get(k));
												if (valueStandardDate != null && !valueStandardDate.isEmpty()){
													toDate.setStandardDate(valueStandardDate);
												}
												toDate.setContent(valueToList.get(k));
		
												DateRange dateRange = new DateRange();
												dateRange.setFromDate(fromDate);
												dateRange.setToDate(toDate);
	
												if (Eag2012.ROOT_SUBSECTION.equalsIgnoreCase(subsectionValueFromKey)
														&& Eag2012.ROOT_SUBSECTION.equalsIgnoreCase(subsectionValueToKey)) {
													List<Object> nonpreformObjectList = nonpreform.getContent();
													UseDates useDates = null;
													int index = 0;
													for (int l = 0; l < nonpreformObjectList.size(); l++) {
														if (nonpreformObjectList.get(l) instanceof UseDates) {
															useDates = (UseDates) nonpreformObjectList.get(l);
															index = l;
														}
													}
													boolean emptyUseDates = false;
													if (useDates == null) {
														useDates = new UseDates();
														emptyUseDates = true;
													}
	
													if (emptyUseDates) {
														useDates.setDateRange(dateRange);
													} else {
														DateSet dateSet = null;
														if (useDates.getDateSet() == null) {
															dateSet = new DateSet();
														} else {
															dateSet = useDates.getDateSet();
														}
	
														// Recover previous single element.
														if (useDates.getDate() != null) {
															Date previousDate = useDates.getDate();
															useDates.setDate(null);
															dateSet.getDateOrDateRange().add(previousDate);
														}
														if (useDates.getDateRange() != null) {
															DateRange previousDateRange = useDates.getDateRange();
															useDates.setDateRange(null);
															dateSet.getDateOrDateRange().add(previousDateRange);
														}
	
														dateSet.getDateOrDateRange().add(dateRange);
														useDates.setDateSet(dateSet);
													}
	
													if (index == 0) {
														nonpreform.getContent().add(useDates);
													} else {
														nonpreform.getContent().set(index, useDates);
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
	}
}
