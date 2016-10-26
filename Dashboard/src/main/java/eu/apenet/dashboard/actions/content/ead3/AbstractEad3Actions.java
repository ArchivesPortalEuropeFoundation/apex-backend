/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.ead3;

import eu.apenet.dashboard.actions.content.AbstractTypeActions;

/**
 *
 * @author kaisar
 */
public abstract class AbstractEad3Actions extends AbstractTypeActions {

    @Override
    public String execute() throws Exception {
        if (null != action) {
            switch (action) {
                case VALIDATE:
                    return validateEad3();
                case PUBLISH:
                    return publishEad3();
                case UNPUBLISH:
                    return unpublishEad3();
                case DELETE:
                    return deleteEad3();
                case VALIDATE_PUBLISH:
                    return validatePublishEad3();
                default:
                    break;
            }
        }
        return ERROR;
    }

    public abstract String validateEad3();

//    public abstract String convertEad(Properties properties);
    public abstract String publishEad3();

    public abstract String unpublishEad3();

    public abstract String deleteEad3();

//    public abstract String convertValidateEad(Properties properties);
//    public abstract String convertValidatePublishEad(Properties properties);
    public abstract String validatePublishEad3();

//    public abstract String changeToDynamic();
//
//    public abstract String changeToStatic();
}
