package test.java;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ShellScriptRunner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		excute("127.0.0.1",22,"admin","c://test");
	}
	public static void excute(String host, int port, String userName, String location) {

		Session session = null;
		ChannelExec channelExec = null;
		try {
            JSch jsch = new JSch();
            session = jsch.getSession(userName, host, port);//set username and Ip and port
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            String pass=JOptionPane.showInputDialog("Enter pass", 
            "");
            
            session.setPassword(pass);
            session.setConfig( "PreferredAuthentications", "publickey,keyboard-interactive,password");
            System.out.println("setting up");
            session.connect();
			System.out.println("connected");
			
			// create the execution channel over the session
			channelExec = (ChannelExec) session.openChannel("exec");
		
			// Gets an InputStream for this channel. All data arriving in as
			// messages from the remote side can be read from this stream.
			InputStream in = channelExec.getInputStream();
			
			InputStream out= channelExec.getErrStream();
			System.out.println(location);

			// Set the command that you want to execute  
			// In our case its the location of the remote shell script 
			
			channelExec.setCommand("sh "+location);
			channelExec.connect();
			// Read the output from the input stream we set above
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			// Read each line from the buffered reader and add it to result list
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			BufferedReader errorReader = new BufferedReader(new InputStreamReader(out));
			while ((line = errorReader.readLine()) != null) {
				System.out.println(line);
			}
			
			reader.close();
			in.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (channelExec != null && channelExec.isConnected())
				channelExec.disconnect();
			if (session != null && session.isConnected())
				session.disconnect();
		}
		

	}

}
