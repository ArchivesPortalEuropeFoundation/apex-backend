package eu.apenet.oaiserver.config.other.dao;

/**
 * Implementation: If you write a new DAO, this class has to know about it.
 * @author Paul
 */


public class HibernateDAOFactory extends DAOFactory {

    @Override
    public EadObjectDAO getEadObjectDAO() {
        return (EadObjectDAO) instantiateDAO(EadObjectDAO.class);
    }

    @Override
    public ResumptionTokenDAO getResumptionTokenDAO() {
        return (ResumptionTokenDAO) instantiateDAO(ResumptionTokenDAO.class);
    }
}
