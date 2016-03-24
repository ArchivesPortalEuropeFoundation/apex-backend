/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;

/**
 * 
 * @author kaisar
 */
public class OverViewFrontPageContent {

    private XmlType xmlType;
    private EadContent eadContent;
    private int aiId;
    private String eadId;
    private String xmlTypeName;
    private CLevel currentLevel;
    private String aiRepoCode;

    public XmlType getXmlType() {
        return xmlType;
    }

    public void setXmlType(XmlType xmlType) {
        this.xmlType = xmlType;
    }

    public EadContent getEadContent() {
        return eadContent;
    }

    public void setEadContent(EadContent eadContent) {
        this.eadContent = eadContent;
    }

    public int getAiId() {
        return aiId;
    }

    public void setAiId(int aiId) {
        this.aiId = aiId;
    }

    public String getEadId() {
        return eadId;
    }

    public void setEadId(String eadId) {
        this.eadId = eadId;
    }

    public String getXmlTypeName() {
        return xmlTypeName;
    }

    public void setXmlTypeName(String xmlTypeName) {
        this.xmlTypeName = xmlTypeName;
    }

    public CLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(CLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getAiRepoCode() {
        return aiRepoCode;
    }

    public void setAiRepoCode(String aiRepoCode) {
        this.aiRepoCode = aiRepoCode;
    }

}
