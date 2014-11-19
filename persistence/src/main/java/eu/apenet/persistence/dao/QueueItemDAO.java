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
    Long getPositionOfNextItem(int archivalInstitutionId);
}

