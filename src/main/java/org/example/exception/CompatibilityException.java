package org.example.exception;

public class CompatibilityException{
    private CompatibilityException(){
    }

    public static class IncompatibleComponentException extends BaseException{
        public IncompatibleComponentException(String message) {
            super(message);
        }
    }
}
