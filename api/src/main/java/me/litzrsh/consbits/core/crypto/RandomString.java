package me.litzrsh.consbits.core.crypto;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unused"})
public class RandomString {

    public static final String DEFAULT_PRESET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890@#!$%^&";

    private final int length;
    private final boolean allowDuplicated;
    private final String preset;

    public RandomString() {
        this.length = 12;
        this.allowDuplicated = false;
        this.preset = DEFAULT_PRESET;
    }

    public RandomString(int length) {
        this.length = length;
        this.allowDuplicated = false;
        this.preset = DEFAULT_PRESET;
        assert this.length < this.preset.length();
    }

    public RandomString(int length, String preset) {
        this.length = length;
        this.allowDuplicated = false;
        this.preset = preset;
        assert this.length < this.preset.length();
    }

    public RandomString(int length, boolean allowDuplicated, String preset) {
        this.length = length;
        this.allowDuplicated = allowDuplicated;
        this.preset = preset;
        assert allowDuplicated || this.length < this.preset.length();
    }

    public String generate(SecureRandom random) {
        final String[] pool = this.preset.split("");
        Set<Integer> consumed = new HashSet<>();
        StringBuilder builder = new StringBuilder();
        while (builder.length() < this.length) {
            int index = random.nextInt(pool.length);
            if (this.allowDuplicated || !consumed.contains(index)) {
                builder.append(pool[index]);
                consumed.add(index);
            }
        }
        return builder.toString();
    }

    public String generate() {
        return generate(new SecureRandom());
    }
}
