package eu.apenet.dashboard.manual.contentmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.DashboardAPEnetException;
import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.QueueItem;
import eu.apenet.persistence.vo.SourceGuide;

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




	/**
	 * This method provides the functionality of only delete a file from index
	 * and not on the system.
	 * 
	 * @param id
	 * @param xmlType
	 * @param aiId
	 * @throws IOException
	 * @throws SolrServerException
	 * @throws Exception
	 */
	public static void deleteOnlyFromIndex(Integer id, XmlType xmlType, Integer aiId, boolean commits) throws Exception {

	}
}
