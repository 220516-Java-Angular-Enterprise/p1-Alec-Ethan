package com.revature.reimbursement.daos;

import com.revature.reimbursement.models.ReimbursementTypes;
import com.revature.reimbursement.models.Users;
import com.revature.reimbursement.util.customException.InvalidSQLException;
import com.revature.reimbursement.util.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsersDAO implements CrudDAO<Users> {
    //Connection con = DatabaseConnection.getCon();

    @Override
    public void save(Users obj) {
        if (obj.getId() == null)
            obj.setId(UUID.randomUUID().toString());
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (id, username, email, password, given_name, surname, is_active, role_id)" +
                    "VALUES (?, ?, ?,  crypt(?, gen_salt('bf')), ?, ?, ?, ?)");
            ps.setString(1, obj.getId());
            ps.setString(2, obj.getUsername());
            ps.setString(3, obj.getEmail());
            ps.setString(4, obj.getPassword());
            ps.setString(5, obj.getGiven_name());
            ps.setString(6, obj.getSurname());
            ps.setBoolean(7, obj.isIs_active());
            ps.setString(8, obj.getRole_id());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when trying to save a new Users type to the Data Base.");
        }
    }

    //DO NOT USE!
    @Override
    public void update(Users obj) {
        throw new RuntimeException("Method not implemented... Use the update method with 2 arguments!");
    }

    //@Override
    public void update(Users obj, boolean isPasswordChanged) {
        if (isPasswordChanged) {
            delete(obj.getId());
            save(obj);
        }
        else{
            try (Connection con = ConnectionFactory.getInstance().getConnection()){
                PreparedStatement ps = con.prepareStatement("UPDATE users " +
                        "SET id = ?, " +
                        "username = ?," +
                        "email = ?," +
                        "given_name = ?," +
                        "surname = ?," +
                        "is_active = ?," +
                        "role_id = ?" +
                        "WHERE id = '" + obj.getId() + "'");


                ps.setString(1, obj.getId());
                ps.setString(2, obj.getUsername());
                ps.setString(3, obj.getEmail());
                ps.setString(4, obj.getGiven_name());
                ps.setString(5, obj.getSurname());
                ps.setBoolean(6, obj.isIs_active());
                ps.setString(7, obj.getRole_id());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new InvalidSQLException("An error occurred when trying to save a new Users type to the Data Base.");
            }
        }
    }

    @Override
    public void delete(String id) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new InvalidSQLException("An Error occurred when trying to delete a Users type.");
        }
    }

    @Override
    public Users getById(String id) {
        Users rem = new Users();

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
                rem = new Users();
                rem.setId(rs.getString("id"));
                rem.setUsername(rs.getString("username"));
                rem.setEmail(rs.getString("email"));
                rem.setPassword(rs.getString("password"));
                rem.setGiven_name(rs.getString("given_name"));
                rem.setSurname(rs.getString("surname"));
                rem.setIs_active(rs.getBoolean("is_active"));
                rem.setRole_id(rs.getString("role_id"));


        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Users Type by ID from the DataBase");
        }
        return rem;
    }

    @Override
    public List<Users> getAll() {
        List<Users> rems = new ArrayList<Users>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Users rem = new Users();
                rem.setId(rs.getString("id"));
                rem.setUsername(rs.getString("username"));
                rem.setEmail(rs.getString("email"));
                rem.setPassword(rs.getString("password"));
                rem.setGiven_name(rs.getString("given_name"));
                rem.setSurname(rs.getString("surname"));
                rem.setIs_active(rs.getBoolean("is_active"));
                rem.setRole_id(rs.getString("role_id"));

                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Users Type by ID from the DataBase");
        }
        return rems;
    }

    @Override
    public Users getRowByColumnValue(String column, String input){
        Users row = new Users();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE " + column + " = " + input);
            ResultSet rs = ps.executeQuery();

            while (rs.next())  {
                row.setId(rs.getString("id"));
                row.setUsername(rs.getString("username"));
                row.setEmail(rs.getString("email"));
                row.setPassword(rs.getString("password"));
                row.setGiven_name(rs.getString("given_name"));
                row.setSurname(rs.getString("surname"));
                row.setIs_active(rs.getBoolean("is_active"));
                row.setRole_id(rs.getString("role_id"));
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get a Users by " + column + "using value: " + input);
        }

        return row;

    }

    @Override
    public List<Users> getAllRowsByColumnValue(String column, String input) {
        List<Users> rems = new ArrayList<Users>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE " + column + " = " + input);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Users rem = new Users();
                rem.setId(rs.getString("id"));
                rem.setUsername(rs.getString("username"));
                rem.setEmail(rs.getString("email"));
                rem.setPassword(rs.getString("password"));
                rem.setGiven_name(rs.getString("given_name"));
                rem.setSurname(rs.getString("surname"));
                rem.setIs_active(rs.getBoolean("is_active"));
                rem.setRole_id(rs.getString("role_id"));
                rems.add(rem);
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get all Users by " + column + "using value: " + input);
        }
        return rems;
    }

    public Users getUserByUsernameAndPassword(String username, String password) {
        Users user = null;

        try (Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = crypt(?, password)");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new Users(rs.getString("id"), rs.getString("username"),
                        rs.getString("email"), rs.getString("password"),
                        rs.getString("given_name"), rs.getString("surname"),
                        rs.getString("role_id"), rs.getBoolean("is_active"));
            }
        } catch (SQLException e) {
            throw new InvalidSQLException("An error occurred when tyring to get data from to the database.");
        }

        return user;
    }

    public List<String> getAllUsernames() {
        List<String> usernames = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT username FROM users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usernames.add(rs.getString("username").toLowerCase());
            }
        } catch (SQLException e) {
            throw new RuntimeException("An error occurred when tyring to get data from to the database.");
        }

        return usernames;
    }
}
