/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package display;

/**
 *
 * @author Aumi
 */
//import LeadDisplay.*;
//import Timer.StopWatch;
//import ecgbmptversion15.*;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jivesoftware.smack.XMPPException;
import processing.core.*;
//import Timer.StopWatch;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;

/*
 * This PApplet (Processing applet) gets data from GetData.java and draws graph
 */


public class EcgGraph extends PApplet //implements Runnable{
{
        /*
         * Saves the value of data
         */
        public static int yval;
        /*
         * previous value of x and y
         */
        private int xprev = 0;
        private int yprev = 0;
        private int offset = 127;

        private int xto, yto;
        /*
         * horizontal position of the graph
         */
        //private int xPos = 1;
        /*
         * position in recorded ecg
         */
        private int iPos;
        private int ecgrecord[];
        //private static int recordBuffer[] = new int[4096];
        private int recordBuffer[] = new int[4096];
        //private static int iirRecordBuffer[] = new int[4096];
        private int iirRecordBuffer[] = new int[4096];
        //private static int bufferPos = 0;
        private int bufferPos = 0;
        
        private static boolean flag = true;
        /*
         * Screen resolution
         */
        private int resolution;
        /*
         * unit size along x
         */
        private double del;
        
        private int horScale = 0;

        /*
         * EcgRecordGraph object
         */
        EcgRecordGraph ecgR;
        LeadDisplay1 ld1;
        LeadDisplay2 ld2;
        LeadDisplay3 ld3 ;
        LeadDisplay4 ld4 ;
        LeadDisplayExtended2 lde2 ;
//        LeadDisplay5 ld5 ;
//        LeadDisplay6 ld6 ;
//        LeadDisplay7 ld7 ;
//        LeadDisplay8 ld8 ;
//        LeadDisplay9 ld9 ;
//        LeadDisplay10 ld10 ;
//        LeadDisplay11 ld11 ;
//        LeadDisplay12 ld12 ;
         //EcgRecordGraph2 ecgR2;
         ClientStat clstat;
         //StopWatch s = new StopWatch();
         private static boolean startstop = false;
         private static boolean stopWatch = true;

         private static int isAvailable = 0;

         public void startstop(boolean flag)
        {
                startstop = flag;

         }

        public EcgGraph(EcgRecordGraph ecgr, int res, ClientStat cl, LeadDisplay1 l1, LeadDisplay2 l2, LeadDisplay3 l3, LeadDisplay4 l4, LeadDisplayExtended2 le2)//, LeadDisplay5 l5, LeadDisplay6 l6, LeadDisplay7 l7, LeadDisplay8 l8, LeadDisplay9 l9, LeadDisplay10 l10, LeadDisplay11 l11, LeadDisplay12 l12)
         {
             ecgR = ecgr;
             //ecgR2 = ecgr2;
             resolution = res;
             clstat = cl;
             ld1 = l1;ld2 = l2;ld3 = l3;ld4 = l4;lde2 = le2;
//             ld5 = l5;ld6 = l6;ld7 = l7;ld8 = l8;
//             ld9 = l9;ld10 = l10;ld11 = l11;ld12 = l12;
         }
//         public EcgGraph()
//         {
//
//         }

         public void clearBuffer()
         {
             bufferPos = 0;
         }


    /*
     * Get data from GetData.java
     */
public void setValue(int data)
{

        yval = data;


//        if(!clstat.gethorScalling())
//        {
//            horScale = 2048;
//            //recordBuffer = new int[2048];
//        }
//        else
//        {
//            horScale = 4096;
//            //recordBuffer = new int[4096];
//        }
        //System.out.println("B" + data);
        

//        System.out.println("DataFlag = " + clstat.getAge());
//        else
//        {
           if((bufferPos<horScale))//)&&(flag))
           {
               
               
              // if(flag) 
               //{
                   recordBuffer[bufferPos] = data;
                   bufferPos++;
               //}
                //System.out.println("Buffeeeeeeeeeeeeeeeeeeeerrrrrrrrrr [ = "+bufferPos+ "]"  + recordBuffer[bufferPos]);
                
           }
           else
        {

            //flag = false ;
            //JOptionPane.showMessageDialog(null, "" + bufferPos);

            bufferPos = 0;
            iirRecordBuffer[0] = 0;
            float alfa = 0.9999999999999f;
            //double alfa = 0.99f;
            //double alfa = 1.0;
            //float alfa = 1.1f;
            int i;
             
//            for(i = 0; i < recordBuffer.length-1; i++)
//            {
//               //iirRecordBuffer[i] = (int) ((0.996f * iirRecordBuffer[i-1]) + (0.996f*recordBuffer[i]) - (0.996 * recordBuffer[i-1])); 
//               // iirRecordBuffer[i] = (int) ((alfa * iirRecordBuffer[i-1]) + (alfa*recordBuffer[i]) - (alfa * recordBuffer[i-1]));
//                iirRecordBuffer[i+1] = (int) (((alfa *recordBuffer[i+1]) - (alfa *recordBuffer[i]))+(alfa * iirRecordBuffer[i]));
//                              //recordBuffer[i-1] = 0;
//               System.out.println("Filtered Data= " + recordBuffer[i]);
//            }
            
//            for(i = 0; i < recordBuffer.length-1; i++)
//            {
//                iirRecordBuffer[i]=(int)(1.75f*iirRecordBuffer[i]);
//            }
            
//            for(int j = 0; j < recordBuffer.length; j++)
//            {
//                recordBuffer[j] = 0;
//            }
            //flag = true;
            
            //recordBuffer[i] = 0;
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + clstat.getLeadNo());
            
            if(clstat.getLeadNo() == "Lead I" || clstat.getLeadNo() == "Lead II" || clstat.getLeadNo() == "Lead III")
            {    try {
                ld1.setValue(recordBuffer);//ld1.setValue(iirRecordBuffer);
            } catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }//ld1.setValue(iirRecordBuffer);
            }
            else if(clstat.getLeadNo() == "Lead aVR" || clstat.getLeadNo() == "Lead aVL" || clstat.getLeadNo() == "Lead aVF")
            {   try {
                ld2.setValue(recordBuffer);
            } catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            else if(clstat.getLeadNo() == "Lead V1" || clstat.getLeadNo() == "Lead V2" || clstat.getLeadNo() == "Lead V3")
            {    try {
                ld3.setValue(recordBuffer);
            } catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            else if(clstat.getLeadNo() == "Lead V4" || clstat.getLeadNo() == "Lead V5" || clstat.getLeadNo() == "Lead V6")
            {    try {
                ld4.setValue(recordBuffer);

            //recordBuffer = null;
            }
            
                catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            else if(clstat.getLeadNo() == "Extended Lead II")
            {
                 try {
                     
                lde2.setValue(recordBuffer);
                

            //recordBuffer = null;
            }
            
                catch (XMPPException ex) {
                Logger.getLogger(EcgGraph.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
           
        }

        isAvailable = 1;
}

/*
 * setupt method of this PApplet
 */
public void setup () {
      /*
       *set the window size:
       */
      size(541, 121);

      ecgrecord = new int[resolution];
      del = width*1.0/resolution;
      iPos = 0;

      /*
       * Color of the line
       */
       //stroke(128,0,64);
       //smooth();
       /*
        * weight of line
        */
       //strokeWeight(1);

       background(255,255,240);
}
/*
 * draw method of this PApplet
 */
public void draw () {


        /*
         *
         * Only Draw graph when start button is pressed
         */
        

            //recordBuffer = null;


        }
//        if(startstop)
//        {
//
//
//            if(iPos==resolution){
//                 iPos = 0;
//                 xprev = 0;
//                 background(255,255,240);
//                 //s.stop();
//                 //ecgR.setValue(ecgrecord,5000);
////                 if(clstat.getLeadNo() == "Lead I")
////                    ld1.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead II")
////                    ld2.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead III")
////                    ld3.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead aVR")
////                    ld4.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead aVL")
////                    ld5.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead aVF")
////                    ld6.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead V1")
////                    ld7.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead V2")
////                    ld8.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead V3")
////                    ld9.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead V4")
////                    ld10.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead V5")
////                    ld11.setValue(ecgrecord);
////                 else if(clstat.getLeadNo() == "Lead V6")
////                    ld12.setValue(ecgrecord);
//
//
//                 //ecgR2.setValue(ecgrecord);
//
//                 stopWatch = true;
//             }
//
////            if(available){
//
//            /*
//             *
//             * Plot data when data is avialable
//             */
//            if(isAvailable == 1){
//
//                /*
//                 * Start counting time (useful for bitrate calculation)
//                 */
//                if(stopWatch)
//                   // s.start();
//
//               stopWatch = false;
//               /**
//                * Save data to a buffer for sending to EcgRecordGraph
//                */
//               ecgrecord[iPos] = yval;
//
//
//                /*
//                 *My range of data is 0 to 255, so here we convert our data value to 0 to Screen Height
//                 */
//                //yval =  (int) map(yval, 0, 255, 0, height);
//                //System.out.println("Yvaaaaaaaaaaaaaaallllll = "+ yval);
//
//
//                 /*
//                  * value of Xnew and Ynew for line
//                  */
//                 xto = (int) (iPos * del);
//                 //yto = height-yval;
//                 //yto = (int) (yval * clstat.getGain());
//                 yto = (int) (yval * 0.6);
//                 //System.out.println("Yvaaaaaaaaaaaaaaallllll = "+ (height/7)*3);
//                /*
//                 * Draw the line
//                 */
//                 /**
//                  * Draw  a straight line in front of the graph
//                  */
//                 //smooth();
//                 stroke(0);
//                 strokeWeight((float) 0.5);
//                 line(xto,0, xto,255);
//                 strokeWeight(1.3f);
//                 stroke(255,255,240);
//                 line(xprev,0, xprev,255);
//                 /**
//                  * Draw ecg values
//                  */
//                 stroke(128,0,64);
//                 strokeWeight(1.2f);
//                 //line(xprev,yprev, xto,yto);
//                 //JOptionPane.showMessageDialog(null, ""+ 60*clstat.getGain());
//                 //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA"+ 60*clstat.getGain());
//                 line(xprev,((offset* 0.6f)-yprev)+((height/2)), xto,((offset* 0.6f)-yto)+((height/2)));
//                 iPos++;
//
//                 xprev = xto;
//                 yprev = yto;
//
//                 isAvailable = 0;
//
//            }



//        }
    


}
