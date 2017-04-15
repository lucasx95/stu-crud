package com.crud.interceptor;

import com.crud.exception.ExceptionMessages;
import com.crud.exception.ServiceException;
import com.crud.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Interceptor de requisições para evitar vazamento de StackTrace para
 * fora da aplicação; Logando o stacktrace e formatando a mensagem
 * caso necessário
 */
@ControllerAdvice
@EnableWebMvc
public class ExceptionInterceptor {
    private final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler(value = {ServiceException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorDto handleKnown(Exception ex) {
        logInterception(ex);
        return new ErrorDto(ex.getMessage());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorDto handleArgumentMismatch(Exception ex) {
        logInterception(ex);
        return new ErrorDto(ExceptionMessages.INVALID_REQUEST.name());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    protected ErrorDto handleUnknown(Exception ex) {
        logInterception(ex);
        return new ErrorDto(ExceptionMessages.UNEXPECTED_ERROR.name());
    }

    /**
     * Convert o stack trace em string e loga em modo debugger
     *
     * @param exception a exceção
     */
    private void logInterception(Throwable exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        exception.printStackTrace(pw);
        logger.debug(sw.getBuffer().toString());
    }

    /**
     * Dto com mensagem de erro
     */
    public class ErrorDto {
        private String message;

        public ErrorDto(String message) {
            this.message = message;
        }

        public ErrorDto() {
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}

