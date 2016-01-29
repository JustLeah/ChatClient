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
    private String in = "", out = "";
    private String username;
    private int portToUse;
    private String ipToUse;

    public Client() {

        Scanner user_input = new Scanner(System.in);

        //Get the IP address you want to connect to
        System.out.println("Please enter the IP address to connect to");
        ipToUse = user_input.next();

        //Get the Port number you want to conenct to
        System.out.println("Please enter the port address to connect to");
        portToUse = Integer.parseInt(user_input.next());

        //Let the user set their username
        System.out.println("Please enter your username");
        username = user_input.next();

        try {
            outgoingThread = new Thread(this);
            incomingThread = new Thread(this);
            socket = new Socket(ipToUse, portToUse);
            outgoingThread.start();;
            incomingThread.start();

        } catch (Exception e) {
        }
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
                    br2 = new BufferedReader(new   InputStreamReader(socket.getInputStream()));
                    out = br2.readLine();
                    System.out.println(out);
                } while (!out.contains("/END/"));
            }
        } catch (Exception e) {
        }

    }

    public static void main(String[] args) {
        new Client();
    }
}