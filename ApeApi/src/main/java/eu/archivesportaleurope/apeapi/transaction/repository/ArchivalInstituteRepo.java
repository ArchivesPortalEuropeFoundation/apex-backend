/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository;

import eu.apenet.persistence.vo.ArchivalInstitution;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kaisar
 */
public interface ArchivalInstituteRepo extends JpaRepository<ArchivalInstitution, Integer> {

    @Transactional
    @Query("select a from ArchivalInstitution a where aiId = ?")
    ArchivalInstitution findBy(Integer id);

    @Transactional
    @Query("select a from ArchivalInstitution a where openDataEnabled = true")
    List<ArchivalInstitution> findByOpenDataEnabled(Pageable pageable);

    @Transactional
    @Query("select count(*) from ArchivalInstitution a where openDataEnabled = true")
    int getCount();
}
