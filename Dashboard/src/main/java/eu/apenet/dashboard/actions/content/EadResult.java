package eu.apenet.dashboard.actions.content;

import java.text.SimpleDateFormat;

import eu.apenet.persistence.vo.Ead;

public class EadResult {
	private static final int MAX_TITLE = 120;
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	private Integer id;
	private String title;
	private String eadid;
	private String date;
	public EadResult(Ead ead){
		this.eadid = ead.getEadid();
		this.id = ead.getId();
        if(ead.getTitle().length() > MAX_TITLE)
        	this.title =  ead.getTitle().substring(0, MAX_TITLE) + "...";
		else
			this.title =  ead.getTitle();
        this.date = FORMATTER.format(ead.getUploadDate());
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
	public String getDate() {
		return date;
	}


	
}
