package eu.apenet.dashboard.services.eaccpf;

import java.util.Properties;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.dashboard.services.eaccpf.xml.stream.XmlEacCpfParser;
import eu.apenet.dashboard.services.eaccpf.xml.stream.publish.EacCpfSolrPublisher;
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
			try {
				long startTime = System.currentTimeMillis();
				long solrTime = XmlEacCpfParser.parseAndPublish(eacCpf);
				logSolrAction(eacCpf, "", solrTime, System.currentTimeMillis()-(startTime+solrTime));
			} catch (Exception e) {
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
