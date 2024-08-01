package me.litzrsh.consbits.test;

import me.litzrsh.consbits.core.util.RestUtils;
import org.junit.jupiter.api.Test;

public class ArrayJoinTests {

    @Test
    public void test1() {
        String[] tests = {"/", "./upload", "/test/upload"};
        String append = "IR21";

        for (String test : tests) {
            System.out.println("Result : " + RestUtils.append(test, append));
        }
        System.out.println("Result : " + RestUtils.append("", ""));
    }
}
