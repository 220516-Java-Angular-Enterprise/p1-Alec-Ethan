package com.revature.reimbursement.services;

import com.revature.reimbursement.daos.ReimbursementsDAO;
import com.revature.reimbursement.dtos.requests.NewReimbursementRequest;
import com.revature.reimbursement.dtos.requests.ReimbursementUpdateRequest;
import com.revature.reimbursement.dtos.requests.StatusChangeRequest;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.util.annotations.Inject;

import java.util.List;

public class ReimbursementsService {

    @Inject
    private final ReimbursementsDAO reimbursementsDAO;

    @Inject
    public ReimbursementsService(ReimbursementsDAO reimbursementsDAO) {
        this.reimbursementsDAO = reimbursementsDAO;
    }


    public void saveReimbursement(Reimbursements rem) { reimbursementsDAO.save(rem); }

    public Reimbursements saveReimbursement(NewReimbursementRequest rem) {
        Reimbursements newRem = rem.extractReimbursement();
        reimbursementsDAO.save(newRem);
        return newRem;
    }

    public Reimbursements updateReimbursement(NewReimbursementRequest rem) {
        Reimbursements newRem = rem.extractReimbursement();
        reimbursementsDAO.update(newRem);
        return newRem;
    }

    public Reimbursements updateReimbursementStatus(StatusChangeRequest rem) {
        Reimbursements newRem = getById(rem.getRem_id());
        System.out.println(newRem.getAmount());
        System.out.println(newRem.getId());
        newRem.setStatus_id(rem.getStatus_id());
        newRem.setResolved(rem.getResolved());
        newRem.setResolver_id(rem.getResolver_id());
        reimbursementsDAO.update(newRem);
        return newRem;
    }

    //public void updateReimbursement(Reimbursements rem) { reimbursementsDAO.update(rem); }
    public void deleteReimbursement(String id) { reimbursementsDAO.delete(id); }
    public Reimbursements getById(String id) { return reimbursementsDAO.getById(id); }
    public List<Reimbursements> getAll() { return reimbursementsDAO.getAll(); }
    public List<Reimbursements> getAllByAuthorID(String author_id) { return reimbursementsDAO.getAllByAuthorID(author_id); }
    public Reimbursements getRowByColumnValue(String column, String value) {
        return reimbursementsDAO.getRowByColumnValue(column, value);
    }
    public List<Reimbursements> getAllRowsByColumnValue(String column, String value) {
        return reimbursementsDAO.getAllRowsByColumnValue(column, value);
    }
}
