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

    private long cpfRelations;
    private long resourceRelations;
    private long functionRelations;

    public EacCpfResult(EacCpf eacCpf) {
        this.id = eacCpf.getId();
        if (eacCpf.getTitle().length() > MAX_TITLE) {
            this.title = eacCpf.getTitle().substring(0, MAX_TITLE) + "...";
        } else {
            this.title = eacCpf.getTitle();
        }
        this.date = FORMATTER.format(eacCpf.getUploadDate());
        this.converted = eacCpf.isConverted();
        this.published = eacCpf.isPublished();
        this.validated = ValidatedState.VALIDATED.equals(eacCpf.getValidated());
        this.validatedFatalError = ValidatedState.FATAL_ERROR.equals(eacCpf.getValidated());
        this.queueReady = QueuingState.READY.equals(eacCpf.getQueuing());
        this.queueError = QueuingState.ERROR.equals(eacCpf.getQueuing());
        this.queueProcessing = QueuingState.BUSY.equals(eacCpf.getQueuing());
        if ((!QueuingState.NO.equals(eacCpf.getQueuing()) && eacCpf.getQueueItem() != null)) {
            queueAction = eacCpf.getQueueItem().getAction();
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
}
