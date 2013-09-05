package eu.apenet.commons.view.jsp;

import javax.servlet.jsp.tagext.SimpleTagSupport;

public class AbstractPagingTag extends SimpleTagSupport {


	private Object pageNumber;

	private Object pageSize;

	private Object numberOfItems;

	private Object numberFormat;
	
	public Object getPageNumber() {
		return pageNumber;
	}
	
	public Object getPageSize() {
		return pageSize;
	}

	public Object getNumberOfItems() {
		return numberOfItems;
	}
	
	public void setPageNumber(Object pageNumber) {
		this.pageNumber = pageNumber;
	}

	public void setPageSize(Object pageSize) {
		this.pageSize = pageSize;
	}

	public void setNumberOfItems(Object numberOfItems) {
		this.numberOfItems = numberOfItems;
	}

	public Object getNumberFormat() {
		return numberFormat;
	}
	
	public void setNumberFormat(Object numberFormat) {
		this.numberFormat = numberFormat;
	}
	
	public static Integer toInteger(Object object) {
		if (object != null) {
			if (object instanceof Integer) {
				return (Integer) object;
			} else if (object instanceof String) {
				String objectString = (String) object;
				return new Integer(objectString);
			}
		}
		return null;
	}

	public static Long toLong(Object object) {
		if (object != null) {
			if (object instanceof Integer) {
				return ((Integer) object).longValue();
			} else if (object instanceof Long) {
				return ((Long) object);
			} else if (object instanceof String) {
				String objectString = (String) object;
				return new Long(objectString);
			}
		}
		return null;
	}


}
