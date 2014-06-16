package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public abstract class StringXpathHandler extends AbstractXpathHandler {

	private List<String> result = new ArrayList<String>();

	

	public StringXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}




	public List<String> getResult() {
		return result;
	}
	public Set<String> getResultSet() {
		return new HashSet<String>(result);
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
