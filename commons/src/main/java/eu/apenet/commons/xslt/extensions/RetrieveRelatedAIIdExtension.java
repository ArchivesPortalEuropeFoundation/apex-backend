package eu.apenet.commons.xslt.extensions;

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
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
 * "<eagRelation>".
 *
 */
public class RetrieveRelatedAIIdExtension extends ExtensionFunctionDefinition {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -8406741587979046031L;

	/**
	 * Name of the function to call.
	 */
	private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
			"retrieveRelatedAIId");
//	private static final Logger LOG = Logger.getLogger(RetrieveRelatedAIId.class);
	private RetrieveRelatedAIIdCall retrieveRelatedAIIdCall;

	public RetrieveRelatedAIIdExtension(final String currentAIRepositorCode, final String requiredAIRepositorCode) {
		this.retrieveRelatedAIIdCall = new RetrieveRelatedAIIdCall(currentAIRepositorCode, requiredAIRepositorCode);
	}

	@Override
	public SequenceType[] getArgumentTypes() {
		return new SequenceType[] { SequenceType.OPTIONAL_STRING };
	}

	@Override
	public StructuredQName getFunctionQName() {
		return RetrieveRelatedAIIdExtension.funcname;
	}

	@Override
	public SequenceType getResultType(SequenceType[] arg0) {
		return SequenceType.OPTIONAL_STRING;
	}

	@Override
	public ExtensionFunctionCall makeCallExpression() {
		return this.retrieveRelatedAIIdCall;
	}

	@Override
	public int getMinimumNumberOfArguments() {
		return 2;
	}

	public int getMaximumNumberOfArguments() {
		return 2;
	}

	class RetrieveRelatedAIIdCall extends ExtensionFunctionCall {
		/**
		 * Serializable.
		 */
		private static final long serialVersionUID = 6099497471179098886L;

		private String currentAIRepositorCode;
		private String requiredAIRepositorCode;

		/**
		 * Constructor.
		 */
		public RetrieveRelatedAIIdCall (final String currentAIRepositorCode, final String requiredAIRepositorCode) {
			this.currentAIRepositorCode = currentAIRepositorCode;
			this.requiredAIRepositorCode = requiredAIRepositorCode;
		}

		@Override
		public SequenceIterator call(SequenceIterator[] arguments, XPathContext arg1)
				throws XPathException {
			if (arguments.length == 2) {
//				String firstArgValue = arguments[0].next().getStringValue();
				String secondArgValue = arguments[1].next().getStringValue();
				String value = "";

				if (secondArgValue != null && !secondArgValue.isEmpty()) {
					ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
					ArchivalInstitution archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByAiName(secondArgValue);
					value = String.valueOf(archivalInstitution.getAiId());
				} else if(this.requiredAIRepositorCode != null && !this.requiredAIRepositorCode.isEmpty()) {
					
				}

				return SingletonIterator.makeIterator(new StringValue(value));
			} else {
				return SingletonIterator.makeIterator(new StringValue("ERROR"));
			}
		}
	}
}
