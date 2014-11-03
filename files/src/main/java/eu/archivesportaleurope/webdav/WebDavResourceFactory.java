package eu.archivesportaleurope.webdav;

import io.milton.common.Path;
import io.milton.http.ResourceFactory;
import io.milton.resource.Resource;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import eu.apenet.commons.utils.APEnetUtilities;

public class WebDavResourceFactory implements ResourceFactory {

	private static MimetypesFileTypeMap mimeTypes = new MimetypesFileTypeMap();

	@Override
	public Resource getResource(String host, String url) {
		String basePath = APEnetUtilities.getConfig().getRepoDirPath();
		Path path = Path.path(url);
		String rootPath ="";
		if (url.startsWith("/files")){
			rootPath = path.getStripFirst().toPath();
			path = path.getStripFirst();
		}else {
			rootPath = path.toPath();
		}
		String relativeUrl =null;
		if (path.isRoot()){
			relativeUrl ="";
		}else {
			String[] parts = path.getParts();
			for (int i = 0; i < parts.length;i++){
				String temp = "";
				if (i == 1){
					int index = parts[i].indexOf("-");
					if (index > 0){
						temp = parts[i].substring(0,index);
					}
				}else {
					temp = parts[i];
				}
				if (relativeUrl == null){
					relativeUrl = temp;
				}else {
					relativeUrl += APEnetUtilities.FILESEPARATOR + temp;
				}
			}
		}
		File file = new File(basePath, relativeUrl);
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
