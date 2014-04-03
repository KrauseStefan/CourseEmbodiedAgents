
/*
 * Cruise behavior, p. 303 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */

class Cruise extends Thread
{
    private SharedCar car;
    private int power = 70;
    
    public Cruise(SharedCar car)
    {
    	this.car = car;
    }
    
	public void run() 
    {				       
        while (true)
        {
        	/*  Drive forward */
			car.forward(power, power); 
        }
    }
}
	

