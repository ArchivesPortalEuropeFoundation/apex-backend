package eu.apenet.persistence.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import eu.apenet.persistence.dao.NormalUserDAO;
import eu.apenet.persistence.dao.UserStateDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.NormalUser;
import eu.apenet.persistence.vo.UserState;

@Ignore
public class NormalUserDAOTest extends AbstractHibernateTestCase{
	
	/*TODO: The test is not finish, the tasks related to NormalUsers are still not finish, 
	 * that is the reason because it is update with all methods commented. Also the system.out calls are
	 * not removed for the same reason...*/

	private NormalUserDAO daoNormalUser= null;
	private UserStateDAO daoUserState = null;
	
	public NormalUserDAOTest (){
	       super("/hibernate.cfg.xml", false);
	}
	
	@Before
	public void initConfigurationPortal() throws NamingException{
        daoNormalUser = DAOFactory.instance().getNormalUserDAO();
        daoUserState = DAOFactory.instance().getUserStateDAO();
 	} 
	
	@Test
	public void testFindAll() {
	    try {
	        List<NormalUser> list = daoNormalUser.findAll();
	        Assert.assertNotNull(list);
	    } catch (Exception e){
	        throw new RuntimeException(e);
	    }		
	}
	
	
	//@Test
	public void testStoreNuOperation() {
		NormalUser result = new NormalUser();
		result.setNick("AdminNormalUserDAOTest" );
		result.setEmailAddress("adminNormalUserDAOTest@adminNormalUserDAOTest.es" );
		result.setPwd("PwdAdminNormalUserDAOTest");
		
		UserState userstate = new UserState();
		userstate.setUsId(new Long(0));
		userstate.setState("Blocked");
		
		result.setUserState(userstate);
	
		//OperationType opType = new OperationType();
		//opType.setOtId(2); //0;"Registration"
		//opType.setOptype("Auth Failure");
		//opType.setOtId(new Integer(1)); //0;"Registration"
		//opType.setOptype("Log in");
		//opType.setOtId(new Integer(0)); //0;"Registration"
		//opType.setOptype("Registration");
		
	}
	
	
	
	//@Test
	public void testStoreNormalUser() {
		NormalUser result = new NormalUser();
		result.setNick("AdminNormalUserDAOTest" );
		result.setEmailAddress("adminNormalUserDAOTest@adminNormalUserDAOTest.es" );
		result.setPwd("PwdAdminNormalUserDAOTest");
		UserState userstate = new UserState();
		//userstate.setUsId(new Long(1));
		//userstate.setState("Active");
		//result.setUserState(userstate);
		
		userstate.setUsId(new Long(0));
		userstate.setState("Blocked");
		result.setUserState(userstate);
		
		daoNormalUser.store(result);
		//System.out.println("Normal user saved succesfully in BD with id:"
		//		+ result.getUId() + " User: "+ result.toString());
		Assert.assertTrue(result.getUId() > 0);
		Map<String, Object> result1 = this.simpleJdbcTemplate.queryForMap("SELECT * FROM normal_user");
		
		Assert.assertEquals(result.getNick(),result1.get("nick"));
		Assert.assertEquals(result.getEmailAddress(), result1.get("email_address"));
		Assert.assertEquals(result.getPwd(), result1.get("pwd"));
		Assert.assertEquals(result.getUserState().getUsId(), result1.get("us_id"));
	}

	
	//@Test
	public void testLogin() {
		//NormalUser result2 = new NormalUser();
		//result2 = (NormalUser) 
		NormalUser inputLogin = new NormalUser();
		inputLogin.setNick("UserTestLogin" );
		inputLogin.setEmailAddress("userTestLogin@userTestLogin.es" );
		inputLogin.setPwd("PwdUserTestLogin");
		UserState userstate = new UserState();
		userstate.setUsId(new Long(1));
		userstate.setState("Active");
		inputLogin.setUserState(userstate);
		daoNormalUser.store(inputLogin);
//		System.out.println("Login user saved succesfully in BD with id:"
//				+ inputLogin.getUId() + " User: "+ inputLogin.toString());
		Assert.assertTrue(inputLogin.getUId() > 0);
		NormalUser exitLoginActived = new NormalUser();
		NormalUser exitLoginBlocked = new NormalUser();
		exitLoginActived = daoNormalUser.loginUser("UserTestLogin", "PwdUserTestLogin",true);
		exitLoginBlocked = daoNormalUser.loginUser("UserTestLogin", "PwdUserTestLogin",false);
		Assert.assertNull(exitLoginBlocked);
//		System.out.println("Login user recover succesfully from BD with id:"
//				+ exitLogin.getUId() + " User: "+ exitLogin.toString());
		System.out.println("Estado: "+ exitLoginActived.getUserState().getState());
		Assert.assertNotNull(exitLoginActived);
		
		System.out.println("Login user recover succesfully from BD with id:"+ exitLoginActived.getUId() + " User: "+ exitLoginActived.toString());
		daoNormalUser.delete(exitLoginActived);
		//Assert.assertEquals(expected, actual)
	}
	
	
	
	//@Test
	public void testUserState() {
		System.out.println("testUserState");
		NormalUserDAO nuDAO = DAOFactory.instance().getNormalUserDAO();
		Collection <String> listBlocked = null;
		Collection <String> listActived = null;
		Collection <String> listEmpty = null;
		listBlocked = new LinkedList<String>();  
		listActived = new LinkedList<String>();
		listBlocked.add("Blocked");  
		listActived.add("Actived");  
		listEmpty = new LinkedList<String>();
		
		long numGnalUsers = nuDAO.countNormalUsers("", "", "", listEmpty); 
		System.out.println("Número de usuarios total:"+numGnalUsers);
		System.out.println("------------------------------------------------------");
		
		long numBlockedUsers = nuDAO.countNormalUsers("", "", "", listBlocked); 
		System.out.println("Número de usuarios con condición de estado 'Blocked':"+numBlockedUsers);
		System.out.println("------------------------------------------------------");
		
		
		long numActivedUsers = nuDAO.countNormalUsers("", "", "", listActived); 
		System.out.println("Número de usuarios con condición de estado 'Actived':"+numActivedUsers);
		System.out.println("------------------------------------------------------");
		
		
		System.out.println("Lista de usuarios activos:");
		int pageNumber=1;
		int pagesize=20;
		List<NormalUser> myUsers = nuDAO.getNormalUsersPage("", "", "", listActived ,"", false, pageNumber, pagesize);
        //List<NormalUser> myUsers = nuDAO.getNormalUsersPage("AdminHibernate", "", "PwdAdminHibernate", listEmpty ,"", false, pageNumber, pagesize);
        //List<NormalUser> myUsers = nuDAO.getNormalUsersPage("AdminHibernate", "", "PwdAdminHibernate", listEmpty ,"emailAddress", true, pageNumber, pagesize);
        if (myUsers.size() > 0) {
           int j=0;
           for (Iterator<NormalUser> i = myUsers.iterator(); i.hasNext() ; ) {
              NormalUser nextUser = (NormalUser) i.next();
              j++;
              System.out.println("Nick NormalUser number("+j+"): "+ nextUser.getNick());
              System.out.println("Email NormalUser number("+j+"):"+ nextUser.getEmailAddress());
              System.out.println("Pwd NormalUser number("+j+"):  "+ nextUser.getPwd());
              //nextUser.setPassword("secret");
              System.out.println("--------------------------------------------------------------");
           }
        }else {
           System.out.println("Didn't find any matching users..");
        }
		
		long numUsersBlocked = nuDAO.countNormalUsers("AdminHibernate1", "adminHibernate@adminHibernate", "PwdAdminHibernate", listBlocked);
		System.out.println("Number of users AdminHibernate1 with condition of userState = Blocked "+numUsersBlocked);
		
		long numUsersActivated = nuDAO.countNormalUsers("AdminHibernate2", "adminHibernate@adminHibernate", "PwdAdminHibernate", listActived);
		System.out.println("Number of users AdminHibernate2 with condition of userState = Actived "+numUsersActivated);
		
	}
	
	
	//@Before
	//@After
	public void cleanTable(){
		SimpleJdbcTestUtils.deleteFromTables(simpleJdbcTemplate, "normal_user");
	}

}
