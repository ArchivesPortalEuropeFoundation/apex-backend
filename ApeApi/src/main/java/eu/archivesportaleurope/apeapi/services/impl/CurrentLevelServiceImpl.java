/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.response.OverViewResponse;
import eu.archivesportaleurope.apeapi.services.CurrentLevelService;
import eu.archivesportaleurope.apeapi.transaction.repository.CLevelRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mahbub
 */
public class CurrentLevelServiceImpl implements CurrentLevelService {
    @Autowired
    CLevelRepo cLevelRepo;
    
    @Override
    @Transactional
    public OverViewResponse findOverviewByClId(Long id) {
        CLevel currentLevel = cLevelRepo.findById(id);
        
        if (currentLevel == null) {
            throw new ResourceNotFoundException("Couldn't find any item with the given id", "Clevel Item not found, id"+id);
        }
        
        Ead ead = currentLevel.getEadContent().getEad();
        
        ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();
        
        XmlType xmlType = XmlType.getContentType(ead);
        OverViewResponse overViewResponse = new OverViewResponse();
        overViewResponse.setXmlType(xmlType);
        overViewResponse.setOverViewXml(currentLevel.getXml());
        overViewResponse.setAiId(archivalInstitution.getAiId());
        overViewResponse.setId(currentLevel.getId()+"");
        overViewResponse.setUnitId(currentLevel.getUnitid());
        overViewResponse.setUnitTitle(currentLevel.getUnittitle());
        return overViewResponse;
    }
    
}
