/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.Userprofile;
import java.util.List;

/**
 *
 * @author papp
 */
public interface UserprofileDAO extends GenericDAO<Userprofile, Long>{
    public List<Userprofile> getUserprofiles(Integer aiId);
}
