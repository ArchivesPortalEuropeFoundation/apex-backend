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

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		generateXxxPropertiesFile();

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
	private static void sortPropertiesFile()throws IOException{
		System.setProperty("file.encoding", "UTF-8");
		File propertiesDir = new File(
				"/home/bastiaan/apex/apexv1/workspace-dashboard/portal-project/Dashboard/src/main/resources/i18n");
		for (File file : propertiesDir.listFiles()) {
			if (file.getName().startsWith("ApplicationResources_")) {
				Properties properties = new OwnProperties();
				InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				properties.load(inputStreamReader);
				//inputStreamReader.close();
				File newFile = new File(propertiesDir, "" + file.getName());
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8");
				properties.store(outputStreamWriter, "");
				outputStreamWriter.flush();
				outputStreamWriter.close();
			}
		}
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
