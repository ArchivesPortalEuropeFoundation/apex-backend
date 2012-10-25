package eu.apenet.persistence.dao;

import java.util.Date;
import java.util.List;

import eu.apenet.persistence.vo.IndexQueue;

/**
 * 
 * @author Jara
 *
 */

public interface IndexQueueDAO extends GenericDAO<IndexQueue, Integer> {
	public IndexQueue getIndexQueueByHg(Integer hgId);
	public IndexQueue getIndexQueueByFa(Integer faId);
	public IndexQueue getIndexQueueBySg(Integer sgId);
	public Integer getMaxIqId();
	public List<IndexQueue> getAllOrderedbyId();
	public List<IndexQueue> getFilesOrderedbyId(String type);
	public Integer getMaxPosition();
	public List<IndexQueue> getFilesbeforeDate(Date date, String sortValue, boolean ascending);
	public Integer getMaxPositionofHgs();
	public Integer getMaxPositionofFas();
    public Integer getLastPositionOfHgs(int aiId);
    public Integer getLastPositionOfFas(int aiId);
	public IndexQueue getFilebyPosition(Integer position);
	public List<IndexQueue> getHgs();
	public List<IndexQueue> getFas();
	public List<IndexQueue> getFilesFromPosition(Integer position);
	public List<IndexQueue> getFilesWithErrors();
	List<IndexQueue> getFirstItems(int limit);
}

