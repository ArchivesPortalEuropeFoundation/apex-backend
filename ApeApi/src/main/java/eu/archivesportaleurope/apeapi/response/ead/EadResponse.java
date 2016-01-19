/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.archivesportaleurope.apeapi.common.fieldDef.EadFieldDefs;
import eu.archivesportaleurope.apeapi.utils.CommonUtils;
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
    private String scopeContent;
    private String scopeContentWithHighlighting;
//    private String other;
    private String fondsUnitTitle;
    private String fondsUnitId;
    private String repository;
    private String country;
    private String language;
    private String langMaterial;
//    private String otherUnitId;
    private String unitDate;
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
        this.scopeContent = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.SCOPE_CONTENT));
        if (response.getHighlighting().get(id).get(EadFieldDefs.SCOPE_CONTENT) != null) {
            this.scopeContentWithHighlighting = this.objectToString(response.getHighlighting().get(id).get(EadFieldDefs.SCOPE_CONTENT).get(0));
        } else {
            this.scopeContentWithHighlighting = this.scopeContent;
        }
        
//        this.other = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.OTHER));

        this.language = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.LANGUAGE));
        this.langMaterial = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.LANG_MATERIAL));
//        this.otherUnitId = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.OTHER_UNIT_ID));
        this.unitDate = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.ALTER_DATE));

        this.country = CommonUtils.splitByColon(this.objectToString(solrDocument.getFieldValue(EadFieldDefs.COUNTRY)), 0);

        this.fondsUnitTitle = CommonUtils.splitByColon(this.objectToString(solrDocument.getFieldValue(EadFieldDefs.FONDS_TITLE)), 0);
        this.fondsUnitId = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.FONDS_UNIT_ID));

        this.repository = CommonUtils.splitByColon(this.objectToString(solrDocument.getFieldValue(EadFieldDefs.AI)), 0);
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

    public String getLangMaterial() {
        return langMaterial;
    }

    public void setLangMaterial(String langMaterial) {
        this.langMaterial = langMaterial;
    }

    public String getUnitTitleWithHighlighting() {
        return unitTitleWithHighlighting;
    }

    public void setUnitTitleWithHighlighting(String unitTitleWithHighlighting) {
        this.unitTitleWithHighlighting = unitTitleWithHighlighting;
    }

    public String getScopeContent() {
        return scopeContent;
    }

    public void setScopeContent(String scopeContent) {
        this.scopeContent = scopeContent;
    }

    public String getScopeContentWithHighlighting() {
        return scopeContentWithHighlighting;
    }

    public void setScopeContentWithHighlighting(String scopeContentWithHighlighting) {
        this.scopeContentWithHighlighting = scopeContentWithHighlighting;
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

    public String getUnitDate() {
        return unitDate;
    }

    public void setUnitDate(String unitDate) {
        this.unitDate = unitDate;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }
}