package org.example.exception;

public class DomainValidationExceptions {

    private DomainValidationExceptions() {
    }

    public static class DomainValidationException extends BaseException{
        public DomainValidationException(String message){
            super(message);
        }
    }
}
