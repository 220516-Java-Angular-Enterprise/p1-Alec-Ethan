package com.revature.reimbursement.models;

public class UserRoles {

    String id;
    String role;

    public UserRoles(String id, String role) {
        this.id = id;
        this.role = role;
    }

    public UserRoles() {}
    //<editor-fold desc="Get/Set">

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    //</editor-fold>

}
