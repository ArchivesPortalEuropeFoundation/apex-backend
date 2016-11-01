package eu.apenet.dashboard.actions.content.ead;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AbstractContent;
import eu.apenet.persistence.vo.Ead;

public class HoldingsGuideSourceGuideResult extends EadResult {

	private long possibleFindingAidsLinked = 0;
	private long findingAidsLinked = 0;
	private long findingAidsLinkedAndPublished = 0;
	public HoldingsGuideSourceGuideResult(Ead ead) {
		super(ead);
		if (this.isPublished() || this.isDynamic()){
			Class<? extends AbstractContent> clazz = XmlType.getContentType(ead).getEadClazz();
			CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
			HgSgFaRelationDAO hgSgFaRelationDAO = DAOFactory.instance().getHgSgFaRelationDAO();
			possibleFindingAidsLinked = clevelDAO.countPossibleLinkedCLevels(ead.getId(), clazz);
			findingAidsLinkedAndPublished = hgSgFaRelationDAO.countHgSgFaRelations(ead.getId(), clazz, true);
			findingAidsLinked = hgSgFaRelationDAO.countHgSgFaRelations(ead.getId(), clazz, null);
		}
  	}
	public long getPossibleFindingAidsLinked() {
		return possibleFindingAidsLinked;
	}
	public long getFindingAidsLinked() {
		return findingAidsLinked;
	}
	public long getFindingAidsLinkedAndPublished() {
		return findingAidsLinkedAndPublished;
	}

	public String getLinkedCssClass(){
		if (possibleFindingAidsLinked == findingAidsLinkedAndPublished){
			return STATUS_OK;
		}else if(possibleFindingAidsLinked == findingAidsLinked && findingAidsLinked > findingAidsLinkedAndPublished ){
			return STATUS_WARNING;
		}else {
			return STATUS_ERROR;
		}
	}
	public String getDynamicCssClass(){
		if (isDynamic()){
			return STATUS_NO;
		}else {
			return STATUS_OK;
		}
	}
}
