package com.revature.reimbursement.daos;

import com.revature.reimbursement.models.ReimbursementStatus;
import com.revature.reimbursement.models.Reimbursements;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import com.revature.reimbursement.util.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementStatusDAO implements CrudDAO<ReimbursementStatus> {
    //Connection con = DatabaseConnection.getCon();

    @Override
    public void save(ReimbursementStatus obj) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("INSERT INTO reimbursement_statuses (id, status) " +
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
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
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

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
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

    public String getIdByStatus(String name) {
        String id = "";

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_statuses WHERE status = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
               id = rs.getString("id");

        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement Status by ID from the DataBase");
        }
        return id;
    }

    @Override
    public List<ReimbursementStatus> getAll() {
        List<ReimbursementStatus> rems = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
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

    @Override
    public ReimbursementStatus getRowByColumnValue(String column, String input){
        ReimbursementStatus row = new ReimbursementStatus();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_statuses WHERE " + column + " = " + input);
            ResultSet rs = ps.executeQuery();

            while (rs.next())  {
                row.setId(rs.getString("id"));
                row.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a reimbursement_statuses by " + column + "using value: " + input);
        }

        return row;

    }

    @Override
    public List<ReimbursementStatus> getAllRowsByColumnValue(String column, String input) {
        List<ReimbursementStatus> rems = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_statuses WHERE " + column + " = " + input);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReimbursementStatus rem = new ReimbursementStatus(
                        rs.getString("id"),
                        rs.getString("status"));
                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get all ReimbursementStatus by " + column + "using value: " + input);
        }
        return rems;
    }
}
