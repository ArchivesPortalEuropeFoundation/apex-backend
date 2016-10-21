/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.types.XmlType;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.UpFile;
import org.apache.log4j.Logger;

/**
 *
 * @author kaisar
 */
public class Ead3Service {

    protected static final Logger LOGGER = Logger.getLogger(Ead3Service.class);

    public Ead3 create(XmlType xmlType, UpFile upFile, Integer aiId, String ead3Id, String title) throws Exception {
        SecurityContext.get().checkAuthorized(aiId);
        Ead3 ead3 = new CreateEad3Task().execute(xmlType, upFile, aiId, ead3Id, title);
        DAOFactory.instance().getUpFileDAO().delete(upFile);
        return ead3;
    }

}
