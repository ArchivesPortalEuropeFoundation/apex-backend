package eu.apenet.dashboard.services.ead;

import java.io.IOException;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.UpdateSolrServerHolder;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;

public class UnpublishTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "unpublish";
	}
	public static boolean valid(Ead ead){
		return ead.isPublished();
	}
	@Override
	public void execute(Ead ead, Properties properties) throws Exception {
		if (valid(ead)) {
			try {
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				XmlType xmlType = XmlType.getEadType(ead);
				logger.debug("Removing the EAD (" + xmlType.getName() + ") with eadid '" + ead.getEadid()
						+ "' from the index");
				deleteFromSolr(ead.getEadid(), ead.getAiId());
				logger.debug("Changing EAD (" + xmlType.getName() + ") state of the EAD with eadid " + ead.getEadid());
				ContentUtils.changeSearchable(ead, false);
				ead.setTotalNumberOfUnits(0l);
				ead.setTotalNumberOfUnitsWithDao(0l);
				eadDAO.store(ead);
				logAction(ead, true);
			} catch (Exception e) {
				logAction(ead, false);
				throw new APEnetException("Could not unpublish the file with ID: " + ead.getId(), e);
			}
		}
	}

	private static void deleteFromSolr(String eadid, int aiId) throws SolrServerException, IOException {
		UpdateSolrServerHolder server = UpdateSolrServerHolder.getInstance();
		server.deleteByQuery("(" + SolrFields.AI_ID + ":" + aiId + " AND " + SolrFields.EADID + ":\"" + eadid + "\")");
		server.commit();

	}
}
