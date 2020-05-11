package by.beg.payment_system.controller.advice_controller;

import by.beg.payment_system.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
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
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> paramValidate() {
        return ResponseEntity.badRequest().body("Invalid path variable.");
    }


    //USER

    @ExceptionHandler(UserIsPresentException.class)
    public ResponseEntity<String> userIsPresent() {
        return new ResponseEntity<>("User is present. Check your email or passport id.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound() {
        return new ResponseEntity<>("User not found. Check your input data.", HttpStatus.NO_CONTENT);
    }


//
//    @ExceptionHandler(UserIsNotAuthorizedException.class)
//    public ResponseEntity<String> checkAuth() {
//        return new ResponseEntity<>("User isn't authorized.", HttpStatus.LOCKED);
//    }
//
//    @ExceptionHandler(UserBlockedException.class)
//    public ResponseEntity<String> blockUser() {
//        return new ResponseEntity<>("User is blocked.", HttpStatus.LOCKED);
//    }

    //WALLET

    @ExceptionHandler(WalletIsExistException.class)
    public ResponseEntity<String> walletIsPresent() {
        return new ResponseEntity<>("Wallet is present.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<String> walletNotFound() {
        return new ResponseEntity<>("Wallet not found.", HttpStatus.BAD_REQUEST);
    }

    //TRANSFER

    @ExceptionHandler(LackOfMoneyException.class)
    public ResponseEntity<String> notMoney() {
        return new ResponseEntity<>("Not enough money on current wallet for transfer.", HttpStatus.LOCKED);
    }

    @ExceptionHandler(TargetWalletNotFoundException.class)
    public ResponseEntity<String> targetNotFound() {
        return new ResponseEntity<>("Target wallet not found.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CurrencyConverterException.class)
    public ResponseEntity<String> converterFail() {
        return new ResponseEntity<>("Service is temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(TransferNotFoundException.class)
    public ResponseEntity<String> transferNotFound() {
        return new ResponseEntity<>("Transfer not found.", HttpStatus.BAD_REQUEST);
    }

    //DEPOSIT

    @ExceptionHandler(DepositNotFoundException.class)
    public ResponseEntity<String> depositNotFound() {
        return new ResponseEntity<>("Deposit not found.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DepositIsPresentException.class)
    public ResponseEntity<String> depositIsPresent() {
        return new ResponseEntity<>("Deposit is present. Change deposit's name.", HttpStatus.BAD_REQUEST);
    }

    //CREDIT

    @ExceptionHandler(CreditNotFoundException.class)
    public ResponseEntity<String> creditNotFound() {
        return new ResponseEntity<>("Credit not found.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreditDetailIsPresentException.class)
    public ResponseEntity<String> creditDetailIsPresent() {
        return new ResponseEntity<>("CreditDetail is present for current user.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CreditIsPresentException.class)
    public ResponseEntity<String> creditIsPresent() {
        return new ResponseEntity<>("Credit is present. Change credit name.", HttpStatus.BAD_REQUEST);
    }


    //STATUS

    @ExceptionHandler(UnremovableStatusException.class)
    public ResponseEntity<String> unremovableStatus() {
        return new ResponseEntity<>("Unremovable status.", HttpStatus.LOCKED);
    }

    //MAIL_SENDER

    @ExceptionHandler(MailException.class)
    public ResponseEntity<String> invalidAddress() {
        return new ResponseEntity<>("Invalid user's address or something else... Check log files on the server", HttpStatus.BAD_REQUEST);
    }


}
