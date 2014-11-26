/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package display;

import processing.core.*;

/**
 *
 * @author Aumi
 */
public class BeatRate extends PApplet{

    public void setBeat(int beat)
    {
        background(250,250,250);
        fill(0,153,102);
        textSize(28);
        text(""+beat, 20, 30);
    }

    public void setup () {
      /*
       *set the window size:
       */
      size(104, 60);



      /*
       * Color of the line
       */
       //stroke(128,0,64);
       //smooth();
       /*
        * weight of line
        */
       //strokeWeight(1);

       background(250,250,250);
}

    public void draw()
    {

    }

}
