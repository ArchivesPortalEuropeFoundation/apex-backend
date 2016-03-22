/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository;

import eu.apenet.persistence.vo.EadContent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kaisar
 */
@Transactional
public interface EadContentRepo extends JpaRepository<EadContent, Long> {

    @Transactional
    @Query("select a from EadContent a where faId = ?")
    EadContent findByFaId(Integer id);

    @Transactional
    @Query("select a from EadContent a where hgId = ?")
    EadContent findByHgId(Integer id);

    @Transactional
    @Query("select a from EadContent a where sgId = ?")
    EadContent findBySgId(Integer id);
}
