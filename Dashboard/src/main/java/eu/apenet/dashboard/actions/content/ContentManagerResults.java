package eu.apenet.dashboard.actions.content;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.vo.Ead;

public class ContentManagerResults {
	
	private List<EadResult> eadResults = new ArrayList<EadResult>();
	private Long totalNumberOfResults = 0l;
	private Long totalConvertedFiles = 0l;
	private Long totalValidatedFiles = 0l;
	private Long totalPublishedUnits = 0l;
	private Long totalChos = 0l;
	private Long totalChosDeliveredToEuropeana = 0l;
	private boolean hasDynamicHg = false;
	private boolean hasDynamicSg = false;
	private EadSearchOptions eadSearchOptions;
	private XmlType xmlType;
	
	public ContentManagerResults (EadSearchOptions eadSearchOptions){
		this.eadSearchOptions = eadSearchOptions;
		xmlType = XmlType.getType(eadSearchOptions.getEadClazz());
	}
	public List<EadResult> getEadResults() {
		return eadResults;
	}

	public void setEadResults(List<EadResult> eadResults) {
		this.eadResults = eadResults;
	}
	public void setEads(List<Ead> eads) {
		for (Ead ead: eads){
			if (XmlType.EAD_FA.equals(xmlType)){
				this.eadResults.add(new FindingAidResult(ead));
			}else if (XmlType.EAD_HG.equals(xmlType)){
				this.eadResults.add(new HoldingsGuideSourceGuideResult(ead));
			}else {
				this.eadResults.add(new HoldingsGuideSourceGuideResult(ead));
			}
			
		}
	}

	public EadSearchOptions getEadSearchOptions() {
		return eadSearchOptions;
	}

	public void setEadSearchOptions(EadSearchOptions eadSearchOptions) {
		this.eadSearchOptions = eadSearchOptions;
	}

	public long getTotalNumberOfResults() {
		return totalNumberOfResults;
	}

	public void setTotalNumberOfResults(long totalNumberOfResults) {
		this.totalNumberOfResults = totalNumberOfResults;
	}
	public int getXmlTypeId(){
		return xmlType.getIdentifier();
	}
	public boolean isFindingAid(){
		return XmlType.EAD_FA.equals(xmlType);
	}
	public boolean isHoldingsGuide(){
		return XmlType.EAD_HG.equals(xmlType);
	}
	public boolean isSourceGuide(){
		return XmlType.EAD_SG.equals(xmlType);
	}
	public Long getTotalConvertedFiles() {
		return totalConvertedFiles;
	}
	public void setTotalConvertedFiles(Long totalConvertedFiles) {
		this.totalConvertedFiles = totalConvertedFiles;
	}
	public Long getTotalValidatedFiles() {
		return totalValidatedFiles;
	}
	public void setTotalValidatedFiles(Long totalValidatedFiles) {
		this.totalValidatedFiles = totalValidatedFiles;
	}
	public Long getTotalPublishedUnits() {
		return totalPublishedUnits;
	}
	public void setTotalPublishedUnits(Long totalPublishedUnits) {
		this.totalPublishedUnits = totalPublishedUnits;
	}

	public Long getTotalChos() {
		return totalChos;
	}
	public void setTotalChos(Long totalChos) {
		this.totalChos = totalChos;
	}

	public Long getTotalChosDeliveredToEuropeana() {
		return totalChosDeliveredToEuropeana;
	}
	public void setTotalChosDeliveredToEuropeana(Long totalChosDeliveredToEuropeana) {
		this.totalChosDeliveredToEuropeana = totalChosDeliveredToEuropeana;
	}
	public XmlType getXmlType() {
		return xmlType;
	}
	public void setXmlType(XmlType xmlType) {
		this.xmlType = xmlType;
	}
	public void setTotalNumberOfResults(Long totalNumberOfResults) {
		this.totalNumberOfResults = totalNumberOfResults;
	}
	public boolean isHasDynamicHg() {
		return hasDynamicHg;
	}
	public void setHasDynamicHg(boolean hasDynamicHg) {
		this.hasDynamicHg = hasDynamicHg;
	}
	public boolean isHasDynamicSg() {
		return hasDynamicSg;
	}
	public void setHasDynamicSg(boolean hasDynamicSg) {
		this.hasDynamicSg = hasDynamicSg;
	}
	public boolean isHasDynamicHgSg() {
		return hasDynamicSg || hasDynamicHg;
	}
}
