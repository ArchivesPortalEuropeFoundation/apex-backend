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
    @Column(name = "ai_id")
    private Integer aiId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_id", insertable = false, updatable = false)
    private ArchivalInstitution archivalInstitution;
    @Column(name = "name_profile")
    private String nameProfile;
    @Column(name = "file_type")
    private Integer fileType;
    @Column(name = "upload_action")
    private UserprofileDefaultUploadAction uploadAction = UserprofileDefaultUploadAction.CONVERT_VALIDATE_PUBLISH;
    @Column(name = "exist_action")
    private UserprofileDefaultExistingFileAction existAction = UserprofileDefaultExistingFileAction.KEEP;
    @Column(name = "noeadid_action")
    private UserprofileDefaultNoEadidAction noeadidAction = UserprofileDefaultNoEadidAction.REMOVE;
    @Column(name = "dao_type")
    private UserprofileDefaultDaoType daoType = UserprofileDefaultDaoType.UNSPECIFIED;
    @Column(name = "dao_type_from_file")
    private Boolean daoTypeFromFile = true;
    @Column(name = "europeana_dp")
    private String europeanaDataProvider;
    @Column(name = "europeana_dp_from_file")
    private Boolean europeanaDataProviderFromFile = true;
    @Column(name = "europeana_dao_type")
    private Integer europeanaDaoType;
    @Column(name = "europeana_dao_type_from_file")
    private Boolean europeanaDaoTypeFromFile = true;
    @Column(name = "europeana_languages")
    private String europeanaLanguages;
    @Column(name = "europeana_languages_from_file")
    private Boolean europeanaLanguagesFromFile = true;
    @Column(name = "europeana_license")
    private String europeanaLicense;
    @Column(name = "europeana_license_details")
    private String europeanaLicenseDetails;
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

    public Userprofile(Integer aiId, String nameProfile, Integer fileType) {
        this.aiId = aiId;
        this.nameProfile = nameProfile;
        this.fileType = fileType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAiId() {
        return aiId;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
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

    public UserprofileDefaultNoEadidAction getNoeadidAction() {
        return noeadidAction;
    }

    public void setNoeadidAction(UserprofileDefaultNoEadidAction noeadidAction) {
        this.noeadidAction = noeadidAction;
    }

    public UserprofileDefaultDaoType getDaoType() {
        return daoType;
    }

    public void setDaoType(UserprofileDefaultDaoType daoType) {
        this.daoType = daoType;
    }

    public Boolean getDaoTypeFromFile() {
        return daoTypeFromFile;
    }

    public void setDaoTypeFromFile(Boolean daoTypeFromFile) {
        this.daoTypeFromFile = daoTypeFromFile;
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

    public Integer getEuropeanaDaoType() {
        return europeanaDaoType;
    }

    public void setEuropeanaDaoType(Integer europeanaDaoType) {
        this.europeanaDaoType = europeanaDaoType;
    }

    public Boolean getEuropeanaDaoTypeFromFile() {
        return europeanaDaoTypeFromFile;
    }

    public void setEuropeanaDaoTypeFromFile(Boolean europeanaDaoTypeFromFile) {
        this.europeanaDaoTypeFromFile = europeanaDaoTypeFromFile;
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

    public String getEuropeanaLicense() {
        return europeanaLicense;
    }

    public void setEuropeanaLicense(String europeanaLicense) {
        this.europeanaLicense = europeanaLicense;
    }

    public String getEuropeanaLicenseDetails() {
        return europeanaLicenseDetails;
    }

    public void setEuropeanaLicenseDetails(String europeanaLicenseDetails) {
        this.europeanaLicenseDetails = europeanaLicenseDetails;
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
