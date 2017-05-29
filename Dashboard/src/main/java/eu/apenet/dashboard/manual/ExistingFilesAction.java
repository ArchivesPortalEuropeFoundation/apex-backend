package eu.apenet.dashboard.manual;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FileType;

/**
 * User: Eloy García Date: Sep 22th, 2010
 *
 */
public class ExistingFilesAction extends AbstractInstitutionAction {

    private static final long serialVersionUID = 412564723638638837L;

    private final static Logger LOG = Logger.getLogger(ExistingFilesAction.class);
    private Set<SelectItem> typeSet = new TreeSet<SelectItem>();
    //Attributes
    private List<FileUnit> existingNewXmlFilesUploaded;	//This attribute contains all the XML file names that have been uploaded to the Dashboard through HTTP, FTP or OAI-PMH protocols.
    private List<FileUnit> existingNewXslFilesUploaded;	//This attribute contains all the XSL file names that have been uploaded to the Dashboard through HTTP, FTP or OAI-PMH protocols.
    private List<FileUnit> existingFiles;		//This attribute contains all the file names that already exist in the Dashboard
    private List<FileUnit> filesSuccessful;		//This attribute contains all the files with a format not proper for APEnet
    private List<FileUnit> filesWithErrors;		//This attribute contains all the files with a format not proper for APEnet
    private List<FileUnit> filesWithEmptyEadid;		//This attribute contains all the files with empty EADID.
    private List<FileUnit> filesWithEadidTooLong;		//This attribute contains all the files with EADID too long.
    private List<FileUnit> filesNotUploaded;	//This attribute contains all the files not uploaded to APEnet
    private List<FileUnit> filesBlocked;		//This attribute contains all the files blocked because of Europeana is harvesting and those files are FAs which have ESE files published
    private Map<String, String> existingFilesChoice = new LinkedHashMap<String, String>(); //This list contains all the possible actions a user can do when a file is already stored in his Dashboard
    private Map<String, String> existingFilesChoiceAddEADID = new LinkedHashMap<String, String>(); //This list contains all the possible actions a user can do when a file contains empty EADID
    private Map<String, String> existingFilesChoiceOverwriteCancelEADID; //This list contains all the possible actions a user can do when the EADID is repeated and he doesn't want to add a new one.
    private Map<String, String> existingEADIDAnswersChoice; //This list contains all the possible answer that the action of saving the change of EADID has returned.
    private String[] filesTypeAnswers; //This array contains all the answers typed by the user regarding to the decision about if a file is a Finding Aid or a Holdings Guide
    private String[] existingChangeEADIDAnswersRepeated; //This array contains all the answers typed by the user regarding to the decision about if a file is a Finding Aid or a Holdings Guide
    private String[] existingChangeEADIDAnswersEmpty; //This array contains all the answers typed by the user regarding to the decision about if a file is a Finding Aid or a Holdings Guide
    private String[] existingCancelOverwriteEADIDAnswers;
    private String[] existingFilesAnswers; //This array contains all the answers typed by the user regarding to the actions to carry out with the existing files
    private String additionalError;
    private final static String OVERWRITE = "Overwrite";
    private final static String CANCEL = "Cancel";
    private final static String CHANGE = "Change ID";
    private final static String ADD = "Add EADID";
    private final static String OK = "OK";//
    private final static String KO = "KO";//

    private String eadid;
    private String neweadid;
    private String[] arrayneweadid;
    private boolean filesLeft;
    private String newXmlFilesTitle;

    public void setFilesSuccessful(List<FileUnit> filesSuccessful) {
        this.filesSuccessful = filesSuccessful;
    }

    public List<FileUnit> getFilesSuccessful() {
        return filesSuccessful;
    }

    public void setFilesWithErrors(List<FileUnit> filesWithErrors) {
        this.filesWithErrors = filesWithErrors;
    }

    public List<FileUnit> getFilesWithErrors() {
        return filesWithErrors;
    }

    public List<FileUnit> getFilesWithEmptyEadid() {
        return filesWithEmptyEadid;
    }

    public void setFilesWithEmptyEadid(List<FileUnit> filesWithEmptyEadid) {
        this.filesWithEmptyEadid = filesWithEmptyEadid;
    }

    public List<FileUnit> getFilesWithEadidTooLong() {
        return filesWithEadidTooLong;
    }

    public void setFilesWithEadidTooLong(List<FileUnit> filesWithEadidTooLong) {
        this.filesWithEadidTooLong = filesWithEadidTooLong;
    }

    public List<FileUnit> getExistingFiles() {
        return existingFiles;
    }

    public void setExistingFiles(List<FileUnit> existingFiles) {
        this.existingFiles = existingFiles;
    }

    public List<FileUnit> getExistingNewXslFilesUploaded() {
        return existingNewXslFilesUploaded;
    }

    public void setExistingNewXslFilesUploaded(
            List<FileUnit> existingNewXslFilesUploaded) {
        this.existingNewXslFilesUploaded = existingNewXslFilesUploaded;
    }

    public void setFilesTypeAnswers(String[] filesTypeAnswers) {
        this.filesTypeAnswers = filesTypeAnswers;
    }

    public String[] getFilesTypeAnswers() {
        return filesTypeAnswers;
    }

    public Map<String, String> getExistingFilesChoice() {
        return existingFilesChoice;
    }

    public void setExistingFilesChoice(Map<String, String> existingFilesChoice) {
        this.existingFilesChoice = existingFilesChoice;
    }

    public Map<String, String> getExistingFilesChoiceAddEADID() {
        return existingFilesChoiceAddEADID;
    }

    public void setExistingFilesChoiceAddEADID(
            Map<String, String> existingFilesChoiceAddEADID) {
        this.existingFilesChoiceAddEADID = existingFilesChoiceAddEADID;
    }

    public Map<String, String> getExistingFilesChoiceOverwriteCancelEADID() {
        return existingFilesChoiceOverwriteCancelEADID;
    }

    public void setExistingFilesChoiceOverwriteCancelEADID(
            Map<String, String> existingFilesChoiceOverwriteCancelEADID) {
        this.existingFilesChoiceOverwriteCancelEADID = existingFilesChoiceOverwriteCancelEADID;
    }

    public Map<String, String> getExistingEADIDAnswersChoice() {
        return existingEADIDAnswersChoice;
    }

    public void setExistingEADIDAnswersChoice(
            Map<String, String> existingEADIDAnswersChoice) {
        this.existingEADIDAnswersChoice = existingEADIDAnswersChoice;
    }

    public void setExistingFilesAnswers(String[] existingFilesAnswers) {
        this.existingFilesAnswers = existingFilesAnswers;
    }

    public String[] getExistingFilesAnswers() {
        return existingFilesAnswers;
    }

    public String[] getExistingCancelOverwriteEADIDAnswers() {
        return existingCancelOverwriteEADIDAnswers;
    }

    public void setExistingCancelOverwriteEADIDAnswers(
            String[] existingCancelOverwriteEADIDAnswers) {
        this.existingCancelOverwriteEADIDAnswers = existingCancelOverwriteEADIDAnswers;
    }

    public String[] getExistingChangeEADIDAnswersRepeated() {
        return this.existingChangeEADIDAnswersRepeated;
    }

    public void setExistingChangeEADIDAnswersRepeated(String[] existingChangeEADIDAnswersRepeated) {
        this.existingChangeEADIDAnswersRepeated = existingChangeEADIDAnswersRepeated;
    }

    public String[] getExistingChangeEADIDAnswersEmpty() {
        return this.existingChangeEADIDAnswersEmpty;
    }

    public void setExistingChangeEADIDAnswersEmpty(String[] existingChangeEADIDAnswersEmpty) {
        this.existingChangeEADIDAnswersEmpty = existingChangeEADIDAnswersEmpty;
    }

    public List<FileUnit> getExistingNewXmlFilesUploaded() {
        return existingNewXmlFilesUploaded;
    }

    public void setExistingNewXmlFilesUploaded(List<FileUnit> existingNewXmlFilesUploaded) {
        this.existingNewXmlFilesUploaded = existingNewXmlFilesUploaded;
    }

    public void setFilesNotUploaded(List<FileUnit> filesNotUploaded) {
        this.filesNotUploaded = filesNotUploaded;
    }

    public List<FileUnit> getFilesNotUploaded() {
        return filesNotUploaded;
    }

    public List<FileUnit> getFilesBlocked() {
        return filesBlocked;
    }

    public void setFilesBlocked(List<FileUnit> filesBlocked) {
        this.filesBlocked = filesBlocked;
    }

    public String[] getArrayneweadid() {
        return arrayneweadid;
    }

    public void setArrayneweadid(String[] arrayneweadid) {
        this.arrayneweadid = arrayneweadid;
    }

    private Integer fileId;

    private String responseSaveChanges;

    public String getResponseSaveChanges() {
        return responseSaveChanges;
    }

    public void setResponseSaveChanges(String responseSaveChanges) {
        this.responseSaveChanges = responseSaveChanges;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getNeweadid() {
        return neweadid;
    }

    public void setNeweadid(String neweadid) {
        this.neweadid = neweadid;
    }

    public String getEadid() {
        return eadid;
    }

    public void setEadid(String eadid) {
        this.eadid = eadid;
    }

    public String getAdditionalError() {
        return additionalError;
    }

    //Constructor
    public ExistingFilesAction() {
        this.existingFilesChoiceOverwriteCancelEADID = new LinkedHashMap<String, String>();
        this.existingFilesChoiceOverwriteCancelEADID.put(OVERWRITE, this.getText("existingFiles.overwrite"));
        this.existingFilesChoiceOverwriteCancelEADID.put(CANCEL, this.getText("existingFiles.cancel"));
        this.existingEADIDAnswersChoice = new LinkedHashMap<String, String>();
        this.existingEADIDAnswersChoice.put(KO, this.getText("existingFiles.ko"));
        this.existingEADIDAnswersChoice.put(OK, this.getText("existingFiles.ok"));
        this.existingNewXmlFilesUploaded = new ArrayList<FileUnit>();
        this.existingNewXslFilesUploaded = new ArrayList<FileUnit>();
        this.filesSuccessful = new ArrayList<FileUnit>();
        this.filesWithErrors = new ArrayList<FileUnit>();
        this.filesWithEmptyEadid = new ArrayList<FileUnit>();
        this.filesWithEadidTooLong = new ArrayList<FileUnit>();
        this.existingFiles = new ArrayList<FileUnit>();
        this.filesNotUploaded = new ArrayList<FileUnit>();
        this.filesBlocked = new ArrayList<FileUnit>();
    }

    @Override
    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("breadcrumb.section.checkExistingFiles"));
    }

    //Methods
    @Override
    public String execute() throws Exception {

        typeSet.add(new SelectItem("0", getText("content.message.fa")));
        typeSet.add(new SelectItem("1", getText("content.message.hg")));
        typeSet.add(new SelectItem("3", getText("content.message.sg")));
        typeSet.add(new SelectItem("2", getText("content.message.cpf")));
//        Hide Ead3
//        typeSet.add(new SelectItem("4", getText("content.message.ead3")));

        ExistingFilesChecker checker = new ExistingFilesChecker(this.getAiId());
        checker.retrieveUploadedFiles(this.existingNewXmlFilesUploaded, this.existingNewXslFilesUploaded);
        long totalNumberOfFiles = DAOFactory.instance().getUpFileDAO().countNewUpFiles(this.getAiId(), FileType.XML);
        int numberOfFiles = this.existingNewXmlFilesUploaded.size();
        newXmlFilesTitle = getText("content.message.files.processing", new String[]{numberOfFiles + "", totalNumberOfFiles + ""});
        if (numberOfFiles > numberOfFiles) {
            filesLeft = true;
        }
        if (existingNewXslFilesUploaded.size() == 0 && existingNewXmlFilesUploaded.size() == 0) {
            return "DIRECT";
        }
        return SUCCESS;
    }

    public String overwriteExistingFiles() {
        ExistingFilesChecker checker = new ExistingFilesChecker(this.getAiId());
        int k = 0;
        for (int i = 0; i < this.existingFiles.size(); i++) {
            k = this.existingFiles.size();
            //existingChangeEADIDAnswers contains the result of changing the EADID. OK or KO
            String typeAnswer = null;
            typeAnswer = existingFiles.get(i).getEadType();
            if (!this.existingFilesAnswers[i].equals("Change ID")) { //todo: What is this check for??
                //String initial[] = {"KO"};
                //this.setExistingChangeEADIDAnswers(initial);
                //System.out.print(this.existingChangeEADIDAnswers[i].toString());
            }
            if (checker.overwriteFile(this.existingFiles.get(i), this.existingFilesAnswers[i], this.existingChangeEADIDAnswersRepeated[i], this.existingCancelOverwriteEADIDAnswers[i], typeAnswer, arrayneweadid[i]).equals("error")) {
                //The file has not been correctly uploaded
                this.filesNotUploaded.add(this.existingFiles.get(i));
            }
        }
        this.existingFiles = null;

        // Files blocked because Europeana is Harvesting must be deleted
        for (int i = 0; i < this.filesBlocked.size(); i++) {
            checker.overwriteFile(this.filesBlocked.get(i), "Cancel", null, null, null, null);
        }
        this.filesBlocked = null;

        for (int j = 0; j < this.filesWithEmptyEadid.size(); j++) {
            String typeAnswer = null;
            //Check if existing files to send to overwriteFile the correct data of each file.
            if (k != 0) {
                if (j != 0) {
                    k++;
                }
            } else {
                k = j;
            }

            typeAnswer = filesWithEmptyEadid.get(j).getEadType();

            //existingChangeEADIDAnswers contains the result of changing the EADID. OK or KO
            if (checker.overwriteFile(this.filesWithEmptyEadid.get(j), this.existingFilesAnswers[k], this.existingChangeEADIDAnswersEmpty[j], this.existingCancelOverwriteEADIDAnswers[k], typeAnswer, arrayneweadid[k]).equals("error")) {
                //The file has not been correctly uploaded
                this.filesNotUploaded.add(this.filesWithEmptyEadid.get(j));
            }
        }
        this.filesWithEmptyEadid = null;

        if (filesWithEadidTooLong.size() > 0) {
            for (FileUnit fileUnit : filesWithEadidTooLong) {
                checker.deleteUpFile(fileUnit);
            }
            filesWithEadidTooLong.clear();
        }

        if (existingNewXslFilesUploaded.size() == 0 && existingNewXmlFilesUploaded.size() == 0 && filesWithErrors.size() == 0 && filesNotUploaded.size() == 0 && filesWithEadidTooLong.size() == 0) {
            if (DAOFactory.instance().getUpFileDAO().hasNewUpFiles(getAiId(), FileType.XML)) {
                return "filesLeft";
            } else {
                return "contentmanager";
            }
        }

        return SUCCESS;
    }

    public String canceloverwriteExistingFiles() {
        Boolean existingfiles = false;

        ExistingFilesChecker checker = new ExistingFilesChecker(this.getAiId());
        int k = 0;
        for (int i = 0; i < this.existingFiles.size(); i++) {
            existingfiles = true;
            k = this.existingFiles.size();
            //existingChangeEADIDAnswers contains the result of changing the EADID. OK or KO
            String typeAnswer = null;
            typeAnswer = existingFiles.get(i).getEadType();
            if (checker.overwriteFile(this.existingFiles.get(i), "Cancel", this.existingChangeEADIDAnswersRepeated[i], this.existingCancelOverwriteEADIDAnswers[i], typeAnswer, arrayneweadid[i]).equals("error")) {
                //The file has not been correctly uploaded
                this.filesNotUploaded.add(this.existingFiles.get(i));
            }
        }

        this.existingFiles = null;

        for (int j = 0; j < this.filesWithEmptyEadid.size(); j++) {
            String typeAnswer = null;
            //Check if existing files to send to overwriteFile the correct data of each file.
            if (existingfiles) {
                if (j == 0) {
                    k = k + j;
                } else {
                    k++;
                }
            } else {
                k = j;
            }
            typeAnswer = filesWithEmptyEadid.get(j).getEadType();

            //existingChangeEADIDAnswers contains the result of changing the EADID. OK or KO
            if (checker.overwriteFile(this.filesWithEmptyEadid.get(j), this.existingFilesAnswers[k], this.existingChangeEADIDAnswersEmpty[j], this.existingCancelOverwriteEADIDAnswers[k], typeAnswer, arrayneweadid[k]).equals("error")) {
                //The file has not been correctly uploaded
                this.filesNotUploaded.add(this.filesWithEmptyEadid.get(j));
            }
        }
        this.filesWithEmptyEadid = null;

        checker = null;

        return SUCCESS;
    }

    public String checkExistingFiles() {

        this.existingFilesChoice.put(OVERWRITE, this.getText("existingFiles.overwrite"));
        this.existingFilesChoice.put(CHANGE, this.getText("existingFiles.change"));
        this.existingFilesChoice.put(CANCEL, this.getText("existingFiles.cancel"));
        this.existingFilesChoiceAddEADID.put(CANCEL, this.getText("existingFiles.cancel"));
        this.existingFilesChoiceAddEADID.put(ADD, this.getText("existingFiles.add"));

        FileUnit fileUnit = new FileUnit();
        ExistingFilesChecker checker = new ExistingFilesChecker(this.getAiId());

        //XSL files
        for (int i = 0; i < this.existingNewXslFilesUploaded.size(); i++) {

            fileUnit = this.getExistingNewXslFilesUploaded().get(i);

            if (checker.checkFile(fileUnit, null).equals("exists")) {
                //The file checked already exists in the Dashboard
                fileUnit.setEadType("XSL file");
                this.existingFiles.add(fileUnit);
            }

        }

        //XML files
        for (int i = 0; i < this.existingNewXmlFilesUploaded.size(); i++) {

            fileUnit = this.getExistingNewXmlFilesUploaded().get(i);

            XmlType xmlType;
            try {
                xmlType = XmlType.getType(Integer.parseInt(filesTypeAnswers[i]));
            } catch (Exception e) {
                LOG.error("Could not decide which type it is, switching to default: FA");
                xmlType = XmlType.EAD_FA;
            }

            String result = checker.checkFile(fileUnit, xmlType);
            additionalError = checker.getAdditionalErrors();
            if (!"".equals(additionalError)) {
                LOG.error("Error: " + additionalError);
                fileUnit.setErrorInformation(additionalError);
            }

            if (result.equals(ExistingFilesChecker.STATUS_NO_EXIST)) {
                //Nothing happens
                this.filesSuccessful.add(fileUnit);
            } else if (result.equals(ExistingFilesChecker.STATUS_EMPTY)) {
                this.filesWithEmptyEadid.add(fileUnit);
                //this.filesSuccessful.add(fileUnit);
                //this.filesWithErrors.add(fileUnit);
            } else if (result.equals(ExistingFilesChecker.STATUS_EADID_TOO_LONG)) {
                this.filesWithEadidTooLong.add(fileUnit);
                this.filesNotUploaded.add(fileUnit);
            } else if (result.equals(ExistingFilesChecker.STATUS_ERROR)) {
                this.filesWithErrors.add(fileUnit);
                this.filesNotUploaded.add(fileUnit);
            } else if (result.equals(ExistingFilesChecker.STATUS_BLOCKED)) {
                this.filesBlocked.add(fileUnit);
                this.filesNotUploaded.add(fileUnit);
            } else {
                //The file checked already exists in the Dashboard
                fileUnit.setEadType(xmlType.getName());
                this.existingFiles.add(fileUnit);
            }
        }

        fileUnit = null;
        this.existingNewXslFilesUploaded = null;
        this.existingNewXmlFilesUploaded = null;
        checker = null;
        if (filesSuccessful.size() > 0 && filesWithEmptyEadid.size() == 0
                && filesWithErrors.size() == 0 && filesNotUploaded.size() == 0 && filesBlocked.size() == 0 && existingFiles.size() == 0 && filesWithEadidTooLong.size() == 0) {
            if (DAOFactory.instance().getUpFileDAO().hasNewUpFiles(getAiId(), FileType.XML)) {
                return "filesLeft";
            } else {
                return "contentmanager";
            }
        } else {
            return SUCCESS;
        }
    }

    public String cancelCheckExistingFiles() {

        FileUnit fileUnit = new FileUnit();
        ExistingFilesChecker checker = new ExistingFilesChecker(this.getAiId());

        //XSL files
        for (int i = 0; i < this.existingNewXslFilesUploaded.size(); i++) {

            fileUnit = this.getExistingNewXslFilesUploaded().get(i);

            if (checker.checkFile(fileUnit, null).equals("exists")) {
                //The file checked already exists in the Dashboard
                fileUnit.setEadType("XSL file");
                if (checker.overwriteFile(this.existingNewXslFilesUploaded.get(i), "Cancel", "", "", "", "").equals("error")) {
                    //The file has not been correctly uploaded
                    this.filesNotUploaded.add(this.existingNewXslFilesUploaded.get(i));
                }
                //this.existingFiles.add(fileUnit);
            }

        }

        //XML files
        for (int i = 0; i < this.existingNewXmlFilesUploaded.size(); i++) {

            fileUnit = this.getExistingNewXmlFilesUploaded().get(i);

            if (checker.overwriteFile(this.existingNewXmlFilesUploaded.get(i), "Cancel", "", "", "", "").equals("error")) {
                //The file has not been correctly uploaded
                this.filesNotUploaded.add(this.existingNewXmlFilesUploaded.get(i));
            }
        }

        fileUnit = null;
        this.existingNewXslFilesUploaded = null;
        this.existingNewXmlFilesUploaded = null;
        checker = null;

        return SUCCESS;
    }

    public Set<SelectItem> getTypeSet() {
        return typeSet;
    }

    /**
     * @param typeSet the typeSet to set
     */
    public void setTypeSet(Set<SelectItem> typeSet) {
        this.typeSet = typeSet;
    }

    public boolean isFilesLeft() {
        return filesLeft;
    }

    public String getNewXmlFilesTitle() {
        return newXmlFilesTitle;
    }
}
