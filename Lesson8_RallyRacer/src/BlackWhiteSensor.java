import lejos.nxt.*;

/**
 * A sensor that is able to distinguish a black/dark surface
 * from a white/bright surface.
 * 
 * Light percent values from an active light sensor and a
 * threshold value calculated based on a reading over a 
 * black/dark surface and a reading over a light/bright 
 * surface is used to make the distinction between the two 
 * types of surfaces.
 *  
 * @author  Ole Caprani
 * @version 23.08.07
 */
public class BlackWhiteSensor {

   private LightSensor ls; 
   public int blackLightValue;
   public int whiteLightValue;
   public int blackWhiteThreshold;
   private SensorPort port;

   public static void main(String[] args)
   {
	   BlackWhiteSensor BWS = new BlackWhiteSensor(SensorPort.S1); 
	   BWS.calibrate();
	   
	   LCD.clear();
	   LCD.drawString("Sort " + BWS.blackLightValue, 0, 0);
	   LCD.drawString("Hvid " + BWS.whiteLightValue, 0, 1);
	   LCD.drawString("Thes " + BWS.blackWhiteThreshold, 0, 2);
	   while(true){}
   }
   
   public BlackWhiteSensor(SensorPort p)
   {
	   port = p;
	   ls = new LightSensor(p); 
	   // Use the light sensor as a reflection sensor
	   ls.setFloodlight(true);
   }

   private int read(String color){
	   
	   int lightValue=0;
	   
	   while (Button.ENTER.isPressed());
	   
	   LCD.clear();
	   LCD.drawString("Press ENTER", 0, 0);
	   LCD.drawString("to callibrate", 0, 1);
	   LCD.drawString(color, 0, 2);
	   while( !Button.ENTER.isPressed() ){
	      lightValue = light();
	      LCD.drawInt(lightValue, 4, 10, 2);
	      LCD.refresh();
	   }
	   return lightValue;
   }
   
   public void calibrate()
   {
	   blackLightValue = read("black");
	   whiteLightValue = read("white");
	   // The threshold is calculated as the median between 
	   // the two readings over the two types of surfaces
	   blackWhiteThreshold = (blackLightValue+whiteLightValue)/2;
   }
   
   public boolean black() {
           return (ls.readValue()< blackWhiteThreshold);
   }
   
   public boolean white() {
	   return (ls.readValue()> blackWhiteThreshold);
   }
   
   public int light() {
	   return 1024 - port.readRawValue();
 	   //return ls.readValue();
   }
   
}