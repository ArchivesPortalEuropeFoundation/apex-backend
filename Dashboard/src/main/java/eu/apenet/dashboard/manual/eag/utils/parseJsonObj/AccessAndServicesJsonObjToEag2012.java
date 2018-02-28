package eu.apenet.dashboard.manual.eag.utils.parseJsonObj;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import eu.apenet.dashboard.manual.eag.Eag2012;

/**
 * class for Fill a {@link Eag2012} eag2012 object got from params, fill it with
 * the information provided into {@link JSONObject} JSONObject (got from params)
 * in part of access and services.
 */
public class AccessAndServicesJsonObjToEag2012 extends AbstractJsonObjtoEag2012 implements JsonObjToEag2012 {

    private JSONObject accessTable;
    private JSONObject accessAndServices;
    private Eag2012 eag2012;
    private int j;
    private int i;
    private String target1;
    private String target2;
    private String target3;
    private String target4;
    private int targetNumber;
    private final Logger log = Logger.getLogger(getClass());

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of opening times and opening
     * Langs
     */
    private void OpeningHours() throws JSONException {
        this.log.debug("Method start: \"OpeningHours\"");
        j = 0;
        while (accessTable.has("textOpeningTimes_" + (++j)) && (accessTable.has("selectLanguageOpeningTimes_" + (j)))) {
            if (accessTable.has("textOpeningTimes_" + j)) {
                List<Map<String, List<String>>> openingValues = eag2012.getOpeningValue();
                openingValues = createData(openingValues, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("textOpeningTimes_" + j), i, i, 0);
                eag2012.setOpeningValue(openingValues);
            }
            if (accessTable.has("selectLanguageOpeningTimes_" + j)) {
                List<Map<String, List<String>>> openingLangs = eag2012.getOpeningLang();
                openingLangs = createData(openingLangs, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("selectLanguageOpeningTimes_" + j), i, i, 0);
                eag2012.setOpeningLang(openingLangs);
            }
            if (accessTable.has("linkOpeningTimes_" + j)) {
                List<Map<String, List<String>>> openingHrefs = eag2012.getOpeningHref();
                openingHrefs = createData(openingHrefs, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("linkOpeningTimes_" + j), i, i, 0);
                eag2012.setOpeningHref(openingHrefs);
            }
        }//end while opening times
        //closing times and langs closing
        this.log.debug("End method: \"OpeningHours\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of closing dates
     */
    private void ClosingDates() throws JSONException {
        this.log.debug("Method start: \"ClosingDates\"");
        j = 0;
        while (accessTable.has("textClosingDates_" + (++j)) && (accessTable.has("selectLanguageClosingDates_" + (j)))) {
            if (accessTable.has("textClosingDates_" + j)) {
                List<Map<String, List<String>>> closingValues = eag2012.getClosingStandardDate();
                closingValues = createData(closingValues, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("textClosingDates_" + j), i, i, 0);
                eag2012.setClosingStandardDate(closingValues);
            }
            if (accessTable.has("selectLanguageClosingDates_" + j)) {
                List<Map<String, List<String>>> closingLangs = eag2012.getClosingLang();
                closingLangs = createData(closingLangs, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("selectLanguageClosingDates_" + j), i, i, 0);
                eag2012.setClosingLang(closingLangs);
            }
        }//end while closing times
        this.log.debug("End method: \"ClosingDates\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of description of directions
     */
    private void DescriptionOfDirections() throws JSONException {
        this.log.debug("Method start: \"DescriptionOfDirections\"");
        j = 0;
        while ((accessTable.has("textTravellingDirections_" + (++j))) && (accessTable.has("selectASATDSelectLanguage_" + (j))) && (accessTable.has("textTravelLink_" + (j)))) {
            if (accessTable.has("textTravellingDirections_" + j)) {
                List<List<String>> listDirectionsList = eag2012.getDirectionsValue();
                listDirectionsList = createDataList(listDirectionsList, accessTable.getString("textTravellingDirections_" + j), i);
                eag2012.setDirectionsValue(listDirectionsList);
            }
            if (accessTable.has("selectASATDSelectLanguage_" + j)) {
                List<List<String>> listDirectionsLangList = eag2012.getDirectionsLang();
                listDirectionsLangList = createDataList(listDirectionsLangList, accessTable.getString("selectASATDSelectLanguage_" + j), i);
                eag2012.setDirectionsLang(listDirectionsLangList);
            }
            if (accessTable.has("textTravelLink_" + j)) {
                List<Map<String, List<String>>> listCitationMap = eag2012.getCitationHref();
                listCitationMap = createData(listCitationMap, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("textTravelLink_" + j), i, i, 0);
                eag2012.setCitationHref(listCitationMap);
            }
        }//end while directions
        this.log.debug("End method: \"DescriptionOfDirections\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of access information and
     * access langs
     */
    private void AccessConditions() throws JSONException {
        this.log.debug("Method start: \"AccessConditions\"");
        //access information and access langs
        j = 0;
        while ((accessTable.has("textASAccessRestrictions_" + (++j))) && (accessTable.has("selectASARSelectLanguage_" + (j)))) {
            if (accessTable.has("textASAccessRestrictions_" + j)) {
                List<Map<String, List<String>>> listMapRestaccessList = eag2012.getRestaccessValue();
                listMapRestaccessList = createData(listMapRestaccessList, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("textASAccessRestrictions_" + j), i, i, 0);
                eag2012.setRestaccessValue(listMapRestaccessList);
            }
            if (accessTable.has("selectASARSelectLanguage_" + j)) {   //access information lang
                List<Map<String, List<String>>> listMapRestaccessList = eag2012.getRestaccessLang();
                listMapRestaccessList = createData(listMapRestaccessList, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("selectASARSelectLanguage_" + j), i, i, 0);
                eag2012.setRestaccessLang(listMapRestaccessList);
            }
        }// end while access information
        this.log.debug("End method: \"AccessConditions\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of term of use
     */
    private void TermOfUse() throws JSONException {
        this.log.debug("Method start: \"TermOfUse\"");
        j = 0;
        while ((accessTable.has("textASTermOfUse_" + (++j))) && (accessTable.has("selectASAFTOUSelectLanguage_" + (j))) && (accessTable.has("textASTOULink_" + (j)))) {
            if (accessTable.has("textASTermOfUse_" + j)) {
                List<List<String>> listTermsList = eag2012.getTermsOfUseValue();
                listTermsList = createDataList(listTermsList, accessTable.getString("textASTermOfUse_" + j), i);
                eag2012.setTermsOfUseValue(listTermsList);
            }
            if (accessTable.has("selectASAFTOUSelectLanguage_" + j)) {
                List<List<String>> listTermsLangList = eag2012.getTermsOfUseLang();
                listTermsLangList = createDataList(listTermsLangList, accessTable.getString("selectASAFTOUSelectLanguage_" + j), i);

                eag2012.setTermsOfUseLang(listTermsLangList);
            }
            if (accessTable.has("textASTOULink_" + j)) {
                List<List<String>> listTermsHrefList = eag2012.getTermsOfUseHref();
                listTermsHrefList = createDataList(listTermsHrefList, accessTable.getString("textASTOULink_" + j), i);
                eag2012.setTermsOfUseHref(listTermsHrefList);
            }

        }//end while terms of use
        this.log.debug("End method: \"TermOfUse\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of accessibility
     */
    private void accessibility() throws JSONException {
        this.log.debug("Method start: \"accessibility\"");
        //accessibility yes or no, it appears in tab yourinstitution, in tab accessAndServices and for each repository
        if (accessTable.has("selectASFacilitiesForDisabledPeopleAvailable")) {
            List<Map<String, String>> listAccessibilityMap = eag2012.getAccessibilityQuestion();
            listAccessibilityMap = createDataListString(listAccessibilityMap, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("selectASFacilitiesForDisabledPeopleAvailable"), i, i);
            eag2012.setAccessibilityQuestion(listAccessibilityMap);
        }
        j = 0;
        while ((accessTable.has("textASAccessibility_" + (++j))) && (accessTable.has("selectASASelectLanguage_" + (j)))) {
            if (accessTable.has("textASAccessibility_" + j)) {
                List<Map<String, List<String>>> listMapAccessibilityList = eag2012.getAccessibilityValue();
                listMapAccessibilityList = createData(listMapAccessibilityList, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("textASAccessibility_" + j), i, i, 0);
                eag2012.setAccessibilityValue(listMapAccessibilityList);
            }
            if (accessTable.has("selectASASelectLanguage_" + j)) {   //accessibility information lang
                List<Map<String, List<String>>> listMapAccessibilityList = eag2012.getAccessibilityLang();
                listMapAccessibilityList = createData(listMapAccessibilityList, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("selectASASelectLanguage_" + j), i, i, 0);
                eag2012.setAccessibilityLang(listMapAccessibilityList);
            }
        }// end while accessibility
        this.log.debug("End method: \"accessibility\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of SearchRoomSection
     */
    private void SearchRoomSection() throws JSONException {
        this.log.debug("Method start: \"SearchRoomSection\"");
        //search_room section		
        if (accessTable.has("textASSRTelephone_1")) {
            List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
            telephones = createListMapList(telephones, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, accessTable.getString("textASSRTelephone_1"), i, i, 0, 0);
            eag2012.setTelephoneValue(telephones);
        }
        if (accessTable.has("textASSREmailAddress")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailHref();
            listMapEmailValueList = createListMapList(listMapEmailValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, accessTable.getString("textASSREmailAddress"), i, i, 0, 0);
            eag2012.setEmailHref(listMapEmailValueList);
        }
        if (accessTable.has("textASSREmailLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
            listMapEmailList = createListMapList(listMapEmailList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, accessTable.getString("textASSREmailLinkTitle"), i, i, 0, 0);
            eag2012.setEmailValue(listMapEmailList);
        }
        // Add empty language to the map email.
        if (accessTable.has("textASSREmailAddress") || accessTable.has("textASSREmailLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailLang();
            listMapEmailList = createListMapListAcces(listMapEmailList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, i, i, 0, 0);
            eag2012.setEmailLang(listMapEmailList);
        }
        // End empty language to the map email
        if (accessTable.has("textASSRWebpage")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageHref();
            listMapWebpagelList = createListMapList(listMapWebpagelList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, accessTable.getString("textASSRWebpage"), i, i, 0, 0);
            eag2012.setWebpageHref(listMapWebpagelList);
        }
        if (accessTable.has("textASSRWebpageLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageValue();
            listMapWebpageValueList = createListMapList(listMapWebpageValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, accessTable.getString("textASSRWebpageLinkTitle"), i, i, 0, 0);
            eag2012.setWebpageValue(listMapWebpageValueList);
        }
        //Add empty language to the webpage map
        if (accessTable.has("textASSRWebpage") || accessTable.has("textASSRWebpageLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageLang();
            listMapWebpageValueList = createListMapListAcces(listMapWebpageValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, i, i, 0, 0);
            eag2012.setWebpageLang(listMapWebpageValueList);
        }
        //End empty language to the webpage map
        if (accessTable.has("textASSRWorkPlaces")) {
            List<Map<String, Map<String, Map<String, List<String>>>>> numValue = eag2012.getNumValue();
            numValue = createDataListMapMapListS(numValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, Eag2012.WORKING_PLACES, accessTable.getString("textASSRWorkPlaces"), i, i, 0, 0, 0);
            eag2012.setNumValue(numValue);
        }
        if (accessTable.has("textASSRComputerPlaces")) {
            List<Map<String, Map<String, Map<String, List<String>>>>> numValue = eag2012.getNumValue();
            numValue = createDataListMapMapListS(numValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, Eag2012.COMPUTER_PLACES, accessTable.getString("textASSRComputerPlaces"), i, i, 0, 0, 0);
            eag2012.setNumValue(numValue);
        }
        target1 = "textDescriptionOfYourComputerPlaces";
        target2 = "selectDescriptionOfYourComputerPlaces";
        targetNumber = 1;
        do {
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = ((target2.indexOf("_") != -1) ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, accessTable.getString(target1), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
            }
            if (accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, accessTable.getString(target2), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
            }
        } while (accessTable.has(target1) && accessTable.has(target2));
        target1 = null;
        target2 = null;
        this.log.debug("End method: \"SearchRoomSection\"");
        //end search room
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of readersticket
     */
    private void readersticket() throws JSONException {
        this.log.debug("Method start: \"readersticket\"");
        if (accessTable.has("textASSRMicrofilmPlaces")) {
            List<Map<String, Map<String, Map<String, List<String>>>>> nums = eag2012.getNumValue();
            nums = createDataListMapMapListS(nums, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.SEARCHROOM, Eag2012.MICROFILM, accessTable.getString("textASSRMicrofilmPlaces"), i, i, 0, 0, 0);
            eag2012.setNumValue(nums);
        }
        if (accessTable.has("selectASSRPhotographAllowance")) {
            List<String> photographAllowance = eag2012.getPhotographAllowanceValue();
            photographAllowance = createDataListAcces(photographAllowance, accessTable.getString("selectASSRPhotographAllowance"), i);
            eag2012.setPhotographAllowanceValue(photographAllowance);
        }
        target1 = "textASSRReadersTicket";
        target2 = "selectReadersTickectLanguage";
        target3 = "textASSRRTLink";
        targetNumber = 1;
        do {
            target1 = (target1.indexOf("_") != -1 ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = (target2.indexOf("_") != -1 ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            target3 = (target3.indexOf("_") != -1 ? target3.substring(0, target3.indexOf("_")) : target3) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<List<String>> readersTicket = eag2012.getReadersTicketValue();
                readersTicket = createDataList(readersTicket, accessTable.getString(target1), i);
                eag2012.setReadersTicketValue(readersTicket);
            }
            if (accessTable.has(target2)) {
                List<List<String>> readersTicketLang = eag2012.getReadersTicketLang();
                readersTicketLang = createDataList(readersTicketLang, accessTable.getString(target2), i);
                eag2012.setReadersTicketLang(readersTicketLang);
            }
            if (accessTable.has(target3)) {
                List<List<String>> readersTicketHref = eag2012.getReadersTicketHref();
                readersTicketHref = createDataList(readersTicketHref, accessTable.getString(target3), i);
                eag2012.setReadersTicketHref(readersTicketHref);
            }
        } while (accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3));
        this.log.debug("End method: \"readersticket\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of documents in advance
     */
    private void documentsInAdvance() throws JSONException {
        this.log.debug("Method start: \"documentsInAdvance\"");
        // documents in advance		
        target1 = "textASSRAdvancedOrders";
        target2 = "textASSRAOLink";
        target3 = "selectASSRAFOIUSelectLanguage";
        targetNumber = 1;
        do {
            target1 = (target1.indexOf("_") != -1 ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = (target2.indexOf("_") != -1 ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            target3 = (target3.indexOf("_") != -1 ? target3.substring(0, target3.indexOf("_")) : target3) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<List<String>> advancedOrders = eag2012.getAdvancedOrdersValue();
                advancedOrders = createDataList(advancedOrders, accessTable.getString(target1), i);
                eag2012.setAdvancedOrdersValue(advancedOrders);
            }
            if (accessTable.has(target2)) {
                List<List<String>> advancedOrdersHref = eag2012.getAdvancedOrdersHref();
                advancedOrdersHref = createDataList(advancedOrdersHref, accessTable.getString(target2), i);
                eag2012.setAdvancedOrdersHref(advancedOrdersHref);
            }
            if (accessTable.has(target3)) {
                List<List<String>> advancedOrdersLang = eag2012.getAdvancedOrdersLang();
                advancedOrdersLang = createDataList(advancedOrdersLang, accessTable.getString(target3), i);
                eag2012.setAdvancedOrdersLang(advancedOrdersLang);
            }
        } while (accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3));
        this.log.debug("End method: \"documentsInAdvance\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of researchServices
     */
    private void researchServices() throws JSONException {
        this.log.debug("Method start: \"researchServices\"");
        //ResearchServices		
        target1 = "textASSRResearchServices";
        target2 = "textASSRRSSelectLanguage";
        targetNumber = 1;
        do {
            target1 = (target1.indexOf("_") != -1 ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = (target2.indexOf("_") != -1 ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotesPValues = eag2012.getDescriptiveNotePValue();
                descriptiveNotesPValues = createListMapList(descriptiveNotesPValues, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESEARCH_SERVICES, accessTable.getString(target1), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotesPValues);
            }
            if (accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotesPLang = eag2012.getDescriptiveNotePLang();
                descriptiveNotesPLang = createListMapList(descriptiveNotesPLang, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESEARCH_SERVICES, accessTable.getString(target2), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotesPLang);
            }
        } while (accessTable.has(target1) && accessTable.has(target2));
        this.log.debug("End method: \"researchServices\"");
        //end ResearchServices
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of library
     */
    private void library() throws JSONException {
        this.log.debug("Method start: \"library\"");
        //library section		
        if (accessTable.has("selectASLibrary")) {
            List<String> libraryList = eag2012.getLibraryQuestion();
            libraryList = createDataListAcces(libraryList, accessTable.getString("selectASLibrary"), i);
            eag2012.setLibraryQuestion(libraryList);
        }
        if (accessTable.has("textASLTelephone_1")) {
            List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
            telephones = createListMapList(telephones, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, accessTable.getString("textASLTelephone_1"), i, i, 0, 0);
            eag2012.setTelephoneValue(telephones);
        }
        if (accessTable.has("textASLEmailAddress")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailHref();
            listMapEmailValueList = createListMapList(listMapEmailValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, accessTable.getString("textASLEmailAddress"), i, i, 0, 0);
            eag2012.setEmailHref(listMapEmailValueList);
        }
        if (accessTable.has("textASLEmailLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
            listMapEmailList = createListMapList(listMapEmailList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, accessTable.getString("textASLEmailLinkTitle"), i, i, 0, 0);
            eag2012.setEmailValue(listMapEmailList);
        }
        //Add empty value to lang email
        if (accessTable.has("textASLEmailAddress") || accessTable.has("textASLEmailLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailLang();
            listMapEmailList = createListMapListAcces(listMapEmailList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, i, i, 0, 0);
            eag2012.setEmailLang(listMapEmailList);
        }
        //End empty value to lang email
        if (accessTable.has("textASLWebpage")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageHref();
            listMapWebpagelList = createListMapList(listMapWebpagelList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, accessTable.getString("textASLWebpage"), i, i, 0, 0);
            eag2012.setWebpageHref(listMapWebpagelList);
        }
        if (accessTable.has("textASLWebpageLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageValue();
            listMapWebpageValueList = createListMapList(listMapWebpageValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, accessTable.getString("textASLWebpageLinkTitle"), i, i, 0, 0);
            eag2012.setWebpageValue(listMapWebpageValueList);
        }
        //Add empty value to lang webpage
        if (accessTable.has("textASLWebpage") || accessTable.has("textASLWebpageLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageLang();
            listMapWebpageValueList = createListMapListAcces(listMapWebpageValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, i, i, 0, 0);
            eag2012.setWebpageLang(listMapWebpageValueList);
        }
        //End empty value to lang webpage
        if (accessTable.has("textASLMonographocPublication")) {
            List<Map<String, Map<String, Map<String, List<String>>>>> numValue = eag2012.getNumValue();
            numValue = createDataListMapMapListS(numValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, Eag2012.MONOGRAPHIC_PUBLICATION, accessTable.getString("textASLMonographocPublication"), i, i, 0, 0, 0);
            eag2012.setNumValue(numValue);
        }
        if (accessTable.has("textASLSerialPublication")) {
            List<Map<String, Map<String, Map<String, List<String>>>>> numValue = eag2012.getNumValue();
            numValue = createDataListMapMapListS(numValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.LIBRARY, Eag2012.SERIAL_PUBLICATION, accessTable.getString("textASLSerialPublication"), i, i, 0, 0, 0);
            eag2012.setNumValue(numValue);
        }
        if (accessTable.has("selectASInternetAccess")) {
            List<String> internetList = eag2012.getInternetAccessQuestion();
            internetList = createDataListAcces(internetList, accessTable.getString("selectASInternetAccess"), i);
            eag2012.setInternetAccessQuestion(internetList);
        }
        target1 = "textASDescription";
        target2 = "selectASDSelectLanguage";
        targetNumber = 1;
        do {
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = ((target2.indexOf("_") != -1) ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.INTERNET_ACCESS, accessTable.getString(target1), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
            }
            if (accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePLang = eag2012.getDescriptiveNotePLang();
                descriptiveNotePLang = createListMapList(descriptiveNotePLang, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.INTERNET_ACCESS, accessTable.getString(target2), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotePLang);
            }
        } while (accessTable.has(target1) && accessTable.has(target2));
        this.log.debug("End method: \"library\"");
        //end library
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of technicalServices
     */
    private void technicalServices() throws JSONException {
        this.log.debug("Method start: \"technicalServices\"");
        if (accessTable.has("selectASTSRestaurationLab")) {
            List<String> restorationList = eag2012.getRestorationlabQuestion();
            restorationList = createDataListAcces(restorationList, accessTable.getString("selectASTSRestaurationLab"), i);
            eag2012.setRestorationlabQuestion(restorationList);
        }
        target1 = "textASTSDescriptionOfRestaurationLab";
        target2 = "selectASTSSelectLanguage";
        targetNumber = 1;
        do {
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = ((target2.indexOf("_") != -1) ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, accessTable.getString(target1), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
            }
            if (accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePLang = eag2012.getDescriptiveNotePLang();
                descriptiveNotePLang = createListMapList(descriptiveNotePLang, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, accessTable.getString(target2), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotePLang);
            }
        } while (accessTable.has(target1) && accessTable.has(target2));
        this.log.debug("End method: \"technicalServices\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of reproductionService
     */
    private void reproductionService() throws JSONException {
        this.log.debug("Method start: \"reproductionService\"");
        //form reproduction service				
        if (accessTable.has("textASTSTelephone_1")) {
            List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
            telephones = createListMapList(telephones, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, accessTable.getString("textASTSTelephone_1"), i, i, 0, 0);
            eag2012.setTelephoneValue(telephones);
        }
        if (accessTable.has("textASRSEmail")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailHref();
            listMapEmailValueList = createListMapList(listMapEmailValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, accessTable.getString("textASRSEmail"), i, i, 0, 0);
            eag2012.setEmailHref(listMapEmailValueList);
        }
        if (accessTable.has("textASRSEmailLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
            listMapEmailList = createListMapList(listMapEmailList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, accessTable.getString("textASRSEmailLinkTitle"), i, i, 0, 0);
            eag2012.setEmailValue(listMapEmailList);
        }
        //ADD empty value to lang email
        if (accessTable.has("textASRSEmail") || accessTable.has("textASRSEmailLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailLang();
            listMapEmailList = createListMapListAcces(listMapEmailList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, i, i, 0, 0);
            eag2012.setEmailLang(listMapEmailList);
        }
        //End empty value to lang email
        if (accessTable.has("textASRSWebpage")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageHref();
            listMapWebpagelList = createListMapList(listMapWebpagelList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, accessTable.getString("textASRSWebpage"), i, i, 0, 0);
            eag2012.setWebpageHref(listMapWebpagelList);
        }
        if (accessTable.has("textASRSWebpageLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageValue();
            listMapWebpageValueList = createListMapList(listMapWebpageValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, accessTable.getString("textASRSWebpageLinkTitle"), i, i, 0, 0);
            eag2012.setWebpageValue(listMapWebpageValueList);
        }
        //Add empty value to lang webpage
        if (accessTable.has("textASRSWebpage") || accessTable.has("textASRSWebpageLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageLang();
            listMapWebpageValueList = createListMapListAcces(listMapWebpageValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.RESTORATION_LAB, i, i, 0, 0);
            eag2012.setWebpageLang(listMapWebpageValueList);
        }
        //End empty value to lang webpage
        //Reproductions services section
        if (accessTable.has("selectASTSReproductionService")) {
            List<String> reproductionserList = eag2012.getReproductionserQuestion();
            reproductionserList = createDataListAcces(reproductionserList, accessTable.getString("selectASTSReproductionService"), i);
            eag2012.setReproductionserQuestion(reproductionserList);
        }
        target1 = "textASTSDescriptionOfReproductionService";
        target2 = "selectASTSRSSelectLanguage";
        targetNumber = 1;
        do {
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = ((target2.indexOf("_") != -1) ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, accessTable.getString(target1), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
            }
            if (accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePLang = eag2012.getDescriptiveNotePLang();
                descriptiveNotePLang = createListMapList(descriptiveNotePLang, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, accessTable.getString(target2), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotePLang);
            }
        } while (accessTable.has(target1) && accessTable.has(target2));
        this.log.debug("End method: \"reproductionService\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of refreshmentArea
     */
    private void refreshmentArea() throws JSONException {
        this.log.debug("Method start: \"refreshmentArea\"");
        //form
        if (accessTable.has("textASTSRSTelephone_1")) {
            List<Map<String, Map<String, List<String>>>> telephones = eag2012.getTelephoneValue();
            telephones = createListMapList(telephones, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, accessTable.getString("textASTSRSTelephone_1"), i, i, 0, 0);
            eag2012.setTelephoneValue(telephones);
        }
        if (accessTable.has("textASTSRSEmailAddress")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailValueList = eag2012.getEmailHref();
            listMapEmailValueList = createListMapList(listMapEmailValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, accessTable.getString("textASTSRSEmailAddress"), i, i, 0, 0);
            eag2012.setEmailHref(listMapEmailValueList);
        }
        if (accessTable.has("textASTSEmailAddressLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailValue();
            listMapEmailList = createListMapList(listMapEmailList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, accessTable.getString("textASTSEmailAddressLinkTitle"), i, i, 0, 0);
            eag2012.setEmailValue(listMapEmailList);
        }
        //Add empty value to lang email
        if (accessTable.has("textASTSRSEmailAddress") || accessTable.has("textASTSEmailAddressLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapEmailList = eag2012.getEmailLang();
            listMapEmailList = createListMapListAcces(listMapEmailList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, i, i, 0, 0);
            eag2012.setEmailLang(listMapEmailList);
        }
        //End empty value to lang email
        if (accessTable.has("textASTSRSWebpage")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpagelList = eag2012.getWebpageHref();
            listMapWebpagelList = createListMapList(listMapWebpagelList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, accessTable.getString("textASTSRSWebpage"), i, i, 0, 0);
            eag2012.setWebpageHref(listMapWebpagelList);
        }
        if (accessTable.has("textASTSRSWebpageLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageValue();
            listMapWebpageValueList = createListMapList(listMapWebpageValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, accessTable.getString("textASTSRSWebpageLinkTitle"), i, i, 0, 0);
            eag2012.setWebpageValue(listMapWebpageValueList);
        }
        //Add empty value to lang webpage
        if (accessTable.has("textASTSRSWebpage") || accessTable.has("textASTSRSWebpageLinkTitle")) {
            List<Map<String, Map<String, List<String>>>> listMapWebpageValueList = eag2012.getWebpageLang();
            listMapWebpageValueList = createListMapListAcces(listMapWebpageValueList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REPRODUCTIONSER, i, i, 0, 0);
            eag2012.setWebpageLang(listMapWebpageValueList);
        }
        //End empty value to lang webpage
        //microform
        if (accessTable.has("selectASTSRSMicroform")) {
            List<String> microformList = eag2012.getMicroformserQuestion();
            microformList = createDataListAcces(microformList, accessTable.getString("selectASTSRSMicroform"), i);
            eag2012.setMicroformserQuestion(microformList);
        }
        //photograph
        if (accessTable.has("selectASTSRSPhotographServices")) {
            List<String> photographList = eag2012.getPhotographserQuestion();
            photographList = createDataListAcces(photographList, accessTable.getString("selectASTSRSPhotographServices"), i);
            eag2012.setPhotographserQuestion(photographList);
        }
        //digitalser
        if (accessTable.has("selectASTSRSDigitalServices")) {
            List<String> digitalserList = eag2012.getDigitalserQuestion();
            digitalserList = createDataListAcces(digitalserList, accessTable.getString("selectASTSRSDigitalServices"), i);
            eag2012.setDigitalserQuestion(digitalserList);
        }
        //photocopyser
        if (accessTable.has("selectASTSRSPhotocopyServices")) {
            List<String> photocopyserList = eag2012.getPhotocopyserQuestion();
            photocopyserList = createDataListAcces(photocopyserList, accessTable.getString("selectASTSRSPhotocopyServices"), i);
            eag2012.setPhotocopyserQuestion(photocopyserList);
        }
        //Recreational services
        // Refreshment area and refreshment area lang
        j = 0;
        while ((accessTable.has("textASReSeRefreshment_" + (++j))) && (accessTable.has("selectASReSeRefreshmentSelectLanguage_" + (j)))) {
            if (accessTable.has("textASReSeRefreshment_" + j)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REFRESHMENT, accessTable.getString("textASReSeRefreshment_" + j), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
            }
            if (accessTable.has("selectASReSeRefreshmentSelectLanguage_" + j)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.REFRESHMENT, accessTable.getString("selectASReSeRefreshmentSelectLanguage_" + j), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
            }
        }
        this.log.debug("End method: \"refreshmentArea\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of exibition
     */
    private void exibition() throws JSONException {
        this.log.debug("Method start: \"exibition\"");
        target1 = "textASReSeExhibition";
        target2 = "textASReSeWebpage";
        target3 = "textASReSeWebpageLinkTitle";
        target4 = "selectASReSeExhibitionSelectLanguage";
        targetNumber = 1;
        do {
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = ((target2.indexOf("_") != -1) ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            target3 = ((target3.indexOf("_") != -1) ? target3.substring(0, target3.indexOf("_")) : target3) + "_" + targetNumber;
            target4 = ((target4.indexOf("_") != -1) ? target4.substring(0, target4.indexOf("_")) : target4) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.EXHIBITION, accessTable.getString(target1), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
            }
            if (accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageHref();
                listMapWebpageHrefList = createListMapList(listMapWebpageHrefList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.EXHIBITION, accessTable.getString(target2), i, i, 0, 0);
                eag2012.setWebpageHref(listMapWebpageHrefList);
            }
            if (accessTable.has(target3)) {
                List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageValue();
                listMapWebList = createListMapList(listMapWebList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.EXHIBITION, accessTable.getString(target3), i, i, 0, 0);
                eag2012.setWebpageValue(listMapWebList);
            }
            //Add empty value to lang webpage
            if (accessTable.has(target2) || accessTable.has(target3)) {
                List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageLang();
                listMapWebList = createListMapListAcces(listMapWebList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.EXHIBITION, i, i, 0, 0);
                eag2012.setWebpageLang(listMapWebList);
            }
            //End empty value to lang webpage
            if (accessTable.has(target4)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.EXHIBITION, accessTable.getString(target4), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
            }
        } while (accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3) && accessTable.has(target4));
        //end exibition
        this.log.debug("End method: \"exibition\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of toursAndSessions
     */
    private void toursAndSessions() throws JSONException {
        this.log.debug("Method start: \"toursAndSessions\"");
        target1 = "textASReSeToursAndSessions";
        target2 = "textASReSeTSWebpage";
        target3 = "textASReSeWebpageTSLinkTitle";
        target4 = "selectASReSeToursAndSessionsSelectLanguage";
        targetNumber = 1;
        do {
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = ((target2.indexOf("_") != -1) ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            target3 = ((target3.indexOf("_") != -1) ? target3.substring(0, target3.indexOf("_")) : target3) + "_" + targetNumber;
            target4 = ((target4.indexOf("_") != -1) ? target4.substring(0, target4.indexOf("_")) : target4) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.TOURS_SESSIONS, accessTable.getString(target1), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
            }
            if (accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageHref();
                listMapWebpageHrefList = createListMapList(listMapWebpageHrefList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.TOURS_SESSIONS, accessTable.getString(target2), i, i, 0, 0);
                eag2012.setWebpageHref(listMapWebpageHrefList);
            }
            if (accessTable.has(target3)) {
                List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageValue();
                listMapWebList = createListMapList(listMapWebList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.TOURS_SESSIONS, accessTable.getString(target3), i, i, 0, 0);
                eag2012.setWebpageValue(listMapWebList);
            }
            //Add empty value to lang webpage
            if (accessTable.has(target2) || accessTable.has(target3)) {
                List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageLang();
                listMapWebList = createListMapListAcces(listMapWebList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.TOURS_SESSIONS, i, i, 0, 0);
                eag2012.setWebpageLang(listMapWebList);
            }
            //End empty value to lang webpage
            if (accessTable.has(target4)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.TOURS_SESSIONS, accessTable.getString(target4), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
            }
        } while (accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3) && accessTable.has(target4));
        this.log.debug("End method: \"toursAndSessions\"");
    }

    /**
     * Method that checks if it have to create if the object and fill it with
     * data the {@link Eag2012} Eag2012 object part of otherServices
     */
    private void otherServices() throws JSONException {
        this.log.debug("Method start: \"otherServices\"");
        target1 = "textASReSeOtherServices";
        target2 = "textASReSeOSWebpage";
        target3 = "textASReSeWebpageOSLinkTitle";
        target4 = "selectASReSeOtherServicesSelectLanguage";
        targetNumber = 1;
        do {
            target1 = ((target1.indexOf("_") != -1) ? target1.substring(0, target1.indexOf("_")) : target1) + "_" + targetNumber;
            target2 = ((target2.indexOf("_") != -1) ? target2.substring(0, target2.indexOf("_")) : target2) + "_" + targetNumber;
            target3 = ((target3.indexOf("_") != -1) ? target3.substring(0, target3.indexOf("_")) : target3) + "_" + targetNumber;
            target4 = ((target4.indexOf("_") != -1) ? target4.substring(0, target4.indexOf("_")) : target4) + "_" + targetNumber;
            targetNumber++;
            if (accessTable.has(target1)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePValue();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.OTHER_SERVICES, accessTable.getString(target1), i, i, 0, 0);
                eag2012.setDescriptiveNotePValue(descriptiveNotePValue);
            }
            if (accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> listMapWebpageHrefList = eag2012.getWebpageHref();
                listMapWebpageHrefList = createListMapList(listMapWebpageHrefList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.OTHER_SERVICES, accessTable.getString(target2), i, i, 0, 0);
                eag2012.setWebpageHref(listMapWebpageHrefList);
            }
            if (accessTable.has(target3)) {
                List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageValue();
                listMapWebList = createListMapList(listMapWebList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.OTHER_SERVICES, accessTable.getString(target3), i, i, 0, 0);
                eag2012.setWebpageValue(listMapWebList);
            }
            //Add empty lang webpage
            if (accessTable.has(target2) || accessTable.has(target2)) {
                List<Map<String, Map<String, List<String>>>> listMapWebList = eag2012.getWebpageLang();
                listMapWebList = createListMapListAcces(listMapWebList, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.OTHER_SERVICES, i, i, 0, 0);
                eag2012.setWebpageLang(listMapWebList);
            }
            //End empty lang webpage
            if (accessTable.has(target4)) {
                List<Map<String, Map<String, List<String>>>> descriptiveNotePValue = eag2012.getDescriptiveNotePLang();
                descriptiveNotePValue = createListMapList(descriptiveNotePValue, Eag2012.TAB_ACCESS_AND_SERVICES, Eag2012.OTHER_SERVICES, accessTable.getString(target4), i, i, 0, 0);
                eag2012.setDescriptiveNotePLang(descriptiveNotePValue);
            }
        } while (accessTable.has(target1) && accessTable.has(target2) && accessTable.has(target3) && accessTable.has(target4));
        ++i;
        this.log.debug("End method: \"otherServices\"");
    }

    /**
     * Method that create de jsonobj de {@link Eag2012} Eag2012 for part
     * accessAndServices
     *
     * @param eag2012 {@link Eag2012} Eag2012
     * @param jsonObj {@link JSONObjec}> JSONObject
     * @return {@link Eag2012} Eag2012
     */
    @Override
    public Eag2012 JsonObjToEag2012(Eag2012 eag20122, JSONObject jsonObj)
            throws JSONException {
        //JSONObject accessAndServices = jsonObj.getJSONObject("accessAndServices");
        accessAndServices = jsonObj.getJSONObject("accessAndServices");
        eag2012 = eag20122;
        if (accessAndServices != null) {
            int i = 0;
            while (accessAndServices.has("accessAndServicesTable_" + (i + 1))) {
                //JSONObject 
                accessTable = accessAndServices.getJSONObject("accessAndServicesTable_" + (i + 1));
                //opening times and opening langs				
                OpeningHours();
                ClosingDates();
                //directions
                DescriptionOfDirections();
                //access yes or no, it appears in tab yourinstitution, in tab accessAndServices and for each repository
                if (accessTable.has("selectASAccesibleToThePublic")) {
                    List<Map<String, String>> accessQuestions = eag2012.getAccessQuestion();
                    accessQuestions = createDataListString(accessQuestions, Eag2012.TAB_ACCESS_AND_SERVICES, accessTable.getString("selectASAccesibleToThePublic"), i, i);
                    eag2012.setAccessQuestion(accessQuestions);
                }
                //access information and access langs
                AccessConditions();
                //terms of use
                TermOfUse();
                //accessibility yes or no, it appears in tab yourinstitution, in tab accessAndServices and for each repository
                accessibility();
                //search_room section
                SearchRoomSection();
                //readersticket				
                readersticket();
                //end readers ticket*/
                // documents in advance				
                documentsInAdvance();
                //ResearchServices				
                researchServices();
                //library section
                library();
                //technical services section Restoration Lab
                technicalServices();
                //reproduction service
                reproductionService();
                //Refreshment area
                refreshmentArea();
                //exibition
                exibition();
                //ToursAndSessions
                toursAndSessions();
                //OtherServices
                otherServices();
                ++i;
            }
        }
        return eag2012;
    }
}
