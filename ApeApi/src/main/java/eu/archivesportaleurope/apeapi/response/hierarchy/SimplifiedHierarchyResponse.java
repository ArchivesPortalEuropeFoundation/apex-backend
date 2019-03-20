/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.hierarchy;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.archivesportaleurope.apeapi.utils.CommonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
@XmlRootElement
@ApiModel
public class SimplifiedHierarchyResponse {

    @ApiModelProperty(required = true, value = "Internal APE identifier of the result")
    protected String id;

    @ApiModelProperty(value = "Identifier of the result provided by the repository")
    private String unitId;

    @ApiModelProperty(value = "Index relative to its siblings")
    private int siblingPosition;

    @ApiModelProperty(value = "Greatest ancestor is a level 0")
    private int ancestorLevel;
    
    public SimplifiedHierarchyResponse(SolrDocument solrDocument, int parentLevel) {
        this.id = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.ID));

        this.siblingPosition = CommonUtils.objectToInt(solrDocument.getFieldValue(Ead3SolrFields.ORDER_ID));

        this.unitId = CommonUtils.objectToString(solrDocument.getFieldValue(Ead3SolrFields.UNIT_ID));

        this.ancestorLevel = parentLevel;
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
