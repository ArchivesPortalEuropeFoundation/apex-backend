package eu.apenet.persistence.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.UserDAO;


import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.ArchivalInstitution;

@Ignore
public class ArchivalInstitutionDAOTest extends AbstractHibernateTestCase{

	
	
	 private UserDAO partnerDao;
	 private User partner;
	 private ArchivalInstitution ai;
	 private ArchivalInstitutionDAO aiDao;
	 
	 @Test
	 public void testFindAllArchivalInstitution() {
		 	try{
		      aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
	         List <ArchivalInstitution> temp= aiDao.findAll();
	         Assert.assertNotNull(temp);
	
	        } catch (Exception e){
	            throw new RuntimeException(e);
	        }  
	 }
	 
	 
	 /*@Test
	 public void testFindByIdPartner() {
		 	try{
			 	partnerDao = DAOFactory.instance().getPartnerDAO();
		        partner = partnerDao.findById(2);
	            Assert.assertNotNull(partner);
	
	        } catch (Exception e){
	            throw new RuntimeException(e);
	        }  
	 }*/
	 
	 
	 
	/*@Test
	 public void teststoreArchival() {
		  
	        try {
		 	partnerDao = DAOFactory.instance().getPartnerDAO();
	        partner = partnerDao.findById(1);
            Assert.assertNotNull(partner);
	         
	        ai = new ArchivalInstitution();
	              //ai.setAiId(3); //banned!! 
	              ai.setAiname("stringTest");
	              ai.setPartner(partner);
	              //Date temp = new Date().getTime(); //As you prefer!
	              ai.setRegistrationDate(new Date()); //As you prefer!
	              ai.setEagPath("/pathTest/test"); //As you prefer!
	              
	         aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
                  aiDao.store(ai);
	
	        } catch (Exception e){
	            throw new RuntimeException(e);
	        }  
	 }*/
	 

}

