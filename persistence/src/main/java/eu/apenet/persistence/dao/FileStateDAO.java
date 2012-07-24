package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.FileState;

/**
 * 
 * @author eloy
 *
 */

public interface FileStateDAO extends GenericDAO<FileState, Integer> {

	public FileState getFileStateByState(String state);
}
