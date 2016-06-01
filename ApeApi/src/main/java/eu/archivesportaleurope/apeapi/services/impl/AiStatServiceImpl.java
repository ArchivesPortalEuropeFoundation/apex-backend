/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.apeapi.response.ArchivalInstitutesResponse;
import eu.archivesportaleurope.apeapi.services.AiStatService;
import eu.archivesportaleurope.apeapi.transaction.repository.ArchivalInstituteRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/**
 *
 * @author kaisar
 */
public class AiStatServiceImpl implements AiStatService {

    @Autowired
    ArchivalInstituteRepo archivalInstituteRepo;

    @Override
    public ArchivalInstitutesResponse getAiWithOpenDataEnabled(int page, int limit) {
        List<ArchivalInstitution> ais = archivalInstituteRepo.findByOpenDataEnabled(new PageRequest(page, limit));
        ArchivalInstitutesResponse aisResponse = new ArchivalInstitutesResponse(ais);
        aisResponse.setTotal(archivalInstituteRepo.getCount());
        return aisResponse;
    }

}
