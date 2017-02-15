/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead3;

import eu.apenet.commons.solr.Ead3SolrFields;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class InstituteEad3Response {

    @ApiModelProperty(required = true, value = "Internal APE identifier of the result")
    protected String id;

    @ApiModelProperty(value = "Title of the finding aid. ")
    private String titleProper;

    @ApiModelProperty(value = "Identifier of the fonds provided by the repository.")
    private String recordId;

    @ApiModelProperty(value = "Name of the repository holding the fonds")
    private String repository;

    @ApiModelProperty(value = "Name of the country where the repository is. In English. ")
    private String country;

    @ApiModelProperty(value = "Language of the description of the result.")
    private String language;

    @ApiModelProperty(value = "Language in which the result is created.")
    private String langMaterial;

    @ApiModelProperty(value = "Date of creation of the result.")
    private String unitDate;

    @ApiModelProperty(value = "Code of the repository holding the fonds. Preferably, but not necessarily <a target='_blank' href='https://en.wikipedia.org/wiki/International_Standard_Identifier_for_Libraries_and_Related_Organizations'>ISIL</a>")
    private String repositoryCode;

    @ApiModelProperty(value = "True if the unit has one or more digital object")
    private String numberOfDigitalObject = "0";

    @ApiModelProperty(value = "Type of result: \"archdesc\" for highest level description or \"clevel\" for subordinate components")
    private String level;

    @ApiModelProperty(value = "Date of publication of this document")
    private String indexDate;

    public InstituteEad3Response(SolrDocument solrDocument, QueryResponse response) {
        this.id = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.ID));

        this.level = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.LEVEL_NAME));

        this.language = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.LANGUAGE));
        this.langMaterial = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.LANG_MATERIAL));
        this.unitDate = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_DATE));

        this.country = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.COUNTRY_NAME));

        this.titleProper = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.TITLE_PROPER));
        this.recordId = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.RECORD_ID));

        this.repository = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.AI_NAME));
        this.repositoryCode = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.REPOSITORY_CODE));
        this.indexDate = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.TIMESTAMP));
        this.numberOfDigitalObject = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_DAO));
    }

    public InstituteEad3Response() {
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

    public String getTitleProper() {
        return titleProper;
    }

    public void setTitleProper(String titleProper) {
        this.titleProper = titleProper;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
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

    public String getNumberOfDigitalObject() {
        return numberOfDigitalObject;
    }

    public void setNumberOfDigitalObject(String numberOfDigitalObject) {
        this.numberOfDigitalObject = numberOfDigitalObject;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIndexDate() {
        return indexDate;
    }

    public void setIndexDate(String indexDate) {
        this.indexDate = indexDate;
    }

}
