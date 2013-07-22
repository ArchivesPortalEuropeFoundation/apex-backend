package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.CpfContent;

import java.util.List;

/**
 * User: Yoann Moranville
 * Date: Mar 7, 2011
 *
 * @author Yoann Moranville
 */
public interface CpfContentDAO extends GenericDAO<CpfContent, Long>  {
    public Long doesCpfExists(String cpfId);
    public CpfContent retrieveCpfContent(Long id);
    public List<CpfContent> retrieveCpfContentByArchivalInstitution(int pageNumber, int limit, int aiId);
    public Long countCpfContentByArchivalInstitution(int aiId);
}
