package eu.apenet.dashboard.services.ead.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class EadDatabaseSaver {
	private static final int MAX_NUMBER = 200;
	protected Logger LOGGER = Logger.getLogger(EadDatabaseSaver.class);

	private List<CLevel> clevels = new ArrayList<CLevel>();
	private List<HgSgFaRelation>  hgSgFaRelations  = new ArrayList<HgSgFaRelation>();

	public void update(CLevel clevel){
		clevels.add(clevel);
		if (clevels.size() > MAX_NUMBER){
			JpaUtil.beginDatabaseTransaction();
			updateDatabaseClevels();
			JpaUtil.commitDatabaseTransaction();
		}
	}
	public void insert(HgSgFaRelation hgSgFaRelation){
		if (hgSgFaRelation != null){
			hgSgFaRelations.add(hgSgFaRelation);
			if (hgSgFaRelations.size() > MAX_NUMBER){
				JpaUtil.beginDatabaseTransaction();
				updateDatabaseHgSgFaRelations();
				JpaUtil.commitDatabaseTransaction();
			}
		}
	}
	public void updateAll(){
		updateDatabaseClevels();
		updateDatabaseHgSgFaRelations();
	}
	private void updateDatabaseClevels(){
		CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
		for (CLevel clevel: clevels){
			clevelDAO.updateSimple(clevel);
		}
		clevels.clear();
	}
	private void updateDatabaseHgSgFaRelations(){
		HgSgFaRelationDAO hgSgFaRelationDAO = DAOFactory.instance().getHgSgFaRelationDAO();
		for (HgSgFaRelation hgSgFaRelation: hgSgFaRelations){
			hgSgFaRelationDAO.insertSimple(hgSgFaRelation);
		}		
		hgSgFaRelations.clear();
	}
	
}
