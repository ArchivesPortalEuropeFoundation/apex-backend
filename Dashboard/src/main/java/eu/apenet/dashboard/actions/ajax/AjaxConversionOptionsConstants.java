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
    public static final String SCRIPT_DEFAULT_RIGHTS_DIGITAL_TEXT = "defaultRightsDigitalObjectText"; // Default rights statement for digital objects text.
    public static final String SCRIPT_RIGHTS_DIGITAL_DESCRIPTION = "defaultRightsDigitalObjectDescription"; // Default rights description for digital objects.
    public static final String SCRIPT_RIGHTS_DIGITAL_HOLDER = "defaultRightsDigitalObjectHolder"; // Default rights holder for digital objects.
    public static final String SCRIPT_DEFAULT_RIGHTS_EAD = "defaultRightsEadData"; // Default rights statement for EAD data.
    public static final String SCRIPT_DEFAULT_RIGHTS_EAD_TEXT = "defaultRightsEadDataText"; // Default rights statement for EAD data text.
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
    public static final String PUBLIC_DOMAIN_MARK = "http://creativecommons.org/publicdomain/mark/1.0/"; // "pdm";
    public static final String EUROPEANA_IN_COPYRIGHT = "http://rightsstatements.org/vocab/InC/1.0/";
    public static final String EUROPEANA_IN_COPYRIGHT_EU_ORPHAN_WORK = "http://rightsstatements.org/vocab/InC-OW-EU/1.0/";
    public static final String EUROPEANA_IN_COPYRIGHT_EDUCATIONAL_USE_ONLY = "http://rightsstatements.org/vocab/InC-EDU/1.0/";
    public static final String EUROPEANA_NO_COPYRIGHT_NONCOMMERCIAL_USE_ONLY = "http://rightsstatements.org/vocab/NoC-NC/1.0/";
    public static final String EUROPEANA_NO_COPYRIGHT_OTHER_KNOWN_LEGAL_RESTRICTIONS = "http://rightsstatements.org/vocab/NoC-OKLR/1.0/";
    public static final String EUROPEANA_COPYRIGHT_NOT_EVALUATED = "http://rightsstatements.org/vocab/CNE/1.0/";

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
