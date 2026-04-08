package org.example.exception;

public class NotFoundException {
    private NotFoundException(){
    }

    public static class EntityNotFoundException extends BaseException{
        public EntityNotFoundException(String message){
            super(message);
        }

        public EntityNotFoundException(){
            super("");
        }
    }
}
