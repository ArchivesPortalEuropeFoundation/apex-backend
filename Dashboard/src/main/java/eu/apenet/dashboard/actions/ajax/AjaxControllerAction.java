package eu.apenet.dashboard.actions.ajax;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.contentmanager.ContentManager;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.dpt.utils.util.CountCLevels;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;

import org.json.JSONObject;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 * User: Yoann Moranville
 * Date: Feb 9, 2011
 *
 * @author Yoann Moranville
 */
public class AjaxControllerAction extends AjaxControllerAbstractAction {
    private static final long serialVersionUID = -5384301574672793021L;

    private CounterCLevelCall counterCLevelCall;

    public String convert2eadAjax() {
        try {
            counterCLevelCall = null;
            Writer writer = openOutputWriter();

            HttpSession session = request.getSession();

            final int id = Integer.parseInt(request.getParameter("id"));

            int xmlTypeId = Integer.parseInt(request.getParameter("xmlTypeId"));

            try {
                final XmlType xmlType = XmlType.getType(xmlTypeId);

                Ead ead = DAOFactory.instance().getEadDAO().findById(id, xmlType.getClazz());
                String filepath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPathApenetead();

                SecurityContext.get().checkAuthorized(ead);

                File file = new File(filepath);
                CountCLevels countCLevels = new CountCLevels();
                int test_counter = countCLevels.countOneFile(file);
                LOG.info("Counter of c levels for this file: " + filepath + " is " + test_counter);
                counterCLevelCall = new CounterCLevelCall();
                counterCLevelCall.initializeCounter(test_counter);
                session.setAttribute("counterCLevelCall", counterCLevelCall);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("counter", 0);
                writer.append(jsonObject.toString());

                final Map<String, String> conversionParameters = getConversionParameters();
                //Start conversion
                Thread conversionThread = new Thread(new Runnable(){
                    public void run(){
                        try {
                            ContentManager.convertToAPEnetEAD(xmlType, id, null, counterCLevelCall, conversionParameters);
                        } catch (APEnetException e) {
                            LOG.error("Error converting file", e);
                        }
                    }
                });
                conversionThread.start();

            } catch (Exception ex){
                //Nothing... We can't use the counter
                LOG.error("The counter could not be created, we discard its use");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error", true);
                writer.append(jsonObject.toString());
            } finally {
                if(writer != null)
                    writer.close();
            }
        } catch (Exception e){
            LOG.error("Error...", e);
        }
        return null;
    }

    public String getCounterStatus() {
        try {
            request.setCharacterEncoding(UTF8);
            response.setCharacterEncoding(UTF8);
            response.setContentType("application/json");
            Writer writer = new OutputStreamWriter(response.getOutputStream(), UTF8);

            HttpSession session = request.getSession();
            try {
                counterCLevelCall = (CounterCLevelCall)session.getAttribute("counterCLevelCall");

                int counter = counterCLevelCall.getCounter();
                int maxCounter = counterCLevelCall.getMaxCounter();

                int counterPourcentage = counter * 100 / maxCounter;

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("counter", counterPourcentage);
                LOG.debug("Current counter is " + counter + "/" + maxCounter);
                if(counter != maxCounter){
                    writer.append(jsonObject.toString());
                    Thread.sleep(2000);
                } else {
                    writer.append(null);
                }
            } catch (Exception ex){
                LOG.error("Error when getting the counter, we wait for the conversion to finish and we reload the page");
                //Nothing... We can't do anything
                JSONObject obj = new JSONObject();
                obj.put("error", true);
                writer.append(obj.toString());
            }
            
            writer.close();
            
        } catch (Exception e){
            LOG.error("Error sending counter", e);
        }

        return null;
    }

    public String checkCurrentConversionOptions() {
        try {
            Writer writer = openOutputWriter();
            try {
                HttpSession session = request.getSession();
                String option_default = (String)session.getAttribute(OPTIONS_DEFAULT);
                String option_use_existing = (String)session.getAttribute(OPTIONS_USE_EXISTING);

                if(option_default != null && option_use_existing != null){
                    JSONObject obj = new JSONObject();
                    obj.put(OPTIONS_DEFAULT, option_default).put(OPTIONS_USE_EXISTING, option_use_existing);
                    writer.append(obj.toString());
                } else
                    throw new APEnetException();
            } catch (Exception e){
                LOG.debug("Error when getting the session conversion options", e);
                JSONObject obj = new JSONObject();
                obj.put("error", true);
                writer.append(obj.toString());
            }
            writer.close();
        } catch (Exception ioe){
            LOG.error("Error opening writer", ioe);
        }
        return null;
    }

    public String saveConversionOptions() {
        try {
            Writer writer = openOutputWriter();
            try {
                String option_default = request.getParameter(OPTIONS_DEFAULT);
                String option_use_existing = request.getParameter(OPTIONS_USE_EXISTING);
                HttpSession session = request.getSession();
                session.setAttribute(OPTIONS_DEFAULT, option_default);
                session.setAttribute(OPTIONS_USE_EXISTING, option_use_existing);

                JSONObject obj = new JSONObject();
                obj.put("error", false);
                writer.append(obj.toString());
            } catch (Exception e){
                LOG.error("Error when setting the session conversion options", e);
                JSONObject obj = new JSONObject();
                obj.put("error", true);
                writer.append(obj.toString());
            }
            writer.close();
        } catch (Exception ioe){
            LOG.error("Error opening writer", ioe);
        }
        return null;
    }
}
