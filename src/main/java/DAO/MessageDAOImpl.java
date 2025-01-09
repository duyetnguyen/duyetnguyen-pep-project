package DAO;

import Model.Message;

import java.sql.Connection;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MessageDAOImpl implements MessageDAO {

    //method to check if  posted_by refers to a real, existing user
    @Override
    public boolean postedbyExistUser (int account_id){
        Connection connection = ConnectionUtil.getConnection();

        try{
            //sql logic
            String sql = "SELECT account_id FROM Account WHERE account_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            
            ResultSet rs = ps.executeQuery();
            //check if rs return anything
            return rs.next();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    //method to check if message_id exist
    @Override
    public boolean messageIdExist (int msgId){
        Connection connection = ConnectionUtil.getConnection();

        try{
            //sql logic
            String sql = "SELECT message_id FROM Message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, msgId);
            
            ResultSet rs = ps.executeQuery();
            //check if rs return anything
            return rs.next();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return true;
    }

    //method to create a message to the Message table
    @Override
    public Message insertMessage( Message message){

        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();

            if (pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    // method to get all message from the Message table
    @Override
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM Message;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    //method to get a message from Message table by message Id
    @Override
    public Message getMessageById (int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
        
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
            Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            return message;    
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

   /*
   method to retrieve all message by a particular user
   */ 
   @Override
    public List<Message> getMessageByUser (int posted_by){

        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, posted_by);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));

                messages.add(message);
            }
        }catch( SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }

    /*
    * method to delete a message from Message table
    */
    @Override
    public void deleteMessageById (int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE * FROM Message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            
            ps.executeQuery();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //method to update a message by Id
    @Override
    public Message updateMessageById (Message message){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "UPDATE Message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.setInt(4, message.getMessage_id());

            ps.executeUpdate();
            //return message;

        } catch (SQLException e) {
            e.getMessage();  
        }
        //return null;
        return message;
    }

    

}
