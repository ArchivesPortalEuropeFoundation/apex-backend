package eu.apenet.dashboard.services.ead.xml.stream.mets.xpath;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StructMapDiv {
	private Integer order;
	private String label;
	private List<String> fileIds = new ArrayList<String>();
	public StructMapDiv(String order, String label){
		if (StringUtils.isNotBlank(order)){
			this.order = Integer.parseInt(order);
		}
		this.label = label;
	}
	
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getFileIds() {
		return fileIds;
	}

	public void setFileIds(List<String> fileIds) {
		this.fileIds = fileIds;
	}
	public boolean isValid(){
		return fileIds.size() > 0;
	}
}
