package eu.apenet.dashboard.actions;

import java.util.ArrayList;
import java.util.List;

import eu.apenet.dashboard.infraestructure.ArchivalInstitutionNode;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.User;
import eu.apenet.persistence.vo.SourceGuide;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * @author Jara Alvarez
 * 
 * To show a general overview of a country
 * 
 */
public class CountryStateAction extends ActionSupport implements Preparable{

	private final Logger log = Logger.getLogger(getClass());
	private List<ArchivalInstitutionNode> aiNodes;
	private List<Country> countriesList;
	private Integer countrySelected;

	public List<ArchivalInstitutionNode> getAiNodes() {
		return aiNodes;
	}

	public void setAiNodes(List<ArchivalInstitutionNode> aiNodes) {
		this.aiNodes = aiNodes;
	}

	public List<Country> getCountriesList() {
		return countriesList;
	}

	public void setCountriesList(List<Country> countriesList) {
		this.countriesList = countriesList;
	}

	public Integer getCountrySelected() {
		return countrySelected;
	}

	public void setCountrySelected(Integer countrySelected) {
		this.countrySelected = countrySelected;
	}

	public void prepare() throws Exception{
		log.debug("Filling in the countries list...");
		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		this.countriesList = countryDAO.findAll();
	}
	
	public String execute() throws Exception{
		
		String result = null;
		try{
			if (this.getCountrySelected()!= null)
			{			
				EadDAO eadDao = DAOFactory.instance().getEadDAO();
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();	
				
				List<ArchivalInstitution> aiList = aiDao.getGroupsAndArchivalInstitutionsByCountryId(countrySelected, "alorder", true );					
					
					this.aiNodes = new ArrayList<ArchivalInstitutionNode>();
					for (int i=0;i<aiList.size();i++)
					{
						Long numSGs = eadDao.countFilesbyInstitution(SourceGuide.class, aiList.get(i).getAiId());
						Long numHGs = eadDao.countFilesbyInstitution(HoldingsGuide.class, aiList.get(i).getAiId());
						Long numFAs = eadDao.countFilesbyInstitution(FindingAid.class, aiList.get(i).getAiId());
						//Long numCpf = cpfDao.countCpfContentByArchivalInstitution(aiList.get(i).getAiId());
						
						ArchivalInstitutionNode aiNode;
						aiNode = new ArchivalInstitutionNode(aiList.get(i),numFAs, numHGs, numSGs);
						this.getAiNodes().add(aiNode);						
					}
				
			}
			result= SUCCESS;
		}catch(Exception e)
		{
			log.error(e.getStackTrace());
			addActionMessage("Some errors occurred in getting information for the country selected.");
			result = ERROR;
		}
		return result;
	}
}	
