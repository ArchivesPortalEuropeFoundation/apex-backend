package eu.apenet.commons;

import java.util.Locale;

/**
 * This interface separates the usage of a resource bundle from the
 * implementation.
 *
 * @author bastiaan
 *
 */
public interface ResourceBundleSource {

    String UNKNOWN = "???";

    /**
     * Retrieves a string from a resource bundle.
     *
     * @param key Key in the resource bundle
     * @return Resource message
     */
    String getString(String key);

    /**
     * Retrieves a string from a resource bundle.
     *
     * @param key Key in the resource bundle
     * @param defaultValue Default value
     * @return
     */
    String getString(String key, String defaultValue);

    /**
     * Retrieve current locale
     *
     * @return
     */
    Locale getLocale();
}
