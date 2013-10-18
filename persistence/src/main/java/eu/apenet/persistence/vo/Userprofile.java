/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.vo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "userprofile")
public class Userprofile implements Serializable {

    private static final long serialVersionUID = 1383138313831383138L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "name_profile")
    private String nameProfile;
    @Column(name = "file_type")
    private Integer fileType;
    @Column(name = "upload_action")
    private UserprofileDefaultUploadAction uploadAction = UserprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH;
    @Column(name = "exist_action")
    private UserprofileDefaultExistingFileAction existAction = UserprofileDefaultExistingFileAction.KEEP;
    @Column(name = "noeadid_action")
    private Integer noeadidAction;
    @Column(name = "dao_type")
    private UserprofileDefaultDaoType daoType = UserprofileDefaultDaoType.UNSPECIFIED;
    @Column(name = "europeana_dp")
    private String europeanaDataProvider;
    @Column(name = "europeana_dp_from_file")
    private Boolean europeanaDataProviderFromFile = true;
    @Column(name = "europeana_languages")
    private String europeanaLanguages;
    @Column(name = "europeana_languages_from_file")
    private Boolean europeanaLanguagesFromFile = true;
    @Column(name = "europeana_license")
    private Integer europeanaLicense = 3;
    @Column(name = "europeana_rights")
    private Integer europeanaRights;
    @Column(name = "europeana_add_rights")
    private String europeanaAddRights;
    @Column(name = "europeana_hp")
    private String europeanaHierarchyPrefix;
    @Column(name = "europeana_inh_elements")
    private Boolean europeanaInheritElements = false;
    @Column(name = "europeana_inh_origin")
    private Boolean europeanaInheritOrigin = false;

    public Userprofile() {
    }

    public Userprofile(User user, String nameProfile, Integer fileType) {
        this.user = user;
        this.nameProfile = nameProfile;
        this.fileType = fileType;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNameProfile() {
        return nameProfile;
    }

    public void setNameProfile(String nameProfile) {
        this.nameProfile = nameProfile;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public UserprofileDefaultUploadAction getUploadAction() {
        return uploadAction;
    }

    public void setUploadAction(UserprofileDefaultUploadAction uploadAction) {
        this.uploadAction = uploadAction;
    }

    public UserprofileDefaultExistingFileAction getExistAction() {
        return existAction;
    }

    public void setExistAction(UserprofileDefaultExistingFileAction existAction) {
        this.existAction = existAction;
    }

    public Integer getNoeadidAction() {
        return noeadidAction;
    }

    public void setNoeadidAction(Integer noeadidAction) {
        this.noeadidAction = noeadidAction;
    }

    public UserprofileDefaultDaoType getDaoType() {
        return daoType;
    }

    public void setDaoType(UserprofileDefaultDaoType daoType) {
        this.daoType = daoType;
    }

    public String getEuropeanaDataProvider() {
        return europeanaDataProvider;
    }

    public void setEuropeanaDataProvider(String europeanaDataProvider) {
        this.europeanaDataProvider = europeanaDataProvider;
    }

    public Boolean getEuropeanaDataProviderFromFile() {
        return europeanaDataProviderFromFile;
    }

    public void setEuropeanaDataProviderFromFile(Boolean europeanaDataProviderFromFile) {
        this.europeanaDataProviderFromFile = europeanaDataProviderFromFile;
    }

    public String getEuropeanaLanguages() {
        return europeanaLanguages;
    }

    public void setEuropeanaLanguages(String europeanaLanguages) {
        this.europeanaLanguages = europeanaLanguages;
    }

    public Boolean getEuropeanaLanguagesFromFile() {
        return europeanaLanguagesFromFile;
    }

    public void setEuropeanaLanguagesFromFile(Boolean europeanaLanguagesFromFile) {
        this.europeanaLanguagesFromFile = europeanaLanguagesFromFile;
    }

    public Integer getEuropeanaLicense() {
        return europeanaLicense;
    }

    public void setEuropeanaLicense(Integer europeanaLicense) {
        this.europeanaLicense = europeanaLicense;
    }

    public Integer getEuropeanaRights() {
        return europeanaRights;
    }

    public void setEuropeanaRights(Integer europeanaRights) {
        this.europeanaRights = europeanaRights;
    }

    public String getEuropeanaAddRights() {
        return europeanaAddRights;
    }

    public void setEuropeanaAddRights(String europeanaAddRights) {
        this.europeanaAddRights = europeanaAddRights;
    }

    public String getEuropeanaHierarchyPrefix() {
        return europeanaHierarchyPrefix;
    }

    public void setEuropeanaHierarchyPrefix(String europeanaHierarchyPrefix) {
        this.europeanaHierarchyPrefix = europeanaHierarchyPrefix;
    }

    public Boolean getEuropeanaInheritElements() {
        return europeanaInheritElements;
    }

    public void setEuropeanaInheritElements(Boolean europeanaInheritElements) {
        this.europeanaInheritElements = europeanaInheritElements;
    }

    public Boolean getEuropeanaInheritOrigin() {
        return europeanaInheritOrigin;
    }

    public void setEuropeanaInheritOrigin(Boolean europeanaInheritOrigin) {
        this.europeanaInheritOrigin = europeanaInheritOrigin;
    }
}
