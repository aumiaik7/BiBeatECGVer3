import java.util.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
 
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
 
public class JabberSmackAPI implements MessageListener{
 
    XMPPConnection connection;
    
    BufferedReader in = null;
    private  char[] char_arr = new char[11];
    private String id ;
    private String pass;
 
//    public void login(String userName, String password) throws XMPPException
//    {
//        //XMPPConnection.DEBUG_ENABLED = true;
//    ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 
//					5222, 
//					"gmail.com");
//    connection = new XMPPConnection(config);
// 
//    connection.connect();
//    connection.login(userName, password);
//    }
    public void login()  throws FileNotFoundException, IOException 
    {
        in = new BufferedReader(new FileReader("./Info/bin.txt")) ;
        id = in.readLine();
        pass = in.readLine();
        
        in.close();
//         System.out.println(pass);
//        
        char_arr = pass.toCharArray();
//                 
//                 //char_arr[0] = (char) (char_arr[0] - 1); 
            char_arr[0] = (char) (char_arr[0] - 1); 
            char_arr[1] = (char) (char_arr[1] - 2); 
            char_arr[2] = (char) (char_arr[2] - 1); 
            char_arr[3] = (char) (char_arr[3] - 2); 
            char_arr[4] = (char) (char_arr[4] - 2); 
            char_arr[5] = (char) (char_arr[5] - 1); 
            char_arr[6] = (char) (char_arr[6] - 2); 
            char_arr[7] = (char) (char_arr[7] - 1); 
            char_arr[8] = (char) (char_arr[8] - 2); 
            char_arr[9] = (char) (char_arr[9] - 2); 
            char_arr[10] = (char) (char_arr[10] - 2); 
                 
           // pass = new String(char_arr);
            //System.out.println(pass);
            //System.out.println(id);
        
        
        //XMPPConnection.DEBUG_ENABLED = true;
    ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 
					5222, 
					"gmail.com");
    connection = new XMPPConnection(config);
        try {
            connection.connect();
        } catch (XMPPException ex) {
            JOptionPane.showMessageDialog(null, "Login Failed Check Internet Connectionnn");
            System.exit(0);
            Logger.getLogger(JabberSmackAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            connection.login(id+"@gmail.com", pass);
        } catch (XMPPException ex) {
            
            JOptionPane.showMessageDialog(null, "Login Failed Check Internet Connection");
            System.exit(0);
            Logger.getLogger(JabberSmackAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    public void JabberSmackAPI() throws XMPPException
//    {
//        ConnectionConfiguration config = new ConnectionConfiguration("talk.google.com", 
//					5222, 
//					"gmail.com");
//    connection = new XMPPConnection(config);
// 
//    connection.connect();
//    connection.login("aumi.aik7@gmail.com", "220522614219");
//    }
// 
    public void sendMessage(String message, String to) throws XMPPException
    {
    Chat chat = connection.getChatManager().createChat(to, this);
    chat.sendMessage(message);
    }
 
    public void displayBuddyList()
    {
    Roster roster = connection.getRoster();
    Collection<RosterEntry> entries = roster.getEntries();
 
    System.out.println("\n\n" + entries.size() + " buddy(ies):");
    for(RosterEntry r:entries)
    {
    System.out.println(r.getUser());
    }
    }
 
    public void disconnect()
    {
    connection.disconnect();
    }
 
    public void processMessage(Chat chat, Message message)
    {
    if(message.getType() == Message.Type.chat)
    System.out.println(chat.getParticipant() + " says: " + message.getBody());
    }
 
    public static void main(String args[]) throws XMPPException, IOException
    {
    // declare variables
    JabberSmackAPI c = new JabberSmackAPI();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String msg;
 
 
    // turn on the enhanced debugger
    XMPPConnection.DEBUG_ENABLED = true;
 
 
    // Enter your login information here
   
 
    c.displayBuddyList();
 
    System.out.println("-----");
 
    System.out.println("Who do you want to talk to? - Type contacts full email address:");
    String talkTo = br.readLine();
 
    System.out.println("-----");
    System.out.println("All messages will be sent to " + talkTo);
    System.out.println("Enter your message in the console:");
    System.out.println("-----\n");
 
    while( !(msg=br.readLine()).equals("bye"))
    {
        c.sendMessage(msg, talkTo);
    }
 
    c.disconnect();
    System.exit(0);
    }

//    public void login() {
//        //throw new UnsupportedOperationException("Not yet implemented");
//    }
 
}