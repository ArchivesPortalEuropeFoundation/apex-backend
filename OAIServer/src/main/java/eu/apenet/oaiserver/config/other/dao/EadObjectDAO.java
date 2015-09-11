package eu.apenet.oaiserver.config.other.dao;

import eu.apenet.oaiserver.config.main.vo.MetadataObject;
import eu.apenet.oaiserver.config.other.vo.EadObject;
import eu.apenet.persistence.vo.MetadataFormat;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public class EadObjectDAO extends AbstractJpaDAO<EadObject, Integer> implements GenericDAO<EadObject, Integer> {
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

    public List<String> getSets() {
        String query = "SELECT DISTINCT(dataSet) FROM EadObject eadObject";
        TypedQuery<String> typedQuery = getEntityManager().createQuery(query, String.class);
        return typedQuery.getResultList();
    }

    public EadObject getEadObjectByIdentifierAndFormat(String identifier, MetadataFormat metadataFormat) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "eadObject");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (identifier != null) {
            criteria.add(Restrictions.like("oaiIdentifier", identifier));
        }
        if (metadataFormat != null) {
            criteria.add(Restrictions.eq("metadataFormat", metadataFormat.toString()));
        }
        if (identifier != null) {
            return (EadObject) criteria.uniqueResult();
        } else if (metadataFormat != null) {
            List<EadObject> tempList = criteria.list();
            if (tempList != null && !tempList.isEmpty()) {
                return (EadObject) tempList.get(0);
            }
        }
        return null;
    }
}
