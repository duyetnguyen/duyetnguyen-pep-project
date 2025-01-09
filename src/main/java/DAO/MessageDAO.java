package DAO;

import Model.Message;

import java.util.*;


public interface MessageDAO {

    public List<Message> getAllMessages();
    public Message getMessageById (int id);
    public void deleteMessageById (int id);
    public Message insertMessage (Message message);
    public boolean postedbyExistUser (int account_id);
    public boolean messageIdExist (int msgId);
    public Message updateMessageById (Message message);
    public List<Message> getMessageByUser(int posted_by);
    



}
