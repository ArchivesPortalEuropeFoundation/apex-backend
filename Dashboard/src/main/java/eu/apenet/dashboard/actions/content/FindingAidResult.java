package eu.apenet.dashboard.actions.content;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;

public class FindingAidResult extends EadResult {

	private boolean convertedToEseEdm;
	private boolean deliveredToEuropeana;
	private boolean harvestedByEuropeana;
	private long totalNumberOfDaos;
	public FindingAidResult(Ead ead) {
		super(ead);
		FindingAid findingAid = (FindingAid) ead;
        this.convertedToEseEdm = EuropeanaState.CONVERTED.equals(findingAid.getEuropeana());
        this.deliveredToEuropeana = EuropeanaState.DELIVERED.equals(findingAid.getEuropeana());
        this.harvestedByEuropeana = EuropeanaState.HARVESTED.equals(findingAid.getEuropeana());
        this.totalNumberOfDaos = findingAid.getTotalNumberOfDaos();
	}

	public boolean isConvertedToEseEdm() {
		return convertedToEseEdm;
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
	public String getEseEdmText(){
		if (convertedToEseEdm){
			return getTotalNumberOfDaos()+"";
		}else {
			return CONTENT_MESSAGE_NO;
		}
	}
	public String getEseEdmCssClass(){
		if (convertedToEseEdm || deliveredToEuropeana || harvestedByEuropeana){
			return STATUS_OK;
		}else {
			return STATUS_NO;
		}
	}
	public String getEuropeanaCssClass(){
		if (deliveredToEuropeana || harvestedByEuropeana){
			return STATUS_OK;
		}else {
			return STATUS_NO;
		}
	}
	public String getEuropeanaText(){
		if (deliveredToEuropeana){
			return "content.message.europeana.delivered";
		}else if (harvestedByEuropeana){
			return "content.message.europeana.harvested";
		}else {
			return CONTENT_MESSAGE_NO;
		}
	}
}
