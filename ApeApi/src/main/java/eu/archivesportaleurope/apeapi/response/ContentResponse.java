/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mahbub
 */
@ApiModel
public class ContentResponse {
    @ApiModelProperty(required = true, value="Internal APE identifier of the Descriptive Unit, Finding Aid, Holdings Guide or Source Guide. The same as your request parameter \"id\". ")
    private String id;
    
    @ApiModelProperty(required = true, value="Original identifier of the information provided by the repository.")
    private String unitId;
    
    @ApiModelProperty(required = true, value="Title of the information provided by the repository.")
    private String unitTitle;
    
    @ApiModelProperty(required = true, value="Internal APE identifier of the repository.")
    private int repositoryId;
    
    @ApiModelProperty(required = true, value="Name of the repository.")
    private String repository;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public int getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
