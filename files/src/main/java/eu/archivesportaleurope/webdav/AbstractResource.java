package eu.archivesportaleurope.webdav;

import io.milton.http.Auth;
import io.milton.http.Range;
import io.milton.http.Request;
import io.milton.http.Request.Method;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.exceptions.NotFoundException;
import io.milton.resource.CollectionResource;
import io.milton.resource.CopyableResource;
import io.milton.resource.DeletableResource;
import io.milton.resource.GetableResource;
import io.milton.resource.MoveableResource;
import io.milton.resource.PropFindableResource;
import io.milton.servlet.MiltonServlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.archivesportaleurope.webdav.security.SecurityContext;
import eu.archivesportaleurope.webdav.security.SecurityService;
import eu.archivesportaleurope.webdav.security.SecurityService.LoginResult;

public abstract class AbstractResource implements CopyableResource, DeletableResource, GetableResource, MoveableResource,  PropFindableResource{
	protected final Logger logger = Logger.getLogger(this.getClass());

	private File file;
	private String url;

	public AbstractResource(File file, String url) {
		this.file = file;
		this.url = url;
	}

	@Override
	public final Object authenticate(String user, String password) {
		try {
			HttpServletRequest request = MiltonServlet.request();
			SecurityContext securityContext = getSecurityContext();
			if (securityContext == null) {
				LoginResult loginResult = SecurityService.webDavLogin(user, password, request);
				securityContext = loginResult.getContext();
			}
			return securityContext;

		} catch (NotAuthorizedException e) {
			logger.error("Error while trying to login with name: " + user);
			return null;
		}

	}

	@Override
	public final boolean authorise(Request request, Method method, Auth auth) {
		
		if (auth != null && auth.getTag() != null) {
			logger.info(auth.getUser() + " " + method + " " + request.getAbsolutePath());
			return true;
		}else {
			logger.info("anonymous " + method + " " + request.getAbsolutePath());
		}
		return false;
	}

	@Override
	public final String getRealm() {
		return "Files at Archives Portal Europe";
	}

	protected File getFile() {
		return file;
	}

	protected String getUrl() {
		return url;
	}

	protected static SecurityContext getSecurityContext() {
		HttpServletRequest request = MiltonServlet.request();
		return SecurityContext.getWebDavContext(request);
	}

	protected static boolean acceptRead(File file) {
		SecurityContext securityContext = getSecurityContext();
		if (securityContext.isAdmin()) {
			return true;
		} else {
			// make relative path
			String path = file.getAbsolutePath();
			if (path.startsWith(APEnetUtilities.getConfig().getRepoDirPath())) {
				path = path.substring(APEnetUtilities.getConfig().getRepoDirPath().length());
			}
			// everyone authenticated user may see the base dir.
			if (path.length() == 0) {
				return true;
			}
			String countryPath = APEnetUtilities.FILESEPARATOR + securityContext.getCountryIsoname();
			if (securityContext.isCountryManager() && path.startsWith(countryPath)) {
				return true;
			} else if (securityContext.isInstitutionManager()) {
				if (path.startsWith(countryPath)) {
					path = path.substring(countryPath.length());
					if (path.length() == 0) {
						return true;
					} else {
						boolean match = false;
						Iterator<Integer> aiIds = securityContext.getAiIds().iterator();
						while (!match && aiIds.hasNext()) {
							Integer aiId = aiIds.next();
							String aiPath = APEnetUtilities.FILESEPARATOR + aiId;
							if (path.startsWith(aiPath)) {
								return true;
							}
						}
					}
				}

			}
		}
		return false;
	}

	@Override
	public void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType)
			throws IOException, io.milton.http.exceptions.NotAuthorizedException, BadRequestException,
			NotFoundException {
		if (acceptRead(getFile())) {
		sendContentInternal(out, range, params, contentType);
	} else {
		throw new BadRequestException("Not existing file");
	}
		
	}

	protected abstract void sendContentInternal(OutputStream out, Range range, Map<String, String> params,
			String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException;

	@Override
	public void delete() throws NotAuthorizedException{
		throw new NotAuthorizedException();
	}

	@Override
	public void moveTo(CollectionResource rDest, String name) throws ConflictException, NotAuthorizedException,
			BadRequestException {
		throw new NotAuthorizedException();
		
	}

	@Override
	public void copyTo(CollectionResource toCollection, String name) throws NotAuthorizedException,
			BadRequestException, ConflictException {
		throw new NotAuthorizedException();
		
	}

	
	

}
