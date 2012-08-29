package eu.apenet.persistence.dao;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.FileState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.UploadMethod;
import eu.archivesportaleurope.persistence.jpa.AbstractJpaTestCase;
@Ignore
public class ArchivalInstitutionDAOTest extends AbstractJpaTestCase{


	@Test 
	public void testGetRootArchivalInstitutionsByCountryId() {
		FindingAid fa = new FindingAid();
		fa.setTitle("hello");
		fa.setUploadDate(new Date());
		fa.setEadid("123");
		fa.setPathApenetead("asdf");
		FileState fileState = DAOFactory.instance().getFileStateDAO().getFileStateByState(FileState.NEW);
		fa.setFileState(fileState);
		UploadMethod uploadMethod = DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(UploadMethod.HTTP);
		fa.setUploadMethod(uploadMethod);
		ArchivalInstitution ai = DAOFactory.instance().getArchivalInstitutionDAO().findById(3);
		fa.setArchivalInstitution(ai);
		fa.setTotalNumberOfDaos(2l);
		fa.setTotalNumberOfUnits(4l);
		fa.setTotalNumberOfUnitsWithDao(5l);
		DAOFactory.instance().getFindingAidDAO().store(fa);



	}

}
