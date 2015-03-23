package eu.archivesportaleurope.xml.xpath.handler;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

import eu.archivesportaleurope.xml.ApeXmlUtil;
/**
 * Handler that return texts of xml elements and mixed context based on xpath query.
 * 
 * @author Bastiaan Verhoef
 *
 */
public class TextXpathHandler extends StringXpathHandler {
	private String temp = null;
	private boolean addWhitespaces;
	private boolean convertToLowerCase;
	/**
	 * Contructor for queries like /ead/c/unitid
	 * 
	 * @param defaultNamespace Default namespace of the xpath query
	 * @param xpathQueryArray xpath query
	 */
	public TextXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}
	/**
	 * Contructor for queries like /ead/c/unitid, that add whitespaces for mixed content. 
	 * unitid<p>sub</p>one = unitid sub one
	 * 
	 * @param defaultNamespace Default namespace of the xpath query
	 * @param xpathQueryArray xpath query
	 * @param addWhitespaces Add whitespaces for mixed content
	 */
	public TextXpathHandler(String defaultNamespace, String[] xpathQueryArray, boolean addWhitespaces) {
		super(defaultNamespace, xpathQueryArray);
		this.addWhitespaces = addWhitespaces;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#writeContent(java.lang.String)
	 */
	@Override
	protected void writeContent(String content) {
		if (StringUtils.isNotBlank(content)){
			temp += ApeXmlUtil.removeUnusedCharacters(content);
			if (addWhitespaces){
				temp += ApeXmlUtil.WHITE_SPACE;
			}
		}

	}
	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#processExactEndElementMatch()
	 */
	@Override
	protected void processExactEndElementMatch() {
		if (StringUtils.isNotBlank(temp)){
			if (convertToLowerCase){
				getResult().add(temp.toLowerCase());
			}else {
				getResult().add(temp);
			}
		}
		temp=null;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.archivesportaleurope.xml.xpath.handler.AbstractXpathHandler#processExactStartElementMatch(javax.xml.stream.XMLStreamReader)
	 */
	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader){
		temp="";
	}
	public void setConvertToLowerCase(boolean convertToLowerCase) {
		this.convertToLowerCase = convertToLowerCase;
	}
	
}
