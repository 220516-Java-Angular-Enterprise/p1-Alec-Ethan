package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.ReimbursementsDAO;
import com.revature.reimbursement.daos.UserRolesDAO;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.models.UserRoles;
import com.revature.reimbursement.util.annotations.Inject;

import java.util.List;

public class UserRolesService {

    @Inject
    private final UserRolesDAO userRolesDAO;

    @Inject
    public UserRolesService(UserRolesDAO userRolesDAO) {
        this.userRolesDAO = userRolesDAO;
    }


    public void saveReimbursement(UserRoles rem) { userRolesDAO.save(rem); }
    public void updateReimbursement(UserRoles rem) { userRolesDAO.update(rem); }
    public void deleteReimbursement(String id) { userRolesDAO.delete(id); }
    public UserRoles getById(String id) { return userRolesDAO.getById(id); }
    public List<UserRoles> getAll() { return userRolesDAO.getAll(); }

    public UserRoles getRowByColumnValue(String column, String value) {
        return userRolesDAO.getRowByColumnValue(column, value);
    }
    public List<UserRoles> getAllRowsByColumnValue(String column, String value) {
        return userRolesDAO.getAllRowsByColumnValue(column, value);
    }

    public boolean getExistsInColumnByStringValue(String column, String value){
        return userRolesDAO.getExistsInColumnByString(column, value);
    }


}
