package me.litzrsh.consbits.core.util;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Date;

@SuppressWarnings({"unused"})
public abstract class DateUtils {

    public static Date add(Date date, Duration duration) {
        Instant instant = date.toInstant();
        return Date.from(instant.plus(duration));
    }

    public static Date add(Date date, Period period) {
        Instant instant = date.toInstant();
        return Date.from(instant.plus(period));
    }

    public static Date sub(Date date, Duration duration) {
        Instant instant = date.toInstant();
        return Date.from(instant.minus(duration));
    }

    public static Date sub(Date date, Period period) {
        Instant instant = date.toInstant();
        return Date.from(instant.minus(period));
    }
}
