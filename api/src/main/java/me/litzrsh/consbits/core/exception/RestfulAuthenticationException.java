package me.litzrsh.consbits.core.exception;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.Message;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

@Getter
public class RestfulAuthenticationException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private final User userDetails;
    private final Message exceptionMessage;

    public RestfulAuthenticationException(String code, Object... args) {
        super(null);
        this.userDetails = null;
        this.exceptionMessage = new Message(code, args);
    }

    public RestfulAuthenticationException(User userDetails, String code, Object... args) {
        super(null);
        this.userDetails = userDetails;
        this.exceptionMessage = new Message(code, args);
    }

    public RestfulAuthenticationException(RestfulAuthenticationException e) {
        super(null);
        this.userDetails = null;
        this.exceptionMessage = e.getExceptionMessage();
    }

    public RestfulAuthenticationException(User userDetails, RestfulAuthenticationException e) {
        super(null);
        this.userDetails = userDetails;
        this.exceptionMessage = e.getExceptionMessage();
    }
}
