package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.UpFile;

/**
 * 
 * @author paul
 *
 */

public interface UpFileDAO extends GenericDAO<UpFile, Integer> {
	public List<UpFile> getUpFiles(Integer aiId, String upFileState, String fileType);
	public List<UpFile> getUpFiles(Integer aiId);
	public UpFile getUpFile(Integer fileId);
}
