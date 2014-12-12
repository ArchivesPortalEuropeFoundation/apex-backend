package eu.archivesportaleurope.xml.xpath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public Set<String> getOtherResultSet() {
		if (result.size() > 1){
			Set<String> temp =  new HashSet<String>(result);
			temp.remove(0);
			return temp;
		}
		return new HashSet<String>(); 
	}
	public String getResultAsString(){
		return convertToString(result, 0, NO_SEPARATOR);	
	}

	public String getResultAsStringWithWhitespace(){
		return convertToString(result,0, WHITE_SPACE);	
	}
	public String getFirstResult(){
		if (result.size() > 0){
			return convertEmptyStringToNull(result.get(0));
		}
		return null;
	}
	
	public String getOtherResultsAsStringWithWhitespace(){
		return convertToString(result,1, WHITE_SPACE);	
	}

	@Override
	protected void clear() {
		result.clear();
		
	}




	
}
