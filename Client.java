package ChatAssignment;

import java.io.*;
import java.net.*;

public class Client extends Thread {
<<<<<<< HEAD

    /* Privates: */
    private int SERVER_PORT = 44000;
    private ObjectInputStream inGoing;
    private ObjectOutputStream outGoing;
    private String userName;
    private Socket socket;
    private boolean connected;
    private ClientGUI cGUI;

    /* Client Code */
            // TODO: Implement send message
    // TODO: Implement recieve TextArea
    // TODO: Implement leave server
=======
    
    /* Privates: */        
    private int                 SERVER_PORT = 44000;
    private ObjectInputStream   inGoing;
    private ObjectOutputStream  outGoing;
    private String              userName;
    private Socket              socket;      
    private ClientGUI           cGUI;
    
>>>>>>> origin/master
    /* Constructor: */
    public Client(String userName, ClientGUI cGUI) {
        this.cGUI = cGUI;
        this.userName = userName;
<<<<<<< HEAD
        connected = true;
=======
>>>>>>> origin/master
    }

    /* Methods: */
<<<<<<< HEAD
    public boolean isConnected() {
        return connected;
    }

=======
>>>>>>> origin/master
    public void run() {
        try {
            socket = new Socket("localhost", SERVER_PORT);
        } catch (IOException e) {
            warning("Failed Connection to server!");
        }
        /* Connection Accepted*/
<<<<<<< HEAD
        display("Connection accepted");
        /* Start Object Streams */
=======
        log("Connection accepted");
        /* Start Object Streams */ 
>>>>>>> origin/master
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
<<<<<<< HEAD

    void display(String msg) {
=======
     
    void log(String msg) {
>>>>>>> origin/master
        System.out.println(msg);
        cGUI.append(msg);
    }

    void warning(String msg) {
        System.err.println(msg);
        cGUI.append(msg);
    }
<<<<<<< HEAD

    ClientGUI getGUI() {
        return cGUI;
    }

=======
    
>>>>>>> origin/master
    void removeClient() {
        try {
            /* Log Off Message */
            outGoing.writeObject(new MessageProtocol(0,null,null,null));
            /* Close Connections*/ 
            outGoing.close();
            inGoing.close();
            socket.close();
        } catch (IOException e) {
<<<<<<< HEAD
            System.err.println("Failed to close " + this);
=======
            warning("Failed to Remove "+this);
        }
    }
    
    void transmit() {
        try {
            outGoing.writeObject(cGUI.messageFormat());
        } catch (Exception e) {
            warning("Failed Sending Message");
>>>>>>> origin/master
        }
    }

    @Override
    public String toString() {
        return socket.toString();
    }
<<<<<<< HEAD

    public static void main(String[] args) {

    }

    // NESTED CLASS START;
    class ServerThread extends Thread {

=======
    
    // NESTED CLASS START;
    class ServerThread extends Thread {
        
        // TODO: Never stops.
        
        /* Methods: */
>>>>>>> origin/master
        public void run() {
            while (true) {
                try {
<<<<<<< HEAD
                    String msg = (String) inGoing.readObject();
                    if (msg.startsWith("+add")) {
                        cGUI.addToList(msg.substring(5, msg.length()));
                        System.out.println("Added " + msg.substring(5, msg.length()) + " to List");
                    } else if (msg.startsWith("-remove")) {
                        cGUI.removeFromList(msg.substring(7, msg.length()));
                    } else {
                        cGUI.append(msg);
=======
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
>>>>>>> origin/master
                    }
                } catch (IOException | ClassNotFoundException e) {
                    warning("Failed Recieving Transmit from Server");
                    return;
                }
            }
        }
    }

    public MessageProtocol MessageConvert() {
        MessageProtocol msg = new MessageProtocol();
        msg.sender= cGUI.getTextName().getText();
        if (cGUI.getOnlineList().getSelectedIndex() != 1) {
            msg.destenation = cGUI.getOnlineList().getSelectedValue();
        } else {
            msg.destenation = null;
        }
        cGUI.getTextUserInput().setText("");
        return msg;
    }

    synchronized void sendMessage() {

        try {
            outGoing.writeObject(MessageConvert());
        } catch (Exception e) {
            System.err.println("");
        }

    }
}
