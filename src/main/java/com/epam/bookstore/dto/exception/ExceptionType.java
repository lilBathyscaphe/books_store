package com.epam.bookstore.dto.exception;

import org.apache.catalina.connector.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.NoSuchElementException;

public enum ExceptionType {

    NOT_FOUND(Response.SC_NOT_FOUND, NoSuchElementException.class),
    CONFLICT(Response.SC_CONFLICT, DataIntegrityViolationException.class),
    BAD_CREDENTIALS(Response.SC_UNAUTHORIZED, BadCredentialsException.class);


    private int statusCode;
    private Class<? extends Throwable> exceptionClass;

    ExceptionType(int statusCode, Class<? extends Throwable> exceptionClass) {
        this.statusCode = statusCode;
        this.exceptionClass = exceptionClass;
    }

    public static int getStatusCodeByException(Class<? extends Throwable> exceptionClass) {
        int resultStatusCode = Response.SC_INTERNAL_SERVER_ERROR;
        for (ExceptionType type : ExceptionType.values()) {
            if (exceptionClass == type.getExceptionClass()) {
                resultStatusCode = type.getStatusCode();
            }
        }
        return resultStatusCode;
    }


    public int getStatusCode() {
        return statusCode;
    }

    public Class<? extends Throwable> getExceptionClass() {
        return exceptionClass;
    }
}
