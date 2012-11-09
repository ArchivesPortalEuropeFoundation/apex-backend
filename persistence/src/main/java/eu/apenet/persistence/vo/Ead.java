package eu.apenet.persistence.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public abstract class Ead  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -831753351582189830L;




	public abstract Integer getId();
	public abstract void setId(Integer id);
//	public abstract FileState getFileState() ;
//	public abstract void setFileState(FileState fileState);
	public abstract UploadMethod getUploadMethod();
	public abstract void setUploadMethod(UploadMethod uploadMethod);
	public abstract ArchivalInstitution getArchivalInstitution();
	public abstract void setArchivalInstitution(ArchivalInstitution archivalInstitution) ;
	public abstract String getTitle();
	public abstract void setTitle(String title);
	public abstract Date getUploadDate();
	public abstract void setUploadDate(Date uploadDate);
	public abstract String getPathApenetead();
	public abstract void setPathApenetead(String pathApenetead);
	public abstract String getEadid();
	public abstract void setEadid(String eadid);
	public abstract Long getTotalNumberOfDaos();
	public abstract void setTotalNumberOfDaos(Long totalNumberOfDaos);
	public abstract Long getTotalNumberOfUnits() ;
	public abstract void setTotalNumberOfUnits(Long totalNumberOfUnits) ;
	public abstract Long getTotalNumberOfUnitsWithDao();
	public abstract void setTotalNumberOfUnitsWithDao(Long totalNumberOfUnitsWithDao);
	public abstract boolean isPublished();
	public abstract void setPublished(boolean searchable);
	public abstract ValidatedState getValidated();
	public abstract void setValidated(ValidatedState validated);
	public abstract boolean isConverted();
	public abstract void setConverted(boolean converted);
	public abstract QueuingState getQueuing();
	public abstract void setQueuing(QueuingState queingState);	
    @Deprecated
	public abstract Set<QueueItem> getQueuesItems();
	public abstract void setQueueItems(Set<QueueItem> indexQueues);
	public abstract Set<Warnings> getWarningses();
	public abstract void setWarningses(Set<Warnings> warningses);
    @Deprecated
	public abstract Set<EadContent> getEadContents() ;
	public abstract void setEadContents(Set<EadContent> eadContents) ;
	
    public EadContent getEadContent() {
        Set<EadContent> set = getEadContents();
        if(set == null || set.isEmpty())
            return null;
        return set.iterator().next();
    }
    public QueueItem getQueueItem() {
        Set<QueueItem> set = getQueuesItems();
        if(set == null || set.isEmpty())
            return null;
        return set.iterator().next();
    }
	public abstract Integer getAiId();
	public abstract void setAiId(Integer aiId) ;
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " - (" + getEadid() + "," + getId() + ") ";
	}
    
}
