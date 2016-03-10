/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.repository.mongo;

import eu.archivesportaleurope.apeapi.request.SearchRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author kaisar
 */
public interface SearchRepo extends MongoRepository<SearchRequest, String>{
    
}
