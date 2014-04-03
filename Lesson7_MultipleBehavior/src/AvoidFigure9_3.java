
import lejos.nxt.*;
import lejos.util.Delay;
/*
 * A simple behavior control program Avoid, 
 * Figure 9.3 in Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */
public class AvoidFigure9_3 {
 	    
    public static void main(String [] args)  throws Exception {

		PrivateCar car = new PrivateCar();
    	int power = 70, ms = 500;
    	UltrasonicSensor sonar = new UltrasonicSensor(SensorPort.S1);
    	int frontDistance, leftDistance, rightDistance;
    	int stopThreshold = 30;
    	
    	// ESCAPE button listener makes it possible to exit the program
    	// at any time.
    	Button.ESCAPE.addButtonListener(new ButtonListener() {
    	      public void buttonPressed(Button b) {
    	    	  System.exit(1);
    	      }

    	      public void buttonReleased(Button b) {
    	      }
    	});
    	
    	LCD.drawString("Avoid 9.3", 0, 0);
    	Button.waitForAnyPress();
	    	      
        while ( true )
        {
            // Go forward
            car.forward(power, power);
	    	
            // Monitor the distance in front of the car and stop
            // when an object gets to close
            frontDistance = sonar.getDistance();
            while ( frontDistance > stopThreshold )
            {
            	frontDistance = sonar.getDistance();
            }
            car.stop();
	    	
            // Get the distance to the left
            car.forward(0, power);
            Delay.msDelay(ms);
            leftDistance = sonar.getDistance();
    	
            // Get the distance to the right
            car.backward(0, power);
            Delay.msDelay(ms);
            car.forward(power, 0);
            Delay.msDelay(ms);
            rightDistance = sonar.getDistance();
	    	
            // Turn in the direction with most space in front of the car
            
            if(leftDistance < stopThreshold && rightDistance < stopThreshold && frontDistance < stopThreshold){
            	car.backward(power, power);
            	Delay.msDelay(ms);
            	car.spin(ms*4, power);            	
            }else  if ( leftDistance > rightDistance ){
            	car.backward(power, 0);
            	Delay.msDelay(ms);
            	car.forward(0, power);
            	Delay.msDelay(ms);
            }		   		   
        }
    }
}






	

