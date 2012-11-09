package eu.apenet.dashboard.actions.ajax;

import java.io.Writer;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.ead2ese.EAD2ESEConverter;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.dpt.utils.ead2ese.EseConfig;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;

/**
 * User: Yoann Moranville
 * Date: Dec 8, 2010
 *
 * @author Yoann Moranville
 */
public class BatchAction extends AjaxControllerAbstractAction {
    private static final long serialVersionUID = -4317782634952116497L;
    private static final String fa_full = "full";
    private static final String fa_small = "small";
    public static final String fa_search = "search";

    private HttpSession session;

    @Override
    public String execute() {
        long startBatchTime = System.currentTimeMillis();
        if(session == null)
            session = request.getSession();
        String faIdList = (String) session.getAttribute("faID");
        String faIdListSmall = (String) session.getAttribute("faID_small");
        String faIdListSearch = (String) session.getAttribute("faID_search");

        try {
            Writer writer = openOutputWriter();

            try {
                String type = request.getParameter("type");
                LOG.debug("type: " + type);

                String nbFa = request.getParameter("nbFindingAid");
                LOG.debug("nbFa: " + nbFa);

                int aiId = Integer.parseInt(request.getParameter("aiId"));
                LOG.debug("aiId: " + aiId);

                String typeBatch = request.getParameter("typeBatch");

                String[] faIdArray = null;
                LOG.debug("typeBatch: " + typeBatch);
                if(typeBatch.equals(fa_small)){
                    faIdListSmall = faIdListSmall.substring(1);
                    faIdListSmall = faIdListSmall.substring(0, faIdListSmall.length() - 1);
                    LOG.debug("faIdListSmall: " + faIdListSmall);
                    faIdArray = faIdListSmall.split(",");
                } else if(typeBatch.equals(fa_full)){
                    faIdList = faIdList.substring(1);
                    faIdList = faIdList.substring(0, faIdList.length() - 1);
                    LOG.debug("faIdList: " + faIdList);
                    faIdArray = faIdList.split(",");
                } else if(typeBatch.equals(fa_search)){
                    faIdListSearch = faIdListSearch.substring(1);
                    faIdListSearch = faIdListSearch.substring(0, faIdListSearch.length() - 1);
                    LOG.debug("faIdList: " + faIdListSearch);
                    faIdArray = faIdListSearch.split(",");
                } else {
                    writer.append(new JSONObject().put("error", "no type defined (small, search or full)").toString());
                    writer.close();
                    return null;
                }
                LOG.debug("faIdList size: " + faIdArray.length);

                int identifierToUse = Integer.parseInt(faIdArray[0]);

                long beforeTimeBatch = System.currentTimeMillis();
                LOG.debug("Time in batch before action: " + (beforeTimeBatch - startBatchTime));

                if(type.equals("conversion"))
                    convertFile(identifierToUse);
                else if(type.equals("validation"))
                    validateFile(identifierToUse);
                else if(type.equals("indexing")){
                    //
                	index(identifierToUse);
                }
                else if(type.equals("deleting"))
                    deleteFile(identifierToUse);
                else if(type.equals("deletingFromIndex"))
                    deleteFromIndex(aiId, identifierToUse);
                else if(type.equals("Converting to ESE"))
                    convertFileToEse(aiId, identifierToUse);
                else if (type.equals("deletingEse")){
                    deleteFromEuropeana(identifierToUse);
                	deleteEseFiles(identifierToUse);
                } else if (type.equals("deliveringToEuropeana"))
                	deliverToEuropeana(identifierToUse);
                else if(type.equals("batchEuropeanaDelete"))
                    deleteFromEuropeana(identifierToUse);
                else if(type.equals("batchDoItAll"))
                    doItAll(identifierToUse);

                long afterTimeBatch = System.currentTimeMillis();
                LOG.debug("Time in batch for action: " + (afterTimeBatch - beforeTimeBatch));

                JSONObject jsonObject = new JSONObject();
                int count = 0;

                StringBuilder faIdListNewBuilder = new StringBuilder();
                faIdListNewBuilder.append("[");
                for(int i = 1; i < faIdArray.length; i++){
                        faIdListNewBuilder.append(faIdArray[i]);
                        if(i != faIdArray.length - 1)
                                faIdListNewBuilder.append(",");
                        count++;
                }
                faIdListNewBuilder.append("]");

                LOG.debug("faListNewBuilder: " + faIdListNewBuilder.toString());

                if(typeBatch.equals(fa_small)) {
                    session.setAttribute("faID_small", faIdListNewBuilder.toString());
                    session.removeAttribute("faID");
                    session.removeAttribute("faID_search");
                } else if(typeBatch.equals(fa_full)) {
                    session.setAttribute("faID", faIdListNewBuilder.toString());
                    session.removeAttribute("faID_search");
                    session.removeAttribute("faID_small");
                } else if(typeBatch.equals(fa_search)) {
                    session.setAttribute("faID_search", faIdListNewBuilder.toString());
                    session.removeAttribute("faID_small");
                    session.removeAttribute("faID");
                }

                jsonObject.put("aiId", aiId);
                jsonObject.put("type", type);
                jsonObject.put("nbFindingAid", count);
                jsonObject.put("typeBatch", typeBatch);

                LOG.info("Remaining number of files in the batch: " + count + " (type: " + typeBatch + ")");
                LOG.debug("Sending to jsp: " + jsonObject.toString());
                writer.append(jsonObject.toString());

                long endTimeBatch = System.currentTimeMillis();
                LOG.debug("Time in batch for finishing: " + (endTimeBatch - afterTimeBatch));
            } catch (Exception e){
                LOG.error("Error, could not read JSON.", e);
                writer.append(new JSONObject().put("error", e.getMessage()).toString());
            }

            writer.close();
        } catch (Exception ex) {
            LOG.error("Problem", ex);
        }
        return null;
    }

    private void convertFile(int id){
        try {
            ContentManager.convertToAPEnetEAD(XmlType.EAD_FA, id, null, new CounterCLevelCall(), getConversionParameters());
        } catch (APEnetException e) {
            LOG.error("Conversion of " + id + " failed in batch processing! " + e.getMessage());
            LOG.error("Cause is", e);
        }
    }

    private void validateFile(int id){
        try {
            ContentManager.APEnetEADValidate(XmlType.EAD_FA, id);
        } catch (APEnetException e) {
            LOG.error("Validation of " + id + " failed in batch processing! " + e.getMessage());
        }
    }

    private void index(int id){
    	try {
    		ContentManager.indexProcess(XmlType.EAD_FA,id);
        } catch (Exception e){
        	LOG.error("Indexing of " + id  +" failed in batch processing! " + e.getMessage());
        }
	}
   	
    private void deleteFile(int id){
    	boolean isBeingHarvested = ContentManager.isBeingHarvested(); 
    	try {
			if (!isBeingHarvested || (isBeingHarvested && !ContentManager.eadHasEsePublished(id))) {
	        	ContentManager.delete(id, XmlType.EAD_FA);
			}
        } catch (Exception e){
        	LOG.error("Delete failed in batch processing!" + e.getMessage());
            //throw new APEnetRuntimeException("Delete failed in batch processing!", e);
        }
    }
    private void deleteEseFiles(int id){
    	boolean isBeingHarvested = ContentManager.isBeingHarvested(); 
        try {
			if (!isBeingHarvested || (isBeingHarvested && !ContentManager.eadHasEsePublished(id))) {
	        	ContentManager.deleteEseFiles(id);
			}
        } catch (Exception e){
        	LOG.error("Delete ese files failed in batch processing!" + e.getMessage());
            //throw new APEnetRuntimeException("Delete failed in batch processing!", e);
        }   	
    }
    private void deliverToEuropeana(int id){
    	boolean isBeingHarvested = ContentManager.isBeingHarvested(); 
        try {
			if (!isBeingHarvested) {
	        	ContentManager.deliverToEuropeana(id);
			}
        } catch (Exception e){
        	LOG.error("Delivering to Europeana failed in batch processing!", e);
            //throw new APEnetRuntimeException("Delete failed in batch processing!", e);
        }   	
    }
    private void deleteFromEuropeana(int id){
    	boolean isBeingHarvested = ContentManager.isBeingHarvested(); 
    	try {
			if (!isBeingHarvested || (isBeingHarvested && !ContentManager.eadHasEsePublished(id))) {
	            ContentManager.deleteFromEuropeana(id);
			}
        } catch (Exception e){
            LOG.error("Delete from europeana batch failed", e);
        }
    }
    private void deleteFromIndex(int aiId,int id){
//    	boolean isBeingHarvested = ContentManager.isBeingHarvested(); 
//        try {
//			if (!isBeingHarvested || (isBeingHarvested && !ContentManager.eadHasEsePublished(id))) {
//	        	ContentManager.deleteOnlyFromIndex(id, XmlType.EAD_FA, aiId, true);
//			}
//        } catch (Exception e){
//        	LOG.error("Delete from index failed in batch processing!" + e.getMessage());
//            //throw new APEnetRuntimeException("Delete from index failed in batch processing!", e);
//        }
    }
    private void convertFileToEse(int aiId, int id){
       EseConfig config = (EseConfig) request.getSession().getAttribute("eseConfig");
        try {
        	EAD2ESEConverter.convertEAD2ESE(new Integer(id) , config);
        } catch (Exception e){
        	LOG.error("Converting to ESE files failed in batch processing!" + e.getMessage());
            //throw new APEnetRuntimeException("Delete failed in batch processing!", e);
        }   	

    }
    private void doItAll(int id){
//        String currentState = DAOFactory.instance().getFindingAidDAO().findById(id).getFileState().getState();
//        LOG.info("Current state for id " + id + " is " + currentState);
//        if(currentState.equals(FileState.NEW)){ //Do validate, if ok: index, if not ok: convert - validate - index
//            validateFile(id);
//            doItAll(id);
//        } else if(currentState.equals(FileState.NOT_VALIDATED_NOT_CONVERTED)){ //Do convert - validate - index
//            convertFile(id);
//            doItAll(id);
//        } else if(currentState.equals(FileState.NOT_VALIDATED_CONVERTED)){ //Do validate - index
//            validateFile(id);
//            doItAll(id);
//        } else if(currentState.equals(FileState.VALIDATED_NOT_CONVERTED) || currentState.equals(FileState.VALIDATED_CONVERTED)){ //Do index
//            index(id);
//        }
    }
}

