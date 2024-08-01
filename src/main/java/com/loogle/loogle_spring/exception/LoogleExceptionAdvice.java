package com.loogle.loogle_spring.exception;

import com.loogle.loogle_spring.exception.ErrorEntity;
import com.loogle.loogle_spring.exception.LoogleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class LoogleExceptionAdvice {

    @ExceptionHandler(LoogleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity handleAuthException(LoogleException e) {
        log.error("Brand Exception({})={}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage());
    }
}