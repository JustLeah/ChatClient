import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class Client implements Runnable {

    //Declare some variables that are used later
    private BufferedReader br1, br2;
    private PrintWriter pr1;
    private Socket socket;
    private Thread outgoingThread, incomingThread;
    private String in = "", out = "", username, connected;
    private int portToUse;
    private ChatUserInterface chatUI;


    public Client(String ipToUse, int portToUse, String username, ChatUserInterface chatUI) {
        this.username = username;
        this.chatUI = chatUI;
        try {
            outgoingThread = new Thread(this);
            incomingThread = new Thread(this);
            socket = new Socket(ipToUse, portToUse);
            outgoingThread.start();;
            incomingThread.start();
        }catch(Exception e){}
    }

    public void run() {
        try {
            if (Thread.currentThread() == outgoingThread) {
                do {
                    br1 = new BufferedReader(new InputStreamReader(System.in));
                    pr1 = new PrintWriter(socket.getOutputStream(), true);
                    in = br1.readLine();
                    pr1.println(username + " says: " + in);
                } while (!in.contains("/END/"));
            } else {
                do {
                    br2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = br2.readLine();
                    chatUI.addMessageToChat(out);
                } while (!out.contains("/END/"));
            }
        } catch(Exception e){}
    }

    public void sendMessage(String messageToSend){
        try{
        PrintWriter pr1 = new PrintWriter(socket.getOutputStream(), true);
        pr1.println(messageToSend);
        }catch(Exception e){}
    }

    public String getUserName(){
        return username;
    }

    public boolean connected(){
        return socket.isConnected();
    }
}