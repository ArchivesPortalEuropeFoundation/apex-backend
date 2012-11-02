package eu.apenet.dashboard.services.ead;

import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.persistence.vo.Ead;

public class PublishTask extends AbstractEadTask {


	@Override
	protected void execute(Ead ead) throws Exception {
		EADParser.parseEadAndIndex(ead);
	}
}
