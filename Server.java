package ChatAssignment;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server extends Thread {
    
    /* Privates: */
    private static final int    SERVER_PORT = 45000; 
    private static final long   SERVER_SLEEP = 200; 
    private ServerSocket        serverSocket = null;    
    private Socket              clientSocket = null;
    private boolean             isStopped = false;              
    private ArrayList<Client>   clients = new ArrayList<>();
    private ServerGUI           sGUI;

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
    @Override
    public void run() {
        /* Chat Room Created */
        display("Chat Room has been created!");
        // TODO: add write to server Text area!

        /* Main Listening Loop */
        while (!isStopped()) {
            // TODO: Remove disconnected clients;
            
            try {
                clientSocket = serverSocket.accept(); // Listens and connects a new client socket
            } catch (IOException e) { // accept() failed.
                if (isStopped()) {
                    display("Server Stopped");
                    return;
                }
                throw new RuntimeException("Could not accept client!", e);    
            }
            
            /* Server Recieve Client Data */
            display("Getting Client Data:");
            Client client = getClientData(clientSocket);
             
            /* Client Successfully Connected */
            display("Client "+clientSocket+"has connected");
            
            /* Add New Client to List */
            clients.add(client);
            
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

}
