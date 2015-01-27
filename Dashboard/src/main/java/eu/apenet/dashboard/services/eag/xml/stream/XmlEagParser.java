package eu.apenet.dashboard.services.eag.xml.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dashboard.services.eag.xml.stream.publish.EagPublishData;
import eu.apenet.dashboard.services.eag.xml.stream.publish.EagSolrPublisher;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.xml.XmlParser;


public class XmlEagParser extends AbstractParser {

	private static Logger LOG = Logger.getLogger(XmlEagParser.class);
	public static final String UTF_8 = "utf-8";


    public static long parseAndPublish(ArchivalInstitution archivalInstitution) throws Exception {
    	EagSolrPublisher solrPublisher = new EagSolrPublisher();

		FileInputStream fileInputStream =  getFileInputStream(archivalInstitution.getEagPath());
		EagXpathReader eagXpathReader = new EagXpathReader();
		
		try {
			XmlParser.parse(fileInputStream, eagXpathReader);
			fileInputStream.close();
			EagPublishData publishData = new EagPublishData();
			eagXpathReader.fillData(publishData, archivalInstitution);
			solrPublisher.publish(archivalInstitution, publishData);
			solrPublisher.commitSolrDocuments();
		
		} catch (Exception de) {
			if (solrPublisher != null) {
				LOG.error(archivalInstitution + ": rollback:", de);
				solrPublisher.unpublish(archivalInstitution);
			}
			throw de;
		}
		return solrPublisher.getSolrTime();
	}

	private static FileInputStream getFileInputStream(String path) throws FileNotFoundException, XMLStreamException {
		File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + path);
		return new FileInputStream(file);
	}
}
