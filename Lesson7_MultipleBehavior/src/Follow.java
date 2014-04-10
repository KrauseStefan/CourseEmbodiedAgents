
import lejos.nxt.*;
import lejos.util.Delay;
/*
 * Follow behavior , inspired by p. 304 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */

class Follow extends Thread
{
    private SharedCar car = new SharedCar();
    private final String LEFT = "LEFT";
    private final String RIGHT = "RIGHT";

	private int power = 70, ms = 500, lightDelay = 100;
	LightSensor light = new LightSensor(SensorPort.S4);
	
	int frontLight, leftLight, rightLight, delta;
	int lightThreshold;
	
    public Follow(SharedCar car)
    {
       this.car = car;	
       lightThreshold = light.getLightValue();
    }
    
	public void run() 
    {				       
        while (true)
        {
	    	// Monitor the light in front of the car and start to follow
	    	// the light if light level is above the threshold
        	frontLight = light.getLightValue();
	    	while ( frontLight <= lightThreshold )
	    	{
	    		car.noCommand();
	    		frontLight = light.getLightValue();
	    	}
	    	
	    	// Follow light as long as the light level is above the threshold
	    	while ( frontLight > lightThreshold )
	    	{
	    		// Get the light to the left
	    		car.turnLightSensor(power, LEFT);
	    		//car.forward(0, power);
	    		Delay.msDelay(lightDelay);
	    		leftLight = light.getLightValue();
	    		
	    		// Get the light to the right
	    		car.turnLightSensor(power, RIGHT);
	    		Delay.msDelay(lightDelay);
	    		car.turnLightSensor(power, RIGHT);
	    		Delay.msDelay(lightDelay);
	    		rightLight = light.getLightValue();
	    		
	    		//car.backward(0, power);
	    		//Delay.msDelay(ms);
    		    //car.forward(power, 0);
	    		//Delay.msDelay(ms);
	    		
	    		// Turn back to start position
	    		car.turnLightSensor(power, LEFT);
	    		Delay.msDelay(lightDelay);
	    		car.stopLightSensor();
	    	
	    		// Follow light for a while
	    		delta = leftLight-rightLight;
	    		car.forward(power-delta, power+delta);
	    		Delay.msDelay(ms);
    		
	    		frontLight = light.getLightValue();
	    	}
	    	
	    	car.stop();
	    	Delay.msDelay(ms);
 			
        }
    }
}

