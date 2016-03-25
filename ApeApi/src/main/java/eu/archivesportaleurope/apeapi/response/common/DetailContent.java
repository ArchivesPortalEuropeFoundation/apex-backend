/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;

public class DetailContent {

    private XmlType xmlType;
    private String xml;
    private EadContent eadContent;
    private CLevel currentLevel;
    private ArchivalInstitution archivalInstitution;
    private Ead ead;

    public DetailContent(EadContent eadContent) {
        this.eadContent = eadContent;
        this.currentLevel = null;
        this.ead = this.eadContent.getEad();
        this.archivalInstitution = this.ead.getArchivalInstitution();
        this.xmlType = XmlType.getContentType(ead);
        this.xml = eadContent.getXml().replaceAll("[\t\n\r]", "").replaceAll(">\\s+<", "><");
    }

    public DetailContent(CLevel currentLevel) {
        this.currentLevel = currentLevel;
        this.eadContent = this.currentLevel.getEadContent();
        this.ead = this.eadContent.getEad();
        this.archivalInstitution = this.ead.getArchivalInstitution();
        this.xmlType = XmlType.getContentType(ead);
        this.xml = currentLevel.getXml().replaceAll("[\t\n\r]", "").replaceAll(">\\s+<", "><");
    }
    
    public XmlType getXmlType() {
        return xmlType;
    }

    public void setXmlType(XmlType xmlType) {
        this.xmlType = xmlType;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }
    
    public boolean isClevelType() {
        return this.currentLevel != null;
    }
    
    public String getXml() {
        return this.xml;
    }

    public int getAiId() {
        return archivalInstitution.getAiId();
    }

    public String getEadId() {
        return ead.getEadid();
    }

    public String getAiRepoCode() {
        return archivalInstitution.getEncodedRepositorycode();
    }
    
    public String getAiRepoName() {
        return archivalInstitution.getAiname();
    }

    public String getUnitId() {
        return this.currentLevel == null ? this.eadContent.getEadid() : this.currentLevel.getUnitid();
    }

    public String getUnitTitle() {
        return this.currentLevel == null ? this.eadContent.getUnittitle() : this.currentLevel.getUnittitle();
    }
}
