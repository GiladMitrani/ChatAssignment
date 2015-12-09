/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatAssignment;

import java.io.Serializable;

/**
 *
 * @author YaronK
 */
public class MessageProtocol implements Serializable {

    /* Finals: */
    public static final String LOGOFF = "-LOGOFF";
    public static final String BROADCAST = "BROADCAST";
    public static final String RELAY = "RELAY";
    public static final String ADD = "+ADD";
    public static final String REMOVE = "-REMOVE";
    
    /* Privates: */
    private String type;
    private String data;
    private String dest;
    private String sent;

    /* Constructor */
     public MessageProtocol(int type, String data, String dest, String sent) {
        switch (type) {
            case 0:
                this.type = LOGOFF;
                break;
            case 1:
                this.type = BROADCAST;
                break;
            case 2: 
                this.type = RELAY;
                break;
            case 3:
                this.type = ADD;
                break;
            case 4: 
                this.type = REMOVE;
                break;
            default:
                this.type = "Banana";
        }
        this.data = data;
        this.dest = dest;
        this.sent = sent;
    }
    
    /* Getters: */ 
    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public String getDestination() {
        return dest;
    }

    public String getTransmittor() {
        return sent;
    }
    
    public String toString() {
        return String.format("%s: %s", sent, data);
    }
    
    /* Automated Messages */
    public static MessageProtocol add(String userName) {
        return new MessageProtocol(3,userName,null,null);
    }
    
    public static MessageProtocol remove(String userName) {
        return new MessageProtocol(4,userName,null,null);
    }
    
    public static MessageProtocol name(String userName) {
        return new MessageProtocol(5,userName,null,null);
    }
 }
