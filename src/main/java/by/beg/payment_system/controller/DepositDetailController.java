//package by.beg.payment_system.controller;
//
//import by.beg.payment_system.dto.DateDTO;
//import by.beg.payment_system.dto.DepositOpenDTO;
//import by.beg.payment_system.exception.*;
//import by.beg.payment_system.model.enumerations.Status;
//import by.beg.payment_system.model.finance.DepositDetail;
//import by.beg.payment_system.model.user.User;
//import by.beg.payment_system.model.user.UserRole;
//import by.beg.payment_system.service.DepositDetailService;
//import by.beg.payment_system.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequestMapping("/depositDetail")
//@Validated
//public class DepositDetailController {
//
//    private DepositDetailService depositDetailService;
//    private UserService userService;
//
//    public DepositDetailController(DepositDetailService depositDetailService, UserService userService) {
//        this.depositDetailService = depositDetailService;
//        this.userService = userService;
//    }
//
//    //USER
//
//    @PostMapping("/create")
//    public ResponseEntity<DepositDetail> createDeposit(@RequestHeader String token, @RequestBody @Valid DepositOpenDTO openDTO)
//            throws UserIsNotAuthorizedException, LackOfMoneyException, WalletNotFoundException, DepositNotFoundException, CurrencyConverterException, UserBlockedException {
//
//        User user = userService.checkAuthorization(token);
//        return ResponseEntity.ok(depositDetailService.create(openDTO, user));
//    }
//
//    @GetMapping("/getAll")
//    public ResponseEntity<List<DepositDetail>> getAllByUser(@RequestHeader String token)
//            throws UserIsNotAuthorizedException, UserBlockedException {
//
//        User user = userService.checkAuthorization(token);
//        return ResponseEntity.ok().body(depositDetailService.getAllByUser(user));
//    }
//
//    @PutMapping("/pickUp")
//    public ResponseEntity<List<DepositDetail>> pickUpDeposits(@RequestHeader String token)
//            throws UserIsNotAuthorizedException, WalletNotFoundException, UserBlockedException {
//
//        User user = userService.checkAuthorization(token);
//        return ResponseEntity.ok().body(depositDetailService.pickUp(user));
//    }
//
//
//    //ADMIN
//
//    @GetMapping("/getAll/{userId}")
//    public ResponseEntity<List<DepositDetail>> adminGetAllByUser(@RequestHeader String token, @PathVariable long userId)
//            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException, UserNotFoundException {
//
//        checkAdminRole(userService.checkAuthorization(token));
//        return ResponseEntity.ok().body(depositDetailService.getAllForAdmin(userId));
//    }
//
//    @GetMapping("/getAllByStatus/{status}")
//    public ResponseEntity<List<DepositDetail>> adminGetAllByStatus(@RequestHeader String token, @PathVariable Status status)
//            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {
//
//        checkAdminRole(userService.checkAuthorization(token));
//        return ResponseEntity.ok().body(depositDetailService.getAllForAdminByStatus(status));
//    }
//
//    @PostMapping("/getAll/filterByCreateDate")
//    public ResponseEntity<List<DepositDetail>> adminFilterByDate(@RequestHeader String token, @RequestBody @Valid DateDTO dateDTO)
//            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {
//
//        checkAdminRole(userService.checkAuthorization(token));
//        return ResponseEntity.ok().body(depositDetailService.filterByCreateDate(dateDTO.getFirstDate(), dateDTO.getSecondDate()));
//
//    }
//
//    @DeleteMapping("/delete/{depositId}")
//    public ResponseEntity<DepositDetail> delete(@RequestHeader String token, @PathVariable long depositId)
//            throws UserIsNotAuthorizedException, NoAccessException, DepositNotFoundException, UnremovableStatusException, UserBlockedException {
//
//        checkAdminRole(userService.checkAuthorization(token));
//        return ResponseEntity.ok().body(depositDetailService.delete(depositId));
//
//    }
//
//    @DeleteMapping("/deleteAll")
//    public ResponseEntity<List<DepositDetail>> deleteAll(@RequestHeader String token)
//            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {
//
//        checkAdminRole(userService.checkAuthorization(token));
//        return ResponseEntity.ok().body(depositDetailService.deleteAll());
//
//    }
//
//    @PutMapping("/refreshAll")
//    public ResponseEntity<String> refreshAll(@RequestHeader String token)
//            throws UserIsNotAuthorizedException, NoAccessException, UserBlockedException {
//
//        checkAdminRole(userService.checkAuthorization(token));
//        depositDetailService.refreshAll();
//        return ResponseEntity.ok().body("DepositsDetails were refreshed");
//
//    }
//
//    private void checkAdminRole(User user) throws NoAccessException {
//        if (!user.getUserRole().equals(UserRole.ADMIN)) {
//            throw new NoAccessException();
//        }
//    }
//
//}
