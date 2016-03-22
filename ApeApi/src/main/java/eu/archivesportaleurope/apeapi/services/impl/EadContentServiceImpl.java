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
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.archivesportaleurope.apeapi.response.common.OverViewFrontPageResponse;
import eu.archivesportaleurope.apeapi.services.EadContentService;
import eu.archivesportaleurope.apeapi.transaction.repository.ArchivalInstitutionDao;
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
    @Autowired
    ArchivalInstitutionDao institutionDao;

    @Transactional
    @Override
    public OverViewFrontPageResponse findEadContent(String id) {
        OverViewFrontPageResponse pageResponse = new OverViewFrontPageResponse();
        if (StringUtils.isNotBlank(id)) {
            if (id.startsWith(SolrValues.C_LEVEL_PREFIX)) {
                Long idLong = new Long(id.substring(1));
                CLevel currentLevel = cLevelRepo.findById(idLong);
                if (currentLevel != null) {
                    Ead ead = currentLevel.getEadContent().getEad();
                    ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();
                    pageResponse.setXmlType(XmlType.getContentType(ead));
                    pageResponse.setCurrentLevel(currentLevel);
                    pageResponse.setAiId(archivalInstitution.getAiId());
                    pageResponse.setAiRepoCode(archivalInstitution.getEncodedRepositorycode());
                    pageResponse.setEadId(ead.getEncodedIdentifier());
                }
            } else {
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
                    Ead ead = eadContent.getEad();
                    ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();
                    pageResponse.setXmlType(xmlType);
                    pageResponse.setEadContent(eadContent);
                    pageResponse.setAiId(archivalInstitution.getAiId());
                    pageResponse.setAiRepoCode(archivalInstitution.getEncodedRepositorycode());
                    pageResponse.setEadId(ead.getEncodedIdentifier());
                    pageResponse.setXmlTypeName(xmlType.getResourceName());
                } else {
                    //consider it as AI and fill AI details from DisplayPreviewContoller
                }

            }
        }
        return pageResponse;
    }
}
