package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.UploadMethod;

/**
 * 
 * @author eloy
 *
 */

public interface UploadMethodDAO extends GenericDAO<UploadMethod, Integer> {

	public UploadMethod getUploadMethodByMethod(String method);
}
