package Service;

import DAO.MessageDAOImpl;
import Model.Message;

import java.util.ArrayList;
import java.util.List;


public class MessageServiceImpl implements MessageService {
    
    public MessageDAOImpl messageDAOImpl;

    public MessageServiceImpl(){
        messageDAOImpl = new MessageDAOImpl();
    }

    //instance of MessageDAOImpl
    public MessageServiceImpl (MessageDAOImpl messageDAOImpl){
        this.messageDAOImpl = messageDAOImpl;
    }

    @Override
    public Message insertMessage (Message message){
        String meString = message.getMessage_text();
        int account_id = message.getPosted_by();
        
        //guard statements
        if(meString == "" || meString.length() > 255){
            return null;
        }
        if(messageDAOImpl.postedbyExistUser(account_id)){
            Message newMessage = messageDAOImpl.insertMessage(message);
            return newMessage;
        }

        return null;
    }

    /*
     *Use MessageDAOImpl to retrieve all messages 
     *@return all message
     */
    @Override
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        messages = messageDAOImpl.getAllMessages();
        return messages;
    }

    /*
     * use MessageDAO to retrieve all messsage by a particular user
     */
    @Override
     public List<Message> getMessageByUser(int posted_by){

        List<Message> messages = new ArrayList<>();
        messages = messageDAOImpl.getMessageByUser(posted_by);
        return messages;
     }


    /* 
    Use MessageDAOImpl to retrieve a message by messge Id
     */
    @Override
    public Message getMessageById(int message_id){
        Message message = messageDAOImpl.getMessageById(message_id);
    
        if (message != null){
            return message;
        }else {
            return null;
        }
    }

    /*
     * method to delete a message by id
     */
    @Override
    public void deleteMessageById (int messageId){
         messageDAOImpl.deleteMessageById(messageId);
    }
    /* As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}.
     * The request body should contain a new message_text values to replace the message identified by message_id.
     * The request body can not be guaranteed to contain any other information.
     * The update of message should be sucess if and only if the message Id already exist and the new message text is not blank
     * and is not over 255 characters.if the update is sucessful, the response body should contain the full updated message,
     * (including message_id, posted_by, message_text, and time_posted_epoch) and the response status should be 200, which is the default.
     * The message existing on the database should have the updated message_text.
     */

    @Override
     public Message updateMessageById (Message message){
        
        //get message by message Id
        Message newMessage = messageDAOImpl.getMessageById(message.getMessage_id());
        
        //check if message id exist
        if (!messageDAOImpl.messageIdExist(message.getMessage_id())){
            return null;
        }

        //set new message text
        newMessage.setMessage_text(message.getMessage_text());

        //check if message text is blank   
        if (newMessage.getMessage_text() == null || newMessage.getMessage_text() == ""){
            return null;
        }
        //check if message text > 255 characters
        if (newMessage.getMessage_text().length() > 255){
            return null;
        }

        //update existing message on the db
        messageDAOImpl.updateMessageById(newMessage);
        
        return newMessage;
     }
    

}

