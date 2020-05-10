package by.beg.payment_system.unit_test;

import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.Credit;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
class WalletServiceImplTest {

    @Autowired
    private WalletService walletService;

    @MockBean
    private WalletRepository walletRepository;


    @Test
    void should_throwUnremovableStatusException_whenDeleteWallet() {

        User user = new User();
        Wallet wallet = new Wallet();
        CreditDetail creditDetail = new CreditDetail();
        Credit credit = new Credit();

        credit.setCurrencyType(CurrencyType.USD);
        creditDetail.setCredit(credit);
        user.setCreditDetails(List.of(creditDetail));
        user.setDepositDetails(new ArrayList<>());
        wallet.setCurrencyType(CurrencyType.USD);
        user.setWallets(Set.of(wallet));

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), Mockito.any());

        assertThrows(UnremovableStatusException.class, () -> walletService.delete(CurrencyType.USD, user));
        Mockito.verify(walletRepository, Mockito.times(0)).delete(wallet);

    }

    @Test
    void should_deleteWallet_whenValidWallet() throws WalletNotFoundException, UnremovableStatusException {

        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setId(1);
        CreditDetail creditDetail = new CreditDetail();
        Credit credit = new Credit();

        credit.setCurrencyType(CurrencyType.USD);
        creditDetail.setCredit(credit);
        creditDetail.setCreditStatus(Status.CLOSED);
        user.setCreditDetails(List.of(creditDetail));
        user.setDepositDetails(new ArrayList<>());
        wallet.setCurrencyType(CurrencyType.USD);
        user.setWallets(Set.of(wallet));

        Mockito.doReturn(Optional.of(wallet))
                .when(walletRepository)
                .findByCurrencyTypeAndUser(Mockito.any(), Mockito.any());

        walletService.delete(CurrencyType.USD, user);

        Mockito.verify(walletRepository, Mockito.times(1)).delete(wallet);
    }

}