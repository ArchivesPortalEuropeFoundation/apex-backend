/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.eaccpf;

import eu.apenet.dashboard.actions.content.TypeResult;
import eu.apenet.persistence.vo.*;

/**
 *
 * @author papp
 */
public class EacCpfResult extends TypeResult {

    private String identifier;
    private long cpfRelations;
    private long resourceRelations;
    private long functionRelations;
    private boolean convertedToEseEdm;
    private boolean deliveredToEuropeana;

    public EacCpfResult(EacCpf eacCpf) {
        this.id = eacCpf.getId();
        if (eacCpf.getTitle() == null || eacCpf.getTitle().isEmpty()){
            this.title = "empty";
        } else if (eacCpf.getTitle().length() > MAX_TITLE) {
            this.title = eacCpf.getTitle().substring(0, MAX_TITLE) + "...";
        } else {
            this.title = eacCpf.getTitle();
        }
        this.date = FORMATTER.format(eacCpf.getUploadDate());
        this.converted = eacCpf.isConverted();
        this.published = eacCpf.isPublished();
        this.validated = ValidatedState.VALIDATED.equals(eacCpf.getValidated());
        this.validatedFatalError = ValidatedState.FATAL_ERROR.equals(eacCpf.getValidated());
        this.convertedToEseEdm = EuropeanaState.CONVERTED.equals(eacCpf.getEuropeana());
        this.deliveredToEuropeana = EuropeanaState.DELIVERED.equals(eacCpf.getEuropeana());
        this.queueReady = QueuingState.READY.equals(eacCpf.getQueuing());
        this.queueError = QueuingState.ERROR.equals(eacCpf.getQueuing());
        this.queueProcessing = QueuingState.BUSY.equals(eacCpf.getQueuing());
        if ((!QueuingState.NO.equals(eacCpf.getQueuing()) && eacCpf.getQueueItem() != null)) {
            queueAction = eacCpf.getQueueItem().getAction();
        }
        this.identifier = eacCpf.getIdentifier();
        this.cpfRelations = eacCpf.getCpfRelations();
        this.resourceRelations = eacCpf.getResourceRelations();
        this.functionRelations = eacCpf.getFunctionRelations();
        this.containWarnings = eacCpf.getWarningses().size() > 0;
        this.containValidationErrors = containValidationErrors(eacCpf);
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIndexedText() {
        if (published) {
            return CONTENT_MESSAGE_YES;
        } else {
            return CONTENT_MESSAGE_NO;
        }
    }

    public String getEseEdmText() {
        if (convertedToEseEdm) {
            return CONTENT_MESSAGE_YES;
        } else {
            return CONTENT_MESSAGE_NO;
        }
    }

    public String getEseEdmCssClass() {
        if (convertedToEseEdm || deliveredToEuropeana) {
            return STATUS_OK;
        } else {
            return STATUS_NO;
        }
    }

    public String getEuropeanaText() {
        if (deliveredToEuropeana) {
            return "content.message.europeana.delivered";
        } else {
            return CONTENT_MESSAGE_NO;
        }
    }

    public String getEuropeanaCssClass() {
        if (deliveredToEuropeana) {
            return STATUS_OK;
        } else {
            return STATUS_NO;
        }
    }

    public long getCpfRelations() {
        return cpfRelations;
    }

    public void setCpfRelations(long cpfRelations) {
        this.cpfRelations = cpfRelations;
    }

    public long getResourceRelations() {
        return resourceRelations;
    }

    public void setResourceRelations(long resourceRelations) {
        this.resourceRelations = resourceRelations;
    }

    public long getFunctionRelations() {
        return functionRelations;
    }

    public void setFunctionRelations(long functionRelations) {
        this.functionRelations = functionRelations;
    }

    public boolean isConvertedToEseEdm() {
        return convertedToEseEdm;
    }

    public void setConvertedToEseEdm(boolean convertedToEseEdm) {
        this.convertedToEseEdm = convertedToEseEdm;
    }

    public boolean isDeliveredToEuropeana() {
        return deliveredToEuropeana;
    }

    public void setDeliveredToEuropeana(boolean deliveredToEuropeana) {
        this.deliveredToEuropeana = deliveredToEuropeana;
    }

    private boolean containValidationErrors(EacCpf eacCpf) {
        for (Warnings warnings : eacCpf.getWarningses()) {
            if (!warnings.getIswarning()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEditable() {
        return isValidated() && !isPublished() && !(convertedToEseEdm || deliveredToEuropeana);
    }
}
