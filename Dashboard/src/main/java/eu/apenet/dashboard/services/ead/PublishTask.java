package eu.apenet.dashboard.services.ead;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.dashboard.services.ead.publish.database.DatabaseEadPublisher;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.ValidatedState;

public class PublishTask extends AbstractEadTask {
	public static boolean valid(Ead ead){
		return ValidatedState.VALIDATED.equals(ead.getValidated()) && !ead.isPublished();
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
					for (EadContent eadContent: ead.getEadContents()){
						DAOFactory.instance().getEadContentDAO().delete(eadContent);
					}
				}
				String message = null;
				if (ead.getEadContent() == null){
					message = "xml";
					EADParser.parseEadAndIndex(ead);					
				}else {
					message = "database";
					DatabaseEadPublisher.publish(ead);
				}

				logAction(ead, message, true, System.currentTimeMillis()-startTime);
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
