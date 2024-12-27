package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerUserHandler);
        app.post("/login", this::loginUserHandler);
        app.post("/messages", this::createNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedAccount)).status(200);
        }
    }

    private void loginUserHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account retrievedAccount = accountService.getAccountByUsernameAndPassword(account);
        if (retrievedAccount == null) {
            context.status(401);
        } else {
            context.json(mapper.writeValueAsString(retrievedAccount)).status(200);
        }
    }

    private void createNewMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(addedMessage)).status(200);
        }
    }

    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        context.json(messageService.getAllMessages()).status(200);
    }

    private void getMessageByIdHandler(Context context) throws JsonProcessingException {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message retrievedMessage = messageService.getMessageById(message_id);
        if (retrievedMessage == null) {
            context.result("").status(200);
        } else {
            context.json(retrievedMessage).status(200);
        }
    }

    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException {
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(message_id);
        if (deletedMessage == null) {
            context.result("").status(200);
        } else {
            context.json(deletedMessage).status(200);
        }
    }

    private void updateMessageByIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        String message_text = message.getMessage_text();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageById(message_text, message_id);
        if (updatedMessage == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(updatedMessage)).status(200);
        }
    }

    private void getAllMessagesByUserHandler(Context context) throws JsonProcessingException {
        int account_id = Integer.parseInt(context.pathParam("account_id")); 
        context.json(messageService.getAllMessagesByAccountId(account_id)).status(200);
    }
}