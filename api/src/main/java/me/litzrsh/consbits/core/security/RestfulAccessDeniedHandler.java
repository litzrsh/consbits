package me.litzrsh.consbits.core.security;

import me.litzrsh.consbits.core.util.RestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static me.litzrsh.consbits.core.CommonConstants.FORBIDDEN_EXCEPTION;

public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        RestUtils.response(response, HttpStatus.FORBIDDEN, FORBIDDEN_EXCEPTION.getExceptionMessage());
    }
}
