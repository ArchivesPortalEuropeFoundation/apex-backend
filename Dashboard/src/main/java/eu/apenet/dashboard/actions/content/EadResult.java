package eu.apenet.dashboard.actions.content;

import eu.apenet.persistence.vo.Ead;

public class EadResult {
	private static final int MAX_TITLE = 120;

	private Integer id;
	private String title;
	private String eadid;
	public EadResult(Ead ead){
		this.eadid = ead.getEadid();
		this.id = ead.getId();
        if(ead.getTitle().length() > MAX_TITLE)
        	this.title =  ead.getTitle().substring(0, MAX_TITLE) + "...";
		else
			this.title =  ead.getTitle();
	}
	public Integer getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public String getEadid() {
		return eadid;
	}


	
}
