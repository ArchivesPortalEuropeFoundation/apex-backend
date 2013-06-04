package eu.apenet.dashboard.services.ead;

import java.util.Date;
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
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class DeliverToEuropeanaTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "deliver to europeana";
	}

	public static boolean valid(Ead ead) {
		if (ead instanceof FindingAid) {
			FindingAid findingAid = (FindingAid) ead;
			return EuropeanaState.CONVERTED.equals(findingAid.getEuropeana());
		}
		return false;
	}

	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		if (valid(ead)) {
			try {
				JpaUtil.beginDatabaseTransaction();
				FindingAid findingAid = (FindingAid) ead;
				EseState eseStatePublished = DAOFactory.instance().getEseStateDAO().getEseStateByState(EseState.PUBLISHED);
				Date newModificationDate = new Date();
				EseDAO esesDao = DAOFactory.instance().getEseDAO();
				List<Ese> eses = esesDao.getEses(findingAid.getId(), findingAid.getAiId());
				if (eses != null && !eses.isEmpty()) {
					Iterator<Ese> iterator = eses.iterator();
					while (iterator.hasNext()) {
						Ese ese = iterator.next();
						if (ese.getEseState().getState().equals(EseState.NOT_PUBLISHED)
								|| ese.getEseState().getState().equals(EseState.REMOVED)) {
							ese.setEseState(eseStatePublished);
		                    ese.setModificationDate(newModificationDate);
							esesDao.updateSimple(ese);
						}
					}
					findingAid.setEuropeana(EuropeanaState.DELIVERED);
					eadDAO.updateSimple(findingAid);
					JpaUtil.commitDatabaseTransaction();
					logAction(ead);

				}
			} catch (Exception e) {
				logAction(ead, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}
		}

	}
}
