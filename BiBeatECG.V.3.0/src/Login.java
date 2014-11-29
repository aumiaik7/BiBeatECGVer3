/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Imtiaz
 */

public class Login implements Runnable{
    
    int i = 0;
    ClientStat clstat;
    String ip = "http://192.168.0.102/"; 
    public Login(ClientStat cs)
    {
        clstat = cs;
         
    }

    @Override
    public void run() {
        
        while(true)
        {
         try {
                // open a connection to the site
                URL url = new URL(ip+"ecgserver/ecgcontroller/savedata");
                URLConnection con = url.openConnection();
                // activate the output
                con.setDoOutput(true);
                //con.setRequestProperty("Content-Type", "text/plain");
                PrintStream ps = new PrintStream(con.getOutputStream());
                // send your parameters to your site
                ps.print("id="+i);
                i++;

                // we have to get the input stream in order to actually send the request
                con.getInputStream();

                // close the print stream
                ps.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) 
                {
                    if(line.equals("bay00LAB"))
                    {
                        clstat.setIsLoggedIn(true);
                    }
                    System.out.println(line);
                }
                }   
        catch (MalformedURLException ex) 
        {
                System.out.println(ex);        //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    
        } 
        catch (IOException ex) 
        {
         System.out.println(ex);                //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
            try {
                Thread.sleep(1000);
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            } catch (InterruptedException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    }
    
}
