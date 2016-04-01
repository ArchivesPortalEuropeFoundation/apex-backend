/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import com.google.common.net.HttpHeaders;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerConstants;
import eu.archivesportaleurope.apeapi.services.HttpRequestLogger;
import eu.archivesportaleurope.apeapi.transaction.entity.nosql.UserHttpRequest;
import eu.archivesportaleurope.apeapi.transaction.repository.mongo.UserHttpRequestRepo;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mahbub
 */
public class HttpRequstLoggerMongoImpl implements HttpRequestLogger {

    @Autowired
    private UserHttpRequestRepo userHttpRequestRepo;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void log(HttpServletRequest request) {
        String apiKey = request.getHeader(ServerConstants.HEADER_API_KEY);
        if (apiKey == null) {
            apiKey = "N/A";
        }

        String url = request.getRequestURL().toString();
        String parameterStr = request.getQueryString();
        Object paramObject = parameterStr;
        String bodyContent = "";
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            try {
                bodyContent = IOUtils.toString(request.getInputStream());
                parameterStr = bodyContent;
                paramObject = (DBObject) JSON.parse(parameterStr);
            } catch (IOException | JSONParseException ex) {
                logger.debug("Post method parameter body read exception", ex);
                logger.debug("Saving content as text/plain");
                paramObject = bodyContent;
            }
        }
        String requestMethod = request.getMethod();
        String clientIP = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        if (clientIP == null) {
            clientIP = request.getRemoteAddr();
        }

        String requestOrigin = request.getHeader(HttpHeaders.REFERER);
        String clientAgent = request.getHeader(HttpHeaders.USER_AGENT);

        UserHttpRequest userRequest = new UserHttpRequest(apiKey, url, paramObject, requestMethod, clientIP, requestOrigin, clientAgent);
        logger.debug("HttpRequest: " + userRequest);
        try {
            userHttpRequestRepo.save(userRequest);
        } catch (Exception ex) {
            logger.error("Faild to save request: " + userRequest);
            logger.debug("Mongo Exception: " + Arrays.toString(ex.getStackTrace()), ex);
        }
    }

}
