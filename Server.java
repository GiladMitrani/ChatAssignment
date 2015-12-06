package ChatAssignment;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {
    
    /* Publics: */
    public static final int    SERVER_PORT = 45000; 
    
    /* Privates: */
    private static final long   SERVER_SLEEP = 200; 
    private ServerSocket        serverSocket = null;    
    private Socket              clientSocket = null;
    private boolean             isStopped = false;              
    private ArrayList<Client>   clients = new ArrayList<>();

    /* Constructor: */
    public Server() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Cannot listen on this port.\n" + e.getMessage());
            System.exit(-1);
        }
        /* Server Socket Create Successfully */
        System.out.println("Socket " + serverSocket + " created succefully");
        // TODO: add write to server Text area!
    }
    
   
    /* Methods: */
    @Override
    public void run() {
        /* Chat Room Created */
        System.out.println("Room has been created!");
        // TODO: add write to server Text area!

        /* Main Listening Loop */
        while (!isStopped()) {
            // TODO: Remove disconnected clients;
            
            try {
                clientSocket = serverSocket.accept(); // Listens and connects a new client socket
            } catch (IOException e) { // accept() failed.
                if (isStopped()) {
                    System.out.println("Server Stopped!");
                    return;
                }
                throw new RuntimeException(
                        "Could not accept client!", e);    
            }
            
            /* Client Successfully Connected */
            System.out.println("Client "+clientSocket+"has connected");
            // TODO: add write to server text area!
            
            /* Add New Client to List */
            clients.add(new Client(clientSocket));
            
            /* Server Sleep */
            try {
                Thread.sleep(SERVER_SLEEP);
            } catch (InterruptedException ex) {
                System.out.println("Sleep interrupted! "+ex.getMessage());
            }
            
            /* Server Code */
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

        System.out.println("Server Stopped.");

    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

}
