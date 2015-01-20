package eu.apenet.persistence.dao;

import eu.apenet.persistence.vo.XslUpload;

import java.util.List;

/**
 * Created by yoannmoranville on 29/12/14.
 */
public interface XslUploadDAO extends GenericDAO<XslUpload, Long>{
    public List<XslUpload> getXslUploads();
    public List<XslUpload> getXslUploads(Integer archivalInstitutionId);
    public boolean hasXslUpload(Integer archivalInstitutionId);
}
