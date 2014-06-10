package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public abstract class StringXpathHandler extends AbstractXpathHandler {
	private static final String NO_SEPARATOR = "";
	public static final String WHITE_SPACE = " ";
	private List<String> result = new ArrayList<String>();

	

	public StringXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}


	protected static String removeUnusedCharacters(String input){
		if (input != null){
			String result = input.replaceAll("[\t ]+", " ");
			result = result.replaceAll("[\n\r]+", NO_SEPARATOR);
			return result;
		}else {
			return null;
		}
		
	}

	public List<String> getResult() {
		return result;
	}
	public String getResultAsString(){
		return convertToString(0, NO_SEPARATOR);	
	}

	public String getResultAsStringWithWhitespace(){
		return convertToString(0, WHITE_SPACE);	
	}
	public String getFirstResult(){
		if (result.size() > 0){
			return convertEmptyStringToNull(result.get(0));
		}
		return null;
	}
	
	public String getOtherResultsAsStringWithWhitespace(){
		return convertToString(1, WHITE_SPACE);	
	}
	private String convertToString(int start, String separator){
		if (result.size() == 0){
			return null;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = start; i < result.size(); i++){
			builder.append(result.get(i) + separator);
		}
		return convertEmptyStringToNull(builder.toString());
	}
	private String convertEmptyStringToNull(String string){
		if (StringUtils.isBlank(string)){
			return null;
		}else {
			return string.trim();
		}
	}
	@Override
	protected void clear() {
		result.clear();
		
	}




	
}
