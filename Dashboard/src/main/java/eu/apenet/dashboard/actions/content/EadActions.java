package eu.apenet.dashboard.actions.content;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.services.ead.EadService;

public class EadActions extends AbstractInstitutionAction{

	private Integer id;
	private Integer type;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2671921974304007944L;

	public String validateEad() throws APEnetException{
		XmlType xmlType = XmlType.getType(type);
		EadService.validate(xmlType, id);
		return SUCCESS;
	}
	public String convertEad() throws APEnetException{
		XmlType xmlType = XmlType.getType(type);
		EadService.convert(xmlType, id);
		return SUCCESS;
	}
	
	public String publishEad() throws APEnetException{
		XmlType xmlType = XmlType.getType(type);
		EadService.publish(xmlType, id);
		return SUCCESS;
	}
	public String unpublishEad() throws APEnetException{
		XmlType xmlType = XmlType.getType(type);
		EadService.unpublish(xmlType, id);
		return SUCCESS;
	}
	public String deleteEad() throws APEnetException{
		XmlType xmlType = XmlType.getType(type);
		EadService.delete(xmlType, id);
		return SUCCESS;
	}
}
