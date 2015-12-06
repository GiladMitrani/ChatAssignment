package ChatAssignment;

import java.io.*;
import java.net.*;

public class Client {
    
    /* Privates: */        
    private static final long   CLIENT_SLEEP = 200;
    private Socket              socket;      
    private boolean             connected;
    private ClientThread        clientThread;
    
    // NESTED CLASS START;
    public class ClientThread extends Thread {
        
        /* Privates: */
        private ObjectInputStream in;
        private ObjectOutputStream out;
        
        /* Methods: */
        @Override
        public void run() {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream()); //
            } catch (IOException e) {
                System.err.println("Failed getting stream from "+this);
                return;
            }
            
            /* Stream Successfully Connected */
            System.out.println(this+" has succesfully connected input and output");
            // TODO: Implement communication to server TextArea;
                     
            /* Client Sleep */
            try {
                Thread.sleep(CLIENT_SLEEP);
            } catch (InterruptedException ex) {
                System.err.println(this+" has input interruption");
            } 
            
            /* Client Code */ 
            // TODO: Implement send message
            // TODO: Implement recieve TextArea
            // TODO: Implement leave server
            
            // PlaceHolder:
            PrintWriter outGoing = new PrintWriter(out, true);
            BufferedReader inGoing = new BufferedReader(new InputStreamReader(in));
            
            
        }
    } // NESTED CLASS END;
    
    /* Constructor: */
    public Client(Socket newSocket) {
        socket=newSocket;
        connected=true;
        clientThread = new ClientThread();
        clientThread.start();
    }
    
    /* Methods: */
    public boolean isConnected() {
        return connected;
    }
    
    public void removeClient() {
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
        try {
            Client client = new Client(new Socket("localHost",45000));
        } catch (IOException e) {
            
        }
    }
}
