package eu.apenet.persistence.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ead_content")
public class EadContent implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7231712945693944551L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long ecId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fa_id", foreignKey = @ForeignKey(name = "ead_content_fa_id_fkey"), insertable = false, updatable = false)
    private FindingAid findingAid;
    @Column(name = "fa_id")
    private Integer faId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hg_id", foreignKey = @ForeignKey(name = "ead_content_hg_id_fkey"), insertable = false, updatable = false)
    private HoldingsGuide holdingsGuide;
    @Column(name = "hg_id")
    private Integer hgId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sg_id", foreignKey = @ForeignKey(name = "ead_content_sg_id_fkey"), insertable = false, updatable = false)
    private SourceGuide sourceGuide;
    @Column(name = "sg_id")
    private Integer sgId;
    private String titleproper;
    private String eadid;
    private String unittitle;
    private String xml;

    public Ead getEad() {
        if (findingAid != null) {
            return findingAid;
        } else if (holdingsGuide != null) {
            return holdingsGuide;
        } else {
            return sourceGuide;
        }
    }

    public Integer getEadIdentifier() {
        if (faId != null) {
            return faId;
        } else if (hgId != null) {
            return hgId;
        } else {
            return sgId;
        }
    }

    public FindingAid getFindingAid() {
        return findingAid;
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

    public Integer getSgId() {
        return sgId;
    }

    public void setSgId(Integer sgId) {
        this.sgId = sgId;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getTitleproper() {
        return titleproper;
    }

    public void setTitleproper(String titleproper) {
        this.titleproper = titleproper;
    }

    public String getEadid() {
        return eadid;
    }

    public void setEadid(String eadid) {
        this.eadid = eadid;
    }

    public String getUnittitle() {
        return unittitle;
    }

    public void setUnittitle(String unittitle) {
        this.unittitle = unittitle;
    }

    public Integer getFaId() {
        return faId;
    }

    public void setFaId(Integer faId) {
        this.faId = faId;
    }

    public Long getEcId() {
        return ecId;
    }

    public void setEcId(Long ecId) {
        this.ecId = ecId;
    }

    public HoldingsGuide getHoldingsGuide() {
        return holdingsGuide;
    }

    public void setHoldingsGuide(HoldingsGuide holdingsGuide) {
        this.holdingsGuide = holdingsGuide;
    }

    public Integer getHgId() {
        return hgId;
    }

    public void setHgId(Integer hgId) {
        this.hgId = hgId;
    }

}
