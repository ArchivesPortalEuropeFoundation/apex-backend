package eu.apenet.dashboard.ead2ese;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.dpt.utils.ead2ese.EseFileUtils;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;

public class DownloadEseXmlAction extends AbstractInstitutionAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4216154271715161521L;
	private String id;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String execute() throws IOException {
		EseDAO dao = DAOFactory.instance().getEseDAO();
		if (NumberUtils.isNumber(id)) {

			List<Ese> eses = dao.getEses(NumberUtils.toInt(id), getAiId());
			if (eses.size() > 0) {
				Ese ese = eses.get(0);
				File file = EseFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), ese.getPath());
				ContentUtils.downloadXml(getServletRequest(), getServletResponse(), file);

			}

		}
		return SUCCESS;
	}


}
