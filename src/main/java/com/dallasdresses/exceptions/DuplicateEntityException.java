package com.dallasdresses.exceptions;

public class DuplicateEntityException extends RuntimeException{

    public DuplicateEntityException(String entity){
        super(entity + " already exists ");
    }

    public DuplicateEntityException(String entity, String field, String value){
        super(entity + " with " + field + " '" + value + "' already exists");
    }
}
