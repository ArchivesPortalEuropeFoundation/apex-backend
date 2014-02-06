package eu.apex.eaccpf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author jara
 */
public class ContentUtils {

    public static final String MIME_TYPE_APPLICATION_XML = "application/xml";
    private static final Logger LOGGER = Logger.getLogger(ContentUtils.class);

    public static void downloadXml(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
        download(request, response, file, MIME_TYPE_APPLICATION_XML);
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, File file, String contentType) throws IOException {
        if (file.exists()) {
            response.setContentLength((int) file.length());
            FileInputStream inputStream = new FileInputStream(file);  //read the file
            download(request, response, inputStream, file.getName(), contentType);
        } else {
            LOGGER.error("File does not exist: " + file);
        }
    }

    public static void download(HttpServletRequest request, HttpServletResponse response, InputStream inputStream, String name, String contentType) throws IOException {
        String browserType = (String) request.getHeader("User-Agent");
        if (MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0) {
            response.setContentType("application/file-download");
        } else {
            response.setContentType(contentType);
        }

        response.setBufferSize(4096);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-disposition", "attachment;filename=" + name);
        OutputStream outputStream = response.getOutputStream();
        try {
            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write(c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            outputStream.flush();
            outputStream.close();
        }
    }

    public static PrintWriter getWriterToDownload(HttpServletRequest request, HttpServletResponse response, String name, String contentType) throws IOException {
        String browserType = (String) request.getHeader("User-Agent");
        if (MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0) {
            response.setContentType("application/file-download");
        } else {
            response.setContentType(contentType);
        }

        response.setBufferSize(4096);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-disposition", "attachment;filename=" + name);
        return new PrintWriter(response.getOutputStream());
    }

    public static OutputStream getOutputStreamToDownload(HttpServletRequest request, HttpServletResponse response, String name, String contentType) throws IOException {
        String browserType = (String) request.getHeader("User-Agent");
        if (MIME_TYPE_APPLICATION_XML.equals(contentType) && browserType.indexOf("MSIE") > 0) {
            response.setContentType("application/file-download");
        } else {
            response.setContentType(contentType);
        }

        response.setBufferSize(4096);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-disposition", "attachment;filename=" + name);
        return response.getOutputStream();
    }
}
