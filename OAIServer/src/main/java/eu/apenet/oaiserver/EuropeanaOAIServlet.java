package eu.apenet.oaiserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;

public class EuropeanaOAIServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5447972081653456478L;
	private static final String REQUEST_SUFIX = "/europeana";
	public static final String UTF_8 = "utf-8";
	private static Logger LOGGER = Logger.getLogger(EuropeanaOAIServlet.class);
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getScheme()+"://"+request.getHeader("Host") + request.getContextPath() + REQUEST_SUFIX;
		String remoteHost = request.getRemoteHost()+ ": ";
		String logline = remoteHost + url;
		if (StringUtils.isNotBlank(request.getQueryString())){
			logline += "?" + request.getQueryString();
		}
		LOGGER.info(logline);
		response.setBufferSize(4096);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");

		String acceptEncoding = request.getHeader("Accept-Encoding");
		LOGGER.info(remoteHost + "accepts " + acceptEncoding);
		OutputStream outputStream = null;
		if (acceptEncoding.contains("gzip")){
			response.setHeader("Content-Encoding", "gzip");
			outputStream = new GZIPOutputStream(response.getOutputStream());
		}else {
			outputStream = response.getOutputStream();
		}
		XMLStreamWriterHolder writerHolder;
		try {
			writerHolder = new XMLStreamWriterHolder(XMLOutputFactory.newInstance()
					.createXMLStreamWriter(outputStream, UTF_8));
			RequestProcessor.process(request.getParameterMap(), url, writerHolder);
		} catch (XMLStreamException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ServletException(e);
		} catch (FactoryConfigurationError e) {
			LOGGER.error(e.getMessage(), e);
			throw new ServletException(e);
		}
		
		outputStream.flush();
		outputStream.close();
	}


}
