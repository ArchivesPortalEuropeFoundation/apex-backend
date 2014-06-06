package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

public abstract class StringXpathHandler extends AbstractXpathHandler {
	public static final String WHITE_SPACE = " ";
	private List<String> result = new ArrayList<String>();

	

	public StringXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}


	protected static String removeUnusedCharacters(String input){
		if (input != null){
			String result = input.replaceAll("[\t ]+", " ");
			result = result.replaceAll("[\n\r]+", "");
			return result;
		}else {
			return null;
		}
		
	}

	public List<String> getResult() {
		return result;
	}
	public String getResultAsString(){
		StringBuilder builder = new StringBuilder();
		for (String item: result){
			builder.append(item);
		}
		return builder.toString().trim();
	}

	public String getResultAsStringWithWhitespace(){
		StringBuilder builder = new StringBuilder();
		for (String item: result){
			builder.append(item + WHITE_SPACE);
		}
		return builder.toString().trim();
	}
	public String getFirstResult(){
		if (result.size() > 0){
			return result.get(0).trim();
		}
		return null;
	}
	
	public String getOtherResultsAsStringWithWhitespace(){
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < result.size(); i++){
			builder.append(result.get(i) + WHITE_SPACE);
		}
		return builder.toString().trim();
	}
	
	@Override
	protected void clear() {
		result.clear();
		
	}




	
}
