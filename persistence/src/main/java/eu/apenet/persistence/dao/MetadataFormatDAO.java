package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.MetadataFormat;

public interface MetadataFormatDAO extends GenericDAO<MetadataFormat, Integer>  {
	public MetadataFormat getMetadataFormatByName(String metadataPrefix);
}
