package org.inmediart.commons.exception;

public class GassmanAvailableQuantityException extends RuntimeException {
    public GassmanAvailableQuantityException(){

    }

    public GassmanAvailableQuantityException(String message) {
       super(message);
    }
}
