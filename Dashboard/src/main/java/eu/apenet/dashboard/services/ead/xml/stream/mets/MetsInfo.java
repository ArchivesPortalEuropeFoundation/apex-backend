package eu.apenet.dashboard.services.ead.xml.stream.mets;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class MetsInfo {
	private List<DaoInfo> daoInfos = new ArrayList<DaoInfo>();
	private String rightsCategory;
	private String rightsHolder;
	private String rightsComments;
	private String rightsDeclaration;
	private String rightsConstraint;
	private String rightsOtherCategory;

	public List<DaoInfo> getDaoInfos() {
		return daoInfos;
	}

	public void setDaoInfos(List<DaoInfo> daoInfos) {
		this.daoInfos = daoInfos;
	}

	public String getRightsCategory() {
		return rightsCategory;
	}

	public void setRightsCategory(String rightsCategory) {
		this.rightsCategory = rightsCategory;
	}
	public boolean containRightsInfo(){
		return StringUtils.isNotBlank(rightsCategory) || StringUtils.isNotBlank(rightsHolder)  
				|| StringUtils.isNotBlank(rightsComments) ||StringUtils.isNotBlank(rightsDeclaration)  || StringUtils.isNotBlank(rightsConstraint)  ;
	}

	public String getRightsHolder() {
		return rightsHolder;
	}

	public String getRightsComments() {
		return rightsComments;
	}

	public void setRightsHolder(String rightsHolder) {
		this.rightsHolder = rightsHolder;
	}

	public void setRightsComments(String rightsComments) {
		this.rightsComments = rightsComments;
	}

	public String getRightsDeclaration() {
		return rightsDeclaration;
	}

	public String getRightsConstraint() {
		return rightsConstraint;
	}

	public void setRightsDeclaration(String rightsDeclaration) {
		this.rightsDeclaration = rightsDeclaration;
	}

	public void setRightsConstraint(String rightsConstraint) {
		this.rightsConstraint = rightsConstraint;
	}

	public String getRightsOtherCategory() {
		return rightsOtherCategory;
	}

	public void setRightsOtherCategory(String rightsOtherCategory) {
		this.rightsOtherCategory = rightsOtherCategory;
	}
	
}
