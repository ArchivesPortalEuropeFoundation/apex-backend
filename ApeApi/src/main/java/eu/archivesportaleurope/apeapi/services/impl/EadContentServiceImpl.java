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
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.response.common.DetailContent;
import eu.archivesportaleurope.apeapi.services.EadContentService;
import eu.archivesportaleurope.apeapi.transaction.repository.CLevelRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.EadContentRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.FindingAidRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.HoldingsGuideRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.SourceGuideRepo;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
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
    FindingAidRepo findingAidRepo;

    @Autowired
    HoldingsGuideRepo holdingsGuideRepo;

    @Autowired
    SourceGuideRepo sourceGuideRepo;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Override
    public DetailContent findClevelContent(String id) {
        if (StringUtils.isNotBlank(id) && id.startsWith(SolrValues.C_LEVEL_PREFIX)) {
            Long idLong = new Long(id.substring(1));
            CLevel currentLevel = cLevelRepo.findById(idLong);
            if (currentLevel == null) {
                throw new ResourceNotFoundException("Couldn't find any item with the given id", "Clevel Item not found, id: " + id);
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
    public List<DetailContent> findClevelContent(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new ResourceNotFoundException("Not a descriptive unit id", "No descriptive unit id found");
        }
        List<Long> longIds = new ArrayList<>();
        for (String id : ids) {
            if (StringUtils.isNotBlank(id) && id.startsWith(SolrValues.C_LEVEL_PREFIX)) {
                longIds.add(new Long(id.substring(1)));
            }
        }
        if (!longIds.isEmpty()) {
            List<DetailContent> detailContents = new ArrayList<>();
            List<CLevel> currentLevels;
            currentLevels = cLevelRepo.findByIdIn(longIds);
            if (currentLevels == null) {
                throw new ResourceNotFoundException("Couldn't find any item with the given ids", "No Clevel Item not found");
            }
            if (currentLevels.isEmpty()) {
                throw new ResourceNotFoundException("Couldn't find any Clevel", "Clevel Item not found");
            }
            currentLevels.forEach(currentLevel -> {
                //lazy load
                try {
                    ArchivalInstitution ai = currentLevel.getEadContent().getEad().getArchivalInstitution();
                    ai.getAiname();
                    detailContents.add(new DetailContent(currentLevel));
                } catch (NullPointerException ex) {
                    logger.debug("Null exception currentLevel: " + currentLevel.getId(), ex);
                }
            });

            return detailContents;
        }
        throw new ResourceNotFoundException("Couldn't find any Clevel", "Clevel Item not found");
    }

    @Transactional
    @Override
    public List<DetailContent> getSomeClevelContent() {
        ArrayList<DetailContent> detailContents = new ArrayList<>();
        List<CLevel> currentLevels;
        currentLevels = cLevelRepo.findFirst1000ByOrderByIdAsc(); //new Sort(Sort.Direction.DESC, "id")
        if (currentLevels == null || currentLevels.isEmpty()) {
            throw new ResourceNotFoundException("Couldn't find any Clevel", "Clevel Item not found");
        }
        currentLevels.forEach(currentLevel -> {
            //lazy load
            try {
                ArchivalInstitution ai = currentLevel.getEadContent().getEad().getArchivalInstitution();
                ai.getAiname();
                detailContents.add(new DetailContent(currentLevel));
            } catch (NullPointerException ex) {
                System.out.println("Null exception currentLevel: " + currentLevel.getId() + " : " + ex.getMessage());
            }
        });

        return detailContents;

    }

    @Transactional
    @Override
    public Ead findEadById(String id) {
        if (StringUtils.isNotBlank(id)) {

            String solrPrefix = id.substring(0, 1);
            XmlType xmlType = XmlType.getTypeBySolrPrefix(solrPrefix);
            Ead ead;
            if (xmlType != null) {
                Integer idLong = Integer.parseInt(id.substring(1));
                //get search term from previous request
                if (xmlType.getClazz().equals(FindingAid.class)) {
                    ead = findingAidRepo.findById(idLong);
                } else if (xmlType.getClazz().equals(HoldingsGuide.class)) {
                    ead = holdingsGuideRepo.findById(idLong);
                } else if (xmlType.getClazz().equals(SourceGuide.class)) {
                    ead = sourceGuideRepo.findById(idLong);
                } else {
                    throw new ResourceNotFoundException("Provided item is not fully supported or downloadable", "Id should start with F/H/S: " + id);
                }
                if (ead == null) {
                    throw new ResourceNotFoundException("Couldn't find any item with the given id", "EadContent Item not found, id" + id);
                }
                return ead;

            }
        }
        throw new ResourceNotFoundException("Provided id is either malformed or not an Ead item", "Bad ead id, id" + id);
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
