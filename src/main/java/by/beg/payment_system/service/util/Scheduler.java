package by.beg.payment_system.service.util;

import by.beg.payment_system.service.CreditDetailService;
import by.beg.payment_system.service.DepositDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Scheduler {

    private DepositDetailService depositDetailService;
    private CreditDetailService creditDetailService;

    public Scheduler(DepositDetailService depositDetailService, CreditDetailService creditDetailService) {
        this.depositDetailService = depositDetailService;
        this.creditDetailService = creditDetailService;
    }


    @Scheduled(cron = "0 */3 * * * *")
    public void refreshDepositsDetail() {

        log.info("DepositDetails started to refresh");
        depositDetailService.refreshAll();
        log.info("DepositsDetails were refreshed");

    }

    @Scheduled(cron = "0 */3 * * * *")
    public void refreshCreditsDetail() {

        log.info("CreditDetails started to refresh");
        creditDetailService.refreshAll();
        log.info("CreditDetails were refreshed");

    }


}
