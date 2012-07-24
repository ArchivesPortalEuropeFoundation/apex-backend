package eu.apenet.dashboard.webdav;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import com.bradmcevoy.common.Path;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.ResourceFactory;

import eu.apenet.commons.utils.APEnetUtilities;

public class WebDavResourceFactory implements ResourceFactory {

	private static final String CATALINA_BASE = "catalina.base";
	private static final String ADMIN = "admin";
	private static MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();

	@Override
	public Resource getResource(String host, String url) {
		String basePath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
		Path path = Path.path(url);
		String rootPath = path.getStripFirst().toPath().substring(1);
		path = path.getStripFirst().getStripFirst();
		if (ADMIN.equals(path.getFirst())) {
			path = path.getStripFirst();
			basePath = System.getProperty(CATALINA_BASE);
			rootPath = ADMIN;
		}
		File file = new File(basePath, path.toPath());
		if (path.isRoot()) {
			return new RootDirectoryResource(file, url,rootPath);
		} else {
			if (file.isDirectory()) {
				return new DirectoryResource(file, url);
			} else {
				return new FileResource(file, url);
			}
		}
	}

	public static String getContentType(File file) {
		return mimeTypes.getContentType(file);
	}

}
