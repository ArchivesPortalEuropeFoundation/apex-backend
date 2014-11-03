package eu.apenet.dashboard.webdav;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.CollectionResource;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.PropFindableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Resource;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.bradmcevoy.http.exceptions.NotFoundException;

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
