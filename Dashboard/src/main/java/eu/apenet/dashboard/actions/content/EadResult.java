package eu.apenet.dashboard.actions.content;

import java.text.SimpleDateFormat;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.QueingState;
import eu.apenet.persistence.vo.ValidatedState;

public class EadResult {
	private static final int MAX_TITLE = 120;
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	private Integer id;
	private String title;
	private String eadid;
	private String date;
	private boolean converted;
	private boolean searchable;
	private boolean validated;
	private boolean validatedFatalError;
	private boolean readyForQueueProcessing;
	private boolean queueProcessing;
	private long units;
	public EadResult(Ead ead){
		this.eadid = ead.getEadid();
		this.id = ead.getId();
        if(ead.getTitle().length() > MAX_TITLE)
        	this.title =  ead.getTitle().substring(0, MAX_TITLE) + "...";
		else
			this.title =  ead.getTitle();
        this.date = FORMATTER.format(ead.getUploadDate());
        this.converted = ead.isConverted();
        this.searchable = ead.isSearchable();
        this.validated = ValidatedState.VALIDATED.equals(ead.getValidated());
        this.validatedFatalError = ValidatedState.FATAL_ERROR.equals(ead.getValidated());
        this.units = ead.getTotalNumberOfUnits();
        this.readyForQueueProcessing = QueingState.READY.equals(ead.getQueuing());
        this.queueProcessing = QueingState.BUSY.equals(ead.getQueuing());
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
	public boolean isConverted() {
		return converted;
	}
	public boolean isSearchable() {
		return searchable;
	}
	public boolean isValidated() {
		return validated;
	}
	public boolean isValidatedFatalError() {
		return validatedFatalError;
	}
	public long getUnits() {
		return units;
	}
	public boolean isReadyForQueueProcessing() {
		return readyForQueueProcessing;
	}
	public boolean isQueueProcessing() {
		return queueProcessing;
	}


	
}
