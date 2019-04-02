/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.apenet.persistence.vo.EacCpf;

/**
 *
 * @author kaisar
 */
@FunctionalInterface
public interface EacCpfContentService {
    EacCpf findEacCpfById(String id);
}
