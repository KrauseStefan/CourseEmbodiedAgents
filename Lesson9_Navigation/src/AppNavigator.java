import lejos.nxt.*;
import lejos.robotics.navigation.*;
import lejos.robotics.pathfinding.Path;


public class AppNavigator {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		DifferentialPilot robot = new DifferentialPilot(5.475F, 10.75F, Motor.C, Motor.B, false);  //5.5cm 11.5cm width measured. 5.475cm and 10.75 calculated.
		robot.setTravelSpeed(10);
		robot.setRotateSpeed(50);
		
		Navigator navi = new Navigator(robot);
		
		UltrasonicSensor sonar = new UltrasonicSensor(SensorPort.S1);
		int distance;
		
		while(!Button.ENTER.isPressed())
		{}
		Thread.sleep(100);
		
		driveInSquare(navi);
		driveInSquare(navi);
		driveInSquare(navi);
		driveInSquare(navi);
		driveInSquare(navi);		
		while(true)
		{
			LCD.clear();

			distance = sonar.getDistance();
			LCD.drawString("Navigating", 0, 0);
			LCD.drawInt(distance, 0, 1);
			Thread.sleep(100);
			if(distance < 20)
			{
				LCD.clear();
				LCD.drawString("Stopping", 0, 0);
				
				navi.stop();
				Path p = navi.getPath();
				navi.setPath(new Path());;
				navi.goTo(getNewWP(navi, 20));
				navi.waitForStop();
				navi.followPath(p);				
			}
		}		
	}
	
	public static Waypoint getNewWP(Navigator n, int dist)
	{
		float heading = n.getPoseProvider().getPose().getHeading();
		float x = n.getPoseProvider().getPose().getX();
		float y = n.getPoseProvider().getPose().getY();
		
		
		double x_ = (double)x + Math.cos((heading+(double)45)) * dist;
		double y_ = (double)y + Math.sin((heading+(double)45)) * dist;
		String s1 = "old: " + Float.toString(x) + " " + Float.toString(y);
		String s2 = "new: " + Double.toString(x_) + " " + Double.toString(y_);
		LCD.drawString(s1, 0, 2);	
		LCD.drawString(s2, 0, 3);
		
		return new Waypoint(x_, y_);
	}
	
	
	public static void driveInSquare(Navigator nav){
		nav.goTo(50, 0);
		nav.goTo(50,50);
		nav.goTo(0,50);
		nav.goTo(0, 0);
		}

}
