package by.beg.payment_system.unit_test;

import by.beg.payment_system.dto.request.CreditOpenRequestDTO;
import by.beg.payment_system.exception.LackOfMoneyException;
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
import java.time.Duration;
import java.time.LocalDateTime;
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
    void should_creditStatusOpen_walletBalanceEquals100_whenSending200() throws LackOfMoneyException {

        Credit credit = new Credit();
        credit.setCurrencyType(CurrencyType.USD);

        CreditDetail creditDetail = new CreditDetail();
        creditDetail.setCurrentDebt(new BigDecimal(500));
        creditDetail.setCreditStatus(Status.OPEN);
        creditDetail.setCredit(credit);

        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(300));

        CreditOpenRequestDTO creditOpenRequestDTO = new CreditOpenRequestDTO();
        creditOpenRequestDTO.setMoney(new BigDecimal(200));

        Mockito.doReturn(Optional.of(creditDetail))
                .when(creditDetailRepository)
                .findByUser(Mockito.any());

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), Mockito.any());

        creditDetailService.repayDebt(new User(), creditOpenRequestDTO);

        assertEquals(new BigDecimal(300), creditDetail.getCurrentDebt());
        assertEquals(new BigDecimal(100), wallet.getBalance());
        assertTrue(Duration.between(LocalDateTime.now(), creditDetail.getLastModified()).getSeconds() < 3);
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

        CreditOpenRequestDTO creditOpenRequestDTO = new CreditOpenRequestDTO();
        creditOpenRequestDTO.setMoney(new BigDecimal(600));

        Mockito.doReturn(Optional.of(creditDetail))
                .when(creditDetailRepository)
                .findByUser(Mockito.any());

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), Mockito.any());


        assertThrows(LackOfMoneyException.class, () ->
                creditDetailService.repayDebt(new User(), creditOpenRequestDTO)
        );


        assertEquals(new BigDecimal(500), creditDetail.getCurrentDebt());
        assertEquals(new BigDecimal(300), wallet.getBalance());
        assertEquals(creditDetail.getCreditStatus(), Status.OPEN);

    }

    @Test
    void should_creditStatusClosed_walletBalanceEquals100_whenSending500() throws LackOfMoneyException {

        Credit credit = new Credit();
        credit.setCurrencyType(CurrencyType.USD);

        CreditDetail creditDetail = new CreditDetail();
        creditDetail.setCurrentDebt(new BigDecimal(500));
        creditDetail.setCreditStatus(Status.OPEN);
        creditDetail.setCredit(credit);

        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(600));

        CreditOpenRequestDTO creditOpenRequestDTO = new CreditOpenRequestDTO();
        creditOpenRequestDTO.setMoney(new BigDecimal(500));

        Mockito.doReturn(Optional.of(creditDetail))
                .when(creditDetailRepository)
                .findByUser(Mockito.any());

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), Mockito.any());

        creditDetailService.repayDebt(new User(), creditOpenRequestDTO);

        assertEquals(BigDecimal.ZERO, creditDetail.getCurrentDebt());
        assertEquals(new BigDecimal(100), wallet.getBalance());
        assertTrue(Duration.between(LocalDateTime.now(), creditDetail.getLastModified()).getSeconds() < 3);
        assertEquals(creditDetail.getCreditStatus(), Status.CLOSED);

    }

}