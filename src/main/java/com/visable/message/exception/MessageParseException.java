package com.visable.message.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MessageParseException extends RuntimeException {
    public MessageParseException(String message) {
        super(message);
    }
}
