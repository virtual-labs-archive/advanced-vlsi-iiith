import java.awt.*;
import java.applet.*;

public class DrawExample extends Applet 
{

    Font bigFont;
 // The colors you will use
     Color redColor;
     Color weirdColor;
     Color bgColor;

     public void init() 
     {
          bigFont = new Font("Arial",Font.BOLD,16);

          redColor = Color.red;
          weirdColor = new Color(60,60,122);
          bgColor = Color.blue;
      setBackground(bgColor);
     }

     public void stop()
     {
     }
     public void paint(Graphics g) 
     {
  // tell g to use your font
      g.setFont(bigFont);
      g.drawString("Shapes and Colors",80,20);

  // Now we tell g to change the color
      g.setColor(redColor);

  // This will draw a rectangle (xco,yco,xwidth,height);
      g.drawRect(100,100,100,100);

  // This will fill a rectangle
      g.fillRect(110,110,80,80);

  // change colors again

      g.setColor(weirdColor);

  // a circle (int x, int y, int width, int height,int startAngle, int arcAngle);
  // ovals are also possible this way.

      g.fillArc(120,120,60,60,0,360);

      g.setColor(Color.yellow);

  // Draw a line (int x1, int y1, int x2, int y2)

      g.drawLine(140,140,160,160);

  // reset the color to the standard color for the next time the applets paints
  // an applet is repainted when a part was'nt visible anymore
  // happens most often because of browser minimizing or scrolling. 

      g.setColor(Color.black);

     }

} 
