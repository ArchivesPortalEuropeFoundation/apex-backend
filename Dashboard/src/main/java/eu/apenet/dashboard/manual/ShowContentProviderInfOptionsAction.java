package eu.apenet.dashboard.manual;

import java.io.File;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class ShowContentProviderInfOptionsAction extends AbstractInstitutionAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3716989273471606868L;

	private final Logger log = Logger.getLogger(getClass());

	
	
	//Methods
	@Override
    public String execute() throws Exception {		
			return SUCCESS;			
    }
	
	public String download() {

		try {
			ArchivalInstitutionDAO archivalInstitutionDao = DAOFactory.instance().getArchivalInstitutionDAO();
			ArchivalInstitution archivalInstitution = archivalInstitutionDao.findById(this.getAiId());
			File file = new File(APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + archivalInstitution.getEagPath());
			ContentUtils.downloadXml(getServletRequest(), getServletResponse(), file);
		} catch (Exception e){
			log.error(e.getMessage());
		}
	
		return null;
	}
}