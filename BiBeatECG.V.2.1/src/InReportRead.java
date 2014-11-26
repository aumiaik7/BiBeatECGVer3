/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package ecgbmptversion15;

/**
 *
 * @author Aumi
 */
//import Timer.StopWatch;
import javax.swing.JOptionPane;

/**
 *
 * @author Aumi
 */
public class InReportRead extends Thread{

  
    Synchronization sync;
    MyHID myHID ;
    StopWatch stopwatch;
    private boolean killThread = false;
    
    private static boolean stop = false;

    /*
     * Constructor
     */
    public InReportRead(Synchronization s,MyHID mh,StopWatch sw)
    {
        sync = s;
        myHID = mh;
        stopwatch = sw;

       
       
//        if (myHID.FindTheHID(5824, 1503))
//		System.out.println("Device found");


     
    }
      public void activateStop(boolean startStop)
    {
            stop = startStop;
            
    }
      
    public void killThread()
    {
        killThread = true;
    }
            
    /*
     * Run method of this thread
     */
    public void run()
    {
         
        
       byte buf[] = new byte[myHID.ReadInputReportSize()];
       //System.out.println("Report Length = " + myHID.ReadInputReportSize());

        //stopwatch.start();
        while(!killThread)
        {
            if(!stop)
            {
            buf = myHID.IntReadInputReport();
            //System.out.println("Elasped Time = " + stopwatch.getElapsedTime());
            //System.out.println("Buffer = "+ buf[2]);
            sync.put(buf);
            }
        }

    }
}



