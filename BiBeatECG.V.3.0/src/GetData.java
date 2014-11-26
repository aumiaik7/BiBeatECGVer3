/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package ecgbmptversion15;

/**
 *
 * @author Aumi
 */
//import Client.Client;
//import Timer.StopWatch;
//import display.ClientStat;
//import display.EcgGraph;

/**
 *
 * @author Aumi
 */
public class GetData extends Thread{

    Synchronization sync;
    private byte[] data;
    EcgGraph ecg ;
    LeadDisplay5 ld5;
    //Client cl;
    ClientStat clstat;
    
    private  boolean killThread = false;
    private static boolean stop = false;
    //StopWatch st;
    //Client cl;

    int avgData = 0;
    double[] xv = new double[9];
    double[] yv = new double[9];
    double[] xV = new double[9];
    double[] yV = new double[9];
    
    int[] bufferNF = new int[8];
    int[] bufferF = new int[8];
    
    double[] Inv = new double[9];
    double[] Outv = new double[9];
    
    int[] ReportBufferNF = new int[8];
    int[] ReportBufferHP = new int[8];
    int[] ReportBufferF = new int[8];
    
    private int plotVal;
    //float gain = 0.7f;
    private int plotVal2;
    
    private static int position = 0;
    private int[] storedDataFor15 = new int[13]; 
    private int[] storedDataFor20 = new int[10]; 

    /*
     * Constructor
     */
    public GetData(Synchronization s, LeadDisplay5 l5, ClientStat cs, EcgGraph eg){

        sync = s;
        ld5 = l5;
        clstat = cs;
        ecg = eg;
        //st = sw;
    }
    public void activateStop(boolean startStop)
    {
            stop = startStop;
           
    }
     
    public void killThread()
    {
        killThread = true;
    }

     public void setDatafor15(int data)
     {
         if(position == 13)
         {
             position = 0;
             int avgData = (storedDataFor15[0]+storedDataFor15[1]+storedDataFor15[2]+storedDataFor15[3]+storedDataFor15[4]+storedDataFor15[5]+
                     storedDataFor15[6]+storedDataFor15[7]+storedDataFor15[8]+storedDataFor15[9]+storedDataFor15[10]+storedDataFor15[11]+storedDataFor15[12])/13;
             ld5.setValue((int)(avgData*clstat.getGain()));
                     
         }
         storedDataFor15[position] = data;
         position++;
     }
      public void setDatafor20(int data)
     {
         if(position == 10)
         {
             position = 0;
             int avgData = (storedDataFor15[0]+storedDataFor15[1]+storedDataFor15[2]+storedDataFor15[3]+storedDataFor15[4]+storedDataFor15[5]+
                     storedDataFor15[6]+storedDataFor15[7]+storedDataFor15[8]+storedDataFor15[9])/10;
             ld5.setValue((int)(avgData*clstat.getGain()));
                     
         }
         storedDataFor15[position] = data;
         position++;
     }
    public void run()
    {

      
            while(!killThread)
            {
                 if(!stop)
                 {
                /*
                 * Get data form Synchronization.java(Get method)
                 */
                data = sync.get();

                avgData = 0;
                //bufferNF[0] = data[0] & 0xff ;
                ReportBufferNF[0] = data[1] & 0xff ;
                ReportBufferNF[1] = data[2] & 0xff ;
                ReportBufferNF[2] = data[3] & 0xff ;
                ReportBufferNF[3] = data[4] & 0xff ;
                ReportBufferNF[4] = data[5] & 0xff ;
                ReportBufferNF[5] = data[6] & 0xff ;
                ReportBufferNF[6] = data[7] & 0xff ;
                ReportBufferNF[7] = data[8] & 0xff ;



                 for (int i = 0; i < 8; i++)
                        {

                            Inv[0] = Inv[1]; Inv[1] = Inv[2];
                            Inv[2] = ReportBufferNF[i] / 1.000166622e+00;
                            Outv[0] = Outv[1]; Outv[1] = Outv[2];
                            Outv[2] = (Inv[0] + Inv[2]) - 2 * Inv[1]
                                         + (-0.9996668393 * Outv[0]) + (1.9996667838 * Outv[1]);
                            ReportBufferHP[i] = (int)Outv[2];
    //                        if(!clstat.getFilterFlag())
    //                        {
    //                            ecg.setValue((int)(ReportBufferHP[i]*clstat.getGain()));
    //                        }
                        }

                 if(clstat.getFilterFlag())
                 {
                            for (int i = 0; i < 8; i++)
                            {

                                xv[0] = xv[1];
                                xv[1] = xv[2];
                                xv[2] = xv[3];
                                xv[3] = xv[4];
                                xv[4] = xv[5];
                                xv[5] = xv[6];
                                xv[6] = xv[7];
                                xv[7] = xv[8];
                                xv[8] = ReportBufferHP[i] / 1.041903016e+00;
                                yv[0] = yv[1];
                                yv[1] = yv[2];
                                yv[2] = yv[3];
                                yv[3] = yv[4];
                                yv[4] = yv[5];
                                yv[5] = yv[6];
                                yv[6] = yv[7];
                                yv[7] = yv[8];
                                yv[8] = (xv[0] + xv[8]) - 7.5696167154 * (xv[1] + xv[7]) + 25.4871614570 * (xv[2] + xv[6])
                                             - 49.8171129020 * (xv[3] + xv[5]) + 61.7992703440 * xv[4]
                                             + (-0.9211819292 * yv[0]) + (7.0445476258 * yv[1])
                                             + (-23.9629515810 * yv[2]) + (47.3198842340 * yv[3])
                                             + (-59.3064324560 * yv[4]) + (48.3011538330 * yv[5])
                                             + (-24.9670893260 * yv[6]) + (7.4919409662 * yv[7]);
                                ReportBufferF[i] = (int)yv[8];

    //                             ecg.setValue((int)(ReportBufferF[i]*clstat.getGain()));
                            }
                 }

                        if(!clstat.getFilterFlag() && clstat.gethorScalling()==1)
                        {    
                            //plotVal = (int)(ReportBufferHP[0] + ReportBufferHP[1] + ReportBufferHP[2] + ReportBufferHP[3] + ReportBufferHP[4] + ReportBufferHP[5] + ReportBufferHP[6] + ReportBufferHP[7]) / 8;
                            for(int i = 0 ; i < 8; i++ )
                            {
                                setDatafor15(ReportBufferHP[i]);
                            }
                        } 
                        else if(!clstat.getFilterFlag() && clstat.gethorScalling()==2)
                        {    
                            //plotVal = (int)(ReportBufferHP[0] + ReportBufferHP[1] + ReportBufferHP[2] + ReportBufferHP[3] + ReportBufferHP[4] + ReportBufferHP[5] + ReportBufferHP[6] + ReportBufferHP[7]) / 8;
                            for(int i = 0 ; i < 8; i++ )
                            {
                                setDatafor20(ReportBufferHP[i]);
                            }
                        }
                        else if(!clstat.getFilterFlag() && clstat.gethorScalling()==3)
                        {    
                            plotVal = (int)(ReportBufferHP[0] + ReportBufferHP[1] + ReportBufferHP[2] + ReportBufferHP[3] + ReportBufferHP[4] + ReportBufferHP[5] + ReportBufferHP[6] + ReportBufferHP[7]) / 8;
                             ld5.setValue((int)(plotVal*clstat.getGain()));
                        }
                        else if(!clstat.getFilterFlag() && clstat.gethorScalling()==4)
                        {    
                            //plotVal = (int)(ReportBufferHP[0] + ReportBufferHP[1] + ReportBufferHP[2] + ReportBufferHP[3] + ReportBufferHP[4] + ReportBufferHP[5] + ReportBufferHP[6] + ReportBufferHP[7]) / 8;
                            plotVal = (int)(ReportBufferHP[0] + ReportBufferHP[1] + ReportBufferHP[2] + ReportBufferHP[3])  / 4;
                            plotVal2 = (int)(ReportBufferHP[4] + ReportBufferHP[5] + ReportBufferHP[6] + ReportBufferHP[7]) / 4;
                            ld5.setValue((int)(plotVal*clstat.getGain()));
                            ld5.setValue((int)(plotVal2*clstat.getGain()));
                        }



                        else if(clstat.getFilterFlag() && clstat.gethorScalling()==1)
                        {    
                            //plotVal = (int)(ReportBufferHP[0] + ReportBufferHP[1] + ReportBufferHP[2] + ReportBufferHP[3] + ReportBufferHP[4] + ReportBufferHP[5] + ReportBufferHP[6] + ReportBufferHP[7]) / 8;
                            for(int i = 0 ; i < 8; i++ )
                            {
                                setDatafor15(ReportBufferF[i]);
                            }
                        } 
                        else if(clstat.getFilterFlag() && clstat.gethorScalling()==2)
                        {    
                            //plotVal = (int)(ReportBufferHP[0] + ReportBufferHP[1] + ReportBufferHP[2] + ReportBufferHP[3] + ReportBufferHP[4] + ReportBufferHP[5] + ReportBufferHP[6] + ReportBufferHP[7]) / 8;
                            for(int i = 0 ; i < 8; i++ )
                            {
                                setDatafor20(ReportBufferF[i]);
                            }
                        }       
                        else if(clstat.getFilterFlag() && clstat.gethorScalling()==3)
                        {
                            plotVal = (int)(ReportBufferF[0] + ReportBufferF[1] + ReportBufferF[2] + ReportBufferF[3] + ReportBufferF[4] + ReportBufferF[5] + ReportBufferF[6] + ReportBufferF[7]) / 8;
                             ld5.setValue((int)(plotVal*clstat.getGain()));

                        }
                        else if(clstat.getFilterFlag() && clstat.gethorScalling()==4)
                        {    
                            //plotVal = (int)(ReportBufferHP[0] + ReportBufferHP[1] + ReportBufferHP[2] + ReportBufferHP[3] + ReportBufferHP[4] + ReportBufferHP[5] + ReportBufferHP[6] + ReportBufferHP[7]) / 8;
                            plotVal = (int)(ReportBufferF[0] + ReportBufferF[1] + ReportBufferF[2] + ReportBufferF[3])  / 4;
                            plotVal2 = (int)(ReportBufferF[4] + ReportBufferF[5] + ReportBufferF[6] + ReportBufferF[7]) / 4;
                            ld5.setValue((int)(plotVal*clstat.getGain()));
                            ld5.setValue((int)(plotVal2*clstat.getGain()));
                        }

                        //System.out.println("Avggggggggggggggggggggggggggggggggggggggggggggggggggggggg = " + plotVal);

    //                    if(clstat.gethorScalling()==3)
    //                    {
    //                    
    //                        ld5.setValue((int)(plotVal*clstat.getGain()));
    //                    }
    //                    else if(clstat.gethorScalling()==4)
    //                    {
    //                    
    //                        ld5.setValue((int)(plotVal*clstat.getGain()));
    //                        ld5.setValue((int)(plotVal2*clstat.getGain()));
    //                    }

                            /*
                 * Send average of 8 bytes to EcgGraph.java (SetValue method)
                 */
    //            avgData /=8;
    //            ecg.setValue(avgData);
    //                ecg.setValue((setData1 + setData2)/4);
    //                ecg.setValue((setData3 + setData4)/4);
    //                ecg.setValue((setData3)/2);
    //                ecg.setValue((setData4)/2);



            }
        }
    }

}
