package eu.apenet.dashboard.webdav;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.bradmcevoy.common.Path;
import com.bradmcevoy.http.Auth;
import com.bradmcevoy.http.GetableResource;
import com.bradmcevoy.http.MiltonServlet;
import com.bradmcevoy.http.PropFindableResource;
import com.bradmcevoy.http.Range;
import com.bradmcevoy.http.Request;
import com.bradmcevoy.http.Request.Method;
import com.bradmcevoy.http.exceptions.BadRequestException;
import com.bradmcevoy.http.exceptions.NotAuthorizedException;
import com.bradmcevoy.http.exceptions.NotFoundException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.DashboardAPEnetException;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.security.SecurityService;
import eu.apenet.dashboard.security.SecurityService.LoginResult;

public abstract class AbstractResource implements PropFindableResource, GetableResource {
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

		} catch (DashboardAPEnetException e) {
			logger.error("Error while trying to login with name: " + user);
			return null;
		}

	}

	@Override
	public final boolean authorise(Request request, Method method, Auth auth) {
		if (Method.OPTIONS.equals(method)) {
			return true;
		} else if (auth != null && auth.getTag() != null) {
			logger.info(auth.getUser() + " " + method);
			return true;
		}
		return false;
	}

	@Override
	public final String getRealm() {
		return "Dashboard Archives Portal Europe";
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
			if (path.startsWith(APEnetUtilities.getDashboardConfig().getRepoDirPath())) {
				path = path.substring(APEnetUtilities.getDashboardConfig().getRepoDirPath().length());
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
	public final void sendContent(OutputStream out, Range range, Map<String, String> params, String contentType)
			throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
		if (acceptRead(getFile())) {
			sendContentInternal(out, range, params, contentType);
		} else {
			throw new NotFoundException("not found");
		}

	}

	protected abstract void sendContentInternal(OutputStream out, Range range, Map<String, String> params,
			String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException;

}
