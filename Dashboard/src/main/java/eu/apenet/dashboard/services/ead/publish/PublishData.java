package eu.apenet.dashboard.services.ead.publish;

import java.util.List;
import java.util.Map;

public class PublishData {
	private String xml;
	private Long id;
	private Long parentId;
	private boolean leaf = true;
	private List<LevelInfo> upperLevelUnittitles;
	private Map<String, Object> fullHierarchy;
	private int orderId = 0;
	private boolean archdesc;
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long clId) {
		this.id = clId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public List<LevelInfo> getUpperLevelUnittitles() {
		return upperLevelUnittitles;
	}
	public void setUpperLevelUnittitles(List<LevelInfo> upperLevelUnittitles) {
		this.upperLevelUnittitles = upperLevelUnittitles;
	}

	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public Map<String, Object> getFullHierarchy() {
		return fullHierarchy;
	}
	public void setFullHierarchy(Map<String, Object> fullHierarchy) {
		this.fullHierarchy = fullHierarchy;
	}
	public boolean isArchdesc() {
		return archdesc;
	}
	public void setArchdesc(boolean archdesc) {
		this.archdesc = archdesc;
	}

}
