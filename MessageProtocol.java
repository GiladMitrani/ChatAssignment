/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatAssignment;

/**
 *
 * @author YaronK
 */
public class MessageProtocol {
    String message;
    String destenation;
    String sender;

    public MessageProtocol() {
        
    }

    public MessageProtocol(String message, String destenation, String sender) {
        this.message = message;
        this.destenation = destenation;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDestenation() {
        return destenation;
    }

    public void setDestenation(String destenation) {
        this.destenation = destenation;
    }

    public String getSender() {
        return sender;
    }
 
}
