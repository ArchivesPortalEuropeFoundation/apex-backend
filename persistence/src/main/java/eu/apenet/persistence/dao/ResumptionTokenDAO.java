package eu.apenet.persistence.dao;

import java.util.Date;
import java.util.List;

import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

public interface ResumptionTokenDAO extends GenericDAO<ResumptionToken, Integer>{
	public ResumptionToken getResumptionToken(Date fromDate,Date untilDate,MetadataFormat metadataFormat,String set,String limit);
	public List<ResumptionToken> getOldResumptionTokensThan(Date referenceDate);
	public List<ResumptionToken> getGreaterResumptionTokensThan(Date referenceDate);
}
