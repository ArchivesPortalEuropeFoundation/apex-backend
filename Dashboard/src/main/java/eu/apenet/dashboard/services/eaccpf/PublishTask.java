/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.eaccpf;

import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.ValidatedState;
import java.util.Properties;

/**
 *
 * @author papp
 */
class PublishTask extends AbstractEacCpfTask{
    @Override
    protected void execute(EacCpf eacCpf, Properties properties) throws Exception {
        //TODO: Add content
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getActionName() {
        return "publish";
    }

    static boolean valid(EacCpf eacCpf) {
        return ValidatedState.VALIDATED.equals(eacCpf.getValidated()) && !eacCpf.isPublished();
    }
}
