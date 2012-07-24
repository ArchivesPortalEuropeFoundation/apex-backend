package eu.apenet.dashboard.webdav;

import java.io.File;
import java.io.FileFilter;
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

public class DirectoryResource extends AbstractResource implements PropFindableResource, CollectionResource,
		GetableResource {

	private File[] childs = null;
	public DirectoryResource(File file, String url) {
		super(file, url);
	}

	@Override
	public Resource child(String childName) throws NotAuthorizedException, BadRequestException {
		File child = new File(getFile(), childName);
		if (acceptRead(child)) {
			if (child.exists()) {
				if (child.isDirectory()) {
					return new DirectoryResource(child, getUrl());
				} else {
					return new FileResource(child, getUrl());
				}
			}
		}
		return null;
	}

	@Override
	public List<? extends Resource> getChildren() throws NotAuthorizedException, BadRequestException {
		List<Resource> results = new ArrayList<Resource>();
		for (File child : getChilds()) {
				if (child.isDirectory()) {
					results.add(new DirectoryResource(child, getUrl()));
				} else {
					results.add(new FileResource(child, getUrl()));
				}
		}
		return results;
	}

	@Override
	public String getUniqueId() {
		return getFile().getName();
	}

	@Override
	public String getName() {
		return getFile().getName();
	}

	@Override
	public Date getModifiedDate() {
		return new Date(getFile().lastModified());
	}

	@Override
	public String checkRedirect(Request request) {
		return null;
	}

	@Override
	public Date getCreateDate() {
		return new Date();
	}

	@Override
	public void sendContentInternal(OutputStream out, Range range, Map<String, String> params, String contentType)
			throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
		PrintWriter printWriter = new PrintWriter(out);
		printWriter.append("<html><body><table><tr><th>Name</th><th>Last modified</th><th>Size</th></tr>");
		for (File child : getChilds()) {
				String uri = getUrl() + "/" + child.getName();
				if (child.isDirectory()) {
					printWriter.append("<tr><td><b><a href=\"" + uri + "\"/>" + child.getName()
							+ "</a></b></td><td>Directory</td><td>" + new Date(child.lastModified()) + "</td><td>"
							+ child.length() + "</td></tr>");
				} else {
					printWriter.append("<tr><td><a href=\"" + uri + "\"/>" + child.getName() + "</a></td><td>File</td><td>"
							+ new Date(child.lastModified()) + "</td><td>" + child.length() + "</td></tr>");
				}
	
		}
		printWriter.append("</table></body></html>");
		printWriter.flush();
		// printWriter.close();
		out.flush();
		// out.close();
		
	
	}

	@Override
	public Long getMaxAgeSeconds(Auth auth) {
		// TODO Auto-generated method stub
		return 1l;
	}

	@Override
	public String getContentType(String accepts) {
		return "text/html";
	}

	@Override
	public Long getContentLength() {
		return Integer.valueOf(getChilds().length).longValue();
	}

	protected File[] getChilds(){
		if (childs == null){
			childs = getFile().listFiles(new FileFilter() {
				
				@Override
				public boolean accept(File file) {
					return DirectoryResource.acceptRead(file);
				}
			});
		}
		return childs;
	}
}
