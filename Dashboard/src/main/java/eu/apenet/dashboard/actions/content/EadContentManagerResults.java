package eu.apenet.dashboard.actions.content;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.commons.types.XmlType;
import eu.apenet.persistence.dao.ContentSearchOptions;
import eu.apenet.persistence.vo.Ead;

public class EadContentManagerResults extends ContentManagerResults {

	private List<EadResult> eadResults = new ArrayList<EadResult>();
	private boolean hasDynamicHg = false;
	private boolean hasDynamicSg = false;

	public EadContentManagerResults (ContentSearchOptions eadSearchOptions){
		super(eadSearchOptions);
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
	public boolean isFindingAid(){
		return XmlType.EAD_FA.equals(xmlType);
	}
	public boolean isHoldingsGuide(){
		return XmlType.EAD_HG.equals(xmlType);
	}
	public boolean isSourceGuide(){
		return XmlType.EAD_SG.equals(xmlType);
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
