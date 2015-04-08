package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.QueueItem;

/**
 * 
 * @author Jara
 *
 */

public interface QueueItemDAO extends GenericDAO<QueueItem, Integer> {
	Long countItems();
	List<QueueItem> getItemsWithErrors();
	List<QueueItem> getFirstItems();
	List<QueueItem> getDisabledItems();
	QueueItem getFirstItem();
	boolean hasItemsWithErrors(int aiId);
    Long getPositionOfFirstItem(int aiId);
    Long countItems(int aiId);
    List<Object[]> countByArchivalInstitutions();
    List<QueueItem> getItemsOfInstitution(Integer aiId);
    void setPriorityToQueueOfArchivalInstitution(Integer aiId, Integer priority);
}

