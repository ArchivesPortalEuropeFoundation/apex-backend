package eu.apenet.persistence.dao;

import java.util.Date;
import java.util.List;

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
	public Ese getEseByIdentifierAndFormat(String identifier,MetadataFormat metadataFormat);
	public List<Ese> getEsesByFindingAidAndState(Integer faId,EseState eseState);
	public List<Ese> getEsesFromDeletedFindingaids(String oaiIdentifier);
	public List<String> getSets();
        public List<String> getSetsWithPublicatedFiles();
}
