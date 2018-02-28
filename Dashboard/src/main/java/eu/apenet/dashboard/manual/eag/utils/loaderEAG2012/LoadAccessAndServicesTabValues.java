package eu.apenet.dashboard.manual.eag.utils.loaderEAG2012;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.dashboard.manual.eag.Eag2012;
import eu.apenet.dashboard.manual.eag.utils.EAG2012Loader;
import eu.apenet.dpt.utils.eag2012.Citation;
import eu.apenet.dpt.utils.eag2012.Eag;
import eu.apenet.dpt.utils.eag2012.Library;
import eu.apenet.dpt.utils.eag2012.Repository;
import eu.apenet.dpt.utils.eag2012.Searchroom;
import eu.apenet.dpt.utils.eag2012.Timetable;

/**
 * Class for load access and services tab values from the XML
 */
public class LoadAccessAndServicesTabValues implements LoaderEAG2012 {

    private Repository repository;
    private Searchroom searchRoom;
    private Library library;
    // Searchroom.
    private List<String> srTelephone;
    private List<String> srNumberOfEmail;
    private List<String> srEmailHref;
    private List<String> srEmailTitle;
    private List<String> srNumberOfWebpage;
    private List<String> srWebpageHref;
    private List<String> srWebpageTitle;
    private List<String> srWorkingPlaces;
    private List<String> srComputerPlacesNum;
    private List<String> srComputerPlacesValue;
    private List<String> srComputerPlacesLang;
    private List<String> srMicrofilmReadersNum;
    private List<String> srPhotographAllowance;
    private List<String> srNumberOfReadersTicket;
    private List<String> srReadersTicket;
    private List<String> srReadersTicketHref;
    private List<String> srReadersTicketLang;
    private List<String> srNumberOfAdvancedOrders;
    private List<String> srAdvancedOrders;
    private List<String> srAdvancedOrdersHref;
    private List<String> srAdvancedOrdersLang;
    private List<String> srResearchServices;
    private List<String> srResearchServicesLang;
    // Library
    private List<String> libQuestion;
    private List<String> libTelephone;
    private List<String> libNumberOfEmail;
    private List<String> libEmailHref;
    private List<String> libEmailTitle;
    private List<String> libNumberOfWebpage;
    private List<String> libWebpageHref;
    private List<String> libWebpageTitle;
    private List<String> libMonographicPubNum;
    private List<String> libSerialPubNum;
    // Restoration laboratory.
    private List<String> tsRLQuestion;
    private List<String> tsRLValue;
    private List<String> tsRLValueLang;
    private List<String> tsRLTelephone;
    private List<String> tsRLNumberOfEmail;
    private List<String> tsRLEmailHref;
    private List<String> tsRLEmailTitle;
    private List<String> tsRLNumberOfWebpage;
    private List<String> tsRLWebpageHref;
    private List<String> tsRLWebpageTitle;
    // Reproduction services.
    private List<String> tsRSQuestion;
    private List<String> tsRSValue;
    private List<String> tsRSValueLang;
    private List<String> tsRSTelephone;
    private List<String> tsRSNumberOfEmail;
    private List<String> tsRSEmailHref;
    private List<String> tsRSEmailTitle;
    private List<String> tsRSNumberOfWebpage;
    private List<String> tsRSWebpageHref;
    private List<String> tsRSWebpageTitle;
    private List<String> tsRSMicroformser;
    private List<String> tsRSPhotographser;
    private List<String> tsRSDigitalser;
    private List<String> tsRSPhotocopyser;
    // Recreational services
    private List<String> rsRefreshment;
    private List<String> rsRefreshmentLang;
    private List<String> rsNumberOfExhibitions;
    private List<String> rsExhibition;
    private List<String> rsExhibitionLang;
    private List<String> rsExhibitionWebpage;
    private List<String> rsExhibitionWebpageTitle;
    private List<String> rsNumberOfToursSessions;
    private List<String> rsToursSessions;
    private List<String> rsToursSessionsLang;
    private List<String> rsToursSessionsWebpage;
    private List<String> rsToursSessionsWebpageTitle;
    private List<String> rsNumberOfOtherServices;
    private List<String> rsOtherServices;
    private List<String> rsOtherServicesLang;
    private List<String> rsOtherServicesWebpage;
    private List<String> rsOtherServicesWebpageTitle;
    private final Logger log = Logger.getLogger(getClass());

    /**
     * @param asNumberOfOpening {@link List<String>} the asOpeningContent to add
     */
    public void addAsNumberOfOpening(List<String> asNumberOfOpening) {
        this.loader.getAsNumberOfOpening().add(asNumberOfOpening);
    }

    /**asOpeningContent@param Content {@link List<String>} the asOpeningContent to add
     */
    public void addAsOpeningContent(List<String> asOpeningContent) {
        this.loader.getAsOpeningContent().add(asOpeningContent);
    }

    /**
     * @param asOpeningLang {@link List<String>} the asOpeningLang to add
     */
    public void addAsOpeningLang(List<String> asOpeningLang) {
        this.loader.getAsOpeningLang().add(asOpeningLang);
    }
    
    /**
     * @param asOpeningHref {@link List<String>} the asOpeningHref to add
     */
    public void addAsOpeningHref(List<String> asOpeningHref) {
        this.loader.getAsOpeningHref().add(asOpeningHref);
    }

    /**
     * @param asClosing {@link List<String>} the asClosing to add
     */
    public void addAsClosing(List<String> asClosing) {
        this.loader.getAsClosing().add(asClosing);
    }

    /**
     * @param asClosingLang {@link List<String>} the asClosingLang to add
     */
    public void addAsClosingLang(List<String> asClosingLang) {
        this.loader.getAsClosingLang().add(asClosingLang);
    }

    /**
     * @param asNumberOfDirections {@link List<String>} the asNumberOfDirections
     * to add
     */
    public void addAsNumberOfDirections(List<String> asNumberOfDirections) {
        this.loader.getAsNumberOfDirections().add(asNumberOfDirections);
    }

    /**
     * @param asDirections {@link List<String>} the asDirections to add
     */
    public void addAsDirections(List<String> asDirections) {
        this.loader.getAsDirections().add(asDirections);
    }

    /**
     * @param asDirectionsLang {@link List<String>} the asDirectionsLang to add
     */
    public void addAsDirectionsLang(List<String> asDirectionsLang) {
        this.loader.getAsDirectionsLang().add(asDirectionsLang);
    }

    /**
     * @param asDirectionsCitationHref {@link List<String>} the
     * asDirectionsCitationHref to add
     */
    public void addAsDirectionsCitationHref(List<String> asDirectionsCitationHref) {
        this.loader.getAsDirectionsCitationHref().add(asDirectionsCitationHref);
    }

    /**
     * @param asAccessQuestion {@link List<String>} the asAccessQuestion to add
     */
    public void addAsAccessQuestion(List<String> asAccessQuestion) {
        this.loader.getAsAccessQuestion().add(asAccessQuestion);
    }

    /**
     * @param asRestaccess {@link List<String>} the asRestaccess to add
     */
    public void addAsRestaccess(List<String> asRestaccess) {
        this.loader.getAsRestaccess().add(asRestaccess);
    }

    /**
     * @param asRestaccessLang {@link List<String>} the asRestaccessLang to add
     */
    public void addAsRestaccessLang(List<String> asRestaccessLang) {
        this.loader.getAsRestaccessLang().add(asRestaccessLang);
    }

    /**
     * @param asNumberOfTermsOfUse {@link List<String>} the asNumberOfTermsOfUse
     * to add
     */
    public void addAsNumberOfTermsOfUse(List<String> asNumberOfTermsOfUse) {
        this.loader.getAsNumberOfTermsOfUse().add(asNumberOfTermsOfUse);
    }

    /**
     * @param asTermsOfUse {@link List<String>} the asTermsOfUse to add
     */
    public void addAsTermsOfUse(List<String> asTermsOfUse) {
        this.loader.getAsTermsOfUse().add(asTermsOfUse);
    }

    /**
     * @param asTermsOfUseLang {@link List<String>} the asTermsOfUseLang to add
     */
    public void addAsTermsOfUseLang(List<String> asTermsOfUseLang) {
        this.loader.getAsTermsOfUseLang().add(asTermsOfUseLang);
    }

    /**
     * @param asTermsOfUseHref {@link List<String>} the asTermsOfUseHref to add
     */
    public void addAsTermsOfUseHref(List<String> asTermsOfUseHref) {
        this.loader.getAsTermsOfUseHref().add(asTermsOfUseHref);
    }

    /**
     * @param asAccessibilityQuestion {@link List<String>} the
     * asAccessibilityQuestion to add
     */
    public void addAsAccessibilityQuestion(List<String> asAccessibilityQuestion) {
        this.loader.getAsAccessibilityQuestion().add(asAccessibilityQuestion);
    }

    /**
     * @param asAccessibility {@link List<String>} the asAccessibility to add
     */
    public void addAsAccessibility(List<String> asAccessibility) {
        this.loader.getAsAccessibility().add(asAccessibility);
    }

    /**
     * @param asAccessibilityLang {@link List<String>} the asAccessibilityLang
     * to add
     */
    public void addAsAccessibilityLang(List<String> asAccessibilityLang) {
        this.loader.getAsAccessibilityLang().add(asAccessibilityLang);
    }

    /**
     * @param asSearchRoomTelephone {@link List<String>} the
     * asSearchRoomTelephone to add
     */
    public void addAsSearchRoomTelephone(List<String> asSearchRoomTelephone) {
        this.loader.getAsSearchRoomTelephone().add(asSearchRoomTelephone);
    }

    /**
     * @param asSearchRoomNumberOfEmail {@link List<String>} the
     * asSearchRoomNumberOfEmail to add
     */
    public void addAsSearchRoomNumberOfEmail(List<String> asSearchRoomNumberOfEmail) {
        this.loader.getAsSearchRoomNumberOfEmail().add(asSearchRoomNumberOfEmail);
    }

    /**
     * @param asSearchRoomEmailHref {@link List<String>} the
     * asSearchRoomEmailHref to add
     */
    public void addAsSearchRoomEmailHref(List<String> asSearchRoomEmailHref) {
        this.loader.getAsSearchRoomEmailHref().add(asSearchRoomEmailHref);
    }

    /**
     * @param asSearchRoomEmailTitle {@link List<String>} the
     * asSearchRoomEmailTitle to add
     */
    public void addAsSearchRoomEmailTitle(List<String> asSearchRoomEmailTitle) {
        this.loader.getAsSearchRoomEmailTitle().add(asSearchRoomEmailTitle);
    }

    /**
     * @param asSearchRoomEmailLang {@link List<String>} the
     * asSearchRoomEmailLang to add
     */
    public void addAsSearchRoomEmailLang(List<String> asSearchRoomEmailLang) {
        this.loader.getAsSearchRoomEmailLang().add(asSearchRoomEmailLang);
    }

    /**
     * @param asSearchRoomNumberOfWebpage {@link List<String>} the
     * asSearchRoomNumberOfWebpage to add
     */
    public void addAsSearchRoomNumberOfWebpage(List<String> asSearchRoomNumberOfWebpage) {
        this.loader.getAsSearchRoomNumberOfWebpage().add(asSearchRoomNumberOfWebpage);
    }

    /**
     * @param asSearchRoomWebpageHref {@link List<String>} the
     * asSearchRoomWebpageHref to add
     */
    public void addAsSearchRoomWebpageHref(List<String> asSearchRoomWebpageHref) {
        this.loader.getAsSearchRoomWebpageHref().add(asSearchRoomWebpageHref);
    }

    /**
     * @param asSearchRoomWebpageTitle {@link List<String>} the
     * asSearchRoomWebpageTitle to add
     */
    public void addAsSearchRoomWebpageTitle(List<String> asSearchRoomWebpageTitle) {
        this.loader.getAsSearchRoomWebpageTitle().add(asSearchRoomWebpageTitle);
    }

    /**
     * @param asSearchRoomWebpageLang {@link List<String>} the
     * asSearchRoomWebpageLang to add
     */
    public void addAsSearchRoomWebpageLang(List<String> asSearchRoomWebpageLang) {
        this.loader.getAsSearchRoomWebpageLang().add(asSearchRoomWebpageLang);
    }

    /**
     * @param asSearchRoomWorkPlaces {@link List<String>} the
     * asSearchRoomWorkPlaces to add
     */
    public void addAsSearchRoomWorkPlaces(List<String> asSearchRoomWorkPlaces) {
        this.loader.getAsSearchRoomWorkPlaces().add(asSearchRoomWorkPlaces);
    }

    /**
     * @param asSearchRoomComputerPlaces {@link List<String>} the
     * asSearchRoomComputerPlaces to add
     */
    public void addAsSearchRoomComputerPlaces(List<String> asSearchRoomComputerPlaces) {
        this.loader.getAsSearchRoomComputerPlaces().add(asSearchRoomComputerPlaces);
    }

    /**
     * @param asSearchRoomComputerPlacesDescription {@link List<String>}> the
     * asSearchRoomComputerPlacesDescription to add
     */
    public void addAsSearchRoomComputerPlacesDescription(List<String> asSearchRoomComputerPlacesDescription) {
        this.loader.getAsSearchRoomComputerPlacesDescription().add(asSearchRoomComputerPlacesDescription);
    }

    /**
     * @param asSearchRoomComputerPlacesDescriptionLang {@link List<String>} the
     * asSearchRoomComputerPlacesDescriptionLang to add
     */
    public void addAsSearchRoomComputerPlacesDescriptionLang(List<String> asSearchRoomComputerPlacesDescriptionLang) {
        this.loader.getAsSearchRoomComputerPlacesDescriptionLang().add(asSearchRoomComputerPlacesDescriptionLang);
    }

    /**
     * @param asSearchRoomMicrofilmReaders {@link List<String>} the
     * asSearchRoomMicrofilmReaders to add
     */
    public void addAsSearchRoomMicrofilmReaders(List<String> asSearchRoomMicrofilmReaders) {
        this.loader.getAsSearchRoomMicrofilmReaders().add(asSearchRoomMicrofilmReaders);
    }

    /**
     * @param asSearchRoomPhotographAllowance {@link List<String>} the
     * asSearchRoomPhotographAllowance to add
     */
    public void addAsSearchRoomPhotographAllowance(List<String> asSearchRoomPhotographAllowance) {
        this.loader.getAsSearchRoomPhotographAllowance().add(asSearchRoomPhotographAllowance);
    }

    /**
     * @param asSearchRoomNumberOfReadersTicket {@link List<String>} the
     * asSearchRoomNumberOfReadersTicket to add
     */
    public void addAsSearchRoomNumberOfReadersTicket(List<String> asSearchRoomNumberOfReadersTicket) {
        this.loader.getAsSearchRoomNumberOfReadersTicket().add(asSearchRoomNumberOfReadersTicket);
    }

    /**
     * @param asSearchRoomReadersTicketHref {@link List<String>} the
     * asSearchRoomReadersTicketHref to add
     */
    public void addAsSearchRoomReadersTicketHref(List<String> asSearchRoomReadersTicketHref) {
        this.loader.getAsSearchRoomReadersTicketHref().add(asSearchRoomReadersTicketHref);
    }

    /**
     * @param asSearchRoomReadersTicketContent {@link List<String>} the
     * asSearchRoomReadersTicketContent to add
     */
    public void addAsSearchRoomReadersTicketContent(List<String> asSearchRoomReadersTicketContent) {
        this.loader.getAsSearchRoomReadersTicketContent().add(asSearchRoomReadersTicketContent);
    }

    /**
     * @param asSearchRoomReadersTicketLang {@link List<String>} the
     * asSearchRoomReadersTicketLang to add
     */
    public void addAsSearchRoomReadersTicketLang(List<String> asSearchRoomReadersTicketLang) {
        this.loader.getAsSearchRoomReadersTicketLang().add(asSearchRoomReadersTicketLang);
    }

    /**
     * @param asSearchRoomNumberOfAdvancedOrders {@link List<String>} the
     * asSearchRoomNumberOfAdvancedOrders to add
     */
    public void addAsSearchRoomNumberOfAdvancedOrders(List<String> asSearchRoomNumberOfAdvancedOrders) {
        this.loader.getAsSearchRoomNumberOfAdvancedOrders().add(asSearchRoomNumberOfAdvancedOrders);
    }

    /**
     * @param asSearchRoomAdvancedOrdersHref {@link List<String>} the
     * asSearchRoomAdvancedOrdersHref to add
     */
    public void addAsSearchRoomAdvancedOrdersHref(List<String> asSearchRoomAdvancedOrdersHref) {
        this.loader.getAsSearchRoomAdvancedOrdersHref().add(asSearchRoomAdvancedOrdersHref);
    }

    /**
     * @param asSearchRoomAdvancedOrdersContent {@link List<String>} the
     * asSearchRoomAdvancedOrdersContent to add
     */
    public void addAsSearchRoomAdvancedOrdersContent(List<String> asSearchRoomAdvancedOrdersContent) {
        this.loader.getAsSearchRoomAdvancedOrdersContent().add(asSearchRoomAdvancedOrdersContent);
    }

    /**
     * @param asSearchRoomAdvancedOrdersLang {@link List<String>} the
     * asSearchRoomAdvancedOrdersLang to add
     */
    public void addAsSearchRoomAdvancedOrdersLang(List<String> asSearchRoomAdvancedOrdersLang) {
        this.loader.getAsSearchRoomAdvancedOrdersLang().add(asSearchRoomAdvancedOrdersLang);
    }

    /**
     * @param asSearchRoomResearchServicesContent {@link List<String>} the
     * asSearchRoomResearchServicesContent to add
     */
    public void addAsSearchRoomResearchServicesContent(List<String> asSearchRoomResearchServicesContent) {
        this.loader.getAsSearchRoomResearchServicesContent().add(asSearchRoomResearchServicesContent);
    }

    /**
     * @param asSearchRoomResearchServicesLang {@link List<String>} the
     * asSearchRoomResearchServicesLang to add
     */
    public void addAsSearchRoomResearchServicesLang(List<String> asSearchRoomResearchServicesLang) {
        this.loader.getAsSearchRoomResearchServicesLang().add(asSearchRoomResearchServicesLang);
    }

    /**
     * @param asLibraryQuestion {@link List<String>} the asLibraryQuestion to
     * add
     */
    public void addAsLibraryQuestion(List<String> asLibraryQuestion) {
        this.loader.getAsLibraryQuestion().add(asLibraryQuestion);
    }

    /**
     * @param asLibraryTelephone {@link List<String>} the asLibraryTelephone to
     * add
     */
    public void addAsLibraryTelephone(List<String> asLibraryTelephone) {
        this.loader.getAsLibraryTelephone().add(asLibraryTelephone);
    }

    /**
     * @param asLibraryNumberOfEmail {@link List<String>} the
     * asLibraryNumberOfEmail to add
     */
    public void addAsLibraryNumberOfEmail(List<String> asLibraryNumberOfEmail) {
        this.loader.getAsLibraryNumberOfEmail().add(asLibraryNumberOfEmail);
    }

    /**
     * @param asLibraryEmailHref {@link List<String>} the asLibraryEmailHref to
     * add
     */
    public void addAsLibraryEmailHref(List<String> asLibraryEmailHref) {
        this.loader.getAsLibraryEmailHref().add(asLibraryEmailHref);
    }

    /**
     * @param asLibraryEmailTitle {@link List<String>} the asLibraryEmailTitle
     * to add
     */
    public void addAsLibraryEmailTitle(List<String> asLibraryEmailTitle) {
        this.loader.getAsLibraryEmailTitle().add(asLibraryEmailTitle);
    }

    /**
     * @param asLibraryEmailLang {@link List<String>} the asLibraryEmailLang to
     * add
     */
    public void addAsLibraryEmailLang(List<String> asLibraryEmailLang) {
        this.loader.getAsLibraryEmailLang().add(asLibraryEmailLang);
    }

    /**
     * @param asLibraryNumberOfWebpage {@link List<String>} the
     * asLibraryNumberOfWebpage to add
     */
    public void addAsLibraryNumberOfWebpage(List<String> asLibraryNumberOfWebpage) {
        this.loader.getAsLibraryNumberOfWebpage().add(asLibraryNumberOfWebpage);
    }

    /**
     * @param asLibraryWebpageHref {@link List<String>} the asLibraryWebpageHref
     * to add
     */
    public void addAsLibraryWebpageHref(List<String> asLibraryWebpageHref) {
        this.loader.getAsLibraryWebpageHref().add(asLibraryWebpageHref);
    }

    /**
     * @param asLibraryWebpageTitle {@link List<String>} the
     * asLibraryWebpageTitle to add
     */
    public void addAsLibraryWebpageTitle(List<String> asLibraryWebpageTitle) {
        this.loader.getAsLibraryWebpageTitle().add(asLibraryWebpageTitle);
    }

    /**
     * @param asLibraryWebpageLang {@link List<String>} the asLibraryWebpageLang
     * to add
     */
    public void addAsLibraryWebpageLang(List<String> asLibraryWebpageLang) {
        this.loader.getAsLibraryWebpageLang().add(asLibraryWebpageLang);
    }

    /**
     * @param asLibraryMonographPublication {@link List<String>} the
     * asLibraryMonographPublication to add
     */
    public void addAsLibraryMonographPublication(List<String> asLibraryMonographPublication) {
        this.loader.getAsLibraryMonographPublication().add(asLibraryMonographPublication);
    }

    /**
     * @param asLibrarySerialPublication {@link List<String>} the
     * asLibrarySerialPublication to add
     */
    public void addAsLibrarySerialPublication(List<String> asLibrarySerialPublication) {
        this.loader.getAsLibrarySerialPublication().add(asLibrarySerialPublication);
    }

    /**
     * @param asInternetAccessQuestion {@link List<String>} the
     * asInternetAccessQuestion to add
     */
    public void addAsInternetAccessQuestion(List<String> asInternetAccessQuestion) {
        this.loader.getAsInternetAccessQuestion().add(asInternetAccessQuestion);
    }

    /**
     * @param asInternetAccessDescription {@link List<String>} the
     * asInternetAccessDescription to add
     */
    public void addAsInternetAccessDescription(List<String> asInternetAccessDescription) {
        this.loader.getAsInternetAccessDescription().add(asInternetAccessDescription);
    }

    /**
     * @param asInternetAccessDescriptionLang {@link List<String>} the
     * asInternetAccessDescriptionLang to add
     */
    public void addAsInternetAccessDescriptionLang(List<String> asInternetAccessDescriptionLang) {
        this.loader.getAsInternetAccessDescriptionLang().add(asInternetAccessDescriptionLang);
    }

    /**
     * @param asRestorationlabQuestion {@link List<String>} the
     * asRestorationlabQuestion to add
     */
    public void addAsRestorationlabQuestion(List<String> asRestorationlabQuestion) {
        this.loader.getAsRestorationlabQuestion().add(asRestorationlabQuestion);
    }

    /**
     * @param asRestorationlabDescription {@link List<String>} the
     * asRestorationlabDescription to add
     */
    public void addAsRestorationlabDescription(List<String> asRestorationlabDescription) {
        this.loader.getAsRestorationlabDescription().add(asRestorationlabDescription);
    }

    /**
     * @param asRestorationlabDescriptionLang {@link List<String>} the
     * asRestorationlabDescriptionLang to add
     */
    public void addAsRestorationlabDescriptionLang(List<String> asRestorationlabDescriptionLang) {
        this.loader.getAsRestorationlabDescriptionLang().add(asRestorationlabDescriptionLang);
    }

    /**
     * @param asRestorationlabTelephone {@link List<String>} the
     * asRestorationlabTelephone to add
     */
    public void addAsRestorationlabTelephone(List<String> asRestorationlabTelephone) {
        this.loader.getAsRestorationlabTelephone().add(asRestorationlabTelephone);
    }

    /**
     * @param asRestorationlabNumberOfEmail {@link List<String>} the
     * asRestorationlabNumberOfEmail to add
     */
    public void addAsRestorationlabNumberOfEmail(List<String> asRestorationlabNumberOfEmail) {
        this.loader.getAsRestorationlabNumberOfEmail().add(asRestorationlabNumberOfEmail);
    }

    /**
     * @param asRestorationlabEmailHref {@link List<String>} the
     * asRestorationlabEmailHref to add
     */
    public void addAsRestorationlabEmailHref(List<String> asRestorationlabEmailHref) {
        this.loader.getAsRestorationlabEmailHref().add(asRestorationlabEmailHref);
    }

    /**
     * @param asRestorationlabEmailTitle {@link List<String>} the
     * asRestorationlabEmailTitle to add
     */
    public void addAsRestorationlabEmailTitle(List<String> asRestorationlabEmailTitle) {
        this.loader.getAsRestorationlabEmailTitle().add(asRestorationlabEmailTitle);
    }

    /**
     * @param asRestorationlabEmailLang {@link List<String>} the
     * asRestorationlabEmailLang to add
     */
    public void addAsRestorationlabEmailLang(List<String> asRestorationlabEmailLang) {
        this.loader.getAsRestorationlabEmailLang().add(asRestorationlabEmailLang);
    }

    /**
     * @param asRestorationlabNumberOfWebpage {@link List<String>} the
     * asRestorationlabNumberOfWebpage to add
     */
    public void addAsRestorationlabNumberOfWebpage(List<String> asRestorationlabNumberOfWebpage) {
        this.loader.getAsRestorationlabNumberOfWebpage().add(asRestorationlabNumberOfWebpage);
    }

    /**
     * @param asRestorationlabWebpageHref {@link List<String>} the
     * asRestorationlabWebpageHref to add
     */
    public void addAsRestorationlabWebpageHref(List<String> asRestorationlabWebpageHref) {
        this.loader.getAsRestorationlabWebpageHref().add(asRestorationlabWebpageHref);
    }

    /**
     * @param asRestorationlabWebpageTitle {@link List<String>} the
     * asRestorationlabWebpageTitle to add
     */
    public void addAsRestorationlabWebpageTitle(List<String> asRestorationlabWebpageTitle) {
        this.loader.getAsRestorationlabWebpageTitle().add(asRestorationlabWebpageTitle);
    }

    /**
     * @param asRestorationlabWebpageLang {@link List<String>} the
     * asRestorationlabWebpageLang to add
     */
    public void addAsRestorationlabWebpageLang(List<String> asRestorationlabWebpageLang) {
        this.loader.getAsRestorationlabWebpageLang().add(asRestorationlabWebpageLang);
    }

    /**
     * @param asReproductionserQuestion {@link List<String>} the
     * asReproductionserQuestion to add
     */
    public void addAsReproductionserQuestion(List<String> asReproductionserQuestion) {
        this.loader.getAsReproductionserQuestion().add(asReproductionserQuestion);
    }

    /**
     * @param asReproductionserDescription {@link List<String>} the
     * asReproductionserDescription to add
     */
    public void addAsReproductionserDescription(List<String> asReproductionserDescription) {
        this.loader.getAsReproductionserDescription().add(asReproductionserDescription);
    }

    /**
     * @param asReproductionserDescriptionLang {@link List<String>} the
     * asReproductionserDescriptionLang to add
     */
    public void addAsReproductionserDescriptionLang(List<String> asReproductionserDescriptionLang) {
        this.loader.getAsReproductionserDescriptionLang().add(asReproductionserDescriptionLang);
    }

    /**
     * @param asReproductionserTelephone {@link List<String>} the
     * asReproductionserTelephone to add
     */
    public void addAsReproductionserTelephone(List<String> asReproductionserTelephone) {
        this.loader.getAsReproductionserTelephone().add(asReproductionserTelephone);
    }

    /**
     * @param asReproductionserNumberOfEmail {@link List<String>} the
     * asReproductionserNumberOfEmail to add
     */
    public void addAsReproductionserNumberOfEmail(List<String> asReproductionserNumberOfEmail) {
        this.loader.getAsReproductionserNumberOfEmail().add(asReproductionserNumberOfEmail);
    }

    /**
     * @param asReproductionserEmailHref {@link List<String>} the
     * asReproductionserEmailHref to add
     */
    public void addAsReproductionserEmailHref(List<String> asReproductionserEmailHref) {
        this.loader.getAsReproductionserEmailHref().add(asReproductionserEmailHref);
    }

    /**
     * @param asReproductionserEmailTitle {@link List<String>} the
     * asReproductionserEmailTitle to add
     */
    public void addAsReproductionserEmailTitle(List<String> asReproductionserEmailTitle) {
        this.loader.getAsReproductionserEmailTitle().add(asReproductionserEmailTitle);
    }

    /**
     * @param asReproductionserEmailLang {@link List<String>} the
     * asReproductionserEmailLang to add
     */
    public void addAsReproductionserEmailLang(List<String> asReproductionserEmailLang) {
        this.loader.getAsReproductionserEmailLang().add(asReproductionserEmailLang);
    }

    /**
     * @param asReproductionserNumberOfWebpage {@link List<String>} the
     * asReproductionserNumberOfWebpage to add
     */
    public void addAsReproductionserNumberOfWebpage(List<String> asReproductionserNumberOfWebpage) {
        this.loader.getAsReproductionserNumberOfWebpage().add(asReproductionserNumberOfWebpage);
    }

    /**
     * @param asReproductionserWebpageHref {@link List<String>} the
     * asReproductionserWebpageHref to add
     */
    public void addAsReproductionserWebpageHref(List<String> asReproductionserWebpageHref) {
        this.loader.getAsReproductionserWebpageHref().add(asReproductionserWebpageHref);
    }

    /**
     * @param asReproductionserWebpageTitle {@link List<String>} the
     * asReproductionserWebpageTitle to add
     */
    public void addAsReproductionserWebpageTitle(List<String> asReproductionserWebpageTitle) {
        this.loader.getAsReproductionserWebpageTitle().add(asReproductionserWebpageTitle);
    }

    /**
     * @param asReproductionserWebpageLang {@link List<String>} the
     * asReproductionserWebpageLang to add
     */
    public void addAsReproductionserWebpageLang(List<String> asReproductionserWebpageLang) {
        this.loader.getAsReproductionserWebpageLang().add(asReproductionserWebpageLang);
    }

    /**
     * @param asReproductionserMicrofilmServices {@link List<String>} the
     * asReproductionserMicrofilmServices to add
     */
    public void addAsReproductionserMicrofilmServices(List<String> asReproductionserMicrofilmServices) {
        this.loader.getAsReproductionserMicrofilmServices().add(asReproductionserMicrofilmServices);
    }

    /**
     * @param asReproductionserPhotographicServices {@link List<String>} the
     * asReproductionserPhotographicServices to add
     */
    public void addAsReproductionserPhotographicServices(List<String> asReproductionserPhotographicServices) {
        this.loader.getAsReproductionserPhotographicServices().add(asReproductionserPhotographicServices);
    }

    /**
     * @param asReproductionserDigitisationServices {@link List<String>} the
     * asReproductionserDigitisationServices to add
     */
    public void addAsReproductionserDigitisationServices(List<String> asReproductionserDigitisationServices) {
        this.loader.getAsReproductionserDigitisationServices().add(asReproductionserDigitisationServices);
    }

    /**
     * @param asReproductionserPhotocopyingServices {@link List<String>} the
     * asReproductionserPhotocopyingServices to add
     */
    public void addAsReproductionserPhotocopyingServices(List<String> asReproductionserPhotocopyingServices) {
        this.loader.getAsReproductionserPhotocopyingServices().add(asReproductionserPhotocopyingServices);
    }

    /**
     * @param asRecreationalServicesRefreshmentArea {@link List<String>} the
     * asRecreationalServicesRefreshmentArea to add
     */
    public void addAsRecreationalServicesRefreshmentArea(List<String> asRecreationalServicesRefreshmentArea) {
        this.loader.getAsRecreationalServicesRefreshmentArea().add(asRecreationalServicesRefreshmentArea);
    }

    /**
     * @param asRecreationalServicesRefreshmentAreaLang {@link List<String>} the
     * asRecreationalServicesRefreshmentAreaLang to add
     */
    public void addAsRecreationalServicesRefreshmentAreaLang(List<String> asRecreationalServicesRefreshmentAreaLang) {
        this.loader.getAsRecreationalServicesRefreshmentAreaLang().add(asRecreationalServicesRefreshmentAreaLang);
    }

    /**
     * @param asRSNumberOfExhibition {@link List<String>} the
     * asRSNumberOfExhibition to add
     */
    public void addAsRSNumberOfExhibition(List<String> asRSNumberOfExhibition) {
        this.loader.getAsRSNumberOfExhibition().add(asRSNumberOfExhibition);
    }

    /**
     * @param asRSExhibition {@link List<String>} the asRSExhibition to add
     */
    public void addAsRSExhibition(List<String> asRSExhibition) {
        this.loader.getAsRSExhibition().add(asRSExhibition);
    }

    /**
     * @param asRSExhibitionLang {@link List<String>} the asRSExhibitionLang to
     * add
     */
    public void addAsRSExhibitionLang(List<String> asRSExhibitionLang) {
        this.loader.getAsRSExhibitionLang().add(asRSExhibitionLang);
    }

    /**
     * @param asRSExhibitionWebpageHref {@link List<String>} the
     * asRSExhibitionWebpageHref to add
     */
    public void addAsRSExhibitionWebpageHref(List<String> asRSExhibitionWebpageHref) {
        this.loader.getAsRSExhibitionWebpageHref().add(asRSExhibitionWebpageHref);
    }

    /**
     * @param asRSExhibitionWebpageTitle {@link List<String>} the
     * asRSExhibitionWebpageTitle to add
     */
    public void addAsRSExhibitionWebpageTitle(List<String> asRSExhibitionWebpageTitle) {
        this.loader.getAsRSExhibitionWebpageTitle().add(asRSExhibitionWebpageTitle);
    }

    /**
     * @param asRSExhibitionWebpageLang {@link List<String>} the
     * asRSExhibitionWebpageLang to add
     */
    public void addAsRSExhibitionWebpageLang(List<String> asRSExhibitionWebpageLang) {
        this.loader.getAsRSExhibitionWebpageLang().add(asRSExhibitionWebpageLang);
    }

    /**
     * @param asRSNumberOfToursSessions {@link List<String>} the
     * asRSNumberOfToursSessions to add
     */
    public void addAsRSNumberOfToursSessions(List<String> asRSNumberOfToursSessions) {
        this.loader.getAsRSNumberOfToursSessions().add(asRSNumberOfToursSessions);
    }

    /**
     * @param asRSToursSessions {@link List<String>} the asRSToursSessions to
     * add
     */
    public void addAsRSToursSessions(List<String> asRSToursSessions) {
        this.loader.getAsRSToursSessions().add(asRSToursSessions);
    }

    /**
     * @param asRSToursSessionsLang {@link List<String>} the
     * asRSToursSessionsLang to add
     */
    public void addAsRSToursSessionsLang(List<String> asRSToursSessionsLang) {
        this.loader.getAsRSToursSessionsLang().add(asRSToursSessionsLang);
    }

    /**
     * @param asRSToursSessionsWebpageHref {@link List<String>} the
     * asRSToursSessionsWebpageHref to add
     */
    public void addAsRSToursSessionsWebpageHref(List<String> asRSToursSessionsWebpageHref) {
        this.loader.getAsRSToursSessionsWebpageHref().add(asRSToursSessionsWebpageHref);
    }

    /**
     * @param asRSToursSessionsWebpageTitle {@link List<String>} the
     * asRSToursSessionsWebpageTitle to add
     */
    public void addAsRSToursSessionsWebpageTitle(List<String> asRSToursSessionsWebpageTitle) {
        this.loader.getAsRSToursSessionsWebpageTitle().add(asRSToursSessionsWebpageTitle);
    }

    /**
     * @param asRSToursSessionsWebpageLang {@link List<String>} the
     * asRSToursSessionsWebpageLang to add
     */
    public void addAsRSToursSessionsWebpageLang(List<String> asRSToursSessionsWebpageLang) {
        this.loader.getAsRSToursSessionsWebpageLang().add(asRSToursSessionsWebpageLang);
    }

    /**
     * @param asRSNumberOfOtherServices {@link List<String>} the
     * asRSNumberOfOtherServices to add
     */
    public void addAsRSNumberOfOtherServices(List<String> asRSNumberOfOtherServices) {
        this.loader.getAsRSNumberOfOtherServices().add(asRSNumberOfOtherServices);
    }

    /**
     * @param asRSOtherServices {@link List<String>} the asRSOtherServices to
     * add
     */
    public void addAsRSOtherServices(List<String> asRSOtherServices) {
        this.loader.getAsRSOtherServices().add(asRSOtherServices);
    }

    /**
     * @param asRSOtherServicesLang {@link List<String>} the
     * asRSOtherServicesLang to add
     */
    public void addAsRSOtherServicesLang(List<String> asRSOtherServicesLang) {
        this.loader.getAsRSOtherServicesLang().add(asRSOtherServicesLang);
    }

    /**
     * @param asRSOtherServicesWebpageHref {@link List<String>} the
     * asRSOtherServicesWebpageHref to add
     */
    public void addAsRSOtherServicesWebpageHref(List<String> asRSOtherServicesWebpageHref) {
        this.loader.getAsRSOtherServicesWebpageHref().add(asRSOtherServicesWebpageHref);
    }

    /**
     * @param asRSOtherServicesWebpageTitle {@link List<String>} the
     * asRSOtherServicesWebpageTitle to add
     */
    public void addAsRSOtherServicesWebpageTitle(List<String> asRSOtherServicesWebpageTitle) {
        this.loader.getAsRSOtherServicesWebpageTitle().add(asRSOtherServicesWebpageTitle);
    }

    /**
     * @param asRSOtherServicesWebpageLang {@link List<String>} the
     * asRSOtherServicesWebpageLang to add
     */
    public void addAsRSOtherServicesWebpageLang(List<String> asRSOtherServicesWebpageLang) {
        this.loader.getAsRSOtherServicesWebpageLang().add(asRSOtherServicesWebpageLang);
    }
    private EAG2012Loader loader;
    /**
     * Eag {@link Eag} JAXB object.
     */
    protected Eag eag;

    @Override
    public Eag LoaderEAG2012(Eag eag, EAG2012Loader eag2012Loader) {

        this.eag = eag;
        this.loader = eag2012Loader;
        main();
        return this.eag;
    }

    /**
     * Method main of class LoadAccessAndServicesTabValues to load all values of
     * "Access and Services" tab
     */
    private void main() {
        this.log.debug("Method start: \"Main of class LoadAccessAndServicesTabValues\"");
        // Repositories info.
        if (this.eag.getArchguide() != null && this.eag.getArchguide().getDesc() != null
                && this.eag.getArchguide().getDesc().getRepositories() != null
                && !this.eag.getArchguide().getDesc().getRepositories().getRepository().isEmpty()) {
            // For each repository
            for (int i = 0; i < this.eag.getArchguide().getDesc().getRepositories().getRepository().size(); i++) {
                repository = this.eag.getArchguide().getDesc().getRepositories().getRepository().get(i);
                if (repository != null) {
                    // Timetable info
                    loadAccessAndServicesTimetable();
                    // Travelling directions.
                    loadAccessAndServicesTravelling();
                    // Accessible to the public.
                    loadAccessAndServicesAccessible();
                    // Terms of use.
                    loadAccessAndServicesTermsOfUse();
                    // Facilities for disabled people available.
                    loadAccessAndServicesFacilities();
                    // Searchroom.
                    loadAccessAndServicesSearchroom();
                    // Library
                    loadAccessAndServicesLibrary();
                    // Internet access.
                    loadAccessAndServicesInternetAccess();
                    // Technical services.
                    // Restoration laboratory.
                    loadAccessAndServicesRestorationlab();
                    // Reproduction services.
                    loadAccessAndServicesReproductionServices();
                    // Recreational services
                    loadAccessAndServicesRecreationalServices();
                }
            }
        }
        this.log.debug("End method: \"Main of class LoadAccessAndServicesTabValues\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Timetable of institution
     */
    private void loadAccessAndServicesTimetable() {
        this.log.debug("Method start: \"loadAccessAndServicesTimetable\"");
        // Timetable info
        List<String> numberOfOpeningList = new ArrayList<>();
        List<String> openingContentList = new ArrayList<>();
        List<String> openingLangList = new ArrayList<>();
        List<String> openingHrefList = new ArrayList<>();
        List<String> closingList = new ArrayList<>();
        List<String> closingLangList = new ArrayList<>();
        if (repository.getTimetable() != null) {
            Timetable timetable = repository.getTimetable();
            // Opening times.
            if (!timetable.getOpening().isEmpty()) {
                for (int j = 0; j < timetable.getOpening().size(); j++) {
                    numberOfOpeningList.add("");
                    if (!timetable.getOpening().isEmpty()
                            && timetable.getOpening().size() >= j
                            && timetable.getOpening().get(j).getContent() != null
                            && !timetable.getOpening().get(j).getContent().isEmpty()) {
                        openingContentList.add(timetable.getOpening().get(j).getContent());
                    } else {
                        openingContentList.add("");
                    }
                    if (!timetable.getOpening().isEmpty()
                            && timetable.getOpening().size() >= j
                            && timetable.getOpening().get(j).getLang() != null
                            && !timetable.getOpening().get(j).getLang().isEmpty()) {
                        openingLangList.add(timetable.getOpening().get(j).getLang());
                    } else {
                        openingLangList.add(Eag2012.OPTION_NONE);
                    }
                    if (!timetable.getOpening().isEmpty()
                            && timetable.getOpening().size() >= j
                            && timetable.getOpening().get(j).getHref() != null
                            && !timetable.getOpening().get(j).getHref().isEmpty()) {
                        openingHrefList.add(timetable.getOpening().get(j).getHref());
                    } else {
                        openingHrefList.add("");
                    }
                }
            }
            // Closing dates.
            if (!timetable.getClosing().isEmpty()) {
                // Closing dates for institution.
                for (int j = 0; j < timetable.getClosing().size(); j++) {
                    if (!timetable.getClosing().isEmpty()
                            && timetable.getClosing().size() >= j
                            && timetable.getClosing().get(j).getContent() != null
                            && !timetable.getClosing().get(j).getContent().isEmpty()) {
                        closingList.add(timetable.getClosing().get(j).getContent());
                    } else {
                        closingList.add("");
                    }
                    if (!timetable.getClosing().isEmpty()
                            && timetable.getClosing().size() >= j
                            && timetable.getClosing().get(j).getLang() != null
                            && !timetable.getClosing().get(j).getLang().isEmpty()) {
                        closingLangList.add(timetable.getClosing().get(j).getLang());
                    } else {
                        closingLangList.add(Eag2012.OPTION_NONE);
                    }
                }
            }
        }
        this.addAsNumberOfOpening(numberOfOpeningList);
        this.addAsOpeningContent(openingContentList);
        this.addAsOpeningLang(openingLangList);
        this.addAsOpeningHref(openingHrefList);
        this.addAsClosing(closingList);
        this.addAsClosingLang(closingLangList);
        this.log.debug("End method: \"loadAccessAndServicesTimetable\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Travelling directions of institution
     */
    private void loadAccessAndServicesTravelling() {
        this.log.debug("Method start: \"loadAccessAndServicesTravelling\"");
        // Travelling directions.
        List<String> numberOfDirectios = new ArrayList<String>();
        List<String> directiosValue = new ArrayList<String>();
        List<String> directiosLang = new ArrayList<String>();
        List<String> directiosHref = new ArrayList<String>();
        if (!repository.getDirections().isEmpty()) {
            for (int j = 0; j < repository.getDirections().size(); j++) {
                numberOfDirectios.add("");
                if (repository.getDirections().get(j) != null
                        && !repository.getDirections().get(j).getContent().isEmpty()) {
                    // TODO: Review for multiple values.
                    for (int k = 0; k < repository.getDirections().get(j).getContent().size(); k++) {
                        if (repository.getDirections().get(j).getContent().get(k) != null
                                && repository.getDirections().get(j).getContent().get(k) instanceof String
                                && repository.getDirections().get(j).getContent().get(k).toString() != null
                                && !repository.getDirections().get(j).getContent().get(k).toString().isEmpty()
                                && !repository.getDirections().get(j).getContent().get(k).toString().startsWith("\n")) {
                            directiosValue.add(repository.getDirections().get(j).getContent().get(k).toString());
                        }
                        if (repository.getDirections().get(j).getContent().get(k) != null
                                && repository.getDirections().get(j).getContent().get(k) instanceof Citation) {
                            Citation citation = (Citation) repository.getDirections().get(j).getContent().get(k);
                            if (k == 0) {
                                directiosValue.add("");
                            }
                            if (citation.getHref() != null
                                    && !citation.getHref().isEmpty()) {
                                directiosHref.add(citation.getHref());
                            } else {
                                directiosHref.add("");
                            }
                        }
                    }
                }
                // Travelling directions language.
                if (repository.getDirections().get(j) != null
                        && repository.getDirections().get(j).getLang() != null
                        && !repository.getDirections().get(j).getLang().isEmpty()) {
                    directiosLang.add(repository.getDirections().get(j).getLang());
                } else {
                    directiosLang.add(Eag2012.OPTION_NONE);
                }
            }
        }
        this.addAsNumberOfDirections(numberOfDirectios);
        this.addAsDirections(directiosValue);
        this.addAsDirectionsLang(directiosLang);
        this.addAsDirectionsCitationHref(directiosHref);
        this.log.debug("End method: \"loadAccessAndServicesTravelling\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Accessible to the public of institution
     */
    private void loadAccessAndServicesAccessible() {
        this.log.debug("Method start: \"loadAccessAndServicesAccessible\"");
        //Accessible to the public.
        List<String> accessQuestion = new ArrayList<String>();
        List<String> accessValue = new ArrayList<String>();
        List<String> accessLang = new ArrayList<String>();
        if (repository.getAccess() != null) {
            if (repository.getAccess().getQuestion() != null
                    && !repository.getAccess().getQuestion().isEmpty()) {
                accessQuestion.add(repository.getAccess().getQuestion());
            } else {
                accessQuestion.add(Eag2012.OPTION_NO);
            }
            if (!repository.getAccess().getRestaccess().isEmpty()) {
                for (int j = 0; j < repository.getAccess().getRestaccess().size(); j++) {
                    if (repository.getAccess().getRestaccess().size() >= j
                            && repository.getAccess().getRestaccess().get(j).getContent() != null
                            && !repository.getAccess().getRestaccess().get(j).getContent().isEmpty()) {
                        accessValue.add(repository.getAccess().getRestaccess().get(j).getContent());
                    } else {
                        accessValue.add("");
                    }
                    if (repository.getAccess().getRestaccess().size() >= j
                            && repository.getAccess().getRestaccess().get(j).getLang() != null
                            && !repository.getAccess().getRestaccess().get(j).getLang().isEmpty()) {
                        accessLang.add(repository.getAccess().getRestaccess().get(j).getLang());
                    } else {
                        accessLang.add(Eag2012.OPTION_NONE);
                    }
                }
            }
        }
        this.addAsAccessQuestion(accessQuestion);
        this.addAsRestaccess(accessValue);
        this.addAsRestaccessLang(accessLang);
        this.log.debug("End method: \"loadAccessAndServicesAccessible\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Terms of use of institution
     */
    private void loadAccessAndServicesTermsOfUse() {
        this.log.debug("Method start: \"loadAccessAndServicesTermsOfUse\"");
        // Terms of use.
        List<String> numberOfTermsOfUse = new ArrayList<String>();
        List<String> termsOfUseValue = new ArrayList<String>();
        List<String> termsOfUseHref = new ArrayList<String>();
        List<String> termsOfUseLang = new ArrayList<String>();
        if (repository.getAccess() != null
                && !repository.getAccess().getTermsOfUse().isEmpty()) {
            for (int j = 0; j < repository.getAccess().getTermsOfUse().size(); j++) {
                numberOfTermsOfUse.add("");
                termsOfUseValue.add(repository.getAccess().getTermsOfUse().get(j).getContent());
                termsOfUseLang.add(repository.getAccess().getTermsOfUse().get(j).getLang());
                termsOfUseHref.add(repository.getAccess().getTermsOfUse().get(j).getHref());
            }
        }
        this.addAsNumberOfTermsOfUse(numberOfTermsOfUse);
        this.addAsTermsOfUse(termsOfUseValue);
        this.addAsTermsOfUseHref(termsOfUseHref);
        this.addAsTermsOfUseLang(termsOfUseLang);
        this.log.debug("End method: \"loadAccessAndServicesTermsOfUse\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Facilities for disabled people available of institution
     */
    private void loadAccessAndServicesFacilities() {
        this.log.debug("Method start: \"loadAccessAndServicesFacilities\"");
        // Facilities for disabled people available.
        List<String> accesibilityQuestion = new ArrayList<String>();
        List<String> accesibilityValue = new ArrayList<String>();
        List<String> accesibilityLang = new ArrayList<String>();
        if (!repository.getAccessibility().isEmpty()) {
            for (int j = 0; j < repository.getAccessibility().size(); j++) {
                if (repository.getAccessibility().size() >= j
                        && repository.getAccessibility().get(j).getQuestion() != null
                        && !repository.getAccessibility().get(j).getQuestion().isEmpty()) {
                    accesibilityQuestion.add(repository.getAccessibility().get(j).getQuestion());
                } else {
                    accesibilityQuestion.add(Eag2012.OPTION_NO);
                }
                if (repository.getAccessibility().size() >= j
                        && repository.getAccessibility().get(j).getContent() != null
                        && !repository.getAccessibility().get(j).getContent().isEmpty()) {
                    accesibilityValue.add(repository.getAccessibility().get(j).getContent());

                    if (repository.getAccessibility().size() >= j
                            && repository.getAccessibility().get(j).getLang() != null
                            && !repository.getAccessibility().get(j).getLang().isEmpty()) {
                        accesibilityLang.add(repository.getAccessibility().get(j).getLang());
                    } else {
                        accesibilityLang.add(Eag2012.OPTION_NONE);
                    }
                }
            }
        }
        this.addAsAccessibilityQuestion(accesibilityQuestion);
        this.addAsAccessibility(accesibilityValue);
        this.addAsAccessibilityLang(accesibilityLang);
        this.log.debug("End method: \"loadAccessAndServicesFacilities\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Searchroom of institution
     */
    private void loadAccessAndServicesSearchroom() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchroom\"");
        // Searchroom.
        srTelephone = new ArrayList<String>();
        srNumberOfEmail = new ArrayList<String>();
        srEmailHref = new ArrayList<String>();
        srEmailTitle = new ArrayList<String>();
        srNumberOfWebpage = new ArrayList<String>();
        srWebpageHref = new ArrayList<String>();
        srWebpageTitle = new ArrayList<String>();
        srWorkingPlaces = new ArrayList<String>();
        srComputerPlacesNum = new ArrayList<String>();
        srComputerPlacesValue = new ArrayList<String>();
        srComputerPlacesLang = new ArrayList<String>();
        srMicrofilmReadersNum = new ArrayList<String>();
        srPhotographAllowance = new ArrayList<String>();
        srNumberOfReadersTicket = new ArrayList<String>();
        srReadersTicket = new ArrayList<String>();
        srReadersTicketHref = new ArrayList<String>();
        srReadersTicketLang = new ArrayList<String>();
        srNumberOfAdvancedOrders = new ArrayList<String>();
        srAdvancedOrders = new ArrayList<String>();
        srAdvancedOrdersHref = new ArrayList<String>();
        srAdvancedOrdersLang = new ArrayList<String>();
        srResearchServices = new ArrayList<String>();
        srResearchServicesLang = new ArrayList<String>();
        if (repository.getServices() != null && repository.getServices().getSearchroom() != null) {
            searchRoom = repository.getServices().getSearchroom();
            // Contact.
            loadAccessAndServicesSearchRoomContact();
            // Webpage
            loadAccessAndServicesSearchRoomWebpage();
            // Number of working places.
            loadAccessAndServicesSearchRoomWorkingPlaces();
            // Number of computer places.
            loadAccessAndServicesSearchRoomComputerPlaces();
            // Number of microfilm/fiche readers.
            // Photograph allowance.
            loadAccessAndServicesSearchRoomMicrofilmPhotograph();
            //		// Readerʼs ticket.
            loadAccessAndServicesSearchRoomReaderTicket();
            // Advanced orders.
            loadAccessAndServicesSearchRoomAdvancedOrders();
            // Research services.
            loadAccessAndServicesSearchRoomResearchServices();
        }
        this.addAsSearchRoomTelephone(srTelephone);
        this.addAsSearchRoomNumberOfEmail(srNumberOfEmail);
        this.addAsSearchRoomEmailHref(srEmailHref);
        this.addAsSearchRoomEmailTitle(srEmailTitle);
        this.addAsSearchRoomNumberOfWebpage(srNumberOfWebpage);
        this.addAsSearchRoomWebpageHref(srWebpageHref);
        this.addAsSearchRoomWebpageTitle(srWebpageTitle);
        this.addAsSearchRoomWorkPlaces(srWorkingPlaces);
        this.addAsSearchRoomComputerPlaces(srComputerPlacesNum);
        this.addAsSearchRoomComputerPlacesDescription(srComputerPlacesValue);
        this.addAsSearchRoomComputerPlacesDescriptionLang(srComputerPlacesLang);
        this.addAsSearchRoomMicrofilmReaders(srMicrofilmReadersNum);
        this.addAsSearchRoomPhotographAllowance(srPhotographAllowance);
        this.addAsSearchRoomNumberOfReadersTicket(srNumberOfReadersTicket);
        this.addAsSearchRoomReadersTicketContent(srReadersTicket);
        this.addAsSearchRoomReadersTicketHref(srReadersTicketHref);
        this.addAsSearchRoomReadersTicketLang(srReadersTicketLang);
        this.addAsSearchRoomNumberOfAdvancedOrders(srNumberOfAdvancedOrders);
        this.addAsSearchRoomAdvancedOrdersContent(srAdvancedOrders);
        this.addAsSearchRoomAdvancedOrdersHref(srAdvancedOrdersHref);
        this.addAsSearchRoomAdvancedOrdersLang(srAdvancedOrdersLang);
        this.addAsSearchRoomResearchServicesContent(srResearchServices);
        this.addAsSearchRoomResearchServicesLang(srResearchServicesLang);
        this.log.debug("End method: \"loadAccessAndServicesSearchroom\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * searchroom in contact of institution
     */
    private void loadAccessAndServicesSearchRoomContact() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchRoomContact\"");
        // Contact.
        if (searchRoom.getContact() != null) {
            // Contact -Telephone.
            if (!searchRoom.getContact().getTelephone().isEmpty()) {
                for (int j = 0; j < searchRoom.getContact().getTelephone().size(); j++) {
                    if (searchRoom.getContact().getTelephone().get(j).getContent() != null
                            && !searchRoom.getContact().getTelephone().get(j).getContent().isEmpty()) {
                        srTelephone.add(searchRoom.getContact().getTelephone().get(j).getContent());
                    } else {
                        srTelephone.add("");
                    }
                }
            } else {
                srTelephone.add("");
            }
            // Contact -Email.
            if (!searchRoom.getContact().getEmail().isEmpty()) {
                for (int j = 0; j < searchRoom.getContact().getEmail().size(); j++) {
                    srNumberOfEmail.add("");
                    if (searchRoom.getContact().getEmail().get(j).getHref() != null
                            && !searchRoom.getContact().getEmail().get(j).getHref().isEmpty()) {
                        srEmailHref.add(searchRoom.getContact().getEmail().get(j).getHref());
                    } else {
                        srEmailHref.add("");
                    }
                    if (searchRoom.getContact().getEmail().get(j).getContent() != null
                            && !searchRoom.getContact().getEmail().get(j).getContent().isEmpty()) {
                        srEmailTitle.add(searchRoom.getContact().getEmail().get(j).getContent());
                    } else {
                        srEmailTitle.add("");
                    }
                }
            } else {
                srNumberOfEmail.add("");
                srEmailHref.add("");
                srEmailTitle.add("");
            }
        }
        this.log.debug("End method: \"loadAccessAndServicesSearchRoomContact\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * searchroom in Webpage of institution
     */
    private void loadAccessAndServicesSearchRoomWebpage() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchRoomWebpage\"");
        // Webpage
        if (!searchRoom.getWebpage().isEmpty()) {
            for (int j = 0; j < searchRoom.getWebpage().size(); j++) {
                srNumberOfWebpage.add("");
                if (searchRoom.getWebpage().get(j).getHref() != null
                        && !searchRoom.getWebpage().get(j).getHref().isEmpty()) {
                    srWebpageHref.add(searchRoom.getWebpage().get(j).getHref());
                } else {
                    srWebpageHref.add("");
                }
                if (searchRoom.getWebpage().get(j).getContent() != null
                        && !searchRoom.getWebpage().get(j).getContent().isEmpty()) {
                    srWebpageTitle.add(searchRoom.getWebpage().get(j).getContent());
                } else {
                    srWebpageTitle.add("");
                }
            }
        } else {
            srNumberOfWebpage.add("");
            srWebpageHref.add("");
            srWebpageTitle.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesSearchRoomWebpage\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * searchroom in Number of working places of institution
     */
    private void loadAccessAndServicesSearchRoomWorkingPlaces() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchRoomWorkingPlaces\"");
        // Number of working places.
        if (searchRoom.getWorkPlaces() != null
                && searchRoom.getWorkPlaces().getNum() != null
                && searchRoom.getWorkPlaces().getNum().getContent() != null
                && !searchRoom.getWorkPlaces().getNum().getContent().isEmpty()) {
            srWorkingPlaces.add(searchRoom.getWorkPlaces().getNum().getContent());
        } else {
            srWorkingPlaces.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesSearchRoomWorkingPlaces\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * searchroom in Number of computer places of institution
     */
    private void loadAccessAndServicesSearchRoomComputerPlaces() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchRoomComputerPlaces\"");
        // Number of computer places.
        if (searchRoom.getComputerPlaces() != null
                && searchRoom.getComputerPlaces().getNum() != null
                && searchRoom.getComputerPlaces().getNum().getContent() != null
                && !searchRoom.getComputerPlaces().getNum().getContent().isEmpty()) {
            srComputerPlacesNum.add(searchRoom.getComputerPlaces().getNum().getContent());

            // Description of computer places.
            if (searchRoom.getComputerPlaces().getDescriptiveNote() != null
                    && !searchRoom.getComputerPlaces().getDescriptiveNote().getP().isEmpty()) {
                for (int j = 0; j < searchRoom.getComputerPlaces().getDescriptiveNote().getP().size(); j++) {
                    if (searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j) != null
                            && searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getContent() != null
                            && !searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
                        srComputerPlacesValue.add(searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getContent());

                        if (searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getLang() != null
                                && !searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
                            srComputerPlacesLang.add(searchRoom.getComputerPlaces().getDescriptiveNote().getP().get(j).getLang());
                        } else {
                            srComputerPlacesLang.add(Eag2012.OPTION_NONE);
                        }
                    }
                }
            }
        } else {
            srComputerPlacesNum.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesSearchRoomComputerPlaces\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * searchroom in Number of microfilm/fiche readers and Photograph allowance
     * of institution
     */
    private void loadAccessAndServicesSearchRoomMicrofilmPhotograph() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchRoomMicrofilmPhotograph\"");
        // Number of microfilm/fiche readers.
        if (searchRoom.getMicrofilmPlaces() != null
                && searchRoom.getMicrofilmPlaces().getNum() != null
                && searchRoom.getMicrofilmPlaces().getNum().getContent() != null
                && !searchRoom.getMicrofilmPlaces().getNum().getContent().isEmpty()) {
            srMicrofilmReadersNum.add(searchRoom.getMicrofilmPlaces().getNum().getContent());
        } else {
            srMicrofilmReadersNum.add("");
        }
        // Photograph allowance.
        if (searchRoom.getPhotographAllowance() != null
                && searchRoom.getPhotographAllowance().getValue() != null
                && !searchRoom.getPhotographAllowance().getValue().isEmpty()) {
            String photographAllowanceValue = searchRoom.getPhotographAllowance().getValue();
            if (Eag2012.OPTION_DEPENDING_TEXT.equalsIgnoreCase(photographAllowanceValue)) {
                photographAllowanceValue = Eag2012.OPTION_DEPENDING;
            } else if (Eag2012.OPTION_WITHOUT_TEXT.equalsIgnoreCase(photographAllowanceValue)) {
                photographAllowanceValue = Eag2012.OPTION_WITHOUT;
            }
            srPhotographAllowance.add(photographAllowanceValue);
        } else {
            srPhotographAllowance.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesSearchRoomMicrofilmPhotograph\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * searchroom in Readerʼs ticket of institution
     */
    private void loadAccessAndServicesSearchRoomReaderTicket() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchRoomReaderTicket\"");
        // Readerʼs ticket.
        if (!searchRoom.getReadersTicket().isEmpty()) {
            for (int j = 0; j < searchRoom.getReadersTicket().size(); j++) {
                srNumberOfReadersTicket.add("");
                if (searchRoom.getReadersTicket().get(j) != null
                        && searchRoom.getReadersTicket().get(j).getContent() != null
                        && !searchRoom.getReadersTicket().get(j).getContent().isEmpty()) {
                    srReadersTicket.add(searchRoom.getReadersTicket().get(j).getContent());
                } else {
                    srReadersTicket.add("");
                }

                if (searchRoom.getReadersTicket().get(j) != null
                        && searchRoom.getReadersTicket().get(j).getHref() != null
                        && !searchRoom.getReadersTicket().get(j).getHref().isEmpty()) {
                    srReadersTicketHref.add(searchRoom.getReadersTicket().get(j).getHref());
                } else {
                    srReadersTicketHref.add("");
                }

                if (searchRoom.getReadersTicket().get(j) != null
                        && searchRoom.getReadersTicket().get(j).getLang() != null
                        && !searchRoom.getReadersTicket().get(j).getLang().isEmpty()) {
                    srReadersTicketLang.add(searchRoom.getReadersTicket().get(j).getLang());
                } else {
                    srReadersTicketLang.add("");
                }
            }
        } else {
            srNumberOfReadersTicket.add("");
            srReadersTicket.add("");
            srReadersTicketHref.add("");
            srReadersTicketLang.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesSearchRoomReaderTicket\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * searchroom in Advanced orders of institution
     */
    private void loadAccessAndServicesSearchRoomAdvancedOrders() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchRoomAdvancedOrders\"");
        // Advanced orders.
        if (!searchRoom.getAdvancedOrders().isEmpty()) {
            for (int j = 0; j < searchRoom.getAdvancedOrders().size(); j++) {
                srNumberOfAdvancedOrders.add("");
                if (searchRoom.getAdvancedOrders().get(j) != null
                        && searchRoom.getAdvancedOrders().get(j).getContent() != null
                        && !searchRoom.getAdvancedOrders().get(j).getContent().isEmpty()) {
                    srAdvancedOrders.add(searchRoom.getAdvancedOrders().get(j).getContent());
                } else {
                    srAdvancedOrders.add("");
                }

                if (searchRoom.getAdvancedOrders().get(j) != null
                        && searchRoom.getAdvancedOrders().get(j).getHref() != null
                        && !searchRoom.getAdvancedOrders().get(j).getHref().isEmpty()) {
                    srAdvancedOrdersHref.add(searchRoom.getAdvancedOrders().get(j).getHref());
                } else {
                    srAdvancedOrdersHref.add("");
                }

                if (searchRoom.getAdvancedOrders().get(j) != null
                        && searchRoom.getAdvancedOrders().get(j).getLang() != null
                        && !searchRoom.getAdvancedOrders().get(j).getLang().isEmpty()) {
                    srAdvancedOrdersLang.add(searchRoom.getAdvancedOrders().get(j).getLang());
                } else {
                    srAdvancedOrdersLang.add("");
                }
            }
        } else {
            srNumberOfAdvancedOrders.add("");
            srAdvancedOrders.add("");
            srAdvancedOrdersHref.add("");
            srAdvancedOrdersLang.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesSearchRoomAdvancedOrders\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * searchroom in Research services of institution
     */
    private void loadAccessAndServicesSearchRoomResearchServices() {
        this.log.debug("Method start: \"loadAccessAndServicesSearchRoomResearchServices\"");
        // Research services.
        if (!searchRoom.getResearchServices().isEmpty()) {
            for (int j = 0; j < searchRoom.getResearchServices().size(); j++) {
                if (searchRoom.getResearchServices().get(j) != null
                        && searchRoom.getResearchServices().get(j).getDescriptiveNote() != null
                        && !searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().isEmpty()) {
                    for (int k = 0; k < searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().size(); k++) {
                        if (searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k) != null
                                && searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getContent() != null
                                && !searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getContent().isEmpty()) {
                            srResearchServices.add(searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getContent());
                        } else {
                            srResearchServices.add("");
                        }

                        if (searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k) != null
                                && searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getLang() != null
                                && !searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getLang().isEmpty()) {
                            srResearchServicesLang.add(searchRoom.getResearchServices().get(j).getDescriptiveNote().getP().get(k).getLang());
                        } else {
                            srResearchServicesLang.add("");
                        }
                    }
                } else {
                    srResearchServices.add("");
                    srResearchServicesLang.add(Eag2012.OPTION_NONE);
                }
            }
        } else {
            srResearchServices.add("");
            srResearchServicesLang.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesSearchRoomResearchServices\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Library of institution
     */
    private void loadAccessAndServicesLibrary() {
        this.log.debug("Method start: \"loadAccessAndServicesLibrary\"");
        // Library
        libQuestion = new ArrayList<String>();
        libTelephone = new ArrayList<String>();
        libNumberOfEmail = new ArrayList<String>();
        libEmailHref = new ArrayList<String>();
        libEmailTitle = new ArrayList<String>();
        libNumberOfWebpage = new ArrayList<String>();
        libWebpageHref = new ArrayList<String>();
        libWebpageTitle = new ArrayList<String>();
        libMonographicPubNum = new ArrayList<String>();
        libSerialPubNum = new ArrayList<String>();
        if (repository.getServices() != null
                && repository.getServices().getLibrary() != null) {
            library = repository.getServices().getLibrary();
            //		// Question.
            loadAccessAndServicesLibraryQuestion();
            // Contact.
            loadAccessAndServicesLibraryContact();
            // Webpage.
            loadAccessAndServicesLibraryWebpage();
            // Monographic publications.
            // Serial publications.
            loadAccessAndServicesLibraryMonographicSerial();
        }
        this.addAsLibraryQuestion(libQuestion);
        this.addAsLibraryTelephone(libTelephone);
        this.addAsLibraryNumberOfEmail(libNumberOfEmail);
        this.addAsLibraryEmailHref(libEmailHref);
        this.addAsLibraryEmailTitle(libEmailTitle);
        this.addAsLibraryNumberOfWebpage(libNumberOfWebpage);
        this.addAsLibraryWebpageHref(libWebpageHref);
        this.addAsLibraryWebpageTitle(libWebpageTitle);
        this.addAsLibraryMonographPublication(libMonographicPubNum);
        this.addAsLibrarySerialPublication(libSerialPubNum);
        this.log.debug("End method: \"loadAccessAndServicesLibrary\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Library in Contact of institution
     */
    private void loadAccessAndServicesLibraryContact() {
        this.log.debug("Method start: \"loadAccessAndServicesLibraryContact\"");
        // Contact.
        if (library.getContact() != null) {
            if (!library.getContact().getTelephone().isEmpty()) {
                for (int j = 0; j < library.getContact().getTelephone().size(); j++) {
                    if (library.getContact().getTelephone().get(j) != null
                            && library.getContact().getTelephone().get(j).getContent() != null
                            && !library.getContact().getTelephone().get(j).getContent().isEmpty()) {
                        libTelephone.add(library.getContact().getTelephone().get(j).getContent());
                    } else {
                        libTelephone.add("");
                    }
                }
            } else {
                libTelephone.add("");
            }

            if (!library.getContact().getEmail().isEmpty()) {
                for (int j = 0; j < library.getContact().getEmail().size(); j++) {
                    libNumberOfEmail.add("");
                    if (library.getContact().getEmail().get(j) != null
                            && library.getContact().getEmail().get(j).getContent() != null
                            && !library.getContact().getEmail().get(j).getContent().isEmpty()) {
                        libEmailTitle.add(library.getContact().getEmail().get(j).getContent());
                    } else {
                        libEmailTitle.add("");
                    }

                    if (library.getContact().getEmail().get(j) != null
                            && library.getContact().getEmail().get(j).getHref() != null
                            && !library.getContact().getEmail().get(j).getHref().isEmpty()) {
                        libEmailHref.add(library.getContact().getEmail().get(j).getHref());
                    } else {
                        libEmailHref.add("");
                    }
                }
            } else {
                libNumberOfEmail.add("");
                libEmailHref.add("");
                libEmailTitle.add("");
            }
        } else {
            libTelephone.add("");
            libNumberOfEmail.add("");
            libEmailHref.add("");
            libEmailTitle.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesLibraryContact\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Library in Webpage of institution
     */
    private void loadAccessAndServicesLibraryWebpage() {
        this.log.debug("Method start: \"loadAccessAndServicesLibraryWebpage\"");
        // Webpage.
        if (!library.getWebpage().isEmpty()) {
            for (int j = 0; j < library.getWebpage().size(); j++) {
                libNumberOfWebpage.add("");
                if (library.getWebpage().get(j) != null
                        && library.getWebpage().get(j).getContent() != null
                        && !library.getWebpage().get(j).getContent().isEmpty()) {
                    libWebpageTitle.add(library.getWebpage().get(j).getContent());
                } else {
                    libWebpageTitle.add("");
                }

                if (library.getWebpage().get(j) != null
                        && library.getWebpage().get(j).getHref() != null
                        && !library.getWebpage().get(j).getHref().isEmpty()) {
                    libWebpageHref.add(library.getWebpage().get(j).getHref());
                } else {
                    libWebpageHref.add("");
                }
            }
        } else {
            libNumberOfWebpage.add("");
            libWebpageHref.add("");
            libWebpageTitle.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesLibraryWebpage\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Library in Monographic publications and Serial publications of
     * institution
     */
    private void loadAccessAndServicesLibraryMonographicSerial() {
        this.log.debug("Method start: \"loadAccessAndServicesLibraryMonographicSerial\"");
        // Monographic publications.
        if (library.getMonographicpub() != null
                && library.getMonographicpub().getNum() != null
                && library.getMonographicpub().getNum().getContent() != null
                && !library.getMonographicpub().getNum().getContent().isEmpty()) {
            libMonographicPubNum.add(library.getMonographicpub().getNum().getContent());
        } else {
            libMonographicPubNum.add("");
        }

        // Serial publications.
        if (library.getSerialpub() != null
                && library.getSerialpub().getNum() != null
                && library.getSerialpub().getNum().getContent() != null
                && !library.getSerialpub().getNum().getContent().isEmpty()) {
            libSerialPubNum.add(library.getSerialpub().getNum().getContent());
        } else {
            libSerialPubNum.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesLibraryMonographicSerial\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Library in Question of institution
     */
    private void loadAccessAndServicesLibraryQuestion() {
        this.log.debug("Method start: \"loadAccessAndServicesLibraryQuestion\"");
        // Question.
        if (library.getQuestion() != null
                && !library.getQuestion().isEmpty()) {
            libQuestion.add(library.getQuestion());
        } else {
            libQuestion.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesLibraryQuestion\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Internet access of institution
     */
    private void loadAccessAndServicesInternetAccess() {
        this.log.debug("Method start: \"loadAccessAndServicesInternetAccess\"");
        // Internet access.
        List<String> iaQuestion = new ArrayList<String>();
        List<String> iaValue = new ArrayList<String>();
        List<String> iaLang = new ArrayList<String>();
        if (repository.getServices() != null
                && repository.getServices().getInternetAccess() != null) {
            if (repository.getServices().getInternetAccess().getQuestion() != null
                    && !repository.getServices().getInternetAccess().getQuestion().isEmpty()) {
                iaQuestion.add(repository.getServices().getInternetAccess().getQuestion());
            } else {
                iaQuestion.add(Eag2012.OPTION_NONE);
            }

            if (repository.getServices().getInternetAccess().getDescriptiveNote() != null
                    && !repository.getServices().getInternetAccess().getDescriptiveNote().getP().isEmpty()) {
                for (int j = 0; j < repository.getServices().getInternetAccess().getDescriptiveNote().getP().size(); j++) {
                    if (repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j) != null
                            && repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getContent() != null
                            && !repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
                        iaValue.add(repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getContent());
                    } else {
                        iaValue.add("");
                    }

                    if (repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j) != null
                            && repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getLang() != null
                            && !repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
                        iaLang.add(repository.getServices().getInternetAccess().getDescriptiveNote().getP().get(j).getLang());
                    } else {
                        iaLang.add("");
                    }
                }
            } else {
                iaValue.add("");
                iaLang.add("");
            }
        }
        this.addAsInternetAccessQuestion(iaQuestion);
        this.addAsInternetAccessDescription(iaValue);
        this.addAsInternetAccessDescriptionLang(iaLang);
        this.log.debug("End method: \"loadAccessAndServicesInternetAccess\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Restoration laboratory and Technical services of institution
     */
    private void loadAccessAndServicesRestorationlab() {
        this.log.debug("Method start: \"loadAccessAndServicesRestorationlab\"");
        // Technical services.
        // Restoration laboratory.
        tsRLQuestion = new ArrayList<String>();
        tsRLValue = new ArrayList<String>();
        tsRLValueLang = new ArrayList<String>();
        tsRLTelephone = new ArrayList<String>();
        tsRLNumberOfEmail = new ArrayList<String>();
        tsRLEmailHref = new ArrayList<String>();
        tsRLEmailTitle = new ArrayList<String>();
        tsRLNumberOfWebpage = new ArrayList<String>();
        tsRLWebpageHref = new ArrayList<String>();
        tsRLWebpageTitle = new ArrayList<String>();
        if (repository.getServices() != null
                && repository.getServices().getTechservices() != null
                && repository.getServices().getTechservices().getRestorationlab() != null) {
            // Question.
            if (repository.getServices().getTechservices().getRestorationlab().getQuestion() != null
                    && !repository.getServices().getTechservices().getRestorationlab().getQuestion().isEmpty()) {
                tsRLQuestion.add(repository.getServices().getTechservices().getRestorationlab().getQuestion());
            } else {
                tsRLQuestion.add(Eag2012.OPTION_NONE);
            }

            //		// Description.
            loadAccessAndServicesRestorationlabDescription();
            // Contact.
            loadAccessAndServicesRestorationlabContact();
            // Webpage
            loadAccessAndServicesRestorationlabWebpage();
        }
        this.addAsRestorationlabQuestion(tsRLQuestion);
        this.addAsRestorationlabDescription(tsRLValue);
        this.addAsRestorationlabDescriptionLang(tsRLValueLang);
        this.addAsRestorationlabTelephone(tsRLTelephone);
        this.addAsRestorationlabNumberOfEmail(tsRLNumberOfEmail);
        this.addAsRestorationlabEmailHref(tsRLEmailHref);
        this.addAsRestorationlabEmailTitle(tsRLEmailTitle);
        this.addAsRestorationlabNumberOfWebpage(tsRLNumberOfWebpage);
        this.addAsRestorationlabWebpageHref(tsRLWebpageHref);
        this.addAsRestorationlabWebpageTitle(tsRLWebpageTitle);
        this.log.debug("End method: \"loadAccessAndServicesRestorationlab\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Restoration laboratory in Description of institution
     */
    private void loadAccessAndServicesRestorationlabDescription() {
        this.log.debug("Method start: \"loadAccessAndServicesRestorationlabDescription\"");
        // Description.
        if (repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote() != null
                && !repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().isEmpty()) {
            for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().size(); j++) {
                if (repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j) != null
                        && repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getContent() != null
                        && !repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
                    tsRLValue.add(repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getContent());

                    if (repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getLang() != null
                            && !repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
                        tsRLValueLang.add(repository.getServices().getTechservices().getRestorationlab().getDescriptiveNote().getP().get(j).getLang());
                    } else {
                        tsRLValueLang.add(Eag2012.OPTION_NONE);
                    }
                } else {
                    tsRLValue.add("");
                    tsRLValueLang.add(Eag2012.OPTION_NONE);
                }
            }
        } else {
            tsRLValue.add("");
            tsRLValueLang.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesRestorationlabDescription\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Restoration laboratory in Contact of institution
     */
    private void loadAccessAndServicesRestorationlabContact() {
        this.log.debug("Method start: \"loadAccessAndServicesRestorationlabContact\"");
        // Contact.
        if (repository.getServices().getTechservices().getRestorationlab().getContact() != null) {
            if (!repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().isEmpty()) {
                for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().size(); j++) {
                    if (repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().get(j) != null
                            && repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().get(j).getContent() != null
                            && !repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().get(j).getContent().isEmpty()) {
                        tsRLTelephone.add(repository.getServices().getTechservices().getRestorationlab().getContact().getTelephone().get(j).getContent());
                    } else {
                        tsRLTelephone.add("");
                    }
                }
            } else {
                tsRLTelephone.add("");
            }

            if (!repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().isEmpty()) {
                for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().size(); j++) {
                    tsRLNumberOfEmail.add("");
                    if (repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j) != null
                            && repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getContent() != null
                            && !repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getContent().isEmpty()) {
                        tsRLEmailTitle.add(repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getContent());
                    } else {
                        tsRLEmailTitle.add("");
                    }

                    if (repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j) != null
                            && repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getHref() != null
                            && !repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getHref().isEmpty()) {
                        tsRLEmailHref.add(repository.getServices().getTechservices().getRestorationlab().getContact().getEmail().get(j).getHref());
                    } else {
                        tsRLEmailHref.add("");
                    }
                }
            } else {
                tsRLNumberOfEmail.add("");
                tsRLEmailHref.add("");
                tsRLEmailTitle.add("");
            }
        } else {
            tsRLNumberOfEmail.add("");
            tsRLEmailHref.add("");
            tsRLEmailTitle.add("");
            tsRLTelephone.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesRestorationlabContact\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Restoration laboratory in Webpage of institution
     */
    private void loadAccessAndServicesRestorationlabWebpage() {
        this.log.debug("Method start: \"loadAccessAndServicesRestorationlabWebpage\"");
        // Webpage
        if (!repository.getServices().getTechservices().getRestorationlab().getWebpage().isEmpty()) {
            for (int j = 0; j < repository.getServices().getTechservices().getRestorationlab().getWebpage().size(); j++) {
                tsRLNumberOfWebpage.add("");
                if (repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j) != null
                        && repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getContent() != null
                        && !repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getContent().isEmpty()) {
                    tsRLWebpageTitle.add(repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getContent());
                } else {
                    tsRLWebpageTitle.add("");
                }

                if (repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j) != null
                        && repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getHref() != null
                        && !repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getHref().isEmpty()) {
                    tsRLWebpageHref.add(repository.getServices().getTechservices().getRestorationlab().getWebpage().get(j).getHref());
                } else {
                    tsRLWebpageHref.add("");
                }
            }
        } else {
            tsRLNumberOfWebpage.add("");
            tsRLWebpageHref.add("");
            tsRLWebpageTitle.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesRestorationlabWebpage\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Reproduction services of institution
     */
    private void loadAccessAndServicesReproductionServices() {
        this.log.debug("Method start: \"loadAccessAndServicesReproductionServices\"");
        // Reproduction services.
        tsRSQuestion = new ArrayList<String>();
        tsRSValue = new ArrayList<String>();
        tsRSValueLang = new ArrayList<String>();
        tsRSTelephone = new ArrayList<String>();
        tsRSNumberOfEmail = new ArrayList<String>();
        tsRSEmailHref = new ArrayList<String>();
        tsRSEmailTitle = new ArrayList<String>();
        tsRSNumberOfWebpage = new ArrayList<String>();
        tsRSWebpageHref = new ArrayList<String>();
        tsRSWebpageTitle = new ArrayList<String>();
        tsRSMicroformser = new ArrayList<String>();
        tsRSPhotographser = new ArrayList<String>();
        tsRSDigitalser = new ArrayList<String>();
        tsRSPhotocopyser = new ArrayList<String>();
        if (repository.getServices() != null
                && repository.getServices().getTechservices() != null
                && repository.getServices().getTechservices().getReproductionser() != null) {
            // Question.
            if (repository.getServices().getTechservices().getReproductionser().getQuestion() != null
                    && !repository.getServices().getTechservices().getReproductionser().getQuestion().isEmpty()) {
                tsRSQuestion.add(repository.getServices().getTechservices().getReproductionser().getQuestion());
            } else {
                tsRSQuestion.add(Eag2012.OPTION_NONE);
            }

            //		// Description.
            loadAccessAndServicesReproductionServicesDescription();
            // Contact.
            loadAccessAndServicesReproductionServicesContact();
            // Webpage
            loadAccessAndServicesReproductionServicesWebpage();
            // Microformser.
            loadAccessAndServicesReproductionServicesMPDP();
        }
        this.addAsReproductionserQuestion(tsRSQuestion);
        this.addAsReproductionserDescription(tsRSValue);
        this.addAsReproductionserDescriptionLang(tsRSValueLang);
        this.addAsReproductionserTelephone(tsRSTelephone);
        this.addAsReproductionserNumberOfEmail(tsRSNumberOfEmail);
        this.addAsReproductionserEmailHref(tsRSEmailHref);
        this.addAsReproductionserEmailTitle(tsRSEmailTitle);
        this.addAsReproductionserNumberOfWebpage(tsRSNumberOfWebpage);
        this.addAsReproductionserWebpageHref(tsRSWebpageHref);
        this.addAsReproductionserWebpageTitle(tsRSWebpageTitle);
        this.addAsReproductionserMicrofilmServices(tsRSMicroformser);
        this.addAsReproductionserPhotographicServices(tsRSPhotographser);
        this.addAsReproductionserDigitisationServices(tsRSDigitalser);
        this.addAsReproductionserPhotocopyingServices(tsRSPhotocopyser);
        this.log.debug("End method: \"loadAccessAndServicesReproductionServices\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Reproduction services in Description of institution
     */
    private void loadAccessAndServicesReproductionServicesDescription() {
        this.log.debug("Method start: \"loadAccessAndServicesReproductionServicesDescription\"");
        // Description.
        if (repository.getServices().getTechservices().getReproductionser().getDescriptiveNote() != null
                && !repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().isEmpty()) {
            for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().size(); j++) {
                if (repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j) != null
                        && repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getContent() != null
                        && !repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
                    tsRSValue.add(repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getContent());

                    if (repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getLang() != null
                            && !repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
                        tsRSValueLang.add(repository.getServices().getTechservices().getReproductionser().getDescriptiveNote().getP().get(j).getLang());
                    } else {
                        tsRSValueLang.add(Eag2012.OPTION_NONE);
                    }
                } else {
                    tsRSValue.add("");
                    tsRSValueLang.add(Eag2012.OPTION_NONE);
                }
            }
        } else {
            tsRSValue.add("");
            tsRSValueLang.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesReproductionServicesDescription\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Reproduction services in Contact of institution
     */
    private void loadAccessAndServicesReproductionServicesContact() {
        this.log.debug("Method start: \"loadAccessAndServicesReproductionServicesContact\"");
        // Contact.
        if (repository.getServices().getTechservices().getReproductionser().getContact() != null) {
            if (!repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().isEmpty()) {
                for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().size(); j++) {
                    if (repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().get(j) != null
                            && repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().get(j).getContent() != null
                            && !repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().get(j).getContent().isEmpty()) {
                        tsRSTelephone.add(repository.getServices().getTechservices().getReproductionser().getContact().getTelephone().get(j).getContent());
                    } else {
                        tsRSTelephone.add("");
                    }
                }
            } else {
                tsRSTelephone.add("");
            }

            if (!repository.getServices().getTechservices().getReproductionser().getContact().getEmail().isEmpty()) {
                for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getContact().getEmail().size(); j++) {
                    tsRSNumberOfEmail.add("");
                    if (repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j) != null
                            && repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getContent() != null
                            && !repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getContent().isEmpty()) {
                        tsRSEmailTitle.add(repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getContent());
                    } else {
                        tsRSEmailTitle.add("");
                    }

                    if (repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j) != null
                            && repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getHref() != null
                            && !repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getHref().isEmpty()) {
                        tsRSEmailHref.add(repository.getServices().getTechservices().getReproductionser().getContact().getEmail().get(j).getHref());
                    } else {
                        tsRSEmailHref.add("");
                    }
                }
            } else {
                tsRSNumberOfEmail.add("");
                tsRSEmailHref.add("");
                tsRSEmailTitle.add("");
            }
        } else {
            tsRSNumberOfEmail.add("");
            tsRSEmailHref.add("");
            tsRSEmailTitle.add("");
            tsRSTelephone.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesReproductionServicesContact\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Reproduction services in Webpage of institution
     */
    private void loadAccessAndServicesReproductionServicesWebpage() {
        this.log.debug("Method start: \"loadAccessAndServicesReproductionServicesWebpage\"");
        // Webpage
        if (!repository.getServices().getTechservices().getReproductionser().getWebpage().isEmpty()) {
            for (int j = 0; j < repository.getServices().getTechservices().getReproductionser().getWebpage().size(); j++) {
                tsRSNumberOfWebpage.add("");
                if (repository.getServices().getTechservices().getReproductionser().getWebpage().get(j) != null
                        && repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getContent() != null
                        && !repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getContent().isEmpty()) {
                    tsRSWebpageTitle.add(repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getContent());
                } else {
                    tsRSWebpageTitle.add("");
                }

                if (repository.getServices().getTechservices().getReproductionser().getWebpage().get(j) != null
                        && repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getHref() != null
                        && !repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getHref().isEmpty()) {
                    tsRSWebpageHref.add(repository.getServices().getTechservices().getReproductionser().getWebpage().get(j).getHref());
                } else {
                    tsRSWebpageHref.add("");
                }
            }
        } else {
            tsRSNumberOfWebpage.add("");
            tsRSWebpageHref.add("");
            tsRSWebpageTitle.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesReproductionServicesWebpage\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Reproduction services in Microformser, Photographser, Digitalser and
     * Photocopyser of institution
     */
    private void loadAccessAndServicesReproductionServicesMPDP() {
        this.log.debug("Method start: \"loadAccessAndServicesReproductionServicesMPDP\"");
        // Microformser.
        if (repository.getServices().getTechservices().getReproductionser().getMicroformser() != null
                && repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion() != null
                && !repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion().isEmpty()) {
            tsRSMicroformser.add(repository.getServices().getTechservices().getReproductionser().getMicroformser().getQuestion());
        } else {
            tsRSMicroformser.add(Eag2012.OPTION_NONE);
        }

        // Photographser.
        if (repository.getServices().getTechservices().getReproductionser().getPhotographser() != null
                && repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion() != null
                && !repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion().isEmpty()) {
            tsRSPhotographser.add(repository.getServices().getTechservices().getReproductionser().getPhotographser().getQuestion());
        } else {
            tsRSPhotographser.add(Eag2012.OPTION_NONE);
        }

        // Digitalser.
        if (repository.getServices().getTechservices().getReproductionser().getDigitalser() != null
                && repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion() != null
                && !repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion().isEmpty()) {
            tsRSDigitalser.add(repository.getServices().getTechservices().getReproductionser().getDigitalser().getQuestion());
        } else {
            tsRSDigitalser.add(Eag2012.OPTION_NONE);
        }

        // Photocopyser.
        if (repository.getServices().getTechservices().getReproductionser().getPhotocopyser() != null
                && repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion() != null
                && !repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion().isEmpty()) {
            tsRSPhotocopyser.add(repository.getServices().getTechservices().getReproductionser().getPhotocopyser().getQuestion());
        } else {
            tsRSPhotocopyser.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesReproductionServicesMPDP\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Recreational services of institution
     */
    private void loadAccessAndServicesRecreationalServices() {
        this.log.debug("Method start: \"loadAccessAndServicesRecreationalServices\"");
        // Recreational services
        rsRefreshment = new ArrayList<String>();
        rsRefreshmentLang = new ArrayList<String>();
        rsNumberOfExhibitions = new ArrayList<String>();
        rsExhibition = new ArrayList<String>();
        rsExhibitionLang = new ArrayList<String>();
        rsExhibitionWebpage = new ArrayList<String>();
        rsExhibitionWebpageTitle = new ArrayList<String>();
        rsNumberOfToursSessions = new ArrayList<String>();
        rsToursSessions = new ArrayList<String>();
        rsToursSessionsLang = new ArrayList<String>();
        rsToursSessionsWebpage = new ArrayList<String>();
        rsToursSessionsWebpageTitle = new ArrayList<String>();
        rsNumberOfOtherServices = new ArrayList<String>();
        rsOtherServices = new ArrayList<String>();
        rsOtherServicesLang = new ArrayList<String>();
        rsOtherServicesWebpage = new ArrayList<String>();
        rsOtherServicesWebpageTitle = new ArrayList<String>();
        if (repository.getServices() != null
                && repository.getServices().getRecreationalServices() != null) {
            //		// Refresment area.		
            loadAccessAndServicesRecreationalServicesRefresmentArea();
            // Exhibitions.
            loadAccessAndServicesRecreationalServicesRefExhibitions();
            // Tours and Sessions.
            loadAccessAndServicesRecreationalServicesToursAndSessions();
            // Other services.
            loadAccessAndServicesRecreationalServicesOtherServices();
        }
        this.addAsRecreationalServicesRefreshmentArea(rsRefreshment);
        this.addAsRecreationalServicesRefreshmentAreaLang(rsRefreshmentLang);
        this.addAsRSNumberOfExhibition(rsNumberOfExhibitions);
        this.addAsRSExhibition(rsExhibition);
        this.addAsRSExhibitionLang(rsExhibitionLang);
        this.addAsRSExhibitionWebpageHref(rsExhibitionWebpage);
        this.addAsRSExhibitionWebpageTitle(rsExhibitionWebpageTitle);
        this.addAsRSNumberOfToursSessions(rsNumberOfToursSessions);
        this.addAsRSToursSessions(rsToursSessions);
        this.addAsRSToursSessionsLang(rsToursSessionsLang);
        this.addAsRSToursSessionsWebpageHref(rsToursSessionsWebpage);
        this.addAsRSToursSessionsWebpageTitle(rsToursSessionsWebpageTitle);
        this.addAsRSNumberOfOtherServices(rsNumberOfOtherServices);
        this.addAsRSOtherServices(rsOtherServices);
        this.addAsRSOtherServicesLang(rsOtherServicesLang);
        this.addAsRSOtherServicesWebpageHref(rsOtherServicesWebpage);
        this.addAsRSOtherServicesWebpageTitle(rsOtherServicesWebpageTitle);
        this.log.debug("End method: \"loadAccessAndServicesRecreationalServices\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Recreational services in Refresment area of institution
     */
    private void loadAccessAndServicesRecreationalServicesRefresmentArea() {
        this.log.debug("Method start: \"loadAccessAndServicesRecreationalServicesRefresmentArea\"");
        // Refresment area.
        if (repository.getServices().getRecreationalServices().getRefreshment() != null
                && repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote() != null
                && !repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().isEmpty()) {
            for (int j = 0; j < repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().size(); j++) {
                if (repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j) != null
                        && repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getContent() != null
                        && !repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getContent().isEmpty()) {
                    rsRefreshment.add(repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getContent());
                } else {
                    rsRefreshment.add("");
                }

                if (repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j) != null
                        && repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getLang() != null
                        && !repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getLang().isEmpty()) {
                    rsRefreshmentLang.add(repository.getServices().getRecreationalServices().getRefreshment().getDescriptiveNote().getP().get(j).getLang());
                } else {
                    rsRefreshmentLang.add(Eag2012.OPTION_NONE);
                }
            }
        } else {
            rsRefreshment.add("");
            rsRefreshmentLang.add(Eag2012.OPTION_NONE);
        }
        this.log.debug("End method: \"loadAccessAndServicesRecreationalServicesRefresmentArea\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Recreational services in Exhibitions of institution
     */
    private void loadAccessAndServicesRecreationalServicesRefExhibitions() {
        this.log.debug("Method start: \"loadAccessAndServicesRecreationalServicesRefExhibitions\"");
        // Exhibitions.
        if (!repository.getServices().getRecreationalServices().getExhibition().isEmpty()) {
            for (int j = 0; j < repository.getServices().getRecreationalServices().getExhibition().size(); j++) {
                rsNumberOfExhibitions.add("");
                if (repository.getServices().getRecreationalServices().getExhibition().get(j) != null) {
                    // Description.
                    if (repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote() != null
                            && repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP() != null
                            && !repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().isEmpty()) {
                        for (int k = 0; k < repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().size(); k++) {
                            if (repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k) != null
                                    && repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getContent() != null
                                    && !repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getContent().isEmpty()) {
                                rsExhibition.add(repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getContent());
                            } else {
                                rsExhibition.add("");
                            }

                            if (repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k) != null
                                    && repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getLang() != null
                                    && !repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getLang().isEmpty()) {
                                rsExhibitionLang.add(repository.getServices().getRecreationalServices().getExhibition().get(j).getDescriptiveNote().getP().get(k).getLang());
                            } else {
                                rsExhibitionLang.add(Eag2012.OPTION_NONE);
                            }
                        }
                    } else {
                        rsExhibition.add("");
                        rsExhibitionLang.add(Eag2012.OPTION_NONE);
                    }
                    // Webpage.
                    if (repository.getServices().getRecreationalServices().getExhibition().get(j) != null
                            && repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage() != null) {
                        if (repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getHref() != null
                                && !repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getHref().isEmpty()) {
                            rsExhibitionWebpage.add(repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getHref());
                        } else {
                            rsExhibitionWebpage.add("");
                        }

                        if (repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getContent() != null
                                && !repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getContent().isEmpty()) {
                            rsExhibitionWebpageTitle.add(repository.getServices().getRecreationalServices().getExhibition().get(j).getWebpage().getContent());
                        } else {
                            rsExhibitionWebpageTitle.add("");
                        }
                    } else {
                        rsExhibitionWebpage.add("");
                        rsExhibitionWebpageTitle.add("");
                    }
                } else {
                    rsExhibition.add("");
                    rsExhibitionLang.add(Eag2012.OPTION_NONE);
                    rsExhibitionWebpage.add("");
                    rsExhibitionWebpageTitle.add("");
                }
            }
        } else {
            rsNumberOfExhibitions.add("");
            rsExhibition.add("");
            rsExhibitionLang.add(Eag2012.OPTION_NONE);
            rsExhibitionWebpage.add("");
            rsExhibitionWebpageTitle.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesRecreationalServicesRefExhibitions\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Recreational services in Tours and Sessions of institution
     */
    private void loadAccessAndServicesRecreationalServicesToursAndSessions() {
        this.log.debug("Method start: \"loadAccessAndServicesRecreationalServicesToursAndSessions\"");
        // Tours and Sessions.
        if (!repository.getServices().getRecreationalServices().getToursSessions().isEmpty()) {
            for (int j = 0; j < repository.getServices().getRecreationalServices().getToursSessions().size(); j++) {
                rsNumberOfToursSessions.add("");
                if (repository.getServices().getRecreationalServices().getToursSessions().get(j) != null) {
                    // Description.
                    if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote() != null
                            && repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP() != null
                            && !repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().isEmpty()) {
                        for (int k = 0; k < repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().size(); k++) {
                            if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k) != null
                                    && repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getContent() != null
                                    && !repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getContent().isEmpty()) {
                                rsToursSessions.add(repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getContent());
                            } else {
                                rsToursSessions.add("");
                            }

                            if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k) != null
                                    && repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getLang() != null
                                    && !repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getLang().isEmpty()) {
                                rsToursSessionsLang.add(repository.getServices().getRecreationalServices().getToursSessions().get(j).getDescriptiveNote().getP().get(k).getLang());
                            } else {
                                rsToursSessionsLang.add(Eag2012.OPTION_NONE);
                            }
                        }
                    } else {
                        rsToursSessions.add("");
                        rsToursSessionsLang.add(Eag2012.OPTION_NONE);
                    }
                    // Webpage.
                    if (repository.getServices().getRecreationalServices().getToursSessions().get(j) != null
                            && repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage() != null) {
                        if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getHref() != null
                                && !repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getHref().isEmpty()) {
                            rsToursSessionsWebpage.add(repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getHref());
                        } else {
                            rsToursSessionsWebpage.add("");
                        }
                        if (repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getContent() != null
                                && !repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getContent().isEmpty()) {
                            rsToursSessionsWebpageTitle.add(repository.getServices().getRecreationalServices().getToursSessions().get(j).getWebpage().getContent());
                        } else {
                            rsToursSessionsWebpageTitle.add("");
                        }
                    } else {
                        rsToursSessionsWebpage.add("");
                        rsToursSessionsWebpageTitle.add("");
                    }
                } else {
                    rsToursSessions.add("");
                    rsToursSessionsLang.add(Eag2012.OPTION_NONE);
                    rsToursSessionsWebpage.add("");
                    rsToursSessionsWebpageTitle.add("");
                }
            }
        } else {
            rsNumberOfToursSessions.add("");
            rsToursSessions.add("");
            rsToursSessionsLang.add(Eag2012.OPTION_NONE);
            rsToursSessionsWebpage.add("");
            rsToursSessionsWebpageTitle.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesRecreationalServicesToursAndSessions\"");
    }

    /**
     * Method to load all values of "Access and Services" tab in the part of
     * Recreational services in Other services of institution
     */
    private void loadAccessAndServicesRecreationalServicesOtherServices() {
        this.log.debug("Method start: \"loadAccessAndServicesRecreationalServicesOtherServices\"");
        // Other services.
        if (!repository.getServices().getRecreationalServices().getOtherServices().isEmpty()) {
            for (int j = 0; j < repository.getServices().getRecreationalServices().getOtherServices().size(); j++) {
                rsNumberOfOtherServices.add("");
                if (repository.getServices().getRecreationalServices().getOtherServices().get(j) != null) {
                    // Description.
                    if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote() != null
                            && repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP() != null
                            && !repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().isEmpty()) {
                        for (int k = 0; k < repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().size(); k++) {
                            if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k) != null
                                    && repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getContent() != null
                                    && !repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getContent().isEmpty()) {
                                rsOtherServices.add(repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getContent());
                            } else {
                                rsOtherServices.add("");
                            }
                            if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k) != null
                                    && repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getLang() != null
                                    && !repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getLang().isEmpty()) {
                                rsOtherServicesLang.add(repository.getServices().getRecreationalServices().getOtherServices().get(j).getDescriptiveNote().getP().get(k).getLang());
                            } else {
                                rsOtherServicesLang.add(Eag2012.OPTION_NONE);
                            }
                        }
                    } else {
                        rsOtherServices.add("");
                        rsOtherServicesLang.add(Eag2012.OPTION_NONE);
                    }
                    // Webpage.
                    if (repository.getServices().getRecreationalServices().getOtherServices().get(j) != null
                            && repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage() != null) {
                        if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getHref() != null
                                && !repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getHref().isEmpty()) {
                            rsOtherServicesWebpage.add(repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getHref());
                        } else {
                            rsOtherServicesWebpage.add("");
                        }
                        if (repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getContent() != null
                                && !repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getContent().isEmpty()) {
                            rsOtherServicesWebpageTitle.add(repository.getServices().getRecreationalServices().getOtherServices().get(j).getWebpage().getContent());
                        } else {
                            rsOtherServicesWebpageTitle.add("");
                        }
                    } else {
                        rsOtherServicesWebpage.add("");
                        rsOtherServicesWebpageTitle.add("");
                    }
                } else {
                    rsOtherServices.add("");
                    rsOtherServicesLang.add(Eag2012.OPTION_NONE);
                    rsOtherServicesWebpage.add("");
                    rsOtherServicesWebpageTitle.add("");
                }
            }
        } else {
            rsNumberOfOtherServices.add("");
            rsOtherServices.add("");
            rsOtherServicesLang.add(Eag2012.OPTION_NONE);
            rsOtherServicesWebpage.add("");
            rsOtherServicesWebpageTitle.add("");
        }
        this.log.debug("End method: \"loadAccessAndServicesRecreationalServicesOtherServices\"");
    }
}
