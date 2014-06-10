package eu.apenet.dashboard.services.ead.xml.stream.publish;

import java.util.HashSet;
import java.util.Set;

import eu.apenet.dashboard.services.ead.publish.PublishData;

public class EadPublishData extends PublishData {

	private String unitid;
	private String otherUnitid;
	private String firstUnittitle;
	private String scopecontent;
	private String otherinfo;
	private String unitdate;
	private String unitdateNormal;
	private String langmaterial;
	private int numberOfDaos = 0;
	private String level;
	private Set<String> roledao = new HashSet<String>();
	private String globalLanguage;
	
	public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	public String getOtherUnitid() {
		return otherUnitid;
	}
	public void setOtherUnitid(String otherUnitid) {
		this.otherUnitid = otherUnitid;
	}
	public String getFirstUnittitle() {
		return firstUnittitle;
	}
	public void setFirstUnittitle(String firstUnittitle) {
		this.firstUnittitle = firstUnittitle;
	}
	public String getScopecontent() {
		return scopecontent;
	}
	public void setScopecontent(String scopecontent) {
		this.scopecontent = scopecontent;
	}
	public String getOtherinfo() {
		return otherinfo;
	}
	public void setOtherinfo(String otherinfo) {
		this.otherinfo = otherinfo;
	}
	public String getUnitdate() {
		return unitdate;
	}
	public void setUnitdate(String unitdate) {
		this.unitdate = unitdate;
	}
	public String getUnitdateNormal() {
		return unitdateNormal;
	}
	public void setUnitdateNormal(String unitdateNormal) {
		this.unitdateNormal = unitdateNormal;
	}
	public int getNumberOfDaos() {
		return numberOfDaos;
	}
	public void setNumberOfDaos(int numberOfDaos) {
		this.numberOfDaos = numberOfDaos;
	}
	public String getLangmaterial() {
		return langmaterial;
	}
	public void setLangmaterial(String langmaterial) {
		this.langmaterial = langmaterial;
	}
	public Set<String> getRoledao() {
		return roledao;
	}
	public void setRoledao(Set<String> roledao) {
		this.roledao = roledao;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getGlobalLanguage() {
		return globalLanguage;
	}
	public void setGlobalLanguage(String globalLanguage) {
		this.globalLanguage = globalLanguage;
	}
	@Override
	public String toString() {
		return "EadPublishData [unitid=" + unitid + ", otherUnitid=" + otherUnitid + ", firstUnittitle="
				+ firstUnittitle + ", scopecontent=" + scopecontent + ", otherinfo=" + otherinfo + ", unitdate="
				+ unitdate + ", unitdateNormal=" + unitdateNormal + ", langmaterial=" + langmaterial
				+ ", numberOfDaos=" + numberOfDaos + ", level=" + level + ", roledao=" + roledao + ", globalLanguage="
				+ globalLanguage + "]";
	}


	
}
