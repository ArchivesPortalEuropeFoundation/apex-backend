/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.SolrFields;
import eu.archivesportaleurope.apeapi.response.eaccpf.EacCpfResponse;
import eu.archivesportaleurope.apeapi.response.eaccpf.EacCpfResponseSet;
import eu.archivesportaleurope.apeapi.response.ead.EadResponse;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

    private SolrJsonObject getGenericObject(String filePath) throws IOException {
        return this.getObject(filePath, SolrJsonObject.class);
    }

    public List<SolrInputDocument> getSolrDocsFromGenericJson(String filePath) throws IOException {
        SolrJsonObject object = this.getGenericObject(filePath);
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        for (HashMap<String, String> map : object.getEadSearchResults()) {
            SolrInputDocument solrDoc = new SolrInputDocument();
            for (String key : map.keySet()) {
                solrDoc.addField(key, map.get(key));
            }
            solrDoc.addField(Ead3SolrFields.OPEN_DATA, "true");
            solrDocs.add(solrDoc);
        }
        return solrDocs;
    }

    private List<SolrInputDocument> getEadSolrDocs(EadResponseSet eads) {
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        for (EadResponse ead : eads.getEadSearchResults()) {
            SolrInputDocument solrDoc = new SolrInputDocument();
            solrDoc.addField(Ead3SolrFields.ID, ead.getId());
            solrDoc.addField(Ead3SolrFields.TITLE_PROPER, ead.getFindingAidTitle());
            solrDoc.addField(Ead3SolrFields.UNIT_ID, ead.getUnitId());
            solrDoc.addField(Ead3SolrFields.SCOPE_CONTENT, ead.getScopeContent());
            solrDoc.addField(Ead3SolrFields.PARENT_ID, ead.getParentId());
            solrDoc.addField(Ead3SolrFields.LANGUAGE, ead.getLanguage());
            solrDoc.addField(Ead3SolrFields.LANG_MATERIAL, ead.getLangMaterial());
            solrDoc.addField(Ead3SolrFields.ALTERNATE_UNIT_DATE, ead.getUnitDate());
            solrDoc.addField(Ead3SolrFields.COUNTRY, ead.getCountry());
            solrDoc.addField(Ead3SolrFields.UNIT_TITLE, ead.getUnitTitle());
            solrDoc.addField(Ead3SolrFields.RECORD_ID, ead.getFindingAidNo());
            solrDoc.addField(Ead3SolrFields.AI, ead.getRepository());
            solrDoc.addField(Ead3SolrFields.REPOSITORY_CODE, ead.getRepositoryCode());
            solrDoc.addField(Ead3SolrFields.DAO, ead.isHasDigitalObject());
            solrDoc.addField(Ead3SolrFields.OPEN_DATA, "true");
            solrDocs.add(solrDoc);
        }
        return solrDocs;
    }

    private List<SolrInputDocument> getEadSolrDocs(EadDocSetMock eads) {
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        for (EadDocMock ead : eads.getEadSearchResults()) {
            SolrInputDocument solrDoc = new SolrInputDocument();
            solrDoc.addField(Ead3SolrFields.ID, ead.getId());
            solrDoc.addField(Ead3SolrFields.TITLE_PROPER, ead.getFindingAidTitle());
            solrDoc.addField(Ead3SolrFields.UNIT_ID, ead.getUnitId());
            solrDoc.addField(Ead3SolrFields.SCOPE_CONTENT, ead.getScopeContent());
            solrDoc.addField(Ead3SolrFields.LANGUAGE, ead.getLanguage());
            solrDoc.addField(Ead3SolrFields.LANG_MATERIAL, ead.getLangMaterial());
            solrDoc.addField(Ead3SolrFields.ALTERNATE_UNIT_DATE, ead.getUnitDate());
            solrDoc.addField(Ead3SolrFields.COUNTRY, ead.getCountry());
            solrDoc.addField(Ead3SolrFields.UNIT_TITLE, ead.getUnitTitle());
            solrDoc.addField(Ead3SolrFields.RECORD_ID, ead.getFindingAidNo());
            solrDoc.addField(Ead3SolrFields.AI, ead.getRepository());
            solrDoc.addField(Ead3SolrFields.REPOSITORY_CODE, ead.getRepositoryCode());
            if ("du".equals(ead.getDocTypeId()) || "fa".equals(ead.getDocTypeId())) {
                solrDoc.addField(SolrFields.FA_DYNAMIC_NAME, ead.getDynamicName());
                solrDoc.addField(SolrFields.FA_DYNAMIC_ID + "0_s", ead.getDynamicId());
            } else if ("hg".equals(ead.getDocTypeId())) {
                solrDoc.addField(SolrFields.HG_DYNAMIC_NAME, ead.getDynamicName());
                solrDoc.addField(SolrFields.HG_DYNAMIC_ID + "0_s", ead.getDynamicId());
            } else if ("sg".equals(ead.getDocTypeId())) {
                solrDoc.addField(SolrFields.SG_DYNAMIC_NAME, ead.getDynamicName());
                solrDoc.addField(SolrFields.SG_DYNAMIC_ID + "0_s", ead.getDynamicId());
            }
            solrDoc.addField(Ead3SolrFields.DAO, ead.isHasDigitalObject());
            solrDoc.addField(Ead3SolrFields.OPEN_DATA, "true");
            solrDocs.add(solrDoc);
        }
        return solrDocs;
    }

    private List<SolrInputDocument> getEacSolrDocs(EacCpfResponseSet eacs) {
        List<SolrInputDocument> solrDocs = new ArrayList<>();
        for (EacCpfResponse eac : eacs.getEacSearchResults()) {
            SolrInputDocument solrDoc = new SolrInputDocument();
            solrDoc.addField(Ead3SolrFields.ID, eac.getId());
            solrDoc.addField(SolrFields.EAC_CPF_RECORD_ID, eac.getRecordId());
            solrDoc.addField(SolrFields.EAC_CPF_FACET_ENTITY_TYPE, eac.getEntityType());
            solrDoc.addField(SolrFields.EAC_CPF_NAMES, eac.getNameEntries());
            solrDoc.addField(SolrFields.EAC_CPF_DATE_DESCRIPTION, eac.getExistDates());
            solrDoc.addField(SolrFields.EAC_CPF_DESCRIPTION, eac.getDescription());
            solrDoc.addField(Ead3SolrFields.OTHER, eac.getOther());
            solrDoc.addField(Ead3SolrFields.COUNTRY, eac.getCountry());
            solrDoc.addField(Ead3SolrFields.AI, eac.getRepository());
            solrDoc.addField(Ead3SolrFields.REPOSITORY_CODE, eac.getRepositoryCode());
            solrDoc.addField(SolrFields.EAC_CPF_NUMBER_OF_MATERIAL_RELATIONS, eac.getNumberOfArchivalMaterialRelations());
            solrDoc.addField(SolrFields.EAC_CPF_NUMBER_OF_NAME_RELATIONS, eac.getNumberOfNameRelations());
            solrDoc.addField(Ead3SolrFields.OPEN_DATA, "true");
            solrDocs.add(solrDoc);
        }
        return solrDocs;
    }

    public <T> Collection<SolrInputDocument> getSolrDocs(T object) {
        if (object instanceof EadResponseSet) {
            return this.getEadSolrDocs((EadResponseSet) object);
        } else if (object instanceof EacCpfResponseSet) {
            return this.getEacSolrDocs((EacCpfResponseSet) object);
        } else if (object instanceof EadDocSetMock) {
            return this.getEadSolrDocs((EadDocSetMock) object);
        } else {
            return null;
        }
    }
}
