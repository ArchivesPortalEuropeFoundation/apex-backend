/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.common.fieldDef.EadFieldDefs;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class EadResponse {
    private String id;
    private String unitId;
    private String unitTitle;
    private String unitTitleWithHighlighting;
    private String scopecontent;
    private String other;
    private String fondsUnitTitle;
    private String fondsUnitId;
    private String repository;
    private String unitidForLink;
    private String country;
    private String language;
    private String langmaterial;
    private String otherUnitid;
    private String unitDate;
    private String level;
    private String repositoryCode;

    public EadResponse(SolrDocument solrDocument, QueryResponse response) {
        this.id = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.ID));
        this.unitTitle = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.TITLE));
        if (response.getHighlighting().get(id).get(EadFieldDefs.TITLE) != null) {
            this.unitTitleWithHighlighting = this.objectToString(response.getHighlighting().get(id).get(EadFieldDefs.TITLE).get(0));
        } else {
            this.unitTitleWithHighlighting = this.unitTitle;
        }
        this.unitId = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.UNIT_ID));
        this.scopecontent = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.SCOPE_CONTENT));
        this.other = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.OTHER));
        
        this.language = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.LANGUAGE));
        this.langmaterial = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.LANG_MATERIAL));
        this.otherUnitid = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.OTHER_UNIT_ID));
        this.unitDate = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.ALTER_DATE));
        this.level = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.LEVEL));
        
        this.country = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.COUNTRY));
        
        this.fondsUnitTitle = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.FOND));
        this.fondsUnitId = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.UNIT_ID_FOND));
        
        this.repository = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.AI));
        this.repositoryCode = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.REPOSITORY_CODE));
    }

    public EadResponse() {
    }

    private String objectToString(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLangmaterial() {
        return langmaterial;
    }

    public void setLangmaterial(String langmaterial) {
        this.langmaterial = langmaterial;
    }

    public String getUnitTitleWithHighlighting() {
        return unitTitleWithHighlighting;
    }

    public void setUnitTitleWithHighlighting(String unitTitleWithHighlighting) {
        this.unitTitleWithHighlighting = unitTitleWithHighlighting;
    }

    public String getScopecontent() {
        return scopecontent;
    }

    public void setScopecontent(String scopecontent) {
        this.scopecontent = scopecontent;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getFondsUnitTitle() {
        return fondsUnitTitle;
    }

    public void setFondsUnitTitle(String fondsUnitTitle) {
        this.fondsUnitTitle = fondsUnitTitle;
    }

    public String getFondsUnitId() {
        return fondsUnitId;
    }

    public void setFondsUnitId(String fondsUnitId) {
        this.fondsUnitId = fondsUnitId;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitidForLink() {
        return unitidForLink;
    }

    public void setUnitidForLink(String unitidForLink) {
        this.unitidForLink = unitidForLink;
    }

    public String getOtherUnitid() {
        return otherUnitid;
    }

    public void setOtherUnitid(String otherUnitid) {
        this.otherUnitid = otherUnitid;
    }

    public String getUnitDate() {
        return unitDate;
    }

    public void setUnitDate(String unitDate) {
        this.unitDate = unitDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }
}
