package eu.apenet.oaiserver.config.other.vo;

import eu.apenet.oaiserver.config.main.MetadataFormats;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yoannmoranville on 09/04/15.
 */
@Entity
@Table(name = "resumption_token")
public class ResumptionToken implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @Column(name = "metadata_formats")
    private String metadataFormats;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "from_date")
    private Date fromDate;
    @Column(name = "until_date")
    private Date untilDate;
    private String set;
    @Column(name = "last_record_harvested")
    private String lastRecordHarvested;
}
