package sigma.tailoring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sigma.tailoring.exceptions.ReadyDesignOrderFormException;
import sigma.tailoring.exceptions.UserNotFound;
import sigma.tailoring.exceptions.FormException;
import sigma.tailoring.exceptions.OrderCommentException;


import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
    private static final String ERROR_DUPLICATE_KEY_MESSAGE_RESPONSE = "Sorry, you send duplicate data";
    private static final String WAR_DUPLICATE_KEY_MESSAGE_LOG = "Was send duplicate data";

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public ResponseEntity handleEDuplicateKeyException(DuplicateKeyException ex, WebRequest request) {
        LOGGER.warn(WAR_DUPLICATE_KEY_MESSAGE_LOG, ex);

        return new ResponseEntity<>(ERROR_DUPLICATE_KEY_MESSAGE_RESPONSE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FormException.class)
    public String handleFormException(FormException exception, Model model) {
        model.addAttribute("errors", exception.getMessage());
        model.addAttribute(exception.getFormName(), exception.getForm());
        return exception.getPageName();
    }

    @ResponseBody
    @ExceptionHandler({ConstraintViolationException.class, OrderCommentException.class, UserNotFound.class})
    public ResponseEntity handleConstraintViolationException(RuntimeException ex) {
        LOGGER.warn(ex.getMessage(), ex);
        return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ReadyDesignOrderFormException.class)
    public String handleReadyDesignOrderFormException(ReadyDesignOrderFormException orderFormException, Model model) {
        LOGGER.warn(orderFormException.getMessage(), orderFormException);

        orderFormException
                .getAttributes()
                .forEach((name, value) -> model.addAttribute(name, value));

        return orderFormException.getPageName();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleAllExceptions(Exception ex, Model model) {
        LOGGER.error(ex.getMessage(), ex);
        model.addAttribute("message", ex.getMessage());
        return "ErrorPage";
    }
}
