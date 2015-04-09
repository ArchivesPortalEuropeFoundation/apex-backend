package eu.apenet.oaiserver.config.main.dao;

import eu.apenet.oaiserver.config.main.MetadataFormats;
import eu.apenet.oaiserver.config.main.vo.MetadataObject;

import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public interface MetadataObjectDAOFront {
    public abstract List<MetadataObject> getMetadataObjects(Date fromDate, Date untilDate, MetadataFormats metadataFormats, String set, Integer start, Integer limit);

    public abstract MetadataObject getMetadataObject(String identifier, MetadataFormats metadataFormats);

    public abstract Date getTheEarliestDatestamp();
}
