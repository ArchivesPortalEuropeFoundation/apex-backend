package eu.apenet.oaiserver.config.main.vo;

import eu.apenet.oaiserver.config.main.MetadataFormats;

import java.util.Date;

/**
 * Created by yoannmoranville on 08/04/15.
 */
public class ResumptionTokens {
    private int id;
    private MetadataFormats metadataFormats;
    private Date expirationDate;
    private Date fromDate;
    private Date untilDate;
    private String set;
    private String lastRecordHarvested;

    public ResumptionTokens() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MetadataFormats getMetadataFormats() {
        return metadataFormats;
    }

    public void setMetadataFormats(MetadataFormats metadataFormats) {
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
