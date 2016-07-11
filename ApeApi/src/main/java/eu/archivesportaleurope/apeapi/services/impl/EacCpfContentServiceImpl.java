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
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.response.common.DetailContent;
import eu.archivesportaleurope.apeapi.services.EacCpfContentService;
import eu.archivesportaleurope.apeapi.services.EadContentService;
import eu.archivesportaleurope.apeapi.transaction.repository.CLevelRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.EacCpfRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.EadContentRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.FindingAidRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.HoldingsGuideRepo;
import eu.archivesportaleurope.apeapi.transaction.repository.SourceGuideRepo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kaisar
 */
public class EacCpfContentServiceImpl implements EacCpfContentService {

    @Autowired
    EacCpfRepo eacCpfRepo;

    @Transactional
    @Override
    public EacCpf findEacCpfById(String id) {
        if (StringUtils.isNotBlank(id)) {

            EacCpf eacCpf;

            Integer idInt = Integer.parseInt(id);
            
            eacCpf = eacCpfRepo.findById(idInt);

            if (eacCpf == null) {
                throw new ResourceNotFoundException("Couldn't find any item with the given id", "EadContent Item not found, id" + id);
            }
            return eacCpf;
        }
        throw new ResourceNotFoundException("Couldn't find any item with the given id", "Clevel Item not found, id" + id);
    }
}
