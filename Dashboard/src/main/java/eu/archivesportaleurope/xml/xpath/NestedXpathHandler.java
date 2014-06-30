package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

public class NestedXpathHandler extends AbstractXpathHandler {
	private LinkedList<QName> xpathPosition = new LinkedList<QName>();
	private List<XmlStreamHandler> handlers = new ArrayList<XmlStreamHandler>();
	public NestedXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);

	}

	@Override
	public boolean isAllTextBelow() {
		return true;
	}
	@Override
	public void processChildStartElementMatch(XMLStreamReader xmlReader) throws Exception {
		xpathPosition.add(xmlReader.getName());
		for (XmlStreamHandler handler: handlers){
			handler.processStartElement(xpathPosition, xmlReader);
		}
	}

	@Override
	protected void internalProcessCharacters(XMLStreamReader xmlReader) throws Exception {
		for (XmlStreamHandler handler: handlers){
			handler.processCharacters(xpathPosition, xmlReader);
		}
	}




	@Override
	public void processChildEndElementMatch(XMLStreamReader xmlReader)  throws Exception {
		for (XmlStreamHandler handler: handlers){
			handler.processEndElement(xpathPosition, xmlReader);
		}
		if (xpathPosition.size() > 0)
			xpathPosition.removeLast();

	}








	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader) {
		xpathPosition.clear();
	}




	@Override
	protected void processExactEndElementMatch() {
		xpathPosition.clear();
	}




	@Override
	protected void clear() {
		
	}




	public List<XmlStreamHandler> getHandlers() {
		return handlers;
	}

	
}
