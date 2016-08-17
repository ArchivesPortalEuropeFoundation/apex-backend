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

import org.apache.log4j.Logger;

import eu.apenet.commons.ResourceBundleSource;

public class ResourcebundleExtension extends ExtensionFunctionDefinition {

    /**
     *
     */
    private static final long serialVersionUID = 654874379518388994L;
    private static final StructuredQName funcname = new StructuredQName("ape", "http://www.archivesportaleurope.eu/xslt/extensions",
            "resource");
    private static final Logger LOG = Logger.getLogger(ResourcebundleExtension.class);
    private ResourcebundleExtensionCall resourceCall;

    public ResourcebundleExtension(ResourceBundleSource resourceBundleSource) {
        this.resourceCall = new ResourcebundleExtensionCall(resourceBundleSource);
    }

    @Override
    public StructuredQName getFunctionQName() {
        return funcname;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 1;
    }

    public int getMaximumNumberOfArguments() {
        return 1;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{SequenceType.OPTIONAL_STRING};
    }

    @Override
    public SequenceType getResultType(SequenceType[] sequenceTypes) {
        return SequenceType.OPTIONAL_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return resourceCall;
    }

    class ResourcebundleExtensionCall extends ExtensionFunctionCall {

        private static final long serialVersionUID = 6761914863093344493L;
        private ResourceBundleSource resourceBundleSource;

        public ResourcebundleExtensionCall(ResourceBundleSource resourceBundleSource) {
            this.resourceBundleSource = resourceBundleSource;
        }

        public Sequence call(XPathContext xPathContext, Sequence[] sequences) throws XPathException {
            if (sequences.length == 1) {
                String value = sequences[0].head().getStringValue();
                if (resourceBundleSource != null) {
                    value = resourceBundleSource.getString(value);
                }
                return StringValue.makeStringValue(value);
            } else {
                return StringValue.makeStringValue("ERROR");
            }
        }
    }
}
