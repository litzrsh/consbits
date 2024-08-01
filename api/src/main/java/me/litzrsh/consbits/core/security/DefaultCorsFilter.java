package me.litzrsh.consbits.core.security;

import me.litzrsh.consbits.core.security.props.CorsConfigurationProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultCorsFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME_REQUEST_ORIGIN = "Origin";
    public static final String HEADER_NAME_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String HEADER_NAME_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String HEADER_NAME_MAX_AGE = "Access-Control-Max-Age";
    public static final String HEADER_NAME_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String HEADER_NAME_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    private final CorsConfigurationProperties properties;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String requestUrl = request.getHeader(HEADER_NAME_REQUEST_ORIGIN);
        if (requestUrl == null) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Cors filter : {} => {}", requestUrl, properties.checkOrigin(requestUrl));
        response.setHeader(HEADER_NAME_ALLOW_ORIGIN, properties.checkOrigin(requestUrl));
        response.setHeader(HEADER_NAME_ALLOW_METHODS,
                StringUtils.collectionToCommaDelimitedString(properties.getAllowedMethods()));
        response.setHeader(HEADER_NAME_ALLOW_HEADERS,
                StringUtils.collectionToCommaDelimitedString(properties.getAllowedHeaders()));
        response.setHeader(HEADER_NAME_MAX_AGE, String.valueOf(properties.getMaxAge()));
        response.setHeader(HEADER_NAME_ALLOW_CREDENTIALS, String.valueOf(properties.getAllowCredentials()));
        if (!HttpMethod.OPTIONS.matches(request.getMethod())) {
            filterChain.doFilter(request, response);
        }
    }
}
