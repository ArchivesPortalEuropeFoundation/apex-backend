package eu.apenet.dashboard.actions.content;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.HoldingsGuide;

public class HoldingsGuideResult extends EadResult {

	private long possibleFindingAidsLinked = 0;
	private long findingAidsLinked = 0;
	public HoldingsGuideResult(Ead ead) {
		super(ead);
		HoldingsGuide holdingsGuide = (HoldingsGuide) ead;
		if (this.isPublished()){
			possibleFindingAidsLinked = DAOFactory.instance().getCLevelDAO().countTotalCLevelsByHoldingsGuideId(ead.getId()); //max. number
            findingAidsLinked = DAOFactory.instance().getFindingAidDAO().countFindingAidsByHoldingsGuideId(ead.getId(), true); //currently number
		}
  	}
	public long getPossibleFindingAidsLinked() {
		return possibleFindingAidsLinked;
	}
	public long getFindingAidsLinked() {
		return findingAidsLinked;
	}


}
