/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.ContentSearchOptions;

/**
 *
 * @author papp
 */
public class ContentManagerResults {

    protected Long totalNumberOfResults = 0l;
    protected Long totalConvertedFiles = 0l;
    protected Long totalValidatedFiles = 0l;
    protected Long totalPublishedUnits = 0l;
    protected Long totalChos = 0l;
    protected Long totalChosDeliveredToEuropeana = 0l;
    protected ContentSearchOptions eadSearchOptions;
    protected XmlType xmlType;

    public ContentManagerResults(ContentSearchOptions eadSearchOptions) {
        this.eadSearchOptions = eadSearchOptions;
        xmlType = XmlType.getType(eadSearchOptions.getContentClass());
    }

    public ContentSearchOptions getEadSearchOptions() {
        return eadSearchOptions;
    }

    public void setEadSearchOptions(ContentSearchOptions eadSearchOptions) {
        this.eadSearchOptions = eadSearchOptions;
    }

    public long getTotalNumberOfResults() {
        return totalNumberOfResults;
    }

    public void setTotalNumberOfResults(long totalNumberOfResults) {
        this.totalNumberOfResults = totalNumberOfResults;
    }

    public int getXmlTypeId() {
        return xmlType.getIdentifier();
    }

    public Long getTotalConvertedFiles() {
        return totalConvertedFiles;
    }

    public void setTotalConvertedFiles(Long totalConvertedFiles) {
        this.totalConvertedFiles = totalConvertedFiles;
    }

    public Long getTotalValidatedFiles() {
        return totalValidatedFiles;
    }

    public void setTotalValidatedFiles(Long totalValidatedFiles) {
        this.totalValidatedFiles = totalValidatedFiles;
    }

    public Long getTotalPublishedUnits() {
        return totalPublishedUnits;
    }

    public void setTotalPublishedUnits(Long totalPublishedUnits) {
        this.totalPublishedUnits = totalPublishedUnits;
    }

    public Long getTotalChos() {
        return totalChos;
    }

    public void setTotalChos(Long totalChos) {
        this.totalChos = totalChos;
    }

    public Long getTotalChosDeliveredToEuropeana() {
        return totalChosDeliveredToEuropeana;
    }

    public void setTotalChosDeliveredToEuropeana(Long totalChosDeliveredToEuropeana) {
        this.totalChosDeliveredToEuropeana = totalChosDeliveredToEuropeana;
    }

    public XmlType getXmlType() {
        return xmlType;
    }

    public void setXmlType(XmlType xmlType) {
        this.xmlType = xmlType;
    }

    public void setTotalNumberOfResults(Long totalNumberOfResults) {
        this.totalNumberOfResults = totalNumberOfResults;
    }

}
