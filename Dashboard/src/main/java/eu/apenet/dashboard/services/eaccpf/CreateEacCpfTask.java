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
import java.util.LinkedList;

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
            System.gc();
            Thread.sleep(2000);

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
        LinkedList<String[]> titleElements = new LinkedList<String[]>();
        try {

        	String result = ExistingFilesChecker.extractAttributeFromXML(path, "nameEntry/part", "localType", true, true);
        	if (result.equalsIgnoreCase("corpname") || (result.equalsIgnoreCase("persname")) || result.equalsIgnoreCase("famname") || result.equalsIgnoreCase("error")) {
        		//the title is the first occurrence of the element nameEntry/part
        		title = ExistingFilesChecker.extractAttributeFromXML(path, "nameEntry/part", null, true, true);
        	} else{
        		// the title is formed by "surname, firstname patronymic"
                titleElements = searchForAllElementTitle(path, "nameEntry/part");
        	}

            //List the elements if the titleMap is not empty
            if (titleElements != null && !titleElements.isEmpty()) {
                StringBuilder surname = new StringBuilder();
                StringBuilder firstname = new StringBuilder();
                StringBuilder patronymic = new StringBuilder();
                for (String[] titleElement : titleElements) {
                    if (titleElement[0].equals("surname")) {
                        if(surname.length() != 0){
                            surname.append(" ");
                        }
                        surname.append(titleElement[1]);
                    }
                    if (titleElement[0].equals("firstname")) {
                        if(firstname.length() != 0){
                            firstname.append(" ");
                        }
                        firstname.append(titleElement[1]);
                    }
                    if (titleElement[0].equals("patronymic")) {
                        if(patronymic.length() != 0){
                            patronymic.append(" ");
                        }
                        patronymic.append(titleElement[1]);
                    }
                }
                // build the title
                builderTitle.append(surname);
                if (builderTitle.length() != 0) {
                    builderTitle.append(", ");
                }
                builderTitle.append(firstname);
                if (builderTitle.length() != 0) {
                    builderTitle.append(" ");
                }
                builderTitle.append(patronymic);
                if (builderTitle.length() == 0) {
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
     * Method to recover all the values of <part> in one repeatable element <nameEntry>.
     *
     * @param element, the repeatable element <part>
     *
     * @return all the values of the element
     */
    private LinkedList<String[]> searchForAllElementTitle(String path, String element) {
        final String CONVERTED_FLAG = "Converted_apeEAC-CPF_version_";
        XMLStreamReader input = null;
        InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        LinkedList<String[]> titleElements = new LinkedList<String[]>();

        try {
            sfile = new FileInputStream(path);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);

            boolean abort = false;
            boolean addText = false;
            String[] pathElements = element.split("/");
            String localType = "";
            String elementContent = "";

            logger.debug("Checking file, looking for element " + element + ", path begins with " + pathElements[0]);
            while (!abort && input.hasNext()) {
            switch (input.next()) {
                    case XMLEvent.START_ELEMENT:
                        if (input.getLocalName().equalsIgnoreCase(pathElements[(pathElements.length - 1)])) {
                            for (int i = 0; i < input.getAttributeCount(); i++) {
                                if (input.getAttributeLocalName(i).equals("localType") && (input.getAttributeValue(i).equals("surname")
                                        || (input.getAttributeValue(i).equals("firstname")) || (input.getAttributeValue(i).equals("patronymic")))) {
                                    localType = input.getAttributeValue(i);
                                    addText = true;
                                }
                            }
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                        if (addText) {
                            elementContent = input.getText();
                            if (elementContent.startsWith(CONVERTED_FLAG)) {
                                logger.debug("Returning true");
                                String[] trueArray = {"true", "true"};
                                titleElements.add(trueArray);
                                return titleElements;
                            } else {
                                logger.debug("Adding " + input.getText());
                                String[] titleElement = {localType, elementContent};
                                titleElements.add(titleElement);
                            }
                            addText = false;
                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                    	if (input.getLocalName().equalsIgnoreCase(pathElements[0])){  //if it's nameEntry final
                    		abort = true;
                    	}
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

        if (titleElements.isEmpty()) {
            logger.debug("Returning error");
            String[] errorArray = {"error", "error"};
            titleElements.add(errorArray);
        }
        return titleElements;
    }
}
