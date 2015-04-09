package eu.apenet.oaiserver.config.ape.dao.impl;

import eu.apenet.oaiserver.config.main.MetadataFormats;
import eu.apenet.oaiserver.config.main.dao.MetadataObjectDAOFront;
import eu.apenet.oaiserver.config.main.vo.MetadataObject;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.MetadataFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 08/04/15.
 */
public class MetadataObjectDAOFrontImpl implements MetadataObjectDAOFront {
    private EseDAO eseDAO;

    public MetadataObjectDAOFrontImpl() {
        eseDAO = DAOFactory.instance().getEseDAO();
    }

    @Override
    public List<MetadataObject> getMetadataObjects(Date fromDate, Date untilDate, MetadataFormats metadataFormats, String set, Integer start, Integer limit) {
        MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(metadataFormats.getName());
        List<Ese> eses = eseDAO.getEsesByArguments(fromDate, untilDate, metadataFormat, set, start, limit);
        return createMetadataObjects(eses);
    }

    @Override
    public MetadataObject getMetadataObject(String identifier, MetadataFormats metadataFormats) {
        MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(metadataFormats.getName());
        Ese ese = eseDAO.getEseByIdentifierAndFormat(identifier, metadataFormat);
        if(ese == null) {
            return null;
        }
        return createMetadataObject(ese);
    }

    @Override
    public Date getTheEarliestDatestamp() {
        return eseDAO.getTheEarliestDatestamp();
    }

    private static MetadataObject createMetadataObject(Ese ese) {
        MetadataObject metadataObject = new MetadataObject();
        metadataObject.setId(ese.getEseId());
        metadataObject.setOaiIdentifier(ese.getOaiIdentifier());
        metadataObject.setXmlPath(ese.getPath());
        metadataObject.setSet(ese.getEset());
        metadataObject.setCreationDate(ese.getCreationDate());
        metadataObject.setModificationDate(ese.getModificationDate());
        metadataObject.setState(MetadataObject.State.getStateFromName(ese.getEseState().getState()));
        metadataObject.setMetadataFormats(MetadataFormats.EDM);
        return metadataObject;
    }

    private static List<MetadataObject> createMetadataObjects(List<Ese> eses) {
        List<MetadataObject> metadataObjects = new ArrayList<MetadataObject>(eses.size());
        for(Ese ese : eses) {
            metadataObjects.add(createMetadataObject(ese));
        }
        return metadataObjects;
    }
}
