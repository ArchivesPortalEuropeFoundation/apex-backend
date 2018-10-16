/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.hierarchy;

import eu.apenet.commons.solr.Ead3SolrFields;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author mahbub
 */
@XmlRootElement
@ApiModel
public class HierarchyResponse {

    @ApiModelProperty(required = true, value = "Internal APE identifier of the result")
    protected String id;

    @ApiModelProperty(value = "Identifier of the result provided by the repository")
    private String unitId;

    @ApiModelProperty(value = "Index relative to its siblings")
    private int siblingPosition;

    @ApiModelProperty(value = "Greatest ancestor is a level 0")
    private int ancestorLevel;

    public HierarchyResponse(SolrDocument solrDocument, QueryResponse response, int parentLevel) {
        this.id = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.ID));

        this.siblingPosition = this.objectToInt(solrDocument.getFieldValue(Ead3SolrFields.ORDER_ID));

        this.unitId = this.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_ID));

        this.ancestorLevel = parentLevel;
    }

    /**
     * Default constructor
     */
    public HierarchyResponse() {
        //Do nothing
    }

    private String objectToString(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
    }

    private int objectToInt(Object o) {
        if (o != null) {
            try {
                int n = Integer.parseInt(o.toString());
                return n;
            } catch (NullPointerException | NumberFormatException ex) {
            }
            return 0;
        } else {
            return 0;
        }
    }

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

    public int getSiblingPosition() {
        return siblingPosition;
    }

    public void setSiblingPosition(int siblingPosition) {
        this.siblingPosition = siblingPosition;
    }

    public int getAncestorLevel() {
        return ancestorLevel;
    }

    public void setAncestorLevel(int ancestorLevel) {
        this.ancestorLevel = ancestorLevel;
    }
}
