/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.hibernate;

import eu.apenet.persistence.vo.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Mahbub
 */
public interface ApiKeyRepo extends JpaRepository<ApiKey, Long>{
    
}
