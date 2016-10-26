/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.ead3;

import eu.apenet.dashboard.actions.content.TypeResult;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.QueuingState;
import eu.apenet.persistence.vo.ValidatedState;

/**
 *
 * @author kaisar
 */
public class Ead3Result extends TypeResult {

    private String ead3id;
    private boolean dynamic;
    private Long units;

    public Ead3Result(Ead3 ead3) {
        this.ead3id = ead3.getIdentifier();
        this.id = ead3.getId();
        if (ead3.getTitle().length() > MAX_TITLE) {
            this.title = ead3.getTitle().substring(0, MAX_TITLE) + "...";
        } else {
            this.title = ead3.getTitle();
        }
        this.date = FORMATTER.format(ead3.getUploadDate());
        this.converted = ead3.isConverted();
        this.published = ead3.isPublished();
        this.validated = ValidatedState.VALIDATED.equals(ead3.getValidated());
        this.validatedFatalError = ValidatedState.FATAL_ERROR.equals(ead3.getValidated());
//        this.units = ead3.getTotalNumberOfUnits();
        this.queueReady = QueuingState.READY.equals(ead3.getQueuing());
        this.queueError = QueuingState.ERROR.equals(ead3.getQueuing());
        this.queueProcessing = QueuingState.BUSY.equals(ead3.getQueuing());
        if ((!QueuingState.NO.equals(ead3.getQueuing()) && ead3.getQueueItem() != null)) {
            queueAction = ead3.getQueueItem().getAction();
        }
        this.containWarnings = ead3.getWarningses().size() > 0;
        this.containValidationErrors = containValidationErrors(ead3);
    }

    public String getEad3id() {
        return ead3id;
    }

    public void setEad3id(String ead3id) {
        this.ead3id = ead3id;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public Long getUnits() {
        return units;
    }

    public void setUnits(Long units) {
        this.units = units;
    }

    private boolean containValidationErrors(Ead3 ead3) {
        return ead3.getWarningses().stream().anyMatch((warnings) -> (!warnings.getIswarning()));
    }

}
