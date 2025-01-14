package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    // AccountDAO for class to communicate with database
    private AccountDAO accountDAO;

    // no-args constructor to create a new instance of AccountService.
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    // Constructor for when an AccountDAO is provided.
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Creates a new account if valid username, password, and no other user
     * exists by the username given.
     * @param account Account object from json.
     * @return the newly created account from the database.
     */
    public Account registerNewAccount(Account account) {
        return accountObjectIsInvalid(account) || accountDAO.accountExists(account.getUsername()) ? null :
            accountDAO.insertNewAccount(account);
    }

    /**
     * Attempts logging into an existing account.
     * @param account Account object from json.
     * @return the account if it exists and credentials match.
     */
    public Account login(Account account) {
        return accountObjectIsInvalid(account) ? null :
            accountDAO.login(account);
    }

    /**
     * Check if an account exists by account_id.
     * @param account_id the value of the Message posted_by or Account account_id property
     * @return True/False indicating account existence.
     */
    public boolean accountExists(int account_id) {
        return accountDAO.accountExists(account_id);
    }
    
    /**
     * Helper method defining invalid Account property values.
     * @param account Account object from json.
     * @return True/False indicating if it is invalid.
     */
    private boolean accountObjectIsInvalid(Account account) {
        return account.getUsername() == null||// username is not null or blank
        account.getUsername() == ""         ||
        account.getPassword().length() < 4;// password must have 4 or more characters
    }
}