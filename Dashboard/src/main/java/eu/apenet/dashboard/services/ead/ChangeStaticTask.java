package eu.apenet.dashboard.services.ead;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.xml.ReconstructEadFile;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;

public class ChangeStaticTask extends AbstractEadTask {
	public static boolean valid(Ead ead) {
		return ead.isDynamic();
	}

	@Override
	public void execute(Ead ead, Properties properties) throws Exception {
		if (valid(ead)) {
			try {
				long startTime = System.currentTimeMillis();
				ReconstructEadFile.reconstructEadFile(ead.getEadContent(), APEnetUtilities.getDashboardConfig()
						.getRepoDirPath() + ead.getPathApenetead());
				ead.setDynamic(false);
				DAOFactory.instance().getEadDAO().store(ead);
				logAction(ead, System.currentTimeMillis() - startTime);
			} catch (Exception e) {
				logAction(ead, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}
		}
	}

	@Override
	protected String getActionName() {
		return "static";
	}
}
