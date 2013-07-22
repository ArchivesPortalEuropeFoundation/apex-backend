package eu.apenet.dashboard.infraestructure;

import java.util.HashMap;
import java.util.Map;

public class ArchivalLandscapeNode {

	int nodeId;
	Map<String, String> names = new HashMap<String,String>();
	Map<String, Boolean> primaryName = new HashMap<String,Boolean>();
	String parent_name;
	Boolean is_group;
	String internal_al_id;
	String parent_internal_al_id;
	
	public String getInternal_al_id() {
		return internal_al_id;
	}
	public void setInternal_al_id(String internal_al_id) {
		this.internal_al_id = internal_al_id;
	}
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public Map<String, Boolean> getPrimaryName() {
		return primaryName;
	}
	public void setPrimaryName(Map<String, Boolean> primaryName) {
		this.primaryName = primaryName;
	}
	public Map<String, String> getNames() {
		return names;
	}
	public void setNames(Map<String, String> names) {
		this.names = names;
	}
	public Boolean getIs_group() {
		return is_group;
	}
	public void setIs_group(Boolean is_group) {
		this.is_group = is_group;
	}
	public String getParent_name() {
		return parent_name;
	}
	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}	
	public String getParent_internal_al_id() {
		return parent_internal_al_id;
	}
	public void setParent_internal_al_id(String parent_internal_al_id) {
		this.parent_internal_al_id = parent_internal_al_id;
	}	
	
	public ArchivalLandscapeNode(int id, Map<String, String> names, Boolean is_group, String parent_name, String internal_al_id, String parent_internal_al_id, Map<String, Boolean> primaryName) {
		super();
		this.setNodeId(id);
		this.setNames(names); 
		this.setParent_name(parent_name); 
		this.setIs_group(is_group);
		this.setInternal_al_id(internal_al_id);
		this.setParent_internal_al_id(parent_internal_al_id);
		this.setPrimaryName(primaryName);
	}
	
	
	
	
	
}
