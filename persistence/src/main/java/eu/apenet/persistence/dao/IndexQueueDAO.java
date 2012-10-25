package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.IndexQueue;

/**
 * 
 * @author Jara
 *
 */

public interface IndexQueueDAO extends GenericDAO<IndexQueue, Integer> {
	public List<IndexQueue> getFilesWithErrors();
	List<IndexQueue> getFirstItems();
}

