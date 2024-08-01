package me.litzrsh.consbits.core.security.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

import static me.litzrsh.consbits.core.CommonConstants.AUTHENTICATION_FAIL_EXCEPTION;

public class JdbcAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public static final String PARAMS_USERNAME = "username";
    public static final String PARAMS_PASSWORD = "password";

    private static final AntPathRequestMatcher matcher = new AntPathRequestMatcher("/api/v1.0/login");

    public JdbcAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        super(matcher);
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!HttpMethod.POST.matches(request.getMethod())) {
            throw AUTHENTICATION_FAIL_EXCEPTION;
        }

        final String username = obtainUsername(request);
        final String password = obtainPassword(request);

        if (username.isEmpty() || password.isEmpty()) {
            throw AUTHENTICATION_FAIL_EXCEPTION;
        }

        Authentication authReq = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        return getAuthenticationManager().authenticate(authReq);
    }

    protected final String obtainUsername(HttpServletRequest request) {
        String value = request.getParameter(PARAMS_USERNAME);
        if (value == null) value = "";
        return value.trim();
    }

    protected final String obtainPassword(HttpServletRequest request) {
        String value = request.getParameter(PARAMS_PASSWORD);
        if (value == null) value = "";
        return value.trim();
    }
}
