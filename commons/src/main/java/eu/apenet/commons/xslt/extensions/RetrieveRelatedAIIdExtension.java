package eu.apenet.commons.xslt.extensions;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.iter.SingletonIterator;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

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
        return new SequenceType[]{SequenceType.OPTIONAL_STRING};
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
        public RetrieveRelatedAIIdCall(final String requiredAIRepositorCode) {
            this.requiredAIRepositorCode = requiredAIRepositorCode;
        }

        @Override
        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            if (sequences != null && sequences.length == 1) {
                String firstArgValue = sequences[0].head().getStringValue();
                String value = "";

                ArchivalInstitutionDAO archivalInstitutionDAO = DAOFactory.instance().getArchivalInstitutionDAO();
                ArchivalInstitution archivalInstitution = null;

                if (firstArgValue != null && !firstArgValue.isEmpty()) {
                    archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(firstArgValue);
                } else if (this.requiredAIRepositorCode != null && !this.requiredAIRepositorCode.isEmpty()) {
                    archivalInstitution = archivalInstitutionDAO.getArchivalInstitutionByRepositoryCode(requiredAIRepositorCode);
                }

                if (archivalInstitution != null) {
                    value = String.valueOf(archivalInstitution.getAiId());
                }

                return StringValue.makeStringValue(value);
            } else {
                return StringValue.makeStringValue("ERROR");
            }
        }
    }
}
