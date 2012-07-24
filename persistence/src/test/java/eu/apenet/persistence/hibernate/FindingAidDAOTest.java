package eu.apenet.persistence.hibernate;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FindingAid;
@Ignore
public class FindingAidDAOTest extends AbstractHibernateTestCase{


	@Test
	public void testFindAll() {

		//FindingAidDAO dao = new FindingAidDAO();
		FindingAidDAO dao = DAOFactory.instance().getFindingAidDAO();
		
        try {
            List<FindingAid> list = dao.findAll();
            Assert.assertNotNull(list);
        } catch (Exception e){
            throw new RuntimeException(e);
        }		
	}

}
