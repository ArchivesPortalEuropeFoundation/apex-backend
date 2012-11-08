package eu.apenet.dashboard.actions.content;

import eu.apenet.dashboard.services.ead.EadService;

public class EadActions extends AbstractEadActions {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2671921974304007944L;

	public String validateEad() {
		EadService.validate(getXmlType(), id);
		return SUCCESS;
	}

	public String convertEad() {
		EadService.convert(getXmlType(), id);
		return SUCCESS;
	}

	public String publishEad() {
		EadService.publish(getXmlType(), id);
		return SUCCESS;
	}

	public String unpublishEad() {
		EadService.unpublish(getXmlType(), id);
		return SUCCESS;
	}

	public String deleteEad() {
		EadService.delete(getXmlType(), id);
		return SUCCESS;
	}

	@Override
	public String convertValidatePublishEad() {
		try {
			EadService.convertValidatePublish(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}

	}

}
