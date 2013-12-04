package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.DptUpdate;

/**
 * User: Yoann Moranville
 * Date: 23/05/2012
 *
 * @author Yoann Moranville
 */
public interface DptUpdateDAO extends GenericDAO<DptUpdate, Long> {
    public DptUpdate doesVersionExist(String version);
}
