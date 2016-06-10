/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository;

import eu.apenet.persistence.vo.SourceGuide;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mahbub
 */
@Transactional
public interface SourceGuideRepo extends JpaRepository<SourceGuide, Long> {
    SourceGuide findById(Integer id);
}
