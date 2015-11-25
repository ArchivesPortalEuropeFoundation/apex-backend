/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.backend.apeapi.entity.ead;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author kaisar
 */
public class EadSearchResult {

    private static final char COLON = ':';
    private String id;
    private String title;
    private String titleWithoutHighlighting;
    private String scopecontent;
    private String other;
    private String fond;
    private String fondId;
    private String ai;
    private String aiId;
    private String unitid;
    private String unitidForLink;
    private String country;
    private String language;
    private String langmaterial;
    private String otherUnitid;
    private String alterdate;
    private String alterdateWithoutHighlighting;
    private String level;
    private String repositoryCode;
    private transient SolrDocument solrDocument;

    public EadSearchResult(SolrDocument solrDocument, QueryResponse response) {
        this.solrDocument = solrDocument;
        this.id = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.ID));
        this.titleWithoutHighlighting = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.TITLE));
        this.title = this.objectToString(response.getHighlighting().get(id).get(EadFieldDefs.TITLE).get(0));
        this.ai = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.AI));
        this.scopecontent = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.SCOPE_CONTENT));
        this.other = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.OTHER));
        this.country = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.COUNTRY));
        this.language = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.LANGUAGE));
        this.langmaterial = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.LANG_MATERIAL));
        this.fond = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.FOND));
        this.fondId = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.UNIT_ID_FOND));
        this.otherUnitid = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.OTHER_UNIT_ID));
        this.alterdate = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.ALTER_DATE));
        this.level = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.LEVEL));
        this.repositoryCode = this.objectToString(solrDocument.getFieldValue(EadFieldDefs.REPOSITORY_CODE));
    }

    public EadSearchResult() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getTitleWithoutHighlighting() {
        return titleWithoutHighlighting;
    }

    public void setTitleWithoutHighlighting(String titleWithoutHighlighting) {
        this.titleWithoutHighlighting = titleWithoutHighlighting;
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

    public String getFond() {
        return fond;
    }

    public void setFond(String fond) {
        this.fond = fond;
    }

    public String getFondId() {
        return fondId;
    }

    public void setFondId(String fondId) {
        this.fondId = fondId;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getAiId() {
        return aiId;
    }

    public void setAiId(String aiId) {
        this.aiId = aiId;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
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

    public String getAlterdate() {
        return alterdate;
    }

    public void setAlterdate(String alterdate) {
        this.alterdate = alterdate;
    }

    public String getAlterdateWithoutHighlighting() {
        return alterdateWithoutHighlighting;
    }

    public void setAlterdateWithoutHighlighting(String alterdateWithoutHighlighting) {
        this.alterdateWithoutHighlighting = alterdateWithoutHighlighting;
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

    public SolrDocument getSolrDocument() {
        return solrDocument;
    }

    public void setSolrDocument(SolrDocument solrDocument) {
        if (solrDocument != null) {
            this.solrDocument = solrDocument;
        }
    }
}
