/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository;

import eu.apenet.persistence.vo.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Mahbub
 */
public interface ApiKeyRepo extends JpaRepository<ApiKey, Long>{
    @Query("select a from ApiKey a where status!='deleted' and apiKey = ?")
    ApiKey findByApiKey(String apiKey);
}
