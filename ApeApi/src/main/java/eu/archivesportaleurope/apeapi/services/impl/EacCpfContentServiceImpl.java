/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.archivesportaleurope.apeapi.exceptions.ResourceNotFoundException;
import eu.archivesportaleurope.apeapi.services.EacCpfContentService;
import eu.archivesportaleurope.apeapi.transaction.repository.EacCpfRepo;
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
            
            //lazy load
            ArchivalInstitution ai = eacCpf.getArchivalInstitution();
            ai.getAiname();
            
            return eacCpf;
        }
        throw new ResourceNotFoundException("Couldn't find any item with the given id", "Clevel Item not found, id" + id);
    }
}
