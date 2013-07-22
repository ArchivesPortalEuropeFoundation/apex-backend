package eu.apenet.dashboard.tree;

import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.persistence.dao.CpfContentDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CpfContent;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * User: Yoann Moranville
 * Date: Mar 7, 2011
 *
 * @author Yoann Moranville
 */
public class DisplayCpfContentAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private static final long serialVersionUID = -3739723308040065360L;
    
    private String cpfId;
    private String id;

    private HttpServletRequest request;
    private HttpServletResponse response;

    public String getCpfId() {
        return cpfId;
    }

    public void setCpfId(String cpfId) {
        this.cpfId = cpfId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String execute(){
        if (StringUtils.isNotBlank(id)){
			Long idLong = new Long(id);
            CpfContent cpfContent = DAOFactory.instance().getCpfContentDAO().retrieveCpfContent(idLong);
			request.setAttribute("cpf", cpfContent);
		}
		return SUCCESS;
    }

    @Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

    @Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

    public String hasCpfReady() {
        if(StringUtils.isBlank(cpfId)){
            return null;
        }
        CpfContentDAO cpfContentDAO = DAOFactory.instance().getCpfContentDAO();
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        Long ident = cpfContentDAO.doesCpfExists(cpfId);
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(response.getOutputStream(), "utf-8");
            if(ident != null)
                writer.append(new JSONObject().put("ident", ident).toString());
            writer.close();
        } catch (Exception e){
            try {
                if(writer != null)
                    writer.close();
            } catch (Exception ex){
                //If exception, just do nothing, the link in JSP will not be available
            }
        }
        return null;
    }

}