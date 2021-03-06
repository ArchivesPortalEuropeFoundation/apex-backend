/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.types.XmlType;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.utils.CommonUtils;
import io.swagger.annotations.ApiModelProperty;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author mahbub
 */
public class InstituteEadResponse {

    @ApiModelProperty(required = true, value = "Internal APE identifier of the result")
    protected String id;

    @ApiModelProperty(value = "Title of the finding aid. ")
    private String findingAidTitle;

    @ApiModelProperty(value = "Identifier of the fonds provided by the repository.")
    private String findingAidNo;

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
    private boolean hasDigitalObject = false;

    @ApiModelProperty(value = "Number of DAO")
    private int numberOfDigitalObjects = 0;

    @ApiModelProperty(value = "Number of DAO below")
    private int numberOfDigitalObjectsInDescendents = 0;

    @ApiModelProperty(value = "Number of Descendents")
    private int numberOfDescendents = 0;

    @ApiModelProperty(value = "Type of the description of the result: \"Descriptive Unit\", \"Finding Aid\", \"Holdings Guide\" or \"Source Guide\"")
    private String docType;

    @ApiModelProperty(value = "Id of the doc type")
    private String docTypeId;

    @ApiModelProperty(value = "Type of result: \"archdesc\" for highest level description or \"clevel\" for subordinate components")
    private String level;

    @ApiModelProperty(value = "Date of publication of this document")
    private String indexDate;

    public InstituteEadResponse(SolrDocument solrDocument, QueryResponse response) {
        this.id = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.ID));
        XmlType xmlType = XmlType.getTypeBySolrPrefix(this.id.substring(0, 1));
        if (xmlType == null) {
            if (this.id.startsWith(SolrValues.C_LEVEL_PREFIX)) {
                this.docType = "Descriptive Unit";
                this.level = "clevel";
                this.docTypeId = "du";
            } else {
                this.docType = "Unknown";
            }
        } else {
            this.docType = xmlType.getName();
            this.docTypeId = xmlType.getResourceName();
            this.level = "archdesc";
        }

        this.language = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.LANGUAGE));
        this.langMaterial = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.LANG_MATERIAL));
        this.unitDate = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.ALTERNATE_UNIT_DATE));

        this.country = CommonUtils.splitByColon(CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.COUNTRY)), 0);

        this.findingAidTitle = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.TITLE_PROPER));
        int lastIndexOfColon = this.findingAidTitle.lastIndexOf(":");

        if (lastIndexOfColon < 0) {
            throw new InternalErrorException("Illformated findingAidTitle", "fond don't have fonds id seperated by colon");
        }
        this.findingAidTitle = this.findingAidTitle.substring(0, lastIndexOfColon);

        this.findingAidNo = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.RECORD_ID));

        this.repository = CommonUtils.splitByColon(CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.AI)), 0);
        this.repositoryCode = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.REPOSITORY_CODE));
        this.indexDate = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.TIMESTAMP));
        if (solrDocument.getFieldValue(Ead3SolrFields.DAO) != null) {
            this.hasDigitalObject = (Boolean) solrDocument.getFieldValue(Ead3SolrFields.DAO);
        }
        Object dao = solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_DAO);
        if (dao != null) {
            this.numberOfDigitalObjects = Integer.parseInt(CommonUtils.objectToString(dao));
        }

        Object daoBelow = solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_DAO_BELOW);
        if (daoBelow != null) {
            this.numberOfDigitalObjectsInDescendents = Integer.parseInt(CommonUtils.objectToString(daoBelow));
        }

        Object des = solrDocument.getFieldValue(Ead3SolrFields.NUMBER_OF_DESCENDENTS);
        if (des != null) {
            this.numberOfDescendents = Integer.parseInt(CommonUtils.objectToString(des));
        }
    }

    /**
     * Default constructor
     */
    public InstituteEadResponse() {
        //Do nothing
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocTypeId() {
        return docTypeId;
    }

    public void setDocTypeId(String docTypeId) {
        this.docTypeId = docTypeId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getFindingAidTitle() {
        return findingAidTitle;
    }

    public void setFindingAidTitle(String findingAidTitle) {
        this.findingAidTitle = findingAidTitle;
    }

    public String getFindingAidNo() {
        return findingAidNo;
    }

    public void setFindingAidNo(String findingAidNo) {
        this.findingAidNo = findingAidNo;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
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

    public boolean isHasDigitalObject() {
        return hasDigitalObject;
    }

    public void setHasDigitalObject(boolean hasDigitalObject) {
        this.hasDigitalObject = hasDigitalObject;
    }

    public String getIndexDate() {
        return indexDate;
    }

    public void setIndexDate(String indexDate) {
        this.indexDate = indexDate;
    }

    public int getNumberOfDigitalObjects() {
        return numberOfDigitalObjects;
    }

    public void setNumberOfDigitalObjects(int numberOfDigitalObjects) {
        this.numberOfDigitalObjects = numberOfDigitalObjects;
    }

    public int getNumberOfDigitalObjectsInDescendents() {
        return numberOfDigitalObjectsInDescendents;
    }

    public void setNumberOfDigitalObjectsInDescendents(int numberOfDigitalObjectsInDescendents) {
        this.numberOfDigitalObjectsInDescendents = numberOfDigitalObjectsInDescendents;
    }

    public int getNumberOfDescendents() {
        return numberOfDescendents;
    }

    public void setNumberOfDescendents(int numberOfDescendents) {
        this.numberOfDescendents = numberOfDescendents;
    }

}
