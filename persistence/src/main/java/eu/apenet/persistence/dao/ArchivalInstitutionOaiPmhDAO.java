package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.ArchivalInstitutionOaiPmh;

/**
 * 
 * @author jara
 *
 */

public interface ArchivalInstitutionOaiPmhDAO extends GenericDAO<ArchivalInstitutionOaiPmh, Integer> {
	public List<ArchivalInstitutionOaiPmh> getArchivalInstitutionOaiPmhs(Integer aiId);

}

