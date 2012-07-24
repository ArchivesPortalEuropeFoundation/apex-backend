package eu.apenet.commons.infraestructure;

import java.util.Properties;

public class ConfigProperties {

	private static Properties properties = new Properties();
	
	static{
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			properties.load(
				cl.getResourceAsStream("Config.properties"));
		} catch (Throwable t) {
			throw new ExceptionInInitializerError(t);
		}
	}
	
	public ConfigProperties(){};
	
	public String get(String key) {
		return properties.getProperty(key).trim();
	}
}