package eu.apenet.persistence.factory;

import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.CoordinatesDAO;
import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.DptUpdateDAO;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSavedSearchDAO;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.EseStateDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.dao.HoldingsGuideDAO;
import eu.apenet.persistence.dao.IngestionprofileDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.dao.QueueItemDAO;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.dao.SentMailRegisterDAO;
import eu.apenet.persistence.dao.UpFileDAO;
import eu.apenet.persistence.dao.UploadMethodDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.dao.UserRoleDAO;
import eu.apenet.persistence.dao.WarningsDAO;
import eu.apenet.persistence.hibernate.AiAlternativeNameHibernateDAO;
import eu.apenet.persistence.hibernate.ArchivalInstitutionHibernateDAO;
import eu.apenet.persistence.hibernate.CLevelHibernateDAO;
import eu.apenet.persistence.hibernate.CoordinatesHibernateDAO;
import eu.apenet.persistence.hibernate.CouAlternativeNameHibernateDAO;
import eu.apenet.persistence.hibernate.CountryHibernateDAO;
import eu.apenet.persistence.hibernate.DptUpdateHibernateDAO;
import eu.apenet.persistence.hibernate.EacCpfHibernateDAO;
import eu.apenet.persistence.hibernate.EadContentHibernateDAO;
import eu.apenet.persistence.hibernate.EadHibernateDAO;
import eu.apenet.persistence.hibernate.EseHibernateDAO;
import eu.apenet.persistence.hibernate.EseStateHibernateDAO;
import eu.apenet.persistence.hibernate.FindingAidHibernateDAO;
import eu.apenet.persistence.hibernate.HoldingsGuideHibernateDAO;
import eu.apenet.persistence.hibernate.LangHibernateDAO;
import eu.apenet.persistence.hibernate.QueueItemHibernateDAO;
import eu.apenet.persistence.hibernate.ResumptionTokenHibernateDAO;
import eu.apenet.persistence.hibernate.SentMailRegisterHibernateDAO;
import eu.apenet.persistence.hibernate.UploadMethodHibernateDAO;
import eu.apenet.persistence.hibernate.UserHibernateDAO;
import eu.apenet.persistence.hibernate.UserRoleHibernateDAO;
import eu.apenet.persistence.hibernate.WarningsHibernateDAO;
import eu.archivesportaleurope.persistence.jpa.dao.ArchivalInstitutionOaiPmhJpaDAO;
import eu.archivesportaleurope.persistence.jpa.dao.EadSavedSearchJpaDAO;
import eu.archivesportaleurope.persistence.jpa.dao.HgSgFaRelationJpaDAO;
import eu.archivesportaleurope.persistence.jpa.dao.IngestionprofileJpaDAO;
import eu.archivesportaleurope.persistence.jpa.dao.UpFileJpaDAO;


/**
 * Implementation: If you write a new DAO, this class has to know about it.
 * @author Paul
 */


public class HibernateDAOFactory extends DAOFactory {
    
   
    @Override
    public SentMailRegisterDAO getSentMailRegisterDAO() {
        return (SentMailRegisterDAO) instantiateDAO(SentMailRegisterHibernateDAO.class);
    }
    
    
	@Override
	public FindingAidDAO getFindingAidDAO() {
		return (FindingAidDAO) instantiateDAO(FindingAidHibernateDAO.class);
	}

	@Override
	public UpFileDAO getUpFileDAO() {
		return (UpFileDAO) instantiateDAO(UpFileJpaDAO.class);
	}
 
	@Override
	public HoldingsGuideDAO getHoldingsGuideDAO() {
		return (HoldingsGuideDAO) instantiateDAO(HoldingsGuideHibernateDAO.class);
	}
	
	@Override
	public WarningsDAO getWarningsDAO() {
		return (WarningsDAO) instantiateDAO(WarningsHibernateDAO.class);
	}

	@Override
	public ArchivalInstitutionDAO getArchivalInstitutionDAO() {
		return (ArchivalInstitutionDAO) instantiateDAO(ArchivalInstitutionHibernateDAO.class);
	}

	@Override
	public UserDAO getUserDAO() {
		return (UserDAO) instantiateDAO(UserHibernateDAO.class);
	}

	@Override
	public CountryDAO getCountryDAO() {
		return (CountryDAO) instantiateDAO(CountryHibernateDAO.class);
	}
	@Override
	public LangDAO getLangDAO() {
		return (LangDAO) instantiateDAO(LangHibernateDAO.class);
	}

	@Override
	public EseDAO getEseDAO() {
		return (EseDAO) instantiateDAO(EseHibernateDAO.class);
	}



	@Override
	public UploadMethodDAO getUploadMethodDAO() {
		return (UploadMethodDAO) instantiateDAO(UploadMethodHibernateDAO.class);
	}

	@Override
	public EseStateDAO getEseStateDAO() {
		return (EseStateDAO) instantiateDAO(EseStateHibernateDAO.class);
	}

	@Override
	public CLevelDAO getCLevelDAO() {
		return (CLevelDAO) instantiateDAO(CLevelHibernateDAO.class);
	}

	@Override
	public EadContentDAO getEadContentDAO() {
		return (EadContentDAO) instantiateDAO(EadContentHibernateDAO.class);
	}

	@Override
	public AiAlternativeNameDAO getAiAlternativeNameDAO(){
		return (AiAlternativeNameDAO) instantiateDAO(AiAlternativeNameHibernateDAO.class);
	}
	
	@Override
	public CouAlternativeNameDAO getCouAlternativeNameDAO(){
		return (CouAlternativeNameDAO) instantiateDAO(CouAlternativeNameHibernateDAO.class);
	}


    @Override
    public DptUpdateDAO getDptUpdateDAO(){
        return (DptUpdateDAO) instantiateDAO(DptUpdateHibernateDAO.class);
    }

	
	@Override 
	public ResumptionTokenDAO getResumptionTokenDAO(){
		return (ResumptionTokenDAO) instantiateDAO(ResumptionTokenHibernateDAO.class);
	}
	
	@Override
	public QueueItemDAO getQueueItemDAO() {		
		return (QueueItemDAO) instantiateDAO(QueueItemHibernateDAO.class) ;
	}
	
	@Override
	public UserRoleDAO getUserRoleDAO() {
		return (UserRoleDAO) instantiateDAO(UserRoleHibernateDAO.class);
	}

    @Override
	public EadDAO getEadDAO() {
		return (EadDAO) instantiateDAO(EadHibernateDAO.class);
	}


	@Override
	public HgSgFaRelationDAO getHgSgFaRelationDAO() {
		return (HgSgFaRelationDAO) instantiateDAO(HgSgFaRelationJpaDAO.class);
	}


    @Override
    public ArchivalInstitutionOaiPmhDAO getArchivalInstitutionOaiPmhDAO() {
        return (ArchivalInstitutionOaiPmhDAO) instantiateDAO(ArchivalInstitutionOaiPmhJpaDAO.class);
    }
    
    @Override
    public IngestionprofileDAO getIngestionprofileDAO() {
        return (IngestionprofileDAO) instantiateDAO(IngestionprofileJpaDAO.class);
    }

    @Override
    public CoordinatesDAO getCoordinatesDAO() {
        return (CoordinatesDAO) instantiateDAO(CoordinatesHibernateDAO.class);
    }
    
    @Override
    public EadSavedSearchDAO getEadSavedSearchDAO() {
        return (EadSavedSearchDAO) instantiateDAO(EadSavedSearchJpaDAO.class);
    }
    
    @Override
	public EacCpfDAO getEacCpfDAO() {
		return (EacCpfDAO) instantiateDAO(EacCpfHibernateDAO.class);
	}
}
