package eu.apenet.persistence.dao;

import eu.apenet.persistence.factory.DAOFactory;
import eu.archivesportaleurope.persistence.jpa.AbstractJpaTestCase;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import org.junit.Test;

/**
 * User: Yoann Moranville
 * Date: 29/01/2013
 *
 * @author Yoann Moranville
 */
public class FindingAidDAOTest extends AbstractJpaTestCase {

    @Test
    public void testGetUnlinkedFindingAids() {
        final int aiId = 1;
        final int start = 0;
        final int maxResults = 100000;
        for(int i = 0; i < 10; i++)
            DAOFactory.instance().getFindingAidDAO().getFindingAidsNotLinkedByArchivalInstitution(aiId, start, maxResults);
        for(int i = 0; i < 10; i++)
            DAOFactory.instance().getFindingAidDAO().getFindingAidsNotLinked(aiId);
    }
}
