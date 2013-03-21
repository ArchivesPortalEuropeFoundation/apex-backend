package eu.apenet.oaiserver.action;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;

public class VerbAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final String REQUEST_SUFIX = "/request";
	public static final String UTF_8 = "utf-8";
	/**
	 * 
	 */
	private static final long serialVersionUID = -1614412384938099856L;
	private static Logger LOG = Logger.getLogger(VerbAction.class);
	private InputStream inputStream;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;

	}

	/**
	 * It's the unique action that must be called in server.
	 */
	public String execute() {
		try {
			String url = request.getContextPath() + REQUEST_SUFIX;
			;
			LOG.info(request.getUserPrincipal() + ": " + url + request.getQueryString());
			String verb = request.getParameter("verb");
			OutputStream outputStream = new GZIPOutputStream(response.getOutputStream());
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml");
			response.setHeader("Content-Encoding", "gzip");
			XMLStreamWriterHolder writerHolder = new XMLStreamWriterHolder(XMLOutputFactory.newInstance()
					.createXMLStreamWriter(outputStream, UTF_8));
			RequestProcessor.process(request.getParameterMap(), url, writerHolder);
			outputStream.flush();
			outputStream.close();
			return null;
		} catch (Exception e) {
			LOG.error("Error trying to check verb and params in OAISyntaxChecker.check.", e);
		}
		return SUCCESS;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

}
