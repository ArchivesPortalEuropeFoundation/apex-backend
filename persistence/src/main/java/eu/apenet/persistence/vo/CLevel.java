package eu.apenet.persistence.vo;

import java.io.Serializable;

public class CLevel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2806155070360797061L;
	private Long parentClId;
	private Long clId;
	private Integer orderId;
	private boolean leaf;
	private Long ecId;
	private EadContent eadContent;
	private String unittitle;
	private String unitid;
	private String level;
	private CLevel parent;
	private String hrefEadid;

	private String xml;

	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getUnittitle() {
		return unittitle;
	}
	public void setUnittitle(String unittitle) {
		this.unittitle = unittitle;
	}
    public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public CLevel getParent() {
		return parent;
	}
	public void setParent(CLevel parent) {
		this.parent = parent;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public Long getParentClId() {
		return parentClId;
	}
	public void setParentClId(Long parentClId) {
		this.parentClId = parentClId;
	}
	public Long getClId() {
		return clId;
	}
	public void setClId(Long clId) {
		this.clId = clId;
	}

	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public Long getEcId() {
		return ecId;
	}
	public void setEcId(Long ecId) {
		this.ecId = ecId;
	}
	public EadContent getEadContent() {
		return eadContent;
	}
	public void setEadContent(EadContent eadContent) {
		this.eadContent = eadContent;
	}
	public void setHrefEadid(String hrefEadid) {
		this.hrefEadid = hrefEadid;
	}
	public String getHrefEadid() {
		return hrefEadid;
	}
	
}
