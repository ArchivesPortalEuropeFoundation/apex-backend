/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository;

import eu.apenet.persistence.vo.ArchivalInstitution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kaisar
 */
public interface ArchivalInstitutionDao extends JpaRepository<ArchivalInstitution, Long> {

    @Transactional
    @Query("select a from ArchivalInstitution a where aiId = ?")
    ArchivalInstitution findBy(Long id);
}
