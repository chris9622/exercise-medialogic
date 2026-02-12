package com.medialogic.data_extractor.exception;

public class InvalidFiscalCodeException extends RuntimeException {
    
    public InvalidFiscalCodeException(String message) {
        super(message);
    }
    
    public InvalidFiscalCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
