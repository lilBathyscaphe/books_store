package com.epam.bookstore.dto.exception;

import lombok.Data;

@Data
public class ExceptionMessage {

    private String message;

    public ExceptionMessage(Exception e) {
        this.message = e.getMessage();
    }

    public ExceptionMessage(String message) {
        this.message = message;
    }

}
