package eu.apenet.dashboard.manual.contentmanager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.manual.EADCMUnit;
import eu.apenet.dashboard.manual.contentmanager.ContentManager.EadResult;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.dao.FindingAidDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.archivesportaleurope.commons.config.DashboardConfig;

/**
 * This class has been included because we can not permit have an Action class
 * to big because the fixing and maintenance are impossible. Now this class include
 * all the parameters used into contentmanager.jsp and batch.jsp. The method-actions has been
 * kept into ContentManagerAction
 *
 */
public class AbstractContentManagerAction extends AbstractInstitutionAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1057897685234339657L;

	private static Logger log = Logger.getLogger(AbstractContentManagerAction.class);
	
	protected static final String DOWNLOAD = "download";

    public static final String ORDER_BY_DATE = "uploadDate";
    public static final String ORDER_BY_EADID = "eadid";
    public static final String ORDER_BY_TITLE = "title";
    public static final String ORDER_BY_FILESTATE_CONVERSION = "conversion";
    public static final String ORDER_BY_FILESTATE_VALIDATION = "validation";
    public static final String ORDER_BY_FILESTATE_INDEXATION = "indexation";
    public static final String ORDER_BY_FILESTATE_ESE_CONVERSION = "eseconversion";
    public static final String ORDER_BY_FILESTATE_ESE_DELIVERY = "esedelivery";
    public static final String SORT_BY_CONVERT_STATUS = "convertStatus";
    public static final String SORT_BY_VALIDATE_STATUS = "validateStatus";
    public static final String SORT_BY_HOLDINGS_GUIDE_STATUS = "holdingsGuideStatus";
    public static final String SORT_BY_INDEX_STATUS = "indexStatus";
    public static final String SORT_BY_CONVERT_ESE_STATUS = "convertEseStatus";
    public static final String SORT_BY_DELIVER_STATUS = "deliverStatus";
    public static final int SORT_BY_OK = 0;
    public static final int SORT_BY_NO = 1;
    public static final int SORT_BY_ERROR = 2;
    public static final int SORT_BY_LINKED = 3;
    public static final int SORT_BY_NOT_LINKED = 4;
    public static final int SORT_BY_SCHEDULED = 5;
    public static final int SORT_BY_READY = 6;
    public static final int SORT_BY_DELIVERED = 7;

    //ATTRIBUTES for contentmanager.jsp
	private Integer pageNumber; //Present page
	private Integer id; //It's the id of a file
	private Long size; //Its the number of total results 
	private String orderBy; //Boolean to order the showed results
	private Boolean orderDecreasing;
	private String searchTerms; //String which filter results in a search
	private List<EADCMUnit> listEADCMUnit; //Results which are shown
    private Integer xmlTypeId; //Used to define which kind of EAD is the target file 
	private Integer limit;  //Number of results per page
    private String convertStatus;
    private String validateStatus;
    private String holdingsGuideStatus;
    private String indexStatus;
    private String convertEseStatus;
    private String deliverStatus;
	private String type;
    private int nbOfConvertedFiles = 0;
    private int nbOfValidatedFiles = 0;
    private int nbOfIndexedFiles = 0;
    private int nbOfConvertedToEuropeanaFiles = 0;
	private int nbOfDeliveredToEuropeanaFiles = 0;
	//FA Statistics
	private long totalRecordsConvertedToESEFiles = 0;
    private long totalConvertedFiles = 0;
    private long totalValidatedFiles = 0;
    private long totalIndexedFiles = 0;
    private long totalConvertedToESEFiles = 0;
    private long totalDeliveredToEuropeanaFiles = 0; //TODO: this parameter now is not into contentmanager.jsp, but should be with right results 
    private long totalIndexedUnits = 0;
    private long totalRecordsDeliveredToEuropeana = 0;
    private String xsl;
	private Integer search; //select (JSP)
    private List<String> xslFiles;
    private List<CLevel> listOfFindingAidsOut;
    private List<FindingAid> listOfFindingAidsIndexed;
    private List<FindingAid> listOfFindingAidsNotIndex;
    private Integer listNotLinkedId;
    private Integer harvesting=0;
    private Integer indexing=0;
    //End FA Statistics
    
    //DOWNLOAD attributes
	private InputStream inputStream; //It's necesary to download and preview a file
	private String fileName; //It's used to manage the file download name
	private Long fileSize; //It's used to store the file download size

    //batch.jsp
    private int nbFindingAidForBatch;
    private int nbSelectedFindingAidForBatch;
    private int nbSearchFindingAidForBatch;
	
	//(Internal attributes), also NON contentmanager.jsp attributes
	private XmlType xmlType; //It depends on xmlTypeId, in validate method is filled (before each action)
	private boolean batchProcessingEnabled = APEnetUtilities.getDashboardConfig().isBatchProcessingEnabled();
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb(getText("breadcrumb.section.contentmanager"));
	}
		

	
	/**
	 * This method fills listEADCMUnit to be showed in the "Content Manager" section
	 * @return SUCCESS
	 */
	protected String getEADCMUnits() {
        log.debug("entering getEADCMUnits function");
		Integer aiId = getAiId();
		if(getOrderBy()!=null && getOrderBy().isEmpty()){
			setOrderBy(ORDER_BY_DATE);
		}
		if(aiId!=null){
            log.debug("aiId is not null");

           // setXslFiles(contentManager.getXslFiles(aiId));
		}else {
			log.warn("aiId from session is null");
            List<String> xslFiles = new ArrayList<String>();
            xslFiles.add("default.xsl");
            setXslFiles(xslFiles);
        }
		return SUCCESS;
	}
	/**
	 * Update Statistics part extracted from EADCMUnits to be used
	 * also in search. 
	 */
	private void updateStatisticsValues() {
		for(EADCMUnit eadcmUnit : getListEADCMUnit()){
            if(eadcmUnit.getEadCMUnitState() == null){
                break;
            }
            if(eadcmUnit.getEadCMUnitState() == 3 || eadcmUnit.getEadCMUnitState() == 6){
                setNbOfConvertedFiles(getNbOfConvertedFiles()+1);
            } else if(eadcmUnit.getEadCMUnitState() == 4){
                setNbOfConvertedFiles(getNbOfConvertedFiles()+1);
                setNbOfValidatedFiles(getNbOfValidatedFiles()+1);
            } else if(eadcmUnit.getEadCMUnitState() == 5){
            	setNbOfValidatedFiles(getNbOfValidatedFiles()+1);
            } else if(eadcmUnit.getEadCMUnitState() > 7 && eadcmUnit.getEadCMUnitState()!=15){
            	setNbOfIndexedFiles(getNbOfIndexedFiles()+1);
                if(eadcmUnit.getEadCMUnitState() > 8){
                	setNbOfConvertedToEuropeanaFiles(getNbOfConvertedToEuropeanaFiles()+1);
                	//this.nbOfConvertedDAOs+=new Long(eadcmUnit.getNumberOfDAOs()); //Total number of daos converted
                	if(eadcmUnit.getEadCMUnitState()==10 || eadcmUnit.getEadCMUnitState()==11){
                    	setNbOfDeliveredToEuropeanaFiles(getNbOfDeliveredToEuropeanaFiles()+1);
                    }
                }
            } else if(eadcmUnit.getEadCMUnitState()==15){
            	setNbOfConvertedFiles(getNbOfConvertedFiles()+1);
            	setNbOfValidatedFiles(getNbOfValidatedFiles()+1);
            }
        }
		setNbOfValidatedFiles(getNbOfValidatedFiles()+getNbOfIndexedFiles());
		setNbOfConvertedFiles(getNbOfConvertedFiles()+getNbOfIndexedFiles());
	}
	

	
    protected Boolean isLinkedToHoldingsGuide(){
        try {
            if(holdingsGuideStatus != null){
                 if(Integer.parseInt(holdingsGuideStatus) == SORT_BY_LINKED){
                     return true;
                 } else if(Integer.parseInt(holdingsGuideStatus) == SORT_BY_NOT_LINKED){
                     return false;
                 }
            }
            holdingsGuideStatus = null;
            return null;
        } catch (NumberFormatException e){
            holdingsGuideStatus = null;
            return null;
        }
    }
    
    protected XmlType getXmlType(){
    	return this.xmlType;
    }
	
	protected void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
	
	protected void setListOfFindingAidsNotIndex(List<FindingAid> listOfFindingAidsNotIndex) {
		this.listOfFindingAidsNotIndex = listOfFindingAidsNotIndex;
	}

	protected void setListOfFindingAidsIndexed(List<FindingAid> listOfFindingAidsIndexed) {
		this.listOfFindingAidsIndexed = listOfFindingAidsIndexed;
	}

	protected void setListOfFindingAidsOut(List<CLevel> listOfFindingAidsOut) {
		this.listOfFindingAidsOut = listOfFindingAidsOut;
	}

	public void setNbFindingAidForBatch(int nbFindingAidForBatch) {
		this.nbFindingAidForBatch = nbFindingAidForBatch;
	}

	protected void setNbSelectedFindingAidForBatch(int nbSelectedFindingAidForBatch) {
		this.nbSelectedFindingAidForBatch = nbSelectedFindingAidForBatch;
	}

	protected void setNbSearchFindingAidForBatch(int nbSearchFindingAidForBatch) {
		this.nbSearchFindingAidForBatch = nbSearchFindingAidForBatch;
	}

	protected void setXmlType(XmlType xmlType) {
		this.xmlType = xmlType;
	}

	protected void setFileName(String fileName) {
		this.fileName = fileName;
	}

	protected void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
    public String getOrderBy() {
        return orderBy;
    }

    public List<EADCMUnit> getListEADCMUnit() {
		return listEADCMUnit;
	}

	protected void setListEADCMUnit(List<EADCMUnit> listEADCMUnit) {
		this.listEADCMUnit = listEADCMUnit;
	}

	public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Boolean getOrderDecreasing() {
        return orderDecreasing;
    }

    public void setOrderDecreasing(Boolean orderDecreasing) {
        this.orderDecreasing = orderDecreasing;
    }

    public Integer getSearch() {
		return search;
	}

	public void setSearch(Integer search) {
		this.search = search;
	}

    public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getLimit() {
		return limit;
	}

	public List<String> getXslFiles(){
        return xslFiles;
    }
    
    protected void setXslFiles(List<String> xslFiles){
        this.xslFiles = xslFiles;
    }

    protected String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNbOfConvertedFiles() {
        return nbOfConvertedFiles;
    }

    public int getNbOfValidatedFiles() {
        return nbOfValidatedFiles;
    }

    public int getNbOfIndexedFiles() {
        return nbOfIndexedFiles;
    }

    public int getNbOfDeliveredToEuropeanaFiles(){
    	return this.nbOfDeliveredToEuropeanaFiles;
    }
    
    public long getTotalConvertedFiles() {
        return totalConvertedFiles;
    }

    public long getTotalConvertedToESEFiles() {
		return totalConvertedToESEFiles;
	}

	public long getTotalDeliveredToEuropeanaFiles() {
		return totalDeliveredToEuropeanaFiles;
	}

	public long getTotalValidatedFiles() {
        return totalValidatedFiles;
    }

    public long getTotalIndexedFiles() {
        return totalIndexedFiles;
    }

    protected void setTotalIndexedFiles(long totalIndexedFiles) {
		this.totalIndexedFiles = totalIndexedFiles;
	}

	public int getNbFindingAidForBatch() {
        return nbFindingAidForBatch;
    }

    public int getNbSelectedFindingAidForBatch() {
        return nbSelectedFindingAidForBatch;
    }

    public int getNbSearchFindingAidForBatch() {
        return nbSearchFindingAidForBatch;
    }

	public String getXsl() {
        return xsl;
    }

    public void setXsl(String xsl) {
        this.xsl = xsl;
    }

    public Integer getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(Integer xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }
    
    public Integer getListNotLinkedId() {
		return listNotLinkedId;
	}

	public int getNbOfConvertedToEuropeanaFiles() {
		return nbOfConvertedToEuropeanaFiles;
	}

    public String getConvertStatus() {
        return convertStatus;
    }

    public void setConvertStatus(String convertStatus) {
        this.convertStatus = convertStatus;
    }

    public String getValidateStatus() {
        return validateStatus;
    }

    public void setValidateStatus(String validateStatus) {
        this.validateStatus = validateStatus;
    }

    public String getHoldingsGuideStatus() {
        return holdingsGuideStatus;
    }

    public void setHoldingsGuideStatus(String holdingsGuideStatus) {
        this.holdingsGuideStatus = holdingsGuideStatus;
    }

    public String getIndexStatus() {
        return indexStatus;
    }

    public void setIndexStatus(String indexStatus) {
        this.indexStatus = indexStatus;
    }

    public String getConvertEseStatus() {
        return convertEseStatus;
    }

    public void setConvertEseStatus(String convertEseStatus) {
        this.convertEseStatus = convertEseStatus;
    }

    protected void setSize(Long size) {
		this.size = size;
	}

	public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }
    
    protected void setListNotLinkedId(Integer listNotLinkedId) {
		this.listNotLinkedId = listNotLinkedId;
	}
    
    protected void setTotalConvertedFiles(long totalConvertedFiles) {
		this.totalConvertedFiles = totalConvertedFiles;
	}
    
    protected void setTotalValidatedFiles(long totalValidatedFiles) {
		this.totalValidatedFiles = totalValidatedFiles;
	}

	public Integer getHarvesting() {
		return harvesting;
	}

	public void setHarvesting(Integer harvesting) {
		this.harvesting = harvesting;
	}
	
	public long getTotalRecordsConvertedToESEFiles(){
		return this.totalRecordsConvertedToESEFiles;
	}

    public Long getTotalIndexedUnits(){
		return this.totalIndexedUnits;
	}

    protected void setTotalIndexedUnits(long totalIndexedUnits) {
		this.totalIndexedUnits = totalIndexedUnits;
	}

	public long getTotalRecordsDeliveredToEuropeana(){
    	return this.totalRecordsDeliveredToEuropeana;
    }
	
	protected void setTotalConvertedToESEFiles(long totalConvertedToESEFiles) {
		this.totalConvertedToESEFiles = totalConvertedToESEFiles;
	}

	protected void setTotalDeliveredToEuropeanaFiles(long totalDeliveredToEuropeanaFiles) {
		this.totalDeliveredToEuropeanaFiles = totalDeliveredToEuropeanaFiles;
	}

	protected void setTotalRecordsConvertedToESEFiles(long totalRecordsConvertedToESEFiles) {
		this.totalRecordsConvertedToESEFiles = totalRecordsConvertedToESEFiles;
	}

	protected void setTotalRecordsDeliveredToEuropeana(long totalRecordsDeliveredToEuropeana) {
		this.totalRecordsDeliveredToEuropeana = totalRecordsDeliveredToEuropeana;
	}

	protected void setNbOfConvertedFiles(int nbOfConvertedFiles) {
		this.nbOfConvertedFiles = nbOfConvertedFiles;
	}

	protected void setNbOfValidatedFiles(int nbOfValidatedFiles) {
		this.nbOfValidatedFiles = nbOfValidatedFiles;
	}

	protected void setNbOfIndexedFiles(int nbOfIndexedFiles) {
		this.nbOfIndexedFiles = nbOfIndexedFiles;
	}

	protected void setNbOfConvertedToEuropeanaFiles(int nbOfConvertedToEuropeanaFiles) {
		this.nbOfConvertedToEuropeanaFiles = nbOfConvertedToEuropeanaFiles;
	}

	protected void setNbOfDeliveredToEuropeanaFiles(int nbOfDeliveredToEuropeanaFiles) {
		this.nbOfDeliveredToEuropeanaFiles = nbOfDeliveredToEuropeanaFiles;
	}

	/**
     * Provide the action JSP with the default xsl file
     * @return The "default.xsl" which is always present in the first place of this list
     */
	public String getXslFilesChoice(){
        return xslFiles.get(0);
    }
    
	public List<CLevel> getListOfFindingAidsOut(){
		return this.listOfFindingAidsOut;
	}
	
	public List<FindingAid> getListOfFindingAidsIndexed(){
		return this.listOfFindingAidsIndexed;
	}
	
	public List<FindingAid> getListOfFindingAidsNotIndex(){
		return this.listOfFindingAidsNotIndex;
	}

	public Integer getIndexing() {
		return indexing;
	}

	public void setIndexing(Integer indexing) {
		this.indexing = indexing;
	}



	public boolean isBatchProcessingEnabled() {
		return batchProcessingEnabled;
	}






}
