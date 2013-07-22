package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.UpFile;

/**
 * 
 * @author paul
 *
 */

public interface UpFileDAO extends GenericDAO<UpFile, Integer> {
	static final int MAX_FILES = 500;
	public List<UpFile> getNewUpFiles(Integer aiId, FileType fileType);
	public long countNewUpFiles(Integer aiId, FileType fileType);
	public boolean hasNewUpFiles(Integer aiId, FileType fileType);
	public List<UpFile> getUpFiles(Integer aiId);
	public List<UpFile> getAllNotAssociatedFiles() ;
}
