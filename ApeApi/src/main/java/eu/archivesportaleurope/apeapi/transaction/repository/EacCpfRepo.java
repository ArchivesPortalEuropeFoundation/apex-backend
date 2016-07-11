/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository;


import eu.apenet.persistence.vo.EacCpf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author kaisar
 */
public interface EacCpfRepo extends JpaRepository<EacCpf, Long> {

    @Query("select a from EacCpf a where id = ?")
    EacCpf findById(Integer id);
}
