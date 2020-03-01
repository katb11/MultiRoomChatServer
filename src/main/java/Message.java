
// Class model for a message
public class Message {
    private MessageType messageType;
    private String message;

    public enum MessageType {
        LOGIN,
        MESSAGE
    }

    public Message() {}

    public Message(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
