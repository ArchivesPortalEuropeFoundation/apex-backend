package eu.apenet.oaiserver.config.other.dao.impl;

import eu.apenet.oaiserver.config.main.MetadataFormats;
import eu.apenet.oaiserver.config.main.dao.MetadataObjectDAOFront;
import eu.apenet.oaiserver.config.main.vo.MetadataObject;

import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class MetadataObjectDAOFrontImpl implements MetadataObjectDAOFront {
    @Override
    public List<MetadataObject> getMetadataObjects(Date fromDate, Date untilDate, MetadataFormats metadataFormats, String set, Integer start, Integer limit) {
        return null;
    }

    @Override
    public MetadataObject getMetadataObject(String identifier, MetadataFormats metadataFormats) {
        return null;
    }

    @Override
    public Date getTheEarliestDatestamp() {
        return null;
    }
}
