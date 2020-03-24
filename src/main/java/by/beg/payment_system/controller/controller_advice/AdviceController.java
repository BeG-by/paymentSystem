package by.beg.payment_system.controller.controller_advice;

import by.beg.payment_system.exception.transfer_exception.LackOfMoneyException;
import by.beg.payment_system.exception.transfer_exception.TargetWalletNotFoundException;
import by.beg.payment_system.exception.user_exception.NoAccessException;
import by.beg.payment_system.exception.user_exception.UserIsNotAuthorizedException;
import by.beg.payment_system.exception.user_exception.UserIsPresentException;
import by.beg.payment_system.exception.user_exception.UserNotFoundException;
import by.beg.payment_system.exception.wallet_exception.WalletIsExistException;
import by.beg.payment_system.exception.wallet_exception.WalletNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@RestControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {

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
        return new ResponseEntity<>("User not found. Check your input data.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAccessException.class)
    public ResponseEntity<String> checkAdmin() {
        return new ResponseEntity<>("User role isn't administration.", HttpStatus.LOCKED);
    }

    @ExceptionHandler(UserIsNotAuthorizedException.class)
    public ResponseEntity<String> checkAuth() {
        return new ResponseEntity<>("User isn't authorized.", HttpStatus.LOCKED);
    }

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
}
