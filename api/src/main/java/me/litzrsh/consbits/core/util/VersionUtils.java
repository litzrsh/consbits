package me.litzrsh.consbits.core.util;

import me.litzrsh.consbits.core.CommonConstants;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

public abstract class VersionUtils {

    public static String nextBuild(String version) {
        return new Version(version).nextBuild().toString();
    }

    public static String nextMinor(String version) {
        return new Version(version).nextMinor().toString();
    }

    public static String nextMajor(String version) {
        return new Version(version).nextMajor().toString();
    }

    @Getter
    public static class Version implements Serializable {

        @Serial
        private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

        private long major = 1;
        private long minor = 0;
        private long build = 0;

        public Version(String version) {
            try {
                String[] parsed = version.split("\\.");
                major = Long.parseLong(parsed[0]);
                minor = Long.parseLong(parsed[1]);
                build = Long.parseLong(parsed[2]);
            } catch (Exception e) {
                major = 1;
                minor = 0;
                build = 0;
            }
        }

        private Version(long major, long minor, long build) {
            this.major = major;
            this.minor = minor;
            this.build = build;
        }

        public Version nextBuild() {
            return new Version(major, minor, build + 1);
        }

        public Version nextMinor() {
            return new Version(major, minor + 1, 0);
        }

        public Version nextMajor() {
            return new Version(major + 1, 0, 0);
        }

        @Override
        public String toString() {
            return major + "." + minor + "." + build;
        }
    }
}
