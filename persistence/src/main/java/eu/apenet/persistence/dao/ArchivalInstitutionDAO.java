package eu.apenet.persistence.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.User;

/**
 * 
 * @author paul
 *
 */

public interface ArchivalInstitutionDAO extends GenericDAO<ArchivalInstitution, Integer> {
	public List<ArchivalInstitution> getArchivalInstitutionsWithSearchableItems(Integer countryId, Integer parentAiId);
	public List<ArchivalInstitution> getRootArchivalInstitutionsByCountryId(Integer countryId);
	public List<ArchivalInstitution> getArchivalInstitutionsByParentAiId(Integer parentAiId);
	public Integer countArchivalInstitutionsByCountryId(Integer countryId);
	public Integer countArchivalInstitutionsByParentAiId(Integer parentAiId);
	public ArchivalInstitution getArchivalInstitution(Integer aiId);
	public ArchivalInstitution getArchivalInstitutionByAiName(String InstitutionName);
	public List<ArchivalInstitution> getArchivalInstitutionsByPartnerId(Integer pId);
	public List<ArchivalInstitution> getArchivalInstitutionsByCountryId(Integer countryId, boolean onlyWithoutPartnerIds);
    public List<ArchivalInstitution> getArchivalInstitutionsByCountryId(Integer countryId);
	public Long countTotalArchivalInstitutions();
	//public List<ArchivalInstitution> getGroupsAndArchivalInstitutions(Integer pId, String sortValue, boolean ascending);
	public List<ArchivalInstitution> getArchivalInstitutionsByRepositorycode(String repositorycode);
	public List<ArchivalInstitution> getArchivalInstitutionsWithRepositoryCode();
	public boolean isRepositoryCodeAvailable(String repositorycode, Integer aiId);
	public List<ArchivalInstitution> getArchivalInstitutionsByAutform(String autform);
	//public List<ArchivalInstitution> getGroupsAndArchivalInstitutions(List<Partner> listpartners, String sortValue,boolean ascending);
	//public ArchivalInstitution getArchivalInstitutionbyPartnerandAlIdentifier(List<Partner> listpartners, String internalAlId);
	public ArchivalInstitution getArchivalInstitutionsByCountryIdandAlIdentifier(Integer countryId, String internalAlId);
	public List<ArchivalInstitution> getGroupsAndArchivalInstitutionsByCountryId(Integer countryId, String sortValue, boolean ascending);
	public ArchivalInstitution getArchivalInstitutionByInternalAlId(String identifier, Integer countryIdentifier);
	public Long countArchivalInstitutionsWithContentIndexed();
}

