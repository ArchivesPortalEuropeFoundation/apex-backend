package eu.archivesportaleurope.xml.xpath.handler;

import javax.xml.stream.XMLStreamReader;

/**
 *  Handler that return the number of occurrences of a xpath query.
 * 
 * @author Bastiaan Verhoef
 *
 */
public class CountXpathHandler extends AbstractXpathHandler {
	private int count = 0;

	/**
	 * Contructor for queries like count(/ead/c/unitid)
	 * @param defaultNamespace Default namespace of the xpath query
	 * @param xpathQueryArray xpath query
	 */
	public CountXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#processExactStartElementMatch(javax.xml.stream.XMLStreamReader)
	 */
	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader) {
		count++;
	}

	/**
	 * Return the number of occurrences.
	 * 
	 * @return Number of occurrences
	 */
	public int getCount() {
		return count;
	}

}
