package eu.archivesportaleurope.harvester.oaipmh.portugal.objects;

/**
 * User: yoannmoranville
 * Date: 27/01/14
 *
 * @author yoannmoranville
 */

import java.io.Serializable;

public class CLevel implements Serializable {
    private static final long serialVersionUID = 2806151070360797061L;

    private Long clId;
    private Integer orderId;
    private Long ecId;
    private String unitid;
    private String level;
    private Long parentClId;
    private String xml;

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
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
}

