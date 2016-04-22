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
@Table(name = "hg_sg_fa_relation")
public class HgSgFaRelation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fa_id", foreignKey = @ForeignKey(name = "hg_sg_fa_relation_fa_id_fkey"), insertable = false, updatable = false)
    private FindingAid findingAid;
    @Column(name = "fa_id")
    private Integer faId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hg_id", foreignKey = @ForeignKey(name = "hg_sg_fa_relation_hg_id_fkey"), insertable = false, updatable = false)
    private HoldingsGuide holdingsGuide;
    @Column(name = "hg_id")
    private Integer hgId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sg_id", foreignKey = @ForeignKey(name = "hg_sg_fa_relation_sg_id_fkey"), insertable = false, updatable = false)
    private SourceGuide sourceGuide;
    @Column(name = "sg_id")
    private Integer sgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hg_sg_clevel_id", foreignKey = @ForeignKey(name = "hg_sg_fa_relation_hg_sg_clevel_id_fkey"), insertable = false, updatable = false)
    private CLevel hgSgClevel;

    @Column(name = "hg_sg_clevel_id")
    private Long hgSgClevelId;

    @Column(name = "ai_id")
    private Integer aiId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FindingAid getFindingAid() {
        return findingAid;
    }

    public void setFindingAid(FindingAid findingAid) {
        this.findingAid = findingAid;
    }

    public Integer getFaId() {
        return faId;
    }

    public void setFaId(Integer faId) {
        this.faId = faId;
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

    public CLevel getHgSgClevel() {
        return hgSgClevel;
    }

    public void setHgSgClevel(CLevel hgSgClevel) {
        this.hgSgClevel = hgSgClevel;
    }

    public Long getHgSgClevelId() {
        return hgSgClevelId;
    }

    public void setHgSgClevelId(Long hgSgClevelId) {
        this.hgSgClevelId = hgSgClevelId;
    }

    public Integer getAiId() {
        return aiId;
    }

    public void setAiId(Integer aiId) {
        this.aiId = aiId;
    }

}
