package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.APEnetEAGDashboard;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

/**
 * This class has been created to manage all EAG2012 information
 * into one object.
 */
public class Eag2012 {
	
	private static final String OTHERRECORDID_PATH = "/eag/archguide/otherRecordId";

	public static final String REPOSITORY_ROLE_HEAD_QUARTER = "HeadQuarters";
	
	private String repositoryId;
	private String otherRepositorId;
	private String autform;
	private String controlId;
	private String controlLanguage;
	private String recordIdId;
	private String recordIdValue;
	private List<String> otherRecordIdId;
	private List<String> otherRecordIdValue;
	private String sourcesId;
	private String sourcesLang;
	private List<String> sourceHref;
	private List<String> sourceId;
	private List<String> sourceLastDateTimeVerified;
	private String maintenanceAgencyId;
	private String agencyCodeId;
	private String agencyCodeValue;
	private String agencyNameLang;
	private String agencyNameId;
	private String agencyNameValue;
	private List<String> otherAgencyCodeId;
	private List<String> otherAgencyCodeValue;
	private String maintenanceStatusId;
	private String maintenanceStatusValue;
	private String maintenanceHistoryId;
	private String maintenanceHistoryLang;
	private List<String> maintenanceEventId;
	private List<String> maintenanceEventLang;
	private String agentId;
	private String agentLang;
	private String agentValue;
	private String agentTypeId;
	private String agentTypeValue;
	private String eventDateTimeStandardDateTime;
	private String eventDateTimeLang;
	private String eventDateTimeId;
	private String eventDateTimeValue;
	private String eventTypeId;
	private String eventTypeValue;
	private List<String> languageDeclarationId;
	private List<String> languageDeclarationLang;
	private String languageLanguageCode;
	private String languageId;
	private String scriptScriptCode;
	private String scriptId;
	private String scriptLang;
	private String scriptValue;
	private String languageLang;
	private String languageValue;
	private List<String> conventionDeclarationId;
	private List<String> conventionDeclarationLang;
	private String abbreviationId;
	private String abbreviationLang;
	private String abbreviationValue;
	private List<List<String>> citationId; //first index if control tab, next elements are into repo_# --> <repository></directions></citation></repository>
	private List<List<String>> citationLang;
	private List<List<String>> citationLastDateTimeVerified;
	private List<List<String>> citationHref;
	private List<List<String>> citationValue;
	private List<Map<String,String>> descriptiveNoteLang;
	private List<Map<String,String>> descriptiveNoteId;
	private List<Map<String,List<String>>> descriptiveNotePId;
	private List<Map<String,List<String>>> descriptiveNotePLang;
	private List<Map<String,Map<String,List<String>>>> descriptiveNotePValue;
	private List<String> localControlId;
	private List<String> localControlLang;
	private List<String> termLastDateTimeVerified;
	private List<String> termScriptCode;
	private List<String> termId;
	private List<String> termLang;
	private List<String> termValue;
	private List<Map<String,List<String>>> dateNotAfter;
	private List<Map<String,List<String>>> dateNotBefore;
	private List<Map<String,List<String>>> dateStandardDate;
	private List<Map<String,List<String>>> dateId;
	private List<Map<String,List<String>>> dateLang;
	private List<Map<String,List<String>>> dateValue;
	private List<Map<String,List<String>>> dateRangeId;
	private List<Map<String,List<String>>> dateRangeLang;
	private List<Map<String,List<String>>> toDateNotAfter;
	private List<Map<String,List<String>>> toDateNotBefore;
	private List<Map<String,List<String>>> toDateStandardDate;
	private List<Map<String,List<String>>> fromDateNotAfter;
	private List<Map<String,List<String>>> fromDateNotBefore;
	private List<Map<String,List<String>>> fromDateStandardDate;
	private List<Map<String,List<String>>> fromDateId;
	private List<Map<String,List<String>>> fromDateLang;
	private List<Map<String,List<String>>> fromDateValue;
	private List<Map<String,List<String>>> toDateId;
	private List<Map<String,List<String>>> toDateLang;
	private List<Map<String,List<String>>> toDateValue;
	private List<String> localTypeDeclarationId;
	private List<String> localTypeDeclarationLang;
	private String publicationStatusId;
	private String publicationStatusValue;
	private String sourceEntryScriptCode;
	private String sourceEntryId;
	private String sourceEntryLang;
	private String sourceEntryValue;
	private List<String> objectXMLWrapId;
	private String objectBinWrapId;
	private String repositoridCountrycode;
	private String repositoridRepositorycode;
	private String otherRepositorIdValue;
	private List<String> autformLang;
	private List<String> autformValue;
	private List<String> parformLang;
	private List<String> parformValue;
	private List<String> repositoryTypeValue;
	private List<String> nonpreformLang;
	private List<String> nonpreformValue;
	private String useDatesId;
	private String useDatesLang;
	private List<Map<String,List<String>>> dateSetId;
	private List<Map<String,List<String>>> dateSetLang;
	private List<String> repositoryNameLang;
	private List<String> repositoryNameValue;
	private List<String> repositoryRoleValue;
	private String geogareaLang;
	private String geogareaValue;
	private List<List<String>> locationLocalType;
	private List<List<String>> locationLatitude;
	private List<List<String>> locationLongitude;
	private List<List<String>> countryLang;
	private List<List<String>> countryValue;
	private List<List<String>> firstdemLang;
	private List<List<String>> firstdemValue;
	private List<List<String>> secondemLang;
	private List<List<String>> secondemValue;
	private List<List<String>> municipalityPostalcodeLang;
	private List<List<String>> municipalityPostalcodeValue;
	private List<List<String>> citiesValue;
	private List<List<String>> citiesLang;
	private List<String> localentityLang;
	private List<String> localentityValue;
	private List<List<String>> streetLang;
	private List<List<String>> streetValue;
	private List<List<String>> directionsLang;
	private List<List<String>> directionsValue;
	private List<List<String>> adminunitLang;
	private List<List<String>> adminunitValue;
	private List<Map<String,String>> openingValue;
	private List<Map<String,String>> openingLang;
	private List<Map<String,String>> closingStandardDate;
	private List<Map<String,String>> closingValue;
	private List<Map<String,String>> closingLang;
	private List<Map<String,String>> accessQuestion;
	private List<Map<String,List<String>>> restaccessLang;
	private List<Map<String,List<String>>> restaccessValue;
	private List<List<String>> termsOfUseHref;
	private List<List<String>> termsOfUseLang;
	private List<List<String>> termsOfUseValue;
	private List<Map<String,String>> accessibilityQuestion;
	private List<Map<String,List<String>>> accessibilityLang;
	private List<Map<String,List<String>>> accessibilityValue;
	private String photographAllowanceValue;
	private List<List<String>> readersTicketHref;
	private List<List<String>> readersTicketLang;
	private List<List<String>> readersTicketValue;
	private List<List<String>> advancedOrdersHref;
	private List<List<String>> advancedOrdersLang;
	private List<List<String>> advancedOrdersValue;
	private List<String> libraryQuestion;
	private List<String> internetAccessQuestion;
	private List<String> reproductionserQuestion;
	private List<String> microformserQuestion;
	private List<String> photographserQuestion;
	private List<String> digitalserQuestion;
	private List<String> photocopyserQuestion;
	private List<String> restorationlabQuestion;
	private List<Map<String,Map<String,Map<String,List<String>>>>> numValue;
	private List<List<String>> ruleLang;
	private List<List<String>> ruleValue;
	private List<Map<String, Map<String, List<String>>>> webpageHref;
	private List<Map<String, Map<String, List<String>>>> webpageValue;
	private List<Map<String, Map<String, List<String>>>> emailHref;
	private List<Map<String, Map<String, List<String>>>> emailValue;
	private List<Map<String,List<String> >> faxValue;
	private List<Map<String, Map<String, List<String>>>> telephoneValue;
	private List<String> relationEntryScriptCode;
	private List<String> relationEntryId;
	private List<String> relationEntryLang;
	private List<String> relationEntryValue;
	private List<String> eagRelationEagRelationType;
	private List<String> eagRelationHref;
	private List<String> placeEntryAccuracy;
	private List<String> placeEntryAltitude;
	private List<String> placeEntryId;
	private List<String> placeEntryLang;
	private List<String> placeEntryCountryCode;
	private List<String> placeEntryLatitude;
	private List<String> placeEntryLongitude;
	private List<String> placeEntryScriptCode;
	private List<String> resourceRelationResourceRelationType;
	private List<String> resourceRelationLastDateTimeVerified;
	private List<String> resourceRelationId;
	private List<String> resourceRelationLang;
	private List<String> resourceRelationHref;
	private String relationsId;
	private String relationsLang;
	private List<String> placeEntryValue;
	private List<List<String>> postalStreetValue;
	private List<List<String>> postalStreetLang;

	public Eag2012() {
		// TODO put all parameters here
	}
	
	public List<List<String>> getPostalStreetValue(){
		return this.postalStreetValue;
	}
	
	public String getRepositoryId() {
		return repositoryId;
	}

	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public String getOtherRepositorId() {
		return otherRepositorId;
	}

	public void setOtherRepositorId(String otherRepositorId) {
		this.otherRepositorId = otherRepositorId;
	}

	public String getAutform() {
		return autform;
	}

	public void setAutform(String autform) {
		this.autform = autform;
	}

	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
	}

	public String getControlLanguage() {
		return controlLanguage;
	}

	public void setControlLanguage(String controlLanguage) {
		this.controlLanguage = controlLanguage;
	}

	public String getRecordIdId() {
		return recordIdId;
	}

	public void setRecordIdId(String recordIdId) {
		this.recordIdId = recordIdId;
	}

	public String getRecordIdValue() {
		return recordIdValue;
	}

	public void setRecordIdValue(String recordIdValue) {
		this.recordIdValue = recordIdValue;
	}

	public List<String> getOtherRecordIdId() {
		return otherRecordIdId;
	}

	public void setOtherRecordIdId(List<String> otherRecordIdId) {
		this.otherRecordIdId = otherRecordIdId;
	}

	public List<String> getOtherRecordIdValue() {
		return otherRecordIdValue;
	}

	public void setOtherRecordIdValue(List<String> otherRecordIdValue) {
		this.otherRecordIdValue = otherRecordIdValue;
	}

	public String getSourcesId() {
		return sourcesId;
	}

	public void setSourcesId(String sourcesId) {
		this.sourcesId = sourcesId;
	}

	public String getSourcesLang() {
		return sourcesLang;
	}

	public void setSourcesLang(String sourcesLang) {
		this.sourcesLang = sourcesLang;
	}

	public List<String> getSourceHref() {
		return sourceHref;
	}

	public void setSourceHref(List<String> sourceHref) {
		this.sourceHref = sourceHref;
	}

	public List<String> getSourceId() {
		return sourceId;
	}

	public void setSourceId(List<String> sourceId) {
		this.sourceId = sourceId;
	}

	public List<String> getSourceLastDateTimeVerified() {
		return sourceLastDateTimeVerified;
	}

	public void setSourceLastDateTimeVerified(
			List<String> sourceLastDateTimeVerified) {
		this.sourceLastDateTimeVerified = sourceLastDateTimeVerified;
	}

	public String getMaintenanceAgencyId() {
		return maintenanceAgencyId;
	}

	public void setMaintenanceAgencyId(String maintenanceAgencyId) {
		this.maintenanceAgencyId = maintenanceAgencyId;
	}

	public String getAgencyCodeId() {
		return agencyCodeId;
	}

	public void setAgencyCodeId(String agencyCodeId) {
		this.agencyCodeId = agencyCodeId;
	}

	public String getAgencyCodeValue() {
		return agencyCodeValue;
	}

	public void setAgencyCodeValue(String agencyCodeValue) {
		this.agencyCodeValue = agencyCodeValue;
	}

	public String getAgencyNameLang() {
		return agencyNameLang;
	}

	public void setAgencyNameLang(String agencyNameLang) {
		this.agencyNameLang = agencyNameLang;
	}

	public String getAgencyNameId() {
		return agencyNameId;
	}

	public void setAgencyNameId(String agencyNameId) {
		this.agencyNameId = agencyNameId;
	}

	public String getAgencyNameValue() {
		return agencyNameValue;
	}

	public void setAgencyNameValue(String agencyNameValue) {
		this.agencyNameValue = agencyNameValue;
	}

	public List<String> getOtherAgencyCodeId() {
		return otherAgencyCodeId;
	}

	public void setOtherAgencyCodeId(List<String> otherAgencyCodeId) {
		this.otherAgencyCodeId = otherAgencyCodeId;
	}

	public List<String> getOtherAgencyCodeValue() {
		return otherAgencyCodeValue;
	}

	public void setOtherAgencyCodeValue(List<String> otherAgencyCodeValue) {
		this.otherAgencyCodeValue = otherAgencyCodeValue;
	}

	public String getMaintenanceStatusId() {
		return maintenanceStatusId;
	}

	public void setMaintenanceStatusId(String maintenanceStatusId) {
		this.maintenanceStatusId = maintenanceStatusId;
	}

	public String getMaintenanceStatusValue() {
		return maintenanceStatusValue;
	}

	public void setMaintenanceStatusValue(String maintenanceStatusValue) {
		this.maintenanceStatusValue = maintenanceStatusValue;
	}

	public String getMaintenanceHistoryId() {
		return maintenanceHistoryId;
	}

	public void setMaintenanceHistoryId(String maintenanceHistoryId) {
		this.maintenanceHistoryId = maintenanceHistoryId;
	}

	public String getMaintenanceHistoryLang() {
		return maintenanceHistoryLang;
	}

	public void setMaintenanceHistoryLang(String maintenanceHistoryLang) {
		this.maintenanceHistoryLang = maintenanceHistoryLang;
	}

	public List<String> getMaintenanceEventId() {
		return maintenanceEventId;
	}

	public void setMaintenanceEventId(List<String> maintenanceEventId) {
		this.maintenanceEventId = maintenanceEventId;
	}

	public List<String> getMaintenanceEventLang() {
		return maintenanceEventLang;
	}

	public void setMaintenanceEventLang(List<String> maintenanceEventLang) {
		this.maintenanceEventLang = maintenanceEventLang;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getAgentLang() {
		return agentLang;
	}

	public void setAgentLang(String agentLang) {
		this.agentLang = agentLang;
	}

	public String getAgentValue() {
		return agentValue;
	}

	public void setAgentValue(String agentValue) {
		this.agentValue = agentValue;
	}

	public String getAgentTypeId() {
		return agentTypeId;
	}

	public void setAgentTypeId(String agentTypeId) {
		this.agentTypeId = agentTypeId;
	}

	public String getAgentTypeValue() {
		return agentTypeValue;
	}

	public void setAgentTypeValue(String agentTypeValue) {
		this.agentTypeValue = agentTypeValue;
	}

	public String getEventDateTimeStandardDateTime() {
		return eventDateTimeStandardDateTime;
	}

	public void setEventDateTimeStandardDateTime(
			String eventDateTimeStandardDateTime) {
		this.eventDateTimeStandardDateTime = eventDateTimeStandardDateTime;
	}

	public String getEventDateTimeLang() {
		return eventDateTimeLang;
	}

	public void setEventDateTimeLang(String eventDateTimeLang) {
		this.eventDateTimeLang = eventDateTimeLang;
	}

	public String getEventDateTimeId() {
		return eventDateTimeId;
	}

	public void setEventDateTimeId(String eventDateTimeId) {
		this.eventDateTimeId = eventDateTimeId;
	}

	public String getEventDateTimeValue() {
		return eventDateTimeValue;
	}

	public void setEventDateTimeValue(String eventDateTimeValue) {
		this.eventDateTimeValue = eventDateTimeValue;
	}

	public String getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getEventTypeValue() {
		return eventTypeValue;
	}

	public void setEventTypeValue(String eventTypeValue) {
		this.eventTypeValue = eventTypeValue;
	}

	public List<String> getLanguageDeclarationId() {
		return languageDeclarationId;
	}

	public void setLanguageDeclarationId(List<String> languageDeclarationId) {
		this.languageDeclarationId = languageDeclarationId;
	}

	public List<String> getLanguageDeclarationLang() {
		return languageDeclarationLang;
	}

	public void setLanguageDeclarationLang(List<String> languageDeclarationLang) {
		this.languageDeclarationLang = languageDeclarationLang;
	}

	public String getLanguageLanguageCode() {
		return languageLanguageCode;
	}

	public void setLanguageLanguageCode(String languageLanguageCode) {
		this.languageLanguageCode = languageLanguageCode;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public String getScriptScriptCode() {
		return scriptScriptCode;
	}

	public void setScriptScriptCode(String scriptScriptCode) {
		this.scriptScriptCode = scriptScriptCode;
	}

	public String getScriptId() {
		return scriptId;
	}

	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	public String getScriptLang() {
		return scriptLang;
	}

	public void setScriptLang(String scriptLang) {
		this.scriptLang = scriptLang;
	}

	public String getScriptValue() {
		return scriptValue;
	}

	public void setScriptValue(String scriptValue) {
		this.scriptValue = scriptValue;
	}

	public String getLanguageLang() {
		return languageLang;
	}

	public void setLanguageLang(String languageLang) {
		this.languageLang = languageLang;
	}

	public String getLanguageValue() {
		return languageValue;
	}

	public void setLanguageValue(String languageValue) {
		this.languageValue = languageValue;
	}

	public List<String> getConventionDeclarationId() {
		return conventionDeclarationId;
	}

	public void setConventionDeclarationId(List<String> conventionDeclarationId) {
		this.conventionDeclarationId = conventionDeclarationId;
	}

	public List<String> getConventionDeclarationLang() {
		return conventionDeclarationLang;
	}

	public void setConventionDeclarationLang(List<String> conventionDeclarationLang) {
		this.conventionDeclarationLang = conventionDeclarationLang;
	}

	public String getAbbreviationId() {
		return abbreviationId;
	}

	public void setAbbreviationId(String abbreviationId) {
		this.abbreviationId = abbreviationId;
	}

	public String getAbbreviationLang() {
		return abbreviationLang;
	}

	public void setAbbreviationLang(String abbreviationLang) {
		this.abbreviationLang = abbreviationLang;
	}

	public String getAbbreviationValue() {
		return abbreviationValue;
	}

	public void setAbbreviationValue(String abbreviationValue) {
		this.abbreviationValue = abbreviationValue;
	}

	public List<List<String>> getCitationId() {
		return citationId;
	}

	public void setCitationId(List<List<String>> citationId) {
		this.citationId = citationId;
	}

	public List<List<String>> getCitationLang() {
		return citationLang;
	}

	public void setCitationLang(List<List<String>> citationLang) {
		this.citationLang = citationLang;
	}

	public List<List<String>> getCitationLastDateTimeVerified() {
		return citationLastDateTimeVerified;
	}

	public void setCitationLastDateTimeVerified(
			List<List<String>> citationLastDateTimeVerified) {
		this.citationLastDateTimeVerified = citationLastDateTimeVerified;
	}

	public List<List<String>> getCitationHref() {
		return citationHref;
	}

	public void setCitationHref(List<List<String>> citationHref) {
		this.citationHref = citationHref;
	}

	public List<List<String>> getCitationValue() {
		return citationValue;
	}

	public void setCitationValue(List<List<String>> citationValue) {
		this.citationValue = citationValue;
	}

	public List<Map<String, String>> getDescriptiveNoteLang() {
		return descriptiveNoteLang;
	}

	public void setDescriptiveNoteLang(List<Map<String, String>> descriptiveNoteLang) {
		this.descriptiveNoteLang = descriptiveNoteLang;
	}

	public List<Map<String, String>> getDescriptiveNoteId() {
		return descriptiveNoteId;
	}

	public void setDescriptiveNoteId(List<Map<String, String>> descriptiveNoteId) {
		this.descriptiveNoteId = descriptiveNoteId;
	}

	public List<Map<String, List<String>>> getDescriptiveNotePId() {
		return descriptiveNotePId;
	}

	public void setDescriptiveNotePId(List<Map<String, List<String>>> descriptiveNotePId) {
		this.descriptiveNotePId = descriptiveNotePId;
	}

	public List<Map<String, List<String>>> getDescriptiveNotePLang() {
		return descriptiveNotePLang;
	}

	public void setDescriptiveNotePLang(
			List<Map<String, List<String>>> descriptiveNotePLang) {
		this.descriptiveNotePLang = descriptiveNotePLang;
	}

	public List<Map<String,Map<String,List<String>>>> getDescriptiveNotePValue() {
		return descriptiveNotePValue;
	}

	public void setDescriptiveNotePValue(List<Map<String,Map<String,List<String>>>> descriptiveNotePValue) {
		this.descriptiveNotePValue = descriptiveNotePValue;
	}

	public List<String> getLocalControlId() {
		return localControlId;
	}

	public void setLocalControlId(List<String> localControlId) {
		this.localControlId = localControlId;
	}

	public List<String> getLocalControlLang() {
		return localControlLang;
	}

	public void setLocalControlLang(List<String> localControlLang) {
		this.localControlLang = localControlLang;
	}

	public List<String> getTermLastDateTimeVerified() {
		return termLastDateTimeVerified;
	}

	public void setTermLastDateTimeVerified(List<String> termLastDateTimeVerified) {
		this.termLastDateTimeVerified = termLastDateTimeVerified;
	}

	public List<String> getTermScriptCode() {
		return termScriptCode;
	}

	public void setTermScriptCode(List<String> termScriptCode) {
		this.termScriptCode = termScriptCode;
	}

	public List<String> getTermId() {
		return termId;
	}

	public void setTermId(List<String> termId) {
		this.termId = termId;
	}

	public List<String> getTermLang() {
		return termLang;
	}

	public void setTermLang(List<String> termLang) {
		this.termLang = termLang;
	}

	public List<String> getTermValue() {
		return termValue;
	}

	public void setTermValue(List<String> termValue) {
		this.termValue = termValue;
	}

	public List<Map<String,List<String>>> getDateNotAfter() {
		return dateNotAfter;
	}

	public void setDateNotAfter(List<Map<String,List<String>>> dateNotAfter) {
		this.dateNotAfter = dateNotAfter;
	}

	public List<Map<String,List<String>>> getDateNotBefore() {
		return dateNotBefore;
	}

	public void setDateNotBefore(List<Map<String,List<String>>> dateNotBefore) {
		this.dateNotBefore = dateNotBefore;
	}

	public List<Map<String,List<String>>> getDateStandardDate() {
		return dateStandardDate;
	}

	public void setDateStandardDate(List<Map<String,List<String>>> dateStandardDate) {
		this.dateStandardDate = dateStandardDate;
	}

	public List<Map<String,List<String>>> getDateId() {
		return dateId;
	}

	public void setDateId(List<Map<String,List<String>>> dateId) {
		this.dateId = dateId;
	}

	public List<Map<String,List<String>>> getDateLang() {
		return dateLang;
	}

	public void setDateLang(List<Map<String,List<String>>> dateLang) {
		this.dateLang = dateLang;
	}

	public List<Map<String,List<String>>> getDateValue() {
		return dateValue;
	}

	public void setDateValue(List<Map<String,List<String>>> dateValue) {
		this.dateValue = dateValue;
	}

	public List<Map<String,List<String>>> getDateRangeId() {
		return dateRangeId;
	}

	public void setDateRangeId(List<Map<String,List<String>>> dateRangeId) {
		this.dateRangeId = dateRangeId;
	}

	public List<Map<String, List<String>>> getToDateNotAfter() {
		return toDateNotAfter;
	}

	public void setToDateNotAfter(List<Map<String, List<String>>> toDateNotAfter) {
		this.toDateNotAfter = toDateNotAfter;
	}

	public List<Map<String, List<String>>> getToDateNotBefore() {
		return toDateNotBefore;
	}

	public void setToDateNotBefore(List<Map<String, List<String>>> toDateNotBefore) {
		this.toDateNotBefore = toDateNotBefore;
	}

	public List<Map<String, List<String>>> getToDateStandardDate() {
		return toDateStandardDate;
	}

	public void setToDateStandardDate(List<Map<String, List<String>>> toDateStandardDate) {
		this.toDateStandardDate = toDateStandardDate;
	}

	public List<Map<String,List<String>>> getDateRangeLang() {
		return dateRangeLang;
	}

	public void setDateRangeLang(List<Map<String,List<String>>> dateRangeLang) {
		this.dateRangeLang = dateRangeLang;
	}

	public List<Map<String, List<String>>> getFromDateNotAfter() {
		return fromDateNotAfter;
	}

	public void setFromDateNotAfter(List<Map<String, List<String>>> fromDateNotAfter) {
		this.fromDateNotAfter = fromDateNotAfter;
	}

	public List<Map<String, List<String>>> getFromDateNotBefore() {
		return fromDateNotBefore;
	}

	public void setFromDateNotBefore(List<Map<String, List<String>>> fromDateNotBefore) {
		this.fromDateNotBefore = fromDateNotBefore;
	}

	public List<Map<String, List<String>>> getFromDateStandardDate() {
		return fromDateStandardDate;
	}

	public void setFromDateStandardDate(List<Map<String, List<String>>> fromDateStandardDate) {
		this.fromDateStandardDate = fromDateStandardDate;
	}

	public List<Map<String, List<String>>> getFromDateId() {
		return fromDateId;
	}

	public void setFromDateId(List<Map<String, List<String>>> fromDateId) {
		this.fromDateId = fromDateId;
	}

	public List<Map<String, List<String>>> getFromDateLang() {
		return fromDateLang;
	}

	public void setFromDateLang(List<Map<String, List<String>>> fromDateLang) {
		this.fromDateLang = fromDateLang;
	}

	public List<Map<String, List<String>>> getFromDateValue() {
		return fromDateValue;
	}

	public void setFromDateValue(List<Map<String, List<String>>> fromDateValue) {
		this.fromDateValue = fromDateValue;
	}

	public List<Map<String, List<String>>> getToDateId() {
		return toDateId;
	}

	public void setToDateId(List<Map<String, List<String>>> toDateId) {
		this.toDateId = toDateId;
	}

	public List<Map<String, List<String>>> getToDateLang() {
		return toDateLang;
	}

	public void setToDateLang(List<Map<String, List<String>>> toDateLang) {
		this.toDateLang = toDateLang;
	}

	public List<Map<String, List<String>>> getToDateValue() {
		return toDateValue;
	}

	public void setToDateValue(List<Map<String, List<String>>> toDateValue) {
		this.toDateValue = toDateValue;
	}

	public List<String> getLocalTypeDeclarationId() {
		return localTypeDeclarationId;
	}

	public void setLocalTypeDeclarationId(List<String> localTypeDeclarationId) {
		this.localTypeDeclarationId = localTypeDeclarationId;
	}

	public List<String> getLocalTypeDeclarationLang() {
		return localTypeDeclarationLang;
	}

	public void setLocalTypeDeclarationLang(List<String> localTypeDeclarationLang) {
		this.localTypeDeclarationLang = localTypeDeclarationLang;
	}

	public String getPublicationStatusId() {
		return publicationStatusId;
	}

	public void setPublicationStatusId(String publicationStatusId) {
		this.publicationStatusId = publicationStatusId;
	}

	public String getPublicationStatusValue() {
		return publicationStatusValue;
	}

	public void setPublicationStatusValue(String publicationStatusValue) {
		this.publicationStatusValue = publicationStatusValue;
	}

	public String getSourceEntryScriptCode() {
		return sourceEntryScriptCode;
	}

	public void setSourceEntryScriptCode(String sourceEntryScriptCode) {
		this.sourceEntryScriptCode = sourceEntryScriptCode;
	}

	public String getSourceEntryId() {
		return sourceEntryId;
	}

	public void setSourceEntryId(String sourceEntryId) {
		this.sourceEntryId = sourceEntryId;
	}

	public String getSourceEntryLang() {
		return sourceEntryLang;
	}

	public void setSourceEntryLang(String sourceEntryLang) {
		this.sourceEntryLang = sourceEntryLang;
	}

	public String getSourceEntryValue() {
		return sourceEntryValue;
	}

	public void setSourceEntryValue(String sourceEntryValue) {
		this.sourceEntryValue = sourceEntryValue;
	}

	public List<String> getObjectXMLWrapId() {
		return objectXMLWrapId;
	}

	public void setObjectXMLWrapId(List<String> objectXMLWrapId) {
		this.objectXMLWrapId = objectXMLWrapId;
	}

	public String getObjectBinWrapId() {
		return objectBinWrapId;
	}

	public void setObjectBinWrapId(String objectBinWrapId) {
		this.objectBinWrapId = objectBinWrapId;
	}

	public String getRepositoridCountrycode() {
		return repositoridCountrycode;
	}

	public void setRepositoridCountrycode(String repositoridCountrycode) {
		this.repositoridCountrycode = repositoridCountrycode;
	}

	public String getRepositoridRepositorycode() {
		return repositoridRepositorycode;
	}

	public void setRepositoridRepositorycode(String repositoridRepositorycode) {
		this.repositoridRepositorycode = repositoridRepositorycode;
	}

	public String getOtherRepositorIdValue() {
		return otherRepositorIdValue;
	}

	public void setOtherRepositorIdValue(String otherRepositorIdValue) {
		this.otherRepositorIdValue = otherRepositorIdValue;
	}

	public List<String> getAutformLang() {
		return autformLang;
	}

	public void setAutformLang(List<String> autformLang) {
		this.autformLang = autformLang;
	}

	public List<String> getAutformValue() {
		return autformValue;
	}

	public void setAutformValue(List<String> autformValue) {
		this.autformValue = autformValue;
	}

	public List<String> getParformLang() {
		return parformLang;
	}

	public void setParformLang(List<String> parformLang) {
		this.parformLang = parformLang;
	}

	public List<String> getParformValue() {
		return parformValue;
	}

	public void setParformValue(List<String> parformValue) {
		this.parformValue = parformValue;
	}

	public List<String> getRepositoryTypeValue() {
		return repositoryTypeValue;
	}

	public void setRepositoryTypeValue(List<String> repositoryTypeValue) {
		this.repositoryTypeValue = repositoryTypeValue;
	}

	public List<String> getNonpreformLang() {
		return nonpreformLang;
	}

	public void setNonpreformLang(List<String> nonpreformLang) {
		this.nonpreformLang = nonpreformLang;
	}

	public List<String> getNonpreformValue() {
		return nonpreformValue;
	}

	public void setNonpreformValue(List<String> nonpreformValue) {
		this.nonpreformValue = nonpreformValue;
	}

	public String getUseDatesId() {
		return useDatesId;
	}

	public void setUseDatesId(String useDatesId) {
		this.useDatesId = useDatesId;
	}

	public String getUseDatesLang() {
		return useDatesLang;
	}

	public void setUseDatesLang(String useDatesLang) {
		this.useDatesLang = useDatesLang;
	}

	public List<Map<String,List<String>>> getDateSetId() {
		return dateSetId;
	}

	public void setDateSetId(List<Map<String,List<String>>> dateSetId) {
		this.dateSetId = dateSetId;
	}

	public List<Map<String,List<String>>> getDateSetLang() {
		return dateSetLang;
	}

	public void setDateSetLang(List<Map<String,List<String>>> dateSetLang) {
		this.dateSetLang = dateSetLang;
	}

	public List<String> getRepositoryNameLang() {
		return repositoryNameLang;
	}

	public void setRepositoryNameLang(List<String> repositoryNameLang) {
		this.repositoryNameLang = repositoryNameLang;
	}

	public List<String> getRepositoryNameValue() {
		return repositoryNameValue;
	}

	public void setRepositoryNameValue(List<String> repositoryNameValue) {
		this.repositoryNameValue = repositoryNameValue;
	}

	public List<String> getRepositoryRoleValue() {
		return repositoryRoleValue;
	}

	public void setRepositoryRoleValue(List<String> repositoryRoleValue) {
		this.repositoryRoleValue = repositoryRoleValue;
	}

	public String getGeogareaLang() {
		return geogareaLang;
	}

	public void setGeogareaLang(String geogareaLang) {
		this.geogareaLang = geogareaLang;
	}

	public String getGeogareaValue() {
		return geogareaValue;
	}

	public void setGeogareaValue(String geogareaValue) {
		this.geogareaValue = geogareaValue;
	}

	public List<List<String>> getLocationLocalType() {
		return locationLocalType;
	}

	public void setLocationLocalType(List<List<String>> locationLocalType) {
		this.locationLocalType = locationLocalType;
	}

	public List<List<String>> getLocationLatitude() {
		return locationLatitude;
	}

	public void setLocationLatitude(List<List<String>> locationLatitude) {
		this.locationLatitude = locationLatitude;
	}

	public List<List<String>> getLocationLongitude() {
		return locationLongitude;
	}

	public void setLocationLongitude(List<List<String>> locationLongitude) {
		this.locationLongitude = locationLongitude;
	}

	public List<List<String>> getCountryLang() {
		return countryLang;
	}

	public void setCountryLang(List<List<String>> countryLang) {
		this.countryLang = countryLang;
	}

	public List<List<String>> getCountryValue() {
		return countryValue;
	}

	public void setCountryValue(List<List<String>>  countryValue) {
		this.countryValue = countryValue;
	}

	public List<List<String>> getFirstdemLang() {
		return firstdemLang;
	}

	public void setFirstdemLang(List<List<String>> firstdemLang) {
		this.firstdemLang = firstdemLang;
	}

	public List<List<String>> getFirstdemValue() {
		return firstdemValue;
	}

	public void setFirstdemValue(List<List<String>> firstdemValue) {
		this.firstdemValue = firstdemValue;
	}

	public List<List<String>> getSecondemLang() {
		return secondemLang;
	}

	public void setSecondemLang(List<List<String>> secondemLang) {
		this.secondemLang = secondemLang;
	}

	public List<List<String>> getSecondemValue() {
		return secondemValue;
	}

	public void setSecondemValue(List<List<String>> secondemValue) {
		this.secondemValue = secondemValue;
	}

	public List<List<String>> getMunicipalityPostalcodeLang() {
		return municipalityPostalcodeLang;
	}

	public void setMunicipalityPostalcodeLang(List<List<String>> municipalityPostalcodeLang) {
		this.municipalityPostalcodeLang = municipalityPostalcodeLang;
	}

	public List<List<String>> getMunicipalityPostalcodeValue() {
		return municipalityPostalcodeValue;
	}

	public void setMunicipalityPostalcodeValue(List<List<String>> municipalityPostalcodeValue) {
		this.municipalityPostalcodeValue = municipalityPostalcodeValue;
	}

	public List<String> getLocalentityLang() {
		return localentityLang;
	}

	public void setLocalentityLang(List<String> localentityLang) {
		this.localentityLang = localentityLang;
	}

	public List<String> getLocalentityValue() {
		return localentityValue;
	}

	public void setLocalentityValue(List<String> localentityValue) {
		this.localentityValue = localentityValue;
	}

	public List<List<String>> getStreetLang() {
		return streetLang;
	}

	public void setStreetLang(List<List<String>> streetLang) {
		this.streetLang = streetLang;
	}

	public List<List<String>> getStreetValue() {
		return streetValue;
	}
	
	public void setPostalStreetValue(List<List<String>> postalStreetValue){
		this.postalStreetValue = postalStreetValue;
	}

	public void setStreetValue(List<List<String>> streetValue) {
		this.streetValue = streetValue;
	}

	public List<List<String>> getDirectionsLang() {
		return directionsLang;
	}

	public void setDirectionsLang(List<List<String>> directionsLang) {
		this.directionsLang = directionsLang;
	}

	public List<List<String>> getDirectionsValue() {
		return directionsValue;
	}

	public void setDirectionsValue(List<List<String>> directionsValue) {
		this.directionsValue = directionsValue;
	}

	public List<List<String>> getAdminunitLang() {
		return adminunitLang;
	}

	public void setAdminunitLang(List<List<String>> adminunitLang) {
		this.adminunitLang = adminunitLang;
	}

	public List<List<String>> getAdminunitValue() {
		return adminunitValue;
	}

	public void setAdminunitValue(List<List<String>> adminunitValue) {
		this.adminunitValue = adminunitValue;
	}

	public List<Map<String, String>> getOpeningValue() {
		return openingValue;
	}

	/**
	 * @return the openingLang
	 */
	public List<Map<String, String>> getOpeningLang() {
		return openingLang;
	}

	/**
	 * @param openingLang the openingLang to set
	 */
	public void setOpeningLang(List<Map<String, String>> openingLang) {
		this.openingLang = openingLang;
	}

	public void setOpeningValue(List<Map<String, String>> openingValue) {
		this.openingValue = openingValue;
	}

	public List<Map<String, String>> getClosingStandardDate() {
		return closingStandardDate;
	}

	public void setClosingStandardDate(List<Map<String, String>> closingStandardDate) {
		this.closingStandardDate = closingStandardDate;
	}

	public List<Map<String, String>> getClosingValue() {
		return closingValue;
	}

	/**
	 * @return the closingLang
	 */
	public List<Map<String, String>> getClosingLang() {
		return closingLang;
	}

	/**
	 * @param closingLang the closingLang to set
	 */
	public void setClosingLang(List<Map<String, String>> closingLang) {
		this.closingLang = closingLang;
	}

	public void setClosingValue(List<Map<String, String>> closingValue) {
		this.closingValue = closingValue;
	}

	public List<Map<String, String>> getAccessQuestion() {
		return accessQuestion;
	}

	public void setAccessQuestion(List<Map<String, String>> accessQuestion) {
		this.accessQuestion = accessQuestion;
	}

	public List<Map<String, List<String>>> getRestaccessLang() {
		return restaccessLang;
	}

	public void setRestaccessLang(List<Map<String, List<String>>> restaccessLang) {
		this.restaccessLang = restaccessLang;
	}

	public List<Map<String, List<String>>> getRestaccessValue() {
		return restaccessValue;
	}

	public void setRestaccessValue(List<Map<String, List<String>>> restaccessValue) {
		this.restaccessValue = restaccessValue;
	}

	public List<List<String>> getTermsOfUseHref() {
		return termsOfUseHref;
	}

	public void setTermsOfUseHref(List<List<String>> termsOfUseHref) {
		this.termsOfUseHref = termsOfUseHref;
	}

	public List<List<String>> getTermsOfUseLang() {
		return termsOfUseLang;
	}

	public void setTermsOfUseLang(List<List<String>> termsOfUseLang) {
		this.termsOfUseLang = termsOfUseLang;
	}

	public List<List<String>> getTermsOfUseValue() {
		return termsOfUseValue;
	}

	public void setTermsOfUseValue(List<List<String>> termsOfUseValue) {
		this.termsOfUseValue = termsOfUseValue;
	}

	public List<Map<String, String>> getAccessibilityQuestion() {
		return accessibilityQuestion;
	}

	public void setAccessibilityQuestion(List<Map<String, String>> accessibilityQuestion) {
		this.accessibilityQuestion = accessibilityQuestion;
	}

	public List<Map<String, List<String>>> getAccessibilityLang() {
		return accessibilityLang;
	}

	public void setAccessibilityLang(List<Map<String, List<String>>> accessibilityLang) {
		this.accessibilityLang = accessibilityLang;
	}

	public List<Map<String, List<String>>> getAccessibilityValue() {
		return accessibilityValue;
	}

	public void setAccessibilityValue(List<Map<String, List<String>>> accessibilityValue) {
		this.accessibilityValue = accessibilityValue;
	}

	public String getPhotographAllowanceValue() {
		return photographAllowanceValue;
	}

	public void setPhotographAllowanceValue(String photographAllowanceValue) {
		this.photographAllowanceValue = photographAllowanceValue;
	}

	public List<List<String>> getReadersTicketHref() {
		return readersTicketHref;
	}

	public void setReadersTicketHref(List<List<String>> readersTicketHref) {
		this.readersTicketHref = readersTicketHref;
	}

	public List<List<String>> getReadersTicketLang() {
		return readersTicketLang;
	}

	public void setReadersTicketLang(List<List<String>> readersTicketLang) {
		this.readersTicketLang = readersTicketLang;
	}

	public List<List<String>> getReadersTicketValue() {
		return readersTicketValue;
	}

	public void setReadersTicketValue(List<List<String>> readersTicketValue) {
		this.readersTicketValue = readersTicketValue;
	}

	public List<List<String>> getAdvancedOrdersHref() {
		return advancedOrdersHref;
	}

	public void setAdvancedOrdersHref(List<List<String>> advancedOrdersHref) {
		this.advancedOrdersHref = advancedOrdersHref;
	}

	public List<List<String>> getAdvancedOrdersLang() {
		return advancedOrdersLang;
	}

	public void setAdvancedOrdersLang(List<List<String>> advancedOrdersLang) {
		this.advancedOrdersLang = advancedOrdersLang;
	}

	public List<List<String>> getAdvancedOrdersValue() {
		return advancedOrdersValue;
	}

	public void setAdvancedOrdersValue(List<List<String>> advancedOrdersValue) {
		this.advancedOrdersValue = advancedOrdersValue;
	}

	public List<String> getLibraryQuestion() {
		return libraryQuestion;
	}

	public void setLibraryQuestion(List<String> libraryQuestion) {
		this.libraryQuestion = libraryQuestion;
	}

	public List<String> getInternetAccessQuestion() {
		return internetAccessQuestion;
	}

	public void setInternetAccessQuestion(List<String> internetAccessQuestion) {
		this.internetAccessQuestion = internetAccessQuestion;
	}

	public List<String> getReproductionserQuestion() {
		return reproductionserQuestion;
	}

	public void setReproductionserQuestion(List<String> reproductionserQuestion) {
		this.reproductionserQuestion = reproductionserQuestion;
	}

	public List<String> getMicroformserQuestion() {
		return microformserQuestion;
	}

	public void setMicroformserQuestion(List<String> microformserQuestion) {
		this.microformserQuestion = microformserQuestion;
	}

	public List<String> getPhotographserQuestion() {
		return photographserQuestion;
	}

	public void setPhotographserQuestion(List<String> photographserQuestion) {
		this.photographserQuestion = photographserQuestion;
	}

	public List<String> getDigitalserQuestion() {
		return digitalserQuestion;
	}

	public void setDigitalserQuestion(List<String> digitalserQuestion) {
		this.digitalserQuestion = digitalserQuestion;
	}

	public List<String> getPhotocopyserQuestion() {
		return photocopyserQuestion;
	}

	public void setPhotocopyserQuestion(List<String> photocopyserQuestion) {
		this.photocopyserQuestion = photocopyserQuestion;
	}

	public List<String> getRestorationlabQuestion() {
		return restorationlabQuestion;
	}

	public void setRestorationlabQuestion(List<String> restorationlabQuestion) {
		this.restorationlabQuestion = restorationlabQuestion;
	}

	public List<Map<String,Map<String,Map<String,List<String>>>>> getNumValue() {
		return numValue;
	}

	public void setNumValue(List<Map<String,Map<String,Map<String,List<String>>>>> numValue) {
		this.numValue = numValue;
	}

	public List<List<String>> getRuleLang() {
		return ruleLang;
	}

	public void setRuleLang(List<List<String>> ruleLang) {
		this.ruleLang = ruleLang;
	}

	public List<List<String>> getRuleValue() {
		return ruleValue;
	}

	public void setRuleValue(List<List<String>> ruleValue) {
		this.ruleValue = ruleValue;
	}

	public List<Map<String, Map<String, List<String>>>> getWebpageHref() {
		return webpageHref;
	}

	public void setWebpageHref(List<Map<String, Map<String, List<String>>>> webpageHref) {
		this.webpageHref = webpageHref;
	}

	public List<Map<String, Map<String, List<String>>>> getWebpageValue() {
		return webpageValue;
	}

	public void setWebpageValue(List<Map<String, Map<String, List<String>>>> webpageValue) {
		this.webpageValue = webpageValue;
	}

	public List<Map<String, Map<String, List<String>>>> getEmailHref() {
		return emailHref;
	}

	public void setEmailHref(List<Map<String, Map<String, List<String>>>> emailHref) {
		this.emailHref = emailHref;
	}

	public List<Map<String, Map<String, List<String>>>> getEmailValue() {
		return emailValue;
	}

	public void setEmailValue(List<Map<String, Map<String, List<String>>>> emailValue) {
		this.emailValue = emailValue;
	}

	public List<Map<String, List<String>>> getFaxValue() {
		return faxValue;
	}

	public void setFaxValue(List<Map<String, List<String>>> faxValue) {
		this.faxValue = faxValue;
	}

	public List<Map<String, Map<String, List<String>>>> getTelephoneValue() {
		return telephoneValue;
	}

	public void setTelephoneValue(List<Map<String, Map<String, List<String>>>> telephoneValue) {
		this.telephoneValue = telephoneValue;
	}

	public List<String> getRelationEntryScriptCode() {
		return relationEntryScriptCode;
	}

	public void setRelationEntryScriptCode(List<String> relationEntryScriptCode) {
		this.relationEntryScriptCode = relationEntryScriptCode;
	}

	public List<String> getRelationEntryId() {
		return relationEntryId;
	}

	public void setRelationEntryId(List<String> relationEntryId) {
		this.relationEntryId = relationEntryId;
	}

	public List<String> getRelationEntryLang() {
		return relationEntryLang;
	}

	public void setRelationEntryLang(List<String> relationEntryLang) {
		this.relationEntryLang = relationEntryLang;
	}

	public List<String> getRelationEntryValue() {
		return relationEntryValue;
	}

	public void setRelationEntryValue(List<String> relationEntryValue) {
		this.relationEntryValue = relationEntryValue;
	}

	public List<String> getEagRelationEagRelationType() {
		return eagRelationEagRelationType;
	}

	public void setEagRelationEagRelationType(
			List<String> eagRelationEagRelationType) {
		this.eagRelationEagRelationType = eagRelationEagRelationType;
	}

	public List<String> getEagRelationHref() {
		return eagRelationHref;
	}

	public void setEagRelationHref(List<String> eagRelationHref) {
		this.eagRelationHref = eagRelationHref;
	}

	public List<String> getPlaceEntryAccuracy() {
		return placeEntryAccuracy;
	}

	public void setPlaceEntryAccuracy(List<String> placeEntryAccuracy) {
		this.placeEntryAccuracy = placeEntryAccuracy;
	}

	public List<String> getPlaceEntryAltitude() {
		return placeEntryAltitude;
	}

	public void setPlaceEntryAltitude(List<String> placeEntryAltitude) {
		this.placeEntryAltitude = placeEntryAltitude;
	}

	public List<String> getPlaceEntryId() {
		return placeEntryId;
	}

	public void setPlaceEntryId(List<String> placeEntryId) {
		this.placeEntryId = placeEntryId;
	}

	public List<String> getPlaceEntryLang() {
		return placeEntryLang;
	}

	public void setPlaceEntryLang(List<String> placeEntryLang) {
		this.placeEntryLang = placeEntryLang;
	}

	public List<String> getPlaceEntryCountryCode() {
		return placeEntryCountryCode;
	}

	public void setPlaceEntryCountryCode(List<String> placeEntryCountryCode) {
		this.placeEntryCountryCode = placeEntryCountryCode;
	}

	public List<String> getPlaceEntryLatitude() {
		return placeEntryLatitude;
	}

	public void setPlaceEntryLatitude(List<String> placeEntryLatitude) {
		this.placeEntryLatitude = placeEntryLatitude;
	}

	public List<String> getPlaceEntryLongitude() {
		return placeEntryLongitude;
	}

	public void setPlaceEntryLongitude(List<String> placeEntryLongitude) {
		this.placeEntryLongitude = placeEntryLongitude;
	}

	public List<String> getPlaceEntryScriptCode() {
		return placeEntryScriptCode;
	}

	public void setPlaceEntryScriptCode(List<String> placeEntryScriptCode) {
		this.placeEntryScriptCode = placeEntryScriptCode;
	}

	public List<String> getResourceRelationResourceRelationType() {
		return resourceRelationResourceRelationType;
	}

	public void setResourceRelationResourceRelationType(
			List<String> resourceRelationResourceRelationType) {
		this.resourceRelationResourceRelationType = resourceRelationResourceRelationType;
	}

	public List<String> getResourceRelationLastDateTimeVerified() {
		return resourceRelationLastDateTimeVerified;
	}

	public void setResourceRelationLastDateTimeVerified(
			List<String> resourceRelationLastDateTimeVerified) {
		this.resourceRelationLastDateTimeVerified = resourceRelationLastDateTimeVerified;
	}

	public List<String> getResourceRelationId() {
		return resourceRelationId;
	}

	public void setResourceRelationId(List<String> resourceRelationId) {
		this.resourceRelationId = resourceRelationId;
	}

	public List<String> getResourceRelationLang() {
		return resourceRelationLang;
	}

	public void setResourceRelationLang(List<String> resourceRelationLang) {
		this.resourceRelationLang = resourceRelationLang;
	}

	public List<String> getResourceRelationHref() {
		return resourceRelationHref;
	}

	public void setResourceRelationHref(List<String> resourceRelationHref) {
		this.resourceRelationHref = resourceRelationHref;
	}

	public String getRelationsId() {
		return relationsId;
	}

	public void setRelationsId(String relationsId) {
		this.relationsId = relationsId;
	}

	public String getRelationsLang() {
		return relationsLang;
	}

	public void setRelationsLang(String relationsLang) {
		this.relationsLang = relationsLang;
	}

	public List<List<String>> getPostalStreetLang() {
		return postalStreetLang;
	}

	public void setPostalStreetLang(List<List<String>> postalStreetLang) {
		this.postalStreetLang = postalStreetLang;
	}

	public List<String> getPlaceEntryValue() {
		return placeEntryValue;
	}

	public void setPlaceEntryValue(List<String> placeEntryValue) {
		this.placeEntryValue = placeEntryValue;
	}

	public List<List<String>> getCitiesValue() {
		return citiesValue;
	}

	public void setCitiesValue(List<List<String>> citiesValue) {
		this.citiesValue = citiesValue;
	}

	public List<List<String>> getCitiesLang() {
		return citiesLang;
	}

	public void setCitiesLang(List<List<String>> citiesLang) {
		this.citiesLang = citiesLang;
	}

	/**
	 * 
	 * Through this procedure can be ensured that an institution within the Dashboard
	 * will have its unique ISO 15511 compliant identifier.
	 * 
	 * @param archivalInstitutionId
	 * @param fullFilePath
	 * @return boolean
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static boolean checkAndFixRepositorId(Integer archivalInstitutionId,String fullFilePath) throws TransformerException, ParserConfigurationException, SAXException, IOException{
		boolean changed = false;
		
        APEnetEAGDashboard eag = new APEnetEAGDashboard(archivalInstitutionId, fullFilePath);
        eag.setEagPath(fullFilePath);
        //this information must be encoded in the element <otherRepositorId>;
        String otherRepositorId = eag.lookingForwardElementContent(OTHERRECORDID_PATH);
        
		if(otherRepositorId!=null && !otherRepositorId.isEmpty()){
			if(!isWrongRepositorId(otherRepositorId)){
				//in case it isn’t, a code compliant with ISO 15511 will be created automatically 
				//for @repositorycode, using the country code plus an ascending counter
				otherRepositorId = generatesISOCode(archivalInstitutionId);
			}
		}else{ 
			//code is not provided (doesn't exists target tag). In this case a code 
			//compliant with ISO 15511 will be created automatically for @repositorycode
			otherRepositorId = generatesISOCode(archivalInstitutionId);
		}
		DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document tempDoc = docBuilder.parse(fullFilePath);
		Element currentNode = null;
		//at this case the code provided is compliant with ISO 15511, it will 
		//be copied to the attribute @repositorycode coming with <repositorid>;
		NodeList recordsIds = tempDoc.getElementsByTagName("repositorid");
		for(int i=0;i<recordsIds.getLength() && !changed;i++){
			currentNode = (Element) recordsIds.item(i);
			Node parent = currentNode.getParentNode();
			if(parent!=null && parent.getNodeName().equals("identity")){
				parent = parent.getParentNode();
				if(parent!=null && parent.getNodeName().equals("archguide")){
					parent = parent.getParentNode();
					if(parent!=null && parent.getNodeName().equals("eag")){
						currentNode.setAttribute("repositorycode",otherRepositorId);
						changed = true;
					}
				}
			}
		}
		if(changed){
			TransformerFactory tf = TransformerFactory.newInstance(); // Save changes
			Transformer transformer = tf.newTransformer();
			transformer.transform(new DOMSource(tempDoc), new StreamResult(new File(fullFilePath)));
		}
        return changed;
	}
	/**
	 * Generates a ISO-code based on internal archivalInstitutionId. 
	 * This ISO-code should be unique for each institution.
	 * 
	 * @param archivalInstitutionId
	 * @return String
	 */
	public static String generatesISOCode(Integer archivalInstitutionId) {
		String otherRepositorId = null;
		if(archivalInstitutionId>0){
			int zeroes = 11-archivalInstitutionId.toString().length();
			otherRepositorId = new ArchivalLandscape().getmyCountry()+"-";
	    	for(int x=0;x<zeroes;x++){
	    		otherRepositorId+="0";
	    	}
	    	otherRepositorId+=archivalInstitutionId.toString();
		}
    	return otherRepositorId;
	}

	/**
	 * Checks if it's a valid code.
	 * The code provided must be compliant with ISO 15511. 
	 * In case it isn’t or the code provided could not be used
	 * returns false 
	 * 
	 * @param repositorId
	 * @return boolean
	 */
	private static boolean isWrongRepositorId(String repositorId){
		String isoCountry = new ArchivalLandscape().getmyCountry();
		if(repositorId.length()==14 && repositorId.substring(0,2).toLowerCase().equals(isoCountry) && StringUtils.isNumeric(repositorId.substring(3))){
			//TODO could be helpful store all identifiers or check all existings eags to get all ingested ISO-codes into repositorycode attribute
			ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
			Integer archivalInstitutionId = new Integer(repositorId.substring(3));
			if(archivalInstitutionId!=null || aiDao.isRepositoryCodeAvailable(repositorId, archivalInstitutionId)){ //TODO check if isRepositoryCodeAvailable is useful
				ArchivalInstitution archivalInstitution = aiDao.getArchivalInstitution(archivalInstitutionId);
				if(archivalInstitution==null || archivalInstitution.getCountry().getIsoname().equals(isoCountry)){  
					return true; //the ISO code used could not be unique because it's reserved to existing other institution of this country
				}
			}
		}
		return false;
	}
}