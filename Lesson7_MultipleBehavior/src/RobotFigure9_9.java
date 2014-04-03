
import lejos.nxt.*;

/*
 * Behavior control network of Figure 9.9 in chapter 9 in
 * Jones, Flynn, and Seiger: 
 * "Mobile Robots, Inspiration to Implementation", 
 * Second Edition, 1999.
 */
public class RobotFigure9_9 {

	    
    public static void main(String [] args)  throws Exception {

    	SharedCar [] car = { new SharedCar(), new SharedCar(), 
                             new SharedCar(), new SharedCar()};
    	
    	CarDriver cd = new CarDriver();
    	
    	//Escape escape = new Escape(car[0]);
    	Avoid avoid   = new Avoid(car[1]);
    	Follow follow = new Follow(car[2]);
    	Cruise cruise = new Cruise(car[3]);
    	
    	Arbiter arbiter = new Arbiter(car, cd);
    	
        LCD.drawString("Robot 9.9", 0, 0);
        Button.waitForAnyPress();

        arbiter.setDaemon(true);	    
        arbiter.start();
        avoid.setDaemon(true);
        avoid.start();
        follow.setDaemon(true);
        follow.start();
        cruise.setDaemon(true);
        cruise.start();
	    	      
        while ( ! Button.ESCAPE.isDown())
        {	
            LCD.drawString("Winner " + arbiter.winner(), 0, 3);
        }

    }
}






	

