/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.eaccpf;

import com.fasterxml.jackson.annotation.JsonInclude;
import eu.apenet.commons.solr.SolrFields;
import eu.archivesportaleurope.apeapi.utils.CommonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class EacCpfResponse {

    @ApiModelProperty(required = true, value = "Internal APE identifier of the result")
    private String id;

    @ApiModelProperty(value = "")
    private String recordId;

    @ApiModelProperty(value = "")
    private String entityType;

    @ApiModelProperty(value = "")
    private List<String> nameEntries;

    @ApiModelProperty(value = "")
    private List<String> nameEntriesWithHighlighting;

    @ApiModelProperty(value = "")
    private String existDates;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private String descriptionWithHighlighting;

    @ApiModelProperty(value = "")
    private String other;

    @ApiModelProperty(value = "")
    private String otherWithHighlighting;

    @ApiModelProperty(value = "")
    private String repository;

    @ApiModelProperty(value = "")
    private String repositoryCode;

    @ApiModelProperty(value = "")
    private String country;

    @ApiModelProperty(value = "")
    private String countryId;

    @ApiModelProperty(value = "")
    private String numberOfArchivalMaterialRelations;

    @ApiModelProperty(value = "")
    private String numberOfNameRelations;

    public EacCpfResponse(SolrDocument solrDocument, QueryResponse response) {
        this.id = this.objectToString(solrDocument.getFieldValue(SolrFields.ID));
        Map<String, List<String>> highlightMap = response.getHighlighting().get(id);
        this.recordId = this.objectToString(solrDocument.getFieldValue(SolrFields.EAC_CPF_RECORD_ID));
        this.entityType = this.objectToString(solrDocument.getFieldValue(SolrFields.EAC_CPF_FACET_ENTITY_TYPE));
        this.nameEntries = solrDocument.getFieldValues(SolrFields.EAC_CPF_NAMES)
                .stream().map(object -> this.objectToString(object)).collect(Collectors.toList());
        this.nameEntriesWithHighlighting = highlightMap.get(SolrFields.EAC_CPF_NAMES);
        this.existDates = this.objectToString(solrDocument.getFieldValue(SolrFields.EAC_CPF_DATE_DESCRIPTION));
        this.description = this.objectToString(solrDocument.getFieldValue(SolrFields.EAC_CPF_DESCRIPTION));
        List<String> descriptionHighlight = highlightMap.get(SolrFields.EAC_CPF_DESCRIPTION);
        if (descriptionHighlight != null) {
            this.descriptionWithHighlighting = descriptionHighlight.get(0);
        }
        this.other = this.objectToString(solrDocument.getFieldValue(SolrFields.OTHER));
        List<String> otherHighlight = highlightMap.get(SolrFields.OTHER);
        if (otherHighlight != null) {
            this.otherWithHighlighting = otherHighlight.get(0);
        }
        this.repository = CommonUtils.splitByColon(this.objectToString(solrDocument.getFieldValue(SolrFields.AI)), 0);
        this.repositoryCode = this.objectToString(solrDocument.getFieldValue(SolrFields.REPOSITORY_CODE));
        this.country = CommonUtils.splitByColon(this.objectToString(solrDocument.getFieldValue(SolrFields.COUNTRY)), 0);
        this.countryId = CommonUtils.splitByColon(this.objectToString(solrDocument.getFieldValue(SolrFields.COUNTRY)), 2);
        this.numberOfArchivalMaterialRelations = this.objectToString(solrDocument.getFieldValue(SolrFields.EAC_CPF_NUMBER_OF_MATERIAL_RELATIONS));
        this.numberOfNameRelations = this.objectToString(solrDocument.getFieldValue(SolrFields.EAC_CPF_NUMBER_OF_NAME_RELATIONS));
    }

    /**
     * Default constructor
     */
    public EacCpfResponse() {
        //Do nothing
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<String> getNameEntries() {
        return nameEntries;
    }

    public void setNameEntries(List<String> nameEntries) {
        this.nameEntries = nameEntries;
    }

    public List<String> getNameEntriesWithHighlighting() {
        return nameEntriesWithHighlighting;
    }

    public void setNameEntriesWithHighlighting(List<String> nameEntriesWithHighlighting) {
        this.nameEntriesWithHighlighting = nameEntriesWithHighlighting;
    }

    public String getExistDates() {
        return existDates;
    }

    public void setExistDates(String existDates) {
        this.existDates = existDates;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getNumberOfArchivalMaterialRelations() {
        return numberOfArchivalMaterialRelations;
    }

    public void setNumberOfArchivalMaterialRelations(String numberOfArchivalMaterialRelations) {
        this.numberOfArchivalMaterialRelations = numberOfArchivalMaterialRelations;
    }

    public String getNumberOfNameRelations() {
        return numberOfNameRelations;
    }

    public void setNumberOfNameRelations(String numberOfNameRelations) {
        this.numberOfNameRelations = numberOfNameRelations;
    }

    public String getDescriptionWithHighlighting() {
        return descriptionWithHighlighting;
    }

    public void setDescriptionWithHighlighting(String descriptionWithHighlighting) {
        this.descriptionWithHighlighting = descriptionWithHighlighting;
    }

    public String getOtherWithHighlighting() {
        return otherWithHighlighting;
    }

    public void setOtherWithHighlighting(String otherWithHighlighting) {
        this.otherWithHighlighting = otherWithHighlighting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    private String objectToString(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
    }
}
