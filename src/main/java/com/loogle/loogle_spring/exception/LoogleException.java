package com.loogle.loogle_spring.exception;

import com.loogle.loogle_spring.exception.LoogleErrorCode;
import lombok.Getter;

@Getter
public class LoogleException extends RuntimeException{
    private LoogleErrorCode errorCode;
    private String errorMessage;

    public LoogleException(LoogleErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDefaultMessage();
    }

    public LoogleException(LoogleErrorCode errorCode, String errorMessage) {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}