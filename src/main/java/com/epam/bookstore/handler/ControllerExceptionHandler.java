package com.epam.bookstore.handler;

import com.epam.bookstore.dto.exception.ExceptionMessage;
import com.epam.bookstore.dto.exception.ExceptionType;
import com.epam.bookstore.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    private static final String TAG_NOT_UNIQUE_KEY = "error.tag.notUnique";
    private final ResourceBundleMessageSource messageResourceBundle;

    @Autowired
    public ControllerExceptionHandler(ResourceBundleMessageSource messageResourceBundle) {
        this.messageResourceBundle = messageResourceBundle;
    }


    @ExceptionHandler
    @ResponseBody
    public ResponseEntity handle(ServiceException serviceException) {
        log.debug("Controller Advice exception handling. Message: {}", serviceException.getMessage());

        int errorStatusCode = chooseStatusCode(serviceException);
        log.debug("Response error status code: {}", errorStatusCode);
        return ResponseEntity
                .status(errorStatusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionMessage(serviceException));
    }


    @ResponseBody
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity handle(Exception dataRestException) {
        log.debug("Tag rest repository: {}", dataRestException.getMessage());
        int statusCode = chooseStatusCode(dataRestException);
        return ResponseEntity
                .status(statusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionMessage(messageResourceBundle.getMessage(TAG_NOT_UNIQUE_KEY,null, Locale.US)));
    }

    private int chooseStatusCode(Throwable throwable) {
        Throwable cause = throwable;
        if(throwable instanceof ServiceException){
            cause = throwable.getCause();
        }
        return ExceptionType.getStatusCodeByException(cause.getClass());
    }

}
