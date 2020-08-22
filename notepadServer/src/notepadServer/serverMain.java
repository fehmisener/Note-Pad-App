package notepadServer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class serverMain extends JFrame {

	private JPanel contentPane;
	
	
	
	static private Socket socket;
	static private ServerSocket serversocket;
	static private DataInputStream din;
	static private DataOutputStream dout;
	static boolean serverStat=true;
	
	
	static private  List<serverConn> connList  = new ArrayList<serverConn>();
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					serverMain frame = new serverMain();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}		
		});
		try
		{
			serversocket = new ServerSocket(6064);
			while(serverStat)
			{
				socket=serversocket.accept();
				serverConn sc = new serverConn(socket);
				sc.start();
				connList.add(sc);	
			}
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	public serverMain()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
}
