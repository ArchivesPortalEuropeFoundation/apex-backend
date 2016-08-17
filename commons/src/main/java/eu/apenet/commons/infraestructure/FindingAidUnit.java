package eu.apenet.commons.infraestructure;

import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FindingAid;

/**
 *
 * @author Eloy Date: 10th, Nov
 *
 * This class represents a Finding Aid in the Navigation Tree
 */
@SuppressWarnings("rawtypes")
public class FindingAidUnit implements Comparable {

    //Attributes
    private Integer faId;		//Identifier in finding_aid table
    private String faTitle;		//Finding Aid title
    private String ainame;		//Name of the Archival Institution that this Finding Aid belongs to
    //private Boolean alreadySelected;	//If this attribute is true, this finding aid is within the list of finding aids for searching in the Navigation Tree Search
    private String faEadid;		//EAD identifier for a specific Finding Aid

    //Getters and Setters
    public void setFaId(Integer faId) {
        this.faId = faId;
    }

    public Integer getFaId() {
        return faId;
    }

    public void setFaTitle(String faTitle) {
        this.faTitle = faTitle;
    }

    public String getFaTitle() {
        return faTitle;
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

    public void setFaEadid(String faEadid) {
        this.faEadid = faEadid;
    }

    public String getFaEadid() {
        return faEadid;
    }

    //Constructor
    public FindingAidUnit(Integer faId, String faTitle, String ainame, String faEadid) {

        this.faId = faId;
        this.faTitle = faTitle;
        this.ainame = ainame;
        //this.alreadySelected = false;
        this.faEadid = faEadid;
    }

    /*
	public FindingAidUnit(Integer faId) {
		
		FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
		FindingAid findingAid = findingAidDao.findById(faId);
		
		this.faId = faId;
		this.faTitle = findingAid.getFaTitle();
		this.ainame = findingAid.getArchivalInstitution().getAiname();
		//this.alreadySelected = false;
		this.faEadid = findingAid.getFaEadid();
		
		findingAidDao = null;
		findingAid = null;
	}
     */
 /*
	public static String obtainEadid(Integer faId){
		
		FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
		FindingAid findingAid = findingAidDao.findById(faId);
		String eadid = findingAid.getFaEadid();
		
		findingAidDao = null;
		findingAid = null;
		
		return eadid;		
	}
     */
    //This method compares two FindingAidUnit using faTitle
    public int compareTo(Object fau) {
        FindingAidUnit otherFindingAidUnit = (FindingAidUnit) fau;
        return faTitle.compareTo(otherFindingAidUnit.getFaTitle());
    }

}
