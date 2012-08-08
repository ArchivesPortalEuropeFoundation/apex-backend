package eu.apenet.persistence.hibernate;

import javax.sql.DataSource;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public abstract class AbstractHibernateTestCase {

	protected DataSource dataSource;
	
	protected SimpleJdbcTemplate simpleJdbcTemplate;


	public AbstractHibernateTestCase() {
        this("/hibernate.cfg.xml", true);
	}

	public AbstractHibernateTestCase(String hibernateConfigurationLocation, boolean isDashboard) {
        if(System.getProperty("db_url") == null) {
            System.setProperty("db_url", "jdbc:postgresql:apexv1");
            System.setProperty("db_username_dash", "apenet_dashboard");
            System.setProperty("db_pwd_dash", "AP3n3tSQLD4sh");
        }
		HibernateConfigurator.getInstance().init(hibernateConfigurationLocation, isDashboard);
		dataSource = HibernateConfigurator.getInstance().getCurrentDataSource();
		simpleJdbcTemplate = HibernateConfigurator.getInstance().getCurrentSimpleJdbcTemplate();

	}
	protected void changeHibernateLocation(String hibernateConfigurationLocation, boolean isDashboard) {
		HibernateConfigurator.getInstance().init(hibernateConfigurationLocation, isDashboard);
		dataSource = HibernateConfigurator.getInstance().getCurrentDataSource();
		simpleJdbcTemplate = HibernateConfigurator.getInstance().getCurrentSimpleJdbcTemplate();

	}
	
	@BeforeClass
	public static void classSetUp() {
		Logger rootLogger = Logger.getRootLogger();
		if (!rootLogger.getAllAppenders().hasMoreElements()) {
			rootLogger.setLevel(Level.INFO);
			rootLogger.addAppender(new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN)));
		}

	}

	@Before
	public void initNamingContext() throws Exception {

	}

	@After
	public void clearNamingContext() {
		HibernateUtil.rollbackDatabaseTransaction();
		HibernateUtil.closeDatabaseSession();
	}

}
