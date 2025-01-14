package DAO;

import java.sql.*;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    /**
     * Logs into the account if username and password match an account in the database.
     * @param account to be logged into.
     * @return Account object with updated account_id or null.
     */
    public Account login(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();
            
            while(rs.next()) {
                account.setAccount_id(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                return account;
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Verifies if an account exists in the database by its account_id.
     * @param accountId of the account to verify existence.
     * @return True/False representing existence.
     */
    public boolean accountExists(int accountId) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            return preparedStatement.executeQuery().next();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Verifies if an account exists in the database by its username.
     * @param username of the account to verify existence.
     * @return True/False representing existence.
     */
    public boolean accountExists(String username) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            return preparedStatement.executeQuery().next();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    /**
     * Stores the new account in account table.
     * @param account The account object to be saved.
     * @return New Account object with updated account_id object or old object provided.
     */
    public Account insertNewAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "INSERT INTO account (username, password) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            
            while(pkeyResultSet.next()) {
                account.setAccount_id(pkeyResultSet.getInt(1));
                return account;
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
