/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.transaction.entity.nosql;

/**
 *
 * @author mahbub
 */
public class UserHttpRequest extends BaseEntityNoSql {
    private String ApiKey;
    private String url;
    private Object parameter;
    private String requestMethod;
    private String clientIP;
    private String requestOrigin;
    private String clientAgent;
    
    /**
     * Default constructor
     */
    public UserHttpRequest() {
    }

    public UserHttpRequest(String ApiKey, String url, Object parameter, String requestMethod, String clientIP, String requestOrigin, String clientAgent) {
        this.ApiKey = ApiKey;
        this.url = url;
        this.parameter = parameter;
        this.requestMethod = requestMethod;
        this.clientIP = clientIP;
        this.requestOrigin = requestOrigin;
        this.clientAgent = clientAgent;
    }
    

    public String getApiKey() {
        return ApiKey;
    }

    public void setApiKey(String ApiKey) {
        this.ApiKey = ApiKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getRequestOrigin() {
        return requestOrigin;
    }

    public void setRequestOrigin(String requestOrigin) {
        this.requestOrigin = requestOrigin;
    }

    public String getClientAgent() {
        return clientAgent;
    }

    public void setClientAgent(String clientAgent) {
        this.clientAgent = clientAgent;
    }

    @Override
    public String toString() {
        return "UserHttpRequest{" + "id=" + this.getId() + ", ApiKey=" + ApiKey + ", url=" + url + ", parameter=" + parameter + ", requestMethod=" + requestMethod + ", clientIP=" + clientIP + ", requestOrigin=" + requestOrigin + ", clientAgent=" + clientAgent + ", date=" + this.getDate() + '}';
    }
    
}
