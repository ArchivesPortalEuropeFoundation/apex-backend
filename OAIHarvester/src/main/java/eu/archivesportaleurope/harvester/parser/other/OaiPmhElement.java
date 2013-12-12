package eu.archivesportaleurope.harvester.parser.other;

import org.apache.commons.lang.StringUtils;


/**
 * User: Yoann Moranville
 * Date: 16/10/2013
 *
 * @author Yoann Moranville
 */
public class OaiPmhElement {
    private String element ="";
    private String description ="";
    public OaiPmhElement (){

    }
    public OaiPmhElement (String element){
    	this.element = element;
    }
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		if (element != null){
			this.element = element.replaceAll("[\n\r]", "").trim();
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		if (description != null){
			this.description = description.replaceAll("[\n\r]", "").trim();
		}
	}
	
	@Override
	public String toString() {
		String result = element;
		if (StringUtils.isNotBlank(description)){
			result += " (" + description + ")";
		}
		return result;
	}


}
