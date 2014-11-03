package eu.archivesportaleurope.webdav;

import javax.servlet.ServletException;

import io.milton.http.HttpManager;
import io.milton.servlet.Config;
import io.milton.servlet.DefaultMiltonConfigurator;

public class ApeMiltonConfigurator extends DefaultMiltonConfigurator {

	@Override
	public HttpManager configure(Config config) throws ServletException {
		this.builder.setEnableFormAuth(false);
		this.builder.setEnableDigestAuth(true);
		return super.configure(config);
	}

}
