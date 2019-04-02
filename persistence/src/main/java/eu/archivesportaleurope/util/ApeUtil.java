package eu.archivesportaleurope.util;

public class ApeUtil {

    private static final int MAX_ERROR_LINES = 4;

    public static String generateThrowableLog(Throwable throwable) {
        String result = "";
        result += throwable.getClass().getName() + " " + throwable.getMessage() + "\n";
        result += generateThrowableStackTraceLog(throwable.getStackTrace());
        result += generateThrowableCauseLog(throwable.getCause(), 0);
        return result;
    }

    private static String generateThrowableCauseLog(Throwable throwable, int depth) {
        String result = "";
        if (throwable != null) {
            result += "Caused by: " + throwable.getClass().getName() + " " + throwable.getMessage() + "\n";
            result += generateThrowableStackTraceLog(throwable.getStackTrace());
            result += generateThrowableCauseLog(throwable.getCause(), depth++);
        }
        return result;
    }

    private static String generateThrowableStackTraceLog(StackTraceElement[] elements) {
        String result = "";
        for (int i = 0; i < MAX_ERROR_LINES && i < elements.length; i++) {
            StackTraceElement element = elements[i];
            result += "\t" + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")\n";
        }
        if (elements.length > MAX_ERROR_LINES) {
            result += "\t... " + (elements.length - MAX_ERROR_LINES) + " more\n";
        }
        return result;
    }

    public static String encodeRepositoryCode(String repositoryCode) {
        if (repositoryCode != null) {
            return repositoryCode.replace('/', '_');
        }
        return null;
    }

    public static String decodeRepositoryCode(String repositoryCode) {
        if (repositoryCode != null) {
            return repositoryCode.replace('_', '/');
        }
        return null;
    }

    public static String encodeSpecialCharacters(String urlPart) {
        if (urlPart != null) {
            String result = urlPart.replaceAll(":", "_COLON_");
            result = result.replaceAll("\\*", "_ASTERISK_");
            result = result.replaceAll("=", "_COMP_");
            result = result.replaceAll("/", "_SLASH_");
            result = result.replaceAll("\\\\", "_BSLASH_");
            result = result.replaceAll("\\[", "_LSQBRKT_");
            result = result.replaceAll("\\]", "_RSQBRKT_");
            result = result.replaceAll("\\+", "_PLUS_");
            result = result.replaceAll("%", "_PERCENT_");
            result = result.replaceAll("@", "_ATCHAR_");
            result = result.replaceAll("\\$", "_DOLLAR_");
            result = result.replaceAll("#", "_HASH_");
            result = result.replaceAll("\\^", "_CFLEX_");
            result = result.replaceAll("&", "_AMP_");
            result = result.replaceAll("\\(", "_LRDBRKT_");
            result = result.replaceAll("\\)", "_RRDBRKT_");
            result = result.replaceAll("!", "_EXCLMARK_");
            result = result.replaceAll("~", "_TILDE_");
            result = result.replaceAll("<", "_LT_");
            result = result.replaceAll(">", "_GT_");
            result = result.replaceAll("\"", "_QUOTE_");
            result = result.replaceAll("'", "_SQUOTE_");
            result = result.replaceAll(",", "_COMMA_");
            result = result.replaceAll(";", "_SEMICOLON_");
            return result;
        } else {
            return null;
        }
    }

    public static String encodeSpecialCharactersWithSpaces(String urlPart) {
        if (urlPart != null) {
            String result = urlPart.trim();
            result = ApeUtil.encodeSpecialCharacters(result);
            result = result.replaceAll("\\{", "_LSCUBRKT_");
            result = result.replaceAll("\\}", "_RSCUBRKT_");
            result = result.replaceAll("\\s", "_SPACE_");
            return result;
        }

        return null;
    }

    public static String decodeSpecialCharacters(String urlPart) {
        if (urlPart != null) {
            String result = urlPart.replaceAll("_COLON_", ":");
            result = result.replaceAll("_ASTERISK_", "*");
            result = result.replaceAll("_COMP_", "=");
            result = result.replaceAll("_SLASH_", "/");
            result = result.replaceAll("_BSLASH_", "\\\\");
            result = result.replaceAll("_LSQBRKT_", "[");
            result = result.replaceAll("_RSQBRKT_", "]");
            result = result.replaceAll("_PLUS_", "+");
            result = result.replaceAll("_PERCENT_", "%");
            result = result.replaceAll("_ATCHAR_", "@");
            result = result.replaceAll("_DOLLAR_", "\\$");
            result = result.replaceAll("_HASH_", "#");
            result = result.replaceAll("_CFLEX_", "^");
            result = result.replaceAll("_AMP_", "&");
            result = result.replaceAll("_LRDBRKT_", "(");
            result = result.replaceAll("_RRDBRKT_", ")");
            result = result.replaceAll("_EXCLMARK_", "!");
            result = result.replaceAll("_TILDE_", "~");
            result = result.replaceAll("_LT_", "<");
            result = result.replaceAll("_GT_", ">");
            result = result.replaceAll("_QUOTE_", "\"");
            result = result.replaceAll("_SQUOTE_", "'");
            result = result.replaceAll("_COMMA_", ",");
            result = result.replaceAll("_SEMICOLON_", ";");
            return result.trim();
        } else {
            return null;
        }
    }

    public static String decodeSpecialCharactersWithSpaces(String urlPart) {
        if (urlPart != null) {
            String result = ApeUtil.decodeSpecialCharacters(urlPart);
            result = result.replaceAll("_LSCUBRKT_", "{");
            result = result.replaceAll("_RSCUBRKT_", "}");
            result = result.replaceAll("_SPACE_", " ");
            return result;
        } else {
            return null;
        }
    }

    public static String replaceQuotesAndReturns(String string) {
        String result = string;
        if (result != null) {
            result = result.replaceAll("\"", "'");
            result = result.replaceAll("[\n\t\r\\\\/%;]", "");
            result = result.trim();
        }
        return result;
    }
}
