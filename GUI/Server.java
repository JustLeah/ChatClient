import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Server implements Runnable{

    private ServerSocket serversocket;
    private BufferedReader br1, br2;
    private PrintWriter pr1;
    private Socket socket;
    private String in="",out="";
    private Socket[] allSockets;
    private int socketCounter = 0; 
    private final int MAX_CONNECTIONS = 20;
    private ConnectionHandler[] maxConnections = new ConnectionHandler[MAX_CONNECTIONS];
    private Socket[] maxSockets = new Socket[MAX_CONNECTIONS];
    private String lastMessage = "";

    public Server(boolean isValue, int... portArg) {
        int portToUse = 1337;
        if(!isValue){
            System.out.println("Please enter the port number you wish to use");
            Scanner user_input = new Scanner(System.in);
            portToUse = Integer.parseInt(user_input.next());
        }
        try {
            serversocket = new ServerSocket(portToUse);
            System.out.println("Server is waiting. . . . ");
            new Thread(this).start();
            
        } catch (Exception e) {
        }
    }

    public boolean notInArray(Socket socket){
        for(int i = 0; i < MAX_CONNECTIONS; i++){
            if(maxSockets[i] == socket)
                return false;
        }
        return true;
    }

    public void allocateSpotOnServer(Socket socket){
        for(int i = 0; i < MAX_CONNECTIONS; i++){
            if(maxConnections[i] == null && notInArray(socket)){
                maxConnections[i] = new ConnectionHandler(socket, this);
                maxSockets[i] = socket;
            }
        }
    }

    public void sendMessage(String message){
        if(lastMessage != message){
            lastMessage = message;
            for(int i = 0; i < MAX_CONNECTIONS; i++){
                try{
                    PrintWriter pr1 = new PrintWriter(maxSockets[i].getOutputStream(), true);
                    pr1.println(message);
                }catch(Exception e){}
            }
        }
    }

    public static void main(String[] args) {
        if(args.length != 0){
            int port = Integer.parseInt(args[0]);
            new Server(true, port);
        }
        else
            new Server(false);
    }

    public void run(){
        try{
            while(true){
                socket = serversocket.accept();
                allocateSpotOnServer(socket);
            }
        }catch(Exception e){}
    }
}

class ConnectionHandler implements Runnable{

    private Socket mySocket;
    private String output ="";
    private BufferedReader bReader1;
    private Server thisServer;

    public ConnectionHandler(Socket clientSocket, Server server){
        mySocket = clientSocket;
        thisServer = server;
        System.out.println("Client connected with Ip " + mySocket.getInetAddress().getHostAddress());
        new Thread(this).start();
    }

    public void run(){
        do{
            try{
                bReader1 = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
                output = bReader1.readLine();
                System.out.println(output);
                thisServer.sendMessage(output);
            }catch(Exception e){}
        } while(!output.contains("/END/"));
    }
}