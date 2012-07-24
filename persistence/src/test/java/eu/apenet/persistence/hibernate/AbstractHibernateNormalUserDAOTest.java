package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;

import eu.apenet.persistence.vo.NormalUser;
import eu.apenet.persistence.vo.UserState;
@Ignore
public class AbstractHibernateNormalUserDAOTest extends AbstractHibernateTestCase {

	/**
	 * use a anonymous subclass to test all AbstractHibernateDAO methods
	 */
	private AbstractHibernateDAO<NormalUser, Long> abstractHibernateDAO = new AbstractHibernateDAO<NormalUser, Long>() {
	};
	private AbstractHibernateDAO<UserState, Long> userStateDAO = new AbstractHibernateDAO<UserState, Long>() {
	};
	private final static String INSERT_INTO_QUERY = "INSERT INTO normal_user (u_id, us_id, nick, email_address,pwd) VALUES (?,?,?, ?,?)";

	private NormalUser normalUser1;

	private NormalUser normalUser2;
	
	private NormalUser normalUser3;
	
	
	public AbstractHibernateNormalUserDAOTest (){
	       super("/hibernate.cfg.xml", false);
	}
	

    @Before
	public void fillExamples(){
		UserState userState1 = new UserState(new Long(1));
		UserState userState2 = new UserState(new Long(1));
		normalUser1 = new NormalUser(new Long(1), userState1, "nick1", "nick1@apenet.com", "password1");
		normalUser2 = new NormalUser(new Long(2), userState2, "nick2", "nick1@apenet.com", "password2");
		normalUser3 = new NormalUser(new Long(3), userState2, "nick3", "nick3@apenet.com", "password3");
	}

	private void insertNormalUser(NormalUser normalUser) {
		simpleJdbcTemplate.update(INSERT_INTO_QUERY, normalUser.getUId(), normalUser.getUserState().getUsId(),
				normalUser.getNick(), normalUser.getEmailAddress(), normalUser.getPwd());

	}

	@Before
	@After
	public void cleanTable() {
		SimpleJdbcTestUtils.deleteFromTables(simpleJdbcTemplate, "normal_user");
	}

	public void fillTable(){
		insertNormalUser(normalUser1);
		insertNormalUser(normalUser2);
		insertNormalUser(normalUser3);
	}
	
	@Test
	public void testFindById() {
		fillTable();
		NormalUser result = abstractHibernateDAO.findById(normalUser1.getUId());
		Assert.assertEquals(normalUser1.getUId(), result.getUId());
	}

	@Test
	public void testFindByIds() {
		fillTable();
		List<Long> ids = new ArrayList<Long>();
		ids.add(normalUser1.getUId());
		ids.add(normalUser2.getUId());
		List<NormalUser> result = abstractHibernateDAO.findByIds(ids);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(normalUser1.getUId(), result.get(0).getUId());
		Assert.assertEquals(normalUser2.getUId(), result.get(1).getUId());
	}

	@Test
	public void testFindAll() {
		fillTable();
		List<NormalUser> result = abstractHibernateDAO.findAll();
		Assert.assertEquals(3, result.size());
	}

	@Test
	public void testFindByExampleTCollectionOfString() {
		fillTable();
		NormalUser example = new NormalUser();
		example.setNick(normalUser1.getNick());
		example.setEmailAddress(normalUser1.getEmailAddress());
		List<String> excludedProperties = new ArrayList<String>();
		List<NormalUser> result = abstractHibernateDAO.findByExample(example, excludedProperties);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(normalUser1.getUId(),result.get(0).getUId());	
		
		excludedProperties.add("nick");
		result = abstractHibernateDAO.findByExample(example, excludedProperties);
		Assert.assertEquals(2, result.size());
		
	}

	@Test
	public void testFindByExampleT() {
		fillTable();
		NormalUser example = new NormalUser();
		example.setNick(normalUser1.getNick());
		List<NormalUser> result = abstractHibernateDAO.findByExample(example);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(normalUser1.getUId(),result.get(0).getUId());
	}

	@Test
	public void testStoreT() {
		UserState userState = userStateDAO.findById(1l);
		NormalUser user = new NormalUser();
		user.setNick("AdminHibernate" );
		user.setEmailAddress("adminHibernate@adminHibernate.es" );
		user.setPwd("PwdAdminHibernate");
		user.setUserState(userState);
		abstractHibernateDAO.store(user);
		Map<String, Object> result = this.simpleJdbcTemplate.queryForMap("SELECT * FROM normal_user");
		Assert.assertEquals(user.getNick(),result.get("nick"));
		Assert.assertEquals(user.getEmailAddress(), result.get("email_address"));
		Assert.assertEquals(user.getPwd(), result.get("pwd"));
	}

	@Test
	public void testStoreCollectionOfT() {
		UserState userState = userStateDAO.findById(1l);
		NormalUser user = new NormalUser();
		user.setNick("AdminHibernate" );
		user.setEmailAddress("adminHibernate@adminHibernate.es" );
		user.setPwd("PwdAdminHibernate");
		user.setUserState(userState);
		NormalUser user1 = new NormalUser();
		user1.setNick("AdminHibernate" );
		user1.setEmailAddress("adminHibernate@adminHibernate.es" );
		user1.setPwd("PwdAdminHibernate");
		user1.setUserState(userState);
		List<NormalUser> users = new ArrayList<NormalUser>();
		users.add(user);
		users.add(user1);
		abstractHibernateDAO.store(users);
		Assert.assertEquals(2, SimpleJdbcTestUtils.countRowsInTable(simpleJdbcTemplate, "normal_user"));
	}

	@Test
	public void testDelete() {
		fillTable();
		Assert.assertEquals(3, SimpleJdbcTestUtils.countRowsInTable(simpleJdbcTemplate, "normal_user"));
		NormalUser user = new NormalUser();
		user.setUId(normalUser1.getUId());
		abstractHibernateDAO.delete(normalUser1);
		Assert.assertEquals(2, SimpleJdbcTestUtils.countRowsInTable(simpleJdbcTemplate, "normal_user"));
		
	}

}
