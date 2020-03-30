package by.beg.payment_system.util;

import by.beg.payment_system.service.CreditDetailService;
import by.beg.payment_system.service.DepositDetailService;
import by.beg.payment_system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SchedulerUtil {

    private DepositDetailService depositDetailService;
    private CreditDetailService creditDetailService;
    private UserService userService;

    public SchedulerUtil(DepositDetailService depositDetailService, CreditDetailService creditDetailService, UserService userService) {
        this.depositDetailService = depositDetailService;
        this.creditDetailService = creditDetailService;
        this.userService = userService;
    }

    @Scheduled(cron = "0 */3 * * * *")
    public void refreshDepositsDetail() {

        depositDetailService.refreshAll();
        log.info("DepositsDetails have been refreshed");

    }

    @Scheduled(cron = "0 */3 * * * *")
    public void refreshCreditsDetail() {

        creditDetailService.refreshAll();
        log.info("CreditDetails have been refreshed");

    }

    @Scheduled(cron = "0 */2 * * * *")
    public void clearTokens() {

        userService.clearTokens();
        log.info("Tokens have been refreshed");

    }


}
