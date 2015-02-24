package eu.apenet.dashboard.manual.eag;

import java.util.HashMap;
import java.util.List;

/**
 * Class used to managed a dynamic structure for EAG2012
 */
public class Eag2012Node {
	
	String nodeName =  null;
	HashMap<String,String> attributes = null;
	List<Eag2012Node> children = null;
	String value = null;
	
	public HashMap<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}
	public List<Eag2012Node> getChildren() {
		return children;
	}
	public void setChildren(List<Eag2012Node> children) {
		this.children = children;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
}
