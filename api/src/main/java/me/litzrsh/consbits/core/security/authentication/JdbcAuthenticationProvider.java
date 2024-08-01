package me.litzrsh.consbits.core.security.authentication;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.crypto.RandomString;
import me.litzrsh.consbits.core.exception.RestfulAuthenticationException;
import me.litzrsh.consbits.core.security.service.DefaultUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static me.litzrsh.consbits.core.CommonConstants.*;

@Component
@RequiredArgsConstructor
public class JdbcAuthenticationProvider implements CustomAuthenticationProvider {

    private final RandomString randomString = new RandomString(16);

    private final PasswordEncoder passwordEncoder;

    private final DefaultUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        final String defaultPassword = randomString.generate();
        final String encodedPassword = passwordEncoder.encode(defaultPassword);

        final String username = obtainUsername(authentication);
        final String password = obtainPassword(authentication);

        if (username.isEmpty() || password.isEmpty()) {
            passwordEncoder.matches(defaultPassword, encodedPassword);
            throw AUTHENTICATION_FAIL_EXCEPTION;
        }

        User userDetails;
        try {
            userDetails = (User) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            passwordEncoder.matches(defaultPassword, encodedPassword);
            throw AUTHENTICATION_FAIL_EXCEPTION;
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new RestfulAuthenticationException(userDetails, AUTHENTICATION_FAIL_EXCEPTION);
        }

        if (USER_STATUS_LOCK.equals(userDetails.getStatus())) {
            throw new RestfulAuthenticationException(userDetails, "SE0006");
        }

        if (USER_STATUS_WITHDRAW.equals(userDetails.getStatus())) {
            throw new RestfulAuthenticationException(userDetails, "SE0005");
        }

        userDetailsService.updatePassword(userDetails, passwordEncoder.encode(password));

        return UsernamePasswordAuthenticationToken
                .authenticated(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    protected final String obtainUsername(Authentication authentication) {
        String value = String.valueOf(authentication.getPrincipal());
        if (value == null || "null".equals(value)) value = "";
        return value.trim();
    }

    protected final String obtainPassword(Authentication authentication) {
        String value = String.valueOf(authentication.getCredentials());
        if (value == null || "null".equals(value)) value = "";
        return value.trim();
    }
}
