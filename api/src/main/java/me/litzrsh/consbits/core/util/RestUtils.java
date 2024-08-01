package me.litzrsh.consbits.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.litzrsh.consbits.core.CommonConstants;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.litzrsh.consbits.core.CommonConstants.PATH_DELIM;

public abstract class RestUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void response(HttpServletResponse response, HttpStatus status, Object message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(CommonConstants.CONTENT_TYPE_JSON);
        mapper.writeValue(response.getWriter(), Objects.requireNonNullElse(message, "{}"));
    }

    public static String append(String path, String next) {
        if (path == null) path = "";
        if (next == null) next = "";
        List<String> array = new ArrayList<>(List.of(path.split(PATH_DELIM)));
        array.add(next);
        String prepend = path.startsWith(PATH_DELIM) ? PATH_DELIM : "";
        return prepend + StringUtils.collectionToDelimitedString(array.stream().filter(StringUtils::hasText).toList(), PATH_DELIM);
    }
}
