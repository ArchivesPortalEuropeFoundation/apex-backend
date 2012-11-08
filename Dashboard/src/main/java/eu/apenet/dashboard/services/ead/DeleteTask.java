package eu.apenet.dashboard.services.ead;

import java.util.Properties;
import java.util.Set;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;

public class DeleteTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "delete";
	}
	public static boolean valid(Ead ead) {
		boolean valid = false;
		if (!ead.isPublished()) {
			if (ead instanceof FindingAid) {
				FindingAid findingAid = (FindingAid) ead;
				if (EuropeanaState.NOT_CONVERTED.equals(findingAid.getEuropeana())) {
					valid =true;
				}
			}else{
				valid =true;
			}
		}
		return valid;
	}
	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {

		if (valid(ead)) {
			try {
                if(ead instanceof FindingAid) {
                	FindingAid findingAid = (FindingAid)ead;
                    EseDAO eseDao = DAOFactory.instance().getEseDAO();
                    EseStateDAO eseStateDao = DAOFactory.instance().getEseStateDAO();
                    Set<Ese> eseList = (findingAid).getEses();
                    for(Ese ese : eseList) {
                        if(ese.getEseState().getState().equals(EseState.PUBLISHED) || ese.getEseState().getState().equals(EseState.REMOVED)) {
                            ese.setFindingAid(null);
                            ese.setPath(null);
                            ese.setEseState(eseStateDao.getEseStateByState(EseState.REMOVED));
                            eseDao.update(ese);
                        } else if(ese.getEseState().getState().equals(EseState.NOT_PUBLISHED)) {
                            eseDao.delete(ese);
                        }
                    }
                    findingAid.getEses().clear();
                }
				ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead());
				if (ead.getQueueItem() != null){
					QueueItemDAO queueItemDAO = DAOFactory.instance().getQueueItemDAO();
					queueItemDAO.delete(ead.getQueueItem());
				}
				ead.getQueuesItems().clear();
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				eadDAO.delete(ead);
				logAction(ead, true);
			} catch (Exception e) {
				logAction(ead, false);
				throw new APEnetException("Could not delete the file with ID: " + ead.getId(), e);
			}
		}

	}

}
