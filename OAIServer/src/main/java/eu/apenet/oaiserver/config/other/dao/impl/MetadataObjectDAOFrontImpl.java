package eu.apenet.oaiserver.config.other.dao.impl;

import eu.apenet.oaiserver.config.main.MetadataFormats;
import eu.apenet.oaiserver.config.main.dao.MetadataObjectDAOFront;
import eu.apenet.oaiserver.config.main.vo.MetadataObject;
import eu.apenet.oaiserver.config.other.dao.DAOFactory;
import eu.apenet.oaiserver.config.other.dao.EadObjectDAO;
import eu.apenet.oaiserver.config.other.vo.EadObject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class MetadataObjectDAOFrontImpl implements MetadataObjectDAOFront {
    private static final Logger LOG = Logger.getLogger(MetadataObjectDAOFrontImpl.class);
    private EadObjectDAO eadObjectDAO;

    public MetadataObjectDAOFrontImpl() {
        eadObjectDAO = DAOFactory.instance().getEadObjectDAO();
    }

    @Override
    public List<MetadataObject> getMetadataObjects(Date fromDate, Date untilDate, MetadataFormats metadataFormats, String set, Integer start, Integer limit) {
        List<EadObject> eadObjects = eadObjectDAO.getEadsByArguments(fromDate, untilDate, metadataFormats.getName(), set, start, limit);
        LOG.info("Got " + eadObjects.size() + " objects");
        return createMetadataObjects(eadObjects);
    }

    @Override
    public MetadataObject getMetadataObject(String identifier, MetadataFormats metadataFormats) {
        return null;
    }

    @Override
    public Date getTheEarliestDatestamp() {
        return null;
    }

    private static MetadataObject createMetadataObject(EadObject eadObject) {
        MetadataObject metadataObject = new MetadataObject();
        metadataObject.setId(eadObject.getId());
        metadataObject.setOaiIdentifier(eadObject.getOaiIdentifier());
        metadataObject.setXmlPath(eadObject.getXmlPath());
        metadataObject.setSet(eadObject.getDataSet());
        metadataObject.setCreationDate(eadObject.getCreationDate());
        metadataObject.setModificationDate(eadObject.getModificationDate());
        metadataObject.setState(MetadataObject.State.getStateFromName(eadObject.getState()));
        metadataObject.setMetadataFormats(MetadataFormats.getMetadataFormats(eadObject.getMetadataFormat()));
        return metadataObject;
    }

    private static List<MetadataObject> createMetadataObjects(List<EadObject> eadObjects) {
        List<MetadataObject> metadataObjects = new ArrayList<MetadataObject>(eadObjects.size());
        for(EadObject eadObject : eadObjects) {
            metadataObjects.add(createMetadataObject(eadObject));
        }
        return metadataObjects;
    }
}
