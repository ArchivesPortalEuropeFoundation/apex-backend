package eu.apenet.commons.infraestructure;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.HoldingsGuide;

/**
 *
 * @author Eloy Date: 2nd, Dec
 *
 * This class represents a Holdings Guide in the Navigation Tree
 */
@SuppressWarnings("rawtypes")
public class HoldingsGuideUnit implements Comparable {

    //Attributes
    private Integer hgId;		//Identifier in holdings_guide table
    private String hgTitle;		//Holdings guide title
    private String ainame;		//Name of the Archival Institution that this Holdings Guide belongs to
    //private Boolean alreadySelected;	//If this attribute is true, this holdings guide is within the list of finding aids for searching in the Navigation Tree Search
    private Boolean hasNestedElements;	//If this attribute is true, this holdings guide has more elements (series, subseries, finding aids) nested within it
    private String hgEadid;		//EAD identifier for a specific Holdings Guide

    //Getters and Setters
    public void setHgId(Integer hgId) {
        this.hgId = hgId;
    }

    public Integer getHgId() {
        return hgId;
    }

    public void setHgTitle(String hgTitle) {
        this.hgTitle = hgTitle;
    }

    public String getHgTitle() {
        return hgTitle;
    }

    //public Boolean getAlreadySelected() {
    //return alreadySelected;
    //}
    //public void setAlreadySelected(Boolean alreadySelected) {
    //this.alreadySelected = alreadySelected;
    //}
    public void setAiname(String ainame) {
        this.ainame = ainame;
    }

    public String getAiname() {
        return ainame;
    }

    public Boolean getHasNestedElements() {
        return hasNestedElements;
    }

    public void setHasNestedElements(Boolean hasNestedElements) {
        this.hasNestedElements = hasNestedElements;
    }

    public void setHgEadid(String hgEadid) {
        this.hgEadid = hgEadid;
    }

    public String getHgEadid() {
        return hgEadid;
    }

    //Constructor
    public HoldingsGuideUnit(Integer hgId, String hgTitle, String ainame, String hgEadid) {

        this.hgId = hgId;
        this.hgTitle = hgTitle;
        this.ainame = ainame;
        //this.alreadySelected = false;
        this.hgEadid = hgEadid;

        //Old code:
        //Now, it is necessary to know if the HG has nested elementes within it
        //EadContentDAO eadContentDao = DAOFactory.instance().getEadContentDAO();
        //EadContent eadContent = eadContentDao.getEadContentByHoldingsGuideId(hgId);
        //CLevelDAO cLevelDao = DAOFactory.instance().getCLevelDAO();
        //if (cLevelDao.countTopCLevels(eadContent.getEcId()) > 0){
        //this.hasNestedElements = true;
        //}
        //else {
        //this.hasNestedElements = false;
        //}
        //Due to performance improvement, we will assume that all the HG indexed in the 
        //System will have at least one c-level indexed and therefore 
        //inserted in c-level table. If the old behavior is needed to be enable again,
        //comment this line and uncomment the lines above and below
        this.hasNestedElements = true;

        //eadContentDao = null;
        //eadContent = null;
        //cLevelDao = null;
    }

    /*
	public HoldingsGuideUnit(Integer hgId) {
		
		HoldingsGuideDAO holdingsGuideDao = DAOFactory.instance().getHoldingsGuideDAO();
		HoldingsGuide holdingsGuide = holdingsGuideDao.findById(hgId);
		
		this.hgId = hgId;
		this.hgTitle = holdingsGuide.getHgTittle();
		this.ainame = holdingsGuide.getArchivalInstitution().getAiname();
		this.alreadySelected = false;
		
		holdingsGuideDao = null;
		holdingsGuide = null;
	}
     */
 /*
	public static String obtainEadid(Integer faId){
		
		HoldingsGuideDAO holdingsGuideDao = DAOFactory.instance().getHoldingsGuideDAO();
		HoldingsGuide holdingsGuide = holdingsGuideDao.findById(faId);
		String eadid = holdingsGuide.getHgEadid();
		
		holdingsGuideDao = null;
		holdingsGuide = null;
		
		return eadid;		
	}
     */
    //This method compares two HoldingsGuideUnit using hgTitle
    public int compareTo(Object hgu) {
        HoldingsGuideUnit otherHoldingsGuideUnit = (HoldingsGuideUnit) hgu;
        return hgTitle.compareTo(otherHoldingsGuideUnit.getHgTitle());
    }

    //This method retrieves all the top c-levels for a specific Holdings Guide
    public static List<CLevelUnit> getTopCLevels(Integer hgId, Integer from, Integer maxNumberOfItems) {

        List<CLevelUnit> cLevelUnitList = new ArrayList<CLevelUnit>();
        List<CLevel> cLevelList = new ArrayList<CLevel>();
        Integer aiId = null;

        EadContentDAO eadContentDao = DAOFactory.instance().getEadContentDAO();
        EadContent eadContent = eadContentDao.getEadContentByHoldingsGuideId(hgId);
        aiId = eadContent.getHoldingsGuide().getArchivalInstitution().getAiId();
        CLevelDAO cLevelDao = DAOFactory.instance().getCLevelDAO();
        cLevelList = cLevelDao.findTopCLevels(eadContent.getEcId(), from, maxNumberOfItems);
        for (int i = 0; i < cLevelList.size(); i++) {
            cLevelUnitList.add(new CLevelUnit(cLevelList.get(i).getId(), cLevelList.get(i).isLeaf(), cLevelList.get(i).getHrefEadid(), cLevelList.get(i).getUnittitle(), aiId));
        }

        cLevelList = null;
        eadContentDao = null;
        eadContent = null;
        cLevelDao = null;
        aiId = null;

        return cLevelUnitList;
    }

    //This method retrieves all c-levels which are children from a specific c-level
    public static List<CLevelUnit> getCLevels(Long id, Integer from, Integer maxNumberOfItems) {

        List<CLevelUnit> cLevelUnitList = new ArrayList<CLevelUnit>();
        List<CLevel> cLevelList = new ArrayList<CLevel>();
        Integer aiId = null;

        CLevelDAO cLevelDao = DAOFactory.instance().getCLevelDAO();
        aiId = cLevelDao.findById(id).getEadContent().getHoldingsGuide().getArchivalInstitution().getAiId();
        cLevelList = cLevelDao.findChildCLevels(id, from, maxNumberOfItems);

        for (int i = 0; i < cLevelList.size(); i++) {
            cLevelUnitList.add(new CLevelUnit(cLevelList.get(i).getId(), cLevelList.get(i).isLeaf(), cLevelList.get(i).getHrefEadid(), cLevelList.get(i).getUnittitle(), aiId));
        }

        cLevelList = null;
        cLevelDao = null;
        aiId = null;

        return cLevelUnitList;
    }

}
