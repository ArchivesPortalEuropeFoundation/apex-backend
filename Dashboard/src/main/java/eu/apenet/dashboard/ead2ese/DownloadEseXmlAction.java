package eu.apenet.dashboard.ead2ese;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import eu.apenet.commons.utils.APEnetUtilities;
import org.apache.commons.lang.math.NumberUtils;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.dpt.utils.ead2ese.FileUtils;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;

public class DownloadEseXmlAction extends ActionSupport {

	private String id;
	private FileInputStream inputStream;
	private String fileName;
	private Long fileSize;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String execute() throws FileNotFoundException {
		EseDAO dao = DAOFactory.instance().getEseDAO();
		if (NumberUtils.isNumber(id)) {

			List<Ese> eses = dao.getEses(NumberUtils.toInt(id));
			if (eses.size() > 0) {
				Ese ese = eses.get(0);
				File file = FileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), ese.getPath());
				inputStream = new FileInputStream(file);
				fileName = file.getName();
				fileSize = file.length();

			}

		}
		return SUCCESS;
	}

	public FileInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(FileInputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Long getFileSize() {
		return fileSize;
	}

}
