package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
	private List<String> citationId;
	private List<String> citationLang;
	private List<String> citationLastDateTimeVerified;
	private List<String> citationHref;
	private List<String> citationValue;
	private String descriptiveNoteLang;
	private String descriptiveNoteId;
	private List<String> descriptiveNotePId;
	private List<String> descriptiveNotePLang;
	private List<String> descriptiveNotePValue;
	private List<String> localControlId;
	private List<String> localControlLang;
	private List<String> termLastDateTimeVerified;
	private List<String> termScriptCode;
	private List<String> termId;
	private List<String> termLang;
	private List<String> termValue;
	private List<String> dateNotAfter;
	private List<String> dateNotBefore;
	private List<String> dateStandardDate;
	private List<String> dateId;
	private List<String> dateLang;
	private List<String> dateValue;
	private List<String> dateRangeId;
	private String toDateNotAfter;
	private String toDateNotBefore;
	private String toDateStandardDate;
	private List<String> dateRangeLang;
	private String fromDateNotAfter;
	private String fromDateNotBefore;
	private String fromDateStandardDate;
	private String fromDateId;
	private String fromDateLang;
	private String fromDateValue;
	private String toDateId;
	private String toDateLang;
	private String toDateValue;
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
	private List<String> dateSetId;
	private List<String> dateSetLang;
	private List<String> repositoryNameLang;
	private List<String> repositoryNameValue;
	private String repositoryRoleValue;
	private String geogareaLang;
	private String geogareaValue;
	private List<String> locationLocalType;
	private List<String> locationLatitude;
	private List<String> locationLongitude;
	private String countryLang;
	private String countryValue;
	private String firstdemLang;
	private String firstdemValue;
	private String secondemLang;
	private String secondemValue;
	private String municipalityPostalcodeLang;
	private String municipalityPostalcodeValue;
	private String localentityLang;
	private String localentityValue;
	private String streetLang;
	private String streetValue;
	private List<String> directionsLang;
	private List<String> directionsValue;
	private List<String> adminunitLang;
	private List<String> adminunitValue;
	private String openingValue;
	private String closingStandardDate;
	private String closingValue;
	private String accessQuestion;
	private List<String> restaccessLang;
	private List<String> restaccessValue;
	private List<String> termsOfUseHref;
	private List<String> termsOfUseLang;
	private List<String> termsOfUseValue;
	private List<String> accessibilityQuestion;
	private List<String> accessibilityLang;
	private List<String> accessibilityValue;
	private String photographAllowanceValue;
	private List<String> readersTicketHref;
	private List<String> readersTicketLang;
	private List<String> readersTicketValue;
	private List<String> advancedOrdersHref;
	private List<String> advancedOrdersLang;
	private List<String> advancedOrdersValue;
	private String libraryQuestion;
	private String internetAccessQuestion;
	private String reproductionserQuestion;
	private String microformserQuestion;
	private String photographserQuestion;
	private String digitalserQuestion;
	private String photocopyserQuestion;
	private String restorationlabQuestion;
	private String numValue;
	private List<String> ruleLang;
	private List<String> ruleValue;
	private List<String> webpageHref;
	private List<String> webpageValue;
	private List<String> emailHref;
	private List<String> emailValue;
	private List<String> faxValue;
	private List<String> telephoneValue;
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

	public Eag2012() {
		// TODO put all parameters here
	}



	public String getRepositoryId() {
		return this.repositoryId;
	}

	public String getOtherRepositorId() {
		return this.otherRepositorId;
	}

	public String getAutform() {
		return this.autform;
	}

	public String getControlId() {
		return this.controlId;
	}

	public String getControlLanguage() {
		return this.controlLanguage;
	}

	public String getRecordIdId() {
		return this.recordIdId;
	}

	public String getRecordIdValue() {
		return this.recordIdValue;
	}

	public List<String> getOtherRecordIdId() {
		return this.otherRecordIdId;
	}

	public List<String> getOtherRecordIdValue() {
		return this.otherRecordIdValue;
	}

	public String getSourcesId() {
		return this.sourcesId;
	}

	public String getSourcesLang() {
		return this.sourcesLang;
	}

	public List<String> getSourceLastDateTimeVerified() {
		return sourceLastDateTimeVerified;
	}

	public List<String> getSourceHref() {
		return sourceHref;
	}

	public List<String> getSourceId() {
		return sourceId;
	}

	public String getMaintenanceAgencyId() {
		return maintenanceAgencyId;
	}

	public String getAgencyCodeId() {
		return agencyCodeId;
	}

	public String getAgencyCodeValue() {
		return agencyCodeValue;
	}

	public String getAgencyNameLang() {
		return agencyNameLang;
	}

	public String getAgencyNameId() {
		return agencyNameId;
	}

	public String getAgencyNameValue() {
		return agencyNameValue;
	}


	public List<String> getOtherAgencyCodeId() {
		return otherAgencyCodeId;
	}

	public List<String> getOtherAgencyCodeValue() {
		return otherAgencyCodeValue;
	}

	public String getMaintenanceStatusId() {
		return maintenanceStatusId;
	}



	public String getMaintenanceStatusValue() {
		return maintenanceStatusValue;
	}



	public String getMaintenanceHistoryId() {
		return maintenanceHistoryId;
	}



	public String getMaintenanceHistoryLang() {
		return maintenanceHistoryLang;
	}



	public List<String> getMaintenanceEventId() {
		return maintenanceEventId;
	}



	public List<String> getMaintenanceEventLang() {
		return maintenanceEventLang;
	}



	public String getAgentId() {
		return agentId;
	}



	public String getAgentLang() {
		return agentLang;
	}



	public String getAgentValue() {
		return agentValue;
	}



	public String getAgentTypeId() {
		return agentTypeId;
	}



	public String getAgentTypeValue() {
		return agentTypeValue;
	}



	public String getEventDateTimeStandardDateTime() {
		return eventDateTimeStandardDateTime;
	}



	public String getEventDateTimeLang() {
		return eventDateTimeLang;
	}



	public String getEventDateTimeId() {
		return eventDateTimeId;
	}



	public String getEventDateTimeValue() {
		return eventDateTimeValue;
	}



	public String getEventTypeId() {
		return eventTypeId;
	}



	public String getEventTypeValue() {
		return eventTypeValue;
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



	public String getLanguageLanguageCode() {
		return languageLanguageCode;
	}



	public String getLanguageId() {
		return languageId;
	}



	public String getScriptScriptCode() {
		return scriptScriptCode;
	}



	public String getScriptId() {
		return scriptId;
	}



	public String getScriptLang() {
		return scriptLang;
	}



	public String getScriptValue() {
		return scriptValue;
	}



	public String getLanguageLang() {
		return languageLang;
	}



	public String getLanguageValue() {
		return languageValue;
	}



	public List<String> getConventionDeclarationId() {
		return conventionDeclarationId;
	}



	public List<String> getConventionDeclarationLang() {
		return conventionDeclarationLang;
	}



	public String getAbbreviationId() {
		return abbreviationId;
	}



	public String getAbbreviationLang() {
		return abbreviationLang;
	}



	public String getAbbreviationValue() {
		return abbreviationValue;
	}



	public List<String> getCitationId() {
		return citationId;
	}



	public List<String> getCitationLang() {
		return citationLang;
	}



	public List<String> getCitationLastDateTimeVerified() {
		return citationLastDateTimeVerified;
	}



	public List<String> getCitationHref() {
		return citationHref;
	}



	public List<String> getCitationValue() {
		return citationValue;
	}



	public String getDescriptiveNoteLang() {
		return descriptiveNoteLang;
	}



	public String getDescriptiveNoteId() {
		return descriptiveNoteId;
	}



	public List<String> getPId() {
		return descriptiveNotePId;
	}



	public List<String> getPLang() {
		return descriptiveNotePLang;
	}



	public List<String> getPValue() {
		return descriptiveNotePValue;
	}


	public List<String> getLocalControlId() {
		return localControlId;
	}



	public List<String> getLocalControlLang() {
		return localControlLang;
	}



	public List<String> getTermLastDateTimeVerified() {
		return termLastDateTimeVerified;
	}



	public List<String> getTermScriptCode() {
		return termScriptCode;
	}


	public List<String> getTermId() {
		return termId;
	}



	public List<String> getTermLang() {
		return termLang;
	}



	public List<String> getTermValue() {
		return termValue;
	}



	public List<String> getDateNotAfter() {
		return dateNotAfter;
	}



	public List<String> getDateNotBefore() {
		return dateNotBefore;
	}



	public List<String> getDateStandardDate() {
		return dateStandardDate;
	}



	public List<String> getDateId() {
		return dateId;
	}



	public List<String> getDateLang() {
		return dateLang;
	}



	public List<String> getDateValue() {
		return dateValue;
	}

	public List<String> getDateRangeLang() {
		return dateRangeLang;
	}

	public List<String> getDateRangeId() {
		return dateRangeId;
	}



	public String getToDateNotAfter() {
		return toDateNotAfter;
	}



	public String getToDateNotBefore() {
		return toDateNotBefore;
	}



	public String getToDateStandardDate() {
		return toDateStandardDate;
	}



	public String getFromDateNotAfter() {
		return fromDateNotAfter;
	}



	public String getFromDateNotBefore() {
		return fromDateNotBefore;
	}



	public String getFromDateStandardDate() {
		return fromDateStandardDate;
	}



	public String getFromDateId() {
		return fromDateId;
	}



	public String getFromDateLang() {
		return fromDateLang;
	}



	public String getFromDateValue() {
		return fromDateValue;
	}



	public String getToDateId() {
		return toDateId;
	}



	public String getToDateLang() {
		return toDateLang;
	}



	public String getToDateValue() {
		return toDateValue;
	}



	public List<String> getLocalTypeDeclarationId() {
		return localTypeDeclarationId;
	}



	public List<String> getLocalTypeDeclarationLang() {
		return localTypeDeclarationLang;
	}



	public String getPublicationStatusId() {
		return publicationStatusId;
	}



	public String getPublicationStatusValue() {
		return publicationStatusValue;
	}



	public String getSourceEntryScriptCode() {
		return sourceEntryScriptCode;
	}


	public String getSourceEntryId() {
		return sourceEntryId;
	}



	public String getSourceEntryLang() {
		return sourceEntryLang;
	}



	public String getSourceEntryValue() {
		return sourceEntryValue;
	}



	public List<String> getObjectXMLWrapId() {
		return objectXMLWrapId;
	}



	public String getObjectBinWrapId() {
		return objectBinWrapId;
	}



	public String getRepositoridCountrycode() {
		return repositoridCountrycode;
	}



	public String getRepositoridRepositorycode() {
		return repositoridRepositorycode;
	}



	public String getOtherRepositorIdValue() {
		return otherRepositorIdValue;
	}



	public List<String> getAutformLang() {
		return autformLang;
	}



	public List<String> getAutformValue() {
		return autformValue;
	}



	public List<String> getParformLang() {
		return parformLang;
	}



	public List<String> getParformValue() {
		return parformValue;
	}



	public List<String> getRepositoryTypeValue() {
		return repositoryTypeValue;
	}



	public List<String> getNonpreformLang() {
		return nonpreformLang;
	}



	public List<String> getNonpreformValue() {
		return nonpreformValue;
	}



	public String getUseDatesId() {
		return useDatesId;
	}



	public String getUseDatesLang() {
		return useDatesLang;
	}



	public List<String> getDateSetId() {
		return dateSetId;
	}



	public List<String> getDateSetLang() {
		return dateSetLang;
	}



	public List<String> getRepositoryNameLang() {
		return repositoryNameLang;
	}



	public List<String> getRepositoryNameValue() {
		return repositoryNameValue;
	}



	public String getRepositoryRoleValue() {
		return repositoryRoleValue;
	}



	public String getGeogareaLang() {
		return geogareaLang;
	}



	public String getGeogareaValue() {
		return geogareaValue;
	}



	public List<String> getLocationLocalType() {
		return locationLocalType;
	}



	public List<String> getLocationLatitude() {
		return locationLatitude;
	}



	public List<String> getLocationLongitude() {
		return locationLongitude;
	}



	public String getCountryLang() {
		return countryLang;
	}



	public String getCountryValue() {
		return countryValue;
	}



	public String getFirstdemLang() {
		return firstdemLang;
	}



	public String getFirstdemValue() {
		return firstdemValue;
	}



	public String getSecondemLang() {
		return secondemLang;
	}



	public String getSecondemValue() {
		return secondemValue;
	}



	public String getMunicipalityPostalcodeLang() {
		return municipalityPostalcodeLang;
	}



	public String getMunicipalityPostalcodeValue() {
		return municipalityPostalcodeValue;
	}



	public String getLocalentityLang() {
		return localentityLang;
	}



	public String getLocalentityValue() {
		return localentityValue;
	}



	public String getStreetLang() {
		return streetLang;
	}



	public String getStreetValue() {
		return streetValue;
	}



	public List<String> getDirectionsLang() {
		return directionsLang;
	}



	public List<String> getDirectionsValue() {
		return directionsValue;
	}



	public List<String> getAdminunitLang() {
		return adminunitLang;
	}



	public List<String> getAdminunitValue() {
		return adminunitValue;
	}



	public String getOpeningValue() {
		return openingValue;
	}



	public String getClosingStandardDate() {
		return closingStandardDate;
	}



	public String getClosingValue() {
		return closingValue;
	}



	public String getAccessQuestion() {
		return accessQuestion;
	}



	public List<String> getRestaccessLang() {
		return restaccessLang;
	}



	public List<String> getRestaccessValue() {
		return restaccessValue;
	}



	public List<String> getTermsOfUseHref() {
		return termsOfUseHref;
	}



	public List<String> getTermsOfUseLang() {
		return termsOfUseLang;
	}



	public List<String> getTermsOfUseValue() {
		return termsOfUseValue;
	}



	public List<String> getAccessibilityQuestion() {
		return accessibilityQuestion;
	}



	public List<String> getAccessibilityLang() {
		return accessibilityLang;
	}



	public List<String> getAccessibilityValue() {
		return accessibilityValue;
	}



	public String getPhotographAllowanceValue() {
		return photographAllowanceValue;
	}



	public List<String> getReadersTicketHref() {
		return readersTicketHref;
	}



	public List<String> getReadersTicketLang() {
		return readersTicketLang;
	}



	public List<String> getReadersTicketValue() {
		return readersTicketValue;
	}



	public List<String> getAdvancedOrdersHref() {
		return advancedOrdersHref;
	}



	public List<String> getAdvancedOrdersLang() {
		return advancedOrdersLang;
	}



	public List<String> getAdvancedOrdersValue() {
		return advancedOrdersValue;
	}



	public String getLibraryQuestion() {
		return libraryQuestion;
	}



	public String getInternetAccessQuestion() {
		return internetAccessQuestion;
	}



	public String getReproductionserQuestion() {
		return reproductionserQuestion;
	}



	public String getMicroformserQuestion() {
		return microformserQuestion;
	}



	public String getPhotographserQuestion() {
		return photographserQuestion;
	}



	public String getDigitalserQuestion() {
		return digitalserQuestion;
	}



	public String getPhotocopyserQuestion() {
		return photocopyserQuestion;
	}



	public String getRestorationlabQuestion() {
		return restorationlabQuestion;
	}



	public String getNumValue() {
		return numValue;
	}



	public List<String> getRuleLang() {
		return ruleLang;
	}



	public List<String> getRuleValue() {
		return ruleValue;
	}



	public List<String> getWebpageHref() {
		return webpageHref;
	}



	public List<String> getWebpageValue() {
		return webpageValue;
	}



	public List<String> getEmailHref() {
		return emailHref;
	}



	public List<String> getEmailValue() {
		return emailValue;
	}



	public List<String> getFaxValue() {
		return faxValue;
	}



	public List<String> getTelephoneValue() {
		return telephoneValue;
	}



	public List<String> getRelationEntryScriptCode() {
		return relationEntryScriptCode;
	}


	public List<String> getRelationEntryId() {
		return relationEntryId;
	}



	public List<String> getRelationEntryLang() {
		return relationEntryLang;
	}



	public List<String> getRelationEntryValue() {
		return relationEntryValue;
	}



	public List<String> getEagRelationEagRelationType() {
		return eagRelationEagRelationType;
	}



	public List<String> getEagRelationHref() {
		return eagRelationHref;
	}



	public List<String> getPlaceEntryAccuracy() {
		return placeEntryAccuracy;
	}



	public List<String> getPlaceEntryAltitude() {
		return placeEntryAltitude;
	}



	public List<String> getPlaceEntryId() {
		return placeEntryId;
	}



	public List<String> getPlaceEntryLang() {
		return placeEntryLang;
	}



	public List<String> getPlaceEntryCountryCode() {
		return placeEntryCountryCode;
	}



	public List<String> getPlaceEntryLatitude() {
		return placeEntryLatitude;
	}



	public List<String> getPlaceEntryLongitude() {
		return placeEntryLongitude;
	}



	public List<String> getPlaceEntryScriptCode() {
		return placeEntryScriptCode;
	}

	public List<String> getPlaceEntryValue() {
		return placeEntryValue;
	}

	public List<String> getResourceRelationResourceRelationType() {
		return resourceRelationResourceRelationType;
	}



	public List<String> getResourceRelationLastDateTimeVerified() {
		return resourceRelationLastDateTimeVerified;
	}



	public List<String> getResourceRelationId() {
		return resourceRelationId;
	}



	public List<String> getResourceRelationLang() {
		return resourceRelationLang;
	}



	public List<String> getResourceRelationHref() {
		return resourceRelationHref;
	}



	public String getRelationsId() {
		return relationsId;
	}



	public String getRelationsLang() {
		return relationsLang;
	}



	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}



	public void setOtherRepositorId(String otherRepositorId) {
		this.otherRepositorId = otherRepositorId;
	}



	public void setAutform(String autform) {
		this.autform = autform;
	}



	public void setControlId(String controlId) {
		this.controlId = controlId;
	}



	public void setControlLanguage(String controlLanguage) {
		this.controlLanguage = controlLanguage;
	}



	public void setRecordIdId(String recordIdId) {
		this.recordIdId = recordIdId;
	}



	public void setRecordIdValue(String recordIdValue) {
		this.recordIdValue = recordIdValue;
	}



	public void setOtherRecordIdId(List<String> otherRecordIdId) {
		this.otherRecordIdId = otherRecordIdId;
	}



	public void setOtherRecordIdValue(List<String> otherRecordIdValue) {
		this.otherRecordIdValue = otherRecordIdValue;
	}



	public void setSourcesId(String sourcesId) {
		this.sourcesId = sourcesId;
	}



	public void setSourcesLang(String sourcesLang) {
		this.sourcesLang = sourcesLang;
	}



	public void setSourceHref(List<String> sourceHref) {
		this.sourceHref = sourceHref;
	}



	public void setSourceId(List<String> sourceId) {
		this.sourceId = sourceId;
	}



	public void setSourceLastDateTimeVerified(List<String> sourceLastDateTimeVerified) {
		this.sourceLastDateTimeVerified = sourceLastDateTimeVerified;
	}



	public void setMaintenanceAgencyId(String maintenanceAgencyId) {
		this.maintenanceAgencyId = maintenanceAgencyId;
	}



	public void setAgencyCodeId(String agencyCodeId) {
		this.agencyCodeId = agencyCodeId;
	}



	public void setAgencyCodeValue(String agencyCodeValue) {
		this.agencyCodeValue = agencyCodeValue;
	}



	public void setAgencyNameLang(String agencyNameLang) {
		this.agencyNameLang = agencyNameLang;
	}



	public void setAgencyNameId(String agencyNameId) {
		this.agencyNameId = agencyNameId;
	}



	public void setAgencyNameValue(String agencyNameValue) {
		this.agencyNameValue = agencyNameValue;
	}



	public void setOtherAgencyCodeId(List<String> otherAgencyCodeId) {
		this.otherAgencyCodeId = otherAgencyCodeId;
	}



	public void setOtherAgencyCodeValue(List<String> otherAgencyCodeValue) {
		this.otherAgencyCodeValue = otherAgencyCodeValue;
	}



	public void setMaintenanceStatusId(String maintenanceStatusId) {
		this.maintenanceStatusId = maintenanceStatusId;
	}



	public void setMaintenanceStatusValue(String maintenanceStatusValue) {
		this.maintenanceStatusValue = maintenanceStatusValue;
	}



	public void setMaintenanceHistoryId(String maintenanceHistoryId) {
		this.maintenanceHistoryId = maintenanceHistoryId;
	}



	public void setMaintenanceHistoryLang(String maintenanceHistoryLang) {
		this.maintenanceHistoryLang = maintenanceHistoryLang;
	}



	public void setMaintenanceEventId(List<String> maintenanceEventId) {
		this.maintenanceEventId = maintenanceEventId;
	}



	public void setMaintenanceEventLang(List<String> maintenanceEventLang) {
		this.maintenanceEventLang = maintenanceEventLang;
	}



	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}



	public void setAgentLang(String agentLang) {
		this.agentLang = agentLang;
	}



	public void setAgentValue(String agentValue) {
		this.agentValue = agentValue;
	}



	public void setAgentTypeId(String agentTypeId) {
		this.agentTypeId = agentTypeId;
	}



	public void setAgentTypeValue(String agentTypeValue) {
		this.agentTypeValue = agentTypeValue;
	}



	public void setEventDateTimeStandardDateTime(
			String eventDateTimeStandardDateTime) {
		this.eventDateTimeStandardDateTime = eventDateTimeStandardDateTime;
	}



	public void setEventDateTimeLang(String eventDateTimeLang) {
		this.eventDateTimeLang = eventDateTimeLang;
	}



	public void setEventDateTimeId(String eventDateTimeId) {
		this.eventDateTimeId = eventDateTimeId;
	}



	public void setEventDateTimeValue(String eventDateTimeValue) {
		this.eventDateTimeValue = eventDateTimeValue;
	}



	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}



	public void setEventTypeValue(String eventTypeValue) {
		this.eventTypeValue = eventTypeValue;
	}


	public void setLanguageDeclarationLang(List<String> languageDeclarationLang) {
		this.languageDeclarationLang = languageDeclarationLang;
	}


	public void setLanguageLanguageCode(String languageLanguageCode) {
		this.languageLanguageCode = languageLanguageCode;
	}



	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}



	public void setScriptScriptCode(String scriptScriptCode) {
		this.scriptScriptCode = scriptScriptCode;
	}



	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}



	public void setScriptLang(String scriptLang) {
		this.scriptLang = scriptLang;
	}



	public void setScriptValue(String scriptValue) {
		this.scriptValue = scriptValue;
	}



	public void setLanguageLang(String languageLang) {
		this.languageLang = languageLang;
	}



	public void setLanguageValue(String languageValue) {
		this.languageValue = languageValue;
	}



	public void setConventionDeclarationId(List<String> conventionDeclarationId) {
		this.conventionDeclarationId = conventionDeclarationId;
	}



	public void setConventionDeclarationLang(List<String> conventionDeclarationLang) {
		this.conventionDeclarationLang = conventionDeclarationLang;
	}



	public void setAbbreviationId(String abbreviationId) {
		this.abbreviationId = abbreviationId;
	}



	public void setAbbreviationLang(String abbreviationLang) {
		this.abbreviationLang = abbreviationLang;
	}



	public void setAbbreviationValue(String abbreviationValue) {
		this.abbreviationValue = abbreviationValue;
	}



	public void setCitationId(List<String> citationId) {
		this.citationId = citationId;
	}



	public void setCitationLang(List<String> citationLang) {
		this.citationLang = citationLang;
	}



	public void setCitationLastDateTimeVerified(List<String> citationLastDateTimeVerified) {
		this.citationLastDateTimeVerified = citationLastDateTimeVerified;
	}



	public void setCitationHref(List<String> citationHref) {
		this.citationHref = citationHref;
	}



	public void setCitationValue(List<String> citationValue) {
		this.citationValue = citationValue;
	}



	public void setDescriptiveNoteLang(String descriptiveNoteLang) {
		this.descriptiveNoteLang = descriptiveNoteLang;
	}



	public void setDescriptiveNoteId(String descriptiveNoteId) {
		this.descriptiveNoteId = descriptiveNoteId;
	}



	public void setPId(List<String> descriptiveNotePId) {
		this.descriptiveNotePId = descriptiveNotePId;
	}



	public void setPLang(List<String> descriptiveNotePLang) {
		this.descriptiveNotePLang = descriptiveNotePLang;
	}



	public void setPValue(List<String> descriptiveNotePValue) {
		this.descriptiveNotePValue = descriptiveNotePValue;
	}



	public void setLocalControlId(List<String> localControlId) {
		this.localControlId = localControlId;
	}



	public void setLocalControlLang(List<String> localControlLang) {
		this.localControlLang = localControlLang;
	}



	public void setTermLastDateTimeVerified(List<String> termLastDateTimeVerified) {
		this.termLastDateTimeVerified = termLastDateTimeVerified;
	}



	public void setTermScriptCode(List<String> termScriptCode) {
		this.termScriptCode = termScriptCode;
	}


	public void setTermId(List<String> termId) {
		this.termId = termId;
	}



	public void setTermLang(List<String> termLang) {
		this.termLang = termLang;
	}



	public void setTermValue(List<String> termValue) {
		this.termValue = termValue;
	}



	public void setDateNotAfter(List<String> dateNotAfter) {
		this.dateNotAfter = dateNotAfter;
	}



	public void setDateNotBefore(List<String> dateNotBefore) {
		this.dateNotBefore = dateNotBefore;
	}



	public void setDateStandardDate(List<String> dateStandardDate) {
		this.dateStandardDate = dateStandardDate;
	}



	public void setDateId(List<String> dateId) {
		this.dateId = dateId;
	}



	public void setDateLang(List<String> dateLang) {
		this.dateLang = dateLang;
	}



	public void setDateValue(List<String> dateValue) {
		this.dateValue = dateValue;
	}



	public void setDateRangeId(List<String> dateRangeId) {
		this.dateRangeId = dateRangeId;
	}



	public void setToDateNotAfter(String toDateNotAfter) {
		this.toDateNotAfter = toDateNotAfter;
	}



	public void setToDateNotBefore(String toDateNotBefore) {
		this.toDateNotBefore = toDateNotBefore;
	}



	public void setToDateStandardDate(String toDateStandardDate) {
		this.toDateStandardDate = toDateStandardDate;
	}



	public void setDateRangeLang(List<String> dateRangeLang) {
		this.dateRangeLang = dateRangeLang;
	}



	public void setFromDateNotAfter(String fromDateNotAfter) {
		this.fromDateNotAfter = fromDateNotAfter;
	}



	public void setFromDateNotBefore(String fromDateNotBefore) {
		this.fromDateNotBefore = fromDateNotBefore;
	}



	public void setFromDateStandardDate(String fromDateStandardDate) {
		this.fromDateStandardDate = fromDateStandardDate;
	}



	public void setFromDateId(String fromDateId) {
		this.fromDateId = fromDateId;
	}



	public void setFromDateLang(String fromDateLang) {
		this.fromDateLang = fromDateLang;
	}



	public void setFromDateValue(String fromDateValue) {
		this.fromDateValue = fromDateValue;
	}



	public void setToDateId(String toDateId) {
		this.toDateId = toDateId;
	}



	public void setToDateLang(String toDateLang) {
		this.toDateLang = toDateLang;
	}



	public void setToDateValue(String toDateValue) {
		this.toDateValue = toDateValue;
	}



	public void setLocalTypeDeclarationId(List<String> localTypeDeclarationId) {
		this.localTypeDeclarationId = localTypeDeclarationId;
	}



	public void setLocalTypeDeclarationLang(List<String> localTypeDeclarationLang) {
		this.localTypeDeclarationLang = localTypeDeclarationLang;
	}



	public void setPublicationStatusId(String publicationStatusId) {
		this.publicationStatusId = publicationStatusId;
	}



	public void setPublicationStatusValue(String publicationStatusValue) {
		this.publicationStatusValue = publicationStatusValue;
	}



	public void setSourceEntryScriptCode(String sourceEntryScriptCode) {
		this.sourceEntryScriptCode = sourceEntryScriptCode;
	}



	public void setSourceEntryId(String sourceEntryId) {
		this.sourceEntryId = sourceEntryId;
	}



	public void setSourceEntryLang(String sourceEntryLang) {
		this.sourceEntryLang = sourceEntryLang;
	}



	public void setSourceEntryValue(String sourceEntryValue) {
		this.sourceEntryValue = sourceEntryValue;
	}



	public void setObjectXMLWrapId(List<String> objectXMLWrapId) {
		this.objectXMLWrapId = objectXMLWrapId;
	}



	public void setObjectBinWrapId(String objectBinWrapId) {
		this.objectBinWrapId = objectBinWrapId;
	}



	public void setRepositoridCountrycode(String repositoridCountrycode) {
		this.repositoridCountrycode = repositoridCountrycode;
	}



	public void setRepositoridRepositorycode(String repositoridRepositorycode) {
		this.repositoridRepositorycode = repositoridRepositorycode;
	}



	public void setOtherRepositorIdValue(String otherRepositorIdValue) {
		this.otherRepositorIdValue = otherRepositorIdValue;
	}



	public void setAutformLang(List<String> autformLang) {
		this.autformLang = autformLang;
	}



	public void setAutformValue(List<String> autformValue) {
		this.autformValue = autformValue;
	}



	public void setParformLang(List<String> parformLang) {
		this.parformLang = parformLang;
	}



	public void setParformValue(List<String> parformValue) {
		this.parformValue = parformValue;
	}



	public void setRepositoryTypeValue(List<String> repositoryTypeValue) {
		this.repositoryTypeValue = repositoryTypeValue;
	}



	public void setNonpreformLang(List<String> nonpreformLang) {
		this.nonpreformLang = nonpreformLang;
	}



	public void setNonpreformValue(List<String> nonpreformValue) {
		this.nonpreformValue = nonpreformValue;
	}



	public void setUseDatesId(String useDatesId) {
		this.useDatesId = useDatesId;
	}



	public void setUseDatesLang(String useDatesLang) {
		this.useDatesLang = useDatesLang;
	}



	public void setDateSetId(List<String> dateSetId) {
		this.dateSetId = dateSetId;
	}



	public void setDateSetLang(List<String> dateSetLang) {
		this.dateSetLang = dateSetLang;
	}



	public void setRepositoryNameLang(List<String> repositoryNameLang) {
		this.repositoryNameLang = repositoryNameLang;
	}



	public void setRepositoryNameValue(List<String> repositoryNameValue) {
		this.repositoryNameValue = repositoryNameValue;
	}



	public void setRepositoryRoleValue(String repositoryRoleValue) {
		this.repositoryRoleValue = repositoryRoleValue;
	}



	public void setGeogareaLang(String geogareaLang) {
		this.geogareaLang = geogareaLang;
	}



	public void setGeogareaValue(String geogareaValue) {
		this.geogareaValue = geogareaValue;
	}



	public void setLocationLocalType(List<String> locationLocalType) {
		this.locationLocalType = locationLocalType;
	}



	public void setLocationLatitude(List<String> locationLatitude) {
		this.locationLatitude = locationLatitude;
	}



	public void setLocationLongitude(List<String> locationLongitude) {
		this.locationLongitude = locationLongitude;
	}



	public void setCountryLang(String countryLang) {
		this.countryLang = countryLang;
	}



	public void setCountryValue(String countryValue) {
		this.countryValue = countryValue;
	}



	public void setFirstdemLang(String firstdemLang) {
		this.firstdemLang = firstdemLang;
	}



	public void setFirstdemValue(String firstdemValue) {
		this.firstdemValue = firstdemValue;
	}



	public void setSecondemLang(String secondemLang) {
		this.secondemLang = secondemLang;
	}



	public void setSecondemValue(String secondemValue) {
		this.secondemValue = secondemValue;
	}



	public void setMunicipalityPostalcodeLang(String municipalityPostalcodeLang) {
		this.municipalityPostalcodeLang = municipalityPostalcodeLang;
	}



	public void setMunicipalityPostalcodeValue(String municipalityPostalcodeValue) {
		this.municipalityPostalcodeValue = municipalityPostalcodeValue;
	}



	public void setLocalentityLang(String localentityLang) {
		this.localentityLang = localentityLang;
	}



	public void setLocalentityValue(String localentityValue) {
		this.localentityValue = localentityValue;
	}



	public void setStreetLang(String streetLang) {
		this.streetLang = streetLang;
	}



	public void setStreetValue(String streetValue) {
		this.streetValue = streetValue;
	}



	public void setDirectionsLang(List<String> directionsLang) {
		this.directionsLang = directionsLang;
	}



	public void setDirectionsValue(List<String> directionsValue) {
		this.directionsValue = directionsValue;
	}



	public void setAdminunitLang(List<String> adminunitLang) {
		this.adminunitLang = adminunitLang;
	}



	public void setAdminunitValue(List<String> adminunitValue) {
		this.adminunitValue = adminunitValue;
	}



	public void setOpeningValue(String openingValue) {
		this.openingValue = openingValue;
	}



	public void setClosingStandardDate(String closingStandardDate) {
		this.closingStandardDate = closingStandardDate;
	}



	public void setClosingValue(String closingValue) {
		this.closingValue = closingValue;
	}



	public void setAccessQuestion(String accessQuestion) {
		this.accessQuestion = accessQuestion;
	}



	public void setRestaccessLang(List<String> restaccessLang) {
		this.restaccessLang = restaccessLang;
	}



	public void setRestaccessValue(List<String> restaccessValue) {
		this.restaccessValue = restaccessValue;
	}



	public void setTermsOfUseHref(List<String> termsOfUseHref) {
		this.termsOfUseHref = termsOfUseHref;
	}



	public void setTermsOfUseLang(List<String> termsOfUseLang) {
		this.termsOfUseLang = termsOfUseLang;
	}



	public void setTermsOfUseValue(List<String> termsOfUseValue) {
		this.termsOfUseValue = termsOfUseValue;
	}



	public void setAccessibilityQuestion(List<String> accessibilityQuestion) {
		this.accessibilityQuestion = accessibilityQuestion;
	}



	public void setAccessibilityLang(List<String> accessibilityLang) {
		this.accessibilityLang = accessibilityLang;
	}



	public void setAccessibilityValue(List<String> accessibilityValue) {
		this.accessibilityValue = accessibilityValue;
	}



	public void setPhotographAllowanceValue(String photographAllowanceValue) {
		this.photographAllowanceValue = photographAllowanceValue;
	}



	public void setReadersTicketHref(List<String> readersTicketHref) {
		this.readersTicketHref = readersTicketHref;
	}



	public void setReadersTicketLang(List<String> readersTicketLang) {
		this.readersTicketLang = readersTicketLang;
	}



	public void setReadersTicketValue(List<String> readersTicketValue) {
		this.readersTicketValue = readersTicketValue;
	}



	public void setAdvancedOrdersHref(List<String> advancedOrdersHref) {
		this.advancedOrdersHref = advancedOrdersHref;
	}



	public void setAdvancedOrdersLang(List<String> advancedOrdersLang) {
		this.advancedOrdersLang = advancedOrdersLang;
	}



	public void setAdvancedOrdersValue(List<String> advancedOrdersValue) {
		this.advancedOrdersValue = advancedOrdersValue;
	}



	public void setLibraryQuestion(String libraryQuestion) {
		this.libraryQuestion = libraryQuestion;
	}



	public void setInternetAccessQuestion(String internetAccessQuestion) {
		this.internetAccessQuestion = internetAccessQuestion;
	}



	public void setReproductionserQuestion(String reproductionserQuestion) {
		this.reproductionserQuestion = reproductionserQuestion;
	}



	public void setMicroformserQuestion(String microformserQuestion) {
		this.microformserQuestion = microformserQuestion;
	}



	public void setPhotographserQuestion(String photographserQuestion) {
		this.photographserQuestion = photographserQuestion;
	}



	public void setDigitalserQuestion(String digitalserQuestion) {
		this.digitalserQuestion = digitalserQuestion;
	}



	public void setPhotocopyserQuestion(String photocopyserQuestion) {
		this.photocopyserQuestion = photocopyserQuestion;
	}



	public void setRestorationlabQuestion(String restorationlabQuestion) {
		this.restorationlabQuestion = restorationlabQuestion;
	}



	public void setNumValue(String numValue) {
		this.numValue = numValue;
	}



	public void setRuleLang(List<String> ruleLang) {
		this.ruleLang = ruleLang;
	}



	public void setRuleValue(List<String> ruleValue) {
		this.ruleValue = ruleValue;
	}



	public void setWebpageHref(List<String> webpageHref) {
		this.webpageHref = webpageHref;
	}


	public void setWebpageValue(List<String> webpageValue) {
		this.webpageValue = webpageValue;
	}


	public void setEmailHref(List<String> emailHref) {
		this.emailHref = emailHref;
	}


	public void setEmailValue(List<String> emailValue) {
		this.emailValue = emailValue;
	}


	public void setFaxValue(List<String> faxValue) {
		this.faxValue = faxValue;
	}


	public void setTelephoneValue(List<String> telephoneValue) {
		this.telephoneValue = telephoneValue;
	}

	public void setRelationEntryId(List<String> relationEntryId) {
		this.relationEntryId = relationEntryId;
	}


	public void setRelationEntryScriptCode(List<String> relationEntryScriptCode) {
		this.relationEntryScriptCode = relationEntryScriptCode;
	}



	public void setRelationEntryLang(List<String> relationEntryLang) {
		this.relationEntryLang = relationEntryLang;
	}



	public void setRelationEntryValue(List<String> relationEntryValue) {
		this.relationEntryValue = relationEntryValue;
	}



	public void setEagRelationEagRelationType(List<String> eagRelationEagRelationType) {
		this.eagRelationEagRelationType = eagRelationEagRelationType;
	}



	public void setEagRelationHref(List<String> eagRelationHref) {
		this.eagRelationHref = eagRelationHref;
	}



	public void setPlaceEntryAccuracy(List<String> placeEntryAccuracy) {
		this.placeEntryAccuracy = placeEntryAccuracy;
	}



	public void setPlaceEntryAltitude(List<String> placeEntryAltitude) {
		this.placeEntryAltitude = placeEntryAltitude;
	}



	public void setPlaceEntryId(List<String> placeEntryId) {
		this.placeEntryId = placeEntryId;
	}



	public void setPlaceEntryLang(List<String> placeEntryLang) {
		this.placeEntryLang = placeEntryLang;
	}



	public void setPlaceEntryCountryCode(List<String> placeEntryCountryCode) {
		this.placeEntryCountryCode = placeEntryCountryCode;
	}



	public void setPlaceEntryLatitude(List<String> placeEntryLatitude) {
		this.placeEntryLatitude = placeEntryLatitude;
	}



	public void setPlaceEntryLongitude(List<String> placeEntryLongitude) {
		this.placeEntryLongitude = placeEntryLongitude;
	}



	public void setPlaceEntryScriptCode(List<String> placeEntryScriptCode) {
		this.placeEntryScriptCode = placeEntryScriptCode;
	}



	public void setResourceRelationResourceRelationType(
			List<String> resourceRelationResourceRelationType) {
		this.resourceRelationResourceRelationType = resourceRelationResourceRelationType;
	}



	public void setResourceRelationLastDateTimeVerified(
			List<String> resourceRelationLastDateTimeVerified) {
		this.resourceRelationLastDateTimeVerified = resourceRelationLastDateTimeVerified;
	}



	public void setResourceRelationId(List<String> resourceRelationId) {
		this.resourceRelationId = resourceRelationId;
	}



	public void setResourceRelationLang(List<String> resourceRelationLang) {
		this.resourceRelationLang = resourceRelationLang;
	}



	public void setResourceRelationHref(List<String> resourceRelationHref) {
		this.resourceRelationHref = resourceRelationHref;
	}



	public void setRelationsId(String relationsId) {
		this.relationsId = relationsId;
	}



	public void setRelationsLang(String relationsLang) {
		this.relationsLang = relationsLang;
	}

	
	public void setPlaceEntryValue(List<String> placeEntryValue) {
		this.placeEntryValue = placeEntryValue;
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
	private static String generatesISOCode(Integer archivalInstitutionId) {
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