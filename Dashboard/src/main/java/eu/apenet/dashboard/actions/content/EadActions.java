package eu.apenet.dashboard.actions.content;

import eu.apenet.dashboard.services.ead.EadService;

import java.util.Properties;

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
		try {
			EadService.validate(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String convertEad(Properties properties) {
		try {
			EadService.convert(getXmlType(), id, properties);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String publishEad() {
		try {
			EadService.publish(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String unpublishEad() {
		try {
			EadService.unpublish(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String deleteEad() {
		try {
			EadService.delete(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String convertValidatePublishEad(Properties properties) {
		try {
			EadService.convertValidatePublish(getXmlType(), id, properties);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}

	}

	@Override
	public String deleteEseEdm() {
		try {
			EadService.deleteEseEdm(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String deleteFromEuropeana() {
		try {
			EadService.deleteFromEuropeana(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String deliverToEuropeana() {
		try {
			EadService.deliverToEuropeana(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String deleteFromQueue() {
		try {
			EadService.deleteFromQueue(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

}
