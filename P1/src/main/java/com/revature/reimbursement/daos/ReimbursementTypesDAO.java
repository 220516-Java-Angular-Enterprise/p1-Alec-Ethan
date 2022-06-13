package com.revature.reimbursement.daos;

import com.revature.reimbursement.models.ReimbursementStatus;
import com.revature.reimbursement.models.ReimbursementTypes;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import com.revature.reimbursement.util.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementTypesDAO implements CrudDAO<ReimbursementTypes> {
    //Connection con = DatabaseConnection.getCon();

    @Override
    public void save(ReimbursementTypes obj) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
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
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
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

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
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

    public String getIdByType(String name) {
        String id = "";

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_types WHERE type = ?");
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
    public List<ReimbursementTypes> getAll() {
        List<ReimbursementTypes> rems = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_types");
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

    @Override
    public ReimbursementTypes getRowByColumnValue(String column, String input){
        ReimbursementTypes row = new ReimbursementTypes();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_types WHERE " + column + " = " + input);
            ResultSet rs = ps.executeQuery();

            while (rs.next())  {
                row.setId(rs.getString("id"));
                row.setType(rs.getString("status"));
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a reimbursement_types by " + column + "using value: " + input);
        }

        return row;

    }

    @Override
    public List<ReimbursementTypes> getAllRowsByColumnValue(String column, String input) {
        List<ReimbursementTypes> rems = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reimbursement_types WHERE " + column + " = " + input);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ReimbursementTypes rem = new ReimbursementTypes(
                        rs.getString("id"),
                        rs.getString("type"));
                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get all ReimbursementTypes by " + column + "using value: " + input);
        }
        return rems;
    }
}
