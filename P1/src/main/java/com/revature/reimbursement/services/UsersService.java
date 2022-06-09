package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.UsersDAO;
import com.revature.reimbursement.dtos.requests.NewLoginRequest;
import com.revature.reimbursement.dtos.requests.NewUserRequest;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.util.annotations.Inject;
import com.revature.reimbursement.util.customException.InvalidRequestException;
import com.revature.reimbursement.util.customException.InvalidUserException;
import com.revature.reimbursement.util.customException.ResourceConflictException;

import java.util.List;
import java.util.UUID;

public class UsersService {

    @Inject
    private final UsersDAO usersDAO;

    @Inject
    public UsersService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Users login(NewLoginRequest newLogin) {
        /* List<User> users = new ArrayList<>() */
        /* users = userDAO.getAll() */

        String username = newLogin.getUsername();
        String password = newLogin.getPassword();

        Users user = new Users();
        List<Users> users = usersDAO.getAll();

        for (Users u : users) {
            if (u.getUsername().equals(username)) {
                user.setId(u.getId());
                user.setUsername(u.getUsername());
                if (u.getPassword().equals(password)) {
                    user.setPassword(u.getPassword());
                    break;
                }
            }
            if (u.getPassword().equals(password)) {
                user.setPassword(u.getPassword());
            }
        }

        return isValidCredentials(user);
    }

    public Users register(NewUserRequest request) {
        Users user = request.extractUser();

        if (isNotDuplicateUsername(user.getUsername())) {
            if (isValidUsername(user.getUsername())) {
                if (isValidPassword(user.getPassword())) {
                    user.setId(UUID.randomUUID().toString());
                    usersDAO.save(user);
                } else throw new InvalidRequestException("Invalid password. Minimum eight characters, at least one letter, one number and one special character.");
            } else throw new InvalidRequestException("Invalid username. Username needs to be 8-20 characters long.");
        } else throw new ResourceConflictException("Username is already taken :(");

        return user;
    }



    public void saveReimbursement(Users rem) { usersDAO.save(rem); }
    public void updateReimbursement(Users rem) { usersDAO.update(rem); }
    public void deleteReimbursement(String id) { usersDAO.delete(id); }
    public Users getById(String id) { return usersDAO.getById(id); }
    public List<Users> getAll() { return usersDAO.getAll(); }


    private boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    private boolean isNotDuplicateUsername(String username) {
        return !usersDAO.getAllUsernames().contains(username);
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }

    private Users isValidCredentials(Users user) {
        if (user.getUsername() == null && user.getPassword() == null)
            throw new InvalidUserException("Incorrect username and password.");
        else if (user.getUsername() == null) throw new InvalidUserException("Incorrect username.");
        else if (user.getPassword() == null) throw new InvalidUserException("Incorrect password.");

        return user;
    }

    public Users getRowByColumnValue(String column, String value) {
        return usersDAO.getRowByColumnValue(column, value);
    }
    public List<Users> getAllRowsByColumnValue(String column, String value) {
        return usersDAO.getAllRowsByColumnValue(column, value);
    }

}
