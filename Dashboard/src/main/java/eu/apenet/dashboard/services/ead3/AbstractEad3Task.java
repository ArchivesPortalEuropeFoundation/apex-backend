/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead3;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author kaisar
 */
public abstract class AbstractEad3Task {

    protected Logger logger = Logger.getLogger(getClass());

    public void execute(Ead3 ead3) throws Exception {
        execute(ead3, new Properties());
    }

    protected abstract void execute(Ead3 ead3, Properties properties) throws Exception;

    protected abstract String getActionName();

    protected void logAction(Ead3 ead3) {
        logAction(ead3, null);
    }

    protected void logAction(Ead3 ead3, Exception e) {
        XmlType xmlType = XmlType.EAD_3;
        String successString = "succeed";
        if (e != null) {
            successString = "failed";
        }
        logger.info("Ead 3 " + ead3.getIdentifier() + "(" + xmlType.getName() + "): " + getActionName() + " - " + successString);
    }

    protected void logAction(Ead3 ead3, long milliseconds) {
        XmlType xmlType = XmlType.EAD_3;
        String successString = "succeed";
        logger.info("Ead 3 " + ead3.getIdentifier() + "(" + xmlType.getName() + "): " + getActionName() + " - " + successString + " - " + milliseconds + "ms");
    }

    protected void logSolrAction(Ead3 ead3, String message, long solrMilliseconds, long milliseconds) {
        XmlType xmlType = XmlType.EAD_3;
        String successString = "succeed";
        logger.info("EacCpf " + ead3.getIdentifier() + "(" + xmlType.getName() + "): " + getActionName() + " - " + message + " - " + successString + " - s:" + solrMilliseconds + "ms" + " - o:" + milliseconds + "ms");
    }

    protected static boolean isBeingHarvested() {
        return DAOFactory.instance().getResumptionTokenDAO().containsValidResumptionTokens(new Date());
    }
}
