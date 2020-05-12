package by.beg.payment_system.controller.advice_controller;

import by.beg.payment_system.dto.response.ErrorResponseDTO;
import by.beg.payment_system.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class MainAdviceController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.badRequest().body(responseDTO);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> paramValidate() {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, "Invalid path variable");
        return ResponseEntity.badRequest().body(responseDTO);
    }

    //SECURITY

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> badCredentials() {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, "Incorrect email or password");
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponseDTO> blocked() {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.LOCKED, "User is blocked");
        return new ResponseEntity<>(responseDTO, HttpStatus.LOCKED);
    }

    //NOT_FOUND && EXIST

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> notFound(NotFoundException e) {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.badRequest().body(responseDTO);
    }


    @ExceptionHandler(ExistException.class)
    public ResponseEntity<ErrorResponseDTO> notFound(ExistException e) {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.badRequest().body(responseDTO);
    }


    //TRANSFER

    @ExceptionHandler(LackOfMoneyException.class)
    public ResponseEntity<ErrorResponseDTO> lackOfMoney() {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.LOCKED, "Not enough money on current wallet for transfer");
        return new ResponseEntity<>(responseDTO, HttpStatus.LOCKED);
    }


    @ExceptionHandler(CurrencyConverterException.class)
    public ResponseEntity<ErrorResponseDTO> converterFail() {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.SERVICE_UNAVAILABLE, "Service is temporarily unavailable");
        return new ResponseEntity<>(responseDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }


    //STATUS

    @ExceptionHandler(UnremovableStatusException.class)
    public ResponseEntity<ErrorResponseDTO> unremovableStatus() {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.LOCKED, "Unremovable status");
        return new ResponseEntity<>(responseDTO, HttpStatus.LOCKED);
    }

    //MAIL_SENDER

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponseDTO> invalidAddress() {
        ErrorResponseDTO responseDTO = new ErrorResponseDTO(HttpStatus.SERVICE_UNAVAILABLE, "Invalid user's address or something else... Check log files on the server");
        return new ResponseEntity<>(responseDTO, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
