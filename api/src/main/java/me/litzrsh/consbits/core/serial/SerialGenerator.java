package me.litzrsh.consbits.core.serial;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface SerialGenerator {

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    String getId();

    default String getPrefix() {
        String[] strs = getId().split("_");
        List<String> array = Stream.of(strs)
                .map((e) -> e.substring(0, 1))
                .toList();
        return StringUtils
                .collectionToDelimitedString(array, "")
                .toUpperCase();
    }

    default DateFormat getDateFormat() {
        return dateFormat;
    }

    default boolean compare(Date date1, Date date2) {
        String str1 = getDateFormat().format(date1);
        String str2 = getDateFormat().format(date2);
        return str1.equals(str2);
    }

    default String format(Date date, Long value) {
        return String.format("%s%s%04d", getPrefix(), getDateFormat().format(date), value);
    }
}
