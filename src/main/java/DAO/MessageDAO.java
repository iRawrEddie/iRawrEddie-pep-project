package DAO;

import java.sql.*;
import Model.Message;
import java.util.ArrayList;
import Util.ConnectionUtil;

public class MessageDAO {
    /**
     * Retrieves a message from the message table by its message_id.
     * @param message_id of the message to retrieve.
     * @return the Message Object or null.
     */
    public Message selectMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                return new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all messages from the message table.
     * @return a list of all Message objects from message table.
     */
    public ArrayList<Message> selectAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            String sql = "SELECT * FROM message";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()) {
                messages.add(new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")));
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Retrieves a list of Message objects from message table based on account_id.
     * @param account_id of the account to retrieves message for.
     * @return a list containing Message objects loaded from message table. 
     */
    public ArrayList<Message> selectAllUserMessages(int account_id) {        
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            String sql = "SELECT * FROM message WHERE message.posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()) {
                messages.add(new Message(rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"))
                );
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
     * Stores a new message in message table.
     * @param message Message object to be saved to message table.
     * @return the saved Message object from database with updated message id or null.
     */
    public Message insertNewMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            while(pkeyResultSet.next()) {
                message.setMessage_id(pkeyResultSet.getInt(1));
                return message;
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Updates a message in the message table.
     * @param message_id id of the message to be updated in message table.
     * @param message Message object containing updated text to save to message table.
     * @return True/False indicating save success or failure.
     */
    public boolean updateMessage(int message_id, Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message_id);
            return preparedStatement.executeUpdate() > 0;
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Remove a message from the message table.
     * @param message_id of Message to be deleted from message table.
     * @return True/False indicating save success or failure.
     */
    public boolean deleteMessageById(int message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            return preparedStatement.executeUpdate() > 0;
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
