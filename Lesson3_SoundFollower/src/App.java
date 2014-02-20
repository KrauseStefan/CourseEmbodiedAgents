import lejos.nxt.*;
/**
 * A simple sonar sensor test program.
 * 
 * The sensor should be connected to port 1. In the
 * known bugs and limitations of leJOS NXJ version alfa_03
 * it is mentioned that a gap of at least 300 msec is 
 * needed between calls of getDistance. This is the reason 
 * for the delay of 300 msec between sonar readings in the 
 * loop.
 * 
 * @author  Ole Caprani
 * @version 30.08.07
 */
public class App {

	   public static void main(String [] args)  
			   throws Exception 
			   {
				   SoundSensor s = new SoundSensor(SensorPort.S2);
				   int soundLevel;

			       LCD.drawString("Level(dB) ", 0, 0);
				   
			       while (! Button.ESCAPE.isDown())
			       {
			           soundLevel = s.readValue();
			           LCD.drawInt(soundLevel,3,9,0);

			           Thread.sleep(5);
			       }
			       LCD.clear();
			       LCD.drawString("Program stopped", 0, 0);
			       Thread.sleep(2000);
			   }

}
