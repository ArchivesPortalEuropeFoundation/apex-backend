package eu.apenet.persistence.hibernate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class HibernateConfigurator {
	private static final Logger LOGGER = Logger.getLogger(HibernateConfigurator.class);
	private Map<String,Configuration> configurations = new HashMap<String, Configuration>();
	private String currentConfigurationLocation;
	private DataSource currentDataSource;
	private SimpleJdbcTemplate currentSimpleJdbcTemplate;
	
	private static HibernateConfigurator instance;
	
	private HibernateConfigurator(){
		
	}
	public static HibernateConfigurator getInstance(){
		if (instance == null){
			instance = new HibernateConfigurator();
		}
		return instance;
	}
	public void init(String configurationLocation, boolean isDashboard){
		if (!configurationLocation.equals(currentConfigurationLocation)){
			if (currentConfigurationLocation != null){
				HibernateUtil.rollbackDatabaseTransaction();
				HibernateUtil.closeDatabaseSession();
				HibernateUtil.closeSessionFactory();
			}
//			String datasourceValue = null;
//            if(isDashboard){
//            	datasourceValue = "java:comp/env/jdbc/APEnetDatabaseDashboard";
//            }else{
//            	datasourceValue = "java:comp/env/jdbc/APEnetDatabasePortal";
//            }	
			
			//Properties hibernateProperties = configuration.getProperties();

			Properties dataSourceProperties = new Properties();
			dataSourceProperties.put("driverClassName", "org.postgresql.Driver");
			dataSourceProperties.put("url", System.getProperty("db_url"));
			String username = null;
			String password = null;
            if(isDashboard){
            	username= System.getProperty("db_username_dash");
            	password = System.getProperty("db_pwd_dash") ;
            } else {
            	username= System.getProperty("db_username_portal");
            	password = System.getProperty("db_pwd_portal");

            }
            dataSourceProperties.put("username", username);
		    dataSourceProperties.put("password", password);
			try {
				currentDataSource = BasicDataSourceFactory.createDataSource(dataSourceProperties);
//                SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
//                builder.bind(datasourceValue, currentDataSource);
//                builder.activate();
                initHibernate(configurationLocation, username, password);
			} catch (Exception e) {
				LOGGER.fatal(e.getMessage(),e);
				throw new BadConfigurationException(e);
			}
			
			currentSimpleJdbcTemplate = new SimpleJdbcTemplate(currentDataSource);
		}

	}
	private void initHibernate(String configurationLocation, String username, String password){
		Configuration configuration = configurations.get(configurationLocation);
		currentConfigurationLocation = configurationLocation;
		if (configuration == null){
			//configuration = new Configuration().configure(configurationLocation);
	
    		configuration = new Configuration();
            configuration.setProperty("hibernate.connection.url", System.getProperty("db_url"));
    		configuration.setProperty("hibernate.connection.username", username);
    		configuration.setProperty("hibernate.connection.password", password);

            configuration = configuration.configure(configurationLocation);
			configurations.put(configurationLocation, configuration);
		}
		HibernateUtil.init(configuration);
	}
	public DataSource getCurrentDataSource() {
		return currentDataSource;
	}
	public SimpleJdbcTemplate getCurrentSimpleJdbcTemplate() {
		return currentSimpleJdbcTemplate;
	}

}
