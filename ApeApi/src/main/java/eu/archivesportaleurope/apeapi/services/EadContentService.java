/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.apenet.persistence.vo.Ead;
import eu.archivesportaleurope.apeapi.response.common.DetailContent;
import java.util.List;

/**
 *
 * @author kaisar
 */
public interface EadContentService {
    DetailContent findClevelContent(String id);
    List<DetailContent> findClevelContent(List<String> ids);
    List<DetailContent> getSomeClevelContent();
    DetailContent findEadContent(String id);
    Ead findEadById(String id);
}
