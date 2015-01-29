package eu.apenet.dashboard.ead2edm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.ead2edm.EdmFileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;

/**
 * Servlet implementation class EseHTMLServlet
 */
public class EseFileServlet extends HttpServlet {
    protected Logger logger = Logger.getLogger(getClass());
	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EseFileServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Ese ese = getEse(request);
		if (ese != null) {

			if (ese.getPathHtml() == null) {
				try {
					EAD2EDMConverter.generateHtml(ese);
				} catch (TransformerException e) {
					logger.error(e.getMessage(),e);
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not create html files.");
				}
			}
			if (ese.getPathHtml() != null) {
				File file = EdmFileUtils.getRepoFile(APEnetUtilities.getConfig().getRepoDirPath(), ese.getPathHtml());
				if (!file.exists()) {
					// Do your thing if the file appears to be non-existing.
					// Throw an exception, or send 404, or show default/warning
					// page, or just ignore it.
					response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
					return;
				} else {
					String path = request.getParameter("path");
					if (StringUtils.isBlank(path)) {
						write(file, response);
					} else {
						File parentDir = file.getParentFile();
						File subHtmlFile = EdmFileUtils.getFile(parentDir, path);
						if (!subHtmlFile.exists()) {
							// Do your thing if the file appears to be
							// non-existing.
							// Throw an exception, or send 404, or show
							// default/warning page, or just ignore it.
							response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
							return;
						}
						write(subHtmlFile, response);
					}
				}
			}

		} else {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No parameter id or eseId given.");
		}
	}

	private Ese getEse(HttpServletRequest request) {
		String eseId = request.getParameter("eseId");
		String id = request.getParameter("id");
		Ese ese = null;
		EseDAO dao = DAOFactory.instance().getEseDAO();
		if (NumberUtils.isNumber(id)) {
//		TODO: this class should be move to Struts Action
//			List<Ese> eses = dao.getEses(NumberUtils.toInt(id));
//			if (eses.size() > 0) {
//				ese = eses.get(0);
//			}
		} else if (NumberUtils.isNumber(eseId)) {
			ese = dao.findById(NumberUtils.toInt(eseId));
		}
		return ese;
	}

	protected void write(File file, HttpServletResponse response) throws IOException {
		// Check if file actually exists in filesystem.

		// Get content type by filename.
		String contentType = getServletContext().getMimeType(file.getName());

		// If content type is unknown, then set the default value.
		// For all content types, see:
		// http://www.w3schools.com/media/media_mimeref.asp
		// To add new content types, add new mime-mapping entry in web.xml.
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		// Init servlet response.
		response.reset();
		response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setContentType(contentType);
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

		// Prepare streams.
		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		try {
			// Open streams.
			input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
			output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

			// Write file contents to response.
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
		} finally {
			// Gently close streams.
			close(output);
			close(input);
		}
	}

	// Helpers (can be refactored to public utility class)
	// ----------------------------------------

	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				// Do your thing with the exception. Print it, log it or mail
				// it.
			}
		}
	}

}
