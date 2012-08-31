package eu.apenet.commons.infraestructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.Lang;


/**
 * 
 * @author Eloy
 * Date: 10th, Nov
 *
 * This class represents an Archival Institution in the Navigation Tree
 */

public class ArchivalInstitutionUnit implements Comparable<ArchivalInstitutionUnit> {

	//Constants
	private static final String GROUP_WITH_AI_WITH_EAG = "group_with_ai_with_eag";
	
	//Attributes
	private Integer aiId;		//Identifier in archival_institution table
	private String ainame;		//Archival Institution name
	private String aiScndname;  //Archival Institution name in other language
	private List<FindingAidUnit> findingAidList;	//A list with all the finding aids which belong to this Archival Institution
	private List<HoldingsGuideUnit> holdingsGuideList;	//A list with all the holdings guide which belong to this Archival Institution
	private String couName;	//The country name which this Archival Institution belongs to
	private String pathEAG; //The EAG path of an institution
	private Integer numberOfFindingAids; //Number of Finding Aids which belong to an archival institution
	private Integer numberOfHoldingsGuide; //Number of holdings guide which belong to an archival institution
	private Boolean isgroup; //This variable indicates whether the archival institution is a group or not
	private Integer numberOfArchivalInstitutions; //This variable only has sense if the Archival Institution is a group and stores the number of Archival Institutions define within it
	private boolean hasArchivalInstitutions = false;
	
	private Integer alorder; //The order of the archival institution within the Archival Landscape	
	
	//Getters and Setters
	public void setAiId(Integer aiId) {
		this.aiId = aiId;
	}

	public Integer getAiId() {
		return aiId;
	}

	public void setAiname(String ainame) {
		this.ainame = ainame;
	}

	public String getAiname() {
		return ainame;
	}

	public String getAiScndname() {
		return aiScndname;
	}

	public void setAiScndname(String aiScndname) {
		this.aiScndname = aiScndname;
	}

	public void setFindingAidList(List<FindingAidUnit> findingAidList) {
		this.findingAidList = findingAidList;
	}

	public List<FindingAidUnit> getFindingAidList() {
		return findingAidList;
	}
	
	public void setCouName(String countryName) {
		this.couName = countryName;
	}

	public String getCouName() {
		return couName;
	}
	
	public void setHoldingsGuideList(List<HoldingsGuideUnit> holdingsGuideList) {
		this.holdingsGuideList = holdingsGuideList;
	}

	public List<HoldingsGuideUnit> getHoldingsGuideList() {
		return holdingsGuideList;
	}

	public String getPathEAG() {
		return pathEAG;
	}

	public void setPathEAG(String pathEAG) {
		this.pathEAG = pathEAG;
	}
	
	public void setNumberOfFindingAids(Integer numberOfFindingAids) {
		this.numberOfFindingAids = numberOfFindingAids;
	}

	public Integer getNumberOfFindingAids() {
		return numberOfFindingAids;
	}

	public void setNumberOfHoldingsGuide(Integer numberOfHoldingsGuide) {
		this.numberOfHoldingsGuide = numberOfHoldingsGuide;
	}

	public Integer getNumberOfHoldingsGuide() {
		return numberOfHoldingsGuide;
	}

	public Boolean getIsgroup() {
		return isgroup;
	}

	public void setIsgroup(Boolean isgroup) {
		this.isgroup = isgroup;
	}

	public Integer getNumberOfArchivalInstitutions() {
		return numberOfArchivalInstitutions;
	}

	public void setNumberOfArchivalInstitutions(Integer numberOfArchivalInstitutions) {
		this.numberOfArchivalInstitutions = numberOfArchivalInstitutions;
	}
	
	public void setAlorder(Integer alorder) {
		this.alorder = alorder;
	}

	public Integer getAlorder() {
		return alorder;
	}

	/**
	 * Constructor	
	 * @param aiId
	 * @param ainame
	 * @param couName
	 * @param pathEAG
	 * @param language
	 */
	public ArchivalInstitutionUnit(Integer aiId, String ainame, String couName, String pathEAG, Boolean isgroup, Integer numberOfArchivalInstitutions, String language, Integer alorder) {
		
		this.aiId = aiId;
		this.ainame = ainame;
		this.couName = couName;
		this.findingAidList = new ArrayList<FindingAidUnit>();
		this.holdingsGuideList = new ArrayList<HoldingsGuideUnit>();
		this.pathEAG=pathEAG;
		this.isgroup = isgroup;
		this.numberOfArchivalInstitutions = numberOfArchivalInstitutions;
		this.aiScndname = getArchivalInstitutionScndName(language);
		this.alorder = alorder;

        FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
        HoldingsGuideDAO holdingsGuideDao = DAOFactory.instance().getHoldingsGuideDAO();
		if (this.isgroup){
            // It is necessary to count all the finding aids and holdings guide indexed within all the final archival institutions which belongs to this group
            // First, it is necessary to retrieve all the final archival institutions which belongs to this group
            List<ArchivalInstitution> finalArchivalInstitutionList = new ArrayList<ArchivalInstitution>();
            retrieveFinalArchivalInstitutions(finalArchivalInstitutionList, this.getAiId(), true);

            // Second, it is necessary to count all the finding aids and holdings guide indexed for every final archival institution which belongs to this group
            setNumberOfFindingAids(findingAidDao.countFindingAids(finalArchivalInstitutionList, Arrays.asList(FileState.INDEXED_FILE_STATES)).intValue());
            setNumberOfHoldingsGuide(holdingsGuideDao.countHoldingsGuide(finalArchivalInstitutionList, Arrays.asList(FileState.INDEXED_FILE_STATES)).intValue());

            // It is necessary to check if some of the final archival institutions which belong to this group have EAG files uploaded into the System
            for(ArchivalInstitution archivalInstitution : finalArchivalInstitutionList) {
                if (archivalInstitution.getEagPath() != null){
                    setPathEAG(GROUP_WITH_AI_WITH_EAG);
                    break;
                }
            }
        } else {
            setNumberOfFindingAids(findingAidDao.countFindingAids(this.aiId, Arrays.asList(FileState.INDEXED_FILE_STATES), null, null).intValue());
            setNumberOfHoldingsGuide(holdingsGuideDao.countHoldingsGuide(this.aiId, Arrays.asList(FileState.INDEXED_FILE_STATES)).intValue());
            this.findingAidList = null;
            this.holdingsGuideList = null;
		}
	}

	/**
	 * This method has been rewritten because the algorithm doesn't need read from file because
	 * names from AL have been written in DDBB (ai_alternative_name), it searches
	 * the AI_NAME in the selected language, if there aren't, it queries by EN name
	 * and if it isn't, it's caught the first name of AI
	 */ 
	private String getArchivalInstitutionScndName(String language) {
		AiAlternativeNameDAO andao = DAOFactory.instance().getAiAlternativeNameDAO();
		ArchivalInstitutionDAO aidao = DAOFactory.instance().getArchivalInstitutionDAO();
		LangDAO ldao = DAOFactory.instance().getLangDAO();
		ArchivalInstitution ai = aidao.getArchivalInstitution(this.aiId);
		if(ai!=null){
			Lang lang = ldao.getLangByIso2Name(language.toUpperCase());
			if (lang!=null) {
				AiAlternativeName an = andao.findByAIIdandLang(ai, lang);
				if (an == null) {
					Lang lTemp = ldao.getLangByIso2Name("EN");
					if(lTemp!=null){
						an = andao.findByAIIdandLang(ai, lTemp);
					}
				}
				if (an != null) {
					return an.getAiAName();
				}else{
					//This case happens when institution could be written only in other/s language/s 
					//different to English
					List<AiAlternativeName> temp = andao.findByAIId(ai);
					if(temp!=null && temp.size()>0){ //Country could have several names in other language (f.e. Belgium)
						return temp.get(0).getAiAName(); 
					}
				}
			}
		}
		//This return never should happens because always ai_alternative_name should has a relative name
		return "Archival Institution";
	}

	/**
	 * This method compares two ArchivalInstitutionUnit using alorder
	 */
	public int compareTo(ArchivalInstitutionUnit aiu) {
		return alorder.compareTo(aiu.getAlorder());
	}

	public static Long countHoldingsGuide (Integer aiId) {
		return DAOFactory.instance().getHoldingsGuideDAO().countHoldingsGuideByArchivalInstitution(aiId, Arrays.asList(FileState.INDEXED_FILE_STATES));
	}

	public static Long countFindingAidsNotLinked (Integer aiId) {
		return DAOFactory.instance().getFindingAidDAO().countFindingAidsNotLinkedByArchivalInstitution(aiId, Arrays.asList(FileState.INDEXED_FILE_STATES));
	}
	
	//This method obtains all the finding aids not linked and indexed related to an Archival Insititution using pagination
	public static List<FindingAidUnit> getFindingAidsNotLinkedBySegment (Integer aiId, Integer from, Integer maxNumberOfItems) {
		List<FindingAid> findingAidList = DAOFactory.instance().getFindingAidDAO().getFindingAidsNotLinkedBySegment(aiId, Arrays.asList(FileState.INDEXED_FILE_STATES), "faTitle", true, from, maxNumberOfItems);
        return createFindingAidUnits(findingAidList);
	}

	//This method obtains all the finding aids not linked and indexed related to an Archival Insititution
	public static List<FindingAidUnit> getFindingAidsNotLinked (Integer aiId){
		List<FindingAid> findingAidList = DAOFactory.instance().getFindingAidDAO().getFindingAidsNotLinked(aiId, Arrays.asList(FileState.INDEXED_FILE_STATES), "faTitle", true);
        return createFindingAidUnits(findingAidList);
	}

    private static List<FindingAidUnit> createFindingAidUnits(List<FindingAid> findingAidList) {
        List<FindingAidUnit> findingAidUnitList = new ArrayList<FindingAidUnit>();
        for (FindingAid findingAid : findingAidList)
            findingAidUnitList.add(new FindingAidUnit(findingAid.getId(), findingAid.getTitle(), findingAid.getArchivalInstitution().getAiname(), findingAid.getEadid()));
        return findingAidUnitList;
    }

	//This method obtains all the holdings guide indexed related to an Archival Institution
	public static List<HoldingsGuideUnit> getHoldingsGuide (Integer aiId){
		List<HoldingsGuide> holdingsGuideList = DAOFactory.instance().getHoldingsGuideDAO().getHoldingsGuidesByStateAndArchivalInstitution(aiId, Arrays.asList(FileState.INDEXED_FILE_STATES));

        List<HoldingsGuideUnit> holdingsGuideUnitList = new ArrayList<HoldingsGuideUnit>();
        for (HoldingsGuide currentHoldingsGuide : holdingsGuideList)
            holdingsGuideUnitList.add(new HoldingsGuideUnit(currentHoldingsGuide.getId(), currentHoldingsGuide.getTitle(), null, currentHoldingsGuide.getEadid()));
		return holdingsGuideUnitList;
	}
	
	// This method retrieves all the final archival institutions which belongs to a certain group
	public static void retrieveFinalArchivalInstitutions(List<ArchivalInstitution> finalArchivalInstitutionList, Integer aiId, Boolean isGroup) {
		if (isGroup) {
			ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
			List<ArchivalInstitution> archivalInstitutionDescendantList = archivalInstitutionDao.getArchivalInstitutionsByParentAiId(aiId);
            for (ArchivalInstitution anArchivalInstitutionDescendantList : archivalInstitutionDescendantList) {
                if (anArchivalInstitutionDescendantList.isGroup())
                    retrieveFinalArchivalInstitutions(finalArchivalInstitutionList, anArchivalInstitutionDescendantList.getAiId(), true);
                else
                    finalArchivalInstitutionList.add(anArchivalInstitutionDescendantList);
            }
		}
	}

	public boolean isHasArchivalInstitutions() {
		return numberOfArchivalInstitutions > 0;
	}

	public void setHasArchivalInstitutions(boolean hasArchivalInstitutions) {
		this.hasArchivalInstitutions = hasArchivalInstitutions;
	}

}