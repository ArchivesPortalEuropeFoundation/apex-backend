package eu.apenet.commons.view.jsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class HrefObject {

    private String javascriptMethod;
//	private String url;
    private String action;
    private char quote;
    private Map<String, List<String>> parameters = new HashMap<String, List<String>>();

    public HrefObject(String href) {
        href = href.trim();
        boolean containsJavaScript = href.startsWith("javascript");
        if (containsJavaScript) {
            int openParentheseIndex = href.indexOf('(');
            javascriptMethod = href.substring(0, openParentheseIndex);
            String temp = href.substring(openParentheseIndex + 1).trim();
            quote = temp.charAt(0);
            int closeQuoteIndex = temp.indexOf(quote, 1);
            href = temp.substring(1, closeQuoteIndex);

        }
        String[] splitted = href.split("\\?");
        action = splitted[0];
        if (splitted.length > 1) {
            String parameterString = splitted[1];
            String[] parameterArray = parameterString.split("&");
            for (int i = 0; i < parameterArray.length; i++) {
                String[] nameValue = parameterArray[i].split("=");
                String name = nameValue[0];
                String value = "";
                if (nameValue.length > 1) {
                    value = nameValue[1];
                };
                if (!parameters.containsKey(name)) {
                    List<String> values = new ArrayList<String>();
                    parameters.put(name, values);
                }
                List<String> values = parameters.get(name);
                values.add(value);
            }
        }
    }

    public boolean containsParameter(String name) {
        return parameters.containsKey(name);
    }

    public String getParameter(String name) {
        String result = null;
        if (parameters.containsKey(name)) {
            List<String> values = parameters.get(name);
            if (values.size() > 0) {
                result = values.get(0);
            }
        }
        return result;
    }

    public void removeParameter(String name) {
        parameters.remove(name);
    }

    public void addParameter(String name, Object value) {
        if (!parameters.containsKey(name)) {
            List<String> values = new ArrayList<String>();
            parameters.put(name, values);
        }
        List<String> values = parameters.get(name);
        String valueString = "";
        if (value != null) {
            valueString = value.toString();
        }
        values.add(valueString);
    }

    public void setParameter(String name, Object value) {
        List<String> values = new ArrayList<String>();
        String valueString = "";
        if (value != null) {
            valueString = value.toString();
        }
        values.add(valueString);
        parameters.put(name, values);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (javascriptMethod != null) {
            builder.append(javascriptMethod + "(" + quote);
        }
        builder.append(action);
        boolean noAction = StringUtils.isBlank(action);
        boolean first = true;
        for (Map.Entry<String, List<String>> parameter : parameters.entrySet()) {
            for (String value : parameter.getValue()) {
                if (first) {
                    if (noAction) {
                        builder.append(parameter.getKey() + "=" + value);
                    } else {
                        builder.append("?" + parameter.getKey() + "=" + value);
                    }
                    first = false;
                } else {
                    builder.append("&" + parameter.getKey() + "=" + value);
                }
            }

        }
        if (javascriptMethod != null) {
            builder.append(quote + ")");
        }
        return builder.toString();
    }

}
