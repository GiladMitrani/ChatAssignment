package ChatAssignment;

import java.io.*;
import java.net.*;

public class Client implements Serializable {
    
    /* Serial: */
    protected static final long SerialVersionUID = 1113377L;
    
    /* Privates: */        
    private static final long   CLIENT_SLEEP = 200;
            Socket              socket;      
    private boolean             connected;
            ClientThread        clientThread;
    private ClientGUI           cGUI;
    
    // NESTED CLASS START;
    public class ClientThread extends Thread {
        
        /* Privates: */
        ObjectInputStream in;
        ObjectOutputStream out;
        
        /* Methods: */
        @Override
        public void run() {
            display("Setting up input and output");
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                display("Successfully Connected Output");
//                in  = new ObjectInputStream(socket.getInputStream());
//                display("Successfully Connected Input");
            } catch (IOException e) {
                System.err.println("Failed getting stream from "+this);
                return;
            }
            System.out.println("aaa");
            
            /* Stream Successfully Connected */
            display(this+" has succesfully connected input and output");
            // TODO: Implement communication to server TextArea;
            
            /* Client Send Data to Server */ 
            display("Trying to send Client Data");
            try {
            out.writeObject(Client.this);
            } catch (IOException e) {
                System.err.println("Failed Sending Client Data!");
            }
            display("Client Data Sent Successfully!");
                     
            /* Client Sleep */
            try {
                Thread.sleep(CLIENT_SLEEP);
            } catch (InterruptedException ex) {
                System.err.println(this+" has input interruption");
            } 
        }
    } // NESTED CLASS END;
    
    /* Client Code */ 
            // TODO: Implement send message
            // TODO: Implement recieve TextArea
            // TODO: Implement leave server
            
            // PlaceHolder:
//            PrintWriter outGoing = new PrintWriter(out, true);
//            BufferedReader inGoing = new BufferedReader(new InputStreamReader(in));
    
    /* Constructor: */
       
    public Client(Socket newSocket, ClientGUI cGUI) {
        this.cGUI = cGUI;
        socket=newSocket;
        connected=true;
        clientThread = new ClientThread();
    }
    
    /* Methods: */
    public boolean isConnected() {
        return connected;
    }
    
    public void start() {
        clientThread.start();
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
            this.clientThread.in.close();
            this.clientThread.out.close();
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
