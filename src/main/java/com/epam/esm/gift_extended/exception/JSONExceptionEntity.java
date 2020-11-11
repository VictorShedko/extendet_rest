package com.epam.esm.gift_extended.exception;

import org.springframework.http.HttpStatus;

public class JSONExceptionEntity {

    private Integer errorCode;
    private String errorMassage;
    private HttpStatus status;

    public JSONExceptionEntity(Integer errorCode, String errorMassage, HttpStatus status) {
        this.errorCode = errorCode;
        this.errorMassage = errorMassage;
        this.status = status;
    }

    @Deprecated
    public JSONExceptionEntity(GiftException exception, HttpStatus status) {
        super();
        this.errorMassage = exception.getMessage();
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMassage() {
        return errorMassage;
    }

    public void setErrorMassage(String errorMassage) {
        this.errorMassage = errorMassage;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
