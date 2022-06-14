package com.revature.reimbursement.services;

import com.revature.reimbursement.dtos.requests.NewLoginRequest;
import com.revature.reimbursement.models.Users;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersServiceTest {

    @Test
    void login() {
        NewLoginRequest loginRequest = new NewLoginRequest();
        loginRequest.setUsername("myCOOLName123");
        loginRequest.setPassword("P@ssw0rd");

        Users fakeUser = new Users("12f8c662-6e72-4211-9569-5ea757d4fc15", loginRequest.getUsername(), "myCOOLEmail", loginRequest.getPassword(),
                "CoolerName", "Supercoolerdude","0",false);

        doReturn(fakeUser).when(mockDAO).getByUsernameandPassword("Username1","P@ssword32");
        assertThrows(NotAuthorizedException.class, () -> usersServices.login(fakeLoginRequest));
        // ------------------------------------------

        // Invalid User cred

        doReturn(null).when(mockDAO).getByUsernameandPassword("Username1","P@ssword32");
        assertThrows(InvalidAuthenticationException.class, () -> usersServices.login(fakeLoginRequest));

        // ------------------------------------------

        // Works
        LoginRequest testRequest3 = new LoginRequest();

        fakeUser = new Users("1", fakeLoginRequest.getUsername(), " ", fakeLoginRequest.getPassword(),
                "", "",true,"DEFAULT");

        doReturn(fakeUser).when(mockDAO).getByUsernameandPassword("Username1","P@ssword32");
        assertEquals(fakeUser, usersServices.login(fakeLoginRequest));
    }

    @Test
    void register() {
    }
}