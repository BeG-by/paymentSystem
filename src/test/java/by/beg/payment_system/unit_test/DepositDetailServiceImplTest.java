package by.beg.payment_system.unit_test;

import by.beg.payment_system.dto.DepositOpenRequestDTO;
import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.DepositNotFoundException;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Deposit;
import by.beg.payment_system.model.finance.DepositDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.DepositDetailRepository;
import by.beg.payment_system.repository.DepositRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.DepositDetailService;
import by.beg.payment_system.service.util.DepositDetailFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@RunWith(SpringRunner.class)
class DepositDetailServiceImplTest {

    @Autowired
    private DepositDetailService depositDetailService;

    @MockBean
    private DepositRepository depositRepository;

    @MockBean
    private DepositDetailRepository depositDetailRepository;

    @MockBean
    private WalletRepository walletRepository;

    @Test
    void should_walletBalanceEquals50_whenSending100()
            throws CurrencyConverterException, LackOfMoneyException, WalletNotFoundException, DepositNotFoundException {

        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(150));

        DepositOpenRequestDTO openDTO = new DepositOpenRequestDTO(new BigDecimal(100), CurrencyType.USD, "BYN360");

        Deposit deposit = new Deposit(1, "BYN360", CurrencyType.USD, 360, new BigDecimal(10), false, Status.OPEN, new ArrayList<>());
        DepositDetail depositDetail = DepositDetailFactory.createInstance(deposit, openDTO.getMoney());


        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), ArgumentMatchers.any(User.class));

        Mockito.doReturn(Optional.of(deposit))
                .when(depositRepository)
                .findByName(Mockito.any());

        Mockito.doReturn(depositDetail)
                .when(depositDetailRepository)
                .save(Mockito.any());

        depositDetailService.create(openDTO, new User());

        assertEquals(new BigDecimal(50), wallet.getBalance());
        assertEquals(new BigDecimal(110), depositDetail.getReturnBalance().setScale(0, RoundingMode.DOWN));

        Mockito.verify(depositDetailRepository, Mockito.times(1)).save(ArgumentMatchers.any(DepositDetail.class));


    }

    @Test
    void should_trowLackOfMoneyException_whenSending200() {

        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(150));

        DepositOpenRequestDTO openDTO = new DepositOpenRequestDTO(new BigDecimal(200), CurrencyType.USD, "BYN360");

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), ArgumentMatchers.any(User.class));

        assertThrows(LackOfMoneyException.class, () -> {
            depositDetailService.create(openDTO, new User());
        });

        assertEquals(new BigDecimal(150), wallet.getBalance());
        Mockito.verify(depositDetailRepository, Mockito.times(0)).save(ArgumentMatchers.any(DepositDetail.class));

    }


}