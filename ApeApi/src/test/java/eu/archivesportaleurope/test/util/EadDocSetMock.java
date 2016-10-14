/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import eu.archivesportaleurope.apeapi.response.ResponseSet;
import eu.archivesportaleurope.apeapi.response.SearchStatResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class EadDocSetMock extends ResponseSet {

    private List<EadDocMock> eadSearchResults;

    public EadDocSetMock() {
        eadSearchResults = new ArrayList<>();
    }

    public EadDocSetMock(QueryResponse response) throws SolrServerException {
        eadSearchResults = new ArrayList<>();
        SearchStatResponse responseHeader = new SearchStatResponse(response);

        SolrDocumentList documentList = response.getResults();
        super.setTotalResults(documentList.getNumFound());
        super.setStartIndex(documentList.getStart());

        for (SolrDocument document : documentList) {

            this.addEadSearchResult(new EadDocMock(document, response));
        }
        this.setTotalPages((int) (super.totalResults / responseHeader.getRows()));
        if (super.totalResults % responseHeader.getRows() > 0) {
            super.totalPages++;
        }
    }

    public List<EadDocMock> getEadSearchResults() {
        return Collections.unmodifiableList(eadSearchResults);
    }

    public final void setEadSearchResults(List<EadDocMock> eadSearchResutls) {
        if (eadSearchResutls != null) {
            this.eadSearchResults = eadSearchResutls;
        }
    }

    public final void addEadSearchResult(EadDocMock eadSearchResutl) {
        if (eadSearchResutl != null) {
            this.eadSearchResults.add(eadSearchResutl);
        }
    }
}
