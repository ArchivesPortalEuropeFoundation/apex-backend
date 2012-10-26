package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.Ead;

public class EadSearchOptions {
	private int pageNumber = 1;
	private int pageSize = 20;
	private Class<? extends Ead> eadClazz;


	public Class<? extends Ead> getEadClazz() {
		return eadClazz;
	}

	public void setEadClazz(Class<? extends Ead> eadClazz) {
		this.eadClazz = eadClazz;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
}
