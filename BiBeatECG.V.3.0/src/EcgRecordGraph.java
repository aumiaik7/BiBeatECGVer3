/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package display;

/**
 *
 * @author Aumi
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import processing.core.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import java.net.*;
import java.io.*;

public class EcgRecordGraph extends PApplet{

     private int ecgrecord1[];
     private int ecgrecord2[];
     /*
      * Saves the value of data
      */
      public static int yval, yval1;
     /*
      * previous value of x and y
      */
      private int xprev = 0;
      private int yprev = 0;

      private static int resolution;
      private double del;
      private int iPos;
      private static boolean isTrue = false; //When true send data to next graph

      //EcgRecordGraph2 Ecgr2;
      EcgDisplay Disp;
      BeatRate Beat;
      ClientStat clstat;

      //beat calculation variables
      private double lastbeats[];
      private int windowsize = 20;
      private int skip = 4;
      private int noiseoffset = 5;
      private double timeInterval ;
      private boolean isUp; //for rate calculation
      double res;
      private static boolean isRes = true;
      private static int[] xBeat = new int[50];
      private static int[] yDel;
      private static int[] y2Del;
      private static int[] avgyDel;


      /***
       * Data is saved in this text file which is sent to remote PC
       */
      //private static String filepath = "c:/ECGtelemedicine.txt";

      /***
       * File writer
       */
      //FileWriter outputStream = null;

      /**
       * Constructor
       * @param ecgr2
       * @param res
       * @param b
       * @param cs
       */
      EcgRecordGraph(ClientStat cs) {

            //resolution = res;
            //Beat = b;
            clstat = cs;

        }



    /**
     *
     *
     * @param val
     * @param t
     */
    public void setValue(int[] val)
    {
       
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
       
       
       
        int sumyDel = 0;
        int avgyDel = 0;
        for (int i = 0; i< val.length - 1; i++)
        {
                //yval =  (int) map(ecgrecord1[i], 0, 255, 0, height);
                //yval1 =  (int) map(ecgrecord1[i+1], 0, 255, 0, height);
                yDel[i] = val[i+1] - val[i];
                sumyDel += yDel[i];

        }
        avgyDel = sumyDel / (val.length - 1);
//        int mapAvg =  (int) map(avgyDel, 0, 255, 0, height);
//        line(0, avgyDel, width, avgyDel);




        int upBeatcount = 0;
        int downBeatcount = 0;
        int beatBuffer[] = new int[50];
        boolean up = false;
        boolean pos = false;
        int fBeat = 0;
        int lBeat = 0;
        int yMax = 0;
        int avgX = 0;
        int sumxBit = 0;
        double rr = 0;
        for(int i = 0; i< val.length-1; i++)
        {


              if(yDel[i] > avgyDel + 15 && up == false)
              {
                    if(yMax < (yDel[i+1] - yDel[i]))
                    {
                        yMax = yDel[i];
                        //up = true;
                    }
                    else
                    {
                        //yMax = 0;
                        up = true;
                        //break;
                    }
                    //up = false;
              }
              else if(up)
              {
                    beatBuffer[upBeatcount] = i;
                    if(!pos)
                    {
                        fBeat = i;
                        pos = true;
                    }
                    else
                    {

                        sumxBit += i - fBeat;
                        fBeat = i;
                    }
                    stroke(37, 177, 76);
                    line((int) (i * del), 0, (int) (i * del) , height);
                    upBeatcount++;
                    i += 5;
                    up = false;
              }
}
        stroke(128,0,64);
       smooth();
       /*
        * weight of line
        */
        xprev = 0;
        strokeWeight(1.2f);
         for(int i = 0;i<512;i++)
         {
                int xto = iPos;
                //float Gain =  clstat.getGain();
                
                // yval =  (int) map(yval, 0, 255, 0, height);

                
                int yto = height-yDel[i];
                /*
                 * Draw the line
                 */
                //line(xprev,yprev, xto,yto);
                //line(xprev,((60*clstat.getGain())-yprev)+((height/7)*4), xto,(60*clstat.getGain()-yto)+((height/7)*4));
                stroke(128,0,64);
                strokeWeight(1.2f);
                smooth();
                line(xprev,yprev-90, xto,yto-90);

                xprev = xto;
                yprev = yto;
                iPos++;
                //ecgrecord2[i] = ecgrecord1[i];

//            try {
//                outputStream.write(ecgrecord1[i]+"\n");
//            }
//            catch (IOException ex) {
//                Logger.getLogger(EcgRecordGraph.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }

        try{
         avgX = sumxBit/(upBeatcount-1);
     }
     catch(ArithmeticException e)
     {
     }
        //JOptionPane.showMessageDialog(null, upBeatcount + "avgX" + avgX + "TimeInt" + timeInterval);

    double avgT = (timeInterval/resolution)*avgX;

//   double rr = (beatcount-1) * 60*1000.0 /((lbeat-fbeat)*timeInterval);
     //JOptionPane.showMessageDialog(null, "Tiem int =" + timeInterval);
     //double rr = beatcount * (60/timeInterval);
    if(upBeatcount>0)
    {
     rr = 60/avgT;
     }
    //JOptionPane.showMessageDialog(null, "Beat Count =" + beatcount + "lbeat = " +lbeat+ "fbeat = "+ fbeat);
    //double rr = (beatcount-1) * 60*1000.0 /((resolution)*timeInterval);
    //res is the heart rate;
   if(isRes)
   {
       //Beat.setBeat((int)rr);
       //JOptionPane.showMessageDialog(null, ""+rr);
       res = rr;
       isRes = false;
   }
 else
   {
       res = (res + rr) / 2;
       System.out.println("heart rate = "+(int)res+" beats/minute");

//    if(res<0)
//    Beat.setBeat(0);
//    else
      //Beat.setBeat((int)res);
   }

//     try {
//
//            outputStream.write((int)res + "\n");
//
//        }
//        catch (IOException ex) {
//            Logger.getLogger(EcgRecordGraph.class.getName()).log(Level.SEVERE, null, ex);
//        }
//     try {
//            outputStream.flush();
//            outputStream.close();
//            //lastvals = val;
//        } catch (IOException ex) {
//            Logger.getLogger(EcgRecordGraph.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        for(int i = 0; i< val.length-1; i++)
//        {
//
//
//              if(yDel[i] < avgyDel - 15 && up == false)
//              {
//                    if(yMax > (yDel[i+1] - yDel[i]))
//                    {
//                        yMax = yDel[i];
//                        //up = true;
//                    }
//                    else
//                    {
//                        //yMax = 0;
//                        up = true;
//                        //break;
//                    }
//                    //up = false;
//              }
//              else if(up)
//              {
//                    beatBuffer[upBeatcount] = i;
//                    stroke(7, 7, 76);
//                    line((int) (i * del), 0, (int) (i * del) , height);
//                    upBeatcount++;
//                    i += 5;
//                    up = false;
//              }
////            if( yDel[i] > avgyDel + 10 && up == false)
////            {
////                upBeatcount++;
////                up = true;
////            }
////            else if (yDel[i] < avgyDel && up == true)
////            {
////                up = false;
////            }
////            else if (yDel[i] < avgyDel-10 && up == false)
////            {
////                downBeatcount++;
////                up = true;
////            }
//
//
//        }


//        for (int i = 0; i< val.length - 1; i++)
//        {
//
//                y2Del[i] = yDel[i+1] - yDel[i];
//
//        }








         /**
          * Send The buffer to next record display
          *
          */

         /**********
     *
     *
     *
     *
     *
     *
     *
     */
//        int sumw = 0;
//        double avw = 0;
//        double stdw = 0;
//        double ts = 0;
//        int sum=0;
//        double avg=0;

//        for(int i = 0; i <val.length-windowsize ; i++){
//            sumw = 0;
//            avw = 0;
//            stdw = 0;
//            for(int j = 0;j<windowsize;j++){
//                sumw+=val[i+j];
//            }
//            avw = sumw*1.0/windowsize;
//            for(int j=0;j<windowsize;j++){
//                ts = val[i+j]-avw;
//                stdw += ts*ts;
//            }
//
//           lastbeats[i] =Math.sqrt(stdw);
//           System.out.println("i = "+ i + "   LastBeats[i]" + lastbeats[i]);
//            sum += lastbeats[i];
//        }
//        avg = sum*1.0/(val.length-windowsize);
//        JOptionPane.showMessageDialog(null, avg);
//            sumw = 0;
//            avw = 0;
//            stdw = 0;
//        for(int j = 0;j<val.length;j++){
//                sumw+=val[j];
//            }
//            avw = sumw*1.0/val.length;
//
//
//
//            for(int j=0;j<val.length;j++){
//                ts = val[j]-avw;
//                stdw = ts*ts;
//                lastbeats[j] =Math.sqrt(stdw);
//                System.out.println("i = "+ j + "   LastBeats[i]" + lastbeats[j]);
//                sum += lastbeats[j];
//            }
//
//
//
//        avg = sum*1.0/(val.length);
//        JOptionPane.showMessageDialog(null, avg);
//        int ymax = 0;
//        int delY = 0;
//        int beatPos[] = new int[50];
//        int beatMax[] = new int[50];
//        int beatCnt = 0;
//        boolean beat = false;
//         for(int i=2;i<lastbeats.length;i++){
//
//
//             if(lastbeats[i]>avg + 30 )
//             {
//                //delY = (int) (lastbeats[i] - lastbeats[i - 1]);
//                if(lastbeats[i]>lastbeats[i-1])
//                {
//                    ymax = (int) lastbeats[i];
//
//                 }
//
//                beat = true;
//             }
//             else if(beat)
//             {
//                beatPos[beatCnt] = i;
//                beatMax[beatCnt] = ymax;
//                beatCnt++;
//                stroke(37, 177, 76);
//                line((int) (i * del), 0, (int) (i * del) , height);
//                i+=skip;
//
//                //JOptionPane.showMessageDialog(null, "BeatCount position = " + i + "beatMax[beatmax] = " + beatMax[beatCnt-1]);
//                beat = false;
//                ymax = 0;
//
//             }
//
//
//    }
//        JOptionPane.showMessageDialog(null, beatCnt);


        /**********
         *
         *
         *
         *
         *
         *
         */
//     int beatcount = 0;
//     System.out.println("avg heart = "+ avg);
//     //JOptionPane.showMessageDialog(null, "Avg = "+ avg);
//     int fbeat = 0;
//     int lbeat = 0;
//     int sumxBit = 0;
//     int avgX = 0;
//     double rr = 0;
//     boolean isf= true;
//     //JOptionPane.showMessageDialog(null, "LastBeat Length =" + lastbeats.length);
//     for(int i=0;i<lastbeats.length;i++){
//        //printf("%d ",lastbeats[i]);
////        if(isUp ==true && lastbeats[i]>=avg){
////            cout<<"a";
////            System.out.print("a");
////            ;
////        }
//        if(isUp == true && lastbeats[i] <avg){
//            isUp=false;
//            i+=skip;
//            if(i>=lastbeats.length) break;
//        //cout<<"b";
//          //  System.out.print("b");
//        }
////        else if(isUp == false && lastbeats[i] < avg){
////        //cout<<"c";
////        //    System.out.print("c");
////        }
//        else if(isUp == false && lastbeats[i] -avg >= 18){
//
////            xBeat[beatcount] = i;
//            //sumxBit+=i;
//            beatcount++;
//            isUp=true;
//
//            stroke(37, 177, 76);
//            line((int) (i * del), 0, (int) (i * del) , height);
//         //   cout<<"d";
//            System.out.print("d");
//            if(isf==true){
//                fbeat = i;
//                isf= false;
//            }
//            else{
////                lbeat = i;
//                sumxBit += i - fbeat;
//                fbeat = i;
//            }
//            i+=skip;
//            if(i>=lastbeats.length) break;
//
//        }
//       // cout<<"e";
//       // System.out.print("e");
//    }
//
//
//     try{
//         avgX = sumxBit/(beatcount);
//     }
//     catch(ArithmeticException e)
//     {
//     }
//
//    double avgT = (timeInterval/resolution)*avgX;
//
////   double rr = (beatcount-1) * 60*1000.0 /((lbeat-fbeat)*timeInterval);
//     //JOptionPane.showMessageDialog(null, "Tiem int =" + timeInterval);
//     //double rr = beatcount * (60/timeInterval);
//    if(beatcount>0)
//    {
//     rr = 60/avgT;
//     }
//    //JOptionPane.showMessageDialog(null, "Beat Count =" + beatcount + "lbeat = " +lbeat+ "fbeat = "+ fbeat);
//    //double rr = (beatcount-1) * 60*1000.0 /((resolution)*timeInterval);
//    //res is the heart rate;
//   if(isRes)
//   {
//       Beat.setBeat((int)rr);
//       //JOptionPane.showMessageDialog(null, ""+rr);
//       res = rr;
//       isRes = false;
//   }
// else
//   {
//       res = (res + rr) / 2;
//       System.out.println("heart rate = "+(int)res+" beats/minute");
//
////    if(res<0)
////    Beat.setBeat(0);
////    else
//      Beat.setBeat((int)res);
//   }
//
//     try {
//
//            outputStream.write((int)res + "\n");
//
//        }
//        catch (IOException ex) {
//            Logger.getLogger(EcgRecordGraph.class.getName()).log(Level.SEVERE, null, ex);
//        }
//     try {
//            outputStream.flush();
//            outputStream.close();
//            //lastvals = val;
//        } catch (IOException ex) {
//            Logger.getLogger(EcgRecordGraph.class.getName()).log(Level.SEVERE, null, ex);
//        }


    /************
     *
     *
     * Send Data to Server
     *
     *
     */
   

    }
    public int getHeartrate()
    {
        return (int) res;
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
      size(501, 151);

      ecgrecord1 = new int[512];
      yDel = new int[512];
      y2Del = new int[resolution];
      ecgrecord2 = new int[resolution];
      del = width*1.0/resolution;
      iPos = 0;
      lastbeats = new double[resolution];
      isUp = false;




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

//         iPos++;

//        if(iPos==resolution){
//             iPos = 0;
//             xprev = 0;
//             background(255,255,200);
//             //ecgR.setValue(ecgrecord,resolution);
//             //something
//         }
         //ecgrecord2[iPos] = yval;
        /*
         *My range of data is 0 to 255, so here we convert our data value to 0 to Screen Height
         */



  }
}
