package eu.apenet.commons.xslt.extensions;

import java.util.List;

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
			"related");
//	private static final Logger LOG = Logger.getLogger(RetrieveRelatedAIId.class);
	private RetrieveRelatedAIIdCall retrieveRelatedAIIdCall;

	public RetrieveRelatedAIIdExtension(final String requiredAIRepositorCode) {
		this.retrieveRelatedAIIdCall = new RetrieveRelatedAIIdCall(requiredAIRepositorCode);
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
		return 1;
	}

	public int getMaximumNumberOfArguments() {
		return 1;
	}

	class RetrieveRelatedAIIdCall extends ExtensionFunctionCall {
		/**
		 * Serializable.
		 */
		private static final long serialVersionUID = 6099497471179098886L;

		private String requiredAIRepositorCode;

		/**
		 * Constructor.
		 */
		public RetrieveRelatedAIIdCall (final String requiredAIRepositorCode) {
			this.requiredAIRepositorCode = requiredAIRepositorCode;
		}

		@Override
		public SequenceIterator call(SequenceIterator[] arguments, XPathContext arg1)
				throws XPathException {
			if (arguments.length == 1) {
				String firstArgValue = arguments[0].next().getStringValue();
				String value = "";

				ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
				List<ArchivalInstitution> archivalInstitutionList = null;

				if (firstArgValue != null && !firstArgValue.isEmpty()) {
					archivalInstitutionList = archivalInstitutionDAO.getArchivalInstitutionsByRepositorycode(firstArgValue);
				} else if(this.requiredAIRepositorCode != null && !this.requiredAIRepositorCode.isEmpty()) {
					archivalInstitutionList = archivalInstitutionDAO.getArchivalInstitutionsByRepositorycode(firstArgValue);
				}

				if (archivalInstitutionList != null
						&& !archivalInstitutionList.isEmpty()
						&& archivalInstitutionList.size() > 0) {
					value = String.valueOf(archivalInstitutionList.get(0).getAiId());
				}

				return SingletonIterator.makeIterator(new StringValue(value));
			} else {
				return SingletonIterator.makeIterator(new StringValue("ERROR"));
			}
		}
	}
}
