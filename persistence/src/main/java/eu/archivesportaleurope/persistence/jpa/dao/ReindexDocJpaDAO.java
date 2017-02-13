/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.persistence.jpa.dao;

import eu.apenet.persistence.dao.ReindexDocDAO;
import eu.apenet.persistence.hibernate.AbstractHibernateDAO;
import eu.apenet.persistence.vo.ReindexDoc;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author kaisar
 */
public class ReindexDocJpaDAO extends AbstractHibernateDAO<ReindexDoc, Integer> implements ReindexDocDAO {

    private final Logger log = Logger.getLogger(getClass());

    @Override
    public Long countDocs() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ReindexDoc getFirstDoc() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long countDocs(int aiId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object[]> countByArchivalInstitutions() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ReindexDoc> getItemsOfInstitution(Integer aiId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
