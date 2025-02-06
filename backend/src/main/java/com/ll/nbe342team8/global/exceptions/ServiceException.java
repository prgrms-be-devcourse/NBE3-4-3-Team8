package com.ll.nbe342team8.global.exceptions;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final int resultCode;
    private final String msg;

    public ServiceException(int resultCode, String msg) {
        super(resultCode + " : " + msg);
        this.resultCode = resultCode;
        this.msg = msg;
    }


}
