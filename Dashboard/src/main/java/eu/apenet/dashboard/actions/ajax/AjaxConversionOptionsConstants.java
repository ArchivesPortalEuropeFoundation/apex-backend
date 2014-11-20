package eu.apenet.dashboard.actions.ajax;

import eu.apenet.commons.exceptions.APEnetException;

/**
 * Class to store the constants related to the different conversion options and
 * its values within the Dashboard and when should be passed to the conversion
 * script.
 *
 * Also stores the constants related to the links to the different rights
 * statements.
 */
public final class AjaxConversionOptionsConstants {
    // Constants for the names of the conversion options within the Dashboard.
    public static final String OPTIONS_DEFAULT = "optsDefault"; // Default DAO type.
    public static final String OPTIONS_USE_EXISTING = "optsUseExisting"; // Use DAO type if exists.
    public static final String OPTIONS_DEFAULT_RIGHTS_DIGITAL = "optsDefaultRightsDigitalObjects"; // Default rights statement for digital objects.
    public static final String OPTIONS_RIGHTS_DIGITAL_DESCRIPTION = "optsRightsDigitalDesription"; // Default rights description for digital objects.
    public static final String OPTIONS_RIGHTS_DIGITAL_HOLDER = "optsRightsDigitalHolder"; // Default rights holder for digital objects.
    public static final String OPTIONS_DEFAULT_RIGHTS_EAD = "optsDefaultRightsEadData"; // Default rights statement for EAD data.
    public static final String OPTIONS_RIGHTS_EAD_DESCRIPTION = "optsRightsEadDesription"; // Default rights description for EAD data.
    public static final String OPTIONS_RIGHTS_EAD_HOLDER = "optsRightsEadHolder"; // Default rights holder for EAD data.

    // Constants for the names of the conversion options when should be passed
    // to the conversion script.
    public static final String SCRIPT_DEFAULT = "defaultRoleType"; // Default DAO type.
    public static final String SCRIPT_USE_EXISTING = "useDefaultRoleType"; // Use DAO type if exists.
    public static final String SCRIPT_DEFAULT_RIGHTS_DIGITAL = "defaultRightsDigitalObject"; // Default rights statement for digital objects.
    public static final String SCRIPT_RIGHTS_DIGITAL_DESCRIPTION = "defaultRightsDigitalObjectDescription"; // Default rights description for digital objects.
    public static final String SCRIPT_RIGHTS_DIGITAL_HOLDER = "defaultRightsDigitalObjectHolder"; // Default rights holder for digital objects.
    public static final String SCRIPT_DEFAULT_RIGHTS_EAD = "defaultRightsEadData"; // Default rights statement for EAD data.
    public static final String SCRIPT_RIGHTS_EAD_DESCRIPTION = "defaultRightsEadDataDescription"; // Default rights description for EAD data.
    public static final String SCRIPT_RIGHTS_EAD_HOLDER = "defaultRightsEadDataHolder"; // Default rights holder for EAD data.

	// Rights statements types.
    public static final String NO_SELECTED = "---"; // No license selected.
    public static final String CREATIVECOMMONS_ATTRIBUTION = "http://creativecommons.org/licenses/by/4.0/"; // "cca";
    public static final String CREATIVECOMMONS_ATTRIBUTION_NO_DERIVATES = "http://creativecommons.org/licenses/by-nd/4.0/"; // "ccand";
    public static final String CREATIVECOMMONS_ATTRIBUTION_NON_COMERCIAL = "http://creativecommons.org/licenses/by-nc/4.0/"; // "ccanc";
    public static final String CREATIVECOMMONS_ATTRIBUTION_NC_NO_DERIVATES = "http://creativecommons.org/licenses/by-nc-nd/4.0/"; // "ccancnd";
    public static final String CREATIVECOMMONS_ATTRIBUTION_NC_SHARE= "http://creativecommons.org/licenses/by-nc-sa/4.0/"; // "ccancsa";
    public static final String CREATIVECOMMONS_ATTRIBUTION_SHARE = "http://creativecommons.org/licenses/by-sa/4.0/"; // "ccasa";
    public static final String CREATIVECOMMONS_CC0_PUBLIC = "http://creativecommons.org/publicdomain/zero/1.0/"; // "cc0pdd";
    public static final String FREE_ACCESS_NO_REUSE = "http://www.europeana.eu/rights/rr-f/"; // "freeaccess";
    public static final String ORPHAN_WORKS = "http://www.europeana.eu/rights/orphan-work-eu/"; // "orphanworks";
    public static final String OUT_OF_COPYRIGHT = "http://www.europeana.eu/rights/out-of-copyright-non-commercial/"; // "outofcopyright";
    public static final String PAID_ACCESS_NO_REUSE = "http://www.europeana.eu/rights/rr-p/"; // "paidaccess";
    public static final String PUBLIC_DOMAIN_MARK = "http://creativecommons.org/publicdomain/mark/1.0/"; // "pdm";
    public static final String UNKNOWN = "http://www.europeana.eu/rights/unknown/"; // "unknown";

	/**
	 * Private constructor to prevent instances of this constants class.
	 *
	 * Also this constructor throws an exception. 
	 *
	 * @throws APEnetException 
	 */
	private AjaxConversionOptionsConstants() throws APEnetException {	
		throw new APEnetException();
	}
}
