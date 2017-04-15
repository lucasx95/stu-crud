package com.crud.interceptor;

import com.crud.exception.ExceptionMessages;
import com.crud.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.xml.bind.ValidationException;


/**
 * Interceptor de requisições para evitar vazamento de StackTrace para
 * fora da aplicação; Logando o stacktrace e formatando a mensagem
 * caso necessário
 */
@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {
    final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(value = {ServiceException.class})
    protected ResponseEntity<Object> handleKnown(ServiceException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleUnknown(Exception ex, WebRequest request) {
        String bodyOfResponse = ExceptionMessages.UNEXPECTED_ERROR.name();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}

