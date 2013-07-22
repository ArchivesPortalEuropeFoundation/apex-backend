package eu.archivesportaleurope.persistence.jpa;

import javax.sql.DataSource;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.JdbcTemplate;

import eu.archivesportaleurope.database.mock.DatabaseConfigurator;

public abstract class AbstractJpaTestCase {
	private static final Logger LOGGER = Logger.getLogger(AbstractJpaTestCase.class);

	protected DataSource dataSource;
	
	protected JdbcTemplate jdbcTemplate;


	public AbstractJpaTestCase(){
		try {
			DatabaseConfigurator.getInstance().init();
			dataSource = DatabaseConfigurator.getInstance().getCurrentDataSource();
			jdbcTemplate = DatabaseConfigurator.getInstance().getCurrenJdbcTemplate();
			JpaUtil.init();
		} catch (Exception e) {
			LOGGER.fatal(e.getMessage(),e);
			throw new RuntimeException(e);
		}

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
		JpaUtil.rollbackDatabaseTransaction();
		JpaUtil.closeDatabaseSession();
	}

}
