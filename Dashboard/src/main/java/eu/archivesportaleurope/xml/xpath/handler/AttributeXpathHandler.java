package eu.archivesportaleurope.xml.xpath.handler;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
/**
 * Handler that return values of attributes
 * 
 * @author Bastiaan Verhoef
 *
 */
public class AttributeXpathHandler extends StringXpathHandler {
	protected static final String WHITE_SPACE = " ";
	private String attributeName = null;
	private String attributeNamespace = null;
	
	/**
	 * Contructor for queries like /ead/c/unitid@type
	 * 
	 * @param defaultNamespace Default namespace of the xpath query
	 * @param xpathQueryArray xpath query
	 * @param attributeName attribute name
	 */
	public AttributeXpathHandler(String defaultNamespace, String[] xpathQueryArray, String attributeName) {
		this(defaultNamespace, xpathQueryArray, null, attributeName);
	}

	/**
	 * Contructor for queries like /ead/c/unitid@link:href
	 * 
	 * @param defaultNamespace efault namespace of the xpath query
	 * @param xpathQueryArray xpath query
	 * @param attributeNamespace namespace of the attribute
	 * @param attributeName name of the attribute
	 */
	public AttributeXpathHandler(String defaultNamespace, String[] xpathQueryArray, String attributeNamespace, String attributeName) {
		super(defaultNamespace, xpathQueryArray);
		this.attributeName = attributeName;
		this.attributeNamespace = attributeNamespace;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#processExactStartElementMatch(javax.xml.stream.XMLStreamReader)
	 */
	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader){
		String attributeValue = xmlReader.getAttributeValue(attributeNamespace, attributeName);
		/*
		 * if there is an match, this method is called.
		 * And if the attribute value is not null, the value is added to the result.
		 */
		if (StringUtils.isNotBlank(attributeValue)){
			getResult().add(attributeValue.trim());
		}
	}

	
}
