package com.joongna.edu.exception;

public class NotFoundUserIdException extends RuntimeException {

    public NotFoundUserIdException() {
        super();
    }

    public NotFoundUserIdException(String message) {
        super(message);
    }
}