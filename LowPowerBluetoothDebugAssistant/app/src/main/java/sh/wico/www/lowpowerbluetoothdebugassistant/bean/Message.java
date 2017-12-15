package sh.wico.www.lowpowerbluetoothdebugassistant.bean;

/**
 * Created by HYW on 2017/12/14.
 */

public class Message {

    public enum MessageType {
        SEND, RECEIVED
    }

    private String content;
    private MessageType messageType;

    public Message(String content, MessageType messageType) {
        this.content = content;
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

}
