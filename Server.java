package ChatAssignment;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server {

    /* Privates: */
    private static final int            SERVER_PORT = 45000;
    private ServerSocket                serverSocket;
    private Socket                      clientSocket;
    private boolean                     isStopped;
    private ArrayList<ClientThread>     clients;
    private ServerGUI                   sGUI;

    /* Constructor: */
    public Server(ServerGUI sGUI) {
        this.sGUI = sGUI;
        serverSocket = null;
        clientSocket = null;
        isStopped = true;
        clients = new ArrayList<>();
    }

    /* Methods: */
    public void start() {
        isStopped = false;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            display("Server Socket Created!");
            /* Main Connection Loop */
            while (!isStopped()) {
                // TODO: Remove disconnected clients;
                display("Server Waiting for Clients on Port: " + serverSocket.getLocalPort());
                clientSocket = serverSocket.accept();
                System.out.println("Socket accepted");
                /* Client Successfully Connected */
                display("Client Socket established on Port: " + clientSocket.getPort());
                if (isStopped) {
                    break;
                }
                ClientThread client = new ClientThread(clientSocket);
                clients.add(client);
                client.start();
            }
            /* Server Stopped */
            try {
                serverSocket.close();
                /* Close All Clients */
                for (int i = 0; i < clients.size(); i++) {
                    ClientThread client = clients.get(i);
                    try {
                        client.inGoing.close();
                        client.outGoing.close();
                        client.socket.close();
                    } catch (IOException e) {
                        System.err.println("Failed Closing Client Socket!");
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed Closing Server Socket!");
            }
        } catch (IOException e) {
            System.err.println("Failed Initializing Server Socket!");
        }
    }

    // TODO: BroadCast() send to all;
    // TODO: Send() sends PM
    void serverStop() {
        isStopped = true;
        // TODO: connect to myself ?
    }

    public static int getPort() {
        return SERVER_PORT;
    }

    private void display(String msg) {
        sGUI.append(msg);
        System.out.println(msg);
    }

    private synchronized void message(MessageProtocol msg) {
        // TODO: Complete MessageProtocol Class
    }

    private synchronized void broadcast(MessageProtocol msg) {
        // TODO: Complete MessageProtocol Class
    }
    
    private synchronized void remove(String userName) {
        // TODO: Check need.
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    // NESTED CLASS START;
    public class ClientThread extends Thread {

        /* Privates: */
        Socket socket;
        ObjectInputStream inGoing;
        ObjectOutputStream outGoing;
        String userName;
        MessageProtocol msg;

        /* Constructor: */
        ClientThread(Socket newSocket) {
            this.socket=newSocket;
            /* Create Object Streams */
            display("Initializing Input/Output Streams on Port: "+socket.getPort());
            try {
                outGoing = new ObjectOutputStream(socket.getOutputStream());
                inGoing = new ObjectInputStream(socket.getInputStream());
                userName = (String) inGoing.readObject();
            } catch (IOException e) {
                System.err.println("Failed Creating Input/Output Streams!"); 
            } catch (ClassNotFoundException e) {
                System.err.println("Failed Recieving Username!");
            }
        }
        
        /* Methods: */
        public void run() {
            boolean running = true;
            while (running) {
                try {
                    msg = (MessageProtocol) inGoing.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Failed Recieving Message From Client on Port: "+socket.getPort());
                    return;
                }
                // TODO: switch/ifelse (msg)?
            }
            remove(userName);
            try {
                outGoing.close();
                inGoing.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed Closing "+userName);
            }
        }
        
        private void sendString(String msg) {
            try {
                outGoing.writeObject(msg);
            } catch (IOException e) {
                System.err.println("Failed Sending String to "+userName);
            }
        }
    } // NESTED CLASS END;
}
