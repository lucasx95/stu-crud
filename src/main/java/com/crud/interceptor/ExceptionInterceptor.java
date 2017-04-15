package com.crud.interceptor;

import com.crud.exception.ExceptionMessages;
import com.crud.exception.ServiceException;
import com.crud.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Interceptor de requisições para evitar vazamento de StackTrace para
 * fora da aplicação; Logando o stacktrace e formatando a mensagem
 * caso necessário
 */
@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(value = {ServiceException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected String handleKnown(Exception ex) {
        logInterception(ex);
        return ex.getMessage();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected String handleUnknown(Exception ex) {
        logInterception(ex);
        return ExceptionMessages.UNEXPECTED_ERROR.name();
    }

    /**
     * Convert o stack trace em string e loga em modo debugger
     * @param exception a exceção
     */
    private void logInterception(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        exception.printStackTrace(pw);
        logger.debug(sw.getBuffer().toString());
    }

}

