package com.revature.reimbursement.daos;

import com.revature.reimbursement.models.ReimbursementStatus;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import com.revature.reimbursement.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementStatusDAO implements CrudDAO<ReimbursementStatus> {
    Connection con = DatabaseConnection.getCon();

    @Override
    public void save(ReimbursementStatus obj) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursements (id, status) " +
                    "VALUES (?, ?)");
            ps.setString(1, obj.getId());
            ps.setString(2, obj.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when trying to save a new reimbursement status to the Data Base.");
        }
    }

    @Override
    public void update(ReimbursementStatus obj) {
        delete(obj.getId());
        save(obj);
    }

    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM reimbursement_statuses WHERE id = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An Error occurred when trying to delete a reimbursement status.");
        }
    }

    @Override
    public ReimbursementStatus getById(String id) {
        ReimbursementStatus rem = new ReimbursementStatus();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_statuses WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                rem = new ReimbursementStatus(
                        rs.getString("id"),
                        rs.getString("status"));

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement Status by ID from the DataBase");
        }
        return rem;
    }

    @Override
    public List<ReimbursementStatus> getAll() {
        List<ReimbursementStatus> rems = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_statuses");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReimbursementStatus rem = new ReimbursementStatus(
                        rs.getString("id"),
                        rs.getString("status"));
                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement Status by ID from the DataBase");
        }
        return rems;
    }
}
