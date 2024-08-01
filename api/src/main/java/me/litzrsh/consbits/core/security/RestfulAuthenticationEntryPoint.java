package me.litzrsh.consbits.core.security;

import me.litzrsh.consbits.core.util.RestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static me.litzrsh.consbits.core.CommonConstants.UNAUTHORIZED_EXCEPTION;

public class RestfulAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        RestUtils.response(response, HttpStatus.UNAUTHORIZED, UNAUTHORIZED_EXCEPTION.getExceptionMessage());
    }
}
