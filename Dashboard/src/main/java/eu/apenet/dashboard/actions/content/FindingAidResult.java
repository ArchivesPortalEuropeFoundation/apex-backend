package eu.apenet.dashboard.actions.content;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;

public class FindingAidResult extends EadResult {

	private boolean convertedToEuropeana;
	private boolean deliveredToEuropeana;
	private boolean harvestedByEuropeana;
	private long totalNumberOfDaos;
	public FindingAidResult(Ead ead) {
		super(ead);
		FindingAid findingAid = (FindingAid) ead;
        this.convertedToEuropeana = EuropeanaState.CONVERTED.equals(findingAid.getEuropeana());
        this.deliveredToEuropeana = EuropeanaState.DELIVERED.equals(findingAid.getEuropeana());
        this.harvestedByEuropeana = EuropeanaState.HARVESTED.equals(findingAid.getEuropeana());
        this.totalNumberOfDaos = findingAid.getTotalNumberOfDaos();
	}
	public boolean isConvertedToEuropeana() {
		return convertedToEuropeana;
	}
	public boolean isDeliveredToEuropeana() {
		return deliveredToEuropeana;
	}
	public boolean isHarvestedByEuropeana() {
		return harvestedByEuropeana;
	}
	public long getTotalNumberOfDaos() {
		return totalNumberOfDaos;
	}

}
