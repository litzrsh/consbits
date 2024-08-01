package me.litzrsh.consbits.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class RegexTest {

    @Test
    public void test1() {
        String[] testStrings = {"test 01", "한글002", "템_%#@91"};

        for (String text : testStrings) {
            log.info("{} -> {}", text, convert(text));
        }
    }

    protected String convert(String value) {
        if (value == null) value = "";
        value = value.trim().toUpperCase();
        value = value.replaceAll("\\s+", "_");
        value = value.replaceAll("([^A-Z|0-9|_])", "");
        return value.startsWith("_") ? value.substring(1) : value;
    }
}
