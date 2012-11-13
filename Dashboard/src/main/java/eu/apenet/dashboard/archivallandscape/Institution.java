package eu.apenet.dashboard.archivallandscape;

import java.util.List;

public class Institution{
	public String name;
	public String id;
	public String level;
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
}
