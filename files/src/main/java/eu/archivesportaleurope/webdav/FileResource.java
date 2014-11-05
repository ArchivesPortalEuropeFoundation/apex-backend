package eu.archivesportaleurope.webdav;

import io.milton.http.Auth;
import io.milton.http.FileItem;
import io.milton.http.LockToken;
import io.milton.http.Range;
import io.milton.http.Request;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.resource.Resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

public class FileResource extends AbstractResource implements io.milton.resource.FileResource {

	public FileResource(File file, String url) {
		super(file, url);
	}

	@Override
	public String getUniqueId() {
		return getUrl().hashCode() + "";
	}

	public int compareTo(Resource res) {
		return this.getName().compareTo(res.getName());
	}

	@Override
	public void sendContentInternal(OutputStream out, Range range, Map<String, String> params, String contentType)
			throws IOException {
		if (getFile().exists()) {
			FileInputStream fis = new FileInputStream(getFile());
			BufferedInputStream bin = new BufferedInputStream(fis);
			final byte[] buffer = new byte[1024];
			int n = 0;
			while (-1 != (n = bin.read(buffer))) {
				out.write(buffer, 0, n);
			}
			fis.close();
		}
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
	public Long getContentLength() {
		return getFile().length();
	}

	@Override
	public String getContentType(String preferredList) {
		return WebDavResourceFactory.getContentType(getFile());
	}

	public String checkRedirect(Request request) {
		return null;
	}

	public Long getMaxAgeSeconds(Auth auth) {
		return 1l;
	}

	public LockToken getLockToken() {
		return null;
	}

	@Override
	public Date getCreateDate() {
		// TODO Auto-generated method stub
		return new Date(getFile().lastModified());
	}

	@Override
	public String processForm(Map<String, String> parameters, Map<String, FileItem> files) throws BadRequestException,
			NotAuthorizedException, ConflictException {
		throw new NotAuthorizedException();
	}

}
