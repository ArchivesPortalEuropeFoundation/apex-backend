package eu.apenet.persistence.hibernate;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

public class ResumptionTokenHibernateDAO extends AbstractHibernateDAO<ResumptionToken, Integer> implements
		ResumptionTokenDAO {
	@Override
	public ResumptionToken getResumptionToken(Date fromDate, Date untilDate, MetadataFormat metadataFormat, String set,
			String limit) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "resumptionToken");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("fromDate", fromDate));
		criteria.add(Restrictions.eq("untilDate", untilDate));
		if (metadataFormat != null) {
			criteria.add(Restrictions.eq("metadataFormat", metadataFormat));
		}
		if (set != null && !set.isEmpty()) {
			criteria.add(Restrictions.eq("set", set));
		}
		criteria.add(Restrictions.eq("lastRecordHarvested", limit));
		List<ResumptionToken> resultsList = criteria.list();
		ResumptionToken resumptionToken = null;
		if (resultsList != null && !resultsList.isEmpty()) {
			resumptionToken = resultsList.get(0);
		}
		return resumptionToken; // criteria.uniqueResult();
	}

	@Override
	public List<ResumptionToken> getOldResumptionTokensThan(Date referenceDate) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "resumptionToken");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.lt("expirationDate", referenceDate));
		return criteria.list();
	}

	@Override
	public boolean containsValidResumptionTokens(Date referenceDate) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "resumptionToken");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.ge("expirationDate", referenceDate));
		criteria.setMaxResults(1);
		return criteria.list().size() > 0;
	}

	public Date getPossibleEndDateTime() {
		TypedQuery<Date> query = getEntityManager().createQuery(
				"SELECT rt.expirationDate FROM ResumptionToken rt ORDER BY rt.expirationDate DESC", Date.class);
		query.setMaxResults(1);
		if (query.getResultList().size() > 0) {
			return query.getResultList().get(0);
		}
		return null;
	}
}
