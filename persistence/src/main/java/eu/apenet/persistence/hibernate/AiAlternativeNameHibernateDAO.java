package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Lang;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;

public class AiAlternativeNameHibernateDAO extends AbstractHibernateDAO<AiAlternativeName, Integer> implements AiAlternativeNameDAO{
	@Override
	public ArrayList<AiAlternativeName> findByAIId(ArchivalInstitution ai) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("archivalInstitution",ai));
		return (ArrayList<AiAlternativeName>) criteria.list();
	}
	
	@Override
	public AiAlternativeName findByAIIdandLang(ArchivalInstitution ai, Lang language){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("archivalInstitution", ai));
		criteria.add(Restrictions.eq("lang", language));
		return (AiAlternativeName)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<AiAlternativeName> findByAiAName(String AiAName){
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("AiAName", AiAName));
		return (ArrayList<AiAlternativeName>)criteria.list();
	}

	@Override
	public AiAlternativeName findByAIId_primarykey(ArchivalInstitution ai) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "archivalInstitution");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.add(Restrictions.eq("archivalInstitution",ai));
		criteria.add(Restrictions.eq("primaryName", true));
		return (AiAlternativeName) criteria.uniqueResult();
	}
}