package com.revature.reimbursement.dtos.responses;

import com.revature.reimbursement.models.Users;

public class Principal {
    private String id;
    private String username;
    private String role;

    public Principal() {
        super();
    }

    public Principal(Users user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole_id();
    }

    public Principal(String id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    //<editor-fold desc="Get/Set">

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    //</editor-fold>
}
