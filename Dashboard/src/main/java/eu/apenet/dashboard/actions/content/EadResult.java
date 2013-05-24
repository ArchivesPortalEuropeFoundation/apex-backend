package eu.apenet.dashboard.actions.content;

import java.text.SimpleDateFormat;

import eu.apenet.persistence.vo.*;

public class EadResult {
	protected static final String CONTENT_MESSAGE_NO = "content.message.no";
	protected static final String CONTENT_MESSAGE_YES = "content.message.yes";
	protected static final String CONTENT_MESSAGE_QUEUE = "content.message.queue";
	protected static final String CONTENT_MESSAGE_FATAL_ERROR = "content.message.fatalerror";
	protected static final String CONTENT_MESSAGE_ERROR = "content.message.errorsmall";

	protected static final String STATUS_ERROR = "status_error";
	protected static final String STATUS_WARNING = "status_warning";
	protected static final String STATUS_NO = "status_no";
	protected static final String STATUS_NOT_AVAILABLE = "status_not_available";
	protected static final String STATUS_OK = "status_ok";
	protected static final String STATUS_QUEUE = "status_queue";
	protected static final String STATUS_QUEUE_ERROR = "status_queue_error";
	private static final int MAX_TITLE = 120;
	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	private Integer id;
	private String title;
	private String eadid;
	private String date;
	private boolean converted;
	private boolean published;
	private boolean validated;
	private boolean validatedFatalError;
	private boolean queueReady;
	private boolean queueError;
	private boolean queueProcessing;
	private Long units;
	private boolean containWarnings;
	private boolean containValidationErrors;
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
        this.published = ead.isPublished();
        this.validated = ValidatedState.VALIDATED.equals(ead.getValidated());
        this.validatedFatalError = ValidatedState.FATAL_ERROR.equals(ead.getValidated());
        this.units = ead.getTotalNumberOfUnits();
        this.queueReady = QueuingState.READY.equals(ead.getQueuing());
        this.queueError = QueuingState.ERROR.equals(ead.getQueuing());
        this.queueProcessing = QueuingState.BUSY.equals(ead.getQueuing());
        if ((!QueuingState.NO.equals(ead.getQueuing()) && ead.getQueueItem() != null)){
        	queueAction = ead.getQueueItem().getAction();
        }
        this.containWarnings = ead.getWarningses().size() > 0;
        this.containValidationErrors = containValidationErrors(ead);
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

	public boolean isPublished() {
		return published;
	}
	public boolean isValidated() {
		return validated;
	}
	public boolean isValidatedFatalError() {
		return validatedFatalError;
	}
	public boolean isEditable(){
		return validated & !published;
	}
	public Long getUnits() {
		return units;
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
		if (published){
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
		} else if(!converted && containValidationErrors) {
            return CONTENT_MESSAGE_ERROR;
        } else if (validatedFatalError) {
            return CONTENT_MESSAGE_FATAL_ERROR;
        } else {
			return CONTENT_MESSAGE_NO;
		}
	}
	public String getIndexedText(){
		if (published){
			return getUnits()+"";
		}else {
			return CONTENT_MESSAGE_NO;
		}
	}
	public QueueAction getQueueAction() {
		return queueAction;
	}
	public String getQueueCssClass(){
		if (queueReady){
			return STATUS_QUEUE;
		}
		if (queueError){
			return STATUS_QUEUE_ERROR;
		}else {
			return "";
		}
	}
	
	public String getQueueText(){
		if (isQueueReady() ||isQueueProcessing()){
			return queueAction.getResourceName();
		}else if (isQueueError()){
			return CONTENT_MESSAGE_FATAL_ERROR;
		}else {
			return null;
		}
	}
	public boolean isQueueReady() {
		return queueReady;
	}
	public boolean isQueueError() {
		return queueError;
	}

    public boolean isContainWarnings() {
        return containWarnings;
    }

	public boolean containValidationErrors(Ead ead) {
        for(Warnings warnings : ead.getWarningses()) {
            if(!warnings.getIswarning())
                return true;
        }
		return false;
	}
	
}
