package eu.apenet.dashboard.actions.content;

import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.persistence.vo.QueueAction;

public class BatchEadActions extends AbstractEadActions{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2671921974304007944L;

	public String validateEad(){
		EadService.addBatchToQueue(getAiId(), getXmlType(), QueueAction.VALIDATE);
		return SUCCESS;
	}
	public String convertEad(){
		EadService.addBatchToQueue(getAiId(), getXmlType(), QueueAction.CONVERT);
		return SUCCESS;
	}
	
	public String publishEad(){
		EadService.addBatchToQueue(getAiId(), getXmlType(), QueueAction.PUBLISH);
		return SUCCESS;
	}
	public String unpublishEad(){
		EadService.addBatchToQueue(getAiId(), getXmlType(), QueueAction.UNPUBLISH);
		return SUCCESS;
	}
	public String deleteEad() {
		EadService.addBatchToQueue(getAiId(), getXmlType(), QueueAction.DELETE);
		return SUCCESS;
	}
	@Override
	public String convertValidatePublishEad() {
		EadService.addBatchToQueue(getAiId(), getXmlType(), QueueAction.CONVERT_VALIDATE_PUBLISH);
		return SUCCESS;
	}
	
}
