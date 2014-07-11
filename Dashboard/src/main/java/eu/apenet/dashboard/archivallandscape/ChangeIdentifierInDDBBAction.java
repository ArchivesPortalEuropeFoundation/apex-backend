package eu.apenet.dashboard.archivallandscape;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Action;

import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * Changing the identifiers of the archival landscape items that don't begin with letter
 * in DDBB archival_institution entity
 * 
 */
public class ChangeIdentifierInDDBBAction extends AbstractInstitutionAction{
	
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(getClass());
	
	public String execute() throws Exception {
			String result = Action.SUCCESS;
			try{
				JpaUtil.beginDatabaseTransaction();
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
				List<ArchivalInstitution> ai = new ArrayList<ArchivalInstitution>();	
				ai=aiDao.findAll();
				for(int i=0;i<ai.size();i++){
					String identifier=ai.get(i).getInternalAlId();
					if (identifier!=null && !ArchivalLandscapeUtils.isValidIdentifier(identifier)){
						if (identifier.length()>0 && identifier.length()<99){
							identifier="A"+identifier;
						}else{
							identifier = generateNewRandomIdentifier();
						}
						ai.get(i).setInternalAlId(identifier);
						aiDao.updateSimple(ai.get(i));
					}	
				}
				JpaUtil.commitDatabaseTransaction();
			} catch (Exception e) {
				result = ERROR;
				// Rollback current database transaction.
				JpaUtil.rollbackDatabaseTransaction();
				log.error("Error trying to update the AL identifiers");
				log.error(e.getCause());
			}
			
		return result;
	}
		private static String generateNewRandomIdentifier() {
			return "A"+System.currentTimeMillis()+"-"+(new Float(+Math.random()*1000000).toString());
		}
}
	
	
