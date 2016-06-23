/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.apenet.commons.solr.SolrFields;
import eu.archivesportaleurope.apeapi.response.ead.EadResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author kaisar
 */
public class JsonToObject {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T getObject(String filePath, Class<T> type) throws IOException {
//        URL url = Thread.currentThread().getContextClassLoader().getResource(filePath);
        File file = new File(filePath);
        T object = mapper.readValue(file, type);
        return object;
    }
    
    public List<SolrInputDocument> getEadSolrDocs(EadResponseSet eads) {
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        for (EadResponse ead : eads.getEadSearchResults()) {
            SolrInputDocument solrDoc = new SolrInputDocument();
            solrDoc.addField(SolrFields.ID, ead.getId());
            solrDoc.addField(SolrFields.TITLE, ead.getUnitTitle());
            solrDoc.addField(SolrFields.UNITID, ead.getUnitId());
            solrDoc.addField(SolrFields.SCOPECONTENT, ead.getScopeContent());
            solrDoc.addField(SolrFields.LANGUAGE, ead.getLanguage());
            solrDoc.addField(SolrFields.LANGMATERIAL, ead.getLangMaterial());
            solrDoc.addField(SolrFields.ALTERDATE, ead.getUnitDate());
            solrDoc.addField(SolrFields.COUNTRY, ead.getCountry());
            solrDoc.addField(SolrFields.TITLE_OF_FOND, ead.getFondsUnitTitle());
            solrDoc.addField(SolrFields.UNITID_OF_FOND, ead.getFondsUnitId());
            solrDoc.addField(SolrFields.AI, ead.getRepository());
            solrDoc.addField(SolrFields.REPOSITORY_CODE, ead.getRepositoryCode());
            solrDoc.addField(SolrFields.DAO, ead.isHasDigitalObject());
            solrDoc.addField(SolrFields.OPEN_DATA_ENABLE, "true");
            solrDocs.add(solrDoc);
        }
        return solrDocs;
    }
}
