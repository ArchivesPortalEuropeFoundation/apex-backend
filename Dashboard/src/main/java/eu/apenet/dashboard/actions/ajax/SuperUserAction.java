package eu.apenet.dashboard.actions.ajax;

import java.io.Writer;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

/**
 * User: Yoann Moranville
 * Date: Mar 3, 2011
 *
 * @author Yoann Moranville
 */
public class SuperUserAction extends AjaxControllerAbstractAction {
    private static final long serialVersionUID = 792819998058035041L;

    private Map<String, Integer> dataPerCountry;
    private Map<String, Map<String, Integer>> allDataPerCountry;

    private static final Set<String> areIndexableStates;
    static {
        Set<String> set = new HashSet<String>();
        set.add(FileState.READY_TO_INDEX);
        areIndexableStates = Collections.unmodifiableSet(set);
    }

    @Override
    public String execute() {
        if(SecurityContext.get().isAdmin()){
            List<Country> countries = getAllCountries();
            retrieveCounts(countries);
            return SUCCESS;
        } else {
            return ERROR;
        }
    }

    private List<Country> getAllCountries(){
        CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
        return countryDAO.findAll();
    }

    private void retrieveCounts(List<Country> countries){
        try {
            allDataPerCountry = new HashMap<String, Map<String, Integer>>();
            for(Country country : countries){
                dataPerCountry = new HashMap<String, Integer>();
                int totalFaNumberCounter = 0;
                int totalHgNumberCounter = 0;
                int totalArchivalInstitutionNumberCounter = 0;
                int totalArchivalInstitutionNoGroupsNumberCounter = 0;
                int totalNumberOfDaos = 0;
                int totalNumberOfUnits = 0;

                for(ArchivalInstitution archivalInstitution : country.getArchivalInstitutions()){
                        for(FindingAid findingAid : archivalInstitution.getFindingAids()){
                            totalNumberOfUnits += (findingAid.getTotalNumberOfUnits() == null ? 0 : findingAid.getTotalNumberOfUnits());
                            totalNumberOfDaos += (findingAid.getTotalNumberOfDaos() == null ? 0 : findingAid.getTotalNumberOfDaos());
                        }
                        for(HoldingsGuide holdingsGuide : archivalInstitution.getHoldingsGuides()){
                            totalNumberOfUnits += (holdingsGuide.getTotalNumberOfUnits() == null ? 0 : holdingsGuide.getTotalNumberOfUnits());
                            totalNumberOfDaos += (holdingsGuide.getTotalNumberOfDaos() == null ? 0 : holdingsGuide.getTotalNumberOfDaos());
                        }
                        totalFaNumberCounter += archivalInstitution.getFindingAids().size();
                        totalHgNumberCounter += archivalInstitution.getHoldingsGuides().size();
                        if(!archivalInstitution.getIsgroup())
                            totalArchivalInstitutionNoGroupsNumberCounter++;
                    }
                   totalArchivalInstitutionNumberCounter += country.getArchivalInstitutions().size();
         
                dataPerCountry.put("FAs", totalFaNumberCounter);
                dataPerCountry.put("HGs", totalHgNumberCounter);
                dataPerCountry.put("AIs with groups", totalArchivalInstitutionNumberCounter);
                dataPerCountry.put("AIs without groups", totalArchivalInstitutionNoGroupsNumberCounter);
                dataPerCountry.put("DAOs", totalNumberOfDaos);
                dataPerCountry.put("DUs", totalNumberOfUnits);

                allDataPerCountry.put(country.getCname(), dataPerCountry);
            }
        } catch (Exception e){
            LOG.error("Error in counting.", e);
        }
    }

    public String indexAllData(){
        if(!SecurityContext.get().isAdmin()){
            return null;
        }
        Set<String> filesNotIndex = new HashSet<String>();
        try {
            Writer writer = openOutputWriter();

            List<Ead> eads = DAOFactory.instance().getEadDAO().getEadsByStates(areIndexableStates, FindingAid.class);
            int maxIndexingAtOnce = 100;
            if(eads != null){
                LOG.info("Total number of Finding Aids: " + eads.size());
                for(Ead ead : eads){
                    if(maxIndexingAtOnce-- <= 0){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("continueIndexing", "true");
                        if(filesNotIndex.size() == 0){
                            jsonObject.put("result", new Date().toString() + " - No errors so far");
                        } else {
                            jsonObject.put("result", new Date().toString() + " - ERROR: " + filesNotIndex.size() + " files not indexed.<br/>" + listToString(filesNotIndex));
                        }
                        writer.append(jsonObject.toString());
                        writer.close();
                        return null;
                    }
                    try {
                        LOG.info("Indexing and updating units/daos: FA id: " + ead.getId() + ", state: " + ead.getFileState().getState() + ", eadid: " + ead.getEadid() + ", aiId: " + ead.getArchivalInstitution().getAiId() + ", file path: " + ead.getPathApenetead());
                        ContentManager.index(XmlType.EAD_FA, ead.getId());
                    } catch (Exception e){
                        String err = "eadid: " + ead.getEadid() + " - faId: " + ead.getId();
                        LOG.error("Error indexing: " + err);
                        filesNotIndex.add(err);
                    }
                }
            }

            eads = DAOFactory.instance().getEadDAO().getEadsByStates(areIndexableStates, HoldingsGuide.class);
            if(eads != null){
                LOG.info("Total number of Holdings Guides: " + eads.size());
                for(Ead ead : eads){
                    if(maxIndexingAtOnce-- <= 0){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("continueIndexing", "true");
                        if(filesNotIndex.size() == 0){
                            jsonObject.put("result", new Date().toString() + " - No errors so far");
                        } else {
                            jsonObject.put("result", new Date().toString() + " - ERROR: " + filesNotIndex.size() + " files not indexed.<br/>" + listToString(filesNotIndex));
                        }
                        writer.append(jsonObject.toString());
                        writer.close();
                        return null;
                    }
                    try {
                        LOG.info("Indexing and updating units/daos: HG id: " + ead.getId() + ", state: " + ead.getFileState().getState() + ", eadid: " + ead.getEadid() + ", aiId: " + ead.getArchivalInstitution().getAiId() + ", file path: " + ead.getPathApenetead());
                        ContentManager.index(XmlType.EAD_HG, ead.getId());
                    } catch (Exception e){
                        String err = "eadid: " + ead.getEadid() + " - hgId: " + ead.getId();
                        LOG.error("Error indexing: " + err);
                        filesNotIndex.add(err);
                    }

                }
            }

            eads = DAOFactory.instance().getEadDAO().getEadsByStates(areIndexableStates, SourceGuide.class);
            if(eads != null){
                LOG.info("Total number of Source Guides: " + eads.size());
                for(Ead ead : eads){
                    if(maxIndexingAtOnce-- <= 0){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("continueIndexing", "true");
                        if(filesNotIndex.size() == 0){
                            jsonObject.put("result", new Date().toString() + " - No errors so far");
                        } else {
                            jsonObject.put("result", new Date().toString() + " - ERROR: " + filesNotIndex.size() + " files not indexed.<br/>" + listToString(filesNotIndex));
                        }
                        writer.append(jsonObject.toString());
                        writer.close();
                        return null;
                    }
                    try {
                        LOG.info("Indexing and updating units/daos: SG id: " + ead.getId() + ", state: " + ead.getFileState().getState() + ", eadid: " + ead.getEadid() + ", aiId: " + ead.getArchivalInstitution().getAiId() + ", file path: " + ead.getPathApenetead());
                        ContentManager.index(XmlType.EAD_SG, ead.getId());
                    } catch (Exception e){
                        String err = "eadid: " + ead.getEadid() + " - sgId: " + ead.getId();
                        LOG.error("Error indexing: " + err);
                        filesNotIndex.add(err);
                    }

                }
            }

            if(filesNotIndex.size() == 0){
                writer.append(new JSONObject().put("result", new Date().toString() + " - Finished the whole batch! Congratulations!").toString());
                writer.close();
            } else {
                writer.append(new JSONObject().put("result", new Date().toString() + " - ERROR: " + filesNotIndex.size() + " files not indexed.<br/>" + listToString(filesNotIndex)).toString());
                writer.close();
            }
        } catch (Exception e){
            LOG.error("Error when indexing all", e);
        }
        return null;
    }

    private String listToString(Set<String> listFiles){
        StringBuilder errorString = new StringBuilder();
        for(String fileError : listFiles){
            errorString.append(fileError).append("<br />");
        }
        return errorString.toString();
    }

    public Map<String, Integer> getDataPerCountry() {
        return dataPerCountry;
    }

    public void setDataPerCountry(Map<String, Integer> dataPerCountry) {
        this.dataPerCountry = dataPerCountry;
    }

    public Map<String, Map<String, Integer>> getAllDataPerCountry() {
        return allDataPerCountry;
    }

    public void setAllDataPerCountry(Map<String, Map<String, Integer>> allDataPerCountry) {
        this.allDataPerCountry = allDataPerCountry;
    }
}
