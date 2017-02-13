/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.ReindexDoc;
import java.util.List;

/**
 *
 * @author kaisar
 */
public interface ReindexDocDAO extends GenericDAO<ReindexDoc, Integer> {

    Long countDocs();

    ReindexDoc getFirstDoc();

    Long countDocs(int aiId);

    List<Object[]> countByArchivalInstitutions();

    List<ReindexDoc> getItemsOfInstitution(Integer aiId);
}
