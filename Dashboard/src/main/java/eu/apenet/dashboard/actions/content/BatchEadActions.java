package eu.apenet.dashboard.actions.content;

import java.util.List;

import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.vo.QueueAction;

public class BatchEadActions extends AbstractEadActions {
	public static final String ALL_ITEMS = "all";
	public static final String SELECTED_ITEMS = "only_selected";
	public static final String SEARCHED_ITEMS = "only_searched";
	private String batchItems;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2671921974304007944L;

	public String validateEad() {
		return addBatchToQueue(QueueAction.VALIDATE);
	}

	public String convertEad() {
		return addBatchToQueue(QueueAction.CONVERT);
	}

	public String publishEad() {
		return addBatchToQueue(QueueAction.PUBLISH);
	}

	public String unpublishEad() {
		return addBatchToQueue(QueueAction.UNPUBLISH);
	}

	public String deleteEad() {
		return addBatchToQueue(QueueAction.DELETE);
	}

	@Override
	public String convertValidatePublishEad() {
		return addBatchToQueue(QueueAction.CONVERT_VALIDATE_PUBLISH);
	}
	
	

	@Override
	public String deleteEseEdm() {
		return addBatchToQueue(QueueAction.DELETE_ESE_EDM);
	}

	
	@Override
	public String deleteFromEuropeana() {
		return addBatchToQueue(QueueAction.DELETE_FROM_EUROPEANA);
	}

	@Override
	public String deliverToEuropeana() {
		return addBatchToQueue(QueueAction.DELIVER_TO_EUROPEANA);
	}
	

	@Override
	public String deleteFromQueue() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	private String addBatchToQueue(QueueAction queueAction) {
		try {
		if (SELECTED_ITEMS.equals(batchItems)) {
			List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
					AjaxControllerAbstractAction.LIST_IDS);
			if (ids != null) {
				EadService.addBatchToQueue(ids, getAiId(), getXmlType(), queueAction, null);
				return SUCCESS;
			} else {
				return ERROR;
			}

		} else if (SEARCHED_ITEMS.equals(batchItems)) {
			EadSearchOptions eadSearchOptions = (EadSearchOptions)getServletRequest().getSession()
					.getAttribute(ContentManagerAction.EAD_SEARCH_OPTIONS);
			EadService.addBatchToQueue(eadSearchOptions, queueAction,null);
			return SUCCESS;
		} else {
			EadService.addBatchToQueue(null, getAiId(), getXmlType(), queueAction, null);
			return SUCCESS;
		}
		} catch (Exception e){
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String getBatchItems() {
		return batchItems;
	}

	public void setBatchItems(String batchItems) {
		this.batchItems = batchItems;
	}




}
