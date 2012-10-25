package eu.apenet.dashboard.actions;
	
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.IndexUtils;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.IndexQueueDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.IndexQueue;
import eu.apenet.persistence.vo.SourceGuide;

public class IndexingAction extends ActionSupport{

	private static final long TIME_TO_INDEX = 3600000; //3minutes
	private static final long serialVersionUID = 5552216375175501342L;

	private final Logger LOG = Logger.getLogger(getClass());	


	public String execute() throws Exception {
		return indexQueueNew();
	    
	}
	

	
	public String indexQueueOld() throws Exception {

		
		LOG.info("Starting indexing night process...");
		int indexed = 0;
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
                        ContentManager.indexFromQueue(ead);
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
                    indexed++;
                } else {
                    break;
                }
                
            }
            LOG.info("Number of indexed files: " + indexed);
			//Re-organize the queue positions if there're some files still to be indexed in next process
            long startTime1 = System.currentTimeMillis();
			ContentManager.reOrganizeQueue();			
			LOG.info("ReorganizedQueue: " + (System.currentTimeMillis() - startTime1));
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
	public String indexQueueNew() throws Exception {

		
		LOG.info("Starting indexing process...");
		int indexed = 0;
		long startTime = System.currentTimeMillis();
		long endTime = startTime + TIME_TO_INDEX;
		IndexUtils.setIndexing(true);
		

		
		try
		{
			IndexQueueDAO indexqueueDAO =  DAOFactory.instance().getIndexQueueDAO();	
			boolean filesLeft = true;
			while (filesLeft && System.currentTimeMillis() < endTime){
				List<IndexQueue> filesToIndex = indexqueueDAO.getFirstItems(20);
				LOG.info("Indexing sub process for " + filesToIndex.size() + " files");
				indexed += indexSub(filesToIndex, endTime);
				if (filesToIndex.size() == 0){
					filesLeft = false;
				}
			}
            
            LOG.info("Number of indexed files: " + indexed);
			//Re-organize the queue positions if there're some files still to be indexed in next process
		
			IndexUtils.setIndexing(false);
			LOG.info("Indexing process finished: " + (System.currentTimeMillis() - startTime));
			
		}catch (Exception e){
            LOG.error("Error when indexing all", e);
            addActionMessage("error");
            IndexUtils.setIndexing(false);
            return ERROR;
        }
	
        addActionMessage("OK");
        return SUCCESS;
	    
	}
	public int indexSub(List<IndexQueue> filesToIndex, long endTime) throws Exception {
		int indexed = 0;
		IndexQueueDAO indexqueueDAO =  DAOFactory.instance().getIndexQueueDAO();	
		for(IndexQueue indexQueue : filesToIndex) {
            Ead ead;
            if(indexQueue.getHoldingsGuide() != null) {
                ead = indexQueue.getHoldingsGuide();
            } else if(indexQueue.getFindingAid() != null) {
                ead = indexQueue.getFindingAid();
            } else {
                ead = indexQueue.getSourceGuide();
            }
            XmlType xmlType = XmlType.getEadType(ead);

            if (System.currentTimeMillis() < endTime) {
                try {
                	LOG.info(indexQueue.getIqId());
                    //LOG.info("Indexing and updating units/daos: '" + xmlType.getName() + "' id: " + ead.getId() + ", state: " + ead.getFileState().getState() + ", eadid: " + ead.getEadid() + ", aiId: " + ead.getArchivalInstitution().getAiId() + ", file path: " + ead.getPathApenetead());
                    ContentManager.indexFromQueue(ead);
                    if (indexQueue != null)
                        indexqueueDAO.delete(indexQueue);
                } catch (Exception e){
                    String err = "eadid: " + ead.getEadid() + " - id: " + ead.getId() + " - type: " + xmlType.getName();
                    LOG.error("Error indexing: " + err, e);
                    if (indexQueue != null) {
                        indexQueue.setErrors(new Date() + err + ". Error: " + e.getMessage() +"-"+ e.getCause());
                        HibernateUtil.getDatabaseSession().update(indexQueue);
                    }
                    addActionMessage("error: " + err);
                }
                indexed++;
            } else {
                break;
            }
            
        }
		return indexed;
	}
}


