package notepadClient;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Cursor;
import javax.swing.JTextArea;

import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class clientMain extends JFrame
{

	private JPanel contentPane;
	private JTextField txtTitle;
	private static JTextArea txtContent;
	private static JList <String> list;
	
	
	static private Socket socket;
	static private DataInputStream din;
	static private DataOutputStream dout;
	
	static private String filesArr[];
	static private DefaultListModel<String> defaultList;
	static private String content;

	public static void connect()
	{
		try
		{
			socket = new Socket("localhost",6064);
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			defaultList = new DefaultListModel<String>();
			
			dout.writeUTF("getFiles");
			dout.flush();
			
			String text = "";
			while(!text.equals("ext"))
			{
				text=din.readUTF();
			
				if(text.startsWith("files"))
				{
					text=text.substring(5, text.length());
					filesArr=text.split(":");
					for (String str :filesArr)					
						defaultList.addElement(str);
													
				}
				else if(text.startsWith("content"))
				{
					content=text.substring(7,text.length());
					txtContent.setText("");
					txtContent.setText(content);
				}
			}		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					clientMain frame = new clientMain();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		connect();
	}

	
	public clientMain() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		setLocationRelativeTo(null);
		
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		setJMenuBar(menuBar);
		
		JMenu mFile = new JMenu("File");
		menuBar.add(mFile);
		
		JMenuItem miSave = new JMenuItem("Save File");
		miSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				
				String title,content;
				title=txtTitle.getText();
				content=txtContent.getText();
				
				String code="newnote"+title+"%"+content;
				try
				{
					dout.writeUTF(code);
					dout.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				defaultList.addElement(title+".txt");
			}
		});
		mFile.add(miSave);
		
		JSeparator sprtr1 = new JSeparator();
		mFile.add(sprtr1);
		
		JMenuItem miClose = new JMenuItem("Close Program");
		miClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		mFile.add(miClose);
		
		JMenu mHelp = new JMenu("Help");
		menuBar.add(mHelp);
		
		JMenuItem miAbout = new JMenuItem("About Program");
		miAbout.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(contentPane,"This program is a notepad application that works synchronously on mobile and desktop."
						+ "\nIt is designed using Socket Programming. The server application of the program is run on the server."
						+ "\nBefore running the program, you need to edit the ports, ip and path according to your own server."
						+ "\nThe server application must be run before running the client."
						+ "\nIn this way, you can run the client anywhere as long as you access the server."
						+ "\nAt the same time, your notes are stored on your server.","About",1);
			}
		});
		mHelp.add(miAbout);
		
		JSeparator sprtr2 = new JSeparator();
		mHelp.add(sprtr2);
		
		JMenuItem miLicence = new JMenuItem("Program Licence");
		miLicence.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(contentPane,"GNU General Public License v3.0","Licence",1);
			}
		});
		mHelp.add(miLicence);
		
		JSeparator sprtr3 = new JSeparator();
		mHelp.add(sprtr3);
		
		JMenuItem miDeveloper = new JMenuItem("Developer Contact");
		miDeveloper.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(contentPane,"This program developed by Fehmi ÞENER.\n You can contant me with \"fehmisener\" on GitHub or Facebook.","Developer",1);
			}
		});
		mHelp.add(miDeveloper);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelLeft = new JPanel();
		panelLeft.setBackground(Color.DARK_GRAY);
		panelLeft.setBounds(0, 0, 300, 439);
		contentPane.add(panelLeft);
		panelLeft.setLayout(null);
		
		list = new JList<String>();
		list.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				
				String selectedNote=list.getSelectedValue();
				txtTitle.setText(selectedNote.substring(0, selectedNote.length()-4));
				selectedNote="getContent"+selectedNote;
				try
				{
					dout.writeUTF(selectedNote);
					dout.flush();
				}catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
		});
		list.setFont(new Font("Tahoma", Font.PLAIN, 20));
		list.setForeground(Color.LIGHT_GRAY);
		list.setBackground(Color.DARK_GRAY);
		list.setBounds(0, 0, 300, 439);
		list.setModel(defaultList);
		panelLeft.add(list);
		
		JPanel panelRight = new JPanel();
		panelRight.setBounds(300, 0, 500, 475);
		contentPane.add(panelRight);
		panelRight.setLayout(null);
		
		JLabel lblTitle = new JLabel("Title : ");
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblTitle.setBounds(39, 0, 102, 45);
		panelRight.add(lblTitle);
		
		JLabel lblBack = new JLabel("");
		lblBack.setIcon(new ImageIcon(clientMain.class.getResource("/images/backgrond.jpg")));
		lblBack.setBounds(0, 0, 488, 441);
		panelRight.add(lblBack);
		
		txtTitle = new JTextField();
		txtTitle.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		txtTitle.setBorder(null);
		txtTitle.setBackground(new Color(246, 246, 246));
		txtTitle.setFont(new Font("Tahoma", Font.PLAIN, 30));
		txtTitle.setBounds(124, 0, 364, 45);
		panelRight.add(txtTitle);
		txtTitle.setColumns(10);
		
		txtContent = new JTextArea();
		txtContent.setLineWrap(true);
		txtContent.setFont(new Font("Monospaced", Font.PLAIN, 20));
		txtContent.setBackground(new Color(246, 246, 246));
		txtContent.setBounds(39, 70, 449, 364);	
		panelRight.add(txtContent);
	}
}
