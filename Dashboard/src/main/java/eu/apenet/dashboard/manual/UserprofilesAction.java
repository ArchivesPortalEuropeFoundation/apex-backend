/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.factory.DAOFactory;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class UserprofilesAction extends AbstractInstitutionAction {

    private List userprofiles = new LinkedList();
    private Set<SelectItem> associatedFiletypes = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> uploadedFileActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> existingFileActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> noEadidActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> daoTypes = new LinkedHashSet<SelectItem>();

    private String profileName;
    private String associatedFiletype;
    private String uploadedFileAction;
    private String existingFileAction;
    private String noEadidAction;
    private String daoType;

    private static final Logger LOG = Logger.getLogger(UserprofilesAction.class);

    public UserprofilesAction() {
        userprofiles = DAOFactory.instance().getUserprofileDAO().getUserprofiles(368);
        associatedFiletypes.add(new SelectItem("1", getText("content.message.fa")));
        associatedFiletypes.add(new SelectItem("2", getText("content.message.hg")));
        associatedFiletypes.add(new SelectItem("3", getText("content.message.sg")));
        associatedFiletype = "1";
        uploadedFileActions.add(new SelectItem("1", getText("userprofiles.upload.convertValidatePublish")));
        uploadedFileActions.add(new SelectItem("2", getText("userprofiles.upload.convertValidatePublishEuropeana")));
        uploadedFileActions.add(new SelectItem("3", getText("userprofiles.upload.convert")));
        uploadedFileActions.add(new SelectItem("4", getText("userprofiles.upload.validate")));
        uploadedFileActions.add(new SelectItem("0", getText("userprofiles.upload.nothing")));
        uploadedFileAction = "1";
        existingFileActions.add(new SelectItem("1", getText("userprofiles.existing.overwrite")));
        existingFileActions.add(new SelectItem("2", getText("userprofiles.existing.keep")));
        existingFileAction = "1";
        noEadidActions.add(new SelectItem("1", getText("userprofiles.noeadid.remove")));
        noEadidActions.add(new SelectItem("2", getText("userprofiles.noeadid.addLater")));
        noEadidAction = "1";
        daoTypes.add(new SelectItem("1", getText("userprofiles.dao.text")));
        daoTypes.add(new SelectItem("2", getText("userprofiles.dao.image")));
        daoTypes.add(new SelectItem("3", getText("userprofiles.dao.sound")));
        daoTypes.add(new SelectItem("4", getText("userprofiles.dao.video")));
        daoTypes.add(new SelectItem("5", getText("userprofiles.dao.3D")));
        daoTypes.add(new SelectItem("0", getText("userprofiles.dao.unspecified")));
        daoType = "0";
    }

    public List getUserprofiles() {
        return userprofiles;
    }

    public void setUserprofiles(List userprofiles) {
        this.userprofiles = userprofiles;
    }

    public Set<SelectItem> getAssociatedFiletypes() {
        return associatedFiletypes;
    }

    public void setAssociatedFiletypes(Set<SelectItem> associatedFiletypes) {
        this.associatedFiletypes = associatedFiletypes;
    }

    public Set<SelectItem> getUploadedFileActions() {
        return uploadedFileActions;
    }

    public void setUploadedFileActions(Set<SelectItem> uploadedFileActions) {
        this.uploadedFileActions = uploadedFileActions;
    }

    public Set<SelectItem> getExistingFileActions() {
        return existingFileActions;
    }

    public void setExistingFileActions(Set<SelectItem> existingFileActions) {
        this.existingFileActions = existingFileActions;
    }

    public Set<SelectItem> getNoEadidActions() {
        return noEadidActions;
    }

    public void setNoEadidActions(Set<SelectItem> noEadidActions) {
        this.noEadidActions = noEadidActions;
    }

    public Set<SelectItem> getDaoTypes() {
        return daoTypes;
    }

    public void setDaoTypes(Set<SelectItem> daoTypes) {
        this.daoTypes = daoTypes;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAssociatedFiletype() {
        return associatedFiletype;
    }

    public void setAssociatedFiletype(String associatedFiletype) {
        this.associatedFiletype = associatedFiletype;
    }

    public String getUploadedFileAction() {
        return uploadedFileAction;
    }

    public void setUploadedFileAction(String uploadedFileAction) {
        this.uploadedFileAction = uploadedFileAction;
    }

    public String getExistingFileAction() {
        return existingFileAction;
    }

    public void setExistingFileAction(String existingFileAction) {
        this.existingFileAction = existingFileAction;
    }

    public String getNoEadidAction() {
        return noEadidAction;
    }

    public void setNoEadidAction(String noEadidAction) {
        this.noEadidAction = noEadidAction;
    }

    public String getDaoType() {
        return daoType;
    }

    public void setDaoType(String daoType) {
        this.daoType = daoType;
    }
}
