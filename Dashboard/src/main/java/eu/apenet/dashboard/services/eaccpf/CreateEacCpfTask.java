package eu.apenet.dashboard.services.eaccpf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.util.TextProviderHelper;

import com.ctc.wstx.exc.WstxParsingException;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.ExistingFilesChecker;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.UpFile;

/**
 * Creates a new EAC-CPF in the file system and in the data base and
 * builds the title of the EAC-CPF based on the value of @localType
 * in the element {@code <part>}.
 */
public class CreateEacCpfTask extends AbstractEacCpfTask {

    @Override
    protected String getActionName() {
        return "create EAC-CPF";
    }

   /**
    * Stores in the data base and in the file system the EAC-CPF uploaded in the Dashboard.
    * @param xmlType {@link XmlType} The type of the file to upload.
    * @param upFile {@link UpFile} The file uploaded.
    * @param aiId {@link Integer} The identifier of the archival institution.
    * @return {@link EacCpf} 
    * @throws Exception
    * @see ExistingFilesChecker#extractAttributeFromXML(String, String, String, boolean, boolean)
    * @see eu.apenet.persistence.dao.EacCpfDAO
    * @see eu.apenet.persistence.vo.EacCpf
    * @see eu.apenet.persistence.vo.ArchivalInstitution
    */
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
     * Builds the EAC-CPF's title.
     *
     * @param path String The file's path to upload
     * @return String The title of the EAC-CPF.
     */
    private String builderTitle(String path) {
        StringBuilder builderTitle = new StringBuilder();

        //titleElements collects all <nameEntry> with their paths
        LinkedList<NameEntry> titleElements = searchForAllTitleElements(path, "nameEntry/part");

        //if the list is populated, sort it by priority and process the first entry
        if (titleElements != null && !titleElements.isEmpty()) {
    		StringBuilder unknownLocalType = new StringBuilder();
    		boolean hasPart = false;

            //Collections.sort(titleElements, new NameEntryComp());

            //Since the list is sorted prior to use, its first element should almost always return a value from which a
            //title can be built without problems. If this however should not be the case, for example if the file uses
            //other values than the six possibilities for apeEAC-CPF, an error message will be displayed in the title column
        	// If the content of the "<part>" of the first element is empty,
        	// will check the content of the next ones.
        	boolean foundTitle = false;
        	for (int i = 0; !foundTitle && i < titleElements.size(); i++) {
        		NameEntry currentNameEntry = retrieveEntryByLocalType(titleElements);

	            if (!currentNameEntry.getLocalType().equals(NameEntryLocalType.UNKNOWN)) {
	                LinkedList<Part> titleEntryParts = currentNameEntry.getParts();

	                //StringBuilder objects for single parts of the name, if needed
	                StringBuilder surname = new StringBuilder();
	                StringBuilder firstname = new StringBuilder();
	                StringBuilder patronymic = new StringBuilder();
	                StringBuilder content = new StringBuilder();

	                for (Part part : titleEntryParts) {
	                	hasPart = true;
	                    if (part.getLocalType().equals("persname") || part.getLocalType().equals("famname") || part.getLocalType().equals("corpname")) {
	                        content.append(part.getContent());
	                    }
	                    if (part.getLocalType().equals("surname")) {
	                        if (surname.length() != 0) {
	                            surname.append(" ");
	                        }
	                        surname.append(part.getContent());
	                    }
                            if (part.getLocalType().equals("firstname")) {
	                        if (firstname.length() != 0) {
	                            firstname.append(" ");
	                        }
	                        firstname.append(part.getContent());
	                    }
                            if (part.getLocalType().equals("patronymic")) {
	                        if (patronymic.length() != 0) {
	                            patronymic.append(" ");
	                        }
	                        patronymic.append(part.getContent());
	                    }
	                }

	                // if the builder is empty, build the title from the parts
                        if (surname.length() > 0 && firstname.length() > 0){
                            builderTitle.append(surname).append(", ").append(firstname);
                            if(patronymic.length() > 0){
                                builderTitle.append(" ").append(patronymic);
                            }
                        } else if (content.length() > 0){
                            builderTitle.append(content);
                        }
                        
	                //output of the title
	                if (builderTitle.length() != 0) {
	                	foundTitle = true;
	                	//issue #1442 extension's, 
	                	//cases in which it's created via DPT or other external system. 
	                	//For additional requirements see comment #7.
	                	String title = builderTitle.toString().trim();
	                	if(title.endsWith(", ") || title.endsWith(",")){
	                		builderTitle.setLength(title.lastIndexOf(","));
	                	}
	                }
	            } else {
	            	unknownLocalType.append(getText("eaccpf.error.no.known.localtype"));
	            }
        	}

            if (builderTitle.length() != 0) {
            	// Case in which at least one "<part>" element has content.
                return builderTitle.toString();
            } else if (unknownLocalType.length() != 0 && !hasPart) {
            	// Case in which the "@localType" attribute of "<nameEntry>"
            	// element has unknown value.
                return unknownLocalType.toString();
            } else if (hasPart) {
            	// Case in which has "<part>" element but all have no content.
            	return " ";
            } else {
            	// Case in which the part is ineligible.
                return getText("eaccpf.error.no.ineligible.part");
            }
        } else {
            return getText("eaccpf.error.no.title");
        }
    }
    
    private NameEntry retrieveEntryByLocalType(LinkedList<NameEntry> titleElements) {
        int preferred = -1;
        int authorized = -1;
        int alternative = -1;
        int abbreviation = -1;
        int other = -1;
        int noLocalType = -1;
        for (int counter = 0; counter < titleElements.size(); counter++) {
            NameEntry nameEntry = titleElements.get(counter);
            if (nameEntry.getLocalType().equals("preferred") && preferred == -1) {
                preferred = counter;
            } else if (nameEntry.getLocalType().equals("authorized") && authorized == -1) {
                authorized = counter;
            } else if (nameEntry.getLocalType().equals("alternative") && alternative == -1) {
                alternative = counter;
            } else if (nameEntry.getLocalType().equals("abbreviation") && abbreviation == -1) {
                abbreviation = counter;
            } else if (nameEntry.getLocalType().equals("other") && other == -1) {
                other = counter;
            } else if (noLocalType == -1) {
                noLocalType = counter;
            }
        }
        if (preferred != -1) {
            return titleElements.get(preferred);
        } else if (authorized != -1) {
            return titleElements.get(authorized);
        } else if (alternative != -1) {
            return titleElements.get(alternative);
        } else if (abbreviation != -1) {
            return titleElements.get(abbreviation);
        } else if (other != -1) {
            return titleElements.get(other);
        } else if (noLocalType != -1) {
            return titleElements.get(noLocalType);
        } else {
            return titleElements.get(0);
        }
    }

   /**
    * Empty method.
    */
    @Override
    protected void execute(EacCpf eac, Properties properties) throws APEnetException {
    }
    /**
     * This method is implemented by some developer to logs Action.
     * @param xmlType The type of the file.
     * @param fileName The name of the file.
     * @param exception The exception to treat.
     */
    protected void logAction(XmlType xmlType, String fileName, Exception exception) {
        if (exception == null) {
            logger.info(fileName + "(" + xmlType.getName() + "): " + getActionName() + " - succeed");
        } else {
            logger.error(fileName + "(" + xmlType.getName() + "): " + getActionName() + " - failed", exception);
        }

    }

    /**
     * Gets the path of the EAC-CPF.
     * @param xmlType {@link XmlType} The type of the file.
     * @param archivalInstitution {@link ArchivalInstitution} The institution where is the EAC-CPF.
     * @return String The path of the EAC-CPF file.
     */
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
     * Method to recover all the values of {@code <part>} in one repeatable element {@code <nameEntry>}.
     *
     * @param element String the repeatable element {@code <part>}
     *
     * @return  {@link LinkedList}{@code <}{@link NameEntry}{@code >} All the values of the element
     * @see javax.xml.stream.XMLInputFactory
     * @see javax.xml.stream.XMLStreamReader
     * @see javax.xml.stream.events.XMLEvent
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
                                    }
                                }
                            }
                        	addText = true;
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

   
    /**
     * 
     * Class for the element <code>&lt;nameEntry&gt;</code> in the EAC-CPF.
     *
     */
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

    /**
     * 
     * Class for the element <code>&lt;part&gt;</code> in the EAC-CPF.
     *
     */
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

   /**
    * 
    *Values of the attribute @localType in the element <code>&lt;nameEntry&gt;</code>.
    *
    */
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

    /**
     * 
     * Compares two elements <code>&lt;nameEntry&gt;</code>.
     *
     */
    private class NameEntryComp implements Comparator<NameEntry> {

        @Override
        public int compare(NameEntry o1, NameEntry o2) {
            return o1.getLocalTypeEnumValue().compareTo(o2.getLocalTypeEnumValue());
        }
    }

    /**
     * Method to get the localized texts.
     *
     * @param code Key for the text to recover in localized form.
     * 
     * @return Localized text.
     */
    private String getText(String code) {
    	ValueStack valueStack = ActionContext.getContext().getValueStack();
    	return TextProviderHelper.getText(code, code, valueStack);
    }
}
