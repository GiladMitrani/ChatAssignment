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
    private ClientGUI           cGUI;
    
    /* Constructor: */
    public Client(String userName, ClientGUI cGUI) {
        this.cGUI = cGUI;
        this.userName = userName;
    }
    
    /* Methods: */
    public void run() {
        try {
            socket = new Socket("localhost",SERVER_PORT);
        } catch (IOException e) {
            warning("Failed Connection to server!");
        }
        /* Connection Accepted*/
        log("Connection accepted");
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
     
    void log(String msg) {
        System.out.println(msg);
        cGUI.append(msg);
    }
    
    void warning(String msg) {
        System.err.println(msg);
        cGUI.append(msg);
    }
    
    void removeClient() {
        try {
            /* Log Off Message */
            outGoing.writeObject(new MessageProtocol(0,null,null,null));
            /* Close Connections*/ 
            outGoing.close();
            inGoing.close();
            socket.close();
        } catch (IOException e) {
            warning("Failed to Remove "+this);
        }
    }
    
    void transmit() {
        try {
            outGoing.writeObject(cGUI.messageFormat());
        } catch (Exception e) {
            warning("Failed Sending Message");
        }
    }
    
    @Override
    public String toString() {
        return socket.toString();
    }
    
    // NESTED CLASS START;
    class ServerThread extends Thread {
        
        // TODO: Never stops.
        
        /* Methods: */
        public void run() {
            while(true) {
                try {
                    /* Read Transmit */
                    MessageProtocol msg = (MessageProtocol) inGoing.readObject();
                    System.out.println("Recieved Object!");
                    /* Interpet Transmission */
                    switch (msg.getType()) {
                        case MessageProtocol.ADD:
                            cGUI.addToList(msg.getData());
                            System.out.println("Added "+msg.getData()+" to List");
                            break;
                        case MessageProtocol.REMOVE:
                            cGUI.removeFromList(msg.getData());
                            System.out.println("Removed "+msg.getData()+" from List");
                            break;
                        default:
                            cGUI.append(msg.toString());
                            break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    warning("Failed Recieving Transmit from Server");
                    return;
                }
            }
        }
    }
}
