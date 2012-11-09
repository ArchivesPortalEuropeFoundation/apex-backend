package eu.apenet.dashboard.manual.contentmanager;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.vo.Ead;

/**
 * This class is used for store all the actions which will be used in
 * ContentManager, the only different is these actions are related to indexing
 * process. It's used (extended) into ContentManager class.
 * 
 */
public abstract class ContentManagerIndexer {
	private static final Logger log = Logger.getLogger(ContentManagerIndexer.class);



	public static boolean indexProcess(XmlType xmlType, int id) throws Exception {
		return true;
	}



	public static void indexFromQueue(Ead ead) throws Exception {
	}

	public static void reindex(XmlType xmlType, int id) throws Exception {
//		Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
//		SecurityContext.get().checkAuthorized(ead);
//		EADParser.parseEadAndIndex(ead);
	}





}
