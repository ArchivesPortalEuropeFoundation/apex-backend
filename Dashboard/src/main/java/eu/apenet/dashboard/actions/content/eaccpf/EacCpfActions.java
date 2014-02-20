/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.actions.content.eaccpf;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;
import eu.apenet.dashboard.actions.content.AbstractTypeActions;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dashboard.utils.ContentUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 *
 * @author papp
 */
public class EacCpfActions extends AbstractTypeActions {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String execute() throws Exception {
        if (VALIDATE.equals(action)) {
            return validateEacCpf();
        } else if (CONVERT.equals(action)) {
            return convertEacCpf(getConversionParameters());
        } else if (PUBLISH.equals(action)) {
            return publishEacCpf();
        } else if (UNPUBLISH.equals(action)) {
            return unpublishEacCpf();
        } else if (DELETE.equals(action)) {
            return deleteEacCpf();
        } else if (CONVERT_VALIDATE.equals(action)) {
            return convertValidateEacCpf(getConversionParameters());
        } else if (CONVERT_VALIDATE_PUBLISH.equals(action)) {
            return convertValidatePublishEacCpf(getConversionParameters());
        } else if (DELETE_ESE_EDM.equals(action)) {
            return deleteEseEdm();
        } else if (DELETE_FROM_EUROPEANA.equals(action)) {
            return deleteFromEuropeana();
        } else if (DELIVER_TO_EUROPEANA.equals(action)) {
            return deliverToEuropeana();
        } else if (DELETE_FROM_QUEUE.equals(action)) {
            return deleteFromQueue();
        }
        return ERROR;
    }

    private String convertValidatePublishEacCpf(Properties properties) {
        try {
            EacCpfService.convertValidatePublish(id, properties);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    private String convertValidateEacCpf(Properties properties) {
        try {
            EacCpfService.convertValidate(id, properties);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    private String convertEacCpf(Properties properties) {
        try {
            EacCpfService.convert(id, properties);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    private String validateEacCpf() {
        try {
            EacCpfService.validate(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    private String publishEacCpf() {
        try {
            EacCpfService.publish(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    private String unpublishEacCpf() {
        try {
            EacCpfService.unpublish(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    private String deleteEacCpf() {
        try {
            EacCpfService.delete(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteEseEdm() {
        try {
            EacCpfService.deleteEdm(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteFromEuropeana() {
        try {
            EacCpfService.deleteFromEuropeana(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deliverToEuropeana() {
        try {
            EacCpfService.deliverToEuropeana(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    @Override
    public String deleteFromQueue() {
        try {
            EacCpfService.deleteFromQueue(id);
            return SUCCESS;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ERROR;
        }
    }

    public String download() {
        try {
            File file = EacCpfService.download(getId());
            ContentUtils.downloadXml(this.getServletRequest(), getServletResponse(), file);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected Properties getConversionParameters() {
        Properties parameters = new Properties();
//        HttpSession session = getServletRequest().getSession();
//        String option_default = (String) session.getAttribute(AjaxControllerAbstractAction.OPTIONS_DEFAULT);
//        String option_use_existing = (String) session.getAttribute(AjaxControllerAbstractAction.OPTIONS_USE_EXISTING);
//        boolean option_use_existing_bool = true;
//        if (option_use_existing != null) {
//            option_use_existing_bool = !Boolean.parseBoolean(option_use_existing);
//        }
//        if (option_default == null) {
//            option_default = "UNSPECIFIED";
//        }
//        parameters.put("defaultRoleType", option_default);
//        parameters.put("useDefaultRoleType", Boolean.toString(option_use_existing_bool));
        return parameters;
    }

}
