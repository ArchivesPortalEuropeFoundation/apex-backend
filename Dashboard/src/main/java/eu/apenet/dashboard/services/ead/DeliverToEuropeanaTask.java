package eu.apenet.dashboard.services.ead;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.ResumptionToken;

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

			FindingAid findingAid = (FindingAid) ead;

			try {

				removeOldResumptionTokens();
				Ese ese = null;
				if (!isBeingHarvested()) {
					EseDAO esesDao = DAOFactory.instance().getEseDAO();
					List<Ese> eses = esesDao.getEses(findingAid.getId());
					if (eses != null && !eses.isEmpty()) {
						Iterator<Ese> iterator = eses.iterator();
						while (iterator.hasNext()) {
							ese = iterator.next();
							if (ese.getEseState().getState().equals(EseState.NOT_PUBLISHED)
									|| ese.getEseState().getState().equals(EseState.REMOVED)) {
								ese.setEseState(DAOFactory.instance().getEseStateDAO()
										.getEseStateByState(EseState.PUBLISHED));
								esesDao.update(ese);
							}
						}
						findingAid.setEuropeana(EuropeanaState.DELIVERED);
						eadDAO.store(findingAid);
						logAction(ead, true);

					}
				}
			} catch (Exception e) {
				logAction(ead, false);
				throw new APEnetException("Could not delete ese/edm the file with ID: " + ead.getId(), e);
			}
		}

	}

	public static void removeOldResumptionTokens() {
		ResumptionTokenDAO resumptionTokenDao = DAOFactory.instance().getResumptionTokenDAO();
		List<ResumptionToken> resumptionTokenList = resumptionTokenDao.getOldResumptionTokensThan(new Date());
		Iterator<ResumptionToken> iterator = resumptionTokenList.iterator();
		while (iterator.hasNext()) {
			resumptionTokenDao.delete(iterator.next());
		}
	}
}
