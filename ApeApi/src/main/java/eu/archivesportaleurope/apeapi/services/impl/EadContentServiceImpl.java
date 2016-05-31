/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.response.common.DetailContent;
import eu.archivesportaleurope.apeapi.services.EadContentService;
import eu.archivesportaleurope.apeapi.transaction.repository.CLevelRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.EadContentRepo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kaisar
 */
public class EadContentServiceImpl implements EadContentService {

    @Autowired
    EadContentRepo contentRepo;
    @Autowired
    CLevelRepo cLevelRepo;

    @Transactional
    @Override
    public DetailContent findClevelContent(String id) {
        if (StringUtils.isNotBlank(id) && id.startsWith(SolrValues.C_LEVEL_PREFIX)) {
            Long idLong = new Long(id.substring(1));
            CLevel currentLevel = cLevelRepo.findById(idLong);
            if (currentLevel == null) {
                throw new ResourceNotFoundException("Couldn't find any item with the given id", "Clevel Item not found, id" + id);
            }
            //lazy load
            ArchivalInstitution ai = currentLevel.getEadContent().getEad().getArchivalInstitution();
            ai.getAiname();
            return new DetailContent(currentLevel);
        }
        throw new ResourceNotFoundException("Not a descriptive unit id", "Not a descriptive unit id: " + id);
    }

    @Transactional
    @Override
    public DetailContent findEadContent(String id) {
        if (StringUtils.isNotBlank(id)) {

            String solrPrefix = id.substring(0, 1);
            XmlType xmlType = XmlType.getTypeBySolrPrefix(solrPrefix);
            EadContent eadContent;
            if (xmlType != null) {
                Integer idLong = Integer.parseInt(id.substring(1));
                //get search term from previous request
                if (xmlType.getClazz().equals(FindingAid.class)) {
                    eadContent = contentRepo.findByFaId(idLong);
                } else if (xmlType.getClazz().equals(HoldingsGuide.class)) {
                    eadContent = contentRepo.findByHgId(idLong);
                } else {
                    eadContent = contentRepo.findBySgId(idLong);
                }
                if (eadContent == null) {
                    throw new ResourceNotFoundException("Couldn't find any item with the given id", "EadContent Item not found, id" + id);
                }

                DetailContent pageResponse = new DetailContent(eadContent);
                ArchivalInstitution ai = eadContent.getEad().getArchivalInstitution();
                ai.getAiname();
                return pageResponse;

            } else {
                //consider it as AI and fill AI details from DisplayPreviewContoller
            }
        }
        throw new ResourceNotFoundException("Couldn't find any item with the given id", "Clevel Item not found, id" + id);
    }
}
