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
	private List<Map<String, List<String>>> results = new ArrayList<Map<String, List<String>>>();
	private Map<String, List<String>> tempResults;
	private String currentKey;
	private String temp = "";
	private boolean addWhitespaces;
	private String attributeValueAsKey;
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
	

	public void setAttributeValueAsKey(String attributeValueAsKey) {
		this.attributeValueAsKey = attributeValueAsKey;
	}
	@Override
	protected void processExactStartElementMatch(XMLStreamReader xmlReader) {
		tempResults = new HashMap<String, List<String>>();
	}
	@Override
	protected void processExactEndElementMatch() {
		results.add(tempResults);
		tempResults = null;
		currentKey=null;
	}

	@Override
	public void processChildStartElementMatch(XMLStreamReader xmlReader) {
		currentKey = xmlReader.getLocalName();
		if (StringUtils.isNotBlank(attributeValueAsKey)){ 
			String attrValue = xmlReader.getAttributeValue(null, attributeValueAsKey);
			if (StringUtils.isNotBlank(attrValue)){
				currentKey+= "@"+ attrValue;
			}
		}
	}

	@Override
	public void processChildEndElementMatch(XMLStreamReader xmlReader) {
		if (currentKey != null && StringUtils.isNotBlank(temp)){
			List<String> values = tempResults.get(currentKey);
			if (values == null){
				values = new ArrayList<String>();
				tempResults.put(currentKey, values);
			}
			values.add(temp);
		}
		currentKey = null;
		temp = "";
	}

	public List<Map<String, List<String>>> getResults() {
		return results;
	}

	public static Set<String> getResultSet(Map<String, List<String>> source, String key) {
		List<String> temp = source.get(key);
		if (temp == null){
			return new HashSet<String>();
		}else {
			return new LinkedHashSet<String>(temp);
		}
	}
	public static String getResultAsStringWithWhitespace(Map<String, List<String>> source, String[] keys, String separator){
		String result = null;
		for (String key: keys){
			 List<String> values = source.get(key);
			 if (values != null && values.size() > 0){
				String value = getResultAsStringWithWhitespaceWithKey(source, key);
				if (result == null){
					result = value;
				}else {
					result += separator + value;
				}
				
			 }
		}
		return result;
	}
	public static String getResultAsStringWithWhitespaceWithKey(Map<String, List<String>> source, String key){
		return convertToString(getResultSet(source, key),0, WHITE_SPACE);	
	}
	public static String getResultAsStringWithWhitespace(Map<String, List<String>> source, String separator){
		String result = null;
		for (String key: source.keySet()){
			String value = convertToString(getResultSet(source, key),0, WHITE_SPACE);
			if (result == null){
				result = value;
			}else {
				result += separator + value;
			}

		}
		return result;
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
