package eu.apenet.dashboard.actions;
	
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.IndexQueue;
import eu.apenet.persistence.vo.SourceGuide;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.IndexUtils;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.persistence.dao.IndexQueueDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;

public class IndexingAction extends ActionSupport implements ServletRequestAware{
	
	private static final long TIME_TO_INDEX = 10800000; //3hours
	private static final long serialVersionUID = 5552216375175501342L;

	private final Logger LOG = Logger.getLogger(getClass());	
	private static final String INDEX_PASS = "INDEX_PASS";
	private HttpServletRequest request;	
		   
    private String pwd=null;
    
    public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String execute() throws Exception {

		// Needed for the launching of the process via cron
		String index_password = request.getSession().getServletContext().getInitParameter(INDEX_PASS);	
				
		//Check the password
		if (getPwd() == null) {
			addActionMessage("Error. Password required");		
			return ERROR;
		}
		if (!getPwd().equals(index_password)) {
			addActionMessage("Error. Password invalid");		
			return ERROR;
		}
		
		LOG.info("Starting indexing night process...");
		long startTime = System.currentTimeMillis();
		
		IndexUtils.setIndexing(true);
		
		Set<String> filesNotIndex = new HashSet<String>();
		
		try
		{
			IndexQueueDAO indexqueueDAO =  DAOFactory.instance().getIndexQueueDAO();			
			List<IndexQueue> filesToIndex = indexqueueDAO.getFilesbeforeDate(new Date(), "position", true);
			LOG.info("Indexing night process for " + filesToIndex.size() + " files");


            for(IndexQueue indexQueue : filesToIndex) {
                Ead ead;
                if(indexQueue.getHoldingsGuide() != null) {
                	EadDAO eadDao = DAOFactory.instance().getEadDAO();
                    ead = eadDao.findById(indexQueue.getHoldingsGuide().getId(),HoldingsGuide.class);
                } else if(indexQueue.getFindingAid() != null) {
                	EadDAO eadDao = DAOFactory.instance().getEadDAO();                	
                    ead = eadDao.findById(indexQueue.getFindingAid().getId(),FindingAid.class);
                } else {
                	EadDAO eadDao = DAOFactory.instance().getEadDAO();
                    ead = eadDao.findById(indexQueue.getSourceGuide().getId(),SourceGuide.class);
                }
                XmlType xmlType = XmlType.getEadType(ead);

                long endTime = System.currentTimeMillis();
                if ((endTime - startTime) < TIME_TO_INDEX) {
                    try {
                        LOG.info("Indexing and updating units/daos: '" + xmlType.getName() + "' id: " + ead.getId() + ", state: " + ead.getFileState().getState() + ", eadid: " + ead.getEadid() + ", aiId: " + ead.getArchivalInstitution().getAiId() + ", file path: " + ead.getPathApenetead());
                        ContentManager.index(xmlType, ead.getId());
                        if (indexQueue != null)
                            indexqueueDAO.delete(indexQueue);
                    } catch (Exception e){
                        String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
                        LOG.error("Error indexing: " + err, e);
                        if (indexQueue != null) {
                            indexQueue.setErrors(new Date() + err + ". Error: " + e.getMessage() +"-"+ e.getCause());
                            HibernateUtil.getDatabaseSession().update(indexQueue);
                        }
                        filesNotIndex.add(err);
                        addActionMessage("error: " + err);
                    }
                } else {
                    break;
                }
            }

			//Re-organize the queue positions if there're some files still to be indexed in next process
			ContentManager.reOrganizeQueue();			
			
			filesToIndex.clear();
			filesNotIndex.clear();
			
			IndexUtils.setIndexing(false);
			LOG.info("Indexing night process finished: " + new Date());
			
		}catch (Exception e){
            LOG.error("Error when indexing all", e);
            addActionMessage("error");
            IndexUtils.setIndexing(false);
            return ERROR;
        }
	
        addActionMessage("OK");
        return SUCCESS;
	    
	}
	

	

}


