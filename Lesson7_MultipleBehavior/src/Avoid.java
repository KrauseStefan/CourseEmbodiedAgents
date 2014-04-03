
import lejos.nxt.*;
import lejos.util.Delay;
/*
 * Avoid behavior
 */

class Avoid extends Thread
{
    private SharedCar car = new SharedCar();

	private int power = 70, ms = 500;
	UltrasonicSensor sonar = new UltrasonicSensor(SensorPort.S1);
	
	int frontDistance, leftDistance, rightDistance;
	int stopThreshold = 30;
	
    public Avoid(SharedCar car)
    {
       this.car = car;		    	
    }
    
	public void run() 
    {				       
        while (true)
        {
	    	// Monitor the distance in front of the car and stop
	    	// when an object gets to close
        	frontDistance = sonar.getDistance();
	    	while ( frontDistance >= stopThreshold )
	    	{
	    		car.noCommand();
	    		frontDistance = sonar.getDistance();
	    	}
	    	car.stop();
	    	Delay.msDelay(ms);
	    	
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
	    	if ( leftDistance > rightDistance ){
		    	car.backward(power, 0);
		    	Delay.msDelay(ms);
		    	car.forward(0, power);
		    	Delay.msDelay(ms);
	    	}
        }
    }
}

