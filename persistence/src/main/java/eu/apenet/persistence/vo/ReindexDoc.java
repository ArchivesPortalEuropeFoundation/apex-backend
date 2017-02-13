/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author kaisar
 */
@Entity
@Table(name = "reindex_doc")
public class ReindexDoc implements Serializable {

    private static final long serialVersionUID = -3567653491060394677L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "hg_id", foreignKey = @ForeignKey(name = "reindex_hg_id_fkey"))
    private HoldingsGuide holdingsGuide;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fa_id", foreignKey = @ForeignKey(name = "reindex_fa_id_fkey"))
    private FindingAid findingAid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sg_id", foreignKey = @ForeignKey(name = "reindex_sg_id_fkey"))
    private SourceGuide sourceGuide;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eac_cpf_id", foreignKey = @ForeignKey(name = "reindex_eac_id_fkey"))
    private EacCpf eacCpf;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ead3_id", foreignKey = @ForeignKey(name = "reindex_ead3_id_fkey"))
    private Ead3 ead3;
    @JoinColumn(name = "ai_id", foreignKey = @ForeignKey(name = "reindex_ai_id_fkey"), updatable = false, insertable = false)
    private ArchivalInstitution archivalInstitution;
    @Column(name = "ai_id")
    private Integer aiId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HoldingsGuide getHoldingsGuide() {
        return holdingsGuide;
    }

    public void setHoldingsGuide(HoldingsGuide holdingsGuide) {
        this.holdingsGuide = holdingsGuide;
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

    public void setAbstractContent(AbstractContent content) {
        if (content == null) {
            this.findingAid = null;
            this.holdingsGuide = null;
            this.sourceGuide = null;
            this.eacCpf = null;
        } else if (content instanceof FindingAid) {
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
