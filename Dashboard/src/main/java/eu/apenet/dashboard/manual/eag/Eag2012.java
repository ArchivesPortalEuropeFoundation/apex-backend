package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.IOException;

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
	private String otherRecordIdId;
	private String otherRecordIdValue;
	private String sourcesId;
	private String sourcesLang;
	private String sourceHref;
	private String sourceId;
	private String sourceLastDateTimeVerified;
	private String maintenanceAgencyId;
	private String agencyCodeId;
	private String agencyCodeValue;
	private String agencyNameLang;
	private String agencyNameId;
	private String agencyNameValue;
	private String otherAgencyCodeId;
	private String otherAgencyCodeValue;
	private String maintenanceStatusId;
	private String maintenanceStatusValue;
	private String maintenanceHistoryId;
	private String maintenanceHistoryLang;
	private String maintenanceEventId;
	private String maintenanceEventLang;
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
	private String languageDeclarationId;
	private String languageDeclarationLang;
	private String languageLanguageCode;
	private String languageId;
	private String scriptScriptCode;
	private String scriptId;
	private String scriptLang;
	private String scriptValue;
	private String languageLang;
	private String languageValue;
	private String conventionDeclarationId;
	private String convetionDeclarationLang;
	private String abbreviationId;
	private String abbreviationLang;
	private String abbreviationValue;
	private String citationId;
	private String citationLang;
	private String citationLastDateTimeVerified;
	private String citationHref;
	private String citationValue;
	private String descriptiveNoteLang;
	private String descriptiveNoteId;
	private String descriptiveNotePId;
	private String descriptiveNotePLang;
	private String descriptiveNotePValue;
	private String localControlId;
	private String localControlLang;
	private String termLastDateTimeVerified;
	private String termScriptCode;
	private String termId;
	private String termLang;
	private String termValue;
	private String dateNotAfter;
	private String dateNotBefore;
	private String dateStandardDate;
	private String dateId;
	private String dateLang;
	private String dateValue;
	private String dateRangeId;
	private String toDateNotAfter;
	private String toDateNotBefore;
	private String toDateStandardDate;
	private String dateRangeLang;
	private String fromDateNotAfter;
	private String fromDateNotBefore;
	private String fromDateStandardDate;
	private String fromDateId;
	private String fromDateLang;
	private String fromDateValue;
	private String toDateId;
	private String toDateLang;
	private String toDateValue;
	private String localTypeDeclarationId;
	private String localTypeDeclarationLang;
	private String publicationStatusId;
	private String publicationStatusValue;
	private String sourceEntryScriptCode;
	private String sourceEntryId;
	private String sourceEntryLang;
	private String sourceEntryValue;
	private String objectXMLWrapId;
	private String objectBinWrapId;
	private String repositoridCountrycode;
	private String repositoridRepositorycode;
	private String otherRepositorIdValue;
	private String autformLang;
	private String autformValue;
	private String parformLang;
	private String parformValue;
	private String repositoryTypeValue;
	private String nonpreformLang;
	private String nonpreformValue;
	private String useDatesId;
	private String useDatesLang;
	private String dateSetId;
	private String dateSetLang;
	private String repositoryNameLang;
	private String repositoryNameValue;
	private String repositoryRoleValue;
	private String geogareaLang;
	private String geogareaValue;
	private String locationLocalType;
	private String locationLatitude;
	private String locationLongitude;
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
	private String directionsLang;
	private String directionsValue;
	private String adminunitLang;
	private String adminunitValue;
	private String openingValue;
	private String closingStandardDate;
	private String closingValue;
	private String accessQuestion;
	private String restaccessLang;
	private String restaccessValue;
	private String termsOfUseHref;
	private String termsOfUseLang;
	private String termsOfUseValue;
	private String accessibilityQuestion;
	private String accessibilityLang;
	private String accessibilityValue;
	private String photographAllowanceValue;
	private String readersTicketHref;
	private String readersTicketLang;
	private String readersTicketValue;
	private String advancedOrdersHref;
	private String advancedOrdersLang;
	private String advancedOrdersValue;
	private String libraryQuestion;
	private String internetAccessQuestion;
	private String reproductionserQuestion;
	private String microformserQuestion;
	private String photographserQuestion;
	private String digitalserQuestion;
	private String photocopyserQuestion;
	private String restorationlabQuestion;
	private String numValue;
	private String ruleLang;
	private String ruleValue;
	private String webpageHref;
	private String webpageValue;
	private String emailHref;
	private String emailValue;
	private String faxValue;
	private String telephoneValue;
	private String relationEntryScriptCode;
	private String relationEntryId;
	private String relationEntryLang;
	private String relationEntryValue;
	private String eagRelationEagRelationType;
	private String eagRelationHref;
	private String placeEntryAccuracy;
	private String placeEntryAltitude;
	private String placeEntryId;
	private String placeEntryLang;
	private String placeEntryCountryCode;
	private String placeEntryLatitude;
	private String placeEntryLongitude;
	private String placeEntryScriptCode;
	private String resourceRelationResourceRelationType;
	private String resourceRelationLastDateTimeVerified;
	private String resourceRelationId;
	private String resourceRelationLang;
	private String resourceRelationHref;
	private String relationsId;
	private String relationsLang;
	private String placeEntryValue;

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

	public String getOtherRecordIdId() {
		return this.otherRecordIdId;
	}

	public String getOtherRecordIdValue() {
		return this.otherRecordIdValue;
	}

	public String getSourcesId() {
		return this.sourcesId;
	}

	public String getSourcesLang() {
		return this.sourcesLang;
	}

	public String getSourceLastDateTimeVerified() {
		return sourceLastDateTimeVerified;
	}

	public String getSourceHref() {
		return sourceHref;
	}

	public String getSourceId() {
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


	public String getOtherAgencyCodeId() {
		return otherAgencyCodeId;
	}

	public String getOtherAgencyCodeValue() {
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



	public String getMaintenanceEventId() {
		return maintenanceEventId;
	}



	public String getMaintenanceEventLang() {
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



	public String getLanguageDeclarationId() {
		return languageDeclarationId;
	}



	public String getLanguageDeclarationLang() {
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



	public String getConventionDeclarationId() {
		return conventionDeclarationId;
	}



	public String getConvetionDeclarationLang() {
		return convetionDeclarationLang;
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



	public String getCitationId() {
		return citationId;
	}



	public String getCitationLang() {
		return citationLang;
	}



	public String getCitationLastDateTimeVerified() {
		return citationLastDateTimeVerified;
	}



	public String getCitationHref() {
		return citationHref;
	}



	public String getCitationValue() {
		return citationValue;
	}



	public String getDescriptiveNoteLang() {
		return descriptiveNoteLang;
	}



	public String getDescriptiveNoteId() {
		return descriptiveNoteId;
	}



	public String getDescriptiveNotePId() {
		return descriptiveNotePId;
	}



	public String getDescriptiveNotePLang() {
		return descriptiveNotePLang;
	}



	public String getDescriptiveNotePValue() {
		return descriptiveNotePValue;
	}


	public String getLocalControlId() {
		return localControlId;
	}



	public String getLocalControlLang() {
		return localControlLang;
	}



	public String getTermLastDateTimeVerified() {
		return termLastDateTimeVerified;
	}



	public String getTermScriptCode() {
		return termScriptCode;
	}


	public String getTermId() {
		return termId;
	}



	public String getTermLang() {
		return termLang;
	}



	public String getTermValue() {
		return termValue;
	}



	public String getDateNotAfter() {
		return dateNotAfter;
	}



	public String getDateNotBefore() {
		return dateNotBefore;
	}



	public String getDateStandardDate() {
		return dateStandardDate;
	}



	public String getDateId() {
		return dateId;
	}



	public String getDateLang() {
		return dateLang;
	}



	public String getDateValue() {
		return dateValue;
	}

	public String getDateRangeLang() {
		return dateRangeLang;
	}

	public String getDateRangeId() {
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



	public String getLocalTypeDeclarationId() {
		return localTypeDeclarationId;
	}



	public String getLocalTypeDeclarationLang() {
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



	public String getObjectXMLWrapId() {
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



	public String getAutformLang() {
		return autformLang;
	}



	public String getAutformValue() {
		return autformValue;
	}



	public String getParformLang() {
		return parformLang;
	}



	public String getParformValue() {
		return parformValue;
	}



	public String getRepositoryTypeValue() {
		return repositoryTypeValue;
	}



	public String getNonpreformLang() {
		return nonpreformLang;
	}



	public String getNonpreformValue() {
		return nonpreformValue;
	}



	public String getUseDatesId() {
		return useDatesId;
	}



	public String getUseDatesLang() {
		return useDatesLang;
	}



	public String getDateSetId() {
		return dateSetId;
	}



	public String getDateSetLang() {
		return dateSetLang;
	}



	public String getRepositoryNameLang() {
		return repositoryNameLang;
	}



	public String getRepositoryNameValue() {
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



	public String getLocationLocalType() {
		return locationLocalType;
	}



	public String getLocationLatitude() {
		return locationLatitude;
	}



	public String getLocationLongitude() {
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



	public String getDirectionsLang() {
		return directionsLang;
	}



	public String getDirectionsValue() {
		return directionsValue;
	}



	public String getAdminunitLang() {
		return adminunitLang;
	}



	public String getAdminunitValue() {
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



	public String getRestaccessLang() {
		return restaccessLang;
	}



	public String getRestaccessValue() {
		return restaccessValue;
	}



	public String getTermsOfUseHref() {
		return termsOfUseHref;
	}



	public String getTermsOfUseLang() {
		return termsOfUseLang;
	}



	public String getTermsOfUseValue() {
		return termsOfUseValue;
	}



	public String getAccessibilityQuestion() {
		return accessibilityQuestion;
	}



	public String getAccessibilityLang() {
		return accessibilityLang;
	}



	public String getAccessibilityValue() {
		return accessibilityValue;
	}



	public String getPhotographAllowanceValue() {
		return photographAllowanceValue;
	}



	public String getReadersTicketHref() {
		return readersTicketHref;
	}



	public String getReadersTicketLang() {
		return readersTicketLang;
	}



	public String getReadersTicketValue() {
		return readersTicketValue;
	}



	public String getAdvancedOrdersHref() {
		return advancedOrdersHref;
	}



	public String getAdvancedOrdersLang() {
		return advancedOrdersLang;
	}



	public String getAdvancedOrdersValue() {
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



	public String getRuleLang() {
		return ruleLang;
	}



	public String getRuleValue() {
		return ruleValue;
	}



	public String getWebpageHref() {
		return webpageHref;
	}



	public String getWebpageValue() {
		return webpageValue;
	}



	public String getEmailHref() {
		return emailHref;
	}



	public String getEmailValue() {
		return emailValue;
	}



	public String getFaxValue() {
		return faxValue;
	}



	public String getTelephoneValue() {
		return telephoneValue;
	}



	public String getRelationEntryScriptCode() {
		return relationEntryScriptCode;
	}


	public String getRelationEntryId() {
		return relationEntryId;
	}



	public String getRelationEntryLang() {
		return relationEntryLang;
	}



	public String getRelationEntryValue() {
		return relationEntryValue;
	}



	public String getEagRelationEagRelationType() {
		return eagRelationEagRelationType;
	}



	public String getEagRelationHref() {
		return eagRelationHref;
	}



	public String getPlaceEntryAccuracy() {
		return placeEntryAccuracy;
	}



	public String getPlaceEntryAltitude() {
		return placeEntryAltitude;
	}



	public String getPlaceEntryId() {
		return placeEntryId;
	}



	public String getPlaceEntryLang() {
		return placeEntryLang;
	}



	public String getPlaceEntryCountryCode() {
		return placeEntryCountryCode;
	}



	public String getPlaceEntryLatitude() {
		return placeEntryLatitude;
	}



	public String getPlaceEntryLongitude() {
		return placeEntryLongitude;
	}



	public String getPlaceEntryScriptCode() {
		return placeEntryScriptCode;
	}



	public String getResourceRelationResourceRelationType() {
		return resourceRelationResourceRelationType;
	}



	public String getResourceRelationLastDateTimeVerified() {
		return resourceRelationLastDateTimeVerified;
	}



	public String getResourceRelationId() {
		return resourceRelationId;
	}



	public String getResourceRelationLang() {
		return resourceRelationLang;
	}



	public String getResourceRelationHref() {
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



	public void setOtherRecordIdId(String otherRecordIdId) {
		this.otherRecordIdId = otherRecordIdId;
	}



	public void setOtherRecordIdValue(String otherRecordIdValue) {
		this.otherRecordIdValue = otherRecordIdValue;
	}



	public void setSourcesId(String sourcesId) {
		this.sourcesId = sourcesId;
	}



	public void setSourcesLang(String sourcesLang) {
		this.sourcesLang = sourcesLang;
	}



	public void setSourceHref(String sourceHref) {
		this.sourceHref = sourceHref;
	}



	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}



	public void setSourceLastDateTimeVerified(String sourceLastDateTimeVerified) {
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



	public void setOtherAgencyCodeId(String otherAgencyCodeId) {
		this.otherAgencyCodeId = otherAgencyCodeId;
	}



	public void setOtherAgencyCodeValue(String otherAgencyCodeValue) {
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



	public void setMaintenanceEventId(String maintenanceEventId) {
		this.maintenanceEventId = maintenanceEventId;
	}



	public void setMaintenanceEventLang(String maintenanceEventLang) {
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



	/*public void setLanguageDeclaration(String languageDeclaration) {
		this.languageDeclaration = languageDeclaration;
	}*/



	public void setLanguageDeclarationLang(String languageDeclarationLang) {
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



	public void setConventionDeclarationId(String conventionDeclarationId) {
		this.conventionDeclarationId = conventionDeclarationId;
	}



	public void setConvetionDeclarationLang(String convetionDeclarationLang) {
		this.convetionDeclarationLang = convetionDeclarationLang;
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



	public void setCitationId(String citationId) {
		this.citationId = citationId;
	}



	public void setCitationLang(String citationLang) {
		this.citationLang = citationLang;
	}



	public void setCitationLastDateTimeVerified(String citationLastDateTimeVerified) {
		this.citationLastDateTimeVerified = citationLastDateTimeVerified;
	}



	public void setCitationHref(String citationHref) {
		this.citationHref = citationHref;
	}



	public void setCitationValue(String citationValue) {
		this.citationValue = citationValue;
	}



	public void setDescriptiveNoteLang(String descriptiveNoteLang) {
		this.descriptiveNoteLang = descriptiveNoteLang;
	}



	public void setDescriptiveNoteId(String descriptiveNoteId) {
		this.descriptiveNoteId = descriptiveNoteId;
	}



	public void setDescriptiveNotePId(String descriptiveNotePId) {
		this.descriptiveNotePId = descriptiveNotePId;
	}



	public void setDescriptiveNotePLang(String descriptiveNotePLang) {
		this.descriptiveNotePLang = descriptiveNotePLang;
	}



	public void setDescriptiveNotePValue(String descriptiveNotePValue) {
		this.descriptiveNotePValue = descriptiveNotePValue;
	}



	public void setLocalControlId(String localControlId) {
		this.localControlId = localControlId;
	}



	public void setLocalControlLang(String localControlLang) {
		this.localControlLang = localControlLang;
	}



	public void setTermLastDateTimeVerified(String termLastDateTimeVerified) {
		this.termLastDateTimeVerified = termLastDateTimeVerified;
	}



	public void setTermScriptCode(String termScriptCode) {
		this.termScriptCode = termScriptCode;
	}


	public void setTermId(String termId) {
		this.termId = termId;
	}



	public void setTermLang(String termLang) {
		this.termLang = termLang;
	}



	public void setTermValue(String termValue) {
		this.termValue = termValue;
	}



	public void setDateNotAfter(String dateNotAfter) {
		this.dateNotAfter = dateNotAfter;
	}



	public void setDateNotBefore(String dateNotBefore) {
		this.dateNotBefore = dateNotBefore;
	}



	public void setDateStandardDate(String dateStandardDate) {
		this.dateStandardDate = dateStandardDate;
	}



	public void setDateId(String dateId) {
		this.dateId = dateId;
	}



	public void setDateLang(String dateLang) {
		this.dateLang = dateLang;
	}



	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}



	public void setDateRangeId(String dateRangeId) {
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



	public void setDateRangeLang(String dateRangeLang) {
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



	public void setLocalTypeDeclarationId(String localTypeDeclarationId) {
		this.localTypeDeclarationId = localTypeDeclarationId;
	}



	public void setLocalTypeDeclarationLang(String localTypeDeclarationLang) {
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



	public void setObjectXMLWrapId(String objectXMLWrapId) {
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



	public void setAutformLang(String autformLang) {
		this.autformLang = autformLang;
	}



	public void setAutformValue(String autformValue) {
		this.autformValue = autformValue;
	}



	public void setParformLang(String parformLang) {
		this.parformLang = parformLang;
	}



	public void setParformValue(String parformValue) {
		this.parformValue = parformValue;
	}



	public void setRepositoryTypeValue(String repositoryTypeValue) {
		this.repositoryTypeValue = repositoryTypeValue;
	}



	public void setNonpreformLang(String nonpreformLang) {
		this.nonpreformLang = nonpreformLang;
	}



	public void setNonpreformValue(String nonpreformValue) {
		this.nonpreformValue = nonpreformValue;
	}



	public void setUseDatesId(String useDatesId) {
		this.useDatesId = useDatesId;
	}



	public void setUseDatesLang(String useDatesLang) {
		this.useDatesLang = useDatesLang;
	}



	public void setDateSetId(String dateSetId) {
		this.dateSetId = dateSetId;
	}



	public void setDateSetLang(String dateSetLang) {
		this.dateSetLang = dateSetLang;
	}



	public void setRepositoryNameLang(String repositoryNameLang) {
		this.repositoryNameLang = repositoryNameLang;
	}



	public void setRepositoryNameValue(String repositoryNameValue) {
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



	public void setLocationLocalType(String locationLocalType) {
		this.locationLocalType = locationLocalType;
	}



	public void setLocationLatitude(String locationLatitude) {
		this.locationLatitude = locationLatitude;
	}



	public void setLocationLongitude(String locationLongitude) {
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



	public void setDirectionsLang(String directionsLang) {
		this.directionsLang = directionsLang;
	}



	public void setDirectionsValue(String directionsValue) {
		this.directionsValue = directionsValue;
	}



	public void setAdminunitLang(String adminunitLang) {
		this.adminunitLang = adminunitLang;
	}



	public void setAdminunitValue(String adminunitValue) {
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



	public void setRestaccessLang(String restaccessLang) {
		this.restaccessLang = restaccessLang;
	}



	public void setRestaccessValue(String restaccessValue) {
		this.restaccessValue = restaccessValue;
	}



	public void setTermsOfUseHref(String termsOfUseHref) {
		this.termsOfUseHref = termsOfUseHref;
	}



	public void setTermsOfUseLang(String termsOfUseLang) {
		this.termsOfUseLang = termsOfUseLang;
	}



	public void setTermsOfUseValue(String termsOfUseValue) {
		this.termsOfUseValue = termsOfUseValue;
	}



	public void setAccessibilityQuestion(String accessibilityQuestion) {
		this.accessibilityQuestion = accessibilityQuestion;
	}



	public void setAccessibilityLang(String accessibilityLang) {
		this.accessibilityLang = accessibilityLang;
	}



	public void setAccessibilityValue(String accessibilityValue) {
		this.accessibilityValue = accessibilityValue;
	}



	public void setPhotographAllowanceValue(String photographAllowanceValue) {
		this.photographAllowanceValue = photographAllowanceValue;
	}



	public void setReadersTicketHref(String readersTicketHref) {
		this.readersTicketHref = readersTicketHref;
	}



	public void setReadersTicketLang(String readersTicketLang) {
		this.readersTicketLang = readersTicketLang;
	}



	public void setReadersTicketValue(String readersTicketValue) {
		this.readersTicketValue = readersTicketValue;
	}



	public void setAdvancedOrdersHref(String advancedOrdersHref) {
		this.advancedOrdersHref = advancedOrdersHref;
	}



	public void setAdvancedOrdersLang(String advancedOrdersLang) {
		this.advancedOrdersLang = advancedOrdersLang;
	}



	public void setAdvancedOrdersValue(String advancedOrdersValue) {
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



	public void setRuleLang(String ruleLang) {
		this.ruleLang = ruleLang;
	}



	public void setRuleValue(String ruleValue) {
		this.ruleValue = ruleValue;
	}



	public void setWebpageHref(String webpageHref) {
		this.webpageHref = webpageHref;
	}


	public void setWebpageValue(String webpageValue) {
		this.webpageValue = webpageValue;
	}


	public void setEmailHref(String emailHref) {
		this.emailHref = emailHref;
	}


	public void setEmailValue(String emailValue) {
		this.emailValue = emailValue;
	}


	public void setFaxValue(String faxValue) {
		this.faxValue = faxValue;
	}


	public void setTelephoneValue(String telephoneValue) {
		this.telephoneValue = telephoneValue;
	}

	public void setRelationEntryId(String relationEntryId) {
		this.relationEntryId = relationEntryId;
	}


	public void setRelationEntryLang(String relationEntryLang) {
		this.relationEntryLang = relationEntryLang;
	}



	public void setRelationEntryValue(String relationEntryValue) {
		this.relationEntryValue = relationEntryValue;
	}



	public void setEagRelationEagRelationType(String eagRelationEagRelationType) {
		this.eagRelationEagRelationType = eagRelationEagRelationType;
	}



	public void setEagRelationHref(String eagRelationHref) {
		this.eagRelationHref = eagRelationHref;
	}



	public void setPlaceEntryAccuracy(String placeEntryAccuracy) {
		this.placeEntryAccuracy = placeEntryAccuracy;
	}



	public void setPlaceEntryAltitude(String placeEntryAltitude) {
		this.placeEntryAltitude = placeEntryAltitude;
	}



	public void setPlaceEntryId(String placeEntryId) {
		this.placeEntryId = placeEntryId;
	}



	public void setPlaceEntryLang(String placeEntryLang) {
		this.placeEntryLang = placeEntryLang;
	}



	public void setPlaceEntryCountryCode(String placeEntryCountryCode) {
		this.placeEntryCountryCode = placeEntryCountryCode;
	}



	public void setPlaceEntryLatitude(String placeEntryLatitude) {
		this.placeEntryLatitude = placeEntryLatitude;
	}



	public void setPlaceEntryLongitude(String placeEntryLongitude) {
		this.placeEntryLongitude = placeEntryLongitude;
	}



	public void setPlaceEntryScriptCode(String placeEntryScriptCode) {
		this.placeEntryScriptCode = placeEntryScriptCode;
	}



	public void setResourceRelationResourceRelationType(
			String resourceRelationResourceRelationType) {
		this.resourceRelationResourceRelationType = resourceRelationResourceRelationType;
	}



	public void setResourceRelationLastDateTimeVerified(
			String resourceRelationLastDateTimeVerified) {
		this.resourceRelationLastDateTimeVerified = resourceRelationLastDateTimeVerified;
	}



	public void setResourceRelationId(String resourceRelationId) {
		this.resourceRelationId = resourceRelationId;
	}



	public void setResourceRelationLang(String resourceRelationLang) {
		this.resourceRelationLang = resourceRelationLang;
	}



	public void setResourceRelationHref(String resourceRelationHref) {
		this.resourceRelationHref = resourceRelationHref;
	}



	public void setRelationsId(String relationsId) {
		this.relationsId = relationsId;
	}



	public void setRelationsLang(String relationsLang) {
		this.relationsLang = relationsLang;
	}



	public String getPlaceEntryValue() {
		return placeEntryValue;
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