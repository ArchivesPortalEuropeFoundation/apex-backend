package eu.apenet.dashboard.services.ead;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;

public class DeleteFromEuropeanaTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "delete from europeana";
	}

	public static boolean valid(Ead ead) {
		if (ead instanceof FindingAid) {
			FindingAid findingAid = (FindingAid) ead;
			return EuropeanaState.DELIVERED.equals(findingAid.getEuropeana());
		}
		return false;
	}

	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		if (valid(ead)) {

			FindingAid findingAid = (FindingAid) ead;

			try {
				//Change ese_state from ese to "Removed"
		        Ese ese = null;
		        EseDAO esesDao = DAOFactory.instance().getEseDAO();
		        List<Ese> eses = esesDao.getEses(findingAid.getId());
		        if(eses!=null && !eses.isEmpty()){
		            Iterator<Ese> iterator = eses.iterator();
		            while(iterator.hasNext()){
		                ese = iterator.next();
		                if(ese.getEseState().getState().equals(EseState.PUBLISHED)){
		                    ese.setEseState(DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.REMOVED));
		                    esesDao.update(ese);
		                }
		            }
		        }
		        findingAid.setEuropeana(EuropeanaState.CONVERTED);
				eadDAO.store(findingAid);
				logAction(ead, true);
			} catch (Exception e) {
				logAction(ead, false);
				throw new APEnetException("Could not delete ese/edm the file with ID: " + ead.getId(), e);
			}
		}

	}

}
