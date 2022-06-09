package com.revature.reimbursement.dtos.responses;

import com.revature.reimbursement.models.Users;

public class Principal {
    private String id;
    private String username;
    private String role_id;

    public Principal() {
        super();
    }

    public Principal(Users user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role_id = user.getRole_id();
    }

    public Principal(String id, String username, String role_id) {
        this.id = id;
        this.username = username;
        this.role_id = role_id;
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
        return role_id;
    }

    public void setRole(String role_id) {
        this.role_id = role_id;
    }

    //</editor-fold>
}
