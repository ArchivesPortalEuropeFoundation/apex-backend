package eu.apenet.dashboard.services.ead.xml.stream.mets.xpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler;

public class StructMapDivXpathHandler extends AbstractXpathHandler {
	private static final QName FPTR = new QName(ApeXMLConstants.METS_NAMESPACE, "fptr");
	private List<StructMapDiv> results = new ArrayList<StructMapDiv>();
	private StructMapDiv tempStructMapDiv;

	public StructMapDivXpathHandler() {
		super(ApeXMLConstants.METS_NAMESPACE, new String[] { "div", "div" });
	}

	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader) {
		String order = xmlReader.getAttributeValue(null, "ORDER");
		String label = xmlReader.getAttributeValue(null, "LABEL");
		tempStructMapDiv = new StructMapDiv(order, label);
	}

	@Override
	protected void processExactEndElementMatch() {
		if (tempStructMapDiv.isValid()) {
			results.add(tempStructMapDiv);
		}
		tempStructMapDiv = null;
	}

	@Override
	public void processChildStartElementMatch(XMLStreamReader xmlReader) {
		if (FPTR.equals(xmlReader.getName())) {
			String fileId = xmlReader.getAttributeValue(null, "FILEID");
			tempStructMapDiv.getFileIds().add(fileId);
		}
	}

	@Override
	public void processChildEndElementMatch(XMLStreamReader xmlReader) {

	}



	public List<StructMapDiv> getResults() {
		Collections.sort(results, new StructMapDivComparator());
		return results;
	}

	@Override
	public boolean isAllTextBelow() {
		return true;
	}
	private static class StructMapDivComparator implements Comparator<StructMapDiv>{

		@Override
		public int compare(StructMapDiv o1, StructMapDiv o2) {
			if (o1.getOrder() != null && o2.getOrder() != null){
				return o1.getOrder().compareTo(o2.getOrder());
			}
			return 0;
		}
		
	}
}
