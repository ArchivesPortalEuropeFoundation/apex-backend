package eu.apenet.dashboard.services.ead;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.ValidatedState;

public class PublishTask extends AbstractEadTask {
	public static boolean valid(Ead ead){
		return ValidatedState.VALIDATED.equals(ead.getValidated()) && !ead.isPublished();
	}
	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		if (valid(ead)) {
			try {
				EADParser.parseEadAndIndex(ead);
				logAction(ead, true);
			} catch (Exception e) {
				logAction(ead, false);
				throw new APEnetException("Could not publish the file with ID: " + ead.getId(), e);
			}
		}
	}

	@Override
	protected String getActionName() {
		return "publish";
	}
}
