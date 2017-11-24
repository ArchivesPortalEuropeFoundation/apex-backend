/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.commons.solr;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;

/**
 *
 * @author mahbub
 */
public class SolrDocUtils {

    /**
     * @param d SolrDocument to convert
     * @return a SolrInputDocument with the same fields and values as the
     * SolrDocument. All boosts are 1.0f
     * No, not deprecated x(
     */
    public static SolrInputDocument toSolrInputDocument(SolrDocument d) {
        SolrInputDocument doc = new SolrInputDocument();
        for (String name : d.getFieldNames()) {
            doc.addField(name, d.getFieldValue(name), 1.0f);
        }
        return doc;
    }

    /**
     * @param d SolrInputDocument to convert
     * @return a SolrDocument with the same fields and values as the
     * SolrInputDocument
     * No, not deprecated x(
     */
    public static SolrDocument toSolrDocument(SolrInputDocument d) {
        SolrDocument doc = new SolrDocument();
        for (SolrInputField field : d) {
            doc.setField(field.getName(), field.getValue());
        }
        if (d.getChildDocuments() != null) {
            for (SolrInputDocument in : d.getChildDocuments()) {
                doc.addChildDocument(toSolrDocument(in));
            }

        }
        return doc;
    }
}
