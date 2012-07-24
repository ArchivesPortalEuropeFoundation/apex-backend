package eu.apenet.persistence.hibernate;

import java.util.Date;
import java.util.List;
import javax.naming.NamingException;

import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import eu.apenet.persistence.vo.SentMailRegister;
import eu.apenet.persistence.dao.SentMailRegisterDAO;
import eu.apenet.persistence.factory.DAOFactory; 
@Ignore
public class SentMailRegisterDAOTest extends AbstractHibernateTestCase{
	
	/*TODO: The test is not finish, the tasks related to Normal, Sent Mail Registers and Temporal Users are still not finish
	 * until after the integration process, that is the reason because it is update with some methods commented. */

	private Logger log = Logger.getLogger(SentMailRegisterDAOTest.class);
	
	private SentMailRegisterDAO daoSentMailRegister= null;
	
	public SentMailRegisterDAOTest (){
	       super("/hibernate.cfg.xml", false);
	}
	
	@Before
	public void initConfigurationPortal() throws NamingException{
		daoSentMailRegister = DAOFactory.instance().getSentMailRegisterDAO();
	} 
	
	@Test
	public void testFindAll() {
	    try {
	        List<SentMailRegister> list = daoSentMailRegister.findAll();
	        Assert.assertNotNull(list);
	    } catch (Exception e){
	        throw new RuntimeException(e);
	    }		
	}
	
	
	@Test
	public void testStoreSentMailRegisterBasic() {
	
	log.info("SentMailRegisterDAOTest: testStoreSentMailRegisterBasic() method is called");
	SentMailRegister result = new SentMailRegister();
	result.setValidationLink("TestValidationLinkSentMailRegisterDAOTest" );
	result.setDate(new Date());
	result.setEmailAddress("adminSentMailRegisterDAOTest@adminSentMailRegisterDAOTest.es" );
	daoSentMailRegister.store(result);
	
	log.info("SentMailRegisterDAOTest: testStoreSentMailRegister() sent mail register saved succesfully in BD with SmrId:"
			+ result.getSmrId() + "\n Temporal User: "+ result.toString());
			Assert.assertTrue(result.getSmrId() > 0);
			Map<String, Object> result1 = this.simpleJdbcTemplate.queryForMap("SELECT * FROM sent_mail_register");
			
	Assert.assertEquals(result.getValidationLink(),result1.get("validation_link"));
	Assert.assertEquals(result.getDate(), result1.get("date"));
	Assert.assertEquals(result.getEmailAddress(), result1.get("email_address"));
	}
	
	
	
	
	//@Before
	//@After
	public void cleanTable(){
		SimpleJdbcTestUtils.deleteFromTables(simpleJdbcTemplate, "sent_mail_register");
	}

}
