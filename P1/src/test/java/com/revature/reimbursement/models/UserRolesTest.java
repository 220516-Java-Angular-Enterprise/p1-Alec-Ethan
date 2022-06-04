package com.revature.reimbursement.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRolesTest {

    @Test
    void getRole_id() {
        UserRoles ur = new UserRoles();
        ur.setRole("Foo");
        assertEquals("Foo", ur.getRole());
    }

    @Test
    void setRole_id() {
    }

    @Test
    void getRole() {
    }

    @Test
    void setRole() {
    }
}