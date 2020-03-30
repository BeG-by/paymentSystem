package by.beg.payment_system.unit_test;

import by.beg.payment_system.exception.UnremovableStatusException;
import by.beg.payment_system.exception.UserNotFoundException;
import by.beg.payment_system.model.enumerations.Status;
import by.beg.payment_system.model.finance.CreditDetail;
import by.beg.payment_system.model.finance.TransferDetail;
import by.beg.payment_system.model.security.Token;
import by.beg.payment_system.model.user.User;
import by.beg.payment_system.model.user.UserRole;
import by.beg.payment_system.repository.TokenRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenRepository tokenRepository;


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
    void should_returnUser_whenDeleteUser() throws UserNotFoundException, UnremovableStatusException {

        User user = new User();
        user.setId(1);
        user.setCreditDetails(new ArrayList<>());
        user.setWallets(new HashSet<>());
        TransferDetail transferDetail = new TransferDetail();
        user.setTransferDetails(List.of(transferDetail));
        user.setDepositDetails(new ArrayList<>());

        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(Mockito.anyLong());

        User returnUser = userService.deleteUser(ArgumentMatchers.anyLong());
        assertEquals(user.getId(), returnUser.getId());
        Mockito.verify(userRepository, Mockito.times(1)).delete(ArgumentMatchers.any(User.class));


    }

    @Test
    void should_returnUserAdmin_whenGivenUser() throws UserNotFoundException {
        User user = new User();
        user.setId(1);

        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(Mockito.anyLong());

        User adminUser = userService.getAdminRole(Mockito.anyLong());
        assertEquals(user.getId(), adminUser.getId());
        assertEquals(UserRole.ADMIN, user.getUserRole());
        assertEquals(new Date().getTime(), adminUser.getLastUpdate().getTime(), 10);

    }

    @Test
    void should_returnDeleteStatus_whenGivenStatusAndUser() throws UserNotFoundException {
        User user = new User();
        user.setId(1);

        Mockito.doReturn(Optional.of(user))
                .when(userRepository)
                .findById(Mockito.anyLong());

        User adminUser = userService.changeStatus(Mockito.anyLong(), Status.DELETED);
        assertEquals(user.getId(), adminUser.getId());
        assertEquals(Status.DELETED, user.getStatus());
        assertEquals(new Date().getTime(), adminUser.getLastUpdate().getTime(), 10);

    }


    @Test
    void should_deleteOneToken_whenGivenTwoToken() {

        Token firstToken = new Token();
        Calendar firstCalendar = Calendar.getInstance();
        firstCalendar.add(Calendar.MINUTE, -20);
        firstToken.setCreateDate(firstCalendar.getTime());

        Token secondToken = new Token();
        Calendar secondCalendar = Calendar.getInstance();
        secondCalendar.add(Calendar.MINUTE, -10);
        secondToken.setCreateDate(secondCalendar.getTime());

        List<Token> tokens = List.of(firstToken, secondToken);

        Mockito.doReturn(tokens)
                .when(tokenRepository)
                .findAll();

        userService.clearTokens();

        Mockito.verify(tokenRepository, Mockito.times(1)).delete(ArgumentMatchers.any(Token.class));

    }


}