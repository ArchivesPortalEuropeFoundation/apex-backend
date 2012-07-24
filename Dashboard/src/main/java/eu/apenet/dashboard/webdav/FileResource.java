package eu.apenet.dashboard.webdav;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.LockToken;
import com.bradmcevoy.http.PropFindableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Resource;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;

public class FileResource extends AbstractResource implements GetableResource,PropFindableResource  {
    


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
    public void sendContentInternal(OutputStream out, Range range, Map<String, String> params, String contentType) throws IOException {
        FileInputStream fis = new FileInputStream(getFile());
        BufferedInputStream bin = new BufferedInputStream(fis);
        final byte[] buffer = new byte[ 1024 ];
        int n = 0;
        while( -1 != (n = bin.read( buffer )) ) {
            out.write( buffer, 0, n );
        }
        fis.close();
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

	public LockToken getLockToken()
	{
		return null;
	}

	@Override
	public Date getCreateDate() {
		// TODO Auto-generated method stub
		return new Date(getFile().lastModified());
	}

}
