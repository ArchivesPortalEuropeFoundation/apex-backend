package eu.apenet.dashboard.services.ead;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;

public class LinkingService {

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

}
