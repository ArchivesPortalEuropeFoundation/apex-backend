/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.apenet.commons.types.XmlType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class OverViewResponse {
    private String id;
    private XmlType xmlType;
    private String overViewXml;
    private String unitId;
    private String unitTitle;
    private int aiId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public XmlType getXmlType() {
        return xmlType;
    }

    public void setXmlType(XmlType xmlType) {
        this.xmlType = xmlType;
    }

    public String getOverViewXml() {
        return overViewXml;
    }

    public void setOverViewXml(String overViewXml) {
        this.overViewXml = overViewXml;
    }

    public int getAiId() {
        return aiId;
    }

    public void setAiId(int aiId) {
        this.aiId = aiId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }
}
