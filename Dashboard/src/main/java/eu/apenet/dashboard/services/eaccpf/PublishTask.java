/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.eaccpf;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.dashboard.services.eaccpf.publish.SolrPublisher;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 *
 * @author papp
 */
public class PublishTask extends AbstractEacCpfTask{
    @Override
    protected void execute(EacCpf eacCpf, Properties properties) throws Exception {
		if (valid(eacCpf)) {
			SolrPublisher solrPublisher = new SolrPublisher();
			try {
				long startTime = System.currentTimeMillis();
				EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
				solrPublisher.publish(eacCpf);
				solrPublisher.commitSolrDocuments();
				long solrTime = solrPublisher.getSolrTime();
				ContentUtils.changeSearchable(eacCpf, true);
				eacCpfDAO.insertSimple(eacCpf);
				JpaUtil.commitDatabaseTransaction();
				logSolrAction(eacCpf, "", solrTime, System.currentTimeMillis()-(startTime+solrTime));
			} catch (Exception e) {
				JpaUtil.rollbackDatabaseTransaction();
				solrPublisher.unpublish(eacCpf);
				logAction(eacCpf, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}
		}
    }

    @Override
    protected String getActionName() {
        return "publish";
    }

    static boolean valid(EacCpf eacCpf) {
        return ValidatedState.VALIDATED.equals(eacCpf.getValidated()) && !eacCpf.isPublished();
    }
}
