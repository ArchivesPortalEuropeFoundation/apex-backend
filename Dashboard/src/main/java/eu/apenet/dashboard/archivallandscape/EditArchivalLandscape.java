package eu.apenet.dashboard.archivallandscape;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Lang;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 27/09/2012
 *
 * @author Yoann Moranville
 */
public class EditArchivalLandscape extends AbstractAction {
    private static final Logger LOGGER = Logger.getLogger(EditArchivalLandscape.class);
    private static final String UTF8 = "UTF-8";

    private HttpServletRequest request;
    private HttpServletResponse response;

    private Country country;
    private List<Lang> langList;
    private List<Institution> archivalInstitutions;
    private List<Institution> groupArchivalInstitutionList;

    public List<Lang> getLangList() {
        return langList;
    }
    public List<Institution> getArchivalInstitutions() {
        return archivalInstitutions;
    }
    protected void setArchivalInstitutions(List<Institution> archivalInstitutions){
        this.archivalInstitutions = archivalInstitutions;
    }
    public List<Institution> getGroupArchivalInstitutionList() {
        return groupArchivalInstitutionList;
    }
    public void setGroupArchivalInstitutionList(List<Institution> groupArchivalInstitutionList) {
        this.groupArchivalInstitutionList = groupArchivalInstitutionList;
    }

    @Override
    public void validate() {
        if(archivalInstitutions == null){
            archivalInstitutions = new ArrayList<Institution>();
        }
        if(groupArchivalInstitutionList == null){
            groupArchivalInstitutionList = new ArrayList<Institution>();
        }
        getLanguagesList();
    }

    @Override
    public String execute() throws Exception {
        SecurityContext securityContext = SecurityContext.get();
        country = DAOFactory.instance().getCountryDAO().findById(securityContext.getCountryId());
        List<Institution> institutions = getArchivalInstitutionListFromDB();
        parseList(institutions, null);
        LOGGER.info("Number of institutions: " + archivalInstitutions.size() + ", number of group institutions: " + institutions.size());

        return SUCCESS;
    }

    /**
     * Fill the action languages list to be selected in the edition form
     */
    private void getLanguagesList(){
        langList = DAOFactory.instance().getLangDAO().findAll();
        Collections.sort(langList);
    }

    private List<Institution> getArchivalInstitutionListFromDB() {
        List<Institution> institutions = new ArrayList<Institution>();

        List<ArchivalInstitution> archivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionsByCountryId(country.getId());

        for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
            if(archivalInstitution.getParentAiId() == null) {
                Institution institution = new Institution();
                institution.setId(archivalInstitution.getAiId()+"");
                institution.setLevel("series");
                institution.setName(archivalInstitution.getAiname());
                institution.setInstitutions(
                        archivalInstitutionToInstitution(new ArrayList<ArchivalInstitution>(archivalInstitution.getChildArchivalInstitutions()))
                );
                institutions.add(institution);
            }
        }

        return institutions;
    }

    private List<Institution> archivalInstitutionToInstitution(List<ArchivalInstitution> archivalInstitutions) {
        List<Institution> institutions = new ArrayList<Institution>(archivalInstitutions.size());
        for(ArchivalInstitution archivalInstitution : archivalInstitutions) {
            Institution institution = new Institution();
            institution.setId(archivalInstitution.getAiId()+"");
            institution.setName(archivalInstitution.getAiname());
            institution.setLevel("series");
            institution.setInstitutions(
                    archivalInstitutionToInstitution(new ArrayList<ArchivalInstitution>(archivalInstitution.getChildArchivalInstitutions()))
            );
            institutions.add(institution);
        }
        return institutions;
    }

    /**
     * This method parse an institution lists to elements to be showed
     * in the edition of Archival Landscape
     */
    private void parseList(List<Institution> institutions, String level){
        for(int i = 0; institutions != null && i < institutions.size(); i++) {
            Institution institution = institutions.get(i);

            Institution institutionTemp = new Institution();
            String tab = "";
            int levels = 1;
            if(level != null) {
                if(level.contains(".")) {
                    String[] array = (level.split(" ")[0]).split("\\.");
                    levels = array.length+1;
                } else {
                    levels = 2;
                }
            }

            for(int j=1; j<levels; j++) {
                tab+="|| ";
            }

            if(institution.getLevel().equals("series")) {
                if(level != null) {
                    if(institution.getId() != null && !institution.getId().isEmpty()) {
                        institutionTemp.setId(institution.getId());
                    } else {
                        institutionTemp.setId("A" + System.currentTimeMillis() + "-" + Math.random()*1000000);
                    }
                    institutionTemp.setName(tab + level + "." + (i+1) + " " + institution.getName());
                    archivalInstitutions.add(institutionTemp);
                } else { //Tab should be ""
                    if(institution.getId()!=null && !institution.getId().isEmpty()){
                        institutionTemp.setId(institution.getId());
                    }else{
                        institutionTemp.setId(institution.getId());
                    }
                    institutionTemp.setName(tab + (i+1) + " " + institution.getName());
                    archivalInstitutions.add(institutionTemp);
                }
                if(groupArchivalInstitutionList.isEmpty()){
                    Institution temp = new Institution();
                    temp.setName(country.getCname().toLowerCase());
//                    temp.setId(countryCIdentifier); //No idea what this is for...
                    groupArchivalInstitutionList.add(temp);
                }
                groupArchivalInstitutionList.add(institution);
                if(institution.getInstitutions()!=null) {
                    if(level!=null) {
                        parseList(institution.getInstitutions(), level + "." + i);
                    } else {
                        parseList(institution.getInstitutions(), i + "");
                    }
                }
            } else {
                if(institution.getId() != null && !institution.getId().isEmpty()) {
                    institutionTemp.setId(institution.getId());
                } else {
                    institutionTemp.setId(institution.getId());
                }
                institutionTemp.setName(tab + " * " + institution.getName());
                archivalInstitutions.add(institutionTemp);
            }
        }
    }

    //Action todo
    public String deleteArchivalInstitution() {
        try {
            Writer writer = openOutputWriter();
            writer.close();
        } catch (IOException ioe) {

        }
        return null;
    }

    protected Writer openOutputWriter() throws IOException {
        request.setCharacterEncoding(UTF8);
        response.setCharacterEncoding(UTF8);
        response.setContentType("application/json");
        return new OutputStreamWriter(response.getOutputStream(), UTF8);
    }
}
