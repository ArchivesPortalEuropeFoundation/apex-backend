package eu.apenet.dashboard.services.ead;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.xml.XmlEadParser;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.ValidatedState;

public class ChangeDynamicTask extends AbstractEadTask {
	public static boolean valid(Ead ead){
		return ValidatedState.VALIDATED.equals(ead.getValidated());
	}
	@Override
	public void execute(Ead ead, Properties properties) throws Exception {
		if (valid(ead)) {
			try {
				long startTime = System.currentTimeMillis();
				/*
				 * there is something wrong
				 */
				if (ead.getEadContents().size() > 1){
					throw new APEnetException(this.getActionName() + " More than one eadcontent found. Please republish.");
				}
				if (!ead.isPublished() && ead.getEadContent() == null){
					XmlEadParser.parseEad(ead);				
				}
				ContentUtils.deleteFile(APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead());
				ead.setDynamic(true);
				DAOFactory.instance().getEadDAO().store(ead);
				logAction(ead, System.currentTimeMillis()-startTime);
			} catch (Exception e) {
				logAction(ead, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}
		}
	}

	@Override
	protected String getActionName() {
		return "dynamic";
	}
}
