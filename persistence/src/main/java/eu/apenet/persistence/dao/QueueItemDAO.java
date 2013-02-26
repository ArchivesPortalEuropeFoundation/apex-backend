package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.QueueItem;

/**
 * 
 * @author Jara
 *
 */

public interface QueueItemDAO extends GenericDAO<QueueItem, Integer> {
	public List<QueueItem> getFilesWithErrors();
	QueueItem getFirstItem();
}

