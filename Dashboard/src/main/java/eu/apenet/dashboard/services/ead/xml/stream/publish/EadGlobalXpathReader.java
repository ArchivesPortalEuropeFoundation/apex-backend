package eu.apenet.dashboard.services.ead.xml.stream.publish;

import org.apache.commons.lang.StringUtils;

import eu.apenet.persistence.vo.EadContent;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.ApeXmlUtil;
import eu.archivesportaleurope.xml.xpath.AbstractXpathReader;
import eu.archivesportaleurope.xml.xpath.handler.AttributeXpathHandler;

public class EadGlobalXpathReader extends AbstractXpathReader<EadPublishData> {

	private AttributeXpathHandler globalLanguageHandler;

	protected void internalInit() throws Exception {
		globalLanguageHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAD_NAMESPACE, new String[] { "ead",
				"eadheader", "profiledesc", "langusage", "language" }, "langcode");
		getXpathHandlers().add(globalLanguageHandler);

	}

	public void fillData(EadPublishData publishData, EadContent eadContent) {
		publishData.setGlobalLanguage(globalLanguageHandler.getResultAsStringWithWhitespace());

	}
}
