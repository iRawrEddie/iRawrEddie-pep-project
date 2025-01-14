package Service;
import Model.Message;
import DAO.MessageDAO;
import java.util.ArrayList;

public class MessageService {
    //MessageDAO for class to communicate with database.
    private MessageDAO messageDAO;

    // no-args constructor
    public MessageService() {
        messageDAO = new MessageDAO();
    }
    
    // constructor with provided MessageDAO object
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * Retrieves all messages from the database.
     * @return a list of all messages.
     */
    public ArrayList<Message> getAllMessages() {
        return messageDAO.selectAllMessages();
    }

    /** 
     * Retrieves a message from the database by Id if valid
     * @param message_id the id of the message to retrieve
     * @return The corresponding message or null.
    */
    public Message getMessageById(int message_id) {
        return messageDAO.selectMessageById(message_id);
    }

    /**
     * Retrieves a list of messages from a given user by account id if valid.
     * @param account_id the id of the account to retrieve messages for
     * @return A list of messages for the account.
     */
    public ArrayList<Message> getAllMessagesByAccountId(int account_id) {
        return messageDAO.selectAllUserMessages(account_id);
    }

    /**
     * Creates a new message in the database if valid message, posted by, and text.
     * @param message Message object from json.
     * @return the newly created message or null
     */
    public Message createNewMessage(Message message) {
        return messageObjectIsInvalid(message) ? null :
            messageDAO.insertNewMessage(message);
    }

    /**
     * Updates an existing message in the database if it is valid and 
     * is already an existing message in the table.
     * @param message_id the id of the message to be updated
     * @param message Message object from json.
     * @return True/False on update success/failure.
     */
    public boolean canUpdateMessage(int message_id, Message message) {
        return messageObjectIsInvalid(message) || messageDAO.selectMessageById(message_id) == null ? false :
            messageDAO.updateMessage(message_id, message);
    }

    /**
     * Deletes a message from the database.
     * @param message_id the id of the message to be deleted
     * @return True/False indicating delete success/failure.
     */
    public boolean canDeleteMessageById(int message_id) {
        return messageDAO.deleteMessageById(message_id);
    }

    /**
     * Helper method to check for empty message text and message text length.
     * @param message Message object to verify.
     */
    private boolean messageObjectIsInvalid(Message message) {
        return message.getMessage_text() == null||// new message_text is not null
        message.getMessage_text() == ""         ||// message is not empty
        message.getMessage_text().length() >  255;// message_text is not over 255 characters
    }
}
