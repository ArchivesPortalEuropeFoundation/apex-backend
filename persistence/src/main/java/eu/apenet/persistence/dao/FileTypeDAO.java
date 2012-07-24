package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.FileType;

/**
 * 
 * @author eloy
 *
 */

public interface FileTypeDAO extends GenericDAO<FileType, Integer> {

	public FileType getFileTypeByType(String ftype);
}
