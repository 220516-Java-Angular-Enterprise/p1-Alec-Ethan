package com.revature.reimbursement.daos;

import com.revature.reimbursement.models.ReimbursementTypes;
import com.revature.reimbursement.models.UserRoles;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import com.revature.reimbursement.util.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRolesDAO implements CrudDAO<UserRoles> {
    //Connection con = DatabaseConnection.getCon();

    @Override
    public void save(UserRoles obj) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("INSERT INTO user_roles (id, role)" +
                    "VALUES (?, ?)");
            ps.setString(1, obj.getId());
            ps.setString(2, obj.getRole());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when trying to save a new reimbursement type to the Data Base.");
        }
    }

    @Override
    public void update(UserRoles obj) {
        delete(obj.getId());
        save(obj);
    }

    @Override
    public void delete(String id) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("DELETE FROM user_roles WHERE id = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An Error occurred when trying to delete a reimbursement type.");
        }
    }

    @Override
    public UserRoles getById(String id) {
        UserRoles rem = new UserRoles();

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_roles WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                rem = new UserRoles(
                        rs.getString("id"),
                        rs.getString("role"));


        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when trying to get a User Role by ID from the DataBase");
        }
        return rem;
    }

    @Override
    public List<UserRoles> getAll() {
        List<UserRoles> rems = new ArrayList<UserRoles>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_roles");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserRoles rem = new UserRoles();
                rem.setId(rs.getString("id"));
                rem.setRole(rs.getString("role"));
                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement Type by ID from the DataBase");
        }
        return rems;
    }

    @Override
    public UserRoles getRowByColumnValue(String column, String input){
        UserRoles row = new UserRoles();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_roles WHERE " + column + " = " + input);
            ResultSet rs = ps.executeQuery();

            while (rs.next())  {
                row.setId(rs.getString("id"));
                row.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Reimbursement by " + column + "using value: " + input);
        }

        return row;

    }

    @Override
    public List<UserRoles> getAllRowsByColumnValue(String column, String input) {
        List<UserRoles> rems = new ArrayList<UserRoles>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user_roles WHERE " + column + " = " + input);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UserRoles rem = new UserRoles();
                rem.setId(rs.getString("id"));
                rem.setRole(rs.getString("role"));
                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get all ReimbursementTypes by " + column + "using value: " + input);
        }
        return rems;
    }


    public boolean getExistsInColumnByString(String column, String input) {

        try {

            PreparedStatement ps = DatabaseConnection.getCon().prepareStatement("SELECT " + column + " FROM user_roles WHERE " + column + " = '" + input + "'");
            ResultSet rs = ps.executeQuery();

            while (rs.next())  {
                String val = rs.getString(column);
                //System.out.println(val);
                rs.close();
                ps.close();
                return true;
                //System.out.print("Column 1 returned ");
                //System.out.println(rs.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("FAILed to see if exists!!");
            //throw new RuntimeException("An error occurred when trying to access the file.");
        }

        return false;
    }


}
