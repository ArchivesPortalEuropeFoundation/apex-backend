package eu.apenet.dashboard.archivallandscape;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import eu.apenet.commons.types.XmlType;
import org.apache.commons.io.FileUtils;
import org.hibernate.HibernateException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;

/**
 * Edit Archival Landscape Action
 * User: Jara Alvarez
 * Date: Sep 8th, 2010
 *
 */
public class EditAction extends eu.apenet.dashboard.archivallandscape.AbstractEditAction {

	private Integer partnerId;
	private List<ArchivalInstitution> archivalInstitutionsToDelete= new ArrayList<ArchivalInstitution>();
	private List<FindingAid> fasDeleted = new ArrayList<FindingAid>();
	private List<HoldingsGuide> hgsDeleted = new ArrayList<HoldingsGuide>();
	private String editTarget;
	private List<String> elementLanguages;
	private String target;
	private String languageTarget;
	
	public List<FindingAid> getFasDeleted() {
		return fasDeleted;
	}

	public void setFasDeleted(List<FindingAid> fasDeleted) {
		this.fasDeleted = fasDeleted;
	}

	public List<HoldingsGuide> getHgsDeleted() {
		return hgsDeleted;
	}

	public void setHgsDeleted(List<HoldingsGuide> hgsDeleted) {
		this.hgsDeleted = hgsDeleted;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}	

	public List<String> getElementLanguages() {
		return elementLanguages;
	}
	
	public void setTarget(String target){
		this.target = target;
	}
	
	public String getTarget(){
		return this.target;
	}
	
	public void setLanguageTarget(String languageTarget){
		this.languageTarget = languageTarget;
	}
	
	public List<ArchivalInstitution> getArchivalInstitutionsToDelete() {
		return archivalInstitutionsToDelete;
	}

	public void setArchivalInstitutionsToDelete(
			List<ArchivalInstitution> archivalInstitutionsToDelete) {
		this.archivalInstitutionsToDelete = archivalInstitutionsToDelete;
	}

	public void setEditTarget(String editTarget){
		this.editTarget = editTarget;
	}
	
	/**
	 * When the AL is being adding the first time. It controls the first 
	 * form when it's pushed the add button.
	 * @return String
	 */
	public String addArchivalLandscape(){
		try {
			makeTemporal();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * Action which controls the moving up of an Institution
	 * while it's edited
	 * 
	 * @return String
	 */
	public String upAnInstitution(){
		return moveAnInstitution(true);
	}
	
	/**
	 * Action which controls the moving down of an Institution
	 * while it's edited.
	 * 
	 * @return String
	 */
	public String downAnInstitution(){
		return moveAnInstitution(false);
	}
	
	public String moveANodeToOtherPlaces(){
		ArchivalLandscape a = new ArchivalLandscape();
		this.setCountry(a.getmyCountryName());
		if(getTextAL()==null || getElement()==null){
			setList(EditArchivalLandscapeLogic.navigate(true));
		}else{
			setList(EditArchivalLandscapeLogic.navigate(false));
		}
		String cadena = null;
		if (getALElement()!=null) {
			cadena = getALElement().toString();
		}
		//Looking forward the institution in the list
		Institution insti = null;
		insti = EditArchivalLandscapeLogic.localizateInstitution(getList(),cadena);
		try{
			Document doc = null;
			String path = a.getmyPath(a.getmyCountry()) + "tmp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + "AL.xml";
			DocumentBuilder docBuilder = fillCountryCIdentifier(path);
			if(insti!=null && getFather()!=null && !getFather().isEmpty()){
				Long counter = EditArchivalLandscapeLogic.countIndexedContentByInstitutionGroupId(insti.getId(), insti.getLevel().equals("series"));
				if(counter!=null && counter<=0){
					doc = changeNodeFather(path,docBuilder,insti);
					log.debug("Starting saving in the temp file...");
					TransformerFactory tf = TransformerFactory.newInstance();
			        Transformer transformer = tf.newTransformer();
			        transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
			        log.debug("Saved in the temp file");
			        setList(EditArchivalLandscapeLogic.navigate(false));
			        parseList(getList(),null);
				}else{
					setList(EditArchivalLandscapeLogic.navigate(false));
					parseList(getList(),null);
					addActionMessage(getText("al.message.institutionhascontentindexed"));
					return SUCCESS;
				}
			}else{
				setList(EditArchivalLandscapeLogic.navigate(false));
				parseList(getList(),null);
				addActionMessage(getText("al.message.noElementSelectedToMove"));
				return SUCCESS;
			}
		}catch(Exception e){
			log.error("Moving a Node - "+e.getMessage());
		}
		return SUCCESS;
	}

	/**
	 * Method which control the second form when it's pressed the Finish
	 * button.
	 * @return String
	 * @throws IOException 
	 * @throws TransformerException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws XMLStreamException 
	 */
	public String appendArchivalLandscape() throws IOException, XMLStreamException, ParserConfigurationException, SAXException, TransformerException{
		ArchivalLandscape a = new ArchivalLandscape();
		this.setCountry(a.getmyCountryName());
		if(getTextAL()==null || getElement()==null){
			setList(EditArchivalLandscapeLogic.navigate(true));
		}else{
			setList(EditArchivalLandscapeLogic.navigate(false));
		}
		Boolean state = null;
		if(getALElement()!=null){
			state = navigateToUploadList(getList());
		}
		String path = a.getmyPath(a.getmyCountry()) + "tmp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + "AL.xml";
		fillCountryCIdentifier(path);
		if(state==null && getALElement()!=null){
			return ERROR; //NOT SAVED
		}else if(getALElement()!=null && !state){
			return ERROR; //NOT SAVED
		}else if((getALElement()!=null && state) || (getList()!=null && getList().size()==0) || (getALElement()==null && getTextAL()!=null && getTextAL().length()>0) ){ //AL should be distinct of null
			List<Institution> tempAL = getAL();
			tempAL.clear();
			setAL(tempAL);
			writeList(getList(),path);
			setList(EditArchivalLandscapeLogic.navigate(false)); //It has to be here because write is the last writer 
		}
		parseList(getList(),null); //Cipher is the start number(1.x in this case)
		return SUCCESS;		//END UPLOAD LIST
	}
	
	/**
	 * 
	 * @return String
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public String seeLanguagesForArchivalLandscapeElement(){
		//First caught the element selected.
		ArchivalLandscape a = new ArchivalLandscape();
		this.setCountry(a.getmyCountryName());
		String path = a.getmyPath(a.getmyCountry())+"tmp"+APEnetUtilities.FILESEPARATOR+a.getmyCountry()+"AL.xml";
		try {
			fillCountryCIdentifier(path);
		} catch (Exception e){
			log.error("Fill countryCIdentifier exception: "+e.getMessage(),e);
		}
		if(getALElement()!=null){
			try{
				DocumentBuilder db = EditArchivalLandscapeLogic.buildDocument();
				Document doc = db.parse(path);
				doc.normalizeDocument();
				doc.normalize();
				NodeList cList = doc.getElementsByTagName("c");
				Node nodeTemp = null;//doc.getElementById(this.ALElement);
				for(int i=0;i<cList.getLength() && nodeTemp==null;i++){
					if(cList.item(i).hasAttributes() && cList.item(i).getAttributes().getNamedItem("id")!=null && cList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(getALElement())){
						nodeTemp = cList.item(i);
					}
				}
				if(nodeTemp!=null){
					this.target = getALElement();
					NodeList childrenBefore = nodeTemp.getChildNodes();
					for(int i=0;i<childrenBefore.getLength();i++){
						if(childrenBefore.item(i).getNodeName().equals("did")){
							NodeList children = childrenBefore.item(i).getChildNodes();
							this.elementLanguages = new ArrayList<String>();
							for(int x=0;x<children.getLength();x++){
								if(children.item(x).getNodeName().equals("unittitle")){
									this.elementLanguages.add(children.item(x).getAttributes().getNamedItem("type").getTextContent()+" - "+children.item(x).getTextContent());
								}
							}
						}
					}
				}
			}catch(Exception e){
				log.error(e.getMessage(),e);
			}
		}else{
			addActionMessage(getText("al.message.noElementSelectedWithAlternativesNames"));
		}
		parseList(EditArchivalLandscapeLogic.navigate(false),null);
		return SUCCESS;
	}
	
	/**
	 * This method controls the action of the second form when user click on
	 * finish button. It write into the XML file (country AL.xml) the data 
	 * selected by user and stored in temp/AL.xml file.
	 * @return String
	 */
	public String finishAddArchivalLandscape() {
		Boolean ddbbResult=true;
		String resultStoreArchives;
		String result=null;
		ArchivalLandscape a = new ArchivalLandscape();
		//Temporary directory where the last archival landscape is changed 
		String tmpDirectory =a.getmyPath(a.getmyCountry())+ "tmp";
		String pathCountryAL = a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL.xml";
		String pathCountryALOld = a.getmyPath(a.getmyCountry()) + a.getmyCountry() + "AL_old.xml";
		//Final country AL.xml file
		File file = new File(tmpDirectory+APEnetUtilities.FILESEPARATOR+a.getmyCountry()+"AL.xml");
		try{
			//Store in data base the operation, the archival institutions
			HibernateUtil.beginDatabaseTransaction();			       

			a.storeOperation("Edited al");
			resultStoreArchives=a.storeArchives(file,true);	
			if (resultStoreArchives.equals("success")){		
					File file2 = new File (pathCountryAL);
					file2.setWritable(true);
					file2.renameTo(new File(pathCountryALOld));					
					log.info("EditAction: archival landscape of " +a.getmyCountry() + " renamed to" + a.getmyCountry() +"_old.xml");
					FileReader in = new FileReader(file);
					FileWriter out = new FileWriter(new File(pathCountryAL));	
					int c;
					while ((c = in.read()) != -1){
					      out.write(c);
					}
					in.close();
					out.close();
					File tmpDirectoryToDelete = new File(tmpDirectory);
					if(tmpDirectoryToDelete.isDirectory()){						
						tmpDirectoryToDelete.delete();
					}
				log.info("EditAction: archival landscape of " +a.getmyCountry() + " overwrited");
				a.changeAL();	

				//The final commits			
			    HibernateUtil.commitDatabaseTransaction();

				result= SUCCESS;
			}
			else{
				String errorMessage = getText("al.message.someErrors");
				addActionMessage(getText("al.message.someErrors"));
				ddbbResult = false;
				log.error(errorMessage);
				result= ERROR;
			}
		} catch (IOException e) {
			ddbbResult=false;
			log.error("The new archival landscape of " +a.getmyCountry() + " could not be overwrited requested by edition process", e);
		} catch (HibernateException e) {
			ddbbResult=false;
			log.error("The new archival landscape of " +a.getmyCountry() + " could not be overwrited requested by edition process", e);
		}
		catch (Exception e) {
			ddbbResult=false;
			log.error("The new archival landscape of " +a.getmyCountry() + " could not be overwrited requested by edition process", e);	
		}finally{		
			if (!ddbbResult)
			{
				log.error("Some operation was not correct in overwriting the AL of the " + a.getmyCountry()+ " requested by edition process. Rollbacking the whole transaction process");
				try {
					this.setArchivalInstitutionsToDelete(a.getArchivalInstitutionsToDelete());
					HibernateUtil.rollbackDatabaseTransaction();
					HibernateUtil.closeDatabaseSession();
					
					//Rollback the repository files AL for this country. Delete the one generated.					
					File oldFile = new File(tmpDirectory + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + "AL.xml");					
					oldFile.delete();
					FileUtils.forceDelete(new File(tmpDirectory));
					//new File(tmpDirectory).delete();
					if (new File(pathCountryALOld).exists())
					{
						new File(pathCountryAL).delete();
						new File(pathCountryALOld).renameTo(new File(pathCountryAL));	
					}
					
					Boolean rollbackFiles = false;
										
					//The general AL file created need to be changed again updating with the former version of this country's AL
					a.changeAL();
					//The repository files renamed to _old must be changed to the former name for rollbacking
					for (int i=0; i< this.archivalInstitutionsToDelete.size();i++){
						//Rename the files in tmp folder
						String tmpDirOldPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId() + "_old";
						String tmpDirPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId();
						File  tmpDirFile = new File (tmpDirOldPath);
						if (tmpDirFile.exists()){
							log.debug("Renaming the tmp folder from the repository related the institution: " + this.archivalInstitutionsToDelete.get(i));
							tmpDirFile.renameTo(new File(tmpDirPath));		
						}
						//Rename the files in Country Repo folder
						String repoDirOldPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId() + "_old";
						String repoDirPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId();
						File repoDirFile = new File(repoDirOldPath);
						if (repoDirFile.exists()){
							log.debug("Renaming the repo folder from the repository related to the institution: " + this.archivalInstitutionsToDelete.get(i));
							repoDirFile.renameTo(new File(repoDirPath));
						}
					}
					//The rollbacking of the files are finished without errors
					rollbackFiles = true;
					
					if (rollbackFiles)
					{
						this.setFasDeleted(a.getFasDeleted());						
						this.setHgsDeleted(a.getHgsDeleted());
						
						//Re-index again the finding aids deleted
						for (int i=0; i< this.fasDeleted.size();i++)
						{
							if (Arrays.asList(FileState.INDEXED_FILE_STATES).contains(this.fasDeleted.get(i).getFileState().getState())) {
								try {
									ContentUtils.indexRollback(XmlType.EAD_FA, this.fasDeleted.get(i).getId());
								} catch (Exception ex) {
									log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + this.fasDeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
								}
								
								//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
								if ((Arrays.asList(FileState.INDEXED_FILE_STATES).contains(this.fasDeleted.get(i).getFileState().getState()) &&(this.fasDeleted.get(i).getFileState().getId()!=8))) {

									try {
										ContentUtils.restoreOriginalStateOfEAD(this.fasDeleted.get(i));
									}
									catch (Exception ex) {
										log.error("Error restoring the original state of the Finding Aid. Check Database");
									}
								}
							}							
						}
						//Re-index again the holding guides deleted
						for (int i=0; i< this.hgsDeleted.size();i++)
						{
							if (Arrays.asList(FileState.INDEXED_FILE_STATES).contains(this.hgsDeleted.get(i).getFileState().getState())) {
								try {
									ContentUtils.indexRollback(XmlType.EAD_HG, this.hgsDeleted.get(i).getId());
								} catch (Exception ex) {
									log.error("FATAL ERROR. Error during Index Rollback [Re-indexing process because of the rollback]. The file affected is " + this.hgsDeleted.get(i).getEadid() + ". Error:" + ex.getMessage());
									log.error("FATAL ERROR. The rollback of index could not be done successfully. A manually review of the AL of: " + a.getmyCountry() + " must be done");
								}
								
								//Due to the re-indexing of this FA, the current state is "Indexed_Not Converted to ESE/EDM". It is necessary to restore the original state if the original state is different from "Indexed_Not Converted to ESE/EDM"
								if ((Arrays.asList(FileState.INDEXED_FILE_STATES).contains(this.hgsDeleted.get(i).getFileState().getState())&&(this.hgsDeleted.get(i).getFileState().getId()!=8))) {

									try {
										ContentUtils.restoreOriginalStateOfEAD(this.hgsDeleted.get(i));
									}
									catch (Exception ex) {
										log.error("Error restoring the original state of the Finding Aid. Check Database");
										log.error("FATAL ERROR. The rollback of index could not be done successfully. A manually review of the AL of: " + a.getmyCountry() + " must be done");
									}
								}
							}							
						}
						

					}
					
				}catch (Exception e) {
					log.error("FATAL ERROR. The rollback of index or in repository could not be done successfully. A manually review of the AL of: " + a.getmyCountry() + " must be done. Error: " + e);	
				}
				addActionMessage(getText("al.message.errorOverwritting"));
				result = ERROR;
			}else{
				try{
					this.setArchivalInstitutionsToDelete(a.getArchivalInstitutionsToDelete());
					//Delete the temporary file (_old) country AL created in overwriting
					File oldFile = new File(pathCountryALOld);
					oldFile.delete();
					//Delete the repository files							
					for (int i=0; i< this.archivalInstitutionsToDelete.size();i++){
						//Delete the files in tmp folder
						String tmpDirOldPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + "tmp" + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId() + "_old";
						File  tmpDirFile = new File (tmpDirOldPath);
						if (tmpDirFile.exists()){
							log.debug("Renaming the tmp folder from the repository related the institution: " + this.archivalInstitutionsToDelete.get(i));
							FileUtils.forceDelete(tmpDirFile);		
						}
						//Rename the files in Country Repo folder
						String repoDirOldPath = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + APEnetUtilities.FILESEPARATOR + this.archivalInstitutionsToDelete.get(i).getAiId() + "_old";
						File repoDirFile = new File(repoDirOldPath);
						if (repoDirFile.exists()){
							log.debug("Renaming the repo folder from the repository related to the institution: " + this.archivalInstitutionsToDelete.get(i));
							FileUtils.forceDelete(repoDirFile);
						}
					}
				}catch(Exception e){
					log.error("The temporary file"+ pathCountryALOld + " or the ones related to the institutions: " + this.archivalInstitutionsToDelete + " could not have been deleted. It should remove it manually", e);
				}
			}
		}
		return result;
	}
	
	/**
	 * It controls the action of the edit button in the first form 
	 * @return String
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 */
	public String modifyArchivalLandscape() throws ParserConfigurationException, SAXException, IOException, TransformerException{
		ArrayList<Institution> list = EditArchivalLandscapeLogic.navigate(false);
		ArchivalLandscape a = new ArchivalLandscape();
		this.setCountry(a.getmyCountryName());
		String path = a.getmyPath(a.getmyCountry()) + "tmp" + APEnetUtilities.FILESEPARATOR + a.getmyCountry() + "AL.xml";
		fillCountryCIdentifier(path);
		if (getALElement() != null){//If there's some element selected in the list
			if(remove(getALElement(),list)){
				list=EditArchivalLandscapeLogic.navigate(false);
				parseList(list,null);
				return SUCCESS;
			}
			else{//Some errors occurs in the deletion process (remove returns FALSE)
				list=EditArchivalLandscapeLogic.navigate(false);
				parseList(list,null);
				addActionMessage(getText("al.message.errorDeletion"));
				log.error("The deletion of the item: " + getALElement() + " from the AL of " + getCountry() + " could not be done");
				return INPUT;
			}
		}else{//There's no element selected from the list
			list=EditArchivalLandscapeLogic.navigate(false);
			parseList(list,null);
			addActionMessage(getText("al.message.noElementSelected"));
			return INPUT;
		}		
	}

	public String editArchivalLandscapeElement(){
		setEdit(true);
		return seeLanguagesForArchivalLandscapeElement();
	}
	
	public String editArchivalLandscapeTarget(){
		try{
			String target = getALElement();
			if(target!=null && this.target!=null){
				ArchivalLandscape a = new ArchivalLandscape();
				String path = a.getmyPath(a.getmyCountry())+"tmp"+APEnetUtilities.FILESEPARATOR+a.getmyCountry()+"AL.xml";
				Document doc = fillCountryCIdentifier(path).parse(path);
				doc.normalizeDocument();
				doc.normalize();
				NodeList cList = doc.getElementsByTagName("c");
				Node nodeTemp = null;//doc.getElementById(target); 
				for(int i=0;i<cList.getLength() && nodeTemp==null;i++){
					if(cList.item(i).hasAttributes() && cList.item(i).getAttributes().getNamedItem("id")!=null && cList.item(i).getAttributes().getNamedItem("id").getNodeValue().equals(getALElement())){
						nodeTemp = cList.item(i);
					}
				}
				NodeList listTemp = nodeTemp.getChildNodes();
				for(int i=0;i<listTemp.getLength();i++){
					Node did = listTemp.item(i);
					if(listTemp.item(i).getNodeName().equals("did")){
						NodeList unittitles = listTemp.item(i).getChildNodes();
						Node nodeToBeReplaced = null;
						for(int x=0;x<unittitles.getLength();x++){
							if(unittitles.item(x).hasAttributes() && unittitles.item(x).getAttributes().getNamedItem("type")!=null && unittitles.item(x).getAttributes().getNamedItem("type").getTextContent().toLowerCase().equals(this.languageTarget.toLowerCase())){
								nodeToBeReplaced = unittitles.item(x);
							}
						}
						Element newLanguageNode = doc.createElement("unittitle");
						newLanguageNode.setTextContent(this.target);
						newLanguageNode.setAttribute("type",this.languageTarget);
						if(nodeToBeReplaced!=null){//if edition of language is not present...
							did.replaceChild(newLanguageNode,nodeToBeReplaced);
							addActionMessage(getText("al.message.elementEdited")+" => "+this.target);
						}else{
							did.appendChild(newLanguageNode);
							addActionMessage(getText("al.message.elementAppended")+" => "+this.target);
						}
						TransformerFactory tf = TransformerFactory.newInstance(); //Save the document with changes
				        Transformer transformer = tf.newTransformer();
				        transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
					}
					
				}
			}else if(this.target!=null){
				addActionMessage(getText("al.message.noElementSelected"));
			}else{
				addActionMessage(getText("al.message.noElementTextPresent"));
			}
		}catch(Exception e){
			log.error("Method editArchivalLandscapeTarget: "+e.getMessage(),e);
		}
		return seeLanguagesForArchivalLandscapeElement();
	}
	
	public String deleteAlternativeNameTarget(){
		try{
			if(this.editTarget!=null){
				String language = null;
				if(this.editTarget.contains("-")){
					language = this.editTarget.substring(0,this.editTarget.indexOf("-")-1);
					this.editTarget = this.editTarget.substring(this.editTarget.indexOf("-")+2);
				}
				ArchivalLandscape a = new ArchivalLandscape();
				String path = a.getmyPath(a.getmyCountry())+"tmp"+APEnetUtilities.FILESEPARATOR+a.getmyCountry()+"AL.xml";
				setCountry(a.getmyCountryName());
				Document doc = fillCountryCIdentifier(path).parse(path);
				doc.normalizeDocument();
				doc.normalize();
				NodeList listTemp = doc.getElementsByTagName("unittitle");
				for(int i=0;i<listTemp.getLength();i++){
					if(listTemp.item(i).getFirstChild().getNodeValue()!=null){
						if(listTemp.item(i).getFirstChild().getNodeValue().trim().equals(this.editTarget)){
							Node did = listTemp.item(i).getParentNode();
							//look for the edition of a language
							NodeList unittitles = did.getChildNodes();
							for(int x=0;x<unittitles.getLength();x++){
								if(unittitles.item(x).hasAttributes() && unittitles.item(x).getAttributes().getNamedItem("type")!=null && unittitles.item(x).getAttributes().getNamedItem("type").getTextContent().toLowerCase().equals(language)){
									if(x>0){
										did.removeChild(unittitles.item(x));
										addActionMessage(getText("al.message.alternativenameremoved"));
										break;
									}else{
										addActionMessage(getText("al.message.cannotremovefirstalternativename"));
										break;
									}
								}
							}
							TransformerFactory tf = TransformerFactory.newInstance(); //Save the document with changes
					        Transformer transformer = tf.newTransformer();
					        transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		parseList(EditArchivalLandscapeLogic.navigate(false),null);
		return SUCCESS;
	}
}