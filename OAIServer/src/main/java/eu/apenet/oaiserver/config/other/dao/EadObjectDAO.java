package eu.apenet.oaiserver.config.other.dao;

import eu.apenet.oaiserver.config.main.vo.MetadataObject;
import eu.apenet.oaiserver.config.other.vo.EadObject;
import org.apache.log4j.Logger;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class EadObjectDAO extends AbstractHibernateDAO<EadObject, Integer> implements GenericDAO<EadObject, Integer> {
    private static final Logger LOG = Logger.getLogger(EadObjectDAO.class);

    public List<EadObject> getEadsByArguments(Date from, Date until, String metadataFormats, String set, Integer start, Integer limitPerResponse) {
        LOG.info("Search for: " + metadataFormats + ", " + set + ", " + start + ", " + limitPerResponse + ", " + from + ", " + until + ", " + MetadataObject.State.PUBLISHED.getName());
        TypedQuery<EadObject> query = getEntityManager().createQuery(
                "SELECT eadObject FROM EadObject eadObject WHERE modificationDate BETWEEN :fromdate AND :untildate AND metadataFormat = :metadataFormats AND (state = :statePublished OR state = :stateRemoved)AND dataSet = :set ORDER BY modificationDate DESC",
                EadObject.class);
        query.setParameter("fromdate", from);
        query.setParameter("untildate", until);
        query.setParameter("metadataFormats", metadataFormats);
        query.setParameter("statePublished", MetadataObject.State.PUBLISHED.getName());
        query.setParameter("stateRemoved", MetadataObject.State.REMOVED.getName());
        query.setParameter("set", set);
        query.setFirstResult(start);
        query.setMaxResults(limitPerResponse + 1);
        return query.getResultList();
    }
}
