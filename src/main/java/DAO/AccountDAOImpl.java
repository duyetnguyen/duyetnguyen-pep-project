package DAO;


import Model.Account;

import Util.ConnectionUtil;
import java.sql.*;



public class AccountDAOImpl implements AccountDAO {

    //method to check if username exist 
    public boolean usernameExist (String username){

        Connection connection = ConnectionUtil.getConnection();

        try{
            //sql logic
            String sql = "SELECT username FROM Account WHERE username = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            //check if rs return anything
            return rs.next();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return true;

    }

    //method to check if an Account exist
    @Override
    public boolean accountExist (int accountId){

        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM Account WHERE account_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    //method to register account to add to Account table

    @Override
    public Account registerAccount(Account account){

        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO Account (username, password) VALUES (?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();

            
            if (pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Account userLogin(Account acc){

        Connection connection = ConnectionUtil.getConnection();
        
        try{
            String sql = "SELECT * FROM Account WHERE username = ?  AND password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());

            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                rs.getString("username"), rs.getString("password"));
                return account;
            }
            
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;   
    }

}
