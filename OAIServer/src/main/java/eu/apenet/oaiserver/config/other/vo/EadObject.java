package eu.apenet.oaiserver.config.other.vo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yoannmoranville on 09/04/15.
 */
@Entity
@Table(name = "filepaths")
public class EadObject implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name="oai_identifier")
    private String oaiIdentifier;
    @Column(name="xml_path")
    private String xmlPath;
    @Column(name="data_set")
    private String dataSet;

    @Column(name="creation_date")
    private Date creationDate;

    @Column(name="modification_date")
    private Date modificationDate;

    private String state;
    @Column(name="metadata_format")
    private String metadataFormat;

    public EadObject() {}

    public EadObject(int id, String metadataFormat, String state, Date creationDate, String dataSet, String xmlPath, String oaiIdentifier) {
        this.id = id;
        this.metadataFormat = metadataFormat;
        this.state = state;
        this.creationDate = creationDate;
        this.dataSet = dataSet;
        this.xmlPath = xmlPath;
        this.oaiIdentifier = oaiIdentifier;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOaiIdentifier() {
        return oaiIdentifier;
    }

    public void setOaiIdentifier(String oaiIdentifier) {
        this.oaiIdentifier = oaiIdentifier;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMetadataFormat() {
        return metadataFormat;
    }

    public void setMetadataFormat(String metadataFormat) {
        this.metadataFormat = metadataFormat;
    }
}