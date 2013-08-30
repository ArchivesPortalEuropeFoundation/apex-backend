package eu.apenet.dashboard.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import eu.apenet.dashboard.AbstractAction;
import eu.apenet.dashboard.services.DptUpdateService;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.DptUpdate;

/**
 * User: Yoann Moranville
 * Date: 24/05/2012
 *
 * @author Yoann Moranville
 */
public class DptVersionAction extends AbstractAction{
    private static final long serialVersionUID = 4754804143407258044L;

    private String versionNb;
    private Long versionId;

    protected void buildBreadcrumbs() {
        super.buildBreadcrumbs();
        addBreadcrumb(getText("dptversion.title"));
    }

    @Override
    public String execute() {
        List<DptUpdate> dptUpdates = DAOFactory.instance().getDptUpdateDAO().findAll();
        getServletRequest().setAttribute("dptUpdates" , dptUpdates);
        return SUCCESS;
    }

    public String addVersion() {
        DptUpdateService.addDptVersion(versionNb);
        return SUCCESS;
    }
    public String deleteVersion() {
        DptUpdateService.deleteDptVersion(versionId);
        return SUCCESS;
    }

    public String getVersionNb() {
        return versionNb;
    }

    public void setVersionNb(String versionNb) {
        this.versionNb = versionNb;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

}
