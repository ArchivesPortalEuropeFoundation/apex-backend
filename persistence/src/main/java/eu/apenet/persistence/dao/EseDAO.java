package eu.apenet.persistence.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.MetadataFormat;

/**
 * 
 * @author eloy
 *
 */

public interface EseDAO extends GenericDAO<Ese, Integer> {
	public List<Ese> getEses(Integer faId, Integer aiId);
	public Date getTheEarliestDatestamp();
	public List<Ese> getEsesByArguments(Date from,Date until,MetadataFormat metadataFormat,String set,Integer startElement,Integer limitPerResponse);
	public Set<Ese> getAllEsesInSetFormat();
	public List<String> getSetsOfEses(Date start,Date end,String eset,Integer startNumber,Integer limit);
	public List<Ese> getEsesWithoutFindingAid();
	public Ese getEseByIdentifierAndFormat(String identifier,MetadataFormat metadataFormat);
	public List<Ese> getEsesByFindingAidAndState(Integer faId,EseState eseState);
	public Long getNumberOfRecordsByAiId(Integer aiId);
	public Long getNumberOfRecordsDeliveredByAiId(Integer aiId);
	public Long getSearchedNumberOfRecordsByAiId(String searchTerms,Integer aiId, int option,Set<String> convertedToESEFileStates);
	public Long getTotalDeliveredToEuropeanaRecordsByAiId(String searchTerms,Integer aiId,int option,Set<String> deliveredToEuropeanaFileStates);
	public List<Ese> getEsesFromDeletedFindingaids(String oaiIdentifier);
}
