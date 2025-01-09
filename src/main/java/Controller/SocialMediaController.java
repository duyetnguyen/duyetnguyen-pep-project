package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAOImpl;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;
import Service.AccountServiceImpl;
import Service.MessageServiceImpl;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountDAOImpl accountDAOImpl;
    AccountServiceImpl accountServiceImpl;
    MessageServiceImpl messageServiceImpl;
    MessageDAOImpl messageDAOImpl;
    

    public SocialMediaController(){
        this.accountServiceImpl = new AccountServiceImpl();
        this.messageServiceImpl = new MessageServiceImpl();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::verifyUserLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessageByUserHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    /*
     * Handler to post a new account 
     * If MessageService return a null account, (meaning posting an Account was unccessful), the API will return a 400
     * @param ctx the context object handles information HTTP requests and generates response within Javalin.
     
     */
    private void postAccountHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountServiceImpl.registerAccount(account);
        if (addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }else {
            ctx.status(400);
        }
    }

    /*
     * Handler to post a verify account login
     */
    private void verifyUserLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account acct = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountServiceImpl.userLogin(acct);

        if(loginAccount != null){
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(loginAccount));
        }else {
            ctx.status(401);
        }
    }

    /*
     * Handler to post a new message. 
     * If MessageService return a null message, (meaning posting a Message was unccessful), the API will return a 400
     * @param ctx the context object handles information HTTP requests and generates response within Javalin.
     */
    private void postMessageHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageServiceImpl.insertMessage(message);
        if (addedMessage != null){
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }else {
            ctx.status(400);
        }  
    }

    /*
     * Handler to get all messages
     * The response body should contain a JSON representation of a list containing all messages retrieved from the database
     */
    private void getAllMessagesHandler (Context ctx) {
        List<Message> messages = messageServiceImpl.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    /*
     * Handler to get a message by message Id
     */
    private void getMessageByIdHandler (Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        
        String messageIdString = ctx.pathParam("message_id");
        int message_id = Integer.parseInt(messageIdString);
        
        Message newmessage = messageServiceImpl.getMessageById(message_id);
        System.out.println(newmessage);

        if(newmessage != null){
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(newmessage));

        }else{
            ctx.status(200).result("");
        }
    }

    /*
     * Handler to retrieve all messages by a particular user
     */
    private void getAllMessageByUserHandler (Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String postedByString = ctx.pathParam("account_id");
        int postedby = Integer.parseInt(postedByString);
        List<Message> messages = messageServiceImpl.getMessageByUser(postedby);
        
        if(messages != null){
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(messages));
        }else {
            ctx.result("");
        }
    }

    /*
     * Handler to delete a message by Id
     */
    private void deleteMessageByIdHandler (Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        String messageIdString = ctx.pathParam("message_id");
        int id = Integer.parseInt(messageIdString);
        System.out.println("Received Deleted request for message_id=");
        Message message = messageServiceImpl.getMessageById(id);

        if (message != null){
            messageServiceImpl.deleteMessageById(id);
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(message));
        } else {
            ctx.status(200).result("");
        }
    }

    /*Handler to update a message by message Id
     */
    private void updateMessageByIdHandler (Context ctx) throws JsonProcessingException {
        
        //utilizee jackson to convert jsonString to a message object
        ObjectMapper mapper = new ObjectMapper();
        String messageIdString = ctx.pathParam("message_id");
        int id = Integer.parseInt(messageIdString);

        //retrieve the json string from request body
        Message updatedMessage = mapper.readValue(ctx.body(), Message.class);
        updatedMessage.setMessage_id(id);

        //update message with new message text
        Message upMessage = messageServiceImpl.updateMessageById(updatedMessage);
        
        if(upMessage != null){
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(upMessage));
        }else {
            ctx.status(400);
        }
    }




    
    
        
    }



    








