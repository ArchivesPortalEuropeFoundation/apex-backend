package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

public class TextMapXpathHandler extends AbstractXpathHandler {
	private Map<String, List<String>> results = new HashMap<String, List<String>>();
	private String currentKey = null;
	private String temp = "";
	private boolean addWhitespaces;
	public TextMapXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}
	public TextMapXpathHandler(String defaultNamespace, String[] xpathQueryArray, boolean addWhitespaces) {
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
		currentKey=null;
	}

	@Override
	public void processChildStartElementMatch(XMLStreamReader xmlReader) {
		currentKey = xmlReader.getLocalName();
	}

	@Override
	public void processChildEndElementMatch(XMLStreamReader xmlReader) {
		if (currentKey != null && StringUtils.isNotBlank(temp)){
			List<String> values = results.get(currentKey);
			if (values == null){
				values = new ArrayList<String>();
				results.put(currentKey, values);
			}
			values.add(temp);
		}
		currentKey = null;
		temp = "";
	}

	public Map<String, List<String>> getResults() {
		return results;
	}

	public Set<String> getResultSet(String key) {
		List<String> temp = results.get(key);
		if (temp == null){
			return new HashSet<String>();
		}else {
			return new LinkedHashSet<String>(temp);
		}
	}
	public String getResultAsStringWithWhitespace(String key){
		return convertToString(getResultSet(key),0, WHITE_SPACE);	
	}
	@Override
	protected void clear() {
		results.clear();
		
	}
	@Override
	public boolean isAllTextBelow() {
		return true;
	}
}
