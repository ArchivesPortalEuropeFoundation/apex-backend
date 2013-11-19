package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;

/**
 * 
 * @author jara
 *
 */

public interface ArchivalInstitutionOaiPmhDAO extends GenericDAO<ArchivalInstitutionOaiPmh, Long> {
	public List<ArchivalInstitutionOaiPmh> getArchivalInstitutionOaiPmhs(Integer archivalInstitutionId);
    public Long countEnabledItems();
    public List<ArchivalInstitutionOaiPmh> getFirstItems();
}

