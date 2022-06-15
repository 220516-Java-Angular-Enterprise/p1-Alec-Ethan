package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.UsersDAO;
import com.revature.reimbursement.dtos.requests.NewLoginRequest;
import com.revature.reimbursement.dtos.requests.NewUserRequest;
import com.revature.reimbursement.dtos.requests.UserModificationRequest;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.util.customException.InvalidRequestException;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

class UsersServiceTest {

    @InjectMocks
    private UsersService usersServiceMock = new UsersService(new UsersDAO());

    //<editor-fold desc="@Spy">
    @Spy
    private UsersDAO usersDAOMock;
    @Spy
    Users userMock = new Users();
    @Spy
    NewLoginRequest newLoginRequest = new NewLoginRequest();
    @Spy
    NewUserRequest newUserRequest = new NewUserRequest();
    @Spy
    UserModificationRequest userModificationRequest = new UserModificationRequest();
    //</editor-fold>

    @Test
    void register() {
        newUserRequest.setUsername("TestUserName");
        newUserRequest.setEmail("Test_Email");
        newUserRequest.setPassword("TestP@ssw0rd");
        newUserRequest.setGiven_name("TestName");
        newUserRequest.setSurname("TestSurName");
        newUserRequest.setRole_id("2");

        //Valid User
        assertEquals(newUserRequest.extractUser().getUsername(),usersServiceMock.register(newUserRequest).getUsername());

        //Duplicated Username
        assertThrows(InvalidSQLException.class, ()->usersServiceMock.register(newUserRequest));

        //Invalid Password
        newUserRequest.setPassword("1");
        assertThrows(InvalidRequestException.class, ()->usersServiceMock.register(newUserRequest));

        usersServiceMock.deleteUserByUsername("TestUserName");
    }

    @Test
    void login() throws Exception {
        newUserRequest.setUsername("TestUserName");
        newUserRequest.setEmail("Test_Email");
        newUserRequest.setPassword("TestP@ssw0rd");
        newUserRequest.setGiven_name("TestName");
        newUserRequest.setSurname("TestSurName");
        newUserRequest.setRole_id("2");
        usersServiceMock.register(newUserRequest);

        //Valid Login
        newLoginRequest.setUsername("TestUserName");
        newLoginRequest.setPassword("TestP@ssw0rd");
        assertEquals(newUserRequest.extractUser().getUsername(), usersServiceMock.login(newLoginRequest).getUsername());

        //Invalid Username
        newLoginRequest.setUsername("Not a Real Username");
        assertThrows(InvalidRequestException.class, ()->usersServiceMock.login(newLoginRequest));

        //Invalid Password
        newLoginRequest.setUsername("TestUserName");
        newLoginRequest.setPassword("Not the Real Password");
        assertThrows(InvalidRequestException.class, ()->usersServiceMock.login(newLoginRequest));

        usersServiceMock.deleteUserByUsername("TestUserName");
    }

}