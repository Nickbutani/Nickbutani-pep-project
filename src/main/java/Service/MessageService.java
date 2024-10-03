package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private final AccountDAO accountDAO;
    private final MessageDAO messageDAO;

    public MessageService() {
        this.accountDAO = new AccountDAO();
        this.messageDAO = new MessageDAO();
    }

    public MessageService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.accountDAO = new AccountDAO();
        this.messageDAO = messageDAO;
    }

    public MessageService(AccountDAO accountDAO, MessageDAO messageDAO) {
        this.accountDAO = accountDAO;
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message newMessage) {
        if (newMessage.getMessage_text().isBlank() || 
            newMessage.getMessage_text().length() >= 255 || 
            this.accountDAO.searchAccountByID(newMessage.getPosted_by()) == null) {
            return null;
        }
        return this.messageDAO.createNewMessage(newMessage);
    }

    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    public Message getMessageByMessageId(int id) {
        return this.messageDAO.getMessageByMessageId(id);
    }

    public Message deleteMessage(int id) {
        return this.messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, String message) {
        if (message.isBlank() || message.length() >= 255 || this.messageDAO.getMessageByMessageId(id) == null) {
            return null;
        }
        return this.messageDAO.updateMessage(id, message);
    }

    public List<Message> getAllMessagesByAccountId(int account_id) {
        return this.messageDAO.getAllMessagesByAccountId(account_id);
    }
}
