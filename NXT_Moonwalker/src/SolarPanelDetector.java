import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.LCD;


public class SolarPanelDetector implements Runnable {

	ColorSensor cs = null;
	Thread t = null;

	public SolarPanelDetector(ColorSensor sensor) {
		cs = sensor;
		cs.setFloodlight(Color.WHITE);
		t = new Thread(this);
		t.start();
	}
	
	
	public Color getColor(){
		
		return null;
	}


	@Override
	public void run() {
		while(true){
			
	        String colorNames[] = {"None", "Red", "Green", "Blue", "Yellow",
                    "Megenta", "Orange", "White", "Black", "Pink",
                    "Grey", "Light Grey", "Dark Grey", "Cyan"};

	        Color c = cs.getRawColor();
	        
			LCD.drawString("Off: " +  c.getBackground(), 0, 2);
			LCD.drawString("B:   " +  c.getBlue(), 0, 3);
			LCD.drawString("G:   " +  c.getGreen(), 0, 4);
			LCD.drawString("R:   " +  c.getRed(), 0, 5);
		}
		
	}
	
	
	
}
