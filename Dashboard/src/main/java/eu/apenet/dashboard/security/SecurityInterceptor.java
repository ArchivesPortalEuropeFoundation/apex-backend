package eu.apenet.dashboard.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import eu.apenet.commons.exceptions.APEnetRuntimeException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.AbstractInstitutionAction;

public class SecurityInterceptor extends AbstractInterceptor implements Serializable {
	private static final String INSTITUTION_REQUIRED = "institution_required";
	private static final String UNKNOWN = "unknown";
	private static final String AUTHORIZATION_XML_FILE = "authorization.xml";
	private static final String COMMA_SEPARATOR = ",";
	private static final String NOROLE = "norole";
	private static final Logger LOGGER = Logger.getLogger(SecurityInterceptor.class);
	private final static XPath XPATH = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();
	private static final long serialVersionUID = -2689741944917203870L;
	private Map<String, Set<String>> groupsOfActions = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> roleMapping = new HashMap<String, Set<String>>();
	private static final String AUTHORIZATION_REQUIRED = "authorization_required";
	private static XPathExpression groupExpression;
	private static XPathExpression roleMappingExpression;
	private static XPathExpression nameAttributeExpression;
	private static XPathExpression groupsAttributeExpression;
	static {
		try {
			groupExpression = XPATH.compile("/authorization/groups/group");
			roleMappingExpression = XPATH.compile("/authorization/role-mappings/role-mapping");
			nameAttributeExpression = XPATH.compile("./@name");
			groupsAttributeExpression = XPATH.compile("./@groups");
		} catch (XPathExpressionException e) {
			LOGGER.fatal(e.getMessage(), e);
		}
	}

	public SecurityInterceptor() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			ClassLoader classLoader = (ClassLoader) Thread.currentThread().getContextClassLoader();

			Document doc = builder.parse(classLoader.getResourceAsStream(AUTHORIZATION_XML_FILE));
			doc.getDocumentElement().normalize();

			NodeList groupNodeList = (NodeList) groupExpression.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < groupNodeList.getLength(); i++) {
				Node node = groupNodeList.item(i);
				String text = removeUnusedCharacters(node.getTextContent());
				String groupName = (String) nameAttributeExpression.evaluate(node, XPathConstants.STRING);
				String[] actionsSplitted = text.split(COMMA_SEPARATOR);
				Set<String> actions = new TreeSet<String>();
				for (int j = 0; j < actionsSplitted.length; j++) {
					String action = actionsSplitted[j].trim();
					boolean found = false;
					for (Map.Entry<String, Set<String>> entry : groupsOfActions.entrySet()) {
						found = found || entry.getValue().contains(action);
					}
					if (found) {
						LOGGER.warn("action " + action + " in group " + groupName + " already defined in another group");
					}
					actions.add(action);
				}
				groupsOfActions.put(groupName, actions);
			}

			NodeList roleMappingNodeList = (NodeList) roleMappingExpression.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < roleMappingNodeList.getLength(); i++) {
				Node node = roleMappingNodeList.item(i);
				String name = (String) nameAttributeExpression.evaluate(node, XPathConstants.STRING);
				String groupsString = (String) groupsAttributeExpression.evaluate(node, XPathConstants.STRING);
				String[] groupsSplitted = groupsString.split(COMMA_SEPARATOR);
				Set<String> groups = new TreeSet<String>();
				for (int j = 0; j < groupsSplitted.length; j++) {
					groups.add(groupsSplitted[j].trim());
				}
				roleMapping.put(name, groups);
			}
		} catch (Exception e) {
			throw new APEnetRuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		final String actionName = invocation.getProxy().getActionName();
		final SecurityContext securityContext = SecurityContext.get();
		String emailAddress = UNKNOWN;
		String role = NOROLE;
		if (securityContext != null){
			emailAddress = securityContext.getEmailAddress();
			role = securityContext.getRole();
		}
		if (isAllowed(emailAddress, role, actionName)) {
			if (invocation.getProxy().getAction() instanceof AbstractInstitutionAction){
				if (securityContext == null || securityContext.getSelectedInstitution() == null){
					LOGGER.info("user: " + emailAddress + " with role: " + role + " has NO institution selected, when accessing: " + invocation.getProxy().getAction().getClass().getName());
					return INSTITUTION_REQUIRED;
				}
			}
			return invocation.invoke();
		};
		return AUTHORIZATION_REQUIRED;
	}

	public boolean isAllowed(String emailAddress, String role, String actionName){
		Set<String> groups = roleMapping.get(role);
		Iterator<String> groupsIterator =  groups.iterator();
		boolean found = false;
		String group = null;
		while (!found && groupsIterator.hasNext()){
			 group = groupsIterator.next();
			Set<String> actions = groupsOfActions.get(group);
			if (actions == null){
				LOGGER.error("Group " + group + " not found in authorization file");
			}else {
				found = found || actions.contains(actionName);
			}
		}
		if (found) {
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("user: " + emailAddress + " with role: " + role + " allowed to access: " + actionName + " (group: " + group + ")");
			}
		}else {
			LOGGER.info("user: " + emailAddress + " with role: " + role + " has NO access to: " + actionName);
		}
		return found;
	}


	private static String removeUnusedCharacters(String input) {
		if (input != null) {
			String result = input.replaceAll("[\t ]+", " ");
			result = result.replaceAll("[\n\r]+", "");
			return result.trim();
		} else {
			return null;
		}

	}
}