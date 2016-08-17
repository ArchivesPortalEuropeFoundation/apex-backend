package eu.apenet.commons.xslt;

import java.util.Locale;

import eu.apenet.commons.ResourceBundleSource;

public class TestResourceBundleSource implements ResourceBundleSource {

    @Override
    public String getString(String key) {
        // TODO Auto-generated method stub
        return UNKNOWN + key + UNKNOWN;
    }

    @Override
    public String getString(String key, String defaultValue) {
        // TODO Auto-generated method stub
        return getString(key);
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return Locale.getDefault();
    }

}
