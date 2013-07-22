package eu.apenet.dashboard.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class TestProperties {

	private static final String SUFFIX = ".properties";
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		generateNewDashboardPropertiesFiles();

	}
	private static void generateXxxPropertiesFile()throws IOException{
		System.setProperty("file.encoding", "UTF-8");
		File propertiesFile = new File(
				"/home/bastiaan/apex/apexv1/workspace-dashboard/portal-project/Dashboard/src/main/resources/i18n/ApplicationResources.properties");
				Properties properties = new OwnProperties();
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(propertiesFile), "UTF-8");
				properties.load(inputStreamReader);
				//inputStreamReader.close();
				Iterator<Entry<Object, Object>> propertiesIterator = properties.entrySet().iterator();
				while (propertiesIterator.hasNext()){
					Entry<Object, Object> entry = propertiesIterator.next();
					String key = (String) entry.getKey();
					String value =(String) entry.getValue();
					properties.put(key, value);
				}
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(propertiesFile), "UTF-8");
				properties.store(outputStreamWriter, "");
				outputStreamWriter.flush();
				outputStreamWriter.close();

	}
	private static void generateNewPortalPropertiesFiles()throws IOException{
		System.setProperty("file.encoding", "UTF-8");
		String shared ="shared-resources";
		String portal ="portal-resources";
		File propertiesDir = new File(
				"/home/bastiaan/apex/apexv1/workspace-new/frontend-project/ape-portal/src/main/resources/i18n");
		Properties sharedProperties = getProperties(new File(propertiesDir, shared +SUFFIX));
		Properties portalProperties = getProperties(new File(propertiesDir, portal +SUFFIX));
		for (File file : propertiesDir.listFiles()) {
			if (file.getName().startsWith("ApplicationResources_")) {
				String langSuffix = file.getName().replaceFirst("ApplicationResources", "");
				Properties sharedLocaleProperties = new OwnProperties();
				Properties portalLocaleProperties = new OwnProperties();
				Properties properties = getProperties(file);
				for (Entry<Object, Object> entry : properties.entrySet()){
					Object key = entry.getKey();
					boolean processed = putEntry(sharedProperties, sharedLocaleProperties, entry);
					if (!processed){
						processed = putEntry(portalProperties, portalLocaleProperties, entry);
					}
					if (!processed){
						System.out.println(key + " dropped");
					}
				}
				writeProperties(sharedLocaleProperties, new File(propertiesDir, shared +langSuffix));
				writeProperties(portalLocaleProperties, new File(propertiesDir, portal +langSuffix));
			}
		}
	}
	private static void generateNewDashboardPropertiesFiles()throws IOException{
		System.setProperty("file.encoding", "UTF-8");
		String shared ="shared-resources";
		String portal ="dashboard-resources";
		File propertiesDir = new File(
				"/home/bastiaan/apex/apexv1/workspace-new/backend-project/Dashboard/src/main/resources/i18n");
		Properties sharedProperties = getProperties(new File(propertiesDir, shared +SUFFIX));
		//Properties portalProperties = getProperties(new File(propertiesDir, portal +SUFFIX));
		for (File file : propertiesDir.listFiles()) {
			if (file.getName().startsWith("ApplicationResources")) {
				String langSuffix = file.getName().replaceFirst("ApplicationResources", "");
				Properties sharedLocaleProperties = new OwnProperties();
				Properties portalLocaleProperties = new OwnProperties();
				Properties properties = getProperties(file);
				for (Entry<Object, Object> entry : properties.entrySet()){
					Object key = entry.getKey();
					boolean processed = putEntry(sharedProperties, sharedLocaleProperties, entry);
					if (!processed){
						portalLocaleProperties.put(key, entry.getValue());
					}else{
						System.out.println(key + " dropped");
					}
				}
				//writeProperties(sharedLocaleProperties, new File(propertiesDir, shared +langSuffix));
				writeProperties(portalLocaleProperties,file);
			}
		}
	}
	private static boolean putEntry(Properties globalProperties, Properties localProperties,Entry<Object, Object> entry){
		Object key = entry.getKey();
		Object value = entry.getValue();
		if (globalProperties.containsKey(key)){
//			Object notTranslatedValue = globalProperties.get(key);
//			if (notTranslatedValue.equals(value)){
//				System.out.println(key + " has fake translation");
//			}else {
				localProperties.put(key, value);
				return true;
//			}
			
		}
		return false;
	}
	private static Properties getProperties(File file) throws IOException{
		Properties properties = new OwnProperties();
		InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
		properties.load(inputStreamReader);
		return properties;
	}
	private static void writeProperties(Properties properties, File file) throws IOException{
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		properties.store(outputStreamWriter, "");
		outputStreamWriter.flush();
		outputStreamWriter.close();
	}
	static class OwnProperties extends Properties {
		@Override
		public Set<Object> keySet() {
			return Collections.unmodifiableSet(new TreeSet<Object>(super.keySet()));
		}

		@Override
		public synchronized Enumeration<Object> keys() {
			// TODO Auto-generated method stub
			return new Vector(keySet()).elements();
		}

	}
}
