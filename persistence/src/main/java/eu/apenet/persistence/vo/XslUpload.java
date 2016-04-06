package eu.apenet.persistence.vo;

import javax.persistence.*;

/**
 * Created by yoannmoranville on 29/12/14.
 */
@Entity
@Table(name = "xsl_upload")
public class XslUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "readable_name")
    private String readableName;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "archival_institution_id", foreignKey = @ForeignKey(name = "archival_institution_id_fkey"))
    private ArchivalInstitution archivalInstitution;
    @Column(name = "archival_institution_id", updatable = false, insertable = false)
    private Integer archivalInstitutionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReadableName() {
        return readableName;
    }

    public void setReadableName(String readableName) {
        this.readableName = readableName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    public Integer getArchivalInstitutionId() {
        return archivalInstitutionId;
    }

    public void setArchivalInstitutionId(Integer archivalInstitutionId) {
        this.archivalInstitutionId = archivalInstitutionId;
    }
}
