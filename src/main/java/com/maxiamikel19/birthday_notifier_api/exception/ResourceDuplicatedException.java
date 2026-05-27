package com.maxiamikel19.birthday_notifier_api.exception;

public class ResourceDuplicatedException extends RuntimeException{
    public ResourceDuplicatedException(String message){
        super(message);
    }
}
