package eu.apenet.persistence.hibernate;

import java.util.ArrayList;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionOaiPmhDAO;


import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;

/**
 * @author jara
 */
@Ignore
public class ArchivalInstitutionOaiPmhDAOTest extends AbstractHibernateTestCase{	
	
	 private ArchivalInstitutionOaiPmh aiOaiPmh;
	 private ArchivalInstitutionOaiPmhDAO aiOaiPmhDao;
	 private ArchivalInstitution ai;
	 private ArchivalInstitutionDAO aiDao;
	 
	
	 
	/*@Test
	 public void teststoreArchival() {
		  
	        try {
	        	
	        	ai = new ArchivalInstitution();
	        	this.aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
	    		List <ArchivalInstitution> ais = new ArrayList<ArchivalInstitution>();
	    		ais = this.aiDao.getArchivalInstitutions(4);
	    		
	        	aiOaiPmh = new ArchivalInstitutionOaiPmh();
	        	aiOaiPmh.setOaiPmhId(1);
	        	aiOaiPmh.setOaiPmhUrl("url/url");
	        	aiOaiPmh.setArchivalInstitution(ais.get(0));	        	
	              
	        	aiOaiPmhDao = DAOFactory.instance().getArchivalInstitutionOaiPmhDAO();
	        	aiOaiPmhDao.store(aiOaiPmh);
	
	        } catch (Exception e){
	            throw new RuntimeException(e);
	        }  
	 }*/
	 

}

