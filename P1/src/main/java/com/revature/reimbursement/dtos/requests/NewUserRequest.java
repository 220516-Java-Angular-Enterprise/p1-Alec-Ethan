package com.revature.reimbursement.dtos.requests;

import com.revature.reimbursement.models.Users;

public class NewUserRequest {

    private String id, username, email, password, given_name, surname, role_id;
    private boolean is_active;

    public NewUserRequest() { }

    public NewUserRequest(String id, String username, String email, String password, String given_name, String surname, String role_id, boolean is_active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.given_name = given_name;
        this.surname = surname;
        this.role_id = role_id;
        this.is_active = is_active;
    }

    public NewUserRequest(String username, String email, String password, String given_name, String surname, String role_id) {
        this.id = "0";
        this.username = username;
        this.email = email;
        this.password = password;
        this.given_name = given_name;
        this.surname = surname;
        this.role_id = role_id;
        this.is_active = false;
    }

    //Change Role ID to getRoleByID!!!!!
    public Users extractUser() {
        return new Users(username, email, password, given_name, surname, role_id);
    }


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

    @Override
    public String toString() {
        return "NewUserRequest{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", given_name='" + given_name + '\'' +
                ", surname='" + surname + '\'' +
                ", role_id='" + role_id + '\'' +
                ", is_active=" + is_active +
                '}';
    }
}
