package eu.apenet.dashboard.actions.content;

import java.text.SimpleDateFormat;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.QueueAction;
import eu.apenet.persistence.vo.ValidatedState;

public class EadResult {
	protected static final String CONTENT_MESSAGE_NO = "content.message.no";
	protected static final String CONTENT_MESSAGE_YES = "content.message.yes";
	protected static final String CONTENT_MESSAGE_QUEUE = "content.message.queue";
	protected static final String CONTENT_MESSAGE_ERROR = "content.message.fatalerror";
	
	protected static final String STATUS_ERROR = "status_error";
	protected static final String STATUS_NO = "status_no";
	protected static final String STATUS_OK = "status_ok";
	protected static final String STATUS_QUEUE = "status_queue";
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
	protected boolean readyForQueueProcessing;
	protected boolean queueProcessing;
	private long units;
	protected QueueAction queueAction;
	public EadResult(Ead ead){
		this.eadid = ead.getEadid();
		this.id = ead.getId();
        if(ead.getTitle().length() > MAX_TITLE)
        	this.title =  ead.getTitle().substring(0, MAX_TITLE) + "...";
		else
			this.title =  ead.getTitle();
        this.date = FORMATTER.format(ead.getUploadDate());
        this.converted = ead.isConverted();
        this.searchable = ead.isPublished();
        this.validated = ValidatedState.VALIDATED.equals(ead.getValidated());
        this.validatedFatalError = ValidatedState.FATAL_ERROR.equals(ead.getValidated());
        this.units = ead.getTotalNumberOfUnits();
        this.readyForQueueProcessing = QueuingState.READY.equals(ead.getQueuing());
        this.queueProcessing = QueuingState.BUSY.equals(ead.getQueuing());
        if ((readyForQueueProcessing || queueProcessing) && ead.getQueueItem() != null){
        	queueAction = ead.getQueueItem().getAction();
        }
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
	public String getCssClass(){
		if (queueProcessing){
			return "status_queue_processing";
		}else {
			return "";
		}
	}
	public String getConvertedCssClass(){
		if (converted){
			return STATUS_OK;
		}else {
		
			return STATUS_NO;
		}
	}

	public String getValidatedCssClass(){
		if (validated){
			return STATUS_OK;
		}else if (validatedFatalError){
			return STATUS_ERROR;
		}else {
			return STATUS_NO;
		}
	}
	public String getIndexedCssClass(){
		if (searchable){
			return STATUS_OK;
		}else {
			return STATUS_NO;
		}
	}
	public String getConvertedText(){
		if (converted){
			return CONTENT_MESSAGE_YES;
		}else {
			return CONTENT_MESSAGE_NO;
		}
	}
	public String getValidatedText(){
		if (validated){
			return CONTENT_MESSAGE_YES;
		}else if (validatedFatalError){
			return CONTENT_MESSAGE_ERROR;
		}else {
			return CONTENT_MESSAGE_NO;
		}
	}
	public String getIndexedText(){
		if (searchable){
			return getUnits()+"";
		}else {
			return CONTENT_MESSAGE_NO;
		}
	}
	public QueueAction getQueueAction() {
		return queueAction;
	}
	public String getActionsCssClass(){
		if (readyForQueueProcessing){
			return STATUS_QUEUE;
		}else {
			return "";
		}
	}

}
