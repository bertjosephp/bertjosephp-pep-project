package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    public AccountDAO accountDAO;
    public MessageDAO messageDAO;

    public MessageService() {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }

    public MessageService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public MessageService(AccountDAO accountDAO, MessageDAO messageDAO) {
        this.accountDAO = accountDAO;
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message) {
        String message_text = message.getMessage_text();
        boolean isMessageTextValid = message_text != null && !message_text.isEmpty() && message_text.length() <= 255;
        boolean isPosterAccountExisting = accountDAO.getAccountById(message.getPosted_by()) != null;

        if (isMessageTextValid && isPosterAccountExisting) {
            return messageDAO.insertMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }

    public Message deleteMessageById(int message_id) {
        Message message = messageDAO.getMessageById(message_id);
        messageDAO.deleteMessageById(message_id);
        return message;
    }

    public Message updateMessageById(String message_text, int message_id) {
        boolean isMessageTextValid = message_text != null && !message_text.isEmpty() && message_text.length() <= 255;
        boolean isMessageExisting = messageDAO.getMessageById(message_id) != null;
        
        if (isMessageTextValid && isMessageExisting) {
            messageDAO.updateMessageById(message_text, message_id);
            return messageDAO.getMessageById(message_id);
        }

        return null;
    }

    public List<Message> getAllMessagesByAccountId(int account_id) {
        return messageDAO.getAllMessagesByAccountId(account_id);
    }
}
