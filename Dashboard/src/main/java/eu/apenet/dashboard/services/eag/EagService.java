package eu.apenet.dashboard.services.eag;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.dashboard.services.eag.xml.stream.XmlEagParser;
import eu.apenet.dashboard.services.eag.xml.stream.publish.EagSolrPublisher;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class EagService {
	private static final Logger LOGGER = Logger.getLogger(EagService.class);
	
	public static void unpublish(ArchivalInstitution archivalInstitution) throws SolrServerException, IOException{
		if (!archivalInstitution.isGroup() && StringUtils.isNotBlank(archivalInstitution.getEagPath())){
			new EagSolrPublisher().unpublish(archivalInstitution);
			LOGGER.info("unpublish: " + archivalInstitution.getAiname());
		}
	}
	public static void publish(ArchivalInstitution archivalInstitution) throws Exception{
		if (!archivalInstitution.isGroup() && StringUtils.isNotBlank(archivalInstitution.getEagPath())){
			XmlEagParser.parseAndPublish(archivalInstitution);
			LOGGER.info("publish: " + archivalInstitution.getAiname());
		}
	}
}
