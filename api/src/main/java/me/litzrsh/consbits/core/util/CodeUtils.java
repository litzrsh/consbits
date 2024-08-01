package me.litzrsh.consbits.core.util;

public abstract class CodeUtils {

    public static String format(String value) {
        if (value == null) value = "";
        value = value.trim().toUpperCase();
        value = value.replaceAll("\\s+", "_");
        value = value.replaceAll("([^A-Z|0-9|_])", "");
        return value.startsWith("_") ? value.substring(1) : value;
    }
}
