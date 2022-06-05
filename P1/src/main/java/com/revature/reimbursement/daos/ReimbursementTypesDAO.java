package com.revature.reimbursement.daos;

import com.revature.reimbursement.models.ReimbursementTypes;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import com.revature.reimbursement.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementTypesDAO implements CrudDAO<ReimbursementTypes> {
    Connection con = DatabaseConnection.getCon();

    @Override
    public void save(ReimbursementTypes obj) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursement_types (id, type)" +
                    "VALUES (?, ?)");
            ps.setString(1, obj.getId());
            ps.setString(2, obj.getType());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when trying to save a new reimbursement type to the Data Base.");
        }
    }

    @Override
    public void update(ReimbursementTypes obj) {
        delete(obj.getId());
        save(obj);
    }

    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM reimbursement_types WHERE id = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An Error occurred when trying to delete a reimbursement type.");
        }
    }

    @Override
    public ReimbursementTypes getById(String id) {
        ReimbursementTypes rem = new ReimbursementTypes();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_types WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                rem = new ReimbursementTypes(
                        rs.getString("id"),
                        rs.getString("type"));


        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement Type by ID from the DataBase");
        }
        return rem;
    }

    @Override
    public List<ReimbursementTypes> getAll() {
        List<ReimbursementTypes> rems = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReimbursementTypes rem = new ReimbursementTypes(
                        rs.getString("id"),
                        rs.getString("type"));
                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement Type by ID from the DataBase");
        }
        return rems;
    }
}
