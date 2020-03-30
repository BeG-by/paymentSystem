package by.beg.payment_system.unit_test;

import by.beg.payment_system.exception.CurrencyConverterException;
import by.beg.payment_system.exception.LackOfMoneyException;
import by.beg.payment_system.exception.TargetWalletNotFoundException;
import by.beg.payment_system.exception.WalletNotFoundException;
import by.beg.payment_system.model.enumerations.CurrencyType;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.finance.Wallet;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.repository.TransferRepository;
import by.beg.payment_system.repository.WalletRepository;
import by.beg.payment_system.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class TransferServiceImplTest {

    @Autowired
    private TransferService transferService;

    @MockBean
    private WalletRepository walletRepository;

    @MockBean
    private TransferRepository transferRepository;


    @Test
    void should_targetWalletBalanceEquals100_walletBalanceEquals0()
            throws CurrencyConverterException, WalletNotFoundException, TargetWalletNotFoundException, LackOfMoneyException {

        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(100));
        user.setWallets(Set.of(wallet));

        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setId(1);
        transferDetail.setCurrencyType(CurrencyType.USD);
        transferDetail.setMoneySend(new BigDecimal(100));

        Wallet targetWallet = new Wallet();
        targetWallet.setBalance(new BigDecimal(0));
        targetWallet.setCurrencyType(CurrencyType.USD);

        Mockito.doReturn(Optional.of(targetWallet))
                .when(walletRepository)
                .findWalletByWalletValue(Mockito.any());

        Mockito.doReturn(transferDetail)
                .when(transferRepository)
                .save(Mockito.any());


        TransferDetail resultTransfer = transferService.doTransfer(user, transferDetail);

        assertEquals(new BigDecimal(0), wallet.getBalance());
        assertEquals(new BigDecimal(100), targetWallet.getBalance());
        assertEquals(resultTransfer.getId(), transferDetail.getId());
        assertEquals(new BigDecimal(100), transferDetail.getMoneyReceive());

        Mockito.verify(walletRepository, Mockito.times(1)).findWalletByWalletValue(Mockito.any());
        Mockito.verify(transferRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    void should_throwLackOfMoneyException_whenWalletBalanceLess100() {

        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setCurrencyType(CurrencyType.USD);
        wallet.setBalance(new BigDecimal(50));
        user.setWallets(Set.of(wallet));

        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setId(1);
        transferDetail.setCurrencyType(CurrencyType.USD);
        transferDetail.setMoneySend(new BigDecimal(100));

        Wallet targetWallet = new Wallet();
        targetWallet.setBalance(new BigDecimal(0));
        targetWallet.setCurrencyType(CurrencyType.USD);

        Mockito.doReturn(Optional.of(targetWallet))
                .when(walletRepository)
                .findWalletByWalletValue(Mockito.any());

        Mockito.doReturn(transferDetail)
                .when(transferRepository)
                .save(Mockito.any());

        assertThrows(LackOfMoneyException.class, () -> {
            transferService.doTransfer(user, transferDetail);
        });


        assertEquals(new BigDecimal(50), wallet.getBalance());
        assertEquals(new BigDecimal(0), targetWallet.getBalance());
        assertNull(transferDetail.getMoneyReceive());

        Mockito.verify(walletRepository, Mockito.times(0)).findWalletByWalletValue(Mockito.any());
        Mockito.verify(transferRepository, Mockito.times(0)).save(Mockito.any());

    }


}