/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.eaccpf;

import java.io.IOException;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.UpdateSolrServerHolder;
import eu.apenet.dashboard.services.eaccpf.publish.SolrPublisher;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 *
 * @author papp
 */
class UnpublishTask extends AbstractEacCpfTask {

    @Override
    protected String getActionName() {
        return "unpublish";
    }

    static boolean valid(EacCpf eacCpf) {
        return eacCpf.isPublished();
    }
    
	@Override
	public void execute(EacCpf eacCpf, Properties properties) throws Exception {
		if (valid(eacCpf)) {
			try {
				long startTime = System.currentTimeMillis();
				EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
				logger.debug("Removing the EacCpf with id '" + eacCpf.getIdentifier()+ "' from the index");
				long solrTime = new SolrPublisher().unpublish(eacCpf);
				JpaUtil.beginDatabaseTransaction();
				ContentUtils.changeSearchable(eacCpf, false);
				eacCpfDAO.insertSimple(eacCpf);
				JpaUtil.commitDatabaseTransaction();
				logSolrAction(eacCpf, "", solrTime, System.currentTimeMillis()-(startTime+solrTime));
			} catch (Exception e) {
				logAction(eacCpf, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}
		}
	}

}
