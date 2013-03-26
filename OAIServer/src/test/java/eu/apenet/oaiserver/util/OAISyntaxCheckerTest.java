package eu.apenet.oaiserver.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;

import org.junit.Ignore;
import org.junit.Test;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.oaiserver.response.AbstractResponse;
import eu.apenet.oaiserver.response.ListIdentifiersResponse;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.archivesportaleurope.commons.config.ApeConfig;

public class OAISyntaxCheckerTest  extends AbstractJpaTestCase {
	public static final String UTF_8 = "utf-8";
	@Test @Ignore
	public void testProcess() throws Exception {
		APEnetUtilities.setConfig(new ApeConfig());
		APEnetUtilities.getConfig().setRepoDirPath("/home/bastiaan/apex/apexv1/data/repo");
		String url = "hi";
		Map<String, String[]> originalParams = new HashMap<String, String[]>();
		originalParams.put("verb", new String[] {"ListRecords"});
//		originalParams.put("metadataPrefix", new String[] {"edm"});
		originalParams.put("resumptionToken",  new String[] {"744"});
//		InputStream inputStream = OAISyntaxChecker.process(originalParams, url) ;
//		FileOutputStream out = new FileOutputStream(new File ("/home/bastiaan/apex/output1.xml"));
//		byte[] buffer = new byte[1024];
//		int len = inputStream.read(buffer);
//		while (len != -1) {
//		    out.write(buffer, 0, len);
//		    len = inputStream.read(buffer);
//		    if (Thread.interrupted()) {
//		        throw new InterruptedException();
//		    }
//		}
//		out.flush();
//		out.close();
//		Map<String, String> properties = new HashMap<String, String>();

		FileOutputStream out1 = new FileOutputStream(new File ("/home/bastiaan/apex/output-new.xml"));
		XMLStreamWriterHolder writerHolder = new XMLStreamWriterHolder(XMLOutputFactory.newInstance().createXMLStreamWriter(out1,UTF_8));
		RequestProcessor.process(originalParams, url, writerHolder);
//		List<Ese> eses = DAOFactory.instance().getEseDAO().findAll();
//		new ListRecordsResponse(eses).generateResponse(writerHolder, properties);
		//OAISyntaxChecker.process(originalParams, url, writerHolder);
	}

}
