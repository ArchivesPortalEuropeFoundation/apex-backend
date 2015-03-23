package eu.archivesportaleurope.xml;

import java.io.InputStream;

public class AbstractTest {
	
	protected InputStream getInputStream(String path){
		return getClass().getResourceAsStream(path);
	}
}
