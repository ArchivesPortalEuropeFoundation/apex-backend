package eu.apenet.dashboard.services.ead;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;

public class DeleteTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "delete";
	}

	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
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
		if (valid) {
			try {
				ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead());
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
