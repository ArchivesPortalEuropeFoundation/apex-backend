package eu.apenet.commons;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DefaultResourceBundleSource implements ResourceBundleSource {

    private ResourceBundle bundle;

    public DefaultResourceBundleSource(ResourceBundle resourceBundle) {
        this.bundle = resourceBundle;
    }

    @Override
    public String getString(String key) {
        return getString(key, UNKNOWN + key + UNKNOWN);

    }

    @Override
    public String getString(String key, String defaultValue) {
        String message = defaultValue;
        if (bundle != null) {
            try {
                message = bundle.getString(key);
            } catch (MissingResourceException mre) {

            }
        }
        return message;
    }

    @Override
    public Locale getLocale() {
        return bundle.getLocale();
    }

}
