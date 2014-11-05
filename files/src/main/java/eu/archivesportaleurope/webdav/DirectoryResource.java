package eu.archivesportaleurope.webdav;

import io.milton.http.Auth;
import io.milton.http.Range;
import io.milton.http.Request;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.exceptions.NotFoundException;
import io.milton.resource.CollectionResource;
import io.milton.resource.FolderResource;
import io.milton.resource.Resource;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class DirectoryResource extends AbstractResource implements FolderResource	 {

	private File[] childs = null;
	private String name;
	public DirectoryResource(File file, String url) {
		super(file, url);
		name = getName(file);
	}
	private String getName(File file){
		if (file.isDirectory() && StringUtils.isNumeric(file.getName())){
			// make relative path
			String path = file.getAbsolutePath();
			if (path.startsWith(APEnetUtilities.getConfig().getRepoDirPath())) {
				path = path.substring(APEnetUtilities.getConfig().getRepoDirPath().length());
			}
			if (path.length() > 0) {
				path = path.substring(1);
				if (path.split(APEnetUtilities.FILESEPARATOR).length == 2){
					Integer aiId = Integer.parseInt(file.getName());
					ArchivalInstitution ai = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(aiId);
					if (ai != null){
						return file.getName() + "-" + APEnetUtilities.convertToFilename(ai.getAiname());
					}
				}

			}
		}


		return file.getName();
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
		return name;
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
	private static String toFileSize(Long fileSize) {
		if (fileSize == null) {
			return null;
		}
		long size = fileSize.longValue();
		if (size > 1024 * 1024) {
			return size / (1024 * 1024) + " MB";
		} else if (size > 1024) {
			return size / 1024 + " KB";
		} else {
			return size + " B";
		}
	}

	@Override
	public void sendContentInternal(OutputStream out, Range range, Map<String, String> params, String contentType)
			throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
		PrintWriter printWriter = new PrintWriter(out);
		printWriter.append("<html><body><table  border=\"1\" cellpadding=\"10\"><tr><th>Name</th><th>Type</th><th>Last modified</th><th>Size</th></tr>");
		String baseUrl = getUrl();
		if (!getUrl().endsWith("/")){
			baseUrl = baseUrl + "/";
		}
		for (File child : getChilds()) {
			String name = getName(child);
				String uri =baseUrl + name;
				if (child.isDirectory()) {
					printWriter.append("<tr><td><b><a href=\"" + uri + "\"/>" + name
							+ "</a></b></td><td>Directory</td><td>" + new Date(child.lastModified()) + "</td><td></td></tr>");
				} else {
					printWriter.append("<tr><td><a href=\"" + uri + "\"/>" + name + "</a></td><td>File</td><td>"
							+ new Date(child.lastModified()) + "</td><td  ALIGN=\"right\">" + toFileSize(child.length()) + "</td></tr>");
				}
	
		}
		printWriter.append("</table></body></html>");
		printWriter.flush();
		// 
		out.flush();
		printWriter.close();
		out.close();

	
	}
	

	@Override
	public Long getMaxAgeSeconds(Auth auth) {
		return 30l;
	}

	@Override
	public String getContentType(String accepts) {
		return "text/html";
	}

	@Override
	public Long getContentLength() {
		return -1l;
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
	@Override
	public Resource createNew(String newName, InputStream inputStream, Long length, String contentType)
			throws IOException, ConflictException, NotAuthorizedException, BadRequestException {
		throw new NotAuthorizedException();
	}
	@Override
	public CollectionResource createCollection(String newName) throws NotAuthorizedException, ConflictException,
			BadRequestException {
		throw new NotAuthorizedException();
	}

}
