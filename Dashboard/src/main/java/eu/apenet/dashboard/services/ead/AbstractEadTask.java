package eu.apenet.dashboard.services.ead;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.ResumptionToken;

public abstract class AbstractEadTask {
	protected Logger logger = Logger.getLogger(getClass());

	protected void execute(Ead ead) throws Exception {
		execute(ead, new Properties());
	}

	protected abstract void execute(Ead ead, Properties properties) throws Exception;

	protected abstract String getActionName();

	protected void logAction(Ead ead, boolean success) {
		XmlType xmlType = XmlType.getEadType(ead);
		String successString = "succeed";
		if (!success) {
			successString = "failed";
		}
		logger.info("Ead " + ead.getEadid() + "(" + xmlType.getName() + "): " + getActionName() + " - " + successString);
	}

	protected static boolean isBeingHarvested () {
		boolean result = true;
		ResumptionTokenDAO resumptionTokenDAO = DAOFactory.instance().getResumptionTokenDAO();
		List<ResumptionToken> listValidResumptionTokens = resumptionTokenDAO.getGreaterResumptionTokensThan(new Date());
		if(listValidResumptionTokens==null || listValidResumptionTokens.isEmpty()){
			result = false;
		}
		
		resumptionTokenDAO = null;
		listValidResumptionTokens = null;
		return result;
	}
}
