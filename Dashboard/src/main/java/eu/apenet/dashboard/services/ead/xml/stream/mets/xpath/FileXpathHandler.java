package eu.apenet.dashboard.services.ead.xml.stream.mets.xpath;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler;

public class FileXpathHandler extends AbstractXpathHandler {
	private static final QName FLOCAT = new QName(ApeXMLConstants.METS_NAMESPACE, "FLocat");
	private Map<String, MetsFile> results = new HashMap<String, MetsFile>();
	private MetsFile tempMetsFile;

	public FileXpathHandler() {
		super(ApeXMLConstants.METS_NAMESPACE, new String[] { "file" });
	}

	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader) {
		String id = xmlReader.getAttributeValue(null, "ID");
		String use = xmlReader.getAttributeValue(null, "USE");
		tempMetsFile = new MetsFile(id, use);
	}

	@Override
	protected void processExactEndElementMatch() {
		if (tempMetsFile.isValid()) {
			results.put(tempMetsFile.getId(), tempMetsFile);
		}
		tempMetsFile = null;
	}

	@Override
	public void processChildStartElementMatch(XMLStreamReader xmlReader) {
		if (FLOCAT.equals(xmlReader.getName())) {
			tempMetsFile.setHref(xmlReader.getAttributeValue(ApeXMLConstants.XLINK_NAMESPACE, "href"));
			tempMetsFile.setRole(xmlReader.getAttributeValue(ApeXMLConstants.XLINK_NAMESPACE, "role"));
		}
	}

	@Override
	public void processChildEndElementMatch(XMLStreamReader xmlReader) {

	}

	public Map<String, MetsFile> getResults() {
		return results;
	}


	@Override
	public boolean isAllTextBelow() {
		return true;
	}
}
