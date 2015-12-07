package ChatAssignment;

import java.io.*;
import java.net.*;

public class Client extends Thread {
    
    /* Privates: */        
    private int                 SERVER_PORT = 44000;
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
    
    public void run() {
        try {
            socket = new Socket("localhost",SERVER_PORT);
        } catch (IOException e) {
            warning("Failed Connection to server!");
        }
        /* Connection Accepted*/
        display("Connection accepted");
        /* Start Object Streams */ 
        try {
            inGoing = new ObjectInputStream(socket.getInputStream());
            outGoing = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            warning("Failed to Start Object Streams!");
        }
        
        /* Start ServerThread Listener */
        new ServerThread().start();
        try {
            outGoing.writeObject(userName);
        } catch (IOException e) {
            warning("Failed Sending Username!");
        }
    }
    
    void display(String msg) {
        System.out.println(msg);
        cGUI.append(msg);
    }
    
    void warning(String msg) {
        System.err.println(msg);
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
    
    // NESTED CLASS START;
    class ServerThread extends Thread {
        
        public void run() {
            while(true) {
                try {
                    String msg = (String) inGoing.readObject();
                    if (msg.startsWith("+add")) {
                        cGUI.addToList(msg.substring(5,msg.length()));
                        System.out.println("Added "+msg.substring(5,msg.length())+" to List");
                    }
                    else if (msg.startsWith("-remove")) {
                        cGUI.removeFromList(msg.substring(7, msg.length()));
                    }
                    else cGUI.append(msg);
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Failed Recieving Object from Server");
                    return;
                }
            }
        }
    }
}
