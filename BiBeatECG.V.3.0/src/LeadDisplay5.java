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
      LeadDisplay1 ld1;
      LeadDisplay2 ld2;
      LeadDisplay3 ld3;
      LeadDisplay4 ld4;
      LeadDisplayExtended2 lde2;
      

      //GetSet getset;

    public LeadDisplay5(ClientStat cs, int r, LeadDisplay1 l1, LeadDisplay2 l2, LeadDisplay3 l3, LeadDisplay4 l4,LeadDisplayExtended2 le2) {
        clstat = cs;
        resolution = r;
        ld1 = l1;
        ld2 = l2;
        ld3 = l3;
        ld4 = l4;
        lde2 = le2;
    }






    public void reset()
    {
        iPos = 0;
        xprev = 0;
        background(255,255,240);
    }

    public void setValue(int val)
    {
        
        if(iPos == 0)
        {
           start1 = System.currentTimeMillis();
           //JOptionPane.showMessageDialog(null,"Hello = " + start1);
        }
        
        storedData[iPos] = val;
        
                 
       
       if(iPos>= width)
       {
           //JOptionPane.showMessageDialog(null,"Hello" + (int)(System.currentTimeMillis() - start1));
           iPos = 0;
           xprev = 0;
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
