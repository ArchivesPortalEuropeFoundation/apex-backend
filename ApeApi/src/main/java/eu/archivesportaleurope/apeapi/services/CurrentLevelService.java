/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.archivesportaleurope.apeapi.response.OverViewResponse;

/**
 *
 * @author mahbub
 */
public interface CurrentLevelService {
    OverViewResponse findOverviewByClId(Long id);
}
