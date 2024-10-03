package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::findMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromAccountIdHandler);

        return app;
    }

    private void registerHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account newAccount = om.readValue(ctx.body(), Account.class);
        Account registeredAccount = this.accountService.registerNewAccount(newAccount);

        if(registeredAccount != null){
            ctx.json(om.writeValueAsString(registeredAccount));
            ctx.status(200);
        }
        else
            ctx.status(400);
    }

    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account loginAccount = om.readValue(ctx.body(), Account.class);
        Account validatedAccount = this.accountService.loginAccount(loginAccount);

        if(validatedAccount != null){
            ctx.json(om.writeValueAsString(validatedAccount));
            ctx.status(200);
        }
        else
            ctx.status(401);
    }

    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message newMessage = om.readValue(ctx.body(), Message.class);
        Message validatedNewMessage = this.messageService.createMessage(newMessage);

        if(validatedNewMessage != null){
            ctx.json(om.writeValueAsString(validatedNewMessage));
            ctx.status(200);
        }
        else
            ctx.status(400);
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> allMessages = this.messageService.getAllMessages();
        ctx.json(allMessages);
        ctx.status(200);
    }

    private void findMessageByIdHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message currentMessage = this.messageService.getMessageByMessageId(messageId);

        if(currentMessage != null){
            ctx.json(om.writeValueAsString(currentMessage));
        }
        else
            ctx.json("");

        ctx.status(200);
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message currentMessage = this.messageService.deleteMessage(messageId);

        if(currentMessage != null){
            ctx.json(om.writeValueAsString(currentMessage));
        }
        else
            ctx.json("");

        ctx.status(200);
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageText = om.readValue(ctx.body().toString(), Message.class);
        Message currentMessage = this.messageService.updateMessage(messageId, messageText.getMessage_text());

        if(currentMessage != null){
            ctx.json(om.writeValueAsString(currentMessage));
            ctx.status(200);
        }
        else{
            ctx.json("");
            ctx.status(400);
        }
    }

    private void getAllMessagesFromAccountIdHandler(Context ctx) {
        List<Message> allMessages = this.messageService.getAllMessagesByAccountId(Integer.parseInt(ctx.pathParam("account_id")));
        ctx.json(allMessages);
        ctx.status(200);
    }
}
