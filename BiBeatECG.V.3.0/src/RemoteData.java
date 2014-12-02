
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
import org.jivesoftware.smack.XMPPException;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Imtiaz
 */
public class RemoteData implements Runnable{

    private boolean stop = false;
    private boolean checkStop = false;
    private boolean infoCheck = false;
    ClientStat clstat;
    String ip="";
    EcgDisplay display;
    public RemoteData()
    {
    }

    public RemoteData(ClientStat clstat, EcgDisplay display) {
        this.clstat = clstat;
        this.display = display;
    }
    public void activateStop()
    {
        this.stop = true;
    }
    public void run() {
        
        while(!checkStop)
        {
            String sender,receiver;
            ip = clstat.getIP();
            receiver = clstat.getUserID();
            sender = clstat.getRemoteUserID();
            

            try {
                    // open a connection to the site
                    URL url = new URL(ip+"ecgserver/ecgcontroller/checkEntry");
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
                          System.out.println(line);
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
                        checkStop = true;
                        System.out.println(parts[0]);
                        
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
       
        while(!stop)
        {
          
            ip = clstat.getIP();
           

            try {
                    // open a connection to the site
                    URL url = new URL(ip+"ecgserver/ecgcontroller/receiveData");
                    URLConnection con = url.openConnection();
                    // activate the output
                    con.setDoOutput(true);
                    //con.setRequestProperty("Content-Type", "text/plain");
                    PrintStream ps = new PrintStream(con.getOutputStream());
                    // send your parameters to your site
                    ps.print("id="+clstat.getTransmissionID());
                    ps.print("&check=bmptw012010");
                    
                   //System.out.println(clstat.getTransmissionID()+"bupla");

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
                        //JOptionPane.showMessageDialog(null, "Problem Occured, Try Again");
                    }
                    else
                    {
                        parts  = response.split("bay00LAB");
                        //clstat.setTransmissionID(parts[0]);
                        if(!infoCheck)
                        {
                            infoCheck();
                        }
                        displayData(parts[1]);
                        //System.out.println(parts[1]);
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
        
    }

    private void displayData(String data) {
         
        
        /*
                   sendData += clstat.getLeadNo() + ";";
                  sendData += clstat.gethorScalling() + ";";
                  sendData += clstat.getVerticalScalling()+ ";";
                  sendData += clstat.getVerticalScallingV()+ ";";  
                  sendData += clstat.getFilterFlag() + ";";
        */
                          String lead = "",gain = "", filt= "",horizontalScalling = "",verticalScalling = "",verticalScallingV = "";
                          int horizontalFlag;
                          String parts[]  = data.split(";");
                          lead = parts[0];
                         
                          horizontalFlag = Integer.parseInt(parts[1]);
                          verticalScalling = parts[2];
                          verticalScallingV = parts[3];
                          filt = parts[4];
                          //display.pfNameTextField.setText(fname);
                          
                          //display.plNameTextField.setText(lname);
//                          clstat.setPatientName(fname, lname);
                          /*
                          */
                                  
                          clstat.setSelectedLead(lead);
                          
                          
                          
                          if(lead.equals("1"))
                          {
                              display.iRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("2"))
                          {
                              display.iiRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("3"))
                          {
                              display.iiiRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("4"))
                          {
                              display.avrRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("5"))
                          {
                              display.avlRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("6"))
                          {
                              display.avfRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("7"))
                          {
                              display.v1RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("8"))
                          {
                              display.v2RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("9"))
                          {
                              display.v3RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("10"))
                          {
                              display.v4RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("11"))
                          {
                              display.v5RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("12"))
                          {
                              display.v6RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          
                           else if(lead.equals("13"))
                          {
                              display.eiiRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          
                          
                         
                          clstat.setVerticalScaling(verticalScalling);
                          clstat.setVerticalScalingV(verticalScallingV);
                          
                             
                        
                           
                          
                          if(filt.equals("false"))
                          {
                              display.filterStatusLabel.setText("OFF");
                          }
                          else
                              display.filterStatusLabel.setText("ON");
                         
                          
                         
                          
                          if(horizontalFlag==1)
                          {
                              clstat.sethorScalling(1);
                              display.mmPerSecLabel.setText("15 mm/sec");
                          }
                          else if((horizontalFlag==2))
                          {
                              clstat.sethorScalling(2);
                              display.mmPerSecLabel.setText("20 mm/sec");
                          }if(horizontalFlag==3)
                          {
                              clstat.sethorScalling(3);
                              display.mmPerSecLabel.setText("25 mm/sec");
                          }
                          else
                          {
                               clstat.sethorScalling(4);
                              display.mmPerSecLabel.setText("50 mm/sec");
                          }
                          
                          
                          String text = "";
                          int [] buffer = new int[512];
                          
                          String partsData[] = parts[5].split(" ");
                          for(int i=0; i<partsData.length; i++)
                          {
                              buffer[i] = Integer.parseInt(partsData[i].trim());
                              //System.out.println(""+buffer[i]);
                              //i++;
                          }
                          
                          if(clstat.getLeadNo().equals("1") || clstat.getLeadNo().equals("2") || clstat.getLeadNo().equals("3"))
                          {
                            try {
                                //JOptionPane.showMessageDialog(null, clstat.getLeadNo()+ "Send Flag = " + clstat.getSendDataFlag() );
                                //ld1.setValue(buffer);
                                //JOptionPane.showMessageDialog(null, "Lovelu");
                                clstat.getDisplay1Object().setValue(buffer);
                                //clstat.getDisplay1Object().setValue(buffer);
                            } catch (XMPPException ex) {
                                  Logger.getLogger(RemoteData.class.getName()).log(Level.SEVERE, null, ex);
                              }
                          }
                          else if(clstat.getLeadNo().equals("4") || clstat.getLeadNo().equals("5") || clstat.getLeadNo().equals("6"))
                             try {
                            clstat.getDisplay2Object().setValue(buffer);// ld2.setValue(buffer);
                        } catch (XMPPException ex) {
                            Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                        }// ld2.setValue(buffer);
                          else if(clstat.getLeadNo().equals("7") || clstat.getLeadNo().equals("8") || clstat.getLeadNo().equals("9"))
                              try {
                            clstat.getDisplay3Object().setValue(buffer);//ld3.setValue(buffer);
                        } catch (XMPPException ex) {
                            Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                        }//ld3.setValue(buffer);
                          else if(clstat.getLeadNo().equals("10") || clstat.getLeadNo().equals("11") || clstat.getLeadNo().equals("12"))
                              try {
                            clstat.getDisplay4Object().setValue(buffer);//ld4.setValue(buffer);
                        
                      //...
                        } catch (XMPPException ex) {
                            Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                        }//ld4.setValue(buffer);
                          
                         else if(clstat.getLeadNo().equals("13"))
                              try {
                            clstat.getDisplaye2Object().setValue(buffer);//ld4.setValue(buffer);
                        
                      //...
                        } catch (XMPPException ex) {
                            Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                        }//ld4.setValue(buffe
    }

    private void infoCheck() {
        
        
        
            try {
                    // open a connection to the site
                    URL url = new URL(ip+"ecgserver/ecgcontroller/patientInfo");
                    URLConnection con = url.openConnection();
                    // activate the output
                    con.setDoOutput(true);
                    //con.setRequestProperty("Content-Type", "text/plain");
                    PrintStream ps = new PrintStream(con.getOutputStream());
                    // send your parameters to your site
                    ps.print("id="+clstat.getTransmissionID());
                    ps.print("&check=bmptw012010");
                    
                   //System.out.println(clstat.getTransmissionID()+"bupla");

                    // we have to get the input stream in order to actually send the request
                    con.getInputStream();

                    // close the print stream
                    ps.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = null;
                    String parts[];
                    String parts_info[];
                    String response="";
                   
                    while ((line = in.readLine()) != null) 
                    {
                          System.out.println(line);
                        if(line.contains("bay00LAB"))
                        {
                             
                             response = line;
                        }


                    }
                    if(!response.contains("bay00LAB"))
                    {
                        //JOptionPane.showMessageDialog(null, "Problem Occured, Try Again");
                    }
                    else
                    {
                        parts  = response.split("bay00LAB");
                        infoCheck = true;
                        parts_info = parts[1].split(" ");
                        clstat.setPatientId(parts_info[0]);
                        display.patientIdTextField.setText(parts_info[0]);
                        
                        clstat.setPatientName(parts_info[1]);
                        display.pfNameTextField.setText(parts_info[1]);
                        if(parts_info[2].equals("Male"))
                        {
                            display.maleButton.setSelected(true);
                            clstat.setSex("Male");
                        }
                        else
                        {
                            display.femaleButton.setSelected(true);
                            clstat.setSex("Female");
                        }
                          display.ageTextField.setText(parts_info[3]);
                          clstat.setAge(parts_info[3]);
                          
                          display.upazilaLabel.setText("Data Comming From "+ parts_info[4]);
                          clstat.setUpazila(parts_info[4]);
                        //clstat.setTransmissionID(parts[0]);
                       
                      
                        //System.out.println(parts[1]);
                   }
                    }   
            catch (MalformedURLException ex) 
            {
                //JOptionPane.showMessageDialog(null, "Problem in connection!"); 
                //System.out.println(ex);        //Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) { 
            Logger.getLogger(RemoteData.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
    }
    
}
