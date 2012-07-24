package eu.apenet.dashboard.archivallandscape;

import java.util.ArrayList;

public class Institution{
	public String name;
	public String id;
	public String level;
	public ArrayList<Institution> institutions;
	
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
	
	public ArrayList<Institution> getInstitutions() {
		return institutions;
	}
	
	public void setInstitutions(ArrayList<Institution> institutions) {
		this.institutions = institutions;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}
}
