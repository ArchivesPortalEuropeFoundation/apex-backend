/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.vo.CLevel;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class OverViewResponse {

    private XmlType xmlType;
    private CLevel currentLevel;
    private int aiId;
    private String aiRepoCode;
    private String eadId;

    public XmlType getXmlType() {
        return xmlType;
    }

    public void setXmlType(XmlType xmlType) {
        this.xmlType = xmlType;
    }

    public CLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(CLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getAiId() {
        return aiId;
    }

    public void setAiId(int aiId) {
        this.aiId = aiId;
    }

    public String getAiRepoCode() {
        return aiRepoCode;
    }

    public void setAiRepoCode(String aiRepoCode) {
        this.aiRepoCode = aiRepoCode;
    }

    public String getEadId() {
        return eadId;
    }

    public void setEadId(String eadId) {
        this.eadId = eadId;
    }

}
