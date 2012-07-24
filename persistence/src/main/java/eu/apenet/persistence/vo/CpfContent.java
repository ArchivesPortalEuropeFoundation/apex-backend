package eu.apenet.persistence.vo;

import java.io.Serializable;

/**
 * User: Yoann Moranville
 * Date: Mar 7, 2011
 *
 * @author Yoann Moranville
 */
public class CpfContent implements Serializable {
    private static final long serialVersionUID = -3382264689231110834L;
    
    private ArchivalInstitution archivalInstitution;
    private Long id;
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
