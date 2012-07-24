package eu.apenet.persistence.vo;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class HoldingsGuide extends Ead {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2297086401601854142L;

	private Set<FindingAid> findingAids = new HashSet<FindingAid>(0);

	public HoldingsGuide() {
	}
	@Deprecated
	public HoldingsGuide(int hgId, FileState fileState,
			UploadMethod uploadMethod, ArchivalInstitution archivalInstitution,
			String hgTittle, Date uploadDate, String pathApenetead) {
		this.setId(hgId);
		this.setFileState(fileState);
		this.setUploadMethod(uploadMethod);
		this.setArchivalInstitution(archivalInstitution);
		this.setTitle(hgTittle);
		this.setUploadDate(uploadDate);
		this.setPathApenetead(pathApenetead);
	}
	@Deprecated
	public HoldingsGuide(int hgId, FileState fileState,
			UploadMethod uploadMethod, ArchivalInstitution archivalInstitution,
			String hgTittle, Date uploadDate, String pathApenetead,
			String hgEadid, String pathHtml, Long totalNumberOfDaos,
			Long totalNumberOfUnits, Long totalNumberOfUnitsWithDao,
			Set<FindingAid> findingAids, Set<IndexQueue> indexQueues,
			Set<Warnings> warningses, Set<EadContent> eadContents) {
		this.setId(hgId);
		this.setFileState(fileState);
		this.setUploadMethod(uploadMethod);
		this.setArchivalInstitution(archivalInstitution);
		this.setTitle(hgTittle);
		this.setUploadDate(uploadDate);
		this.setPathApenetead(pathApenetead);
		this.setEadid(hgEadid);
		//this.pathHtml = pathHtml;
		this.setTotalNumberOfDaos(totalNumberOfDaos);
		this.setTotalNumberOfUnits(totalNumberOfUnits);
		this.setTotalNumberOfUnitsWithDao(totalNumberOfUnitsWithDao);
		this.findingAids = findingAids;
		this.setIndexQueues(indexQueues);
		this.setWarningses(warningses);
		this.setEadContents(eadContents);
	}
	@Deprecated
	public int getHgId() {
		if (this.getId() == null){
			return -1;
		}
		return this.getId();
	}

	@Deprecated
	public String getHgTittle() {
		return this.getTitle();
	}

	@Deprecated
	public String getHgEadid() {
		return this.getEadid();
	}

	public Set<FindingAid> getFindingAids() {
		return this.findingAids;
	}

	public void setFindingAids(Set<FindingAid> findingAids) {
		this.findingAids = findingAids;
	}
}
