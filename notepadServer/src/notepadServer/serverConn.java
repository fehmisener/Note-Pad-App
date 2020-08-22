package notepadServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class serverConn extends Thread
{
	
	
	private Socket socket;
	private ServerSocket serversocket;
	private DataInputStream din;
	private DataOutputStream dout;
	boolean serverStat=true;
	
	public serverConn(Socket socket)
	{
		 this.socket=socket;
	}
	
	public void sendToClient(String msg)
	{
		try
		{
			dout.writeUTF(msg);
			dout.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public void splitText(String text)
	{
		String title=text.substring(7,text.indexOf("%"));
		String content=text.substring(text.indexOf("%")+1,text.length());
		saveFiles(title,content);
	}
	public void saveFiles(String title,String content)
	{
		PrintWriter pw=null;
		
		try
		{
			pw = new PrintWriter(new File("/notepadServer/noteFiles/"+title+".txt"));
			pw.write(content);
		}
		catch (Exception e)
		{
			
		}finally {
			if(pw!=null) 
				pw.close();
		}
		
	}
	public void getFiles()
	{
		File filesArr []= new File("/notepadServer/noteFiles/").listFiles();
		String allFiles="";
		
		for (File file : filesArr){
			allFiles+=file.getName()+":";
		}
		allFiles="files"+allFiles;
		try {
			dout.writeUTF(allFiles);
			dout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getContent(String file)
	{	
		String fileName=file.substring(10,file.length());
		String content="";
		try(Scanner sc = new Scanner(new File("/Users/fehmi/eclipse-workspace/notepadServer/noteFiles/"+fileName)))
		{
			
			while(sc.hasNextLine())
			{
					content+=sc.nextLine()+"\n";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try
		{
			dout.writeUTF("content"+content);
			dout.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	public void run()
	{
		try
		{
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			
			while(serverStat)
			{
				while(din.available()==0)
					Thread.sleep(10);
				
				String msg = din.readUTF();				
				if(msg.startsWith("newnote"))
					splitText(msg);
				else if(msg.startsWith("getFiles"))
					getFiles();
				else if(msg.startsWith("getContent"))
					getContent(msg);
				
				
			}
			
			din.close();
			dout.close();
			socket.close();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
