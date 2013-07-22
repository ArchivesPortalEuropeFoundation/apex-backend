package eu.apenet.dashboard.actions.content;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.ead.EadService;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;

public class EadActions extends AbstractEadActions{

	private Integer id;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2671921974304007944L;

	public String validateEad() {
		try {
			EadService.validate(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String convertEad(Properties properties) {
		try {
			EadService.convert(getXmlType(), id, properties);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String publishEad() {
		try {
			EadService.publish(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String unpublishEad() {
		try {
			EadService.unpublish(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String deleteEad() {
		try {
			EadService.delete(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String convertValidatePublishEad(Properties properties) {
		try {
			EadService.convertValidatePublish(getXmlType(), id, properties);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}

	}

	@Override
	public String deleteEseEdm() {
		try {
			EadService.deleteEseEdm(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String deleteFromEuropeana() {
		try {
			EadService.deleteFromEuropeana(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String deliverToEuropeana() {
		try {
			EadService.deliverToEuropeana(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String deleteFromQueue() {
		try {
			EadService.deleteFromQueue(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String download() {
		try {
			File file = EadService.download(getId(), getXmlType());
			ContentUtils.downloadXml(this.getServletRequest(), getServletResponse(),file);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}
	

	@Override
	public String changeToDynamic() {
		try {
			EadService.makeDynamic(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	@Override
	public String changeToStatic() {
		try {
			EadService.makeStatic(getXmlType(), id);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String downloadHgSgStatistics() throws IOException {
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		Ead ead = eadDAO.findById(id, getXmlType().getClazz());
		SecurityContext.get().checkAuthorized(ead);
		CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
		HgSgFaRelationDAO hgSgFaRelationDAO = DAOFactory.instance().getHgSgFaRelationDAO();
		String name = APEnetUtilities.convertToFilename(ead.getEadid()) + "-statistics.csv";
		PrintWriter printWriter = ContentUtils.getWriterToDownload(this.getServletRequest(), getServletResponse(), name, "text/csv");
		printWriter.println("Holdings guide/Source guide unitid;Holdings guide/Source guide unittitle;Holdings guide/Source guide href eadid;Linked;Findingaid published;Findingaid title");
		List<CLevel> cLevels = clevelDAO.getNotLinkedCLevels(id, getXmlType().getClazz());
		for (CLevel cLevel: cLevels){
			printWriter.println(cLevel.getUnitid() + ";\"" + cLevel.getUnittitle() + "\";" + cLevel.getHrefEadid()+";false");
		}
		List<HgSgFaRelation> hgSgFaRelations = hgSgFaRelationDAO.getHgSgFaRelations(id, getXmlType().getClazz(), false);
		for (HgSgFaRelation hgSgFaRelation: hgSgFaRelations){
			CLevel cLevel = hgSgFaRelation.getHgSgClevel();
			FindingAid findingAid = hgSgFaRelation.getFindingAid();
			printWriter.println(cLevel.getUnitid() + ";\"" + cLevel.getUnittitle() + "\";" + cLevel.getHrefEadid()+";true;" + findingAid.isPublished()+ ";\""+ findingAid.getTitle().replace('"', '\'') + "\"");
		}
		printWriter.flush();
		printWriter.close();
		return null;
	}
}
