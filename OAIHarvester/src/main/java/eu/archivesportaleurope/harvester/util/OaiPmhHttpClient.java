package eu.archivesportaleurope.harvester.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import eu.archivesportaleurope.harvester.oaipmh.auth.JCIFSNTLMSchemeFactory;
import eu.archivesportaleurope.harvester.oaipmh.exception.HarvesterConnectionException;

public class OaiPmhHttpClient {
	private static final int TIMEOUT = 3600000;
	private static final Logger LOGGER = Logger.getLogger(OaiPmhHttpClient.class);
	private CloseableHttpClient httpClient;
	private HttpClientContext context;
	public OaiPmhHttpClient (){
		if (Security.getProperty(BouncyCastleProvider.PROVIDER_NAME)== null){
			Security.addProvider(new BouncyCastleProvider());			
		}		
		RequestConfig defaultRequestConfig = RequestConfig.custom()
			    .setSocketTimeout(TIMEOUT)
			    .setConnectTimeout(TIMEOUT)
			    .setConnectionRequestTimeout(TIMEOUT)
			    .setStaleConnectionCheckEnabled(true)
			    .build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig).useSystemProperties().build();
	}
	
	public OaiPmhHttpClient (String proxyServer, String username, String password){
		if (Security.getProperty(BouncyCastleProvider.PROVIDER_NAME)== null){
			Security.addProvider(new BouncyCastleProvider());			
		}
    	String scheme = "http";
    	String hostname = null;
    	int port = 80;
    	int schemeIndex = proxyServer.indexOf("//");
    	if (schemeIndex > 0){
    		scheme = proxyServer.substring(0, schemeIndex-1);
    		proxyServer = proxyServer.substring(schemeIndex+2);
    	}
    	
    	int portIndex = proxyServer.indexOf(":");
    	if (portIndex > 0){
    		hostname = proxyServer.substring(0,portIndex);
    		proxyServer = proxyServer.substring(portIndex+1);
    		port  = Integer.parseInt(proxyServer);
    	}else {
    		hostname = proxyServer;
    	}
		HttpHost httpProxy = new HttpHost(hostname, port, scheme);
    	//AuthScope authScope = new AuthScope(httpProxy);


    	Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
    	        .register(AuthSchemes.NTLM, new JCIFSNTLMSchemeFactory())
    	        .register(AuthSchemes.BASIC, new BasicSchemeFactory())
    	        .register(AuthSchemes.DIGEST, new DigestSchemeFactory())
    	        .build();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
        	Credentials defaultcreds = new NTCredentials(username+":" + password);
        	credsProvider.setCredentials(AuthScope.ANY, defaultcreds);
        }


		RequestConfig defaultRequestConfig = RequestConfig.custom()
			    .setSocketTimeout(TIMEOUT)
			    .setConnectTimeout(TIMEOUT)
			    .setConnectionRequestTimeout(TIMEOUT)
			    .setStaleConnectionCheckEnabled(true).setProxy(httpProxy)
			    .build();
    	// Add AuthCache to the execution context
    	context = HttpClientContext.create();
    	context.setCredentialsProvider(credsProvider);
    	context.setRequestConfig(defaultRequestConfig);
    	context.setAuthSchemeRegistry(authSchemeRegistry);
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig).useSystemProperties()
				.setDefaultAuthSchemeRegistry(authSchemeRegistry).build();
	}
	public CloseableHttpResponse get(String url) throws HarvesterConnectionException{

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("requestURL=" + url);
		}
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", getName() + "/" + getVersion());
		httpGet.setHeader("Accept-Encoding", "compress, gzip, identify");
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet, context);
			StatusLine statusLine= response.getStatusLine();
			if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
				throw new IOException("HTTP response: " + statusLine.getReasonPhrase());
			}
			return response;
		}catch (Exception e){
			throw new HarvesterConnectionException(url, e);
		}
	}
	public void close() throws IOException{
		httpClient.close();
	}

	protected String getName(){
		return "Archives Portal Europe OAI-PMH Harvester";
	}
	public static String getVersion() {
		String version = OaiPmhHttpClient.class.getPackage().getImplementationVersion();
		if (StringUtils.isBlank(version)) {
			version = "DEV-VERSION";
		}
		return version;
	}
	public InputStream getResponseInputStream(CloseableHttpResponse response) throws IOException {
		Header[] contentEncodingHeader = response.getHeaders("Content-Encoding");
		String contentEncoding = "unknown";
		if (contentEncodingHeader != null && contentEncodingHeader.length > 0){
			contentEncoding = contentEncodingHeader[0].getValue();
		}
		LOGGER.debug("contentEncoding=" + contentEncoding);
		InputStream result = null;
		if ("compress".equals(contentEncoding)) {
			ZipInputStream zis = new ZipInputStream(response.getEntity().getContent());
			zis.getNextEntry();
			result = zis;
		} else if ("gzip".equals(contentEncoding)) {
			result = new GZIPInputStream(response.getEntity().getContent());
		} else if ("deflate".equals(contentEncoding)) {
			result = new InflaterInputStream(response.getEntity().getContent());
		}else {
			result = response.getEntity().getContent();
		}
		return result;
	}
}
