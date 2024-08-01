package me.litzrsh.consbits.core.security.authentication;

import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.Message;
import me.litzrsh.consbits.core.util.RestUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class JdbcLogoutProcessingFilter implements Filter {

    private static final AntPathRequestMatcher matcher = new AntPathRequestMatcher("/api/v1.0/logout");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (matcher.matches((HttpServletRequest) servletRequest)) {
            processLogout((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    protected final void processLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!HttpMethod.POST.matches(request.getMethod())) {
            RestUtils.response(response, HttpStatus.METHOD_NOT_ALLOWED, new Message(CommonConstants.SE0001));
        }
        HttpSession session = request.getSession();
        session.invalidate();
    }
}
