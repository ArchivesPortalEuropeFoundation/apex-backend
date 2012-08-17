package eu.apenet.commons.seconddisplay.jsp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;

public class ContextTag extends SimpleTagSupport{
	private Object clevel;
	private Object eadContent;
	private String onlyArchives;
	private Object country;

	public void doTag() throws JspException, IOException {
		List<String> hierarchy = new ArrayList<String>();
		EadContent eadContent = null;
		StringBuilder result = new StringBuilder();
		if (clevel != null){
			CLevel currentCLevel = (CLevel) clevel;
			CLevel parent = currentCLevel.getParent();
			
			while (parent != null ){
				hierarchy.add(parent.getUnittitle());
				parent = parent.getParent();
			}
			eadContent = currentCLevel.getEadContent();
			hierarchy.add(eadContent.getUnittitle());
		}else if (this.eadContent != null){
			eadContent = (EadContent) this.eadContent;
		}
		
		if (!"true".equals(onlyArchives)){
			hierarchy.add(eadContent.getTitleproper());
		}
		
		ArchivalInstitution ai  = eadContent.getEad().getArchivalInstitution();
		while (ai != null){
			hierarchy.add(ai.getAiname());
			ai = ai.getArchivalInstitution();
		}
		hierarchy.add((String) country);
		int numberOfWhitespaces = 0;
		for (int i = hierarchy.size() -1; i >=0;i--){
			//result.append("<span class=\"contextHierarchyItem\">");
			for (int j = 0; j < numberOfWhitespaces;j++){
				result.append("&nbsp;&nbsp;&nbsp;");
			}
			result.append(hierarchy.get(i));
			//result.append("</span>");
			result.append("<br/>");
			numberOfWhitespaces++;
		}
		this.getJspContext().getOut().print(result);

	}



	public Object getClevel() {
		return clevel;
	}



	public void setClevel(Object clevel) {
		this.clevel = clevel;
	}



	public Object getEadContent() {
		return eadContent;
	}



	public void setEadContent(Object eadContent) {
		this.eadContent = eadContent;
	}



	public String getOnlyArchives() {
		return onlyArchives;
	}



	public void setOnlyArchives(String onlyArchives) {
		this.onlyArchives = onlyArchives;
	}



	public Object getCountry() {
		return country;
	}



	public void setCountry(Object country) {
		this.country = country;
	}


}
