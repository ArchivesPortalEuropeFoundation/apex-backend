package eu.apenet.persistence.hibernate;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.dao.SentMailRegisterDAO;
import eu.apenet.persistence.exception.SentMailRegisterException;
import eu.apenet.persistence.vo.SentMailRegister;
import eu.apenet.persistence.vo.User;

public class SentMailRegisterHibernateDAO extends AbstractHibernateDAO<SentMailRegister, Long> implements
		SentMailRegisterDAO {

	private final Logger log = Logger.getLogger(getClass());




	@SuppressWarnings("unchecked")
	@Override
	public SentMailRegister getSentMailRegisterByPartner(User p) throws SentMailRegisterException {
		String errorMessage = "Error consulting DB for to get a sent_mail_register throught a Partner Id: "
				+ p.getId() + "";
		SentMailRegister result = new SentMailRegister();
		try {
			Criteria criteria = createOperationTypeByPartnerCriteria(p);
			return (SentMailRegister) criteria.uniqueResult();
		}catch (Exception e) {
			throw new SentMailRegisterException(e.getMessage() + errorMessage,e);
		}
	}

	private Criteria createOperationTypeByPartnerCriteria(User p) {
		Criteria criteria = getSession().createCriteria(getPersistentClass(), "sentMailRegister");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		if (p != null) {
			criteria.add(Restrictions.eq("sentMailRegister.user", p));
		}
		return criteria;
	}

	@Override
	public SentMailRegister exitsSentMailRegisterOnDate(String validation_link, Date date)
			throws SentMailRegisterException {
		String errorMessage = "Error consulting DB for validation_link on date in sent_mail_register: ";
		try {
			return (SentMailRegister) getSession().createCriteria(SentMailRegister.class)
					.add(Example.create(new SentMailRegister(validation_link, date, null))).uniqueResult();
		}catch (Exception e) {
			throw new SentMailRegisterException(e.getMessage() + errorMessage,e);
		}
	}



	@Override
	public SentMailRegister exitsValidationLinkSentMailRegister(String validation_link)
			throws SentMailRegisterException {
		String errorMessage = "Error consulting DB for validation_link in sent mail register: ";
		// TODO: Control multiple results.
		try {
			return (SentMailRegister) getSession().createCriteria(SentMailRegister.class)
					.add(Example.create(new SentMailRegister(validation_link, null, null))).uniqueResult();
		} catch (Exception e) {
			throw new SentMailRegisterException(e.getMessage() + errorMessage, e);
		}
	}



	@SuppressWarnings("unchecked")
	@Override
	public List<SentMailRegister> getSentMailRegisterByAi(Integer aiId) {

		Criteria criteria = getSession().createCriteria(getPersistentClass(), "sentMailRegister");
		criteria = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		criteria.add(Restrictions.eq("archivalInstitution.aiId", aiId));
		return criteria.list();

	}

}