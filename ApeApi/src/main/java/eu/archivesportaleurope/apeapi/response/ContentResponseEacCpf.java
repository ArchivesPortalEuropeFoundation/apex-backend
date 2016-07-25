/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import de.staatsbibliothek_berlin.eac.EacCpf;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author mahbub
 */
public class ContentResponseEacCpf {
    @ApiModelProperty(required = true, value="Internal APE identifier of the EAC-CPF. The same as your request parameter \"id\". ")
    private int id;
    
    @ApiModelProperty(value="Name of the entity")
    private String nameEntry;
    
    @ApiModelProperty(value="Internal APE identifier of the repository.")
    private int repositoryId;
    
    @ApiModelProperty(value="Name of the repository.")
    private String repositoryName;
    
    @ApiModelProperty(required = true, value="Information in EAD-format, serialized as JSON. This part could potentially contain all the elements that can be part of an EAD/XML-document.")
    private EacCpf content;

    public ContentResponseEacCpf(eu.apenet.persistence.vo.EacCpf eacCpf) {
        this.id = eacCpf.getId();
        this.nameEntry = eacCpf.getTitle();
        this.repositoryId = eacCpf.getAiId();
        this.repositoryName = eacCpf.getArchivalInstitution().getAiname();
    }
    
    public EacCpf getContent() {
        return content;
    }

    public void setContent(EacCpf content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEntry() {
        return nameEntry;
    }

    public void setNameEntry(String nameEntry) {
        this.nameEntry = nameEntry;
    }

    public int getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}
