package ChatAssignment;

import java.io.*;
import java.net.*;

public class Client {
    
    /* Privates: */        
    private ObjectInputStream   inGoing;
    private ObjectOutputStream  outGoing;
    private String              userName;
    private Socket              socket;      
    private boolean             connected;
    private ClientGUI           cGUI;
    
    
    
    /* Client Code */ 
            // TODO: Implement send message
            // TODO: Implement recieve TextArea
            // TODO: Implement leave server
     
    /* Constructor: */
       
    public Client(String userName, ClientGUI cGUI) {
        this.cGUI = cGUI;
        this.userName = userName;
        connected=true;
    }
    
    /* Methods: */
    public boolean isConnected() {
        return connected;
    }
    
    public boolean start() {
        try {
            socket = new Socket("localhost",45000);
        } catch (IOException e) {
            System.err.println("Failed Connection to server!");
            return false;
        }
        
        return true;
    }
    
    void display(String msg) {
        System.out.println(msg);
        cGUI.append(msg);
    }
    
    ClientGUI getGUI() {
        return cGUI;
    }
    
    void removeClient() {
        try {
            connected = false;
            socket.close();
        } catch (IOException e) {
            System.err.println("Failed to close "+this);
        }
    }
    
    @Override
    public String toString() {
        return socket.toString();
    }
    
    public static void main(String[] args) {

    }
}
