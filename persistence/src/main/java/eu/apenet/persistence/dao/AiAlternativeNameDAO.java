package eu.apenet.persistence.dao;

import java.util.List;

import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Lang;

public interface AiAlternativeNameDAO extends GenericDAO<AiAlternativeName, Integer>{
	public List<AiAlternativeName> findByAIId(ArchivalInstitution ai);
	public AiAlternativeName findByAIIdandLang(ArchivalInstitution ai, Lang language);
	public List<AiAlternativeName> findByAiAName(String AiAName);
	public AiAlternativeName findByAIId_primarykey (ArchivalInstitution ai);
}
