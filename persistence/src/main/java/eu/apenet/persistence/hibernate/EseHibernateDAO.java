package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.UpFile;

public class EseHibernateDAO extends AbstractHibernateDAO<Ese, Integer> implements EseDAO {

    private final Logger log = Logger.getLogger(getClass());

    @SuppressWarnings("unchecked")
    public List<Ese> getEses(Integer faId, Integer aiId) {
        long startTime = System.currentTimeMillis();
        List<Ese> results = new ArrayList<Ese>();
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "ese");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria = criteria.createAlias("ese.findingAid", "findingAid");
        criteria.add(Restrictions.eq("findingAid.id", faId));

        criteria.createAlias("findingAid.archivalInstitution", "archivalInstitution");
        criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));

        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Ese> getEsesFromDeletedFindingaids(String oaiIdentifier) {
        long startTime = System.currentTimeMillis();
        List<Ese> results = new ArrayList<Ese>();
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "ese");
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.add(Restrictions.isNull("ese.path"));
        criteria.add(Restrictions.eq("ese.oaiIdentifier", oaiIdentifier));
        results = criteria.list();
        long endTime = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("query took " + (endTime - startTime) + " ms to read " + results.size() + " objects");
        }

        return results;
    }

    @Override
    public Date getTheEarliestDatestamp() {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "ese");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        criteria.setProjection(Projections.min("creationDate"));
        return (Date) criteria.uniqueResult();
    }

    @Override
    public List<Ese> getEsesByArguments(Date from, Date until, MetadataFormat metadataFormat, String set,
            Integer startElement, Integer limitPerResponse) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "ese");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (from != null && until != null) {
            criteria.add(Restrictions.between("modificationDate", from, until));
        } else if (until != null) {
            criteria.add(Restrictions.le("modificationDate", until));
        } else if (from != null) {
            criteria.add(Restrictions.ge("modificationDate", from));
        }
        if (metadataFormat != null) {
            criteria.add(Restrictions.eq("metadataFormat", metadataFormat));
        }
        criteria.add(Restrictions.between("eseState.id", 2, 3));
        if (set != null && !set.isEmpty()) {
            /*
			 * if(set.endsWith(":")){
			 * criteria.add(Restrictions.ilike("eset",set+"%")); }else{
			 * criteria.add(Restrictions.like("eset",set)); }
             */
            criteria.add(Restrictions.like("eset", set + "%"));
        }
        Integer firstResult = null;
        if (startElement == null) {
            firstResult = 0;
        } else {
            firstResult = startElement;
        }
        criteria.setFirstResult(firstResult);
        Integer limit = null;
        if (limitPerResponse == null) {
            limit = 100;
        } else {
            limit = limitPerResponse + 1;
        }
        criteria.addOrder(Order.desc("modificationDate"));
        criteria.setMaxResults(limit);
        return criteria.list();
    }

    @Override
    public Ese getEseByIdentifierAndFormat(String identifier, MetadataFormat metadataFormat) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "ese");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (identifier != null) {
            criteria.add(Restrictions.like("oaiIdentifier", identifier));
        }
        if (metadataFormat != null) {
            criteria.add(Restrictions.eq("metadataFormat", metadataFormat));
        }
        if (identifier != null) {
            return (Ese) criteria.uniqueResult();
        } else if (metadataFormat != null) {
            List<Ese> tempList = criteria.list();
            if (tempList != null && !tempList.isEmpty()) {
                return (Ese) tempList.get(0);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Ese> getEsesByFindingAidAndState(Integer faId, EseState eseState) {
        Criteria criteria = getSession().createCriteria(getPersistentClass(), "ese");
        criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (faId != null) {
            criteria.add(Restrictions.eq("findingAid.id", faId));
        }
        if (eseState != null) {
            criteria.add(Restrictions.eq("eseState", eseState));
        }
        return criteria.list();
    }

    @Override
    public List<String> getSets() {
        String query = "SELECT DISTINCT(eset) FROM Ese";
        TypedQuery<String> typedQuery = getEntityManager().createQuery(query, String.class);
        return typedQuery.getResultList();
    }

    @Override
    public List<String> getSetsWithPublicatedFiles() {
        //SELECT DISTINCT(eset) FROM Ese WHERE eseState=2;
        String query = "SELECT DISTINCT(eset) FROM Ese WHERE eseState.id = 2";
        TypedQuery<String> typedQuery = getEntityManager().createQuery(query, String.class);
        return typedQuery.getResultList();    }

}
