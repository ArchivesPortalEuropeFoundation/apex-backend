package eu.archivesportaleurope.xml.xpath;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

public class TextXpathHandler extends StringXpathHandler {
	private String temp = null;
	private boolean addWhitespaces;
	public TextXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}
	public TextXpathHandler(String defaultNamespace, String[] xpathQueryArray, boolean addWhitespaces) {
		super(defaultNamespace, xpathQueryArray);
		this.addWhitespaces = addWhitespaces;
	}

	@Override
	protected void writeContent(String content) {
		if (StringUtils.isNotBlank(content)){
			temp += removeUnusedCharacters(content);
			if (addWhitespaces){
				temp += WHITE_SPACE;
			}
		}

	}
	@Override
	protected void processExactEndElementMatch() {
		if (StringUtils.isNotBlank(temp)){
			getResult().add(temp);
		}
		temp=null;
	}


	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader){
		temp="";
	}
}
