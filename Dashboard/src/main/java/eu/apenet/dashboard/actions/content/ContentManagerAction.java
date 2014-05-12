package eu.apenet.dashboard.actions.content;

import eu.apenet.dashboard.actions.content.ead.EadContentManagerResults;
import java.util.LinkedHashMap;
import java.util.Map;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.actions.content.eaccpf.EacCpfContentManagerResults;
import eu.apenet.dashboard.listener.HarvesterDaemon;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.ValidatedState;

public class ContentManagerAction extends AbstractInstitutionAction {

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    public static final String EAD_SEARCH_OPTIONS = "eadSearchOptions";
    protected static final String CONTENT_MESSAGE_NO = "content.message.no";
    protected static final String CONTENT_MESSAGE_YES = "content.message.yes";
    protected static final String CONTENT_MESSAGE_QUEUE = "content.message.queue";

    protected static final String CONTENT_MESSAGE_ERROR = "content.message.fatalerror";
    protected static final String SUCCESS_AJAX = "success_ajax";
    /**
     *
     */
    private static final long serialVersionUID = 4513310293148562803L;
    private Map<String, String> typeList = new LinkedHashMap<String, String>();
    private String xmlTypeId = XmlType.EAD_FA.getIdentifier() + "";
    private Map<String, String> convertedStatusList = new LinkedHashMap<String, String>();
    private String[] convertedStatus = new String[]{FALSE, TRUE};
    private Map<String, String> validatedStatusList = new LinkedHashMap<String, String>();
    private String[] validatedStatus = new String[]{ValidatedState.NOT_VALIDATED.toString(),
        ValidatedState.VALIDATED.toString(), ValidatedState.FATAL_ERROR.toString()};
    private String[] linkedStatus = new String[]{FALSE, TRUE};
    private Map<String, String> linkedStatusList = new LinkedHashMap<String, String>();
    private Map<String, String> publishedStatusList = new LinkedHashMap<String, String>();
    private String[] publishedStatus = new String[]{FALSE, TRUE};
    private Map<String, String> europeanaStatusList = new LinkedHashMap<String, String>();
    private String[] europeanaStatus = new String[]{EuropeanaState.NOT_CONVERTED.toString(),
        EuropeanaState.CONVERTED.toString(), EuropeanaState.DELIVERED.toString(),
        EuropeanaState.NO_EUROPEANA_CANDIDATE.toString()};
    private Map<String, String> queuingStatusList = new LinkedHashMap<String, String>();
    private String[] queuingStatus = new String[]{QueuingState.NO.toString(),
        QueuingState.READY.toString(), QueuingState.BUSY.toString(),
        QueuingState.ERROR.toString()};
    private Map<String, String> searchTermsFieldList = new LinkedHashMap<String, String>();
    private Integer pageNumber = 1;
    private Integer resultPerPage = 100;
    private String orderByField = "id";
    private boolean orderByAscending = false;
    private String searchTerms;
    private String searchTermsField;
    private boolean updateSearchResults = false;
    private boolean ajax = false;
    private boolean errorLinkHgSg = false;

    public ContentManagerAction() {
    }

    public void prepareJspLists() {
        convertedStatusList.put(TRUE, getText(CONTENT_MESSAGE_YES));
        convertedStatusList.put(FALSE, getText(CONTENT_MESSAGE_NO));
        validatedStatusList.put(ValidatedState.VALIDATED.toString(), getText(CONTENT_MESSAGE_YES));
        validatedStatusList.put(ValidatedState.NOT_VALIDATED.toString(), getText(CONTENT_MESSAGE_NO));
        validatedStatusList.put(ValidatedState.FATAL_ERROR.toString(), getText(CONTENT_MESSAGE_ERROR));
        linkedStatusList.put(TRUE, getText(CONTENT_MESSAGE_YES));
        linkedStatusList.put(FALSE, getText(CONTENT_MESSAGE_NO));
        publishedStatusList.put(TRUE, getText(CONTENT_MESSAGE_YES));
        publishedStatusList.put(FALSE, getText(CONTENT_MESSAGE_NO));
        europeanaStatusList.put(EuropeanaState.CONVERTED.toString(), getText("content.message.eseedm"));
        europeanaStatusList.put(EuropeanaState.NOT_CONVERTED.toString(), getText(CONTENT_MESSAGE_NO));
        europeanaStatusList.put(EuropeanaState.DELIVERED.toString(), getText("content.message.europeana.delivered"));
        europeanaStatusList.put(EuropeanaState.NO_EUROPEANA_CANDIDATE.toString(), getText("content.message.europeana.nochos"));

        queuingStatusList.put(QueuingState.NO.toString(), getText(CONTENT_MESSAGE_NO));
        queuingStatusList.put(QueuingState.READY.toString(), getText("content.message.ready"));
        queuingStatusList.put(QueuingState.BUSY.toString(), getText("content.message.queueprocessing"));
        queuingStatusList.put(QueuingState.ERROR.toString(), getText(CONTENT_MESSAGE_ERROR));
        typeList.put(XmlType.EAD_FA.getIdentifier() + "",
                getText("content.message." + XmlType.EAD_FA.getResourceName()));
        typeList.put(XmlType.EAD_HG.getIdentifier() + "",
                getText("content.message." + XmlType.EAD_HG.getResourceName()));
        typeList.put(XmlType.EAD_SG.getIdentifier() + "",
                getText("content.message." + XmlType.EAD_SG.getResourceName()));
        typeList.put(XmlType.EAC_CPF.getIdentifier() + "",
                getText("content.message." + XmlType.EAC_CPF.getResourceName()));
        searchTermsFieldList.put("", getText("content.message.all"));
        searchTermsFieldList.put("eadid", getText("content.message.id"));
        searchTermsFieldList.put("title", getText("content.message.title"));
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getResultPerPage() {
        return resultPerPage;
    }

    public void setResultPerPage(Integer resultPerPage) {
        this.resultPerPage = resultPerPage;
    }

    public String getOrderByField() {
        return orderByField;
    }

    public void setOrderByField(String orderByField) {
        this.orderByField = orderByField;
    }

    public boolean isOrderByAscending() {
        return orderByAscending;
    }

    public void setOrderByAscending(boolean orderByAscending) {
        this.orderByAscending = orderByAscending;
    }

    @Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("breadcrumb.section.contentmanager"));
    }

    @Override
    public void prepare() throws Exception {
        super.prepare();
    }

    @Override
    public String input() throws Exception {
        prepareJspLists();
        ContentSearchOptions contentSearchOptions = initFromExistingContentSearchOptions();
        if (contentSearchOptions == null) {
            contentSearchOptions = createNewContentSearchOptions();
            getServletRequest().getSession().setAttribute(EAD_SEARCH_OPTIONS, contentSearchOptions);
        }

        if (xmlTypeId.equals(XmlType.EAC_CPF.getIdentifier() + "")) {
            return processEacCpf(contentSearchOptions);
        } else {
            return processEad(contentSearchOptions);
        }
    }

    @Override
    public String execute() throws Exception {
        ContentSearchOptions eadSearchOptions = null;
        if (updateSearchResults) {
            eadSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(EAD_SEARCH_OPTIONS);
            if (eadSearchOptions == null) {
                eadSearchOptions = createNewContentSearchOptions();
            }
            eadSearchOptions.setPageNumber(pageNumber);
            eadSearchOptions.setOrderByAscending(orderByAscending);
            eadSearchOptions.setOrderByField(orderByField);
            eadSearchOptions.setPageSize(resultPerPage);
        } else {
            eadSearchOptions = createNewContentSearchOptions();
        }
        getServletRequest().getSession().setAttribute(EAD_SEARCH_OPTIONS, eadSearchOptions);
        return input();
    }

    private ContentSearchOptions initFromExistingContentSearchOptions() {
        ContentSearchOptions contentSearchOptions = (ContentSearchOptions) getServletRequest().getSession().getAttribute(EAD_SEARCH_OPTIONS);
        if (contentSearchOptions != null) {
            if (contentSearchOptions.getConverted() != null) {
                convertedStatus = new String[]{contentSearchOptions.getConverted().toString()};
            }
            if (contentSearchOptions.getLinked() != null) {
                linkedStatus = new String[]{contentSearchOptions.getLinked().toString()};
            }
            if (contentSearchOptions.getValidated().size() > 0) {
                validatedStatus = new String[contentSearchOptions.getValidated().size()];
                for (int i = 0; i < contentSearchOptions.getValidated().size(); i++) {
                    validatedStatus[i] = contentSearchOptions.getValidated().get(i).toString();
                }
            }
            if (contentSearchOptions.getPublished() != null) {
                publishedStatus = new String[]{contentSearchOptions.getPublished().toString()};
            }
            if (contentSearchOptions.getEuropeana().size() > 0) {
                europeanaStatus = new String[contentSearchOptions.getEuropeana().size()];
                for (int i = 0; i < contentSearchOptions.getEuropeana().size(); i++) {
                    europeanaStatus[i] = contentSearchOptions.getEuropeana().get(i).toString();
                }
            }

            if (contentSearchOptions.getQueuing().size() > 0) {
                queuingStatus = new String[contentSearchOptions.getQueuing().size()];
                for (int i = 0; i < contentSearchOptions.getQueuing().size(); i++) {
                    queuingStatus[i] = contentSearchOptions.getQueuing().get(i).toString();
                }
            }
            xmlTypeId = XmlType.getType(contentSearchOptions.getContentClass()).getIdentifier() + "";
            searchTerms = contentSearchOptions.getSearchTerms();
            orderByField = contentSearchOptions.getOrderByField();
            orderByAscending = contentSearchOptions.isOrderByAscending();
            searchTermsField = contentSearchOptions.getSearchTermsField();
            pageNumber = contentSearchOptions.getPageNumber();
            resultPerPage = contentSearchOptions.getPageSize();
            contentSearchOptions.setArchivalInstitionId(getAiId());
        }

        return contentSearchOptions;
    }

    private ContentSearchOptions createNewContentSearchOptions() {
        ContentSearchOptions contentSearchOptions = new ContentSearchOptions();
        contentSearchOptions.setPageNumber(pageNumber);
        contentSearchOptions.setPageSize(resultPerPage);
        contentSearchOptions.setOrderByAscending(orderByAscending);
        contentSearchOptions.setOrderByField(orderByField);
        contentSearchOptions.setArchivalInstitionId(getAiId());
        if (convertedStatus != null && convertedStatus.length == 1) {
            contentSearchOptions.setConverted(Boolean.valueOf(convertedStatus[0]));
        }
        if (linkedStatus != null && linkedStatus.length == 1) {
            contentSearchOptions.setLinked(Boolean.valueOf(linkedStatus[0]));
        }
        if (validatedStatus != null && validatedStatus.length >= 1 && validatedStatus.length <= 2) {
            for (String validatedStatusItem : validatedStatus) {
                contentSearchOptions.getValidated().add(ValidatedState.getValidatedState(validatedStatusItem));
            }
        }
        if (publishedStatus != null && publishedStatus.length == 1) {
            contentSearchOptions.setPublished(Boolean.valueOf(publishedStatus[0]));
        }
        if (europeanaStatus != null && europeanaStatus.length >= 1 && europeanaStatus.length <= 3) {
            for (String europeanaStatusItem : europeanaStatus) {
                contentSearchOptions.getEuropeana().add(EuropeanaState.getEuropeanaState(europeanaStatusItem));
            }
        }
        if (queuingStatus != null && queuingStatus.length >= 1 && queuingStatus.length <= 3) {
            for (String queuingStatusItem : queuingStatus) {
                contentSearchOptions.getQueuing().add(QueuingState.getQueuingState(queuingStatusItem));
            }
        }
        contentSearchOptions.setSearchTerms(searchTerms);
        contentSearchOptions.setSearchTermsField(searchTermsField);
        contentSearchOptions.setContentClass(XmlType.getType(Integer.parseInt(xmlTypeId)).getClazz());
        return contentSearchOptions;
    }

        private String processEad(ContentSearchOptions contentSearchOptions) {
        EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        EadContentManagerResults results = new EadContentManagerResults(contentSearchOptions);
        results.setEads(eadDAO.getEads(contentSearchOptions));
        results.setTotalNumberOfResults(eadDAO.countEads(contentSearchOptions));
        /*
        * statistics for total converted files
        */
        if (contentSearchOptions.getConverted() == null || contentSearchOptions.getConverted() == true) {
            ContentSearchOptions convertedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            convertedSearchOptions.setConverted(true);
            results.setTotalConvertedFiles(eadDAO.countEads(convertedSearchOptions));
        }
        /*
        * statistics for total validated files
        */
        if (contentSearchOptions.getValidated().isEmpty()
                || contentSearchOptions.getValidated().contains(ValidatedState.VALIDATED)) {
            ContentSearchOptions validatedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            if (validatedSearchOptions.getValidated().isEmpty()) {
                validatedSearchOptions.setValidated(ValidatedState.VALIDATED);
            }
            results.setTotalValidatedFiles(eadDAO.countEads(validatedSearchOptions));
        }
        /*
        * statistics for total published units
        */
        if (contentSearchOptions.getPublished() == null || contentSearchOptions.getPublished() == true) {
            ContentSearchOptions publishedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            publishedSearchOptions.setPublished(true);
            results.setTotalPublishedUnits(eadDAO.countUnits(publishedSearchOptions));
        }
        if (contentSearchOptions.getContentClass().equals(FindingAid.class)) {
            /*
            * statistics for total delivered daos
            */
            if (contentSearchOptions.getEuropeana().isEmpty()
                    || contentSearchOptions.getEuropeana().contains(EuropeanaState.DELIVERED)) {
                ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
                europeanaSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
                results.setTotalChosDeliveredToEuropeana(eadDAO.countChos(europeanaSearchOptions));
            }
            /*
            * statistics for total converted daos
            */
            if (contentSearchOptions.getEuropeana().isEmpty()
                    || contentSearchOptions.getEuropeana().contains(EuropeanaState.CONVERTED)) {
                ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
                europeanaSearchOptions.getEuropeana().clear();
                europeanaSearchOptions.getEuropeana().add(EuropeanaState.CONVERTED);
                europeanaSearchOptions.getEuropeana().add(EuropeanaState.DELIVERED);
                results.setTotalChos(eadDAO.countChos(europeanaSearchOptions));
            }

        }

        ContentSearchOptions dynamicEadSearchOptions = new ContentSearchOptions();
        dynamicEadSearchOptions.setPublished(false);
        dynamicEadSearchOptions.setDynamic(true);
        dynamicEadSearchOptions.setContentClass(HoldingsGuide.class);
        results.setHasDynamicHg(eadDAO.existEads(dynamicEadSearchOptions));
        dynamicEadSearchOptions.setContentClass(SourceGuide.class);
        results.setHasDynamicSg(eadDAO.existEads(dynamicEadSearchOptions));
        getServletRequest().setAttribute("results", results);
        getServletRequest().setAttribute("harvestingStarted", EadService.isHarvestingStarted());
        return SUCCESS;
    }

    private String processEacCpf(ContentSearchOptions contentSearchOptions) {
        EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
        EacCpfContentManagerResults results = new EacCpfContentManagerResults(contentSearchOptions);
        results.setEacCpfs(eacCpfDAO.getEacCpfs(contentSearchOptions));
        results.setTotalNumberOfResults(eacCpfDAO.countEacCpfs(contentSearchOptions));
        /*
        * statistics for number of converted files
        */
        if (contentSearchOptions.getConverted() == null || contentSearchOptions.getConverted() == true) {
            ContentSearchOptions convertedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            convertedSearchOptions.setConverted(true);
            results.setTotalConvertedFiles(eacCpfDAO.countEacCpfs(convertedSearchOptions));
        }
        /*
        * statistics for number of validated files
        */
        if (contentSearchOptions.getValidated().isEmpty()
                || contentSearchOptions.getValidated().contains(ValidatedState.VALIDATED)) {
            ContentSearchOptions validatedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            if (validatedSearchOptions.getValidated().isEmpty()) {
                validatedSearchOptions.setValidated(ValidatedState.VALIDATED);
            }
            results.setTotalValidatedFiles(eacCpfDAO.countEacCpfs(validatedSearchOptions));
        }
        /*
        * statistics for number of published files
        */
        if (contentSearchOptions.getPublished() == null || contentSearchOptions.getPublished() == true) {
            ContentSearchOptions publishedSearchOptions = new ContentSearchOptions(contentSearchOptions);
            publishedSearchOptions.setPublished(true);
            results.setTotalPublishedUnits(eacCpfDAO.countEacCpfs(publishedSearchOptions));
        }
        /*
        * statistics for number of files delivered to Europeana
        */
        if (contentSearchOptions.getEuropeana().isEmpty()
                || contentSearchOptions.getEuropeana().contains(EuropeanaState.DELIVERED)) {
            ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
            europeanaSearchOptions.setEuropeana(EuropeanaState.DELIVERED);
            results.setTotalChosDeliveredToEuropeana(eacCpfDAO.countEacCpfs(europeanaSearchOptions));
        }
        /*
        * statistics for number of files converted to EDM
        */
        if (contentSearchOptions.getEuropeana().isEmpty()
                || contentSearchOptions.getEuropeana().contains(EuropeanaState.CONVERTED)) {
            ContentSearchOptions europeanaSearchOptions = new ContentSearchOptions(contentSearchOptions);
            europeanaSearchOptions.getEuropeana().clear();
            europeanaSearchOptions.getEuropeana().add(EuropeanaState.CONVERTED);
            europeanaSearchOptions.getEuropeana().add(EuropeanaState.DELIVERED);
            results.setTotalChos(eacCpfDAO.countEacCpfs(europeanaSearchOptions));
        }
        getServletRequest().setAttribute("results", results);
//            getServletRequest().setAttribute("harvestingStarted", HarvesterDaemon.isHarvesterProcessing() || EadService.isHarvestingStarted());
        return "success-eaccpf";
    }

    public Map<String, String> getConvertedStatusList() {
        return convertedStatusList;
    }

    public void setConvertedStatusList(Map<String, String> convertedStatusList) {
        this.convertedStatusList = convertedStatusList;
    }

    public String[] getConvertedStatus() {
        return convertedStatus;
    }

    public void setConvertedStatus(String[] convertedStatus) {
        this.convertedStatus = convertedStatus;
    }

    public Map<String, String> getValidatedStatusList() {
        return validatedStatusList;
    }

    public void setValidatedStatusList(Map<String, String> validatedStatusList) {
        this.validatedStatusList = validatedStatusList;
    }

    public String[] getValidatedStatus() {
        return validatedStatus;
    }

    public void setValidatedStatus(String[] validatedStatus) {
        this.validatedStatus = validatedStatus;
    }

    public String getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    public Map<String, String> getPublishedStatusList() {
        return publishedStatusList;
    }

    public void setPublishedStatusList(Map<String, String> publishedStatusList) {
        this.publishedStatusList = publishedStatusList;
    }

    public String[] getPublishedStatus() {
        return publishedStatus;
    }

    public void setPublishedStatus(String[] publishedStatus) {
        this.publishedStatus = publishedStatus;
    }

    public Map<String, String> getEuropeanaStatusList() {
        return europeanaStatusList;
    }

    public void setEuropeanaStatusList(Map<String, String> europeanaStatusList) {
        this.europeanaStatusList = europeanaStatusList;
    }

    public String[] getEuropeanaStatus() {
        return europeanaStatus;
    }

    public void setEuropeanaStatus(String[] europeanaStatus) {
        this.europeanaStatus = europeanaStatus;
    }

    public Map<String, String> getTypeList() {
        return typeList;
    }

    public void setTypeList(Map<String, String> typeList) {
        this.typeList = typeList;
    }

    public String getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(String xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public Map<String, String> getSearchTermsFieldList() {
        return searchTermsFieldList;
    }

    public void setSearchTermsFieldList(Map<String, String> searchTermsFieldList) {
        this.searchTermsFieldList = searchTermsFieldList;
    }

    public String getSearchTermsField() {
        return searchTermsField;
    }

    public void setSearchTermsField(String searchTermsField) {
        this.searchTermsField = searchTermsField;
    }

    public Map<String, String> getQueuingStatusList() {
        return queuingStatusList;
    }

    public void setQueuingStatusList(Map<String, String> queuingStatusList) {
        this.queuingStatusList = queuingStatusList;
    }

    public String[] getQueuingStatus() {
        return queuingStatus;
    }

    public void setQueuingStatus(String[] queuingStatus) {
        this.queuingStatus = queuingStatus;
    }

    public String[] getLinkedStatus() {
        return linkedStatus;
    }

    public void setLinkedStatus(String[] linkedStatus) {
        this.linkedStatus = linkedStatus;
    }

    public Map<String, String> getLinkedStatusList() {
        return linkedStatusList;
    }

    public void setLinkedStatusList(Map<String, String> linkedStatusList) {
        this.linkedStatusList = linkedStatusList;
    }

    public boolean isUpdateSearchResults() {
        return updateSearchResults;
    }

    public void setUpdateSearchResults(boolean updateSearchResults) {
        this.updateSearchResults = updateSearchResults;
    }

    public boolean isErrorLinkHgSg() {
        return this.errorLinkHgSg;
    }

    public void setErrorLinkHgSg(boolean errorLinkHgSg) {
        this.errorLinkHgSg = errorLinkHgSg;
    }

}
