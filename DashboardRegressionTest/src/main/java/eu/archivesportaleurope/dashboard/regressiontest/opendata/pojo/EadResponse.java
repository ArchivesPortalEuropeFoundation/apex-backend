/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest.opendata.pojo;

import javax.xml.bind.annotation.XmlRootElement;

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
