package eu.apenet.dashboard.actions.content.ead;

import eu.apenet.dashboard.actions.content.TypeResult;
import eu.apenet.persistence.vo.*;

public class EadResult extends TypeResult {

    private String eadid;
    private boolean dynamic;
    private Long units;

    public EadResult(Ead ead) {
        this.eadid = ead.getEadid();
        this.id = ead.getId();
        if (ead.getTitle().length() > MAX_TITLE) {
            this.title = ead.getTitle().substring(0, MAX_TITLE) + "...";
        } else {
            this.title = ead.getTitle();
        }
        this.date = FORMATTER.format(ead.getUploadDate());
        this.converted = ead.isConverted();
        this.published = ead.isPublished();
        this.dynamic = ead.isDynamic();
        this.validated = ValidatedState.VALIDATED.equals(ead.getValidated());
        this.validatedFatalError = ValidatedState.FATAL_ERROR.equals(ead.getValidated());
        this.units = ead.getTotalNumberOfUnits();
        this.queueReady = QueuingState.READY.equals(ead.getQueuing());
        this.queueError = QueuingState.ERROR.equals(ead.getQueuing());
        this.queueProcessing = QueuingState.BUSY.equals(ead.getQueuing());
        if ((!QueuingState.NO.equals(ead.getQueuing()) && ead.getQueueItem() != null)) {
            queueAction = ead.getQueueItem().getAction();
        }
        this.containWarnings = ead.getWarningses().size() > 0;
        this.containValidationErrors = containValidationErrors(ead);
    }

    public String getEadid() {
        return eadid;
    }

    public Long getUnits() {
        return units;
    }

    public String getIndexedText() {
        if (published) {
            return getUnits() + "";
        } else {
            return CONTENT_MESSAGE_NO;
        }
    }

    public String getDynamicText() {
        if (dynamic) {
            return CONTENT_MESSAGE_YES;
        } else {
            return CONTENT_MESSAGE_NO;
        }
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public boolean containValidationErrors(Ead ead) {
        for (Warnings warnings : ead.getWarningses()) {
            if (!warnings.getIswarning()) {
                return true;
            }
        }
        return false;
    }
}
