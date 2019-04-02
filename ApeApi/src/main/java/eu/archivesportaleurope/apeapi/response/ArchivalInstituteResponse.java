/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.FindingAid;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author kaisar
 */
public class ArchivalInstituteResponse {

    @ApiModelProperty(value = "Name of the AI")
    private String name;
    @ApiModelProperty(required = true, value = "Internal APE identifier of the result")
    private int id;
    @ApiModelProperty(value = "Name of the country where the repository is. In English.")
    private String country;
    @ApiModelProperty(value = "Id of the country")
    private int countryId;
    @ApiModelProperty(required = true, value = "Total number of documents found.")
    private int totalDocs;
    @ApiModelProperty(value = "Code of the repository holding the fonds. Preferably, but not necessarily <a target='_blank' href='https://en.wikipedia.org/wiki/International_Standard_Identifier_for_Libraries_and_Related_Organizations'>ISIL</a>")
    private String repositoryCode;

    public ArchivalInstituteResponse(ArchivalInstitution ai) {
        this.setName(ai.getAiname());
        this.setCountry(ai.getCountry().getCname());
        this.setId(ai.getAiId());
        this.setCountryId(ai.getCountryId());
        int numberOfPublishedItem = 0;

        for (FindingAid fa : ai.getFindingAids()) {
            if (fa.isPublished()) {
                numberOfPublishedItem++;
            }
        }
        for (Ead3 ead3 : ai.getEad3()) {
            if (ead3.isPublished()) {
                numberOfPublishedItem++;
            }
        }
        this.setTotalDocs(numberOfPublishedItem);
        this.repositoryCode = ai.getRepositorycode();
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public final void setCountry(String country) {
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public int getCountryId() {
        return countryId;
    }

    public final void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getTotalDocs() {
        return totalDocs;
    }

    public final void setTotalDocs(int totalDocs) {
        this.totalDocs = totalDocs;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public final void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }
}
