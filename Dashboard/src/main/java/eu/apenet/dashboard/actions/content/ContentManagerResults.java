package eu.apenet.dashboard.actions.content;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.vo.Ead;

public class ContentManagerResults {
	
	private List<EadResult> eadResults = new ArrayList<EadResult>();
	private long totalNumberOfResults;
	private EadSearchOptions eadSearchOptions;
	public List<EadResult> getEadResults() {
		return eadResults;
	}

	public void setEadResults(List<EadResult> eadResults) {
		this.eadResults = eadResults;
	}
	public void setEads(List<Ead> eads) {
		for (Ead ead: eads){
			this.eadResults.add(new EadResult(ead));
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


	
}
