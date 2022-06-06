package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.UserRolesDAO;
import com.revature.reimbursement.daos.UsersDAO;
import com.revature.reimbursement.models.UserRoles;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.util.annotations.Inject;

import java.util.List;

public class UsersService {

    @Inject
    private final UsersDAO usersDAO;

    @Inject
    public UsersService(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }


    public void saveReimbursement(Users rem) { usersDAO.save(rem); }
    public void updateReimbursement(Users rem) { usersDAO.update(rem); }
    public void deleteReimbursement(String id) { usersDAO.delete(id); }
    public Users getById(String id) { return usersDAO.getById(id); }
    public List<Users> getAll() { return usersDAO.getAll(); }

    public Users getRowByColumnValue(String column, String value) {
        return usersDAO.getRowByColumnValue(column, value);
    }
    public List<Users> getAllRowsByColumnValue(String column, String value) {
        return usersDAO.getAllRowsByColumnValue(column, value);
    }

}
