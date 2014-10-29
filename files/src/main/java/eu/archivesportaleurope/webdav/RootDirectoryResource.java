package eu.archivesportaleurope.webdav;

import java.io.File;

public class RootDirectoryResource extends DirectoryResource{

	private String name;
	public RootDirectoryResource(File file, String url, String name) {
		super(file, url);
		this.name = name;
	}



	@Override
	public String getUniqueId() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}



}
