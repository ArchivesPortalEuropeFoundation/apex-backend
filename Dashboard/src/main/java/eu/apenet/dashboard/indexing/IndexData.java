package eu.apenet.dashboard.indexing;

import java.util.List;
import java.util.Map;

public class IndexData {
	private String xml;
	private Long clId;
	private Long parentId;
	private boolean leaf;
	private List<LevelInfo> upperLevelUnittitles;
	private Map<String, Object> fullHierarchy;
	private int orderId;
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public Long getClId() {
		return clId;
	}
	public void setClId(Long clId) {
		this.clId = clId;
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

}
