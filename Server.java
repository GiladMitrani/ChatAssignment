package ChatAssignment;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {

    /* Privates: */
    private static final int SERVER_PORT = 44000;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean isStopped;
    private ArrayList<ClientThread> clients;
    private ServerGUI sGUI;

    /* Constructor: */
    public Server(ServerGUI sGUI) {
        this.sGUI = sGUI;
        serverSocket = null;
        clientSocket = null;
        isStopped = true;
        clients = new ArrayList<>();
    }

    /* Methods: */
    @Override
    public void run() {
        isStopped = false;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            log("Server Socket Created!");
            /* Main Connection Loop */
            while (!isStopped()) {
                log("Server Waiting for Clients on Port: " + serverSocket.getLocalPort());
                clientSocket = serverSocket.accept();
                System.out.println("Socket accepted");
                /* Client Successfully Connected */
                log("Client Socket established on Port: " + clientSocket.getPort());
                if (isStopped) { // serverStop();
                    log("Server Stopped");
                    break;
                }
                /* Create new ClientThread */
                ClientThread client = new ClientThread(clientSocket);
                /* Send OnlineList to Client*/
                client.start();
                for (ClientThread currentClient : clients) {
                    client.transmit(MessageProtocol.add(currentClient.userName));
                }
                clients.add(client);
                /* Update Client in OnlineList */
                for (ClientThread currentClient : clients) {
                    currentClient.transmit(MessageProtocol.add(client.userName));
                }

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
                log("Closed all Clients");
            } catch (IOException e) {
                System.err.println("Failed Closing Server Socket!");
            }
        } catch (IOException e) {
            System.err.println("Failed Initializing Server Socket!");
        }
    }

    void serverStop() { // Called from GUI
        /* Break Loop */
        isStopped = true;
        /* Connect to Self */
        try {
            new Socket("localHost", SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Failed Connecting to Self");
        }
    }

    public static int getPort() {
        return SERVER_PORT;
    }

    private void log(String msg) {
        sGUI.append(msg);
        System.out.println(msg);
    }

    private synchronized void relay(MessageProtocol msg) {
        // TODO: Complete MessageProtocol Class
    }

    private synchronized void broadcast(MessageProtocol msg) {
        // TODO: Complete MessageProtocol Class
    }

    private synchronized void removeClient(ClientThread client) {
        for (ClientThread currentClient : clients) { // Loop remove and transmit
            if (currentClient.userName.equals(client.userName)) clients.remove(currentClient);
            else currentClient.transmit(MessageProtocol.remove(client.userName));
        }
        
        /* Place Holder */
        /*for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).userName.equals(client.userName)) {
                clients.remove(i--);
            } else {
                clients.get(i).transmit(MessageProtocol.remove(client.userName));
            }
        }*/
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
        boolean running;

        /* Constructor: */
        ClientThread(Socket newSocket) {
            this.socket = newSocket;
            /* Create Object Streams */
            log("Initializing Input/Output Streams on Port: " + socket.getPort());
            try {
                outGoing = new ObjectOutputStream(socket.getOutputStream());
                inGoing = new ObjectInputStream(socket.getInputStream());
                userName = (String) inGoing.readObject();
                log(userName + " Has Connected");
                running = true;
            } catch (IOException e) {
                System.err.println("Failed Creating Input/Output Streams!");
            } catch (ClassNotFoundException e) {
                System.err.println("Failed Recieving Username!");
            }
        }

        /* Methods: */
        @Override
        public void run() {
            while (running) {
                try {
                    msg = (MessageProtocol) inGoing.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Failed Recieving Message From Client on Port: " + socket.getPort());
                    break;
                }
                /* Interpet MessageProtocol */
                switch (msg.getType()) {
                    case MessageProtocol.LOGOFF:
                        running = false;
                        break;
                    case MessageProtocol.BROADCAST:
                        // TODO: Broadcast
                        break;
                    case MessageProtocol.RELAY:
                        // TODO: Relay
                        break;
                    default:
                        System.err.println("Message Type Unknown!");
                        break;
                }
            }
            /* Logged Off */
            removeClient(this); // Remove from List
            try {               // Close all Streams
                outGoing.close();
                inGoing.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed Closing " + userName);
            }
        }

        private void transmit(MessageProtocol msg) {
            try {
                outGoing.writeObject(msg);
            } catch (IOException e) {
                System.err.println("Failed Sending String to " + userName);
            }
        }
    } // NESTED CLASS END;
}
