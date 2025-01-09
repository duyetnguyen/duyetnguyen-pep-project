package Service;

import Model.Message;

import java.util.List;

public interface MessageService {

    public Message insertMessage(Message message);
    public List<Message> getAllMessages();
    public Message getMessageById (int message_id);
    public Message updateMessageById (Message message);
    public List<Message> getMessageByUser(int id);
    public void deleteMessageById (int messageId);

    

}
