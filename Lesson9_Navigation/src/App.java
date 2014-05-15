import lejos.nxt.*;
import lejos.robotics.navigation.*;


public class App {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		DifferentialPilot robot = new DifferentialPilot(5.475F, 10.75F, Motor.C, Motor.B, false);  //5.5cm 11.5cm width measured. 5.475cm and 10.75 calculated.
		robot.setTravelSpeed(10);
		robot.setRotateSpeed(50);
		
		while(!Button.ENTER.isPressed())
		{}
		Thread.sleep(100);
		
		
		driveInSquare(robot);
		driveInSquare(robot);
		driveInSquare(robot);
		driveInSquare(robot);
		
	}
	
	public static void driveInSquare(DifferentialPilot dp){
		dp.travel(20.0);
		dp.rotate(90.0);
		dp.travel(20.0);
		dp.rotate(90.0);
		dp.travel(20.0);
		dp.rotate(90.0);
		dp.travel(20.0);
		dp.rotate(90.0);
		}

}
