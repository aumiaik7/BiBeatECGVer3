
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.net.UnknownHostException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package ecgbmptversion15;

/**
 *
 * @author Aumi
 */
//import display.EcgDisplay;

/**
 *
 * @author Aumi
 */
public class Main_1 {

    private static SplashScreen mySplash;
    private static Rectangle2D.Double splashTextArea;
    private static Rectangle2D.Double splashProgressArea;
    private static Graphics2D splashGraphics;
    private static Font font;
    /**
     * @param args the command line arguments
     */
      /**
     * Prepare the global variables for the other splash functions
     */
    private static void splashInit()
    {
        mySplash = SplashScreen.getSplashScreen();
        if (mySplash != null)
        {   // if there are any problems displaying the splash this will be null
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;
            // stake out some area for our status information
            splashTextArea = new Rectangle2D.Double(width * .45, height*.35, width * .17, 20.);
            splashProgressArea = new Rectangle2D.Double(width * .45, height*.30, width*.4, 12 );

            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font = new Font("Times New Roman", Font.PLAIN, 14);
            splashGraphics.setFont(font);
            
            // initialize the status info
            splashText("Starting");
            splashProgress(0);
        }
    }
    
     /**
     * Display text in status area of Splash.  Note: no validation it will fit.
     * @param str - text to be displayed
     */
    public static void splashText(String str)
    {
        if (mySplash != null && mySplash.isVisible())
        {   // important to check here so no other methods need to know if there
            // really is a Splash being displayed

            // erase the last status text
            splashGraphics.setPaint(Color.BLACK);
            splashGraphics.fill(splashTextArea);

            // draw the text
            splashGraphics.setPaint(Color.WHITE);
            splashGraphics.drawString(str, (int)(splashTextArea.getX() + 10),(int)(splashTextArea.getY() + 15));

            // make sure it's displayed
            mySplash.update();
        }
    }
    
        /**
     * Display a (very) basic progress bar
     * @param pct how much of the progress bar to display 0-100
     */
    public static void splashProgress(int pct)
    {
        if (mySplash != null && mySplash.isVisible())
        {

            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            splashGraphics.setPaint(Color.LIGHT_GRAY);
            splashGraphics.fill(splashProgressArea);

            // draw an outline
            splashGraphics.setPaint(Color.BLACK);
            splashGraphics.draw(splashProgressArea);

            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();

            int doneWidth = Math.round(pct*wid/100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid-1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics.setPaint(Color.GREEN);
            splashGraphics.fillRect(x, y+1, doneWidth, hgt-1);

            // make sure it's displayed
            mySplash.update();
        }
    }
      private static void appInit()
    {
        for(int i=10;i<=100;i+=10)
        {
            int pctDone = i ;
            splashText("Loading..." + i +"%");
            splashProgress(pctDone);
            try
            {
                Thread.sleep(300);
            }
            catch (InterruptedException ex)
            {
                // ignore it
            }
        }
    }
    
    public static void main(String[] args) throws UnknownHostException, UnsupportedLookAndFeelException {
        // TODO code application logic here
        
        //splashInit();           // initialize splash overlay drawing parameters
        //appInit();              // simulate what an application would do 
                                // before starting
        //if (mySplash != null)   // check if we really had a spash screen
        //    mySplash.close();   // if so we're now done with it

        //new EcgDisplay().setVisible(true);
        try 
        {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }
            
         new EcgDisplay().setVisible(true);

    }

}
