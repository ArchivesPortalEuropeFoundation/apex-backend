/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.Ead3SolrServerHolder;
import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.Ead3DAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead3;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import java.util.Properties;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author kaisar
 */
public class UnpublishTask extends AbstractEad3Task {

    public static boolean valid(Ead3 ead) {
        return ead.isPublished();
    }

    @Override
    protected void execute(Ead3 ead3, Properties properties) throws Exception {
        Ead3DAO ead3DAO = DAOFactory.instance().getEad3DAO();
        XmlType xmlType = XmlType.getContentType(ead3);
        long solrTime = deleteFromSolr(ead3.getId(), ead3.getAiId());
        JpaUtil.beginDatabaseTransaction();
        ead3.setTotalNumberOfUnits(0l);
        ead3.setTotalNumberOfDaos(0l);
        ead3.setPublished(false);
        ead3DAO.insertSimple(ead3);
        JpaUtil.commitDatabaseTransaction();
    }

    @Override
    protected String getActionName() {
        return "unpublish";
    }

    private static long deleteFromSolr(int eadId, int aiId) throws SolrServerException {
        return Ead3SolrServerHolder.getInstance().deleteByQuery("(" + Ead3SolrFields.AI_ID
                + ":" + aiId + " AND " + Ead3SolrFields.EAD_ID + ":" + eadId + ")");
    }

}
