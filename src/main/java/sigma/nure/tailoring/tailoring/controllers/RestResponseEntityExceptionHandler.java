package sigma.nure.tailoring.tailoring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sigma.nure.tailoring.tailoring.tools.ResponseException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    private final String errorMessage;

    public RestResponseEntityExceptionHandler(@Value("${error.message}") String errorMessage) {
        this.errorMessage = errorMessage;
    }


    @ExceptionHandler(value
            = {ResponseException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        ResponseException exception = (ResponseException) ex;

        exception.log(LOGGER);

        String message = exception.getAnswerMessage() == null ? errorMessage : exception.getAnswerMessage();

        return handleExceptionInternal(exception, message,
                new HttpHeaders(), exception.getStatus(), request);
    }
}
