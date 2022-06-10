package com.revature.reimbursement.dtos.requests;

public class NewLoginRequest {
    private String username, password;

    public NewLoginRequest() {
        super();
    }

    public NewLoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    //<editor-fold desc = "Get/Set">

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //</editor-fold desc = "Get/Set">

}
