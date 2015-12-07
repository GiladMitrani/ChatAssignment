package ChatAssignment;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {
    
    /* Privates: */
    private static final int            SERVER_PORT = 45000; 
    private static final long           SERVER_SLEEP = 200; 
    private ServerSocket                serverSocket = null;    
    private Socket                      clientSocket = null;
    private boolean                     isStopped = false;              
    private ArrayList<ClientThread>     clients = new ArrayList<>();
    private ServerGUI                   sGUI;

    /* Constructor: */
    public Server(ServerGUI sGUI) {
        this.sGUI = sGUI;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Cannot listen on this port.\n" + e.getMessage());
            System.exit(-1);
        }
        /* Server Socket Create Successfully */
        display("Socket " + serverSocket + " created succefully");
        // TODO: add write to server Text area!
    }
    
   
    /* Methods: */
    public void start() {
        /* Chat Room Created */
        display("Chat Room has been created!");
        // TODO: add write to server Text area!

        /* Main Listening Loop */
        while (!isStopped()) {
            // TODO: Remove disconnected clients;
            
            try {
                clientSocket = serverSocket.accept(); // Listens and connects a new client socket
                display("Client Socket established on Port: "+clientSocket.getPort());
                /* Server Recieve Client Data */
                display("Waiting for Client Data:");
                ObjectInputStream inGoing = new ObjectInputStream(clientSocket.getInputStream());
//              ObjectOutputStream outGoing = new ObjectOutputStream(clientSocket.getOutputStream());
                Client client = (Client)inGoing.readObject();
                inGoing.close();
                /* Client Successfully Connected */
                clients.add(client);
                
            } catch (IOException | ClassNotFoundException e) { // accept() failed.
                if (isStopped()) {
                    display("Server Stopped");
                    return;
                }
                throw new RuntimeException("Could not accept client!", e);    
            }
             
            
            /* Server Sleep */
            try {
                Thread.sleep(SERVER_SLEEP);
            } catch (InterruptedException ex) {
                System.err.println("Sleep interrupted! "+ex.getMessage());
            }
        }
        /* Server Stopped */
        try {
            serverSocket.close();
            for (int i=0; i<clients.size(); i++) {
                Client currentClient = clients.get(i);
                currentClient.removeClient();
            }
        } catch (IOException ex) { 
            System.err.println("Failed stopping server!");
        }

        System.out.println("Server Stopped.");

    }
    
    // TODO: BroadCast() send to all;
    // TODO: Send() sends PM
    
    void stopServer() {
        isStopped=true;
        // TODO: connect to myself?
    } 
    
    public static int getPort() {
        return SERVER_PORT;
    }
    
    private Client getClientData(Socket clientSocket) {
        try {
        ObjectInputStream inGoing = new ObjectInputStream(clientSocket.getInputStream());
        Client client = (Client)inGoing.readObject();
        return client;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed Getting Data!"+e.getMessage());
        }
        return null;
    }
    
    private void placeholder() {
        try {
                PrintWriter outGoing = new PrintWriter(
                        clientSocket.getOutputStream(), true);
                BufferedReader inGoing = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                String msg;
                while ((msg = inGoing.readLine()) != null) {
                    // TODO: Implement writer to server text area;
                    // TODO: Implement send PM;
                    // TODO: Implement send all;
                    // TODO: Implement stop server;
                    
                    /*
                    General Idea:
                    */
                    
                    outGoing.println(msg); // PlaceHolder
                    
                }
                
                outGoing.close();
                inGoing.close();
            } catch (IOException e) {
                System.err.println("Error " + e.getMessage());
            }
    }
    
    private void display(String msg) {
        sGUI.append(msg);
        System.out.println(msg);
    } 
    
    public synchronized void message(MessageProtocol msg) {
        // TODO: Complete MessageProtocol Class
    }
    
    public synchronized void broadcast(MessageProtocol msg) {
        // TODO: Complete MessageProtocol Class
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public static void main(String[] args) {
//        Server server = new Server();
//        server.start();
    }
    
    // NESTED CLASS START;
    public class ClientThread extends Thread {
        
        /* Privates: */
        Socket socket;
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
    
    

}
