package eu.apenet.dashboard.actions.content;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.vo.Ead;

public class ContentManagerResults {
	
	private List<EadResult> eadResults = new ArrayList<EadResult>();
	private long totalNumberOfResults;
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
}
