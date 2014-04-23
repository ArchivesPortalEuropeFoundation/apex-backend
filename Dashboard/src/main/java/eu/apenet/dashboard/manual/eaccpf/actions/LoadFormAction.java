/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.actions;

import eu.apenet.dashboard.manual.eaccpf.EacCpfLoader;
import eu.apenet.dashboard.manual.eaccpf.util.DateType;
import eu.apenet.dashboard.manual.eaccpf.util.IdentifierType;
import eu.apenet.dashboard.manual.eaccpf.util.MapEntry;
import eu.apenet.dashboard.manual.eaccpf.util.NameEntryType;
import eu.apenet.dashboard.manual.eaccpf.util.RelationType;
import eu.apenet.persistence.factory.DAOFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author papp
 */
public class LoadFormAction extends EacCpfAction {

    private Map<String, String> dateOrDateRange = new TreeMap<String, String>();
    private Set<MapEntry> countryList = new TreeSet<MapEntry>();
    private Set<MapEntry> formNameList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> componentNameList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> placeEntryList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> addressComponentTypeList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> cpfRelationTypeList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> resRelationTypeList = new LinkedHashSet<MapEntry>();
    private Set<MapEntry> fncRelationTypeList = new LinkedHashSet<MapEntry>();

    private String cpfType;
    private String cpfTypeDescriptionText;
    private String cpfTypeIdentifierText;
    private File upload;
    private String content;
    private EacCpfLoader loader;
    private String useMode;
    private String defaultLanguage;
    private String defaultScript;

    private static final String OTHER = "other";

    @Override
    public String execute() throws Exception {
        this.loader = new EacCpfLoader();
        if (upload != null) {
            boolean result = this.loader.fillEacCpf(upload);
            cpfType = this.loader.getEntityType();
            useMode = "load";
        } else {
            cpfType = getServletRequest().getParameter("cpfType");
            this.loader.setRecordId("");
            this.loader.setControlResponsiblePerson(DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId()).getPartner().getName());
            this.loader.setAgencyCode(DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId()).getRepositorycode());
            useMode = "new";
        }
        if (cpfType.equals(EacCpfAction.PERSON)){
            cpfTypeDescriptionText = "Description of a person";
            cpfTypeIdentifierText = "Identifier of the person";
        } else if (cpfType.equals(EacCpfAction.CORPORATE_BODY)){
            cpfTypeDescriptionText = "Description of a corporate body";
            cpfTypeIdentifierText = "Identifier of the corporate body";
        } else if (cpfType.equals(EacCpfAction.FAMILY)){
            cpfTypeDescriptionText = "Description of a family";
            cpfTypeIdentifierText = "Identifier of the family";
        } else {
            cpfTypeDescriptionText = "";
            cpfTypeIdentifierText = "";
        }
        if (this.loader.getIdentifiers() == null) {
            this.loader.setIdentifiers(new ArrayList<IdentifierType>());
        }
        if (this.loader.getNameEntries() == null) {
            this.loader.setNameEntries(new ArrayList<NameEntryType>());
        }
        if (this.loader.getExistDates() == null) {
            this.loader.setExistDates(new ArrayList<DateType>());
        }
        if (this.loader.getCpfRelations() == null) {
            this.loader.setCpfRelations(new ArrayList<RelationType>());
        }
        if (this.loader.getResRelations() == null) {
            this.loader.setResRelations(new ArrayList<RelationType>());
        }
        if (this.loader.getOtherRecordIds() == null) {
            this.loader.setOtherRecordIds(new ArrayList<String>());
        }
        if (getServletRequest().getParameter("useMode") != null) {
            useMode = getServletRequest().getParameter("useMode");
        }
        if (getServletRequest().getParameter("defaultLanguage") != null) {
            defaultLanguage = getServletRequest().getParameter("defaultLanguage");
        }
        if (getServletRequest().getParameter("defaultScript") != null) {
            defaultScript = getServletRequest().getParameter("defaultScript");
        }

        StringBuilder builder = new StringBuilder();
        Enumeration<String> paramNames = getServletRequest().getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            builder.append(paramName);
            builder.append(": ");
            String[] paramValues = getServletRequest().getParameterValues(paramName);
            for (String string : paramValues) {
                builder.append(string);
                builder.append(", ");
            }
        }
        content = builder.toString();

        setUpLanguages();
        setUpScriptList();
        setUpDateOrDateRange();
        setUpCountryList();
        setUpFormOfNameList();
        setUpComponentNameList();
        setUpPlaceEntryList();
        setUpAddressComponentTypeList();
        setUpCpfRelationTypeList();
        setUpResRelationTypeList();
        setUpFncRelationTypeList();

        return SUCCESS;
    }

    protected void setUpDateOrDateRange() {
        dateOrDateRange.put("date", "a single date");
        dateOrDateRange.put("dateRange", "or a date range");
    }

    protected void setUpCountryList() {
        // Add empty map entry
        countryList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));
        // Add countries
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countryList.add(new MapEntry(obj.getCountry(), obj.getDisplayCountry(Locale.ENGLISH)));
        }
    }

    protected void setUpFormOfNameList() {
        formNameList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));
        formNameList.add(new MapEntry("authorized", "Authorized name"));
        formNameList.add(new MapEntry("alternative", "Alternative name"));
        formNameList.add(new MapEntry("preferred", "Preferred name"));
        formNameList.add(new MapEntry("abbreviation", "Abbreviated name"));
        formNameList.add(new MapEntry(OTHER, "Other"));
    }

    protected void setUpComponentNameList() {
        if (cpfType.equals(CORPORATE_BODY)) {
            componentNameList.add(new MapEntry("corpname", "Name of a corporate body"));
        }
        if (cpfType.equals(FAMILY)) {
            componentNameList.add(new MapEntry("famname", "Name of a family"));
        }
        if (cpfType.equals(PERSON)) {
            componentNameList.add(new MapEntry("persname", "Name of a person"));
        }
        if (cpfType.equals(PERSON) || cpfType.equals(FAMILY)) {
            componentNameList.add(new MapEntry("surname", "Last name"));
        }
        if (cpfType.equals(PERSON)) {
            componentNameList.add(new MapEntry("firstname", "First name"));
            componentNameList.add(new MapEntry("birthname", "Birth name"));
            componentNameList.add(new MapEntry("title", "Academic title"));
        }
        if (cpfType.equals(PERSON) || cpfType.equals(FAMILY)) {
            componentNameList.add(new MapEntry("prefix", "Aristocratic title"));
        }
        componentNameList.add(new MapEntry("suffix", "Name suffix"));
        componentNameList.add(new MapEntry("alias", "Alias"));
        if (cpfType.equals(PERSON)) {
            componentNameList.add(new MapEntry("patronymic", "Patronymic"));
        }
        if (cpfType.equals(CORPORATE_BODY)) {
            componentNameList.add(new MapEntry("legalform", "Legal form of a corporate body"));
        }
    }

    protected void setUpPlaceEntryList() {
        // Add empty list entry
        placeEntryList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        if (cpfType.equals(PERSON)) {
            placeEntryList.add(new MapEntry("birth", "Place of birth"));
        }
        if (cpfType.equals(CORPORATE_BODY)) {
            placeEntryList.add(new MapEntry("foundation", "Place of foundation"));
        }
        if (cpfType.equals(PERSON) || cpfType.equals(FAMILY)) {
            placeEntryList.add(new MapEntry("protected-residence", "Private residence"));
        }
        placeEntryList.add(new MapEntry("business-residence", "Business residence"));
        if (cpfType.equals(FAMILY)) {
            placeEntryList.add(new MapEntry("death", "Place of death"));
        }
        if (cpfType.equals(CORPORATE_BODY)) {
            placeEntryList.add(new MapEntry("suppression", "Place of suppression"));
        }
        placeEntryList.add(new MapEntry(OTHER, "Other"));
    }

    protected void setUpAddressComponentTypeList() {
        // Add empty list entry
        addressComponentTypeList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        addressComponentTypeList.add(new MapEntry("firstdem", "Community/Region"));
        addressComponentTypeList.add(new MapEntry("secondem", "County"));
        addressComponentTypeList.add(new MapEntry("postalcode", "City & Postal code"));
        addressComponentTypeList.add(new MapEntry("localentity", "District/Quarter"));
        addressComponentTypeList.add(new MapEntry("street", "Street"));
        addressComponentTypeList.add(new MapEntry(OTHER, "Other"));
    }

    protected void setUpCpfRelationTypeList() {
        // Add empty list entry
        cpfRelationTypeList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        cpfRelationTypeList.add(new MapEntry("identity", "Identical with entity"));
        cpfRelationTypeList.add(new MapEntry("hierarchical", "Hierarchical related"));
        cpfRelationTypeList.add(new MapEntry("hierarchical-parent", "Hierarchical parent related"));
        cpfRelationTypeList.add(new MapEntry("hierarchical-child", "Hierarchical child related"));
        cpfRelationTypeList.add(new MapEntry("temporal", "Temporal related"));
        cpfRelationTypeList.add(new MapEntry("temporal-earlier", "Temporal earlier related"));
        cpfRelationTypeList.add(new MapEntry("temporal-later", "Temporal later related"));
        cpfRelationTypeList.add(new MapEntry("family", "Family related"));
        cpfRelationTypeList.add(new MapEntry("associative", "Associated"));
    }

    protected void setUpResRelationTypeList() {
        // Add empty list entry
        resRelationTypeList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        resRelationTypeList.add(new MapEntry("creatorOf", "Creator of the resource"));
        resRelationTypeList.add(new MapEntry("subjectOf", "Subject of the resource"));
        resRelationTypeList.add(new MapEntry(OTHER, "Other"));
    }

    protected void setUpFncRelationTypeList() {
        // Add empty list entry
        fncRelationTypeList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));

        fncRelationTypeList.add(new MapEntry("controls", "Controls the function"));
        fncRelationTypeList.add(new MapEntry("owns", "Owns the function"));
        fncRelationTypeList.add(new MapEntry("performs", "Performs the function"));
    }

    public Map<String, String> getDateOrDateRange() {
        return dateOrDateRange;
    }

    public void setDateOrDateRange(Map<String, String> dateOrDateRange) {
        this.dateOrDateRange = dateOrDateRange;
    }

    public Set<MapEntry> getCountryList() {
        return countryList;
    }

    public void setCountryList(Set<MapEntry> countryList) {
        this.countryList = countryList;
    }

    public Set<MapEntry> getFormOfNameList() {
        return formNameList;
    }

    public void setFormOfNameList(Set<MapEntry> formOfNameList) {
        this.formNameList = formOfNameList;
    }

    public Set<MapEntry> getComponentOfNameList() {
        return componentNameList;
    }

    public void setComponentOfNameList(Set<MapEntry> componentOfNameList) {
        this.componentNameList = componentOfNameList;
    }

    public Set<MapEntry> getFormNameList() {
        return formNameList;
    }

    public void setFormNameList(Set<MapEntry> formNameList) {
        this.formNameList = formNameList;
    }

    public Set<MapEntry> getComponentNameList() {
        return componentNameList;
    }

    public void setComponentNameList(Set<MapEntry> componentNameList) {
        this.componentNameList = componentNameList;
    }

    public Set<MapEntry> getPlaceEntryList() {
        return placeEntryList;
    }

    public void setPlaceEntryList(Set<MapEntry> placeEntryList) {
        this.placeEntryList = placeEntryList;
    }

    public Set<MapEntry> getAddressComponentTypeList() {
        return addressComponentTypeList;
    }

    public void setAddressComponentTypeList(Set<MapEntry> addressComponentTypeList) {
        this.addressComponentTypeList = addressComponentTypeList;
    }

    public Set<MapEntry> getCpfRelationTypeList() {
        return cpfRelationTypeList;
    }

    public void setCpfRelationTypeList(Set<MapEntry> cpfRelationTypeList) {
        this.cpfRelationTypeList = cpfRelationTypeList;
    }

    public Set<MapEntry> getResRelationTypeList() {
        return resRelationTypeList;
    }

    public void setResRelationTypeList(Set<MapEntry> resRelationTypeList) {
        this.resRelationTypeList = resRelationTypeList;
    }

    public Set<MapEntry> getFncRelationTypeList() {
        return fncRelationTypeList;
    }

    public void setFncRelationTypeList(Set<MapEntry> fncRelationTypeList) {
        this.fncRelationTypeList = fncRelationTypeList;
    }

    public String getCpfType() {
        return cpfType;
    }

    public void setCpfType(String cpfType) {
        this.cpfType = cpfType;
    }

    public void setUpload(File file) {
        this.upload = file;
    }

    public File getUpload() {
        return upload;
    }

    public EacCpfLoader getLoader() {
        return loader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUseMode() {
        return useMode;
    }

    public void setUseMode(String useMode) {
        this.useMode = useMode;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getDefaultScript() {
        return defaultScript;
    }

    public String getCpfTypeDescriptionText() {
        return cpfTypeDescriptionText;
    }

    public String getCpfTypeIdentifierText() {
        return cpfTypeIdentifierText;
    }
}
