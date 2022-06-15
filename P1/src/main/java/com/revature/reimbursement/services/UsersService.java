package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.UsersDAO;
import com.revature.reimbursement.dtos.requests.NewLoginRequest;
import com.revature.reimbursement.dtos.requests.NewUserRequest;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.util.annotations.Inject;
import com.revature.reimbursement.util.customException.InvalidRequestException;
import com.revature.reimbursement.util.customException.InvalidUserException;
import com.revature.reimbursement.util.customException.ResourceConflictException;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.UUID;

public class UsersService {

    @Inject
    private final UsersDAO usersDAO;

    @Inject
    public UsersService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Users login(NewLoginRequest request) throws Exception {

        Users user = new Users();
        if (!isValidUsername(request.getUsername()) || !isValidPassword(request.getPassword())) throw new InvalidRequestException("Invalid username or password");
        user = usersDAO.getUserByUsernameAndPassword(request.getUsername(), request.getPassword());
        if (user == null) throw new Exception("Invalid credentials provided!");
        return user;

    }

    public Users register(NewUserRequest request) {
        Users user = request.extractUser();
        user.setIs_active(false);

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

    public Users getById(String id) { return usersDAO.getById(id); }
    public List<Users> getAll() { return usersDAO.getAll(); }

    public void deleteUserByUsername(String username){
        String id = getRowByColumnValue("username", "'" + username + "'").getId();
        usersDAO.delete(id);
    }

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

    public List<Users> getAllUsers() {
        return usersDAO.getAll();
    }

    public void saveUser(Users currentUser) {
        usersDAO.save(currentUser);
    }

    public void updateUser(Users currentUser, boolean isPasswordChanged) {
        usersDAO.update(currentUser, isPasswordChanged);
    }
}
