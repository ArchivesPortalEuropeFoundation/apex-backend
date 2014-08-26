package eu.apenet.dashboard.services.ead.xml.stream.mets.xpath;

import org.apache.commons.lang.StringUtils;

public class MetsFile {

	private String id;
	private String role;
	private String use;
	private String href;
	
	public MetsFile(String id, String use){
		this.id = id;
		this.use = use;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}

	public boolean isValid(){
		return StringUtils.isNotBlank(id) && "DISPLAY".equals(use);
	}
}
