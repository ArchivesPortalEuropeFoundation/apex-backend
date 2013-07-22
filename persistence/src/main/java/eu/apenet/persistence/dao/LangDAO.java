package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.Lang;

/**
 * 
 * @author Patricia
 *
 */

public interface LangDAO extends GenericDAO<Lang, Integer> {
	public List<Lang> getLanguages(String isoname);
	public Lang getLangByIso2Name(String iso2name);
	public Lang getLangByIsoname(String isoname);	
}

