package by.beg.payment_system.unit_test;

import by.beg.payment_system.dto.CreditOpenDTO;
import by.beg.payment_system.exception.CreditNotFoundException;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Credit;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.CreditDetailRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.CreditDetailService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class CreditDetailServiceImplTest {

    @Autowired
    private CreditDetailService creditDetailService;

    @MockBean
    private CreditDetailRepository creditDetailRepository;

    @MockBean
    private WalletRepository walletRepository;

    @Test
    void should_depositStatusOpen_walletBalanceEquals100_whenSending200()
            throws CreditNotFoundException, WalletNotFoundException, LackOfMoneyException {

        Credit credit = new Credit();
        credit.setCurrencyType(CurrencyType.USD);

        CreditDetail creditDetail = new CreditDetail();
        creditDetail.setCurrentDebt(new BigDecimal(500));
        creditDetail.setCreditStatus(Status.OPEN);
        creditDetail.setCredit(credit);

        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(300));

        CreditOpenDTO creditOpenDTO = new CreditOpenDTO();
        creditOpenDTO.setMoney(new BigDecimal(200));

        Mockito.doReturn(Optional.of(creditDetail))
                .when(creditDetailRepository)
                .findByUser(Mockito.any());

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), Mockito.any());

        creditDetailService.repayDebt(new User(), creditOpenDTO);

        assertEquals(new BigDecimal(300), creditDetail.getCurrentDebt());
        assertEquals(new BigDecimal(100), wallet.getBalance());
        assertEquals(new Date().getTime(), creditDetail.getLastUpdate().getTime(), 10);
        assertEquals(creditDetail.getCreditStatus(), Status.OPEN);

    }

    @Test
    void should_throwLackOfMoneyException_whenSending600() {

        Credit credit = new Credit();
        credit.setCurrencyType(CurrencyType.USD);

        CreditDetail creditDetail = new CreditDetail();
        creditDetail.setCurrentDebt(new BigDecimal(500));
        creditDetail.setCreditStatus(Status.OPEN);
        creditDetail.setCredit(credit);

        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(300));

        CreditOpenDTO creditOpenDTO = new CreditOpenDTO();
        creditOpenDTO.setMoney(new BigDecimal(600));

        Mockito.doReturn(Optional.of(creditDetail))
                .when(creditDetailRepository)
                .findByUser(Mockito.any());

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), Mockito.any());


        assertThrows(LackOfMoneyException.class, () -> {
            creditDetailService.repayDebt(new User(), creditOpenDTO);
        });


        assertEquals(new BigDecimal(500), creditDetail.getCurrentDebt());
        assertEquals(new BigDecimal(300), wallet.getBalance());
        assertEquals(creditDetail.getCreditStatus(), Status.OPEN);

    }

    @Test
    void should_depositStatusClosed_walletBalanceEquals100_whenSending500()
            throws CreditNotFoundException, WalletNotFoundException, LackOfMoneyException {

        Credit credit = new Credit();
        credit.setCurrencyType(CurrencyType.USD);

        CreditDetail creditDetail = new CreditDetail();
        creditDetail.setCurrentDebt(new BigDecimal(500));
        creditDetail.setCreditStatus(Status.OPEN);
        creditDetail.setCredit(credit);

        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(600));

        CreditOpenDTO creditOpenDTO = new CreditOpenDTO();
        creditOpenDTO.setMoney(new BigDecimal(500));

        Mockito.doReturn(Optional.of(creditDetail))
                .when(creditDetailRepository)
                .findByUser(Mockito.any());

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), Mockito.any());

        creditDetailService.repayDebt(new User(), creditOpenDTO);

        assertEquals(new BigDecimal(0), creditDetail.getCurrentDebt());
        assertEquals(new BigDecimal(100), wallet.getBalance());
        assertEquals(new Date().getTime(), creditDetail.getLastUpdate().getTime(), 10);
        assertEquals(creditDetail.getCreditStatus(), Status.CLOSED);

    }

}