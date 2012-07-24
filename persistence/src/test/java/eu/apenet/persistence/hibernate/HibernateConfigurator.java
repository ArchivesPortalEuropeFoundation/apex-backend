package eu.apenet.persistence.hibernate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class HibernateConfigurator {

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
			String datasourceValue = null;
			Configuration configuration = configurations.get(configurationLocation);
			currentConfigurationLocation = configurationLocation;
			if (configuration == null){
				//configuration = new Configuration().configure(configurationLocation);
                if(isDashboard){
                	datasourceValue = "java:comp/env/jdbc/APEnetDatabaseDashboard";
                }else{
                	datasourceValue = "java:comp/env/jdbc/APEnetDatabasePortal";
                }		
        		configuration = new Configuration();
                configuration.setProperty("hibernate.connection.datasource", datasourceValue);
                configuration = configuration.configure(configurationLocation);
				configurations.put(configurationLocation, configuration);
			}
			
			Properties hibernateProperties = configuration.getProperties();

			Properties dataSourceProperties = new Properties();
			dataSourceProperties.put("driverClassName", hibernateProperties.get("hibernate.connection.driver_class"));
			dataSourceProperties.put("url", System.getProperty("db_url"));
            if(isDashboard){
			    dataSourceProperties.put("username", System.getProperty("db_username_dash"));
			    dataSourceProperties.put("password", System.getProperty("db_pwd_dash"));
            } else {
                dataSourceProperties.put("username", System.getProperty("db_username_portal"));
			    dataSourceProperties.put("password", System.getProperty("db_pwd_portal"));
            }
			try {
				currentDataSource = BasicDataSourceFactory.createDataSource(dataSourceProperties);
                SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
                builder.bind(datasourceValue, currentDataSource);
                builder.activate();
                HibernateUtil.init(configuration);
			} catch (Exception e) {
				throw new BadConfigurationException(e);
			}
			
			currentSimpleJdbcTemplate = new SimpleJdbcTemplate(currentDataSource);
		}

	}
	public DataSource getCurrentDataSource() {
		return currentDataSource;
	}
	public SimpleJdbcTemplate getCurrentSimpleJdbcTemplate() {
		return currentSimpleJdbcTemplate;
	}

}
