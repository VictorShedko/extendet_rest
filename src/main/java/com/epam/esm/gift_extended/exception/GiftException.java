package com.epam.esm.gift_extended.exception;

public class GiftException extends RuntimeException {

    public GiftException(String message) {
        super(message);

    }

    public GiftException(Throwable cause) {
        super(cause);

    }

    public GiftException() {
    }
}
