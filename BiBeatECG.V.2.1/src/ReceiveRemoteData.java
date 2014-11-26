
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.jivesoftware.smack.XMPPException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aumi
 */
public class ReceiveRemoteData extends Thread{
    
    JabberSmackAPI jabb;
    
    FileWriter outputStream = null;
    ClientStat clstat;
    EcgDisplay display;
    //ClientStat clstat;
    
    File file = new File("./filetoreceive/ECGTelemedicine.txt");
    BufferedReader reader = null;
    
    
    String fname = "",lname = "", sex = "", age = "",lead = "",gain = "", filt= "",id = "", upazila = "",horizontalScalling = "",verticalScalling = "",verticalScallingV = "";
    int horizontalFlag;
    
     private boolean stop = false;
     
     public boolean running = false;
   
     public void activateStop(boolean flag)
    {
            stop = flag;
            
    }
     
     public void isRunning()
     {
        running = true;
     }
    
    public ReceiveRemoteData(JabberSmackAPI j, ClientStat cs, EcgDisplay ed) //throws XMPPException
    {
        jabb = j;
        clstat = cs;
        display = ed;
        //jabb.login();
         
    
	}
    
    
    public void run()
    {
    
        PacketFilter filter 
        = new AndFilter(new PacketTypeFilter(Message.class), 
                        new FromContainsFilter(clstat.getSenderGmailID()));

        PacketListener myListener = new PacketListener() {
        public void processPacket(Packet packet) {
            if (packet instanceof Message) {
                Message msg = (Message) packet;
                // Process message
                System.out.println("Roger, Roger");//+ msg.getBody());
                    try {
                        outputStream = new FileWriter("./filetoreceive/ECGtelemedicine.txt");
                        
                        outputStream.write(msg.getBody());
                        outputStream.flush();
                         outputStream.close();
                         
                         reader = new BufferedReader(new FileReader(file));
                         
                         fname = reader.readLine();
                          //lname = reader.readLine();
                          sex = reader.readLine();
                          age = reader.readLine();
                          lead = reader.readLine();
                          filt = reader.readLine();
                          id = reader.readLine();
                          upazila = reader.readLine();
                          horizontalScalling = reader.readLine();
                          verticalScalling = reader.readLine();
                          verticalScallingV = reader.readLine();
                          horizontalFlag = Integer.parseInt(horizontalScalling);
                          
                          display.pfNameTextField.setText(fname);
                          
                          //display.plNameTextField.setText(lname);
//                          clstat.setPatientName(fname, lname);
                          clstat.setPatientName(fname);
                          if(sex.equals("Male"))
                          {
                              display.maleButton.setSelected(true);
                              clstat.setSex("Male");
                          }
                          else
                          {
                              display.femaleButton.setSelected(true);
                              clstat.setSex("Female");
                          }
                          display.ageTextField.setText(age);
                          clstat.setAge(age);
                          clstat.setSelectedLead(lead);
                          
                          
                          
                          if(lead.equals("Lead I"))
                          {
                              display.iRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("Lead II"))
                          {
                              display.iiRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("Lead III"))
                          {
                              display.iiiRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("Lead aVR"))
                          {
                              display.avrRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("Lead aVL"))
                          {
                              display.avlRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("Lead aVF"))
                          {
                              display.avfRadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScalling);
                          }
                          else if(lead.equals("Lead V1"))
                          {
                              display.v1RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("Lead V2"))
                          {
                              display.v2RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("Lead V3"))
                          {
                              display.v3RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("Lead V4"))
                          {
                              display.v4RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("Lead V5"))
                          {
                              display.v5RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          else if(lead.equals("Lead V6"))
                          {
                              display.v6RadioButton.setSelected(true);
                              display.gainLabel.setText(verticalScallingV);
                          }
                          
                           else if(lead.equals("Extended Lead II"))
                          {
                              display.eiiRadioButton.setSelected(true);
                          }
                          
                          
                         
                          clstat.setVerticalScaling(verticalScalling);
                          clstat.setVerticalScalingV(verticalScallingV);
                          //display.gainLabel.setText("10 mm/mV");
                             
                        
                           
                          
                          if(filt.equals("false"))
                          {
                              display.filterStatusLabel.setText("OFF");
                          }
                          else
                              display.filterStatusLabel.setText("ON");
                          clstat.setPatientId(id);
                          clstat.setUpazila(upazila);
                          
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
                          
                          display.upazilaLabel.setText("Data Comming From "+ upazila);
                          display.patientIdTextField.setText(id);
                          String text = "";
                          int [] buffer = new int[512];
                          int i = 0;
                          while((text = reader.readLine()) !=null)
                          {
                              buffer[i] = Integer.parseInt(text.trim());
                              System.out.println(""+buffer[i]);
                              i++;
                          }
                          
                          if(clstat.getLeadNo().equals("Lead I") || clstat.getLeadNo().equals("Lead II") || clstat.getLeadNo().equals("Lead III"))
                          {
                            try {
                                //JOptionPane.showMessageDialog(null, clstat.getLeadNo()+ "Send Flag = " + clstat.getSendDataFlag() );
                                //ld1.setValue(buffer);
                                //JOptionPane.showMessageDialog(null, "Lovelu");
                                clstat.getDisplay1Object().setValue(buffer);
                                //clstat.getDisplay1Object().setValue(buffer);
                            } catch (XMPPException ex) {
                                Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                            }
                          }
                          else if(clstat.getLeadNo().equals("Lead aVR") || clstat.getLeadNo().equals("Lead aVL") || clstat.getLeadNo().equals("Lead aVF"))
                             try {
                            clstat.getDisplay2Object().setValue(buffer);// ld2.setValue(buffer);
                        } catch (XMPPException ex) {
                            Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                        }// ld2.setValue(buffer);
                          else if(clstat.getLeadNo().equals("Lead V1") || clstat.getLeadNo().equals("Lead V2") || clstat.getLeadNo().equals("Lead V3"))
                              try {
                            clstat.getDisplay3Object().setValue(buffer);//ld3.setValue(buffer);
                        } catch (XMPPException ex) {
                            Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                        }//ld3.setValue(buffer);
                          else if(clstat.getLeadNo().equals("Lead V4") || clstat.getLeadNo().equals("Lead V5") || clstat.getLeadNo().equals("Lead V6"))
                              try {
                            clstat.getDisplay4Object().setValue(buffer);//ld4.setValue(buffer);
                        
                      //...
                        } catch (XMPPException ex) {
                            Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                        }//ld4.setValue(buffer);
                          
                         else if(clstat.getLeadNo().equals("Extended Lead II"))
                              try {
                            clstat.getDisplaye2Object().setValue(buffer);//ld4.setValue(buffer);
                        
                      //...
                        } catch (XMPPException ex) {
                            Logger.getLogger(ReceiveRemoteData.class.getName()).log(Level.SEVERE, null, ex);
                        }//ld4.setValue(buffe
                          
                        //...
                    } catch (IOException ex) {
                       System.out.println(""+ex);
                    }
            }
        }
    };
    // Register the listener.
    while(!stop)
    jabb.connection.addPacketListener(myListener, filter);
    }
}
