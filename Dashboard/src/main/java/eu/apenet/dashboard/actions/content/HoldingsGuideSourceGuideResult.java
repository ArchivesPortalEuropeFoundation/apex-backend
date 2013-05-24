package eu.apenet.dashboard.actions.content;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;

public class HoldingsGuideSourceGuideResult extends EadResult {

	private long possibleFindingAidsLinked = 0;
	private long findingAidsLinked = 0;
	private long findingAidsLinkedAndPublished = 0;
	public HoldingsGuideSourceGuideResult(Ead ead) {
		super(ead);
		if (this.isPublished()){
			Class<? extends Ead> clazz = XmlType.getEadType(ead).getClazz();
			CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
			
			possibleFindingAidsLinked = clevelDAO.countPossibleLinkedCLevels(ead.getId(), clazz);
			findingAidsLinkedAndPublished = clevelDAO.countLinkedCLevels(ead.getId(), clazz, true);
			findingAidsLinked = clevelDAO.countLinkedCLevels(ead.getId(), clazz, null);            		
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
}
