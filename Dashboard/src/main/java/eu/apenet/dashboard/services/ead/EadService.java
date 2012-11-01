package eu.apenet.dashboard.services.ead;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;

public class EadService {

	public static void validate(XmlType xmlType, Integer id) throws APEnetException{
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		new ValidateTask().execute(ead);
        eadDAO.update(ead);
		
	}
	public static void convert(XmlType xmlType, Integer id) throws APEnetException{
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
        Ead ead = eadDAO.findById(id, xmlType.getClazz());
		SecurityContext.get().checkAuthorized(ead);
		new ConvertTask().execute(ead);
        eadDAO.update(ead);
		
	}
}
