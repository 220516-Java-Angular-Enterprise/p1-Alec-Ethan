package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.ReimbursementStatusDAO;
import com.revature.reimbursement.models.ReimbursementStatus;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.util.annotations.Inject;

import java.util.List;

public class ReimbursementStatusService {
    @Inject
    private final ReimbursementStatusDAO ReimbursementStatusDAO;

    @Inject
    public ReimbursementStatusService(ReimbursementStatusDAO ReimbursementStatusDAO) {
        this.ReimbursementStatusDAO = ReimbursementStatusDAO;
    }


    public void saveReimbursement(ReimbursementStatus rem) { ReimbursementStatusDAO.save(rem); }
    public void updateReimbursement(ReimbursementStatus rem) { ReimbursementStatusDAO.update(rem); }
    public void deleteReimbursement(String id) { ReimbursementStatusDAO.delete(id); }
    public ReimbursementStatus getById(String id) { return ReimbursementStatusDAO.getById(id); }
    public List<ReimbursementStatus> getAll() { return ReimbursementStatusDAO.getAll(); }

    public ReimbursementStatus getRowByColumnValue(String column, String value) {
        return ReimbursementStatusDAO.getRowByColumnValue(column, value);
    }
    public List<ReimbursementStatus> getAllRowsByColumnValue(String column, String value) {
        return ReimbursementStatusDAO.getAllRowsByColumnValue(column, value);
    }
    public String getIdByStatus(String status) { return ReimbursementStatusDAO.getIdByStatus(status); }
}
