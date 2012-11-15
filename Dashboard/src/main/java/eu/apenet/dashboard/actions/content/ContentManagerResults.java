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
	private Long totalDaosConvertedToEseEdm = 0l;
	private Long totalDaosDeliveredToEuropea = 0l;
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
				this.eadResults.add(new HoldingsGuideResult(ead));
			}else {
				this.eadResults.add(new EadResult(ead));
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
	public Long getTotalDaosConvertedToEseEdm() {
		return totalDaosConvertedToEseEdm;
	}
	public void setTotalDaosConvertedToEseEdm(Long totalDaosConvertedToEseEdm) {
		this.totalDaosConvertedToEseEdm = totalDaosConvertedToEseEdm;
	}
	public Long getTotalDaosDeliveredToEuropea() {
		return totalDaosDeliveredToEuropea;
	}
	public void setTotalDaosDeliveredToEuropea(Long totalDaosDeliveredToEuropea) {
		this.totalDaosDeliveredToEuropea = totalDaosDeliveredToEuropea;
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
	
}
