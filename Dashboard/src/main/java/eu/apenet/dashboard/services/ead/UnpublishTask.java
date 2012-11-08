package eu.apenet.dashboard.services.ead;

import java.io.IOException;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.UpdateSolrServerHolder;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.HoldingsGuide;

public class UnpublishTask extends AbstractEadTask {

	@Override
	protected void execute(Ead ead, Properties properties) throws Exception {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		XmlType xmlType = XmlType.getEadType(ead);
		EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
		EadContent eadContent;

		if (xmlType.equals(XmlType.EAD_HG))
			eadContent = eadContentDAO.getEadContentByHoldingsGuideId(ead.getId());
		else if (xmlType.equals(XmlType.EAD_FA))
			eadContent = eadContentDAO.getEadContentByFindingAidId(ead.getId());
		else
			eadContent = eadContentDAO.getEadContentBySourceGuideId(ead.getId());
		if (eadContent != null)
			eadContentDAO.deleteSimple(eadContent);

		logger.debug("Changing EAD (" + xmlType.getName() + ") state of the EAD with eadid " + ead.getEadid());
		ContentUtils.changeSearchable(ead, false);
		ead.setTotalNumberOfDaos(0l);
		ead.setTotalNumberOfUnits(0l);
		ead.setTotalNumberOfUnitsWithDao(0l);

		eadDAO.updateSimple(ead);
		logger.debug("Removing the EAD (" + xmlType.getName() + ") with eadid '" + ead.getEadid() + "' from the index");
		deleteFromSolr(ead.getEadid(), ead.getAiId());
		logger.info(xmlType.getName() + " with eadid: " + ead.getEadid()
				+ "has been unpublished");
        if(ead instanceof HoldingsGuide)
            ContentUtils.removeHoldingsGuideFromArchivalLandscape((HoldingsGuide)ead);
	}

	private static void deleteFromSolr(String eadid, int aiId) throws SolrServerException, IOException {
		UpdateSolrServerHolder server = UpdateSolrServerHolder.getInstance();
		server.deleteByQuery("(" + SolrFields.AI_ID + ":" + aiId + " AND " + SolrFields.EADID + ":\"" + eadid + "\")");
		server.commit();

	}
}
