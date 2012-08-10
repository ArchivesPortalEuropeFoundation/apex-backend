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

/**
 * User: Yoann Moranville
 * Date: Mar 7, 2011
 *
 * @author Yoann Moranville
 */
@Entity
@Table(name = "cpf_content")
public class CpfContent implements Serializable {
    private static final long serialVersionUID = -3382264689231110834L;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ai_id")
    private ArchivalInstitution archivalInstitution;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cpf_id")
	private String cpfId;
    private String xml;

    public ArchivalInstitution getArchivalInstitution() {
        return archivalInstitution;
    }

    public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
        this.archivalInstitution = archivalInstitution;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpfId() {
        return cpfId;
    }

    public void setCpfId(String cpfId) {
        this.cpfId = cpfId;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
