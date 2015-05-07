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
import eu.apenet.dashboard.services.eag.EagService;
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

/**
 * <p>
 * Class used to manage Archival-Landscape edition into Dashboard.
 * </p>
 */
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

	/**
	 * <p>
	 * Method which is override of AbstractAction.
	 * </p>
	 * <p>
	 * It's used to build the breadcrumb route.
	 * </p>
	 */
	private void buildBreadcrumb() {
		super.buildBreadcrumbs();
		this.addBreadcrumb(null,getText("breadcrumb.section.editArchivalLandscape"));
	}

	public List<Lang> getLangList(){
		return this.langList;
	}
	
	/**
	 * <p>
	 * Method which starts archival landscape editor (default action).
	 * </p>
	 * <p>
	 * It's used to render the client browser part (editor).
	 * </p>
	 * 
	 * @return Structs.STATE
	 */
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
	/**
	 * <p>
	 * Method which manages ajax actions.
	 * </p>
	 * <p>
	 * It uses a writer to communicate with client browser.
	 * </p>
	 * <p>
	 * Uses actions CREATE, DELETE, GET_GROUPS, CHANGE_GROUP, CHANGE_GROUP,
	 * MOVE_UP, MOVE_DOWN, GET_NAMES, CREATE_ALTERNATIVE and DELETE_ALTERNATIVE
	 * </p>
	 * 
	 * @return Structs.STATE -> null
	 */
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
	/**
	 * <p>
	 * Method which create alternative-names for an institution.
	 * </p>
	 * <p>
	 * It uses his aiId to locate and append his new alternative name and language.
	 * </p>
	 * <p> 
	 * Launches transactions to the database creating and writing
	 * new alternative names into table (ai_alternative_name) unique by language.
	 * </p>
	 * <p> 
	 * This language is obtained from ddbb table (lang) and must be unique 
	 * by each (archival_institution).
	 * </p>
	 * <p> 
	 * No rollback is included in this method.
	 * </p>
	 * <p>
	 * It builds into a StringBuilder the structured response and returns it in String format.
	 * </p>
	 * 
	 * @param aiId -> Integer archival-institution id
	 * @param name -> String name
	 * @param lang -> String language
	 * @return String - StringBuilder.toString();
	 */
	private String createAlternativeNames(Integer aiId,String name,String lang) {
		StringBuilder buffer = new StringBuilder();
		if(aiId!=null && name!=null && !name.trim().isEmpty() && lang!=null && !lang.trim().isEmpty()){
			// Store in data base the operation, the archival institutions
			JpaUtil.beginDatabaseTransaction();
			name = name.replaceAll("[\\s+&&[^\\n]]+"," ") //1. reduce all non-newline whitespaces to a unique space
				    .replaceAll("(?m)^\\s+|\\s$","") //2. remove spaces from start or end of the lines
				    .replaceAll("\\n+"," "); //3. remove all newlines, compress it in a unique line
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
			JpaUtil.commitDatabaseTransaction(); //commit to the database
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

	/**
	 * <p>
	 * Return the ajax response (JSON) in a String which contains
	 * alternatives names stored into DDBB for an institution.
	 * </p>
	 * <p>
	 * Uses the target aiId of an existing institution.
	 * </p>
	 * <p>
	 * It builds into a StringBuilder the structured response and returns it in String format.
	 * </p>
	 * 
	 * @param aiId -> Integer archival-institution id
	 * @return String - StringBuilder.toString();
	 */
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
					if(aiAName!=null && !aiAName.isEmpty()) {
						buffer.append(buildNode("name",aiAName));
						buffer.append(COMMA);
					}
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

	/**
	 * <p>
	 * Method which locates an institution and move one possition up or down, 
	 * it depends his attributes.
	 * </p>
	 * <p>
	 * Uses an aiId to determinate which institution is the target and a boolean 
	 * to detect the action (move-up or move-down).
	 * </p>
	 * <p>
	 * It builds into a StringBuilder the structured response and returns it in String format.
	 * </p>
	 * 
	 * @param aiId - Integer archival-institution id
	 * @param moveUp - boolean action
	 * @return String - StringBuilder.toString();
	 */
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
	
	/**
	 * <p>
	 * Changes an institution parent to other parent.
	 * </p>
	 * <p>
	 * It detect an institution with his aiId and determinates
	 * the target parent with his internalParentId.
	 * </p> 
	 * 
	 * @param aiId - Integer archival-institution id
	 * @param internalParentId - Integer archival-institution id
	 * @return String - StringBuilder.toString();
	 * @throws Exception -> EagService.publish(archivalInstitutionTarget); throws Exceptions, for more information see {@link EagService} documentation
	 */
	private String changeGroup(Integer aiId,String internalParentId) throws Exception {
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
					int oldOrder = 0;
					if(parentArchivalInstitution!=null){
						Set<ArchivalInstitution> children = new LinkedHashSet<ArchivalInstitution>(parentArchivalInstitution.getChildArchivalInstitutions());
						if(children.size() > 0){
							int biggestAlOrder = 0;
							for(ArchivalInstitution archivalInstitution : children) {
								if(archivalInstitution.getAlorder() >= biggestAlOrder) {
									biggestAlOrder = archivalInstitution.getAlorder() + 1;
								}
							}
							archivalInstitutionTarget.setAlorder(biggestAlOrder);
						}
						oldOrder = archivalInstitutionTarget.getAlorder();
					} else {
						List<ArchivalInstitution> children = aiDao.getArchivalInstitutionsByCountryIdForAL(SecurityContext.get().getCountryId(),true);
						if(children!=null){
							int biggestAlOrder = 0;
							for(ArchivalInstitution archivalInstitution : children) {
								if(archivalInstitution.getAlorder() >= biggestAlOrder) {
									biggestAlOrder = archivalInstitution.getAlorder() + 1;
								}
							}
							archivalInstitutionTarget.setAlorder(biggestAlOrder);
						}
						oldOrder = archivalInstitutionTarget.getAlorder();
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
							if(oldOrder<=aiTemp.getAlorder() && aiTemp.getAiId()!=archivalInstitutionTarget.getAiId()){
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
		EagService.publish(archivalInstitutionTarget);
		return buffer.toString();
	}

	/**
	 * <p>
	 * Method which builds the target JSON with his new parent.
	 * </p>
	 * <p> 
	 * This JSON should be used to get working dynatree.
	 * </p>
	 * <p>
	 * It builds into a StringBuilder the structured response and returns it in String format.
	 * </p>
	 * 
	 * @param parentArchivalInstitution - parent {@link ArchivalInstitution} 
	 * @return String - StringBuilder.toString();
	 */
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

	/**
	 * <p>
	 * Method which tries to get all groups (JSON).
	 * </p>
	 * <p>
	 * This JSON should be used to get working dynatree.
	 * </p>
	 * 
	 * @param aiId - Integer archival-institution id
	 * @return String -> StringBuilder.toString()
	 */
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

	/**
	 * <p>
	 * Method which deletes an institution.
	 * </p>
	 * <p>
	 * It determinates which institution is the target with his aiId.
	 * </p>
	 * <p>
	 * Returns an String with JSON. It could contain messages translated.
	 * </p>
	 * 
	 * @param aiId - Integer archival-institution id
	 * @return String -> StringBuilder.toString()
	 * @throws IOException -> removePathsToBeDeleted(); and rollbackDeletedPaths(); throws IOException, see his documentation about it.
	 */
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
			if(!ContentUtils.containsEads(ai) && !ContentUtils.containsEacs(ai)){
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
						String path = ArchivalLandscapeUtils.deleteContent(ai);
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
				if(!JpaUtil.noTransaction()){
					JpaUtil.commitDatabaseTransaction();	
				}
				removePathsToBeDeleted();
			}else{ //undo changes
				JpaUtil.rollbackDatabaseTransaction();
				rollbackDeletedPaths();
			}
		}
		return messenger.toString();
	}
	/**
	 * <p>
	 * Method which tries to delete a list of path used before an action. 
	 * </p>
	 * @throws IOException -> {@link FileUtils}.deleteDirectory(new File(subDir+path)); throws Exception, see Apache documentation about it.
	 */
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
	
	/**
	 * <p>
	 * Method which restores deleted path (temporally moved to other path) 
	 * to the original path.
	 * </p>
	 * @throws IOException -> {@link FileUtils}.moveDirectory(new File(subDir+path),new File(subDir+path.substring(0,path.length()-"_old".length()))); throws IOException, see Apache documentation about it.
	 */
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
	 * <p>
	 * Method to delete each Archival Institution.
	 * </p>
	 * <p>
	 * Uses the target Archival-Institution VO.
	 * </p>
	 *
	 * @param ai - {@link ArchivalInstitution} used to get child Archival Institution
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
		String path = ArchivalLandscapeUtils.deleteContent(ai);
		if(path!=null){
			if(this.pathsToBeDeleted==null){ this.pathsToBeDeleted = new HashSet<String>();}
			this.pathsToBeDeleted.add(path);
		}
	}

	/**
	 * <p>
	 * Method which creates an archival institution using params.
	 * </p>
	 * <p>
	 * The current params are name, parent-name, type and language.
	 * </p>
	 * <p>
	 * Returns the JSON (in a String) with the info/error user messages.
	 * </p>
	 * <p>
	 * Returns an String with JSON. It could contain messages translated.
	 * </p>
	 * 
	 * @param name - String name
	 * @param father - String parent
	 * @param type - String type
	 * @param lang - String lang
	 * @return String - StringBuilder.toString();
	 */
	private String createArchivalInstitution(String name,String father,String type,String lang){
		StringBuilder messenger = new StringBuilder();
		if(name!=null && type!=null && !name.trim().isEmpty() && !type.trim().isEmpty()){
			// Store in data base the operation, the archival institutions
			JpaUtil.beginDatabaseTransaction();
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
				ArchivalInstitution archivalInstitution = new ArchivalInstitution();
				name = name.replaceAll("[\\s+&&[^\\n]]+"," ") //1. reduce all non-newline whitespaces to a unique space
					    .replaceAll("(?m)^\\s+|\\s$","") //2. remove spaces from start or end of the lines
					    .replaceAll("\\n+"," ");//3. remove all newlines, compress it in a unique line
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

	/**
	 * <p>
	 * Generates a random internal identifier.
	 * </p>
	 * <p> 
	 * It always starts with an 'A' character.
	 * </p>
	 * 
	 * @return String - (pattern is in this format A+currentTime+'-'+randomFigure)
	 */
	protected static String getNewinternalIdentifier() {
		return "A"+System.currentTimeMillis()+"-"+(new Float(+Math.random()*1000000).toString());
	}

	/**
	 * <p>
	 * Action which writes the JSON (archival-landscape tree) using params (couId).
	 * </p>
	 * <p>
	 * It the target param is not received it's built from session attributes.
	 * </p>
	 * <p>
	 * The next step is call to generateArchivalInstitutionPartJSON to write
	 * the spected result.
	 * </p>
	 * <p>
	 * This method is required for dynatree plugin.
	 * </p>
	 * <p>
	 * Writes an String with JSON into ServletResponse.
	 * </p>
	 * 
	 * @return String - StringBuilder.toString();
	 */
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
	
	/**
	 * <p>
	 * Method which generates an archival institution tree in JSON format.
	 * </p>
	 * <p>
	 * Calls to generateArchivalInstitutionsTreeJSON with the current List<ArchivalInstitutionUnit>
	 * </p>
	 * @param archivalInstitutionList - List<ArchivalInstitution> source list.
	 * @return StringBuffer - generateArchivalInstitutionsTreeJSON response with the target tree.
	 */
	public StringBuffer generateArchivalInstitutionPartJSON(List<ArchivalInstitutionUnit> archivalInstitutionList) {
		StringBuffer tree = null;
		try {
			tree = generateArchivalInstitutionsTreeJSON(archivalInstitutionList,TARGET_ACTION,getServletRequest().getLocale().getDisplayLanguage(),true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return tree;
	}
	
	/**
	 * <p>
	 * Method which returns in JSON format the possible actions for an institution.
	 * </p>
	 * <p>
	 * It uses a param called aieag and aigroup, and extracts it from the request.
	 * </p>
	 * <p>
	 * Writes info ServletResponse an String with JSON.
	 * </p>
	 * @return String - (always is null because this value must never be used, it's useless)
	 */
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
	 * <p>
	 * Method which builds the current actions for an institution.
	 * </p>
	 * <p>
	 * It uses the target archival-institution to be filled.
	 * </p>
	 * <p>
	 * appendInstitution - launch the Name, Select an element type, 
	 * select a language and "add to the list" form part to allow 
	 * create new institutions/groups.
	 * </p>
	 * 
	 * @return StringBuilder - action nodes
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
		if(archivalInstitution!=null && !ContentUtils.containsEads(archivalInstitution) && !ContentUtils.containsEacs(archivalInstitution)){
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

	/**
	 * <p>
	 * Internal method which builds a node in JSON format.
	 * </p>
	 * <p>
	 * It returns the target JSON in a String.
	 * </p>
	 * 
	 * @param actionName -> String action
	 * @param actionValue -> String value
	 * @return String - JSON node in String format
	 */
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
	/**
	 * <p>
	 * Method which returns if an institution is parent of other institution.
	 * </p>
	 * <p>
	 * First param is the source and the second one is the destination.
	 * </p>
	 * <p>
	 * Returns the state: if the target source is not able to be moved to destination.
	 * </p> 
	 * 
	 * @param source - {@link ArchivalInstitution} source
	 * @param destination - {@link ArchivalInstitution} destination
	 * @return boolean - isParent flag
	 */
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
	
	/**
	 * <p>
	 * Method which deletes an alternative-name from DDBB an archival-institution.
	 * </p>
	 * <p>
	 * It uses the aiId to detect which is the archival-institution, and a name and
	 * language for detects target alternative-name.
	 * </p>
	 * <p>
	 * It returns a JSON (String) which the current messages for user (info/error) translated.
	 * </p>
	 * 
	 * @param aiId - Integer archival-institution id
	 * @param name - String name
	 * @param lang - String language
	 * @return String - node with actionTranslate -> StringBuilder.toString();
	 */
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
