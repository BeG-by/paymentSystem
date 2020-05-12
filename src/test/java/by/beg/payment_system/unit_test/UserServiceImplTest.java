package by.beg.payment_system.unit_test;

import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.repository.UserRepository;
import by.beg.payment_system.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @Test
    void should_throwUnremovableStatusException_whenDeleteUser() {

        User user = new User();
        CreditDetail creditDetail = new CreditDetail();
        user.setCreditDetails(List.of(creditDetail));
        user.setWallets(new HashSet<>());
        user.setDepositDetails(new ArrayList<>());

        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(Mockito.anyLong());

        assertThrows(UnremovableStatusException.class, () -> userService.deleteUser(Mockito.anyLong()));
        Mockito.verify(userRepository, Mockito.times(0)).delete(ArgumentMatchers.any(User.class));

    }

    @Test
    void should_userEqualsNull_whenDeleteUser() {

        User user = new User();
        user.setId(1);
        user.setCreditDetails(new ArrayList<>());
        user.setWallets(new HashSet<>());
        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setUser(user);
        user.setTransferDetails(List.of(transferDetail));
        user.setDepositDetails(new ArrayList<>());

        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(Mockito.anyLong());

        userService.deleteUser(ArgumentMatchers.anyLong());
        assertNull(transferDetail.getUser());
        Mockito.verify(userRepository, Mockito.times(1)).delete(ArgumentMatchers.any(User.class));

    }

    @Test
    void should_returnUserAdmin_whenGivenUser() {
        User user = new User();
        user.setId(1);

        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(Mockito.anyLong());

        userService.establishAdminRole(Mockito.anyLong());
        assertEquals(UserRole.ADMIN, user.getUserRole());
        assertTrue(Duration.between(LocalDateTime.now(), user.getLastModified()).getSeconds() < 3);

    }

    @Test
    void should_returnDeleteStatus_whenGivenStatusAndUser() {
        User user = new User();
        user.setId(1);

        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(Mockito.anyLong());

        userService.changeStatus(Mockito.anyLong(), Status.DELETED);
        assertEquals(Status.DELETED, user.getStatus());
        assertTrue(Duration.between(LocalDateTime.now(), user.getLastModified()).getSeconds() < 3);

    }

}