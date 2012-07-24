package eu.apenet.persistence.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class Ead  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -831753351582189830L;
	private Integer id;
	private FileState fileState;
	private UploadMethod uploadMethod;
	private ArchivalInstitution archivalInstitution;
	private Integer aiId;
	private String title;
	private Date uploadDate;
	private String pathApenetead;
	private String eadid;
	private Long totalNumberOfDaos;
	private Long totalNumberOfUnits;
	private Long totalNumberOfUnitsWithDao;
	private Set<IndexQueue> indexQueues = new HashSet<IndexQueue>(0);
	private Set<Warnings> warningses = new HashSet<Warnings>(0);
	private Set<EadContent> eadContents = new HashSet<EadContent>(0);
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public FileState getFileState() {
		return fileState;
	}
	public void setFileState(FileState fileState) {
		this.fileState = fileState;
	}
	public UploadMethod getUploadMethod() {
		return uploadMethod;
	}
	public void setUploadMethod(UploadMethod uploadMethod) {
		this.uploadMethod = uploadMethod;
	}
	public ArchivalInstitution getArchivalInstitution() {
		return archivalInstitution;
	}
	public void setArchivalInstitution(ArchivalInstitution archivalInstitution) {
		this.archivalInstitution = archivalInstitution;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getPathApenetead() {
		return pathApenetead;
	}
	public void setPathApenetead(String pathApenetead) {
		this.pathApenetead = pathApenetead;
	}
	public String getEadid() {
		return eadid;
	}
	public void setEadid(String eadid) {
		this.eadid = eadid;
	}
	public Long getTotalNumberOfDaos() {
		return totalNumberOfDaos;
	}
	public void setTotalNumberOfDaos(Long totalNumberOfDaos) {
		this.totalNumberOfDaos = totalNumberOfDaos;
	}
	public Long getTotalNumberOfUnits() {
		return totalNumberOfUnits;
	}
	public void setTotalNumberOfUnits(Long totalNumberOfUnits) {
		this.totalNumberOfUnits = totalNumberOfUnits;
	}
	public Long getTotalNumberOfUnitsWithDao() {
		return totalNumberOfUnitsWithDao;
	}
	public void setTotalNumberOfUnitsWithDao(Long totalNumberOfUnitsWithDao) {
		this.totalNumberOfUnitsWithDao = totalNumberOfUnitsWithDao;
	}

    @Deprecated
	public Set<IndexQueue> getIndexQueues() {
		return indexQueues;
	}
	public void setIndexQueues(Set<IndexQueue> indexQueues) {
		this.indexQueues = indexQueues;
	}
	public Set<Warnings> getWarningses() {
		return warningses;
	}
	public void setWarningses(Set<Warnings> warningses) {
		this.warningses = warningses;
	}
    @Deprecated
	public Set<EadContent> getEadContents() {
		return eadContents;
	}
	public void setEadContents(Set<EadContent> eadContents) {
		this.eadContents = eadContents;
	}
	
    public EadContent getEadContent() {
        Set<EadContent> set = getEadContents();
        if(set == null || set.isEmpty())
            return null;
        return set.iterator().next();
    }
    public IndexQueue getIndexQueue() {
        Set<IndexQueue> set = getIndexQueues();
        if(set == null || set.isEmpty())
            return null;
        return set.iterator().next();
    }
	public Integer getAiId() {
		return aiId;
	}
	public void setAiId(Integer aiId) {
		this.aiId = aiId;
	}
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " - (" + eadid + "," + id + ") ";
	}
    
}
