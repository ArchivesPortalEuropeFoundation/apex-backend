package eu.apenet.dashboard.manual;

import eu.apenet.persistence.vo.FileType;

/**
 * 
 * @author eloy
 *
 * This class represents a file in the Dashboard
 */

public class FileUnit {
	private Integer fileId;		//Identifier in up_file table
	private FileType fileType;	//File type (zip, xml or xsl)
	private String fileName;	//File name
	private String eadType;		//EAD type (Holdings Guide, Finding Aid or Undefined. Empty in other case)
	private int eadTypeId = 0;
	private String eadid;		//EADID (fa_eadid if the file is a Finding Aid or hg_eadid if the file is a Holdings Guide)
	private Integer permId;		//Permanent Identifier (fa_id if the file is a Finding Aid or hg_id if the file is a Holdings Guide)
    private String errorInformation;
	
	public FileUnit() {
		
	}
	
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setEadType(String eadType) {
		this.eadType = eadType;
	}

	public String getEadType() {
		return eadType;
	}

	public void setEadid(String eadid) {
		this.eadid = eadid;
	}

	public String getEadid() {
		return eadid;
	}

	public void setPermId(Integer permId) {
		this.permId = permId;
	}

	public Integer getPermId() {
		return permId;
	}

    public String getErrorInformation() {
        return errorInformation;
    }

    public void setErrorInformation(String errorInformation) {
        this.errorInformation = errorInformation;
    }

	public int getEadTypeId() {
		return eadTypeId;
	}

	public void setEadTypeId(int eadTypeId) {
		this.eadTypeId = eadTypeId;
	}
    
}
