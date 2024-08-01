package me.litzrsh.consbits.core.security;

import me.litzrsh.consbits.app.user.repository.UserRepository;
import me.litzrsh.consbits.core.exception.RestfulAuthenticationException;
import me.litzrsh.consbits.core.util.RestUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static me.litzrsh.consbits.core.CommonConstants.AUTHENTICATION_FAIL_EXCEPTION;

@Component
@RequiredArgsConstructor
public class RestfulAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof RestfulAuthenticationException e) {
            if (e.getUserDetails() != null) {
                userRepository.addLoginFailCount(e.getUserDetails().getId());
            }
            RestUtils.response(response, HttpStatus.UNAUTHORIZED, e.getExceptionMessage());
        } else {
            RestUtils.response(response, HttpStatus.UNAUTHORIZED, AUTHENTICATION_FAIL_EXCEPTION.getExceptionMessage());
        }
    }
}
