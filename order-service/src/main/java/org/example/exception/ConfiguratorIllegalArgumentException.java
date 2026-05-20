package org.example.exception;

public class ConfiguratorIllegalArgumentException{
    private ConfiguratorIllegalArgumentException(){
    }

    public static class ConfiguratorInvalidArgumentException extends BaseException{
        public ConfiguratorInvalidArgumentException(String message){
            super(message);
        }
    }

    public static class InvalidComponentException extends BaseException{
        public InvalidComponentException(String message){
            super(message);
        }
    }
}
