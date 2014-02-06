package eu.apenet.dashboard.archivallandscape;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import eu.apenet.commons.infraestructure.ArchivalInstitutionUnit;
import eu.apenet.commons.infraestructure.CountryUnit;
import eu.apenet.commons.infraestructure.NavigationTree;
import eu.apenet.commons.infraestructure.StrutsNavigationTree;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Lang;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

public class ArchivalLandscapeEditor extends ArchivalLandscapeDynatreeAction {

	private static final long serialVersionUID = -1631997370424365298L;
	private static final String TARGET_ACTION = "getALTree.action";
	private static final String CREATE = "create";
	private static final String SERIE = "series";
	private static final String DELETE = "delete";
	private static final String GET_GROUPS = "get_groups";
	private static final String CHANGE_GROUP = "change_group";
	private static final String MOVE_UP = "move_up";
	private static final String MOVE_DOWN = "move_down";
	private static final String GET_NAMES = "get_alternative_names";
	private static final String CREATE_ALTERNATIVE = "create_alternative_name";
	private static final String DELETE_ALTERNATIVE = "delete_alternative_name";

	private final Logger log = Logger.getLogger(getClass());
	
	private String countryId;
	private List<Lang> langList;
	private Set<String> pathsToBeDeleted;

	private void buildBreadcrumb() {
		super.buildBreadcrumbs();
		this.addBreadcrumb(null,getText("breadcrumb.section.editArchivalLandscape"));
	}

	public List<Lang> getLangList(){
		return this.langList;
	}

	@Override
	public String execute() throws Exception {
		buildBreadcrumb();
		SecurityContext securityContext = SecurityContext.get();
		Integer couId = securityContext.getCountryId();
		this.countryId = couId.toString();
		this.langList = DAOFactory.instance().getLangDAO().findAll();
		Collections.sort(this.langList);
		log.info("Archival Landscape editor for country: "+couId);
		buildBreadcrumbs();
		return SUCCESS;
	}
	
	public String launchArchivalInstitutionActions(){
		Writer writer = null;
		try{
			log.debug("Archival landscape edition-action launched");
			getServletRequest().setCharacterEncoding(UTF8);
			getServletResponse().setCharacterEncoding(UTF8);
			getServletResponse().setContentType("application/json");
			writer = new OutputStreamWriter(getServletResponse().getOutputStream(),UTF8);
			String action = getServletRequest().getParameter("action");
			if(action.equals(CREATE)){
				String name = getServletRequest().getParameter("name");
				String father = getServletRequest().getParameter("father");
				String type = getServletRequest().getParameter("type");
				String lang = getServletRequest().getParameter("lang");
				if(father!=null && !father.isEmpty() && (father.contains("aigroup_") || father.contains("country_"))){
					father = father.contains("country_")?null:father.substring("aigroup_".length());
					log.info("Archival landscape, create node hanging of "+father+" called.");
					writer.write(createArchivalInstitution(name,father,type,lang));
				}else{
					log.info("Bad father trying to edit archival landscape. Father-> "+father);
				}
			}else if(action.equals(DELETE)){
				String aiId = getServletRequest().getParameter("aiId");
				log.debug("Archival landscape, delete process for id "+aiId+" has been launched.");
				writer.write(deleteArchivalInstitution(aiId));
			}else if(action.equals(GET_GROUPS)){
				String aiId = getServletRequest().getParameter("aiId");
				aiId = aiId.substring(aiId.indexOf("_")+1);
				writer.write(getAllCountryGroups(new Integer(aiId)));
			}else if(action.equals(CHANGE_GROUP)){
				String aiId = getServletRequest().getParameter("aiId");
				if(aiId!=null && aiId.contains("_")){
					aiId = aiId.substring(aiId.indexOf("_")+1);
					String parentId = getServletRequest().getParameter("groupSelected");
					if(!parentId.equals(aiId)){
						writer.write(changeGroup(new Integer(aiId),parentId));
					}else{
						log.debug("Could not put parent group like himself.");
					}
				}
			}else if(action.equals(MOVE_UP)){
				String aiId = getServletRequest().getParameter("aiId");
				if(aiId!=null && aiId.contains("_")){
					aiId = aiId.substring(aiId.indexOf("_")+1);
					writer.write(moveOrder(new Integer(aiId),true));
				}
			}else if(action.equals(MOVE_DOWN)){
				String aiId = getServletRequest().getParameter("aiId");
				if(aiId!=null && aiId.contains("_")){
					aiId = aiId.substring(aiId.indexOf("_")+1);
					writer.write(moveOrder(new Integer(aiId),false));
				}
			}else if(action.equals(GET_NAMES)){
				String aiId = getServletRequest().getParameter("aiId");
				if(aiId!=null && aiId.contains("_")){
					aiId = aiId.substring(aiId.indexOf("_")+1);
					writer.write(getAlternativeNames(new Integer(aiId)));
				}
			}else if(action.equals(CREATE_ALTERNATIVE)){
				String aiId = getServletRequest().getParameter("aiId");
				if(aiId!=null && aiId.contains("_")){
					aiId = aiId.substring(aiId.indexOf("_")+1);
					String lang = getServletRequest().getParameter("lang");
					String name = getServletRequest().getParameter("name");
					writer.write(createAlternativeNames(new Integer(aiId),name,lang));
				}
			} else if(action.equals(ArchivalLandscapeEditor.DELETE_ALTERNATIVE)){
				String aiId = getServletRequest().getParameter("aiId");
				if(aiId!=null && aiId.contains("_")){
					aiId = aiId.substring(aiId.indexOf("_")+1);
					String lang = getServletRequest().getParameter("lang");
					String name = getServletRequest().getParameter("name");
					writer.write(deleteAlternativeNames(new Integer(aiId),name,lang));
				}
			}
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}finally{
			try{
				if(writer!=null){
					writer.flush();
					writer.close();
				}
			}catch(IOException e){
				log.error(e.getMessage(),e);
			}
		}
		return null;
	}
	
	private String createAlternativeNames(Integer aiId,String name,String lang) {
		StringBuilder buffer = new StringBuilder();
		if(aiId!=null && name!=null && !name.trim().isEmpty() && lang!=null && !lang.trim().isEmpty()){
			// Store in data base the operation, the archival institutions
			JpaUtil.beginDatabaseTransaction();
			
			AiAlternativeNameDAO aiAlternativesNamesDAO = DAOFactory.instance().getAiAlternativeNameDAO();
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			LangDAO langDAO = DAOFactory.instance().getLangDAO();
			Lang language = langDAO.getLangByIsoname(lang.toUpperCase());
			ArchivalInstitution ai = archivalInstitutionDAO.getArchivalInstitution(aiId);
			AiAlternativeName alternativeName = aiAlternativesNamesDAO.findByAIIdandLang(ai,language);
			if (alternativeName == null) {
				alternativeName = new AiAlternativeName();
				alternativeName.setArchivalInstitution(ai);
				alternativeName.setPrimaryName(false);
			}
			if(alternativeName!=null
					&& alternativeName.getPrimaryName() != null
					&& !alternativeName.getPrimaryName()){
				if(alternativeName!=null && alternativeName.getAiAName()!=null){ 
					//name exists, so it's needed an updateSimple operation
					alternativeName.setAiAName(name);
					aiAlternativesNamesDAO.updateSimple(alternativeName);
					buffer.append(buildNode("info",getText("al.message.editedalternativesnamesdone")));
				}else{
					//not exist, so it's needed an insertSimple operation
					alternativeName.setAiAName(name);
					alternativeName.setLang(language);
					aiAlternativesNamesDAO.insertSimple(alternativeName);
					buffer.append(buildNode("info",getText("al.message.alternativenamecreated")));
				}
			}else{
				if (alternativeName!=null
						&& alternativeName.getPrimaryName() != null
						&& alternativeName.getPrimaryName()) {
					buffer.append(buildNode("error",getText("al.message.notchangedprimaryname")));
				} else {
					buffer.append(buildNode("error",getText("al.message.noElementTextPresent")));
				}
			}
			// The final commits
			JpaUtil.commitDatabaseTransaction();
		}else{
			if (name != null
					&& (name.isEmpty()
					|| name.trim().isEmpty())) {
				buffer.append(buildNode("error",getText("al.message.alternativenamemustbefilled")));
			} else {
				buffer.append(buildNode("error",getText("al.message.badarguments")));
			}
		}
		return buffer.toString();
	}

	private String getAlternativeNames(Integer aiId) {
		StringBuffer buffer = new StringBuffer();
		AiAlternativeNameDAO aiAlternativesNamesDAO = DAOFactory.instance().getAiAlternativeNameDAO();
		ArchivalInstitutionDAO aiDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitution = aiDAO.getArchivalInstitution(aiId);
		if(archivalInstitution!=null){
			List<AiAlternativeName> aiAlternativeNames = aiAlternativesNamesDAO.findByAIId(archivalInstitution);
			if(aiAlternativeNames!=null){
				Iterator<AiAlternativeName> itAiAlternativeNames = aiAlternativeNames.iterator();
				AiAlternativeName alternativeName = null;
				buffer.append(START_ITEM);
				buffer.append("\"alternativeNames\":");
				buffer.append(START_ARRAY);
				while(itAiAlternativeNames.hasNext()){
					buffer.append(START_ARRAY);
					alternativeName = itAiAlternativeNames.next();
					// Escape char "
					String aiAName = alternativeName.getAiAName();
					if (aiAName.contains("\"")) {
						aiAName = aiAName.replaceAll("\"", "%22");
					}
					buffer.append(buildNode("name",aiAName));
					buffer.append(COMMA);
					buffer.append(buildNode("lang",alternativeName.getLang().getIsoname()));
					buffer.append(END_ARRAY);
					if(itAiAlternativeNames.hasNext()){
						buffer.append(COMMA);	
					}
				}
				buffer.append(END_ARRAY);
				buffer.append(END_ITEM);
			}
		}else{
			buffer.append(START_ARRAY);
			buffer.append(buildNode("error","al.message.error.noInstitutions"));
			buffer.append(END_ARRAY);
		}
		return buffer.toString();
	}

	private String moveOrder(Integer aiId, boolean moveUp) {
		StringBuffer buffer = new StringBuffer();
		// Store in data base the operation, the archival institutions
		JpaUtil.beginDatabaseTransaction();
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitutionTarget = aiDao.getArchivalInstitution(aiId);
		if(archivalInstitutionTarget!=null){

			List<ArchivalInstitution> archivalInstitutions = null;
			if (archivalInstitutionTarget.getParentAiId() != null) {
				archivalInstitutions = aiDao.getArchivalInstitutionsByParentAiId(archivalInstitutionTarget.getParentAiId(), false);
			} else {
				archivalInstitutions = aiDao.getRootArchivalInstitutionsByCountryId(archivalInstitutionTarget.getCountryId());
			}
			Iterator<ArchivalInstitution> itArchivalInstitutions = archivalInstitutions.iterator();
			boolean changed = false; //flag to exit
			int lastPosition = archivalInstitutionTarget.getAlorder();
			archivalInstitutionTarget.setAlorder(lastPosition+(moveUp?-1:1));
			while(itArchivalInstitutions.hasNext() && !changed){
				ArchivalInstitution currentArchivalInstitution = itArchivalInstitutions.next();
				if(archivalInstitutionTarget.getAiId()!=currentArchivalInstitution.getAiId()){
					int alOrder = currentArchivalInstitution.getAlorder();
					if(alOrder==((moveUp)?lastPosition-1:lastPosition+1)){
						currentArchivalInstitution.setAlorder(lastPosition);
						changed = true;
					}
				}
			}
			if(changed){
				// The final commits
				JpaUtil.commitDatabaseTransaction();
				buffer.append(buildNode("info",getText("al.message.orderchanged")));
			}else{
				// rollback
				JpaUtil.rollbackDatabaseTransaction();
				buffer.append(buildNode("error",getText("al.message.ordernotchanged")));
			}
		}
		return buffer.toString();
	}

	private String changeGroup(Integer aiId,String internalParentId) {
		StringBuffer buffer = new StringBuffer();
		// Store in data base the operation, the archival institutions
		JpaUtil.beginDatabaseTransaction();
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ArchivalInstitution archivalInstitutionTarget = aiDao.getArchivalInstitution(aiId);
		ArchivalInstitution lastParent = null;
		if(aiId!=null){
			ArchivalInstitution parentArchivalInstitution = aiDao.getArchivalInstitutionByInternalAlId(internalParentId,archivalInstitutionTarget.getCountryId()); //search for new parent
			if(!archivalGroupIsParentOf(archivalInstitutionTarget,parentArchivalInstitution)){
				if(parentArchivalInstitution!=null || internalParentId.equals(archivalInstitutionTarget.getCountry().getCname())){
					lastParent = archivalInstitutionTarget.getParent();
					if(internalParentId.equals(archivalInstitutionTarget.getCountry().getCname())){
						parentArchivalInstitution = null;
					}
					//last step, reorder old tree nodes and put the current position at the end of the new parent
					int aloOrder = 0;
					int oldOrder = 0;
					if(parentArchivalInstitution!=null){
						Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(parentArchivalInstitution.getChildArchivalInstitutions());
						if(children!=null){
							aloOrder = children.size();
							oldOrder = archivalInstitutionTarget.getAlorder();
							archivalInstitutionTarget.setAlorder(aloOrder);
						}
					}else{
						List<ArchivalInstitution> children = aiDao.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),true);
						if(children!=null){
							aloOrder = children.size();
							oldOrder = archivalInstitutionTarget.getAlorder();
							archivalInstitutionTarget.setAlorder(aloOrder);
						}
					}
					archivalInstitutionTarget.setParent(parentArchivalInstitution);
					List<ArchivalInstitution> siblings = null;
					if(lastParent!=null){
						Set<ArchivalInstitution> tempSiblings = new LinkedHashSet<ArchivalInstitution>(lastParent.getChildArchivalInstitutions());
						if(tempSiblings!=null){
							siblings = new ArrayList<ArchivalInstitution>(tempSiblings);
						}
					}else{
						siblings = aiDao.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),true);
					}
					if(siblings!=null){
						Iterator<ArchivalInstitution> itSiblings = siblings.iterator();
						while(itSiblings.hasNext()){
							ArchivalInstitution aiTemp = itSiblings.next();
							if(oldOrder<aiTemp.getAlorder() && aiTemp.getAiId()!=archivalInstitutionTarget.getAiId()){
								aiTemp.setAlorder(aiTemp.getAlorder()-1);
								aiDao.updateSimple(aiTemp);
							}
						}
					}
					aiDao.updateSimple(archivalInstitutionTarget);
					buffer.append("[");
					buffer.append(buildNode("info",getText("al.message.groupchanged")));
//					if(parentArchivalInstitution!=null){
						buffer.append(COMMA);
						buffer.append(buildParentsNode(parentArchivalInstitution));
//					}
					buffer.append("]");
				}
			}else{
				buffer.append(buildNode("error",getText("al.message.grouptargetisparent"))); 
			}
		}
		// The final commits
		JpaUtil.commitDatabaseTransaction();
		return buffer.toString();
	}

	private String buildParentsNode(ArchivalInstitution parentArchivalInstitution) {
		StringBuffer parents = new StringBuffer();
		if(parentArchivalInstitution!=null){
			parents.append("aigroup_"+parentArchivalInstitution.getAiId());
			while(parentArchivalInstitution.getParentAiId()!=null){
				parentArchivalInstitution = parentArchivalInstitution.getParent();
				parents.append(",");
				parents.append("aigroup_"+parentArchivalInstitution.getAiId());
			}
			parents.append(",");
		}
		parents.append("country_"+SecurityContext.get().getCountryId());
		return buildNode("newparents",parents.toString());
	}

	private String getAllCountryGroups(Integer aiId) {
		StringBuilder institutions = new StringBuilder();
		Integer couId = SecurityContext.get().getCountryId();
		ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
		//List<ArchivalInstitution> archivalInstitutions = new LinkedList<ArchivalInstitution>(archivalInstitutionDAO.getArchivalInstitutionsGroupsByCountryId(couId));
		List<ArchivalInstitution> archivalInstitutions = new LinkedList<ArchivalInstitution>(archivalInstitutionDAO.getArchivalInstitutionsGroupsByCountryId(couId,false,true));
		archivalInstitutions.addAll(new LinkedList<ArchivalInstitution>(archivalInstitutionDAO.getArchivalInstitutionsGroupsByCountryId(couId,true,true)));
		ArchivalInstitution ai = null;
		if(aiId!=null){
			ai = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(aiId);
		}
		Iterator<ArchivalInstitution> it = archivalInstitutions.iterator();
		institutions.append(START_ARRAY);
		boolean first = true;
		while(it.hasNext()){
			ArchivalInstitution aiTemp = it.next();
			if(first){
				institutions.append(START_ARRAY);
				institutions.append(buildNode("name",aiTemp.getCountry().getCname()));
				institutions.append(COMMA);
				institutions.append(buildNode("key",aiTemp.getCountry().getCname()));
				if(ai.getParent()==null){
					institutions.append(COMMA);
					institutions.append(buildNode("disabled","true"));
				}
				institutions.append(END_ARRAY);
			}
			institutions.append(COMMA);
			first = false;
			institutions.append(START_ARRAY);
			institutions.append(buildNode("name",aiTemp.getAiname()));
			institutions.append(COMMA);
			institutions.append(buildNode("key",aiTemp.getInternalAlId()));
			if(ai!=null && (archivalGroupIsParentOf(ai,aiTemp) || aiTemp.getInternalAlId().equals(ai.getInternalAlId()) || (ai.getParent()!=null && aiTemp.getInternalAlId().equals(ai.getParent().getInternalAlId())))){
				institutions.append(COMMA);
				institutions.append(buildNode("disabled","true"));
			}
			institutions.append(END_ARRAY);
		}
		institutions.append(END_ARRAY);
		return institutions.toString();
	}

	private String deleteArchivalInstitution(String aiId) throws IOException {
		StringBuilder messenger = new StringBuilder();
		if(aiId!=null){
			// Store in data base the operation, the archival institutions
			JpaUtil.beginDatabaseTransaction();
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
				ArchivalInstitution ai = aiDao.findById(new Integer(aiId));
				//update the rest of the orders (all siblings are inconsistents)
				int oldOrder = ai.getAlorder();
				ArchivalInstitution parent = ai.getParent();
				Iterator<ArchivalInstitution> childrenIt = null; 
				if(parent!=null){
					Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(parent.getChildArchivalInstitutions());
					childrenIt = children.iterator();
				}else{ //parent is country
					List<ArchivalInstitution> children = aiDao.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(), true);
					childrenIt = children.iterator();
				}
				while(childrenIt.hasNext()){
					ArchivalInstitution childArchivalInstitution = childrenIt.next();
					if (childArchivalInstitution != null) {
						if(childArchivalInstitution.getAlorder()>oldOrder){ //reduce one
							childArchivalInstitution.setAlorder(childArchivalInstitution.getAlorder()-1);
							aiDao.updateSimple(childArchivalInstitution); //updateSimple
						}
					}
				}
				boolean rollback = false;
				if(!ContentUtils.containsEads(ai)){
					if(ai.isGroup()){
						Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(ai.getChildArchivalInstitutions());
						if(children!=null && children.size()>0){
							// #981: Check if childrens has content.
							if (ai.isContainSearchableItems()) {
								messenger.append(buildNode("error",getText("al.message.grouphaschildren")));
								rollback = true;
							}
						}
					}
					if(!rollback){
						// Recover each child to delete the EAG file.
						if (ai.isGroup() 
								&& ai.getChildArchivalInstitutions() != null
								&& !ai.getChildArchivalInstitutions().isEmpty()) {
							this.deleteAIChild(ai);
						} else {
							String path = ArchivalLandscape.deleteContent(ai);
							if(path!=null){
								if(this.pathsToBeDeleted==null){ this.pathsToBeDeleted = new HashSet<String>();}
								this.pathsToBeDeleted.add(path);
							}
						}
//						aiDao.deleteSimple(ai); //deleteSimple institution
						messenger.append(buildNode("info",getText("al.message.institutiondeleted")));
					}
				}else{
					messenger.append(buildNode("error",getText("al.message.institutionhascontentnotdeleted")));
					rollback = true;
				}
				if(!rollback){ // The final commits
					JpaUtil.commitDatabaseTransaction();
					removePathsToBeDeleted();
				}else{ //undo changes
					JpaUtil.rollbackDatabaseTransaction();
					rollbackDeletedPaths();
				}
		}
		return messenger.toString();
	}
	
	private void removePathsToBeDeleted() throws IOException {
		if(this.pathsToBeDeleted!=null){
			Iterator<String> itPaths = this.pathsToBeDeleted.iterator();
			String subDir = APEnetUtilities.getConfig().getRepoDirPath();
			while(itPaths.hasNext()){
				String path = itPaths.next();
				log.debug("Delete operation was ok, deleting _old directory...");
				FileUtils.deleteDirectory(new File(subDir+path));
				log.debug("Done!! Finished.");
			}
		}
	}
	
	private void rollbackDeletedPaths() throws IOException {
		if(this.pathsToBeDeleted!=null){
			Iterator<String> itPaths = this.pathsToBeDeleted.iterator();
			String subDir = APEnetUtilities.getConfig().getRepoDirPath();
			while(itPaths.hasNext()){
				String path = itPaths.next();
				log.debug("Rollback detected, reverting _old to original path...");
				FileUtils.moveDirectory(new File(subDir+path),new File(subDir+path.substring(0,path.length()-"_old".length())));
				log.debug("Revert done!");
			}
		}
	}

	/**
	 * Method to delete each ai.
	 *
	 * @param ai
	 */
	private void deleteAIChild(ArchivalInstitution ai) {
		if (ai.isGroup()) {
			Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(ai.getChildArchivalInstitutions());
			if (children != null && !children.isEmpty()) {
				Iterator<ArchivalInstitution> childrenIt = children.iterator();
				while (childrenIt.hasNext()) {
					this.deleteAIChild(childrenIt.next());
				}
			}
			
		}
		String path = ArchivalLandscape.deleteContent(ai);
		if(path!=null){
			if(this.pathsToBeDeleted==null){ this.pathsToBeDeleted = new HashSet<String>();}
			this.pathsToBeDeleted.add(path);
		}
	}

	private String createArchivalInstitution(String name,String father,String type,String lang){
		StringBuilder messenger = new StringBuilder();
		if(name!=null && type!=null && !name.trim().isEmpty() && !type.trim().isEmpty()){
			// Store in data base the operation, the archival institutions
			JpaUtil.beginDatabaseTransaction();
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
				ArchivalInstitution archivalInstitution = new ArchivalInstitution();
				archivalInstitution.setAiname(name);
				boolean group = (type.equals(SERIE));
				archivalInstitution.setGroup(group);
				if(father!=null){
					ArchivalInstitution parentAI = aiDao.getArchivalInstitution(new Integer(father));
					if(parentAI!=null){
						archivalInstitution.setParent(parentAI);
						Set<ArchivalInstitution> parentChildren = new LinkedHashSet<ArchivalInstitution>(parentAI.getChildArchivalInstitutions());
						if(parentChildren!=null){
							archivalInstitution.setAlorder(parentChildren.size());
						}
					}
				}else{
					List<ArchivalInstitution> countryChildrens = aiDao.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(), true);
					if(countryChildrens!=null){
						archivalInstitution.setAlorder(countryChildrens.size());
					}
				}
//				archivalInstitution.setPartnerId(SecurityContext.get().getPartnerId());
				archivalInstitution.setInternalAlId(getNewinternalIdentifier());
				archivalInstitution.setCountryId(SecurityContext.get().getCountryId());
				archivalInstitution.setContainSearchableItems(false);
				//put at bottom of the parent, needed the last order value and sum 1
				archivalInstitution = aiDao.insertSimple(archivalInstitution);
				AiAlternativeName alternativeName = new AiAlternativeName();
				alternativeName.setAiAName(name);
				Lang language = DAOFactory.instance().getLangDAO().getLangByIsoname(lang);
				alternativeName.setLang(language);
				alternativeName.setPrimaryName(true);
				alternativeName.setArchivalInstitution(archivalInstitution);
				DAOFactory.instance().getAiAlternativeNameDAO().insertSimple(alternativeName);
				messenger.append(buildNode("info",getText("al.message.elementEdited")));
				log.info("Archival institution/group has been created with name: "+name);
			// The final commits
				JpaUtil.commitDatabaseTransaction();
		}else{
			if (name != null
					&& (name.isEmpty()
					|| name.trim().isEmpty())) {
				messenger.append(buildNode("error",getText("al.message.institutionNameMustBeFilled")));
			} else {
				messenger.append(buildNode("error",getText("al.message.badarguments")));
			}
		}
		return messenger.toString();
	}

	protected static String getNewinternalIdentifier() {
		return "A"+System.currentTimeMillis()+"-"+(new Float(+Math.random()*1000000).toString());
	}

	public String getArchivalInstitutionTree(){
		try{
			log.debug("Building tree for Archival Institution");
			getServletRequest().setCharacterEncoding(UTF8);
			getServletResponse().setCharacterEncoding(UTF8);
			getServletResponse().setContentType("application/json");
			Writer writer = new OutputStreamWriter(getServletResponse().getOutputStream(),UTF8);
			NavigationTree navigationTree = new StrutsNavigationTree();
			List<ArchivalInstitutionUnit> archivalInstitutionList = null;
			String nodeId = getServletRequest().getParameter("nodeId");
			StringBuffer stringBuffer = null; 
			if(nodeId!=null){
				log.debug("Building tree for nodeId: "+nodeId);
				archivalInstitutionList = navigationTree.getArchivalInstitutionsByParentAiId(nodeId);
			}else if(getServletRequest().getParameter("couId")!=null){
				List<CountryUnit> countryList = new ArrayList<CountryUnit>(); 
				Integer couId = new Integer(getServletRequest().getParameter("couId"));
				countryList.add(navigationTree.getCountry(couId));
				log.debug("Building tree for countryId: "+couId);
				stringBuffer = generateCountriesTreeJSON(countryList);
			}else{ //never should go into this else, it's the same like the first time which is called a lazy of archival landscape preview, but only for countries
				SecurityContext securityContext = SecurityContext.get();
				Integer couId = securityContext.getCountryId();
				archivalInstitutionList = navigationTree.getArchivalInstitutionsByParentAiId("country_"+couId.toString());
				log.debug("Got archival institution list by country: "+couId);
			}
			if(stringBuffer!=null){
				writer.write(stringBuffer.toString());
			}else{
				Collections.sort(archivalInstitutionList);
				writer.write(generateArchivalInstitutionPartJSON(archivalInstitutionList).toString());
			}
			writer.close();
		}catch(Exception e){
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	public StringBuffer generateArchivalInstitutionPartJSON(List<ArchivalInstitutionUnit> archivalInstitutionList) {
		StringBuffer tree = null;
		try {
			tree = generateArchivalInstitutionsTreeJSON(archivalInstitutionList,TARGET_ACTION,getServletRequest().getLocale().getDisplayLanguage(),true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return tree;
	}
	
	public String getArchivalInstitutionPossibleActions(){
		Writer writer = null;
		try {
			getServletRequest().setCharacterEncoding(UTF8);
			getServletResponse().setCharacterEncoding(UTF8);
			getServletResponse().setContentType("application/json");
			writer = new OutputStreamWriter(getServletResponse().getOutputStream(),UTF8);
			String nodeKey = getServletRequest().getParameter("nodeKey");
			if(nodeKey!=null && nodeKey.contains("_")){
				String[] keys = nodeKey.split("_");
				String key = keys[0];
				StringBuilder response = new StringBuilder();
				if(key.equals("aieag") || key.equals("aigroup")){
					ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(new Integer(keys[1]));
					response = buildActions(archivalInstitution);
				}else if(key.equals("country")){
					response = buildActions(null);
				}
				writer.write(response.toString());
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			if(writer!=null){
				try {
					writer.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return null;
	}
	/**
	 * appendInstitution - launch the Name, Select an element type, 
	 * select a language and "add to the list" form part to allow 
	 * create new institutions/groups 
	 */
	private StringBuilder buildActions(ArchivalInstitution archivalInstitution) {
		
		StringBuilder response = new StringBuilder();
		response.append("[");
		String actionName = "";
		String actionValue = "";
		actionName = "enableAddToList";
		if(archivalInstitution == null || archivalInstitution.isGroup()){
			actionValue = "true";
		}else{
			actionValue = "false";
		}
		response.append(buildNode(actionName,actionValue));
		if(response.length()>1){ 
			response.append(",");
		}
		actionName = "showAlternatives";
		Set<AiAlternativeName> alternativesNames = (archivalInstitution!=null)?archivalInstitution.getAiAlternativeNames():null;
		if(alternativesNames!=null && alternativesNames.size()>0){
			actionValue = "true";
		}else{
			actionValue = "false";
		}
		response.append(buildNode(actionName,actionValue));
		actionName = "showMoveDeleteActions";
		actionValue = "false";
		if(archivalInstitution!=null){
			actionValue = "true";
		}
		if(response.length()>1){ 
			response.append(",");
		}
		response.append(buildNode(actionName,actionValue));
		actionName = "showDeleteAction";
		if(archivalInstitution!=null && !ContentUtils.containsEads(archivalInstitution)){
			actionValue = "true";
		}else{
			actionValue = "false";
		}
		if(response.length()>1){ 
			response.append(",");
		}
		response.append(buildNode(actionName,actionValue));

		if (archivalInstitution != null) {
			actionName = "hasContentPublished";
			actionValue = "";
			if (archivalInstitution.isContainSearchableItems()) {
				actionValue = getText("al.message.notchangedparent");
			} else {
				actionValue = "false";
			}
			if(response.length()>1){ 
				response.append(",");
			}
			response.append(buildNode(actionName,actionValue));
			actionName = "canBeMoved";
			if (archivalInstitution.isContainSearchableItems()) {
				actionValue = "false";
			} else {
				actionValue = "true";
			}
			if(response.length()>1){
				response.append(",");
			}
			response.append(buildNode(actionName,actionValue));
			actionName = "mainAlternativeName";
			actionValue = "";
			AiAlternativeNameDAO aiAlternativesNamesDAO = DAOFactory.instance().getAiAlternativeNameDAO();
			List<AiAlternativeName> aiANameList= aiAlternativesNamesDAO.findByAIId(archivalInstitution);
			for (int i = 0; i < aiANameList.size(); i++) {
				AiAlternativeName aiAlternativeName = aiANameList.get(i);
				if (aiAlternativeName.getPrimaryName()) {
					actionValue = aiAlternativeName.getLang().getIsoname();
				}
			}
			if(actionValue!=null && !actionValue.isEmpty()){
				if(response.length()>1){ //take into account put ',' if necesary
					response.append(",");
				}
				response.append(buildNode(actionName,actionValue));
			}
		}

		response.append("]");
		return response;
	}

	private String buildNode(String actionName, String actionValue) {
		String node = "";
		if(actionName!=null && actionValue!=null && !actionName.isEmpty() && !actionValue.isEmpty()){
			if(actionName.contains("\"")){
				actionName.replace("\"","&quote;");
			}
			if(actionValue.contains("\"")){
				actionValue.replace("\"","&quote;");
			}
			node = "{\""+actionName+"\":\""+actionValue+"\"}";
		}
		return node;
	}
	
	private boolean archivalGroupIsParentOf(ArchivalInstitution source,ArchivalInstitution destination){
		boolean isParent = false;
		if(source!=null && destination!=null){
			Country country = source.getCountry();
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			if(source.isGroup() && destination.isGroup() && country.equals(destination.getCountry())){
				int aiId = source.getAiId();
				while(destination.getParentAiId()!=null && !isParent){
					if(destination.getParentAiId()!=aiId){
						 destination = archivalInstitutionDAO.getArchivalInstitution(destination.getParentAiId());
					}else{
						isParent = true;
					}
				}
			}
		}
		return isParent;
	}
	
	public String getCountryId() {
		return countryId;
	}

	private String deleteAlternativeNames(Integer aiId,String name,String lang) {
		StringBuilder buffer = new StringBuilder();
		if(aiId!=null && name!=null && lang!=null){
			// Store in data base the operation, the archival institutions
			JpaUtil.beginDatabaseTransaction();
			
			AiAlternativeNameDAO aiAlternativesNamesDAO = DAOFactory.instance().getAiAlternativeNameDAO();
			ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
			LangDAO langDAO = DAOFactory.instance().getLangDAO();
			Lang language = langDAO.getLangByIsoname(lang.toUpperCase());
			ArchivalInstitution ai = archivalInstitutionDAO.getArchivalInstitution(aiId);
			AiAlternativeName alternativeName = aiAlternativesNamesDAO.findByAIIdandLang(ai,language);

			if(alternativeName!=null && alternativeName.getPrimaryName() != null && !alternativeName.getPrimaryName()){
				if(alternativeName!=null && alternativeName.getAiAName()!=null){ 
					//name exists, so it's needed a deleteSimple operation
					alternativeName.setAiAName(name);
					aiAlternativesNamesDAO.deleteSimple(alternativeName);
					buffer.append(buildNode("info",getText("al.message.alternativenameremoved")));
				}
			}else{
				if (alternativeName!=null && alternativeName.getPrimaryName() != null && alternativeName.getPrimaryName()) {
					buffer.append(buildNode("error",getText("al.message.cannotremovefirstalternativename")));
				} else {
					buffer.append(buildNode("error",getText("al.message.badalternativename")));
				}
			}
			// The final commits
			JpaUtil.commitDatabaseTransaction();
		}else{
			buffer.append(buildNode("error",getText("al.message.badarguments")));
		}
		return buffer.toString();
	}

}
