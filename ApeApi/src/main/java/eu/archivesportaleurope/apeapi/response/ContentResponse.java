/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.archivesportaleurope.apeapi.response.common.DetailContent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mahbub
 */
@ApiModel
public class ContentResponse {

    @ApiModelProperty(required = true, value = "Internal APE identifier of the Descriptive Unit, Finding Aid, Holdings Guide or Source Guide. The same as your request parameter \"id\". ")
    private String id;

    @ApiModelProperty(required = true, value = "Original identifier of the information provided by the repository.")
    private String unitId;

    @ApiModelProperty(required = true, value = "Title of the information provided by the repository.")
    private String unitTitle;

    @ApiModelProperty(value = "Title of the finding aid. ")
    private String findingAidTitle;

    @ApiModelProperty(required = true, value = "Internal APE identifier of the repository.")
    private int repositoryId;

    @ApiModelProperty(required = true, value = "Name of the repository.")
    private String repository;

    public ContentResponse() {
    }

    public ContentResponse(DetailContent detailContent, String Id) {
        this.setId(Id);
        this.setRepositoryId(detailContent.getAiId());
        this.setRepository(detailContent.getAiRepoName());
        this.setUnitId(detailContent.getUnitId());
        this.setUnitTitle(detailContent.getUnitTitle());
        this.setFindingAidTitle(detailContent.getFindingAidTitle());
    }

    public String getId() {
        return id;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public String getUnitId() {
        return unitId;
    }

    public final void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public final void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public String getFindingAidTitle() {
        return findingAidTitle;
    }

    public final void setFindingAidTitle(String findingAidTitle) {
        this.findingAidTitle = findingAidTitle;
    }

    public int getRepositoryId() {
        return repositoryId;
    }

    public final void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepository() {
        return repository;
    }

    public final void setRepository(String repository) {
        this.repository = repository;
    }
}
