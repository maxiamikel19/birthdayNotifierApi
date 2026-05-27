package com.maxiamikel19.birthday_notifier_api.exception;

public class InputValidationException extends RuntimeException{
    public InputValidationException(String message){
        super(message);
    }
}
