package eu.apenet.persistence.factory;

import eu.apenet.persistence.dao.*;
import eu.apenet.persistence.hibernate.*;


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
		return (UpFileDAO) instantiateDAO(UpFileHibernateDAO.class);
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
	public ArchivalInstitutionOaiPmhDAO getArchivalInstitutionOaiPmhDAO() {
		return (ArchivalInstitutionOaiPmhDAO) instantiateDAO(ArchivalInstitutionOaiPmhHibernateDAO.class);
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
	public CpfContentDAO getCpfContentDAO(){
		return (CpfContentDAO) instantiateDAO(CpfContentHibernateDAO.class);
	}

    @Override
    public DptUpdateDAO getDptUpdateDAO(){
        return (DptUpdateDAO) instantiateDAO(DptUpdateHibernateDAO.class);
    }
	
	@Override
	public MetadataFormatDAO getMetadataFormatDAO(){
		return (MetadataFormatDAO) instantiateDAO(MetadataFormatHibernateDAO.class);
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

}
