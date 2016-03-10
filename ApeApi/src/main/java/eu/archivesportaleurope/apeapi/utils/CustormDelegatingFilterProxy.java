/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.utils;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 *
 * @author mahbub
 */
@Component
public class CustormDelegatingFilterProxy extends DelegatingFilterProxy {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest currentRequest = (HttpServletRequest) servletRequest;
        MultiReadHttpServletRequestWrapper wrappedRequest = new MultiReadHttpServletRequestWrapper(currentRequest);
        super.doFilter(wrappedRequest, response, chain);
    }
}
