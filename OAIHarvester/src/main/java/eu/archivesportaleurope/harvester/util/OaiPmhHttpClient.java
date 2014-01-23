package eu.archivesportaleurope.harvester.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

public class OaiPmhHttpClient {
	private static final int TIMEOUT = 300000;
	private static final Logger LOGGER = Logger.getLogger(OaiPmhHttpClient.class);
	private CloseableHttpClient httpClient;
	public OaiPmhHttpClient (){
		RequestConfig defaultRequestConfig = RequestConfig.custom()
			    .setSocketTimeout(TIMEOUT)
			    .setConnectTimeout(TIMEOUT)
			    .setConnectionRequestTimeout(TIMEOUT)
			    .setStaleConnectionCheckEnabled(true)
			    .build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(defaultRequestConfig).useSystemProperties().build();
	}
	public CloseableHttpResponse get(String url) throws Exception{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("requestURL=" + url);
		}
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", "Archives Portal Europe OAI-PMH Harvester/" + getVersion());
		httpGet.setHeader("Accept-Encoding", "compress, gzip, identify");
	
		CloseableHttpResponse response = httpClient.execute(httpGet);
		StatusLine statusLine= response.getStatusLine();
		if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
			throw new IOException("HTTP response: " + statusLine.getReasonPhrase());
		}
		return response;
	}
	public void close() throws IOException{
		httpClient.close();
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
