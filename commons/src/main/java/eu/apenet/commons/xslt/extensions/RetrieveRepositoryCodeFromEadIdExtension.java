package eu.apenet.commons.xslt.extensions;

import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

/**
 * Class for retrieve the identifier of the Archival Institution from the
 * identifier of an apeEAD.
 */
public class RetrieveRepositoryCodeFromEadIdExtension extends ExtensionFunctionDefinition {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -5996799246531865681L;

	/**
	 * Name of the function to call.
	 */
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"aiFromEad");

	private RetrieveRepositoryCodeFromEadIdCall retrieveRepositoryCodeFromEadIdCall;

	public RetrieveRepositoryCodeFromEadIdExtension() {
		this.retrieveRepositoryCodeFromEadIdCall = new RetrieveRepositoryCodeFromEadIdCall();
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public StructuredQName getFunctionQName() {
		return RetrieveRepositoryCodeFromEadIdExtension.funcname;
	}

	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return this.retrieveRepositoryCodeFromEadIdCall;
	}

	public int getMinimumNumberOfArguments() {
		return 2;
	}

	public int getMaximumNumberOfArguments() {
		return 2;
	}

	class RetrieveRepositoryCodeFromEadIdCall extends ExtensionFunctionCall {
		/**
		 * Serializable.
		 */
		private static final long serialVersionUID = 864847688629772008L;

		/**
		 * Constructor.
		 */
		public RetrieveRepositoryCodeFromEadIdCall() {
			super();
		}

		@Override
		public SequenceIterator call(SequenceIterator[] arguments, XPathContext arg1)
				throws XPathException {
			if (arguments.length == 2) {
				String firstArgValue = arguments[0].next().getStringValue();
				String secondArgValue = arguments[1].next().getStringValue();
				String value = "";
				String type = "";

				// apeEAD.
				EadDAO eadDAO = DAOFactory.instance().getEadDAO();
				Ead ead = null;

				if (firstArgValue != null && !firstArgValue.isEmpty()) {
					// Checks if exists a FA with the provided identifier.
					if (secondArgValue != null && !secondArgValue.isEmpty()) {
						// And repository code.
						ead = eadDAO.getPublishedEadByEadid(FindingAid.class, secondArgValue, firstArgValue);
						type = "fa";
					} else {
						// First FA with identifier.
						ead = eadDAO.getFirstEadByEadid(FindingAid.class, firstArgValue);
						type = "fa";
					}

					if (ead == null) {
						// Checks if exists a HG with the provided identifier.
						if (secondArgValue != null && !secondArgValue.isEmpty()) {
							// And repository code.
							ead = eadDAO.getPublishedEadByEadid(HoldingsGuide.class, secondArgValue, firstArgValue);
							type = "hg";
						} else {
							// First HG with identifier.
							ead = eadDAO.getFirstEadByEadid(HoldingsGuide.class, firstArgValue);
							type = "hg";
						}
					}

					if (ead == null) {
						// Checks if exists a SG with the provided identifier.
						if (secondArgValue != null && !secondArgValue.isEmpty()) {
							// And repository code.
							ead = eadDAO.getPublishedEadByEadid(SourceGuide.class, secondArgValue, firstArgValue);
							type = "sg";
						} else {
							// First SG with identifier.
							ead = eadDAO.getFirstEadByEadid(SourceGuide.class, firstArgValue);
							type = "sg";
						}
					}
				}

				if (ead != null) {
					String repositoryCode = "";
					if (secondArgValue != null && !secondArgValue.isEmpty()) {
						repositoryCode = secondArgValue;
					} else {
						repositoryCode = ead.getArchivalInstitution().getRepositorycode();
					}

					value = "aicode/" + repositoryCode + "/type/" + type + "/id/" + ead.getEadid();
				}

				return SingletonIterator.makeIterator(new StringValue(value));
			} else {
				return SingletonIterator.makeIterator(new StringValue("ERROR"));
			}
		}
		
	}

}
