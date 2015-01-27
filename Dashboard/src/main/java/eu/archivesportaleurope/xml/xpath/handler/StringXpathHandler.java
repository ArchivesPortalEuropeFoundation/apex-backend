package eu.archivesportaleurope.xml.xpath.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.archivesportaleurope.xml.ApeXmlUtil;
/**
 * Handler that return string values
 * 
 * @author Bastiaan Verhoef
 *
 */
public abstract class StringXpathHandler extends AbstractXpathHandler {

	private List<String> result = new ArrayList<String>();

	/**
	 * Contructor for queries like /ead/c/unitid
	 * 
	 * @param defaultNamespace Default namespace of the xpath query
	 * @param xpathQueryArray xpath query
	 */
	public StringXpathHandler(String defaultNamespace, String[] xpathQueryArray) {
		super(defaultNamespace, xpathQueryArray);
	}

	/**
	 * Returns the result of the xpath query.
	 * @return 
	 */
	public List<String> getResult() {
		return result;
	}

	/**
	 * Returns the result with unique values of the xpath query.
	 * @return 
	 */
	public Set<String> getResultSet() {
		return new HashSet<String>(result);
	}

	/**
	 * Returns only the first result of the xpath query.
	 * @return
	 */
	public String getFirstResult() {
		if (result.size() > 0) {
			return ApeXmlUtil.convertEmptyStringToNull(result.get(0));
		}
		return null;
	}
	
	/**
	 * Returns the result with unique values of the xpath query without the first one
	 * @return 
	 */
	public Set<String> getOtherResultSet() {
		Set<String> resultsSet = new HashSet<String>();
		for (int i = 1; i< result.size();i++){
			resultsSet.add(result.get(i));
		}
		return resultsSet;
	}
	
	
	public String getResultAsStringWithWhitespace(){
		return ApeXmlUtil.convertToString(result,0, ApeXmlUtil.WHITE_SPACE);	
	}
	public String getResultAsString(){
		return ApeXmlUtil.convertToString(result, 0, ApeXmlUtil.NO_SEPARATOR);	
	}
	
	public String getOtherResultsAsStringWithWhitespace(){
		return ApeXmlUtil.convertToString(result,1, ApeXmlUtil.WHITE_SPACE);	
	}
}
