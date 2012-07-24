package eu.apenet.persistence.hibernate;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.HoldingsGuide;
@Ignore
public class HoldingsGuidesDAOTest extends AbstractHibernateTestCase{

        @Test
        public void countHoldingsGuides() {

                try {
                        HoldingsGuideDAO dao = DAOFactory.instance().getHoldingsGuideDAO();
                        List<HoldingsGuide> list = dao.findAll();
            Assert.assertNotNull(list);
        } catch (Exception e){
            throw new RuntimeException(e);
        }              
   }

}