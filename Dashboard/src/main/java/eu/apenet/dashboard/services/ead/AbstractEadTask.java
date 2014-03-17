package eu.apenet.dashboard.services.ead;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;

public abstract class AbstractEadTask {
	protected Logger logger = Logger.getLogger(getClass());

	protected void execute(Ead ead) throws Exception {
		execute(ead, new Properties());
	}

	protected abstract void execute(Ead ead, Properties properties) throws Exception;

	protected abstract String getActionName();

	protected void logAction(Ead ead) {
		logAction(ead, null);
	}
	
	protected void logAction(Ead ead, Exception e) {
		XmlType xmlType = XmlType.getContentType(ead);
		String successString = "succeed";
		if (e != null) {
			successString = "failed";
		}
		logger.info("Ead " + ead.getEadid() + "(" + xmlType.getName() + "): " + getActionName() + " - " + successString);
	}
	protected void logAction(Ead ead, long milliseconds) {
		XmlType xmlType = XmlType.getContentType(ead);
		String successString = "succeed";
		logger.info("Ead " + ead.getEadid() + "(" + xmlType.getName() + "): " + getActionName() + " - " + successString + " - " + milliseconds +"ms");
	}
	protected void logSolrAction(Ead ead, String message,long solrMilliseconds, long milliseconds) {
		XmlType xmlType = XmlType.getContentType(ead);
		String successString = "succeed";
		logger.info("Ead " + ead.getEadid() + "(" + xmlType.getName() + "): " + getActionName() + " - " + message + " - " + successString + " - s:" + solrMilliseconds +"ms" + " - o:" + milliseconds +"ms");
	}
	protected static boolean isBeingHarvested () {
		return DAOFactory.instance().getResumptionTokenDAO().containsValidResumptionTokens(new Date());

	}
}
