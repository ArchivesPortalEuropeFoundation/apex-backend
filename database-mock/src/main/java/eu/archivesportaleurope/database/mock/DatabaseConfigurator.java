package eu.archivesportaleurope.database.mock;

import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.postgresql.Driver;

public class DatabaseConfigurator {

	private DataSource currentDataSource;
	private JdbcTemplate currentJdbcTemplate;

	private static DatabaseConfigurator instance;

	private DatabaseConfigurator() {

	}

	public static DatabaseConfigurator getInstance() {
		if (instance == null) {
			instance = new DatabaseConfigurator();
		}
		return instance;
	}

	public void init() throws NamingException {
		if (System.getProperty("db_url") == null) {
			System.setProperty("db_url", "jdbc:postgresql:apenet");
			System.setProperty("db_username", "apenet_dashboard");
			System.setProperty("db_password", "AP3n3tSQLD4sh");
		}
		String url = System.getProperty("db_url");
		String username = System.getProperty("db_username");
		String password = System.getProperty("db_password");
		currentDataSource = new SimpleDriverDataSource(new Driver(), url, username, password);
		currentJdbcTemplate = new JdbcTemplate(currentDataSource);
		NamingManager.setInitialContextFactoryBuilder(new DatabaseContextFactory(currentDataSource));
	}

	public DataSource getCurrentDataSource() {
		return currentDataSource;
	}

	public JdbcTemplate getCurrenJdbcTemplate() {
		return currentJdbcTemplate;
	}

}
