/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package LeadDisplay;

//import display.ClientStat;
/**
 *
 * @author Aumi
 *///import processing.core.PApplet;
import javax.swing.JOptionPane;
import org.jivesoftware.smack.XMPPException;
import processing.core.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import processing.opengl.*;


/**
 *
 * @author Abul Bashar
 */

public class LeadDisplay1 extends PApplet{




     /*
      * Saves the value of data
      */
      public int yval;

      private int ecgrecord[];
      private int recordBuffer[] = new int[1200];
      private int localBuffer1[] = new int[512];
      private int localBuffer2[] = new int[512];
      private int localBuffer3[] = new int[512];
     /*
      * previous value of x and y
      */
      private int xprev = 0;
      private int yprev = 0;
      private int localXprev = 0;
      private int localYprev = 0;
      private int offset = 127;
      private int resolution ;
      private double del;
      private int iPos;
      private float localGain = 1.0f;
      private boolean isTrue = false;
      private boolean isLocal1 = false;
      private boolean isLocal2 = false;
      private boolean isLocal3 = false;
      private boolean flag1 = false;
      private boolean flag2 = false;
      private boolean flag3 = false;
      private boolean bf1 = false; // bf means Buffer Flag
      private boolean bf2 = false;
      private boolean bf3 = false;
      private String sendData = "";
     
      
      byte selectLead[] = new byte[9];

      private String filepath = "c:/a1.txt";

      FileWriter outputStream = null;

      private int midpoint = 0;
      ClientStat clstat;
      EcgRecordGraph ecgr;
      EcgDisplay ecgD;
      communication usb;
     
      JabberSmackAPI jabb;
      Thread transEntry;
      
 
     
      PImage img;
      float avg = 0;
    

    public LeadDisplay1(ClientStat cs, int r, communication u,EcgRecordGraph er,EcgDisplay ed,JabberSmackAPI j) {
        clstat = cs;
        resolution = r;
        usb = u;
        ecgr = er;
        ecgD = ed;
        jabb = j;
        
        for(int i = 0; i <9 ; i++)
          {

              selectLead[i]=  0;
          }
        
        //utp = ut;
    }

    private int mean(int[] data) {
        int sum = 0;  // sum of all the elements
        for (int i=1; i<= 500; i++) {
            sum += data[i];
        }
        return sum / 500;
    }



    private void beatCalculation(int[] data) {
        int avg = mean(data);
        for (int i=0; i<500;i++)
        {
            data[i] = data[i+1] - data[i];//(int) ( Math.abs(data[i] - avg));
            
            rect(i, data[i], 5, 5);
        }
        int count = 0;
        for (int i=25; i<500;i++)
        {
            if(Math.abs(data[i+1] - data[i]) >= 20 )
            {
                count++;
                i+=20;
            }
        }
        JOptionPane.showMessageDialog(null,count+"");
    }


    public void setValue(int[] val) throws XMPPException
    {

        float avg = 0;
     
        

       background(255,255,240);
       strokeWeight( 0.0f);
       stroke(157,157,255);
       smooth();

       /*
        * weight of line
        */
        for(int i = 0; i <= width; i+=5)
        {
            line(i,0,i,height);
        }
       for(int i = 0; i <= height; i+=5)
        {
            line(0,i,width,i);
        }
          stroke(1,7,254);
        smooth();
       /*
        * weight of line
        */
        strokeWeight( 0.4f);

        for(int i = 0; i <= width; i+=25)
        {
            line(i,0,i,height);
        }

        for(int i = 0; i <= height; i+=25)
        {
            line(0,i,width,i);
        }
         /*
       * Color of the line
       */

       

       
       int del3Y[] = new int[512];
       
       
       if(clstat.getSendDataFlag() == 1 || clstat.getSendDataFlag() == 3)
       {    
         
          int j = 0;
      
           
               for(j=0;j < val.length; j++)
               {
                   
                    del3Y[j] = (int)(val[j])  ;
               
               }
               
              if(clstat.getSendDataFlag() == 3)
              {
                  sendData = "";
                  sendData += clstat.getLeadNo() + ";";
                  sendData += clstat.gethorScalling() + ";";
                  sendData += clstat.getVerticalScalling()+ ";";
                  sendData += clstat.getVerticalScallingV()+ ";";  
                  sendData += clstat.getFilterFlag() + ";";
                  
                  
                  
                   
                  
                    for(int i = 0; i<del3Y.length;i++)
                  {
              
                      sendData += del3Y[i] + " ";
                  }
                    
                    transEntry = new Thread(new TransmissionEntry(clstat,3,sendData));
                    transEntry.start();
                  
                //  jabb.sendMessage(sendData, clstat.getgmailID());
                
            
              }
          
       }
       else if(clstat.getSendDataFlag() == 2)
       {
          int j = 0;
    
           
               for(j=0;j < val.length; j++)
               {
                   
                    del3Y[j] = (int)(val[j])  ;
               
               }
           ecgD.jTabbedPane1.setSelectedIndex(0);
       }

        if(clstat.getLeadNo().equals("1"))
         {
             
                flag2 = false;
                flag3 = false;
               
                 
                 flag1 = true;
                

              bf2 = true;
              bf3 = true;
              bf1 = false;
              midpoint = -(25*15);
              clstat.setLead1(del3Y);
          
             
    
         }
         else if(clstat.getLeadNo().equals("2"))
         {
            
                beatCalculation(del3Y);
                flag1 = false;
                flag3 = false;
                flag2 = true;
                bf1 = true;
                bf3 = true;
                bf2 = false;
              
           

             midpoint = -(25*9);
             clstat.setLead2(del3Y);
             
         }
         else if(clstat.getLeadNo().equals("3"))
         {
            
                
                flag1 = false;
                flag2 = false;
                flag3 = true;
                bf1 = true;
                bf2 = true;
                bf3 = false;
                
             

             midpoint = -(25*3);
             clstat.setLead3(del3Y);
            
         }


        if(isLocal1 && bf1)
       {
           stroke(128,0,64);
            //fill(255,255,255,255);
            smooth();
           /*
            * weight of line
            */
            strokeWeight(1);

            localXprev = 0;
           for (int i = 0; i < del3Y.length; i++)
           {
               int xto = (int) (i * del);
                float Gain =  clstat.getGain();
               
                int yto = localBuffer1[i] ;
               
                yto = height-yto;
               
                line(localXprev,(localYprev)+(-(25*15)), xto,(yto)+(-(25*15)));
               


                localXprev = xto;
                localYprev = yto;
           }
       }

       if(isLocal2 && bf2)
       {
           stroke(128,0,64);
            //fill(255,255,255,255);
            smooth();
           /*
            * weight of line
            */
            strokeWeight(1);

            localXprev = 0;
           for (int i = 0; i < del3Y.length; i++)
           {
               int xto = (int) (i * del);
                float Gain =  clstat.getGain();
               
                int yto = localBuffer2[i] ;
               
                yto = height-yto;
              
                line(localXprev,(localYprev)+(-(25*9)), xto,(yto)+(-(25*9)));


                localXprev = xto;
                localYprev = yto;
           }
       }
        if(isLocal3 && bf3)
       {
           stroke(128,0,64);
            //fill(255,255,255,255);
            smooth();
           /*
            * weight of line
            */
            strokeWeight(1);

            localXprev = 0;
           for (int i = 0; i < del3Y.length; i++)
           {
               int xto = (int) (i * del);
                //float Gain =  clstat.getGain();
                //int yto = height-yval;
                int yto = localBuffer3[i] ;
                //yto = (int) (yto * localGain);
                yto = height-yto;
                //int yto = (int) (((ecgrecord[i]+ ecgrecord[i+1])/2) * 0.8);
                //int yto = (int) (ecgrecord[i] * 0.8);
                /*
                 * Draw the line
                 */
                //line(xprev,yprev, xto,yto);
                //System.out.println((offset*Gain) - yval);
                

                //line(localXprev,((offset* 1.0f)-localYprev)+(-50), xto,((offset* 1.0f)-yto)+(-50));
                line(localXprev,(localYprev)+(-(25*3)), xto,(yto)+(-(25*3)));


                localXprev = xto;
                localYprev = yto;
           }
       }

       stroke(128,0,64);
       smooth();
       /*
        * weight of line
        */
       strokeWeight(1);




       
       xprev = 0;


         
         //midpoint = 172;
//JOptionPane.showMessageDialog(null, "Helllooooo =" + val.length);
       

         

         for(int i = 0; i < del3Y.length ; i++)
         {
                //yval =  (int) map(ecgrecord[i], 0, 255, 0, height);


                if(flag1)
                {
                    localBuffer1[i] = del3Y[i];
                    isLocal1 = true;
                    selectLead[1]=  0x01;
                }
                else if(flag2)
                {
                    localBuffer2[i] = del3Y[i];
                    isLocal2 = true;
                    selectLead[1]=  0x02;
                }
                else if(flag3)
                {
                    localBuffer3[i] = del3Y[i];
                    isLocal3 = true;
                    selectLead[1]=  0x03;
                }
                int xto = (int) (i * del);
                float Gain =  clstat.getGain();
                //int yto = height-yval;
                //int yto = val[i] ;
                int yto = 0 ;
                yto = (int)(height-del3Y[i]);
               
                line(xprev,(yprev)+(midpoint), xto,(yto)+(midpoint));


                xprev = xto;
                yprev = yto;
                

        }
         //del3Y = null;
         



    }
      





    /*
 * setupt method of this PApplet
 */
public void setup () {
      /*
       *set the window size:
       */
      //size(774, 664);
     size(501, 451);
      ecgrecord = new int[resolution];

      smooth();
      //

      //hint(ENABLE_DEPTH_SORT);
      //img = loadImage("ecggrid14x12.jpg");

      //hint(DISABLE_DEPTH_TEST);
      //background(img);
      //hint(ENABLE_DEPTH_TEST);

      
      

      //del = width*1.0/resolution;
      del = 1;
      iPos = 0;


      

      /*
       * Color of the line
       */
       stroke(128,0,64);
       smooth();
       /*
        * weight of line
        */
       strokeWeight(1);

       background(255,255,240);
       strokeWeight( 0.0f);
       stroke(157,157,255);
       smooth();
       /*
        * weight of line
        */
        for(int i = 0; i <= width; i+=5)
        {
            line(i,0,i,height);
        }
       for(int i = 0; i <= height; i+=5)
        {
            line(0,i,width,i);
        }
       strokeWeight( 0.4f);
       stroke(1,7,254);
       smooth();
       /*
        * weight of line
        */
        for(int i = 0; i <= width; i+=25)
        {
            line(i,0,i,height);
        }
       for(int i = 0; i <= height; i+=25)
        {
            line(0,i,width,i);
        }
}
/*
 * draw method of this PApplet
 */
public void draw () {





  }

    
}
