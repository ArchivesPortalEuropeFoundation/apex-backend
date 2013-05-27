package eu.apenet.persistence.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.archivesportaleurope.persistence.jpa.AbstractJpaTestCase;
//import eu.apenet.persistence.vo.FileState;

public class ArchivalInstitutionDAOTest extends AbstractJpaTestCase{

	private int id = 13;
	private int aiId = 377;
	private Class<? extends Ead> clazz = HoldingsGuide.class;

	@Test
	public void testGetRootArchivalInstitutionsByCountryId() throws FileNotFoundException {
		CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		HgSgFaRelationDAO hgSgFaRelationDAO = DAOFactory.instance().getHgSgFaRelationDAO();
		long startTime = System.currentTimeMillis();
		PrintWriter printWriter = new PrintWriter(new File("/home/bastiaan/test.csv"));
		printWriter.println("HG/SG unitid;HG/SG unittitle;HG/SG href eadid;Linked;FA published;FA title");
		List<CLevel> cLevels = clevelDAO.getNotLinkedCLevels(id, clazz);
		for (CLevel cLevel: cLevels){
			printWriter.println(cLevel.getUnitid() + ";\"" + cLevel.getUnittitle() + "\";" + cLevel.getHrefEadid()+";");
		}
		List<HgSgFaRelation> hgSgFaRelations = hgSgFaRelationDAO.getHgSgFaRelations(id, clazz, false);
		for (HgSgFaRelation hgSgFaRelation: hgSgFaRelations){
			CLevel cLevel = hgSgFaRelation.getHgSgClevel();
			FindingAid findingAid = hgSgFaRelation.getFindingAid();
			printWriter.println(cLevel.getUnitid() + ";\"" + cLevel.getUnittitle() + "\";" + cLevel.getHrefEadid()+";1;" + findingAid.isPublished()+ ";\""+ findingAid.getTitle().replace('"', '\'') + "\"");
		}
		printWriter.flush();
		printWriter.close();
		System.out.println("time:" + (System.currentTimeMillis()-startTime));
//		long temp = clevelDAO.countNotLinkedCLevels(id, clazz);
//		System.out.println("#notlinked:" +temp +" time:" + (System.currentTimeMillis()-startTime));
//		temp = clevelDAO.countPossibleLinkedCLevels(id, clazz);
//		System.out.println("#total:" +temp +" time:" + (System.currentTimeMillis()-startTime));
//		temp = clevelDAO.countLinkedCLevels(id, clazz,true);
//		System.out.println("#linked:" +temp +" time:" + (System.currentTimeMillis()-startTime));
//		EadSearchOptions eadSearchOptions = new EadSearchOptions();
//		eadSearchOptions.setArchivalInstitionId(aiId);
//		temp = eadDAO.countEads(eadSearchOptions);
//		System.out.println("#all fa:" +temp +" time:" + (System.currentTimeMillis()-startTime));	
//		eadSearchOptions.setLinked(true);
//		temp = eadDAO.countEads(eadSearchOptions);
//		System.out.println("#linked fa:" +temp +" time:" + (System.currentTimeMillis()-startTime));			
//		eadSearchOptions.setLinked(false);
//		temp = eadDAO.countEads(eadSearchOptions);
//		System.out.println("#notlinked fa:" +temp +" time:" + (System.currentTimeMillis()-startTime));	
	}

	@Test @Ignore
	public void linkHgSgToFindingaids() {
		CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
		HgSgFaRelationDAO hgSgFaRelationJpaDAO = DAOFactory.instance().getHgSgFaRelationDAO();
		List<CLevel> clevels = clevelDAO.getNotLinkedCLevels(id, clazz);
		List<HgSgFaRelation> collection = new ArrayList<HgSgFaRelation>();
		for (CLevel clevel: clevels){
			Ead ead = eadDAO.getEadByEadid(FindingAid.class, 377, clevel.getHrefEadid());
			if (ead != null){
				HgSgFaRelation hgSgFaRelation = new HgSgFaRelation();
				hgSgFaRelation.setFaId(ead.getId());
				hgSgFaRelation.setAiId(aiId);
				hgSgFaRelation.setHgSgClevelId(clevel.getClId());
				if(clazz.equals(HoldingsGuide.class)){
					hgSgFaRelation.setHgId(id);
				}else if(clazz.equals(SourceGuide.class)){
					hgSgFaRelation.setSgId(id);
				}
				collection.add(hgSgFaRelation);
				
			}
		}
		hgSgFaRelationJpaDAO.store(collection);

	}
	@Test @Ignore
	public void fillCLevels() {
		CLevelDAO clevelDAO = DAOFactory.instance().getCLevelDAO();
//		EadDAO eadDAO = DAOFactory.instance().getEadDAO();
//		HgSgFaRelationDAO hgSgFaRelationJpaDAO = DAOFactory.instance().getHgSgFaRelationDAO();
//		FindingAidHibernateDAO findingAidDAO = (FindingAidHibernateDAO) DAOFactory.instance().getFindingAidDAO();
//		int id = 12;
//		int aiId = 377;
//		Class<? extends Ead> clazz = HoldingsGuide.class;
		int i = 0;
		while (i < 5000000){
//		long startTime = System.currentTimeMillis();
//		findingAidDAO.existFindingAidsNotLinkedByArchivalInstitution(aiId);
//		System.out.println("old:" + (System.currentTimeMillis()-startTime));
//		startTime = System.currentTimeMillis();
//		findingAidDAO.existFindingAidsNotLinkedByArchivalInstitutionNew(aiId);
//		System.out.println("new:" + (System.currentTimeMillis()-startTime));
//
//		startTime = System.currentTimeMillis();
//		findingAidDAO.existFindingAidsNotLinkedByArchivalInstitution(aiId);
//		System.out.println("old:" + (System.currentTimeMillis()-startTime));

		List<CLevel> collection = new ArrayList<CLevel>();
		for (int j = 0; j <= 10000; j++){
			CLevel clevel = new CLevel();
			clevel.setEcId(5301l);
			clevel.setLeaf(true);
			clevel.setLevel("file");
			clevel.setOrderId(i+j);
			clevel.setUnitid(getRandomString());
			clevel.setUnittitle(getRandomString());
			clevel.setHrefEadid(getRandomString());
			collection.add(clevel);
		}
		System.out.println("Store next once in database");
		clevelDAO.store(collection);
		i = i + 10000;
		System.out.println("Stored in database: " + i);
		}
	}
	private static final int DEFAULT_PASSWORD_LENGTH = 10;

	public static final String SPECIAL_CHARSET = "~!@#$%^&*_-+=`|\\(){}[]:;\"\'<>,.?/";
	public static final String LOWERCASE_CHARSET = "abcdefghijklmnopqrstuvwxyz";
	public static final String UPPER_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String DIGIT_CHARSET = "0123456789";
	public static final String CHARSET = DIGIT_CHARSET + LOWERCASE_CHARSET + UPPER_CHARSET + SPECIAL_CHARSET;
	public static String getRandomString() {
		return getRandomString(DIGIT_CHARSET + LOWERCASE_CHARSET + UPPER_CHARSET, DEFAULT_PASSWORD_LENGTH);
	}
	public static String getRandomString(String charset, int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
	
		return sb.toString();
	}
}
