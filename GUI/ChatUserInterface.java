import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ChatUserInterface extends JFrame
{
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	private CustomTextField ipToConnectLTF, portToConnectTF, userNameTF;
	private JButton connectB, exitB, sendMessageButton;
	private JWindow mainWindow;
	private Client chatConnection;
	private Container pane, homeWindow, chat, chatPanel;
	//Button handlers:
	private ExitButtonHandler ebHandler;
	private ConnectHandler connectHandler;
	private String connectedUsername;
	private boolean connectionEstablished = false;
	private JTextArea display, messageToSendServer;
	JScrollPane chatScroll;

	private ChatUserInterface chatUI;

	public ChatUserInterface()
	{
		this.chatUI = this;
		//Initilize the frame
		//Create a JWindow which is attached to the main application
		//So we can get a borderless panel
		mainWindow = new JWindow();
		setTitle("FreddiesChat");
		pane = mainWindow.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);

		//Now add this to the homeWindow
		homeWindow = getContentPane();
		homeWindow.setLayout(new GridLayout());
		homeWindow.add(pane);

		//IP Address field
		ipToConnectLTF = new CustomTextField(20);
		ipToConnectLTF.setPlaceholder("82.10.49.98");
		setDefaultLayout(c, 1);
		pane.add(ipToConnectLTF, c);

		//Port address field
		portToConnectTF = new CustomTextField(20);
		portToConnectTF.setPlaceholder("1337");
		setDefaultLayout(c, 2);
		pane.add(portToConnectTF, c);

		//Username field
		userNameTF = new CustomTextField(20);
		userNameTF.setPlaceholder("Username");
		setDefaultLayout(c, 3);
		pane.add(userNameTF, c);
		
		//Connect button
		connectB = new JButton("Connect");
		connectHandler = new ConnectHandler();
		connectB.addActionListener(connectHandler);
		setDefaultLayout(c, 4);
		pane.add(connectB, c);

		//Exit button
		exitB = new JButton("Exit");
		setDefaultLayout(c, 5);
		ebHandler = new ExitButtonHandler();
		exitB.addActionListener(ebHandler);	
		pane.add(exitB, c);

		//Override the defualt close button to disconnect cleanly and inform the server
		WindowListener exitListener = new WindowAdapter() {

	    @Override
	    public void windowClosing(WindowEvent e) {
	 		if(connectionEstablished)
	 			chatConnection.sendMessage(connectedUsername + " Disconnected");
	        System.exit(0);
	    }};

	    //Make the program listen for this event
		addWindowListener(exitListener);

	}


	//Method for a consistent default layout between the components
	public void setDefaultLayout(GridBagConstraints c, int row){
		c.insets = new Insets(20, 20, 0, 20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = row;
	}

	public void showChatScreen(){
		//Set up the main window for this stage
		homeWindow.setLayout(new GridLayout(1,1));
		JWindow chatWindow = new JWindow();
		setTitle("FreddiesChat - connected as " + connectedUsername);
		setSize(700, 500);

		//Setup the actual chat window where messages are read
		chat = chatWindow.getContentPane();
		chat.setLayout(new GridLayout(1, 1));
		setVisible(true);

		//Set up the LHS with the send button and message to send
		chatPanel = new Container();
		chatPanel.setLayout(new GridLayout(0,1));
		messageToSendServer = new JTextArea();
		chatPanel.add(messageToSendServer);
		sendMessageButton = new JButton("Send message");
		
		SendMessageHandler smHandler = new SendMessageHandler();
		sendMessageButton.addActionListener(smHandler);	
		chatPanel.add(sendMessageButton);
		chat.add(chatPanel);

		//Add the text area on the RHS
		display = new JTextArea();
    	display.setEditable(false);
		chatScroll = new JScrollPane(display);
		chatScroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		chatScroll.setHorizontalScrollBarPolicy ( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		chat.add(chatScroll);

		//Add it all to the homeWindow
		homeWindow.add(chat);
	}
	
	public void addMessageToChat(String message){
		display.append(message + "\n");
	}

	public class SendMessageHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			if(connectionEstablished && !messageToSendServer.getText().equals(null)){
	 			chatConnection.sendMessage(connectedUsername + " says: " + messageToSendServer.getText());
	 			messageToSendServer.setText(null);
			}
		}
	}

	//ExitButtonHandler Class
	public class ExitButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			if(connectionEstablished)
	 			chatConnection.sendMessage(connectedUsername + " Disconnected");
	        System.exit(0);
		}
	}


	public class ConnectHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){

			String ipAddress = ipToConnectLTF.getText();
			int portAddress = Integer.parseInt(portToConnectTF.getText());
			String username = userNameTF.getText();
			try{
				chatConnection = new Client(ipAddress, portAddress, username, chatUI);
				connectedUsername = chatConnection.getUserName();
				if(chatConnection.connected()){
					homeWindow.removeAll();
					connectionEstablished = true;
					showChatScreen();
				}
			}catch(Exception ex){
				System.out.println("Invalid details or server offline");
			}
		}
	}
	
	//Main method to run the client
	public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChatUserInterface rectObj = new ChatUserInterface();
            }
        });
    }
}
