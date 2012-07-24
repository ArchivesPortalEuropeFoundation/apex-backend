package eu.apenet.dashboard.actions.ajax;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.FindingAid;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 23/05/2011
 *
 * @author Yoann Moranville
 */
public class SelectFindingAidsAction extends AjaxControllerAbstractAction {
    private String id;
    private List<String> ids;

    public String addOneFA2Session() {
        Writer writer = null;
        try {
            writer = openOutputWriter();

            List<Integer> listId = getIdentifierSelectedFAs();

            if(!StringUtils.isEmpty(id)) {
                Integer idInt = Integer.parseInt(id);
                if(listId.contains(idInt))
                    listId.remove(idInt);
                else
                    listId.add(Integer.parseInt(id));
            } else if(ids != null && !ids.isEmpty()){
                for(String oneId : ids){
                    Integer idInt = Integer.parseInt(oneId);
                    if(listId.contains(idInt))
                        listId.remove(idInt);
                    else
                        listId.add(idInt);
                }
            }

            setIdentifierSelectedFAs(listId);

            writer.append(new JSONObject().put("correct", true).put("listId", listId).toString());
            writer.close();
        } catch (Exception e){
            try {
                LOG.error("Error saving the ID", e);
                if(writer != null){
                    writer.append(new JSONObject().put("correct", false).toString());
                    writer.close();
                }
            } catch (Exception ex){
            }
        }
        return null;
    }

    public String clearFAsFromSession() {
        Writer writer = null;
        try {
            writer = openOutputWriter();

            setIdentifierSelectedFAs(new ArrayList<Integer>());

            writer.append(new JSONObject().put("correct", true).put("listId", getIdentifierSelectedFAs()).toString());
            writer.close();
        } catch (Exception e){
            try {
                LOG.error("Error saving the ID", e);
                if(writer != null){
                    writer.append(new JSONObject().put("correct", false).toString());
                    writer.close();
                }
            } catch (Exception ex){
            }
        }
        return null;
    }

    public String addAllFAsInSession() {
        Writer writer = null;
        try {
            writer = openOutputWriter();

            int aiId = getAiId();
            List<Integer> allFaIds = DAOFactory.instance().getEadDAO().getAllIds(FindingAid.class, aiId);
            setIdentifierSelectedFAs(allFaIds);

            writer.append(new JSONObject().put("correct", true).put("listId", getIdentifierSelectedFAs()).toString());
            writer.close();
        } catch (Exception e){
            try {
                LOG.error("Error saving the ID", e);
                if(writer != null){
                    writer.append(new JSONObject().put("correct", false).toString());
                    writer.close();
                }
            } catch (Exception ex){
            }
        }
        return null;
    }

    public String getFAsFromSession() {
        Writer writer = null;
        try {
            writer = openOutputWriter();

            writer.append(new JSONObject().put("correct", true).put("listId", getIdentifierSelectedFAs()).toString());
            writer.close();
        } catch (Exception e){
            try {
                LOG.error("Error getting the FA IDs", e);
                if(writer != null){
                    writer.append(new JSONObject().put("correct", false).toString());
                    writer.close();
                }
            } catch (Exception ex){
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Integer> getIdentifierSelectedFAs(){
        HttpSession session = request.getSession();
        if(session.getAttribute(LIST_IDS) != null)
            return (List<Integer>) session.getAttribute(LIST_IDS);
        return new ArrayList<Integer>();
    }

    @SuppressWarnings("unchecked")
    public void setIdentifierSelectedFAs(List<Integer> listId){
        HttpSession session = request.getSession();
        session.setAttribute(LIST_IDS, listId);
    }

    //---- Getters and Setters ----//
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public List<String> getIds() {
        return ids;
    }
    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
