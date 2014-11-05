/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.Ingestionprofile;
import java.util.List;

/**
 *
 * @author papp
 */
public interface IngestionprofileDAO extends GenericDAO<Ingestionprofile, Long>{
    public Ingestionprofile getIngestionprofile(Long id);
    public List<Ingestionprofile> getIngestionprofiles(Integer aiId);
    public List<Ingestionprofile> getIngestionprofiles(Integer aiId, int fileType);
}
