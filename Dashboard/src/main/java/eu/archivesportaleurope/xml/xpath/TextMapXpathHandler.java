package eu.archivesportaleurope.xml.xpath;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

public class TextMapXpathHandler extends AbstractXpathHandler {
	private Map<String, String> results = new HashMap<String, String>();
	private String currentKey = null;

	public TextMapXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}

	@Override
	protected void writeContent(String content) {
		if (StringUtils.isNotBlank(content) && currentKey != null){
			String item = results.get(currentKey);
			if (StringUtils.isBlank(item)){
				item = removeUnusedCharacters(content);
			}else {
				item += removeUnusedCharacters(content);
			}
			results.put(currentKey, item);
		}
		
	}
	@Override
	protected void processExactEndElementMatch() {
		currentKey=null;
	}



	@Override
	public void processChildStartElementMatch(XMLStreamReader xmlReader) {
		currentKey = xmlReader.getLocalName();
	}

	@Override
	public void processChildEndElementMatch(XMLStreamReader xmlReader) {
		currentKey = null;
	}

	public Map<String, String> getResults() {
		return results;
	}

	@Override
	protected void clear() {
		results.clear();
		
	}
	
}
