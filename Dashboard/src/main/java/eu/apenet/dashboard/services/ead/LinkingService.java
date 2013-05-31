package eu.apenet.dashboard.services.ead;

import java.util.List;

import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class LinkingService {
	protected static final Logger LOGGER = Logger.getLogger(LinkingService.class);
	public static boolean linkWithoutCommit(Ead hgOrSg, CLevel clevel){
		if (hgOrSg instanceof HoldingsGuide || hgOrSg instanceof SourceGuide){
			Ead linkedFindingAid = DAOFactory.instance().getEadDAO().getEadByEadid(FindingAid.class, hgOrSg.getAiId(), clevel.getHrefEadid());
			if (linkedFindingAid != null){
				HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
				hgSgFaRelation.setFaId(linkedFindingAid.getId());
				hgSgFaRelation.setAiId(hgOrSg.getAiId());
				hgSgFaRelation.setHgSgClevelId(clevel.getClId());
				if(hgOrSg instanceof HoldingsGuide){
					hgSgFaRelation.setHgId(hgOrSg.getId());
				}else if(hgOrSg instanceof SourceGuide){
					hgSgFaRelation.setSgId(hgOrSg.getId());
				}
				HibernateUtil.getDatabaseSession().save(hgSgFaRelation);
				return true;
			}
		}
		return false;
	}
	public static boolean linkWithHgOrSg(Ead ead){
		if (ead instanceof FindingAid){
			try {
				FindingAid findingAid = (FindingAid) ead;
				CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
				List<CLevel> clevels = clevelDAO.getClevelsFromSgOrHg(findingAid.getAiId(), findingAid.getEadid());
				JpaUtil.beginDatabaseTransaction();
				for (CLevel clevel : clevels){
					HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
					hgSgFaRelation.setFaId(findingAid.getId());
					hgSgFaRelation.setAiId(findingAid.getAiId());
					hgSgFaRelation.setHgSgClevelId(clevel.getClId());
					EadContent eadContent = clevel.getEadContent();
					hgSgFaRelation.setHgId(eadContent.getHgId());
					hgSgFaRelation.setSgId(eadContent.getSgId());
					JpaUtil.getEntityManager().persist(hgSgFaRelation);
				}
				JpaUtil.commitDatabaseTransaction();
			}catch (Exception e){
				LOGGER.error("Could not link " + ead.getEadid() + " to HG or SG");
				JpaUtil.rollbackDatabaseTransaction();
			}
			return true;
		}
		return false;
	}
}
