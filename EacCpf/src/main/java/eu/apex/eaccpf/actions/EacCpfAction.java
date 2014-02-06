/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apex.eaccpf.actions;

import com.opensymphony.xwork2.ActionSupport;
import eu.apex.eaccpf.util.MapEntry;
import java.util.TreeMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

/**
 *
 * @author papp
 */
public abstract class EacCpfAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, ServletContextAware {

    private Set<MapEntry> useModeList = new TreeSet<MapEntry>();
    private Set<MapEntry> cpfTypeList = new TreeSet<MapEntry>();
    private Set<MapEntry> languages = new TreeSet<MapEntry>();
    private Map<String, String> dateOrDateRange = new TreeMap<String, String>();
    private Set<MapEntry> countryList = new TreeSet<MapEntry>();
    private Map<String, String> scriptList = new TreeMap<String, String>();
    private Set<String> formNameList = new TreeSet<String>();
    private Set<String> componentNameList = new TreeSet<String>();
    private Set<String> cpfRelationTypeList = new TreeSet<String>();
    private Set<String> resRelationTypeList = new TreeSet<String>();
    private Set<String> fncRelationTypeList = new TreeSet<String>();
    private Set<String> addressComponentTypeList = new TreeSet<String>();
    private static final String EMPTY_KEY = "";
    private static final String EMPTY_VALUE = "----";
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;

    private String cpfType;
    private String apeId;

    public EacCpfAction() {
        setUpUseModeList();
        setUpCpfTypeList();
        setUpLanguages();
        setUpDateOrDateRange();
        setUpCountryList();
        setUpScriptList();
        setUpFormOfNameList();
        setUpComponentNameList();
        setUpCpfRelationTypeList();
        setUpResRelationTypeList();
        setUpFncRelationTypeList();
        setUpAddressComponentTypeList();

        Random random = new Random();
        long fakeId = random.nextLong();
        this.apeId = Long.toString(fakeId);
    }

    private void setUpLanguages() {
        //Base variable for setting up the list
        String[] isoLanguages = Locale.getISOLanguages();

        // Add empty map entry
        languages.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));
        // Add 639-2/B variants below this line
        languages.add(new MapEntry("alb", new Locale("sq").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("arm", new Locale("hy").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("baq", new Locale("eu").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("bur", new Locale("my").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("chi", new Locale("zh").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("cze", new Locale("cs").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("dut", new Locale("nl").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("fre", new Locale("fr").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("geo", new Locale("ka").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("ger", new Locale("de").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("gre", new Locale("el").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("ice", new Locale("is").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("mac", new Locale("mk").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("mao", new Locale("mi").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("may", new Locale("ms").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("per", new Locale("fa").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("rum", new Locale("ro").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("slo", new Locale("sk").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("tib", new Locale("bo").getDisplayLanguage(Locale.ENGLISH)));
        languages.add(new MapEntry("wel", new Locale("cy").getDisplayLanguage(Locale.ENGLISH)));
        // Add remaining 639-2 variants; any T variants of countries listed above will not be added to the set
        for (String language : isoLanguages) {
            Locale locale = new Locale(language);
            String languageCode = locale.getISO3Language();
            String languageDescription = locale.getDisplayLanguage(Locale.ENGLISH);
            languages.add(new MapEntry(languageCode, languageDescription));
        }
    }

    private void setUpUseModeList() {
        useModeList.add(new MapEntry("new", "to create a new EAC-CPF instance?"));
        useModeList.add(new MapEntry("load", "to load an existing one for editing?"));
    }

    private void setUpCpfTypeList() {
        cpfTypeList.add(new MapEntry("person", "a person"));
        cpfTypeList.add(new MapEntry("corporateBody", "a corporate body"));
        cpfTypeList.add(new MapEntry("family", "a family"));
    }

    private void setUpDateOrDateRange() {
        dateOrDateRange.put("date", "a single date");
        dateOrDateRange.put("dateRange", "or a date range");
    }

    private void setUpCountryList() {
        // Add empty map entry
        countryList.add(new MapEntry(EMPTY_KEY, EMPTY_VALUE));
        // Add countries
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countryList.add(new MapEntry(obj.getCountry(), obj.getDisplayCountry(Locale.ENGLISH)));
        }
    }

    private void setUpScriptList() {
        // Add empty list entry
        scriptList.put(EMPTY_KEY, EMPTY_VALUE);

        scriptList.put("Arab", "Arabic");
        scriptList.put("Armn", "Armenian");
        scriptList.put("Cyrl", "Cyrilic");
        scriptList.put("Geor", "Georgian");
        scriptList.put("Grek", "Greek");
        scriptList.put("Hebr", "Hebrew");
        scriptList.put("Latn", "Latin");
    }

    private void setUpFormOfNameList() {
        formNameList.add("authorized");
        formNameList.add("alternative");
        formNameList.add("preferred");
        formNameList.add("abbreviation");
        formNameList.add("other");
    }

    private void setUpComponentNameList() {
        componentNameList.add("persname");
        componentNameList.add("surname");
        componentNameList.add("firstname");
        componentNameList.add("birthname");
        componentNameList.add("patronymic");
        componentNameList.add("title");
        componentNameList.add("prefix");
        componentNameList.add("suffix");
        componentNameList.add("alias");
    }

    private void setUpCpfRelationTypeList() {
        // Add empty list entry
        cpfRelationTypeList.add(EMPTY_VALUE);

        cpfRelationTypeList.add("identity");
        cpfRelationTypeList.add("hierarchical");
        cpfRelationTypeList.add("hierarchical-parent");
        cpfRelationTypeList.add("hierarchical-child");
        cpfRelationTypeList.add("temporal");
        cpfRelationTypeList.add("temporal-earlier");
        cpfRelationTypeList.add("temporal-later");
        cpfRelationTypeList.add("family");
        cpfRelationTypeList.add("associative");
    }

    private void setUpResRelationTypeList() {
        // Add empty list entry
        resRelationTypeList.add(EMPTY_VALUE);

        resRelationTypeList.add("creatorOf");
        resRelationTypeList.add("subjectOf");
        resRelationTypeList.add("other");
    }

    private void setUpFncRelationTypeList() {
        // Add empty list entry
        fncRelationTypeList.add(EMPTY_VALUE);

        fncRelationTypeList.add("controls");
        fncRelationTypeList.add("owns");
        fncRelationTypeList.add("performs");
    }

    private void setUpAddressComponentTypeList() {
        // Add empty list entry
        addressComponentTypeList.add(EMPTY_VALUE);

        addressComponentTypeList.add("firstdem");
        addressComponentTypeList.add("secondem");
        addressComponentTypeList.add("postalcode");
        addressComponentTypeList.add("localentity");
        addressComponentTypeList.add("street");
        addressComponentTypeList.add("other");
    }

    public Set<MapEntry> getUseModeList() {
        return useModeList;
    }

    public void setUseModeList(Set<MapEntry> useModeList) {
        this.useModeList = useModeList;
    }

    public Set<MapEntry> getCpfTypeList() {
        return cpfTypeList;
    }

    public void setCpfTypeList(Set<MapEntry> cpfTypeList) {
        this.cpfTypeList = cpfTypeList;
    }

    public Set<MapEntry> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<MapEntry> languages) {
        this.languages = languages;
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

    public Map<String, String> getScriptList() {
        return scriptList;
    }

    public void setScriptList(Map<String, String> scriptList) {
        this.scriptList = scriptList;
    }

    public Set<String> getFormOfNameList() {
        return formNameList;
    }

    public void setFormOfNameList(Set<String> formOfNameList) {
        this.formNameList = formOfNameList;
    }

    public Set<String> getComponentOfNameList() {
        return componentNameList;
    }

    public void setComponentOfNameList(Set<String> componentOfNameList) {
        this.componentNameList = componentOfNameList;
    }

    public Set<String> getFormNameList() {
        return formNameList;
    }

    public void setFormNameList(Set<String> formNameList) {
        this.formNameList = formNameList;
    }

    public Set<String> getComponentNameList() {
        return componentNameList;
    }

    public void setComponentNameList(Set<String> componentNameList) {
        this.componentNameList = componentNameList;
    }

    public Set<String> getCpfRelationTypeList() {
        return cpfRelationTypeList;
    }

    public void setCpfRelationTypeList(Set<String> cpfRelationTypeList) {
        this.cpfRelationTypeList = cpfRelationTypeList;
    }

    public Set<String> getResRelationTypeList() {
        return resRelationTypeList;
    }

    public void setResRelationTypeList(Set<String> resRelationTypeList) {
        this.resRelationTypeList = resRelationTypeList;
    }

    public Set<String> getFncRelationTypeList() {
        return fncRelationTypeList;
    }

    public void setFncRelationTypeList(Set<String> fncRelationTypeList) {
        this.fncRelationTypeList = fncRelationTypeList;
    }

    public Set<String> getAddressComponentTypeList() {
        return addressComponentTypeList;
    }

    public void setAddressComponentTypeList(Set<String> addressComponentTypeList) {
        this.addressComponentTypeList = addressComponentTypeList;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public final void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    protected HttpServletRequest getServletRequest() {
        return request;
    }

    @Override
    public final void setServletRequest(HttpServletRequest request) {
        this.request = request;

    }

    protected HttpServletResponse getServletResponse() {
        return response;
    }

    @Override
    public final void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getCpfType() {
        return cpfType;
    }

    public void setCpfType(String cpfType) {
        this.cpfType = cpfType;
    }

    public String getDefaultCpfType() {
        return "person";
    }

    public String getApeId() {
        return apeId;
    }

    public void setApeId(String apeId) {
        this.apeId = apeId;
    }
}
