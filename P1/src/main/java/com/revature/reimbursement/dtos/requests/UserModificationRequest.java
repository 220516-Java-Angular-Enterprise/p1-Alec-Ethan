package com.revature.reimbursement.dtos.requests;



public class UserModificationRequest {
    private String requestType; //UPDATE,DELETE, GETINFO
    private String username; //how to find user.
    //When value == "" skip.
    private String email, password, given_name, surname, role_id;
    private boolean is_active;

    public UserModificationRequest() {
        super();
    }

    public UserModificationRequest(String requestType, String username){
        this.requestType = requestType;
        this.username = username;
    }

    public UserModificationRequest(String requestType, String username, String email, String password, String given_name, String surname, String role_id, boolean is_active){
        this.requestType = requestType;
        this.username = username;
        this.email = email;
        this.password = password;
        this.given_name = given_name;
        this.surname = surname;
        this.role_id = role_id;
        this.is_active = is_active;
    }

    //<editor-fold desc = "Get/Set">

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    //</editor-fold>

}
