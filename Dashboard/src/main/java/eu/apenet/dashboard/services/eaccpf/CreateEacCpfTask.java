package eu.apenet.dashboard.services.eaccpf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import org.apache.commons.io.FileUtils;
import com.ctc.wstx.exc.WstxParsingException;
import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.UpFile;

public class CreateEacCpfTask extends AbstractEacCpfTask {

    @Override
    protected String getActionName() {
        return "create EAC-CPF";
    }

    protected EacCpf execute(XmlType xmlType, UpFile upFile, Integer aiId) throws Exception {
        String fileName = upFile.getFilename();
        try {
            String uploadedFilesPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath();
            String cpfId = "";
            try {
                cpfId = ExistingFilesChecker.extractAttributeFromXML(uploadedFilesPath + upFile.getPath() + fileName,
                        "eac-cpf/control/recordId", null, true, true).trim();
            } catch (WstxParsingException e) {
            }
            boolean isConverted;
            try {
                isConverted = Boolean.valueOf(ExistingFilesChecker.extractAttributeFromXML(uploadedFilesPath + upFile.getPath() + fileName,
                        "eac-cpf/control/maintenanceHistory/maintenanceEvent/eventDescription", null, false, true));
            } catch (Exception e) {
                isConverted = false;
            }

            //Store the new EAC-CPF in the file system
            String title = builderTitle(uploadedFilesPath + upFile.getPath() + fileName);
            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
            EacCpf newEac = new EacCpf();
            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();

            newEac.setIdentifier(cpfId);
            newEac.setArchivalInstitution(archivalInstitution);
            newEac.setUploadDate(new Date());
            newEac.setTitle(title);
            newEac.setConverted(isConverted);

            String startPath = getPath(xmlType, archivalInstitution) + fileName;
            File destFile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath() + startPath);
            if (destFile.exists()) {
                startPath = getPath(xmlType, archivalInstitution) + System.currentTimeMillis() + "_" + fileName;
                destFile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath() + startPath);
            }
            newEac.setPath(startPath);
            newEac.setUploadMethod(upFile.getUploadMethod());
            newEac = eacCpfDAO.store(newEac);

            File srcFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
                    + upFile.getPath() + fileName);

            FileUtils.copyFile(srcFile, destFile);
            File uploadDir = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + upFile.getPath());

            //Windows file lock workaround; uncomment if necessary
            //System.gc();
            //Thread.sleep(2000);

            if (srcFile.exists()) {
                FileUtils.forceDelete(srcFile);
            }

            if (uploadDir.listFiles().length == 0) {
                FileUtils.forceDelete(uploadDir);
            }
            logAction(xmlType, fileName, null);
            return newEac;

        } catch (Exception e) {
            logAction(xmlType, fileName, e);
            throw new APEnetException("Could not create the file with from: " + upFile.getPath() + fileName, e);
        }
    }

    /**
     * Build the EAC-CPF's title
     *
     * @param path, the file's path to upload
     * @return the title
     */
    private String builderTitle(String path) {
        String title = "";
        StringBuilder builderTitle = new StringBuilder();
        Map<String, String> titleMap = new HashMap<String, String>();
        try {

            if (ExistingFilesChecker.isElementContent(path, "nameEntryParallel")) {
                String result = ExistingFilesChecker.extractAttributeFromXML(path, "eac-cpf/cpfDescription/identity/nameEntryParallel/nameEntry/part", "localType", false, true);
                if (result.equalsIgnoreCase("corpname") || (result.equalsIgnoreCase("persname")) || result.equalsIgnoreCase("famname") || result.equalsIgnoreCase("error")) {
                    // the title is the first occurrence of the element nameEntryParallel/part
                    title = ExistingFilesChecker.extractAttributeFromXML(path, "eac-cpf/identity/nameEntryParallel/nameEntry/part", null, true, true);
                } else {
                    // the title is formed by "surname, firstname patronymic"
                    titleMap = searchForAllElementTitle(path, "eac-cpf/cpfDescription/identity/nameEntryParallel/nameEntry/part");
                }
            } else { //only has "nameEntry"
                String result = ExistingFilesChecker.extractAttributeFromXML(path, "eac-cpf/cpfDescription/identity/nameEntry/part", "localType", false, true);
                if (result.equalsIgnoreCase("corpname") || (result.equalsIgnoreCase("persname")) || result.equalsIgnoreCase("famname") || result.equalsIgnoreCase("error")) {
                    // the title is the first occurrence of the element nameEntry/part
                    title = ExistingFilesChecker.extractAttributeFromXML(path, "eac-cpf/cpfDescription/identity/nameEntry/part", null, true, true);
                } else {
                    // the title is formed by "surname, firstname patronymic"
                    titleMap = searchForAllElementTitle(path, "eac-cpf/cpfDescription/identity/nameEntry/part");
                }
            }
            //List the elements if the titleMap is not empty
            if (titleMap != null && !titleMap.isEmpty()) {
                Iterator<?> it = titleMap.entrySet().iterator();
                String surname = "";
                String firstname = "";
                String patronymic = "";
                while (it.hasNext()) {
                    Map.Entry<String, String> e = (Map.Entry) it.next();
                    if (e.getKey().equals("surname")) {
                        surname = titleMap.get(e.getKey());
                    }
                    if (e.getKey().equals("firstname")) {
                        firstname = titleMap.get(e.getKey());
                    }
                    if (e.getKey().equals("patronymic")) {
                        patronymic = titleMap.get(e.getKey());
                    }
                }
                // build the title
                if (!surname.isEmpty()) {
                    builderTitle.append(surname);
                    if (!firstname.isEmpty()) {
                        builderTitle.append(", ");
                        builderTitle.append(firstname);
                        if (!patronymic.isEmpty()) {
                            builderTitle.append(" ");
                            builderTitle.append(patronymic);
                        }
                    } else if (!patronymic.isEmpty()) {
                        builderTitle.append(" ");
                        builderTitle.append(patronymic);
                    }
                } else if (!firstname.isEmpty()) {
                    builderTitle.append(firstname);
                    if (!patronymic.isEmpty()) {
                        builderTitle.append(" ");
                        builderTitle.append(patronymic);
                    }
                } else if (!patronymic.isEmpty()) {
                    builderTitle.append(patronymic);
                } else {
                    builderTitle.append(" ");
                }
                title = builderTitle.toString();
            }

        } catch (WstxParsingException e) {
            title = "";
        }
        return title;
    }

    @Override
    protected void execute(EacCpf eac, Properties properties) throws APEnetException {
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
        if (xmlType == XmlType.EAC_CPF) {
            return startPath + "EAC-CPF" + APEnetUtilities.FILESEPARATOR;
        }
        return null;
    }

    /**
     * Method to recover all the values of the one repeatable element.
     *
     * @param element, the repeatable element
     *
     * @return all the values of the element
     */
    public Map<String, String> searchForAllElementTitle(String path, String element) {
        final String CONVERTED_FLAG = "Converted_apeEAC-CPF_version_";
        XMLStreamReader input = null;
        InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        Map<String, String> titleMap = new HashMap<String, String>();

        try {
            sfile = new FileInputStream(path);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);

            boolean abort = false;
            boolean addText = false;
            String importantData = "";
            String localTypeValue = "";
            String[] pathElements = element.split("/");

            logger.debug("Checking file, looking for element " + element + ", path begins with " + pathElements[0]);
            while (!abort && input.hasNext()) {
                switch (input.next()) {
                    case XMLEvent.START_ELEMENT:
                        if (input.getLocalName().equalsIgnoreCase(pathElements[(pathElements.length - 1)])) {
                            addText = true;
                            for (int i = 0; i < input.getAttributeCount(); i++) {
                                if (input.getAttributeLocalName(i).equals("localType") && (input.getAttributeValue(i).equals("surname")
                                        || (input.getAttributeValue(i).equals("firstname")) || (input.getAttributeValue(i).equals("patronymic")))) {
                                    localTypeValue = input.getAttributeValue(i);
                                }
                            }
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                        if (addText) {
                            importantData = input.getText();
                            if (importantData.startsWith(CONVERTED_FLAG)) {
                                logger.debug("Returning true");
                                titleMap.put("true", "true");
                                return titleMap;
                            } else {
                                logger.debug("Adding " + input.getText());
                                titleMap.put(localTypeValue, importantData);
                            }
                            addText = false;
                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                        break;
                }
            }
        } catch (Exception e) {
            logger.error("Error parsing StAX for file " + path, e);
        } finally {
            try {
                input.close();
                sfile.close();
            } catch (Exception e) {
                logger.error("Error closing streams" + e.getMessage(), e);
            }
        }

        if (titleMap.isEmpty()) {
            logger.debug("Returning error");
            titleMap.put("error", "error");
        }
        return titleMap;
    }
}
