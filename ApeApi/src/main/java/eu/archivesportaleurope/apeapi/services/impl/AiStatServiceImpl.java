/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.apeapi.response.ArchivalInstitutesResponse;
import eu.archivesportaleurope.apeapi.services.AiStatService;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import java.util.List;

/**
 *
 * @author kaisar
 */
public class AiStatServiceImpl implements AiStatService {

    @Override
    public ArchivalInstitutesResponse getAiWithOpenDataEnabled(int start, int limit) {
        JpaUtil.init();
        ArchivalInstitutionDAO aidao = DAOFactory.instance().getArchivalInstitutionDAO();
        int totla = aidao.countArchivalInstitutionsWithOpenDataEnabled();
        List<ArchivalInstitution> ais = aidao.getArchivalInstitutionsWithOpenDataEnabled(start, limit);
        ArchivalInstitutesResponse aisResponse = new ArchivalInstitutesResponse(ais);
        aisResponse.setTotal(totla);
        return aisResponse;
    }

}
