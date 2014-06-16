package eu.archivesportaleurope.xml.xpath;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

public class TextXpathHandler extends StringXpathHandler {
	private String temp = null;
	public TextXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}

	@Override
	protected void writeContent(String content) {
		if (StringUtils.isNotBlank(content)){
			temp += removeUnusedCharacters(content);
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
