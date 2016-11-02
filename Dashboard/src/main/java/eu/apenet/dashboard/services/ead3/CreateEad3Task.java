/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.XMLUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.util.TextProviderHelper;

/**
 *
 * @author kaisar
 */
public class CreateEad3Task extends AbstractEad3Task {

    protected Ead3 execute(XmlType xmlType, UpFile upFile, Integer aiId, String ead3Id, String title) throws Exception {
        String fileName = upFile.getFilename();
        try {

            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
            Ead3 ead3 = new Ead3();
            if (XmlType.EAD_3.equals(xmlType)) {
                ead3.setAiId(aiId);
                ead3.setArchivalInstitution(archivalInstitution);
                ead3.setIdentifier(XMLUtils.removeUnusedCharacters(ead3Id));
                ead3.setTitle(XMLUtils.removeUnusedCharacters(title));
                ead3.setUploadDate(new Date());
                String startPath = getPath(xmlType, archivalInstitution) + upFile.getFilename();
                File destFile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath() + startPath);
                if (destFile.exists()) {
                    startPath = getPath(xmlType, archivalInstitution)  + upFile.getFilename();
                }
                ead3.setPath(startPath);
                ead3.setUploadMethod(upFile.getUploadMethod());
                ead3 = DAOFactory.instance().getEad3DAO().store(ead3);
                File srcFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
                        + upFile.getPath() + fileName);

                FileUtils.copyFile(srcFile, destFile);
                File uploadDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath());
                if (srcFile.exists()) {
                    FileUtils.forceDelete(srcFile);
                }

                if (uploadDir.exists() && uploadDir.listFiles().length == 0) {
                    FileUtils.forceDelete(uploadDir);
                }
                if (UploadMethod.OAI_PMH.equals(upFile.getUploadMethod().getMethod())) {
                    File parentDir = uploadDir.getParentFile();
                    if (parentDir.exists() && parentDir.listFiles().length == 0) {
                        FileUtils.forceDelete(parentDir);
                    }
                }
                logAction(xmlType, fileName, null);
            }
            return ead3;
        } catch (Exception e) {
            logAction(xmlType, fileName, e);
            throw new APEnetException("Could not create the file with from: " + upFile.getPath() + fileName, e);
        }
    }

    protected void logAction(XmlType xmlType, String fileName, Exception exception) {
        if (exception == null) {
            logger.info(fileName + "(" + xmlType.getName() + "): " + getActionName() + " - succeed");
        } else {
            logger.error(fileName + "(" + xmlType.getName() + "): " + getActionName() + " - failed", exception);
        }

    }

    public static String getPath(XmlType xmlType, ArchivalInstitution archivalInstitution) {
        String countryIso = archivalInstitution.getCountry().getIsoname().trim();
        String startPath = APEnetUtilities.FILESEPARATOR + countryIso + APEnetUtilities.FILESEPARATOR
                + archivalInstitution.getAiId() + APEnetUtilities.FILESEPARATOR;
        if (xmlType == XmlType.EAD_3) {
            return startPath + "EAD3" + APEnetUtilities.FILESEPARATOR;
        }
        return null;
    }

    private String getText(String code) {
        ValueStack valueStack = ActionContext.getContext().getValueStack();
        return TextProviderHelper.getText(code, code, valueStack);
    }

    @Override
    protected void execute(Ead3 ead3, Properties properties) throws Exception {
    }

    @Override
    protected String getActionName() {
        return "create EAD3";
    }
}
