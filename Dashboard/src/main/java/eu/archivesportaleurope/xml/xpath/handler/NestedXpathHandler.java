package eu.archivesportaleurope.xml.xpath.handler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
/**
 * Handler that support xpath queries in xpath queries.
 * 
 * @author Bastiaan Verhoef
 *
 */
public class NestedXpathHandler extends AbstractXpathHandler {
	/**
	 * Xpath position relative to the match of this handler.
	 */
	private LinkedList<QName> xpathPosition = new LinkedList<QName>();
	/**
	 * Sub xpath handlers.
	 */
	private List<XpathHandler> handlers = new ArrayList<XpathHandler>();

	/**
	 * Contructor for queries like /ead/c/unitid
	 * @param defaultNamespace Default namespace of the xpath query
	 * @param xpathQueryArray xpath query
	 */
	public NestedXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);

	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#isAllTextBelow()
	 */
	@Override
	public boolean isAllTextBelow() {
		/*
		 * needed to process all content below.
		 */
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#processChildStartElementMatch(javax.xml.stream.XMLStreamReader)
	 */
	@Override
	public void processChildStartElementMatch(XMLStreamReader xmlReader) throws Exception {
		xpathPosition.add(xmlReader.getName());
		for (XpathHandler handler : handlers) {
			handler.processStartElement(xpathPosition, xmlReader);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#internalProcessCharacters(javax.xml.stream.XMLStreamReader)
	 */
	@Override
	protected void internalProcessCharacters(XMLStreamReader xmlReader) throws Exception {
		for (XpathHandler handler : handlers) {
			handler.processCharacters(xpathPosition, xmlReader);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#processChildEndElementMatch(javax.xml.stream.XMLStreamReader)
	 */
	@Override
	public void processChildEndElementMatch(XMLStreamReader xmlReader) throws Exception {
		for (XpathHandler handler : handlers) {
			handler.processEndElement(xpathPosition, xmlReader);
		}
		if (xpathPosition.size() > 0)
			xpathPosition.removeLast();

	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#processExactStartElementMatch(javax.xml.stream.XMLStreamReader)
	 */
	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader) {
		xpathPosition.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#processExactEndElementMatch()
	 */
	@Override
	protected void processExactEndElementMatch() {
		xpathPosition.clear();
	}

	/**
	 * Returns all the xpath handlers.
	 * 
	 * @return
	 */
	public List<XpathHandler> getHandlers() {
		return handlers;
	}

}
