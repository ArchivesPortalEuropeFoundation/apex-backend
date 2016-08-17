package eu.apenet.commons.infraestructure;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FindingAid;

/**
 *
 * @author Eloy Date: 7th, April
 *
 * This class represents a CLevel in the Navigation Tree (within a Holdings
 * Guide)
 */
public class CLevelUnit {

    //Attributes
    private Long cLevelId;
    private Boolean isLeaf;
    private Boolean isFindingAidIndexed;
    private String faEadid;
    private Integer faId;
    private String unittitle;

    //Getters and Setters
    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setcLevelId(Long cLevelId) {
        this.cLevelId = cLevelId;
    }

    public Long getcLevelId() {
        return cLevelId;
    }

    public void setIsFindingAidIndexed(Boolean isFindingAidIndexed) {
        this.isFindingAidIndexed = isFindingAidIndexed;
    }

    public Boolean getIsFindingAidIndexed() {
        return isFindingAidIndexed;
    }

    public void setFaEadid(String faEadid) {
        this.faEadid = faEadid;
    }

    public String getFaEadid() {
        return faEadid;
    }

    public void setUnittitle(String unittitle) {
        this.unittitle = unittitle;
    }

    public String getUnittitle() {
        return unittitle;
    }

    public void setFaId(Integer faId) {
        this.faId = faId;
    }

    public Integer getFaId() {
        return faId;
    }

    //Constructor
    public CLevelUnit(Long cLevelId, Boolean isLeaf, String faEadid, String unittitle, Integer aiId) {

        //String fileState = null;
        //List<String> collection = new ArrayList<String>();
        this.setcLevelId(cLevelId);
        this.setIsLeaf(isLeaf);
        this.setFaEadid(faEadid);
        this.setUnittitle(unittitle);

        if (isLeaf) {
            //The c-level is a Finding Aid
            //First, it is necessary to check if this FA is uploaded
            if (faEadid != null) {
                EadDAO eadDAO = DAOFactory.instance().getEadDAO();
                Integer faId = eadDAO.isEadidUsed(faEadid, aiId, FindingAid.class);
                if (faId != null) {
                    //The FA has been uploaded
                    this.setFaId(faId);

                    //Second, it is necessary to check if this FA is indexed
                    faId = eadDAO.isEadidIndexed(faEadid, aiId, FindingAid.class);

                    if (faId != null) {
                        //The FA is indexed
                        this.setIsFindingAidIndexed(true);
                    } else {
                        //The FA is not indexed
                        this.setIsFindingAidIndexed(false);

                    }
                } else {
                    //The FA has not been uploaded yet
                    this.setFaId(null);
                    this.setIsFindingAidIndexed(false);
                }

                /*
				FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();
				List<FindingAid> findingAidList = findingAidDao.getFindingAids(faEadid, "", "", collection, aiId, null, "", false);

				if (findingAidList.size() > 0){
					//The FA has been uploaded
					fileState = findingAidList.get(0).getFileState().getState();
					this.setFaId(findingAidList.get(0).getId());
					
					//Second, it is necessary to check if this FA is indexed
					if(fileState.equals("New") || fileState.equals("Not_Validated_Not_Converted") || fileState.equals("Not_Validated_Converted") || fileState.equals("Validated_Converted") || fileState.equals("Validated_Not_Converted") || fileState.equals("Validated_Final_Error") || fileState.equals("Indexing") || fileState.equals("Ready_to_index")) {
						//The FA is not indexed
						this.setIsFindingAidIndexed(false);
					}
					else {
						//The FA is indexed
						this.setIsFindingAidIndexed(true);
					}
				}
				else {
					//The FA has not uploaded yet
					this.setFaId(null);
					this.setIsFindingAidIndexed(false);
				}
				
                 */
            } else {
                //The Finding Aid has href_eadid attribute null
                this.setFaId(null);
                this.setIsFindingAidIndexed(false);
            }

        } else {
            this.setFaId(null);
            this.setIsFindingAidIndexed(null);
        }

        //collection = null;
    }

}
