/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.ead3;

import eu.apenet.dashboard.services.ead3.Ead3Service;
import eu.apenet.dashboard.utils.ContentUtils;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaisar
 */
public class Ead3Actions extends AbstractEad3Actions {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Integer id;
    
    private Ead3Service ead3Service = new Ead3Service();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String validateEad3() {
        try {
            ead3Service.validate(super.getXmlType(), id);
            return SUCCESS;
        } catch (Exception ex) {
            Logger.getLogger(Ead3Actions.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR;
        }
    }

    @Override
    public String publishEad3() {
        try {
            ead3Service.publish(super.getXmlType(), id);
            return SUCCESS;
        } catch (Exception ex) {
            Logger.getLogger(Ead3Actions.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR;
        }
    }

    @Override
    public String unpublishEad3() {
        try {
            ead3Service.unpublish(super.getXmlType(), id);
            return SUCCESS;
        } catch (Exception ex) {
            Logger.getLogger(Ead3Actions.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR;
        }
    }

    @Override
    public String deleteEad3() {
        try {
            ead3Service.delete(super.getXmlType(), id);
            return SUCCESS;
        } catch (Exception ex) {
            Logger.getLogger(Ead3Actions.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR;
        }
    }

    @Override
    public String validatePublishEad3() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Properties getConversionParameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteEseEdm() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteFromEuropeana() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deliverToEuropeana() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deleteFromQueue() {
        try {
            Ead3Service.deleteFromQueue(id);
            return SUCCESS;
        } catch (Exception ex) {
            Logger.getLogger(Ead3Actions.class.getName()).log(Level.SEVERE, null, ex);
            return ERROR;
        }
    }

    public void download() {
        try {
            File file = ead3Service.download(super.getXmlType(), id);
            ContentUtils.downloadXml(this.getServletRequest(), getServletResponse(), file);
        } catch (IOException ex) {
            Logger.getLogger(Ead3Actions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
