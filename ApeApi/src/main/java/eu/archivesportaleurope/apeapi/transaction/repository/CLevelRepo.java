/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository;

import eu.apenet.persistence.vo.CLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author kaisar
 */
public interface CLevelRepo extends JpaRepository<CLevel, Long> {

    @Query("select a from CLevel a where id = ?")
    CLevel findById(Long id);
}
