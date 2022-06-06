package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.ReimbursementTypesDAO;
import com.revature.reimbursement.models.ReimbursementTypes;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.util.annotations.Inject;

import java.util.List;

public class ReimbursementTypesService {
    @Inject
    private final ReimbursementTypesDAO ReimbursementTypesDAO;

    @Inject
    public ReimbursementTypesService(ReimbursementTypesDAO ReimbursementTypeDAO) {
        this.ReimbursementTypesDAO = ReimbursementTypeDAO;
    }


    public void saveReimbursement(ReimbursementTypes rem) { ReimbursementTypesDAO.save(rem); }
    public void updateReimbursement(ReimbursementTypes rem) { ReimbursementTypesDAO.update(rem); }
    public void deleteReimbursement(String id) { ReimbursementTypesDAO.delete(id); }
    public ReimbursementTypes getById(String id) { return ReimbursementTypesDAO.getById(id); }
    public List<ReimbursementTypes> getAll() { return ReimbursementTypesDAO.getAll(); }

    public ReimbursementTypes getRowByColumnValue(String column, String value) {
        return ReimbursementTypesDAO.getRowByColumnValue(column, value);
    }
    public List<ReimbursementTypes> getAllRowsByColumnValue(String column, String value) {
        return ReimbursementTypesDAO.getAllRowsByColumnValue(column, value);
    }
}
