/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package ecgbmptversion15;

/**
 *
 * @author Aumi
 */
public class Synchronization {
     private byte[] content;
    public boolean available = false;


     /*
     * GetData.java Thread gets data from this function
     */
     public synchronized byte[] get(){

        while(available == false)
        {
            try{
                wait();
            }
            catch (InterruptedException e) { }

        }
        /*
         * Data from USB is signed 8 bits. so maximum possible value is 127. But our ECG machine can provide upto 255 so the ANDing is done
         */
        int i = content[1] & 0xff;
        System.out.println("Get = "+ i);
        available = false;
        notifyAll();
        return content;
    }
    /*
     * InReportRead.java puts data in this function so that GetData.java can get it
     */
    public synchronized void put(byte[] in){

        while(available == true)
        {
            try{
                wait();
            }
            catch (InterruptedException e) { }

        }

        /*
         * Data from USB is signed 8 bits. so maximum possible value is 127. But our ECG machine can provide upto 255 so the ANDing is done
         */
        int i = in[1] & 0xff;
        System.out.println("put = "+ i);
        content = in;
        available = true;
        notifyAll();
    }

    /*
     * Get state of available flag
     */
    public boolean available()
    {
        return available;
    }

}
