import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ChatUserInterface extends JFrame
{
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	private CustomTextField ipToConnectLTF, portToConnectTF, userNameTF;
	private JButton connectB, exitB;
	private JWindow mainWindow;
	
	//Button handlers:
	private ExitButtonHandler ebHandler;
	private ConnectHandler connectHandler;
	
	public ChatUserInterface()
	{
		//Initilize the frame
		//Create a JWindow which is attached to the main application
		//So we can get a borderless panel
		mainWindow = new JWindow();
		setTitle("FreddiesChat");
		Container pane = mainWindow.getContentPane();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);

		//Now add this to the homeWindow
		Container homeWindow = getContentPane();
		homeWindow.setLayout(new GridLayout());
		homeWindow.add(pane);

		//IP Address field
		ipToConnectLTF = new CustomTextField(20);
		ipToConnectLTF.setPlaceholder("Ip address");
		setDefaultLayout(c, 1);
		pane.add(ipToConnectLTF, c);

		//Port address field
		portToConnectTF = new CustomTextField(20);
		portToConnectTF.setPlaceholder("Port address");
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
	}

	//Method for a consistent default layout between the components
	public void setDefaultLayout(GridBagConstraints c, int row){
		c.insets = new Insets(20, 20, 0, 20);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = row;
	}
	
	//ExitButtonHandler Class
	public class ExitButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);
		}
	}

	public class ConnectHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			System.out.println("Clicked");
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
