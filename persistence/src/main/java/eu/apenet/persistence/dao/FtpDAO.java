package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.Ftp;

/**
 *
 * @author apef
 */
public interface FtpDAO extends GenericDAO<Ftp, Integer>{
     public Ftp getFtpConfig(Integer aiId);
}
