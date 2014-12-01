
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Imtiaz
 */
public class TransmissionEntry implements Runnable{
    
    ClientStat clstat;
    String ip = "";
    String sendData="";
    
    int flag = 0;
    public TransmissionEntry()
    {
    }
    
    public TransmissionEntry(ClientStat cs, int flag)
    {
        this.clstat = cs;
        this.flag = flag;
    }
    public TransmissionEntry(ClientStat cs, int flag, String data)
    {
        this.clstat = cs;
        this.flag = flag;
        this.sendData = data;
    }

    public void run() {
        
        //This porion creates new entry
        if(flag == 1)
        {
            String sender,receiver;
            ip = clstat.getIP();
            sender = clstat.getUserID();
            receiver = clstat.getRemoteUserID();

            try {
                    // open a connection to the site
                    URL url = new URL(ip+"ecgserver/ecgcontroller/createEntry");
                    URLConnection con = url.openConnection();
                    // activate the output
                    con.setDoOutput(true);
                    //con.setRequestProperty("Content-Type", "text/plain");
                    PrintStream ps = new PrintStream(con.getOutputStream());
                    // send your parameters to your site
                    ps.print("sender="+sender);
                    ps.print("&receiver="+receiver);
                    ps.print("&check=bmptw012010");



                    // we have to get the input stream in order to actually send the request
                    con.getInputStream();

                    // close the print stream
                    ps.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    String parts[];
                    String response="";
                   
                    while ((line = in.readLine()) != null) 
                    {
                          //System.out.println(line);
                        if(line.contains("bay00LAB"))
                        {
                             
                             response = line;
                        }


                    }
                    if(!response.contains("bay00LAB"))
                    {
                        JOptionPane.showMessageDialog(null, "Problem Occured, Try Again");
                    }
                    else
                    {
                        parts  = response.split(" ");
                        clstat.setTransmissionID(parts[0]);
                   }
                    }   
            catch (MalformedURLException ex) 
            {
                //JOptionPane.showMessageDialog(null, "Problem in connection!"); 
                //System.out.println(ex);        //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);

            } 
            catch (IOException ex) 
            {
             //System.out.println(ex);                //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                //JOptionPane.showMessageDialog(null, "Problem in connection!"); 
            }
        }
        
        //This portion updates patient info in db
        else if(flag == 2)
        {
         
           
            
            ip = clstat.getIP();
           
            
             try {
                    // open a connection to the site
                    URL url = new URL(ip+"ecgserver/ecgcontroller/updatePatientInfo");
                    URLConnection con = url.openConnection();
                    // activate the output
                    con.setDoOutput(true);
                    //con.setRequestProperty("Content-Type", "text/plain");
                    PrintStream ps = new PrintStream(con.getOutputStream());
                    // send your parameters to your site
                    ps.print("id="+clstat.getTransmissionID());
                    ps.print("&patient_id="+ clstat.getPatientId());
                    ps.print("&patient_name="+clstat.getFirstName());
                    ps.print("&patient_sex="+ clstat.getSex());
                    ps.print("&patient_age="+clstat.getAge());
                    ps.print("&center="+clstat.getUpazila());
                    ps.print("&check=bmptw012010");
                    
                    



                    // we have to get the input stream in order to actually send the request
                    con.getInputStream();

                    // close the print stream
                    ps.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    String parts[];
                    String response="";
                   
                    while ((line = in.readLine()) != null) 
                    {
                       
                      System.out.println(line);
                        if(line.equals("bay00LAB"))
                        {
                             
                             response = line;
                        }


                    }
                    if(!response.equals("bay00LAB"))
                    {
                        //JOptionPane.showMessageDialog(null, "Problem Occured, Try Again");
                    }
                    
                    }   
            catch (MalformedURLException ex) 
            {
               // JOptionPane.showMessageDialog(null, "Problem in connection!"); 
                //System.out.println(ex);        //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);

            } 
            catch (IOException ex) 
            {
             //System.out.println(ex);                //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
               // JOptionPane.showMessageDialog(null, "Problem in connection!"); 
            }
        }
        
        //This code block updates realtime ecg data in db
        else if(flag == 3)
        {
         
           
            
            ip = clstat.getIP();
           
            
             try {
                    // open a connection to the site
                    URL url = new URL(ip+"ecgserver/ecgcontroller/saveData");
                    URLConnection con = url.openConnection();
                    // activate the output
                    con.setDoOutput(true);
                    //con.setRequestProperty("Content-Type", "text/plain");
                    PrintStream ps = new PrintStream(con.getOutputStream());
                    // send your parameters to your site
                    ps.print("id="+clstat.getTransmissionID());
                    ps.print("&data="+sendData);
                    ps.print("&check=bmptw012010");
                     
                    



                    // we have to get the input stream in order to actually send the request
                    con.getInputStream();

                    // close the print stream
                    ps.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    String parts[];
                    String response="";
                   
                    while ((line = in.readLine()) != null) 
                    {
                      
                       System.out.println(line);
                      //  if(line.equals("bay00LAB"))
                       // {
                             
                             response = line;
                       // }
                        
                            


                    }
                    if(!response.equals("bay00LAB"))
                    {
                        //JOptionPane.showMessageDialog(null, response);
                    }
                    
                    }   
            catch (MalformedURLException ex) 
            {
                //JOptionPane.showMessageDialog(null, "Problem in connection!"); 
                //System.out.println(ex);        //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);

            } 
            catch (IOException ex) 
            {
             //System.out.println(ex);                //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
               // JOptionPane.showMessageDialog(null, "Problem in connection!"); 
            }
        }
        
    }
    
}
