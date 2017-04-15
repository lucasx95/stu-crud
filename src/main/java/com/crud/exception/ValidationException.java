package com.crud.exception;

/**
 * Exceção conhecida jogada pelo sistema em caso de erros na conversão de dados {@link ExceptionMessages}
 */
public class ValidationException extends Exception {

    public ValidationException(ExceptionMessages messge) {
        super(messge.name());
    }
}
