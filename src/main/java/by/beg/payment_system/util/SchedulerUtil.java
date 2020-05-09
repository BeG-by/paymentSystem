package by.beg.payment_system.util;

import by.beg.payment_system.service.CreditDetailService;
import by.beg.payment_system.service.DepositDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SchedulerUtil {

    private DepositDetailService depositDetailService;
    private CreditDetailService creditDetailService;


    public SchedulerUtil(DepositDetailService depositDetailService, CreditDetailService creditDetailService) {
        this.depositDetailService = depositDetailService;
        this.creditDetailService = creditDetailService;
    }

    @Scheduled(cron = "0 */45 * * * *")
    public void refreshDepositsDetail() {

        depositDetailService.refreshAll();
        log.info("DepositsDetails have been refreshed");

    }

    @Scheduled(cron = "0 */45 * * * *")
    public void refreshCreditsDetail() {

        creditDetailService.refreshAll();
        log.info("CreditDetails have been refreshed");

    }

}
