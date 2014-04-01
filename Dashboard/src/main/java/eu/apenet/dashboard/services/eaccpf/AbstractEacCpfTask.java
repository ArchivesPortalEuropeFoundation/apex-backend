/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.eaccpf;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public abstract class AbstractEacCpfTask {

    protected Logger logger = Logger.getLogger(getClass());

    public void execute(EacCpf eacCpf) throws Exception {
        execute(eacCpf, new Properties());
    }

    protected abstract void execute(EacCpf eacCpf, Properties properties) throws Exception;

    protected abstract String getActionName();

    protected void logAction(EacCpf eacCpf) {
        logAction(eacCpf, null);
    }

    protected void logAction(EacCpf eacCpf, Exception e) {
        XmlType xmlType = XmlType.EAC_CPF;
        String successString = "succeed";
        if (e != null) {
            successString = "failed";
        }
        logger.info("EacCpf " + eacCpf.getIdentifier() + "(" + xmlType.getName() + "): " + getActionName() + " - " + successString);
    }

    protected void logAction(EacCpf eacCpf, long milliseconds) {
        XmlType xmlType = XmlType.EAC_CPF;
        String successString = "succeed";
        logger.info("EacCpf " + eacCpf.getIdentifier() + "(" + xmlType.getName() + "): " + getActionName() + " - " + successString + " - " + milliseconds + "ms");
    }

    protected void logSolrAction(EacCpf eacCpf, String message, long solrMilliseconds, long milliseconds) {
        XmlType xmlType = XmlType.EAC_CPF;
        String successString = "succeed";
        logger.info("EacCpf " + eacCpf.getIdentifier() + "(" + xmlType.getName() + "): " + getActionName() + " - " + message + " - " + successString + " - s:" + solrMilliseconds + "ms" + " - o:" + milliseconds + "ms");
    }

    protected static boolean isBeingHarvested() {
        return DAOFactory.instance().getResumptionTokenDAO().containsValidResumptionTokens(new Date());
    }
}
