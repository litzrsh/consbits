package me.litzrsh.consbits.core.exception;

import me.litzrsh.consbits.core.CommonConstants;
import me.litzrsh.consbits.core.Message;
import me.litzrsh.consbits.core.exception.advice.RestfulExceptionAdvice;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

import static me.litzrsh.consbits.core.CommonConstants.SE0001;

@SuppressWarnings({"unused"})
@Getter
public class RestfulException extends Exception {

    @Serial
    private static final long serialVersionUID = CommonConstants.SERIAL_VERSION;

    private final HttpStatus status;
    private final Message exceptionMessage;

    public RestfulException() {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.exceptionMessage = new Message(SE0001);
    }

    public RestfulException(String code, Object... args) {
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.exceptionMessage = new Message(code, args);
    }

    public RestfulException(HttpStatus status, String code, Object... args) {
        this.status = status;
        this.exceptionMessage = new Message(code, args);
    }

    public RestfulException(RestfulException o) {
        this.status = o.status;
        this.exceptionMessage = o.exceptionMessage;
    }
}
