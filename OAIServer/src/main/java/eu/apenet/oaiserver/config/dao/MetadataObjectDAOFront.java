package eu.apenet.oaiserver.config.dao;

import eu.apenet.oaiserver.config.MetadataFormats;
import eu.apenet.oaiserver.config.vo.MetadataObject;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.MetadataFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 08/04/15.
 */
public interface MetadataObjectDAOFront {
    public abstract List<MetadataObject> getMetadataObjects(Date fromDate, Date untilDate, MetadataFormats metadataFormats, String set, Integer start, Integer limit);

    public abstract MetadataObject getMetadataObject(String identifier, MetadataFormats metadataFormats);

    public abstract Date getTheEarliestDatestamp();
}
