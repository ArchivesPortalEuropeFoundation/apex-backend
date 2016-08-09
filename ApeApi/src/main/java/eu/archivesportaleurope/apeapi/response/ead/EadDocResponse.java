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
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.solr.client.solrj.response.FacetField.Count;

import eu.apenet.commons.solr.SolrValues;
import io.swagger.annotations.ApiModel;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.response.Group;

@XmlRootElement
@ApiModel
public class EadDocResponse {

    public enum Type {
        CLEVEL, FOND
    }
    private final String id;
    private final String name;
    private long levelDepth;
    private final long count;
    @JsonProperty("isLeaf")
    private final boolean leaf;

    public EadDocResponse(Group group, Type type) {
        //ToDo: change this
        //ex: Inventaris van het archief van de Nederlandse Ambassade in Nepal, 1965-1974:G:F124
        //00000000:Algemeen:G:C4541
        String temp = group.getGroupValue();
        int lastColonIndex = temp.lastIndexOf(":");
        this.id = temp.substring(lastColonIndex + 1);
        temp = temp.substring(0, lastColonIndex);
        lastColonIndex = temp.lastIndexOf(":");
        String docLeaf = temp.substring(lastColonIndex + 1);
        this.leaf = SolrValues.TYPE_LEAF.equals(docLeaf);
        temp = temp.substring(0, lastColonIndex);
        if (Type.CLEVEL.equals(type)) {
            int firstColonIndex = temp.indexOf(":");
            this.levelDepth = Long.parseLong(temp.substring(0, firstColonIndex));
            this.name = temp.substring(firstColonIndex + 1);
        } else {
            this.name = temp.substring(0);
        }
        this.count = group.getResult().getNumFound();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCount() {
        return count;
    }

    public long getLevelDepth() {
        return levelDepth;
    }

    public boolean isLeaf() {
        return leaf;
    }

}
