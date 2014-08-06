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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;

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
        StringBuilder builderTitle = new StringBuilder();

        //titleElements collects all <nameEntry> with their paths
        LinkedList<NameEntry> titleElements = searchForAllTitleElements(path, "nameEntry/part");

        //if the list is populated, sort it by priority and process the first entry
        if (titleElements != null && !titleElements.isEmpty()) {
            //Collections.sort(titleElements, new NameEntryComp());

            //Since the list is sorted prior to use, its first element should almost always return a value from which a
            //title can be built without problems. If this however should not be the case, for example if the file uses
            //other values than the six possibilities for apeEAC-CPF, an error message will be displayed in the title column
            if (!titleElements.getFirst().getLocalType().equals(NameEntryLocalType.UNKNOWN)) {
                LinkedList<Part> titleEntryParts = titleElements.getFirst().getParts();

                //StringBuilder objects for single parts of the name, if needed
                StringBuilder surname = new StringBuilder();
                StringBuilder firstname = new StringBuilder();
                StringBuilder patronymic = new StringBuilder();
                StringBuilder content = new StringBuilder();

                //if there is only a persname/famname/corpname, directly attach it to the main StringBuilder, otherwise use the partial builders and build title from them
                for (Part part : titleEntryParts) {
                    if (part.getLocalType().equals("persname") || part.getLocalType().equals("famname") || part.getLocalType().equals("corpname")) {
                        builderTitle.append(part.getContent());
                    }
                    if (part.getLocalType().equals("surname")) {
                        if (surname.length() != 0) {
                            surname.append(" ");
                        }
                        surname.append(part.getContent());
                    }else if (part.getLocalType().equals("firstname")) {
                        if (firstname.length() != 0) {
                            firstname.append(" ");
                        }
                        firstname.append(part.getContent());
                    }else if (part.getLocalType().equals("patronymic")) {
                        if (patronymic.length() != 0) {
                            patronymic.append(" ");
                        }
                        patronymic.append(part.getContent());
                    }else{
                    	content.append(part.getContent());
                    }
                }

                // if the builder is empty, build the title from the parts
                if (builderTitle.length() == 0) {
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
                    	if(content.length()>0){
                    		builderTitle.append(content);
                    	}else{
                    		builderTitle.append(" ");
                    	}
                    }
                }
                //output of the title
                if (builderTitle.length() != 0) {
                    return builderTitle.toString();
                } else {
                    return "ERROR: Cannot build title from ineligible part";
                }
            } else {
                return "ERROR: Cannot build title from unknown nameEntry/@localtype";
            }
        } else {
            return "ERROR: No title";
        }
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
    private LinkedList<NameEntry> searchForAllTitleElements(String path, String element) {
        final String CONVERTED_FLAG = "Converted_apeEAC-CPF_version_";
        XMLStreamReader input = null;
        InputStream sfile = null;
        XMLInputFactory xmlif = (XMLInputFactory) XMLInputFactory.newInstance();
        xmlif.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
        xmlif.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);

        LinkedList<NameEntry> nameEntries = new LinkedList<NameEntry>();

        try {
            sfile = new FileInputStream(path);
            input = (XMLStreamReader) xmlif.createXMLStreamReader(sfile);

            boolean addText = false;
            String[] pathElements = element.split("/");
            Stack<NameEntry> nameEntryStack = new Stack<NameEntry>();
            Stack<Part> partStack = new Stack<Part>();

            logger.debug("Checking file, looking for element " + element + ", path begins with " + pathElements[0]);
            while (input.hasNext()) {
                switch (input.next()) {
                    case XMLEvent.START_ELEMENT:
                        if (input.getLocalName().equalsIgnoreCase(pathElements[0])) {
                            NameEntry nameEntry = new NameEntry();
                            for (int i = 0; i < input.getAttributeCount(); i++) {
                                if (input.getAttributeLocalName(i).equals("localType")) {
                                    if (input.getAttributeValue(i).equals("authorized")
                                            || (input.getAttributeValue(i).equals("alternative"))
                                            || (input.getAttributeValue(i).equals("abbreviation"))
                                            || (input.getAttributeValue(i).equals("other"))
                                            || (input.getAttributeValue(i).equals("preferred"))) {
                                        nameEntry.setLocalType(input.getAttributeValue(i));
                                    } else if (!input.getAttributeValue(i).isEmpty()) {
                                        nameEntry.setLocalType("unknownValue");
                                    }
                                }
                            }
                            nameEntryStack.push(nameEntry);
                        }
                        if (input.getLocalName().equalsIgnoreCase(pathElements[(pathElements.length - 1)])) {
                            Part part = new Part();
                            if(input.getAttributeCount()>0){
                            	for (int i = 0; i < input.getAttributeCount(); i++) {
                                    if (input.getAttributeLocalName(i).equals("localType")) {
                                        if (input.getAttributeValue(i).equals("persname")
                                                || (input.getAttributeValue(i).equals("corpname"))
                                                || (input.getAttributeValue(i).equals("famname"))
                                                || (input.getAttributeValue(i).equals("surname"))
                                                || (input.getAttributeValue(i).equals("firstname"))
                                                || (input.getAttributeValue(i).equals("patronymic"))) {
                                            part.setLocalType(input.getAttributeValue(i));
                                        } else if (!input.getAttributeValue(i).isEmpty()) {
                                            part.setLocalType("unknownValue");
                                        }
                                        addText = true;
                                    }
                                }
                            }else{ //in case there is no attributes, take the content
                            	addText = true;
                            }
                            partStack.push(part);
                        }
                        break;
                    case XMLEvent.CHARACTERS:
                        if (addText) {
                            if (input.getText().startsWith(CONVERTED_FLAG)) {
                            } else {
                                logger.debug("Adding " + input.getText());
                                Part part = partStack.peek();
                                part.setContent(input.getText());
                            }
                            addText = false;
                        }
                        break;
                    case XMLEvent.END_ELEMENT:
                        if (input.getLocalName().equalsIgnoreCase(pathElements[0])) {  //if it's nameEntry final
                            NameEntry nameEntry = nameEntryStack.peek();
                            nameEntry.getParts().addAll(partStack);
                            partStack.clear();
                            nameEntries.add(nameEntry);
                            nameEntryStack.pop();
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

        if (nameEntries.isEmpty()) {
            logger.debug("no name entries available; adding element with content \"error\"");
            NameEntry nameEntry = new NameEntry();
            LinkedList<Part> error = new LinkedList<Part>();
            Part part = new Part();
            part.setLocalType("error");
            part.setContent("error");
            error.add(part);
            nameEntry.setParts(error);
            nameEntries.add(nameEntry);
        }
        return nameEntries;
    }

    private class NameEntry {

        private NameEntryLocalType localType;
        private LinkedList<Part> parts;

        public NameEntry() {
            this.localType = NameEntryLocalType.EMPTY;
            this.parts = new LinkedList<Part>();
        }

        public String getLocalType() {
            return localType.toString();
        }

        public void setLocalType(String localType) {
            this.localType = NameEntryLocalType.getNameEntryLocalType(localType);
        }

        public LinkedList<Part> getParts() {
            return parts;
        }

        public void setParts(LinkedList<Part> parts) {
            this.parts = parts;
        }

        public NameEntryLocalType getLocalTypeEnumValue() {
            return localType;
        }
    }

    private class Part {

        private String localType;
        private String content;

        public Part() {
            this.localType = "";
            this.content = "";
        }

        public String getLocalType() {
            return localType;
        }

        public void setLocalType(String localType) {
            this.localType = localType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    private enum NameEntryLocalType {

        PREFERRED("preferred"),
        AUTHORIZED("authorized"),
        EMPTY(""),
        ALTERNATIVE("alternative"),
        ABBREVIATION("abbreviation"),
        OTHER("other"),
        UNKNOWN("unknown");

        private String value;

        private NameEntryLocalType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static NameEntryLocalType getNameEntryLocalType(String value) {
            for (NameEntryLocalType nameEntryLocalType : NameEntryLocalType.values()) {
                if (nameEntryLocalType.toString().equals(value)) {
                    return nameEntryLocalType;
                }
            }
            return UNKNOWN;
        }
    }

    private class NameEntryComp implements Comparator<NameEntry> {

        @Override
        public int compare(NameEntry o1, NameEntry o2) {
            return o1.getLocalTypeEnumValue().compareTo(o2.getLocalTypeEnumValue());
        }
    }
}
