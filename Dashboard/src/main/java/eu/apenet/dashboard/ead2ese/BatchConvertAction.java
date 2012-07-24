package eu.apenet.dashboard.ead2ese;

import java.util.*;
import javax.servlet.http.HttpSession;
import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dpt.utils.ead2ese.Config;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import eu.apenet.dashboard.manual.FindingAidLogic;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;

public class BatchConvertAction extends ConvertAction{
    private Logger log = Logger.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = -304486360468003677L;
	private String searchTerms;
	private Integer search;
    private String type;
    private int nbFindingAidForBatch;
    private String jsonListFindingAid;





	public String execute() {
		Config config = fillConfig();
		ServletActionContext.getRequest().getSession().setAttribute("eseConfig" , config);
		type = "Converting to ESE";

		Integer aiId = getAiId();
        FindingAidDAO findingAidDao = DAOFactory.instance().getFindingAidDAO();

    	Set<String> fileStates = new HashSet<String>();
    	fileStates.add(FileState.INDEXED);

            HttpSession session = ServletActionContext.getRequest().getSession();
            List<Integer> tempIds = (List<Integer>)session.getAttribute(AjaxControllerAbstractAction.LIST_IDS);

            FindingAidLogic fal = new FindingAidLogic();
            List<FindingAid> findingAidSearchListToUse = null;
            if(this.searchTerms.trim().length()>0 && !this.searchTerms.contains("%"))
			    findingAidSearchListToUse = fal.search(searchTerms, null, null, aiId, search, null, false, fileStates, null, null);
            else
                findingAidSearchListToUse = new ArrayList<FindingAid>();

			List<FindingAid> findingAidListToUse = findingAidDao.getFindingAids(aiId, fileStates);
            List<FindingAid> findingAidSelectedListToUse = findingAidDao.getFindingAids(aiId, tempIds, fileStates);

        log.info("Number of Finding Aids to convert to ESE: " + findingAidListToUse.size());
        log.info("Number of Search Finding Aids to convert to ESE: " + findingAidSearchListToUse.size());
        log.info("Number of Selected Finding Aids to convert to ESE: " + findingAidSelectedListToUse.size());

        ServletActionContext.getRequest().setAttribute("nbSearchFindingAidForBatch", findingAidSearchListToUse.size());
        ServletActionContext.getRequest().setAttribute("nbSelectedFindingAidForBatch", findingAidSelectedListToUse.size());
        ServletActionContext.getRequest().setAttribute("nbFindingAidForBatch", findingAidListToUse.size());
        ServletActionContext.getRequest().setAttribute("type", type);
        ServletActionContext.getRequest().setAttribute("aiId", aiId);

        StringBuilder faIdArray = new StringBuilder("[");
        int count = 0;
        int size = findingAidListToUse.size();
        for(FindingAid fa : findingAidListToUse){
        	faIdArray.append(fa.getId());
            if (count < size -1)
            	faIdArray.append(",");
            count++;
        }
        faIdArray.append("]");

        StringBuilder faIdArray_small = new StringBuilder("[");
        count = 0;
        size = findingAidSelectedListToUse.size();
        for(FindingAid fa : findingAidSelectedListToUse){
        	faIdArray_small.append(fa.getId());
            if (count < size -1)
            	faIdArray_small.append(",");
            count++;
        }
        faIdArray_small.append("]");

        StringBuilder faIdArray_search = new StringBuilder("[");
        count = 0;
        size = findingAidSearchListToUse.size();
        for(FindingAid fa : findingAidSearchListToUse){
        	faIdArray_search.append(fa.getId());
            if (count < size -1)
            	faIdArray_search.append(",");
            count++;
        }
        faIdArray_search.append("]");

        session.setAttribute("faID", faIdArray.toString());
        session.setAttribute("faID_small", faIdArray_small.toString());
        session.setAttribute("faID_search", faIdArray_search.toString());

        log.debug("returning input");
        return SUCCESS;
    }

	
	public String getSearchTerms() {
		return searchTerms;
	}


	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}


	public Integer getSearch() {
		return search;
	}


	public void setSearch(Integer search) {
		this.search = search;
	}





	public int getNbFindingAidForBatch() {
		return nbFindingAidForBatch;
	}


	public void setNbFindingAidForBatch(int nbFindingAidForBatch) {
		this.nbFindingAidForBatch = nbFindingAidForBatch;
	}


	public String getJsonListFindingAid() {
		return jsonListFindingAid;
	}


	public void setJsonListFindingAid(String jsonListFindingAid) {
		this.jsonListFindingAid = jsonListFindingAid;
	}

}
