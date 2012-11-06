package eu.apenet.dashboard.actions.content;

import java.util.List;

import eu.apenet.dashboard.actions.ajax.AjaxControllerAbstractAction;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.vo.QueueAction;

public class BatchEadActions extends AbstractEadActions {
	private boolean onlySelectedItems = true;

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

	@SuppressWarnings("unchecked")
	private String addBatchToQueue(QueueAction queueAction) {
		if (onlySelectedItems) {
			List<Integer> ids = (List<Integer>) getServletRequest().getSession().getAttribute(
					AjaxControllerAbstractAction.LIST_IDS);
			if (ids != null) {
				EadService.addBatchToQueue(ids, getAiId(), getXmlType(), queueAction);
				return SUCCESS;
			} else {
				return ERROR;
			}

		} else {
			EadService.addBatchToQueue(null, getAiId(), getXmlType(), queueAction);
			return SUCCESS;
		}
	}

	public boolean isOnlySelectedItems() {
		return onlySelectedItems;
	}

	public void setOnlySelectedItems(boolean onlySelectedItems) {
		this.onlySelectedItems = onlySelectedItems;
	}

}
