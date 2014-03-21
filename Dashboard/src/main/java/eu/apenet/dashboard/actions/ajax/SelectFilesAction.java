package eu.apenet.dashboard.actions.ajax;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.FindingAid;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;

/**
 * User: Yoann Moranville
 * Date: 23/05/2011
 *
 * @author Yoann Moranville
 */
public class SelectFilesAction extends AjaxControllerAbstractAction {
    private String id;
    private List<String> ids;

    public String addOneFile2Session() {
        Writer writer = null;
        try {
            writer = openOutputWriter();

            List<Integer> listId = getIdentifierSelectedFiles();

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

            setIdentifierSelectedFiles(listId);

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

    public String clearFilesFromSession() {
        Writer writer = null;
        try {
            writer = openOutputWriter();

            setIdentifierSelectedFiles(new ArrayList<Integer>());

            writer.append(new JSONObject().put("correct", true).put("listId", getIdentifierSelectedFiles()).toString());
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
            setIdentifierSelectedFiles(allFaIds);

            writer.append(new JSONObject().put("correct", true).put("listId", getIdentifierSelectedFiles()).toString());
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

    public String addAllEacCpfsInSession() {
        Writer writer = null;
        try {
            writer = openOutputWriter();

            int aiId = getAiId();
            List<Integer> allEacCpfIds = DAOFactory.instance().getEacCpfDAO().getAllIds(EacCpf.class, aiId);
            setIdentifierSelectedFiles(allEacCpfIds);

            writer.append(new JSONObject().put("correct", true).put("listId", getIdentifierSelectedFiles()).toString());
            writer.close();
        } catch (IOException e){
            try {
                LOG.error("Error saving the ID", e);
                if(writer != null){
                    writer.append(new JSONObject().put("correct", false).toString());
                    writer.close();
                }
            } catch (JSONException ex){
            } catch (IOException ex) {
            }
        } catch (JSONException e) {
            try {
                LOG.error("Error saving the ID", e);
                if(writer != null){
                    writer.append(new JSONObject().put("correct", false).toString());
                    writer.close();
                }
            } catch (JSONException ex){
            } catch (IOException ex) {
            }
        }
        return null;
    }

    public String getFilesFromSession() {
        Writer writer = null;
        try {
            writer = openOutputWriter();

            writer.append(new JSONObject().put("correct", true).put("listId", getIdentifierSelectedFiles()).toString());
            writer.close();
        } catch (Exception e){
            try {
                LOG.error("Error getting the file IDs", e);
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
    public List<Integer> getIdentifierSelectedFiles(){
        HttpSession session = getServletRequest().getSession();
        if(session.getAttribute(LIST_IDS) != null)
            return (List<Integer>) session.getAttribute(LIST_IDS);
        return new ArrayList<Integer>();
    }

    @SuppressWarnings("unchecked")
    public void setIdentifierSelectedFiles(List<Integer> listId){
        HttpSession session = getServletRequest().getSession();
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
