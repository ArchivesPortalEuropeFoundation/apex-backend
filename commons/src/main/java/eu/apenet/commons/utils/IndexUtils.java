package eu.apenet.commons.utils;

import org.apache.log4j.Logger;

/**
 * User: Jara Alvarez
 * Date: Dec 21, 2011
 *
 * @author Jara Alvarez
 */

public class IndexUtils {

	private static Boolean indexing = false;
    
	public static Boolean getIndexing() {
		return indexing;
	}
	public static void setIndexing(Boolean indexing) {
		IndexUtils.indexing = indexing;
	}
    
}
