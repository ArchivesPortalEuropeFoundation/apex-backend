/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.apenet.commons.solr.SolrFields;
import eu.archivesportaleurope.apeapi.response.eaccpf.EacCpfResponse;
import eu.archivesportaleurope.apeapi.response.eaccpf.EacCpfResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author kaisar
 */
public class JsonToObject {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T getObject(String filePath, Class<T> type) throws IOException {
        File file = new File(filePath);
        T object = mapper.readValue(file, type);
        return object;
    }

    private List<SolrInputDocument> getEadSolrDocs(EadResponseSet eads) {
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

    private List<SolrInputDocument> getEacSolrDocs(EacCpfResponseSet eacs) {
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        for (EacCpfResponse eac : eacs.getEacSearchResults()) {
            SolrInputDocument solrDoc = new SolrInputDocument();
            solrDoc.addField(SolrFields.ID, eac.getId());
            solrDoc.addField(SolrFields.EAC_CPF_RECORD_ID, eac.getRecordId());
            solrDoc.addField(SolrFields.EAC_CPF_FACET_ENTITY_TYPE, eac.getEntityType());
            solrDoc.addField(SolrFields.EAC_CPF_NAMES, eac.getNameEntries());
            solrDoc.addField(SolrFields.EAC_CPF_DATE_DESCRIPTION, eac.getExistDates());
            solrDoc.addField(SolrFields.EAC_CPF_DESCRIPTION, eac.getDescription());
            solrDoc.addField(SolrFields.OTHER, eac.getOther());
            solrDoc.addField(SolrFields.COUNTRY, eac.getCountry());
            solrDoc.addField(SolrFields.AI, eac.getRepository());
            solrDoc.addField(SolrFields.REPOSITORY_CODE, eac.getRepositoryCode());
            solrDoc.addField(SolrFields.EAC_CPF_NUMBER_OF_MATERIAL_RELATIONS, eac.getNumberOfArchivalMaterialRelations());
            solrDoc.addField(SolrFields.EAC_CPF_NUMBER_OF_NAME_RELATIONS, eac.getNumberOfNameRelations());
            solrDoc.addField(SolrFields.OPEN_DATA_ENABLE, "true");
            solrDocs.add(solrDoc);
        }
        return solrDocs;
    }

    public <T> Collection<SolrInputDocument> getSolrDocs(T object) {
        if (object instanceof EadResponseSet) {
            return this.getEadSolrDocs((EadResponseSet) object);
        } else if (object instanceof EacCpfResponseSet) {
            return this.getEacSolrDocs((EacCpfResponseSet) object);
        } else {
            return null;
        }
    }
}
