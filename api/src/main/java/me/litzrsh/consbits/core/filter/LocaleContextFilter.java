package me.litzrsh.consbits.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
@Slf4j
public class LocaleContextFilter extends OncePerRequestFilter {

    public static final String HEADER_LOCALE = "X-Locale";
    public static final String[] LOCALES = {"ko"};

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("Access : {}", request.getRequestURI());
        String locale = obtainLocale(request);
        if (!locale.isEmpty()) {
            LocaleContextHolder.setLocale(Locale.forLanguageTag(locale));
        }
        filterChain.doFilter(request, response);
    }

    protected final String obtainLocale(HttpServletRequest request) {
        String value = request.getHeader(HEADER_LOCALE);
        if (value == null) value = LOCALES[0];
        return value.trim().toLowerCase();
    }
}
