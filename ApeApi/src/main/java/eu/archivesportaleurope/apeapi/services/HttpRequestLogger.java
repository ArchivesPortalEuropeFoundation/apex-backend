/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mahbub
 */
@FunctionalInterface
public interface HttpRequestLogger {
    /**
     * Logs a http request.
     * Warning: if you plan to read the request body make sure 
     * HttpServletRequest object's input stream can be read multiple times
     * @param request 
     */
    void log(HttpServletRequest request);
}
