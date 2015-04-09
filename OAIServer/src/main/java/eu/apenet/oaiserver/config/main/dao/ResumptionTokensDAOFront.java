package eu.apenet.oaiserver.config.main.dao;

import eu.apenet.oaiserver.config.main.vo.ResumptionTokens;

/**
 * Created by yoannmoranville on 09/04/15.
 */
public interface ResumptionTokensDAOFront {
    public abstract  ResumptionTokens getResumptionToken(String resumptionTokenStr);

    public abstract ResumptionTokens saveResumptionTokens(ResumptionTokens resumptionTokens);

    public abstract void removeOldResumptionToken();
}
