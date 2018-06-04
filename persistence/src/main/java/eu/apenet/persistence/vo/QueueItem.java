package eu.apenet.persistence.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "queue")
public class QueueItem implements java.io.Serializable {

    public static final String XML_TYPE = "ape.xmltype";
    public static final String NO_EADID_ACTION = "ape.noeadidaction";
    public static final String EXIST_ACTION = "ape.existaction";
    public static final String DAO_TYPE = "ape.daotype";
    public static final String DAO_TYPE_CHECK = "ape.daotypecheck";
    public static final String PERSIST_EACCPF_FROM_EAD3 = "ape.persisteac-cpf";
    public static final String UPLOAD_ACTION = "ape.uploadaction";
    public static final String CONVERSION_TYPE = "europeana.conversiontype";
    public static final String DATA_PROVIDER = "europeana.dataprovider";
    public static final String DATA_PROVIDER_CHECK = "europeana.dataprovidercheck";
    public static final String EUROPEANA_DAO_TYPE = "europeana.daotype";
    public static final String EUROPEANA_DAO_TYPE_CHECK = "europeana.daotypecheck";
    public static final String LANGUAGES = "europeana.languages";
    public static final String LANGUAGE_CHECK = "europeana.languagecheck";
    public static final String LICENSE_CHECK = "europeana.licensecheck";
    public static final String LICENSE = "europeana.license";
    public static final String LICENSE_DETAILS = "europeana.licensedetails";
    public static final String LICENSE_ADD_INFO = "europeana.licenseaddinfo";
    public static final String INHERIT_FILE_CHECK = "europeana.inheritfilecheck";
    public static final String INHERIT_FILE = "europeana.inheritfile";
    public static final String INHERIT_ORIGINATION_CHECK = "europeana.inheritoriginationcheck";
    public static final String INHERIT_ORIGINATION = "europeana.inheritorigination";
    public static final String INHERIT_UNITTITLE_CHECK = "europeana.inheritunittitlecheck";
    public static final String INHERIT_UNITTITLE = "europeana.inheritunittitle";
    public static final String SOURCE_OF_IDENTIFIERS = "europeana.sourceOfIdentifiers";
    public static final String RIGHTS_OF_DIGITAL_OBJECTS = "ape.rightsOfDigitalObjects";
    public static final String RIGHTS_OF_DIGITAL_OBJECTS_TEXT = "ape.rightsOfDigitalObjectsText";
    public static final String RIGHTS_OF_DIGITAL_DESCRIPTION = "ape.rightsOfDigitalDescription";
    public static final String RIGHTS_OF_DIGITAL_HOLDER = "ape.rightsOfDigitalHolder";
    public static final String RIGHTS_OF_EAD_DATA = "ape.rightsOfEADData";
    public static final String RIGHTS_OF_EAD_DATA_TEXT = "ape.rightsOfEADDataText";
    public static final String RIGHTS_OF_EAD_DESCRIPTION = "ape.rightsOfEADDescription";
    public static final String RIGHTS_OF_EAD_HOLDER = "ape.rightsOfEADHolder";
    public static final String XSL_FILE = "ape.xslFile";

    /**
     *
     */
    private static final long serialVersionUID = -4266955613706066708L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hg_id", foreignKey = @ForeignKey(name = "index_queue_hg_id_fkey"))
    private HoldingsGuide holdingsGuide;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fa_id", foreignKey = @ForeignKey(name = "index_queue_fa_id_fkey"))
    private FindingAid findingAid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sg_id", foreignKey = @ForeignKey(name = "index_queue_sg_id_fkey"))
    private SourceGuide sourceGuide;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eac_cpf_id", foreignKey = @ForeignKey(name = "index_queue_eac_id_fkey"))
    private EacCpf eacCpf;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ead3_id", foreignKey = @ForeignKey(name = "index_queue_ead3_id_fkey"))
    private Ead3 ead3;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uf_id", foreignKey = @ForeignKey(name = "queue_uf_id_fkey"))
    private UpFile upFile;
    @Column(name = "uf_id", updatable = false, insertable = false)
    private Integer upFileId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "queue_date")
    private Date queueDate;
    @Column(columnDefinition="TEXT")
    private String errors;
    private Integer priority;
    @Enumerated(EnumType.STRING)
    private QueueAction action;
    @Column(columnDefinition="TEXT")
    private String preferences;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_id", foreignKey = @ForeignKey(name = "queue_ai_id_fkey"), updatable = false, insertable = false)
    private ArchivalInstitution archivalInstitution;
    @Column(name = "ai_id")
    private Integer aiId;

    public QueueItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HoldingsGuide getHoldingsGuide() {
        return this.holdingsGuide;
    }

    public void setHoldingsGuide(HoldingsGuide holdingsGuide) {
        this.holdingsGuide = holdingsGuide;
    }

    public FindingAid getFindingAid() {
        return this.findingAid;
    }

    public void setFindingAid(FindingAid findingAid) {
        this.findingAid = findingAid;
    }

    public SourceGuide getSourceGuide() {
        return sourceGuide;
    }

    public void setSourceGuide(SourceGuide sourceGuide) {
        this.sourceGuide = sourceGuide;
    }

    public EacCpf getEacCpf() {
        return eacCpf;
    }

    public void setEacCpf(EacCpf eacCpf) {
        this.eacCpf = eacCpf;
    }

    public Ead3 getEad3() {
        return ead3;
    }

    public void setEad3(Ead3 ead3) {
        this.ead3 = ead3;
    }

    public Date getQueueDate() {
        return queueDate;
    }

    public void setQueueDate(Date queueDate) {
        this.queueDate = queueDate;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public QueueAction getAction() {
        return action;
    }

    public void setAction(QueueAction action) {
        this.action = action;
    }

    public String getPreferences() {
        return this.preferences==null?"":this.preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public AbstractContent getAbstractContent() {
        if (this.findingAid != null) {
            return this.findingAid;
        } else if (this.holdingsGuide != null) {
            return this.holdingsGuide;
        } else if (this.sourceGuide != null) {
            return this.sourceGuide;
        } else if (this.ead3 != null) {
            return this.ead3;
        }
        return this.eacCpf;
    }

    public Ead getEad() {
        if (this.findingAid != null) {
            return this.findingAid;
        } else if (this.holdingsGuide != null) {
            return this.holdingsGuide;
        } else if (this.sourceGuide != null) {
            return this.sourceGuide;
        }
        return null;
    }

    public void setAbstractContent(AbstractContent content) {
        if (content == null) {
            this.findingAid = null;
            this.holdingsGuide = null;
            this.sourceGuide = null;
            this.eacCpf = null;
        } else {
            if (content instanceof FindingAid) {
                this.findingAid = (FindingAid) content;
            } else if (content instanceof HoldingsGuide) {
                this.holdingsGuide = (HoldingsGuide) content;
            } else if (content instanceof SourceGuide) {
                this.sourceGuide = (SourceGuide) content;
            } else if (content instanceof Ead3) {
                this.ead3 = (Ead3) content;
            } else {
                this.eacCpf = (EacCpf) content;
            }
        }
    }

    public void setEad(Ead ead) {
        if (ead == null) {
            this.findingAid = null;
            this.holdingsGuide = null;
            this.sourceGuide = null;
        } else {
            if (ead instanceof FindingAid) {
                this.findingAid = (FindingAid) ead;
            } else if (ead instanceof HoldingsGuide) {
                this.holdingsGuide = (HoldingsGuide) ead;
            } else if (ead instanceof SourceGuide) {
                this.sourceGuide = (SourceGuide) ead;
            }
        }
    }

    public UpFile getUpFile() {
        return upFile;
    }

    public void setUpFile(UpFile upFile) {
        this.upFile = upFile;
    }

    public Integer getUpFileId() {
        return upFileId;
    }

    public void setUpFileId(Integer upFileId) {
        this.upFileId = upFileId;
    }

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    public Integer getAiId() {
        return aiId;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

}
