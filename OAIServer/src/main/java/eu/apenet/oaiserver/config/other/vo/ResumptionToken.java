package eu.apenet.oaiserver.config.other.vo;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yoannmoranville on 09/04/15.
 */
@Entity
@Table(name = "resumptiontoken")
public class ResumptionToken implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "metadata_format")
    private String metadataFormats;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "from_date")
    private Date fromDate;
    @Column(name = "until_date")
    private Date untilDate;
    @Column(name = "data_set")
    private String set;
    @Column(name = "last_record_harvested")
    private String lastRecordHarvested;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMetadataFormats() {
        return metadataFormats;
    }

    public void setMetadataFormats(String metadataFormats) {
        this.metadataFormats = metadataFormats;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getLastRecordHarvested() {
        return lastRecordHarvested;
    }

    public void setLastRecordHarvested(String lastRecordHarvested) {
        this.lastRecordHarvested = lastRecordHarvested;
    }
}
