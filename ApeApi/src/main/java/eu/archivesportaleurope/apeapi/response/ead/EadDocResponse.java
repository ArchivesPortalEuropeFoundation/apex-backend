/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.ead;

/**
 *
 * @author kaisar
 */
import eu.apenet.commons.solr.SolrFields;
import eu.archivesportaleurope.apeapi.utils.CommonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.common.SolrDocument;

@XmlRootElement
@ApiModel
public class EadDocResponse {

    @ApiModelProperty(required = true, value = "Internal APE identifier of the result")
    private final String id;
    @ApiModelProperty(value = "Title of the finding aid. ")
    private final String fondsUnitTitle;
    @ApiModelProperty(value = "Number of search hits on the current document.")
    private final long numberOfResults;

    @ApiModelProperty(value = "Name of the repository holding the fonds")
    private String repository;

    @ApiModelProperty(value = "Name of the country where the repository is. In English. ")
    private String country;

    @ApiModelProperty(value = "Language of the description of the result.")
    private String language;

    @ApiModelProperty(value = "Code of the repository holding the fonds. Preferably, but not necessarily <a target='_blank' href='https://en.wikipedia.org/wiki/International_Standard_Identifier_for_Libraries_and_Related_Organizations'>ISIL</a>")
    private String repositoryCode;

    @ApiModelProperty(value = "Fonds unit ID")
    private String fondsUnitId;

    @ApiModelProperty(value = "Unit date")
    private String unitDate;

    @ApiModelProperty(value = "Abstract of the content / Scope Content")
    private String scopeContent;

    public EadDocResponse(Group group) {
        //ToDo: change this
        //ex: Inventaris van het archief van de Nederlandse Ambassade in Nepal, 1965-1974:G:F124
        //00000000:Algemeen:G:C4541
        String temp = group.getGroupValue();
        int lastColonIndex = temp.lastIndexOf(":");
        this.id = temp.substring(lastColonIndex + 1);
        temp = temp.substring(0, lastColonIndex);
        int firstColonIndex = temp.indexOf(":");
        this.fondsUnitTitle = temp.substring(0, firstColonIndex);
        this.numberOfResults = group.getResult().getNumFound();
        //get the default document
        SolrDocument solrDocument = group.getResult().get(0);

        this.language = this.objectToString(solrDocument.getFieldValue(SolrFields.LANGUAGE));
        this.country = CommonUtils.splitByColon(this.objectToString(solrDocument.getFieldValue(SolrFields.COUNTRY)), 0);
        this.repository = CommonUtils.splitByColon(this.objectToString(solrDocument.getFieldValue(SolrFields.AI)), 0);
        this.repositoryCode = this.objectToString(solrDocument.getFieldValue(SolrFields.REPOSITORY_CODE));
        this.fondsUnitId = this.objectToString(solrDocument.getFieldValue(SolrFields.UNITID_OF_FOND));
        this.unitDate = this.objectToString(solrDocument.getFieldValue(SolrFields.ALTERDATE));
        this.scopeContent = this.objectToString(solrDocument.getFieldValue(SolrFields.SCOPECONTENT));

    }

    public String getId() {
        return id;
    }

    public String getFondsUnitTitle() {
        return fondsUnitTitle;
    }

    public long getNumberOfResults() {
        return numberOfResults;
    }

    private String objectToString(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
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

    public String getRepositoryCode() {
        return repositoryCode;
    }

    public void setRepositoryCode(String repositoryCode) {
        this.repositoryCode = repositoryCode;
    }

    public String getFondsUnitId() {
        return fondsUnitId;
    }

    public void setFondsUnitId(String fondsUnitId) {
        this.fondsUnitId = fondsUnitId;
    }

    public String getUnitDate() {
        return unitDate;
    }

    public void setUnitDate(String unitDate) {
        this.unitDate = unitDate;
    }

    public String getScopeContent() {
        return scopeContent;
    }

    public void setScopeContent(String scopeContent) {
        this.scopeContent = scopeContent;
    }

}
