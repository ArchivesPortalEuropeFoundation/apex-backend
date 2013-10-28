/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual;

import eu.apenet.commons.view.jsp.SelectItem;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.UserprofileDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Userprofile;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author papp
 */
public class UserprofilesAction extends AbstractInstitutionAction {

    private Set<SelectItem> userprofiles = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> associatedFiletypes = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> uploadedFileActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> existingFileActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> noEadidActions = new LinkedHashSet<SelectItem>();
    private Set<SelectItem> daoTypes = new LinkedHashSet<SelectItem>();

    private String profilelist;
    private String profileName;
    private String associatedFiletype;
    private String uploadedFileAction;
    private String existingFileAction;
    private String noEadidAction;
    private String daoType;

    private static final Logger LOG = Logger.getLogger(UserprofilesAction.class);

    @Override
    public String input() {
        setUp();

        UserprofileDAO profileDAO = DAOFactory.instance().getUserprofileDAO();
        List<Userprofile> queryResult = profileDAO.getUserprofiles(368);
        for (Userprofile entry : queryResult) {
            userprofiles.add(new SelectItem(Long.toString(entry.getId()), entry.getNameProfile()));
        }
        if(profilelist == null) {
            profilelist = "1";
        }

        if (StringUtils.isNotBlank(profilelist)) {
            Long profilelistLong = Long.parseLong(profilelist);
            Userprofile userprofile = profileDAO.findById(profilelistLong);
            profileName = userprofile.getNameProfile();
            associatedFiletype = userprofile.getFileType().toString();
            uploadedFileAction = Integer.toString(userprofile.getUploadAction().getId());
            existingFileAction = Integer.toString(userprofile.getExistAction().getId());
            noEadidAction = Integer.toString(userprofile.getNoeadidAction().getId());
            daoType = Integer.toString(userprofile.getDaoType().getId());
        }
        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    private void setUp() {
        associatedFiletypes.add(new SelectItem("1", getText("content.message.fa")));
        associatedFiletypes.add(new SelectItem("2", getText("content.message.hg")));
        associatedFiletypes.add(new SelectItem("3", getText("content.message.sg")));
        uploadedFileActions.add(new SelectItem("1", getText("userprofiles.upload.convertValidatePublish")));
        uploadedFileActions.add(new SelectItem("2", getText("userprofiles.upload.convertValidatePublishEuropeana")));
        uploadedFileActions.add(new SelectItem("3", getText("userprofiles.upload.convert")));
        uploadedFileActions.add(new SelectItem("4", getText("userprofiles.upload.validate")));
        uploadedFileActions.add(new SelectItem("0", getText("userprofiles.upload.nothing")));
        existingFileActions.add(new SelectItem("1", getText("userprofiles.existing.overwrite")));
        existingFileActions.add(new SelectItem("0", getText("userprofiles.existing.keep")));
        noEadidActions.add(new SelectItem("0", getText("userprofiles.noeadid.remove")));
        noEadidActions.add(new SelectItem("1", getText("userprofiles.noeadid.addLater")));
        daoTypes.add(new SelectItem("1", getText("userprofiles.dao.text")));
        daoTypes.add(new SelectItem("2", getText("userprofiles.dao.image")));
        daoTypes.add(new SelectItem("3", getText("userprofiles.dao.sound")));
        daoTypes.add(new SelectItem("4", getText("userprofiles.dao.video")));
        daoTypes.add(new SelectItem("5", getText("userprofiles.dao.3D")));
        daoTypes.add(new SelectItem("0", getText("userprofiles.dao.unspecified")));
    }

    public Set<SelectItem> getUserprofiles() {
        return userprofiles;
    }

    public void setUserprofiles(Set<SelectItem> userprofiles) {
        this.userprofiles = userprofiles;
    }

    public String getProfilelist() {
        return profilelist;
    }

    public void setProfilelist(String profilelist) {
        this.profilelist = profilelist;
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
