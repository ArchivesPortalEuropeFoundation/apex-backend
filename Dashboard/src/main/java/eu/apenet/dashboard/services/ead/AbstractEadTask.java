package eu.apenet.dashboard.services.ead;

import org.apache.log4j.Logger;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.Warnings;

public abstract class AbstractEadTask {
	protected Logger logger = Logger.getLogger(getClass());
	protected abstract void execute(Ead ead) throws APEnetException;
    protected static Warnings setCorrectWarning(Ead ead, Warnings warnings){
        if(ead instanceof FindingAid)
            warnings.setFindingAid((FindingAid)ead);
        else if(ead instanceof HoldingsGuide)
            warnings.setHoldingsGuide((HoldingsGuide) ead);
        else if(ead instanceof SourceGuide)
            warnings.setSourceGuide((SourceGuide)ead);
        return warnings;
    }
}
