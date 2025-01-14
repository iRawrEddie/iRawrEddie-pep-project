package Controller;

import Model.*;
import Service.*;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        accountService = new AccountService();
        messageService = new MessageService();
        
        // GET:
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);
        // POST:
        app.post("login", this::loginHandler);
        app.post("register", this::registrationHandler);
        app.post("messages", this::postNewMessageHandler);
        // PATCH
        app.patch("messages/{message_id}", this::patchMessageByIdHandler);
        // DELETE
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);

        return app;
    }

    /**
     * GET Handler to get all messages for logged in user from API. path: /messages
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());      
    }
    
    /**
     * GET Handler to update a message by message id from API. path: /accounts/{account_id}/messages
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesByAccountIdHandler(Context context) {
        context.json(messageService.getAllMessagesByAccountId(context.pathParamAsClass("account_id", int.class).get())); 
    }

    /**
     * GET Handler to get message by Id from API. path: /message/{message_id}
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByIdHandler(Context context) {
        Message message = null;
        // if message is not found, return 200, otherwise return message
        if ((message = messageService.getMessageById(context.pathParamAsClass("message_id", int.class).get())) == null) context.result();
        else context.json(message);
    }

    /**
     * POST Handler to register new Account. path: /register
     * @param context The Javalin Context object manages information about both the HTTP request and response including
     * body of Account JSON.
     */
    private void registrationHandler(Context context) {
        Account newAccount = null;
        // if account registration fails, return 400, otherwise 200
        if ((newAccount = accountService.registerNewAccount(context.bodyAsClass(Account.class))) == null) context.status(400);
        else context.json(newAccount);
    }
    
    /**
     * POST Handler to login to SocialMedia API. path: /login
     * @param context The Javalin Context object manages information about both the HTTP request and response including body of 
     * Account JSON not including account_id.
     */
    private void loginHandler(Context context) {
        Account account = null; 
        // if login fails, return 401, otherwise return account
        if ((account = accountService.login(context.bodyAsClass(Account.class))) == null) context.status(401);
        else context.json(account);
    }

    /**
     * POST Handler to post a new message to the API. path: /messages
     * @param context The Javalin Context object manages information about both the HTTP request and response and body
     * of new message.
     */
    private void postNewMessageHandler(Context context) {
        Message message = context.bodyAsClass(Message.class);

        // if the account doesn't exist or message fails to be created, return 400, otherwise return the new messgae
        if (!accountService.accountExists(message.getPosted_by()) ||
            (message = messageService.createNewMessage(message)) == null) context.status(400);
        else context.json(message);
    }
    
    /**
     * PATCH Handler to update message by Id from API. path: /message/{message_id}
     * @param context The Javalin Context object manages information about both the HTTP request and response and
     * body containing at MOST new message_text values.
     */
    private void patchMessageByIdHandler(Context context) {
        int message_id = context.pathParamAsClass("message_id", int.class).get();
        // if update is not successful return 400, otherwise get the newly updated message
        if (!messageService.canUpdateMessage(message_id, context.bodyAsClass(Message.class))) context.status(400);
        else context.json(messageService.getMessageById(message_id));
    }
    
    /**
     * DELETE Handler to delete message by Id from API. path: /message/{message_id}
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByIdHandler(Context context) {
        Message message = null; 
        int message_id = context.pathParamAsClass("message_id", int.class).get();
        // if message doesn't exist or deletion fails, return 200 with empty body otherwise, return the deleted message.
        if ((message = messageService.getMessageById(message_id)) == null ||
            !messageService.canDeleteMessageById(message_id)) context.result();
        else context.json(message);
    }
}