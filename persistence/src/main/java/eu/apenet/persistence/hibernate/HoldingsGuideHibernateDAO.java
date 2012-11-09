package eu.apenet.persistence.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.HoldingsGuide;



public class HoldingsGuideHibernateDAO extends AbstractHibernateDAO<HoldingsGuide, Integer> implements HoldingsGuideDAO {

	private final Logger log = Logger.getLogger(getClass());


	/**
	 * Returns the title of a holdings guide indexed into the system by a finding_aid.eadid
	 * and a archival institution id.
	 */
	@Override
	public String getLinkedHoldingsGuideTitleByFindingAidEadid(String eadid,Integer aiId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "holdingsGuide");
		criteria.setProjection(Property.forName("holdingsGuide.eadid"));
			DetachedCriteria subQuery = DetachedCriteria.forClass(EadContent.class,"eadContent");
			subQuery.setProjection(Property.forName("eadContent.hgId"));
				DetachedCriteria subQuery2 = DetachedCriteria.forClass(CLevel.class,"cLevel");
				subQuery2.add(Restrictions.eq("cLevel.hrefEadid", eadid));
				subQuery2.setProjection(Property.forName("cLevel.ecId"));
			subQuery.add(Subqueries.propertyIn("eadContent.ecId", subQuery2));
		criteria.add(Subqueries.propertyIn("holdingsGuide.id", subQuery));
		criteria.add(Restrictions.eq("findingAid.published", true));
		if(aiId!=null){
			criteria.add(Restrictions.eq("holdingsGuide.archivalInstitution.aiId", aiId));
		}
		List<?> object = criteria.list();
		if(object!=null && !object.isEmpty()){
			return (String)object.get(0).toString();
		}
		return null;
	}

}
