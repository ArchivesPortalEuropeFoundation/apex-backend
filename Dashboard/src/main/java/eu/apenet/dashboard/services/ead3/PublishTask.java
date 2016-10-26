/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.ValidatedState;
import java.util.Properties;

/**
 *
 * @author kaisar
 */
public class PublishTask extends AbstractEad3Task {

    public static boolean valid(Ead3 ead) {
        return ValidatedState.VALIDATED.equals(ead.getValidated()) && !ead.isPublished();
    }

    @Override
    protected void execute(Ead3 ead3, Properties properties) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getActionName() {
        return "publish";
    }

}
