package eu.apenet.dashboard.services.ead;

import java.util.Set;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.Warnings;

public class DeleteTask extends AbstractEadTask {

	@Override
	protected void execute(Ead ead) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        WarningsDAO warningsDao = DAOFactory.instance().getWarningsDAO();
        Set<Warnings> warningsList = ead.getWarningses();
        for (Warnings warnings : warningsList) {
            logger.debug("Removing warnings for EAD which EADID is " + ead.getEadid());
            warningsDao.deleteSimple(warnings);
        }
        eadDAO.deleteSimple(ead);
        ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead());
	}

}
