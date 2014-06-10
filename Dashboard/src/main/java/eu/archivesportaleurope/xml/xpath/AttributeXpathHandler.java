package eu.archivesportaleurope.xml.xpath;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

public class AttributeXpathHandler extends StringXpathHandler {
	protected static final String WHITE_SPACE = " ";
	private String attributeName = null;
	private String attributeNamespace = null;
	

	public AttributeXpathHandler(String defaultNamespace, String[] xpathQueryArray, String attributeName) {
		this(defaultNamespace, xpathQueryArray, null, attributeName);
	}

	public AttributeXpathHandler(String defaultNamespace, String[] xpathQueryArray, String attributeNamespace, String attributeName) {
		super(defaultNamespace, xpathQueryArray);
		this.attributeName = attributeName;
		this.attributeNamespace = attributeNamespace;
	}

	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader){
		String attributeValue = xmlReader.getAttributeValue(attributeNamespace, attributeName);
		if (StringUtils.isNotBlank(attributeValue)){
			getResult().add(attributeValue.trim());
		}
	}

	
}
