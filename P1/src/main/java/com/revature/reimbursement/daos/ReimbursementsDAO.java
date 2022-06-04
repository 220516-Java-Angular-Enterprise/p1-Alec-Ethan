package com.revature.reimbursement.daos;

import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import com.revature.reimbursement.util.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementsDAO implements CrudDAO<Reimbursements> {

    Connection con = DatabaseConnection.getCon();

    @Override
    public void save(Reimbursements obj) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursements (id, amount, submitted, resolved, " +
                    "description, receipt, payment_id, author_id, resolver_id, status_id, type_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, obj.getId());
            ps.setDouble(2, obj.getAmount());
            ps.setTimestamp(3, obj.getSubmitted());
            ps.setTimestamp(4, obj.getResolved());
            ps.setString(5, obj.getDescription());
            ps.setBlob(6, obj.getReceipt());
            ps.setString(7, obj.getPayment_id());
            ps.setString(8, obj.getAuthor_id());
            ps.setString(9, obj.getResolver_id());
            ps.setString(10, obj.getStatus_id());
            ps.setString(11, obj.getType_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when trying to save a new reimbursement to the Data Base.");
        }
    }

    //Temporary Solution I couldn't figure out how to update an entire object through a prepared statement,
    //so instead I am just deleting the obj from the database and reinserting it back w/ the updated data.
    @Override
    public void update(Reimbursements obj) {
        delete(obj.getId());
        save(obj);

    }

    @Override
    public void delete(String id) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM reimbursements WHERE id = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An Error occurred when trying to delete a reimbursement.");
        }
    }

    @Override
    public Reimbursements getById(String id) {
        Reimbursements rem = new Reimbursements();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                rem = new Reimbursements(
                        rs.getString("id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("submitted"),
                        rs.getTimestamp("resolved"),
                        rs.getString("description"),
                        rs.getBlob("receipt"),
                        rs.getString("payment_id"),
                        rs.getString("author_id"),
                        rs.getString("resolver_id"),
                        rs.getString("status_id"),
                        rs.getString("type_id"));


        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement by ID from the DataBase");
        }
        return rem;
    }

    @Override
    public List<Reimbursements> getAll() {
        List<Reimbursements> rems = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursements");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reimbursements rem = new Reimbursements(
                        rs.getString("id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("submitted"),
                        rs.getTimestamp("resolved"),
                        rs.getString("description"),
                        rs.getBlob("receipt"),
                        rs.getString("payment_id"),
                        rs.getString("author_id"),
                        rs.getString("resolver_id"),
                        rs.getString("status_id"),
                        rs.getString("type_id"));
                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement by ID from the DataBase");
        }
        return rems;
    }
}
