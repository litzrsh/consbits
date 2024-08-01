package me.litzrsh.consbits.core.exception.advice;

import lombok.extern.slf4j.Slf4j;
import me.litzrsh.consbits.core.Message;
import me.litzrsh.consbits.core.exception.RestfulException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static me.litzrsh.consbits.core.CommonConstants.SE0001;

@RestControllerAdvice
@Slf4j
public class RestfulExceptionAdvice {

    @ExceptionHandler({RestfulException.class})
    public ResponseEntity<Message> handleRestfulException(RestfulException e) {
        log.error("{}", e.getExceptionMessage());
        if (log.isTraceEnabled()) e.printStackTrace(System.err);
        return new ResponseEntity<>(e.getExceptionMessage(), e.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Message> handleDefaultException(Exception e) {
        log.error("{}", e.getMessage());
        if (log.isTraceEnabled()) e.printStackTrace(System.err);
        return new ResponseEntity<>(new Message(SE0001), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
