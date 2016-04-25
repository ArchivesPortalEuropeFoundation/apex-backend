package eu.apenet.dashboard.actions.content.ead;

import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;

public class FindingAidResult extends EadResult {

    private static final String ZERO = "0";
    private boolean errorDuringEdmConversion;
    private boolean convertedToEseEdm;
    private boolean deliveredToEuropeana;
    private boolean noEuropeanaCandidate;
    private Long totalNumberOfChos;
    private Long totalNumberOfWebResourceEdm;
    private boolean hasEseEdmFiles = false;
    private int numberOfHgSgRelations;

    public FindingAidResult(Ead ead) {
        super(ead);
        FindingAid findingAid = (FindingAid) ead;
        this.errorDuringEdmConversion = EuropeanaState.FATAL_ERROR.equals(findingAid.getEuropeana());
        this.convertedToEseEdm = EuropeanaState.CONVERTED.equals(findingAid.getEuropeana());
        this.deliveredToEuropeana = EuropeanaState.DELIVERED.equals(findingAid.getEuropeana());
        this.noEuropeanaCandidate = EuropeanaState.NO_EUROPEANA_CANDIDATE.equals(findingAid.getEuropeana());
        this.totalNumberOfChos = findingAid.getTotalNumberOfChos();
        this.totalNumberOfWebResourceEdm = findingAid.getTotalNumberOfWebResourceEdm();
        hasEseEdmFiles = (convertedToEseEdm || deliveredToEuropeana) && totalNumberOfChos > 0;
        numberOfHgSgRelations = findingAid.getHgSgFaRelations().size();
    }

    public boolean isConvertedToEseEdm() {
        return convertedToEseEdm;
    }

    public boolean isDeliveredToEuropeana() {
        return deliveredToEuropeana;
    }

    public Long getTotalNumberOfChos() {
        return totalNumberOfChos;
    }

    public String getEseEdmText() {
        if (convertedToEseEdm) {
            return getTotalNumberOfChos() + "";
        } else if (noEuropeanaCandidate) {
            return ZERO;
        } else if (errorDuringEdmConversion) {
            return CONTENT_MESSAGE_FATAL_ERROR;
        } else {
            return CONTENT_MESSAGE_NO;
        }
    }

    public String getEseEdmCssClass() {
        if (convertedToEseEdm || deliveredToEuropeana) {
            return STATUS_OK;
        } else if (noEuropeanaCandidate) {
            return STATUS_NOT_AVAILABLE;
        } else if (errorDuringEdmConversion) {
            return STATUS_ERROR;
        } else {
            return STATUS_NO;
        }
    }

    public String getEuropeanaCssClass() {
        if (deliveredToEuropeana) {
            return STATUS_OK;
        } else if (noEuropeanaCandidate) {
            return STATUS_NOT_AVAILABLE;
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

    public boolean isHasEseEdmFiles() {
        return hasEseEdmFiles;
    }

    public boolean isNoEuropeanaCandidate() {
        return noEuropeanaCandidate;
    }

    public boolean isEditable() {
        return isValidated() && !isPublished() && !(convertedToEseEdm || deliveredToEuropeana);
    }
    
    public int getNumberOfHgSgRelations() {
        return numberOfHgSgRelations;
    }

    public String getHoldingsGuideTitle() {
        return numberOfHgSgRelations + "";
    }

    public Long getTotalNumberOfWebResourceEdm() {
        return totalNumberOfWebResourceEdm;
    }
    
    public boolean hasEdmConversionErrors() {
        return errorDuringEdmConversion;
    }
}
