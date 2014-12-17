/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package LeadDisplay;

//import display.ClientStat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jivesoftware.smack.XMPPException;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
/**
 *
 * @author Aumi
 */import processing.core.PApplet;


/**
 *
 * @author Abul Bashar
 */

public class LeadDisplay5 extends PApplet{




     /*
      * Saves the value of data
      */
      public int yval;

      private static int[] ecgrecord;
     /*
      * previous value of x and y
      */
      private int xprev = 0;
      private int yprev = 0;
      private int offset = 127;
      private int resolution ;
      private double del;
      private int iPos;
      private boolean isTrue = false;
      ClientStat clstat;
      long start1 ;
      static int[] storedData = new int[512]; 
      static int[] storedRawData = new int[10000];
      int rawDataPosition = 0;
      LeadDisplay1 ld1;
      LeadDisplay2 ld2;
      LeadDisplay3 ld3;
      LeadDisplay4 ld4;
      LeadDisplayExtended2 lde2;
      EcgDisplay ecgD;
      

      //GetSet getset;

    public LeadDisplay5(ClientStat cs, int r, LeadDisplay1 l1, LeadDisplay2 l2, LeadDisplay3 l3, LeadDisplay4 l4,LeadDisplayExtended2 le2, EcgDisplay ecgD) {
        clstat = cs;
        resolution = r;
        ld1 = l1;
        ld2 = l2;
        ld3 = l3;
        ld4 = l4;
        lde2 = le2;
        this.ecgD = ecgD;
    }






    public void reset()
    {
        iPos = 0;
        xprev = 0;
        rawDataPosition = 0;
        background(255,255,240);
    }
    
    private int mean(int[] num) {
        int sum = 0;  // sum of all the elements
        for (int i=0; i<(rawDataPosition/2); i++) {
            sum += num[i];
        }
        return sum / (rawDataPosition/2);
    }
    
    private int standardDeviation(int[] num,int average) {
        int sd = 0;    
        for (int i=0; i<(rawDataPosition/2);i++)
            {
                sd = (int) (sd + Math.abs(num[i] - average));
            
            }
        return sd / (rawDataPosition/2);
    }
    
    private int maximum(int[] num) {
        int max = 0;
        for (int i=0; i<rawDataPosition;i++)
        {
            if(num[i]>max)
                max = num[i];

        }
        return max;
    }
    
    private int minimum(int[] num) {
        int min = 0;
        for (int i=0; i<rawDataPosition;i++)
        {
            if(num[i]<min)
                min = num[i];

        }
        return min;
    }

    public void setValue(int val,int[] rawData)
    {
        
        if(iPos == 0)
        {
           start1 = System.currentTimeMillis();
           //JOptionPane.showMessageDialog(null,"Hello = " + start1);
        }
        
        storedData[iPos] = val;
        
        for(int i = 0; i < rawData.length; i++)
        {
            storedRawData[rawDataPosition] = rawData[i] - 127;
            rawDataPosition++;
        }
        
                 
       
       if(iPos>= width)
       {
           //JOptionPane.showMessageDialog(null,"Hello" + (int)(System.currentTimeMillis() - start1));
           iPos = 0;
          
           xprev = 0;
           int halfRawDataSize = rawDataPosition /2;
           int[] array1 = Arrays.copyOfRange(storedRawData, 0, halfRawDataSize);
           int[] array2 = Arrays.copyOfRange(storedRawData,  halfRawDataSize, rawDataPosition);
           int avg1 = mean(array1);
           int avg2 = mean(array2);
           int sd1  = standardDeviation(storedRawData, avg1);
           int sd2  = standardDeviation(storedRawData, avg2);
           int max = maximum(storedRawData);
           int min = minimum(storedRawData);
           int Max = Math.max(max, Math.abs(min));
           
          
           rawDataPosition = 0;
           ecgD.meanLabel.setText(avg1+" "+avg2);
           ecgD.sdLabel.setText(sd1+" "+sd2);
           ecgD.maxLabel.setText(Max+"");
           
           background(255,255,240);
           //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+ (System.currentTimeMillis()- start1));
            if(clstat.getLeadNo().equals("1") || clstat.getLeadNo().equals("2") || clstat.getLeadNo().equals("3"))
                try {
                ld1.setValue(storedData);//ld1.setValue(iirRecordBuffer);
            } catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }//ld1.setValue(iirRecordBuffer);
            else if(clstat.getLeadNo().equals("4") || clstat.getLeadNo().equals("5") || clstat.getLeadNo().equals("6"))
                try {
                ld2.setValue(storedData);
            } catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            else if(clstat.getLeadNo().equals("7") || clstat.getLeadNo().equals("8") || clstat.getLeadNo().equals("9"))
                try {
                ld3.setValue(storedData);
            } catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            else if(clstat.getLeadNo().equals("10") || clstat.getLeadNo().equals("11") || clstat.getLeadNo().equals("12"))
                try {
                ld4.setValue(storedData);

            //recordBuffer = null;
            } catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            else if(clstat.getLeadNo().equals("13"))
            {
                 try {
                     
                lde2.setValue(storedData);

            //recordBuffer = null;
            } catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }

            //recordBuffer = null;
            }
             if((avg1>=7 && avg1 <=20 ) && (sd1 <=15 && sd1>=4) &&  (Max <100 &&  Max >0) && (avg2>=7 && avg2 <=20 ) && (sd2 <=15 && sd2>=4))
           {
                int j = ecgD.leadComboBox.getSelectedIndex();
                if(j == 13)
                {  
                    j = 1;
                    ecgD.leadComboBox.setSelectedIndex(j);
                   // leadComboBoxActionPerformed(evt);


                }
                else
                {
                    j += 1;
                    ecgD.leadComboBox.setSelectedIndex(j);
                   // leadComboBoxActionPerformed(evt);

                }
           }
//             else
//             {
//                 int j = ecgD.leadComboBox.getSelectedIndex();
//                  ecgD.leadComboBox.setSelectedIndex(j);
//             }
           start1 = System.currentTimeMillis();
           
       }
       
         
//         for(int i = 0;i<ecgrecord.length-3;i++)
//         {
//                //yval =  (int) map(ecgrecord[i], 0, 255, 0, height);

                int xto = iPos;
                //float Gain =  clstat.getGain();
                
                // yval =  (int) map(yval, 0, 255, 0, height);

                
                int yto = height-val;
                
                //smooth();
                stroke(0);
                strokeWeight(0.5f);
                line(xto+1,0, xto+1,height);
                strokeWeight(1f);
                stroke(255,255,240);
                line(xto,0, xto,height);
                
                
                
                stroke(128,0,64);
                strokeWeight(1.2f);
                smooth();
       /*
        * weight of line
        */
       //strokeWeight(1);
                //int yto = (int) (((ecgrecord[i]+ ecgrecord[i+1])/2) * 0.8);
                //int yto = (int) (ecgrecord[i] * 0.8);
                /*
                 * Draw the line
                 */
               line(xprev,yprev-90, xto,yto-90);
                //System.out.println((offset*Gain) - yval);
                //line(xprev,((offset* 0.8f)-yprev)+((height/2)), xto,((offset* 0.8f)-yto)+((height/2)));


                xprev = xto;
                yprev = yto;
                iPos++;

//        }


    }
        //resolution = res;

         //JOptionPane.showMessageDialog(null,"Length="+ecgrecord1.length);






    /*
 * setupt method of this PApplet
 */
public void setup () {
      /*
       *set the window size:
       */
      size(501, 181);
      ecgrecord = new int[resolution];


      //del = width*1.0/resolution;
      del = 1;
      iPos = 0;
      xprev = 0;


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
//        for(int i = 0; i <= width; i+=4)
//        {
//            line(i,0,i,height);
//        }
//       for(int i = 0; i <= height; i+=4)
//        {
//            line(0,i,width,i);
//        }
//       strokeWeight( 0.4f);
//       stroke(1,7,254);
//       smooth();
//       /*
//        * weight of line
//        */
//        for(int i = 0; i <= width; i+=20)
//        {
//            line(i,0,i,height);
//        }
//       for(int i = 0; i <= height; i+=20)
//        {
//            line(0,i,width,i);
//        }
}
/*
 * draw method of this PApplet
 */
public void draw () {




  }

    
}
