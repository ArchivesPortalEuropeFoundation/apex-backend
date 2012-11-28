package eu.apenet.dashboard.archivallandscape;

import java.util.List;

public class Institution{
	public String name;
	public String id;
	public String level;
	private boolean group;
	private boolean containsEads;
	private int depth = 0;
	public List<Institution> institutions;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
	public List<Institution> getInstitutions() {
		return institutions;
	}
	
	public void setInstitutions(List<Institution> institutions) {
		this.institutions = institutions;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}

	public boolean isGroup() {
		return group;
	}

	public void setGroup(boolean group) {
		this.group = group;
	}

	public boolean isContainsEads() {
		return containsEads;
	}

	public void setContainsEads(boolean containsEads) {
		this.containsEads = containsEads;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	public String getCssClass(){
		String cssClass = null;
		if (group){
			cssClass = "aiGroup";
		}else {
			cssClass = "aiNormal";
		}
		if (containsEads){
			cssClass += " nodelete";
		}
		return cssClass;
	}
}
