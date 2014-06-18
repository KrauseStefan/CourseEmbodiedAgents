import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.LCD;

public class SolarPanelDetector implements Runnable {

	ColorSensor cs = null;
	Thread t = null;
	Color c = null;
	ReversibleDifferentialPilot dp;
	public enum SolarPanelStates { CORRECT, REVERSED, BROKEN }

	public SolarPanelDetector(ColorSensor sensor, ReversibleDifferentialPilot dp_) {
		cs = sensor;
		dp = dp_;
		cs.setFloodlight(Color.WHITE);
//		t = new Thread(this);
//		t.start();
	}
	
	
	public SolarPanelStates getState(){
		
		c = cs.getColor();
				
//	    LCD.drawString(Integer.toString(c.getColor()), 0, 2);
		
	    if(c.getColor() == Color.BLACK)
	    	return SolarPanelStates.BROKEN;
	    else if(c.getColor() == Color.RED)
	    	if(dp.GetDirectionForward())
	    	{
	    		return SolarPanelStates.REVERSED;
	    	}
	    	else 
	    	{
	    		return SolarPanelStates.CORRECT;
	    	}
	    else if (c.getColor() == Color.BLUE)
	    	if(dp.GetDirectionForward())
	    	{
	    		return SolarPanelStates.CORRECT;
	    	}
	    	else 
	    	{
	    		return SolarPanelStates.REVERSED;
	    	}
	    else 
	    	return SolarPanelStates.CORRECT;
	}


	@Override
	public void run() {
		while(true){
			      
	        c = cs.getColor();
	        
	        if(c.getColor() == Color.BLACK) {
	        	LCD.clear();
	        	LCD.drawString("BLACK", 0, 2);
	        }
		    else if(c.getColor() == Color.RED) {
	        	LCD.clear();
	        	LCD.drawString("RED", 0, 2);
		    }
		    else if (c.getColor() == Color.BLUE) {
	        	LCD.clear();
	        	LCD.drawString("BLUE", 0, 2);
		    }
		    else {
		    	LCD.clear();
		    	LCD.drawString("OTHER", 0, 2);
		    }
	        	        
			/*LCD.drawString("Off: " +  c.getBackground(), 0, 2);
			LCD.drawString("B:   " +  c.getBlue(), 0, 3);
			LCD.drawString("G:   " +  c.getGreen(), 0, 4);
			LCD.drawString("R:   " +  c.getRed(), 0, 5);*/
		}
		
	}
	
	
	
}
