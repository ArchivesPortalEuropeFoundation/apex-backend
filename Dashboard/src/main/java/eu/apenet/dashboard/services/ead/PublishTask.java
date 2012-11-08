package eu.apenet.dashboard.services.ead;

import java.util.Properties;

import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.persistence.vo.Ead;

public class PublishTask extends AbstractEadTask {


	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		EADParser.parseEadAndIndex(ead);
	}
}
