/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.FindingAid;

/**
 *
 * @author kaisar
 */
public class ArchivalInstituteResponse {

    private String name;
    private int id;
    private String country;
    private int countryId;
    private int numberOfFindingAids;
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
        this.setNumberOfFindingAids(numberOfPublishedItem);
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

    public int getNumberOfFindingAids() {
        return numberOfFindingAids;
    }

    public final void setNumberOfFindingAids(int numberOfFindingAids) {
        this.numberOfFindingAids = numberOfFindingAids;
    }

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }
}
