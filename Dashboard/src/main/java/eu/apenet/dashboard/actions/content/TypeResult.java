/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.apenet.dashboard.actions.content;

import eu.apenet.persistence.vo.QueueAction;
import java.text.SimpleDateFormat;

/**
 *
 * @author papp
 */
public class TypeResult {
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
    protected static final int MAX_TITLE = 120;
    protected static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    protected Integer id;
    protected String title;
    protected String date;
    protected boolean converted;
    protected boolean published;
    protected boolean validated;
    protected boolean validatedFatalError;
    protected boolean queueReady;
    protected boolean queueError;
    protected boolean queueProcessing;
    protected boolean containWarnings;
    protected boolean containValidationErrors;
    protected QueueAction queueAction;

    public TypeResult() {
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public boolean isEditable() {
        return false;
    }

    public boolean isQueueProcessing() {
        return queueProcessing;
    }

    public String getCssClass() {
        if (queueProcessing) {
            return "status_queue_processing";
        } else {
            return "";
        }
    }

    public String getConvertedCssClass() {
        if (converted) {
            return STATUS_OK;
        } else {
            return STATUS_NO;
        }
    }

    public String getValidatedCssClass() {
        if (validated) {
            return STATUS_OK;
        } else if (validatedFatalError) {
            return STATUS_ERROR;
        } else {
            return STATUS_NO;
        }
    }

    public String getIndexedCssClass() {
        if (published) {
            return STATUS_OK;
        } else {
            return STATUS_NO;
        }
    }

    public String getConvertedText() {
        if (converted) {
            return CONTENT_MESSAGE_YES;
        } else {
            return CONTENT_MESSAGE_NO;
        }
    }

    public String getValidatedText() {
        if (validated) {
            return CONTENT_MESSAGE_YES;
        } else if (!converted && containValidationErrors) {
            return CONTENT_MESSAGE_ERROR;
        } else if (validatedFatalError) {
            return CONTENT_MESSAGE_FATAL_ERROR;
        } else {
            return CONTENT_MESSAGE_NO;
        }
    }

    public QueueAction getQueueAction() {
        return queueAction;
    }

    public String getQueueCssClass() {
        if (queueReady) {
            return STATUS_QUEUE;
        }
        if (queueError) {
            return STATUS_QUEUE_ERROR;
        } else {
            return "";
        }
    }

    public String getQueueText() {
        if (isQueueReady() || isQueueProcessing()) {
            return queueAction.getResourceName();
        } else if (isQueueError()) {
            return CONTENT_MESSAGE_FATAL_ERROR;
        } else {
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
}
