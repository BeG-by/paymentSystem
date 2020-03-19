package by.beg.payment_system.controller.controller_advice;

import by.beg.payment_system.exception.NoAccessException;
import by.beg.payment_system.exception.UserIsPresentException;
import by.beg.payment_system.exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserIsPresentException.class)
    public ResponseEntity<String> userIsPresent() {
        return new ResponseEntity<>("User is present. Check your email or passport id.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound() {
        return new ResponseEntity<>("User not found. Check your input data.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAccessException.class)
    public ResponseEntity<String> checkAdmin() {
        return new ResponseEntity<>("User role isn't administration", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(UserIsNotAuthorizedException.class)
    public ResponseEntity<String> checkAuth() {
        return new ResponseEntity<>("User isn't authorized", HttpStatus.NOT_ACCEPTABLE);
    }
}
