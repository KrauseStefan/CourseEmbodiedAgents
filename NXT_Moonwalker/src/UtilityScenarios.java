import java.io.IOException;

import lejos.nxt.LCD;
import lejos.robotics.navigation.Pose;

public class UtilityScenarios {

	static double mesureLineDistance(TrackNavigator navigator) throws Exception {

		double speed = navigator.getMoveController().getTravelSpeed();
		// navigator.getMoveController().setTravelSpeed(3);

		navigator.getMoveController().forward();
		navigator.getPoseProvider().waitForLine();
		navigator.getMoveController().stop(); // stop at firstLine
		Pose p1 = null;
		try {
			Thread.sleep(200);

			p1 = navigator.getPoseProvider().getPose();

			navigator.getMoveController().forward();
			Thread.sleep(7000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		navigator.getPoseProvider().waitForLine();
		navigator.getMoveController().stop(); // stop at secondLine

		Pose p2 = navigator.getPoseProvider().getPose();

		navigator.getMoveController().setTravelSpeed(speed);

		double x2 = Math.pow(p1.getX() - p2.getX(), 2);
		double y2 = Math.pow(p1.getY() - p2.getY(), 2);

		double result = Math.sqrt(x2 + y2);

		return result;
	}

	static void calibrationProgram(TrackNavigator navigator) {
		float x = (float) GridPoseProvider.LINE_SEPERATION_X;
		float y = (float) GridPoseProvider.LINE_SEPERATION_Y;

		Pose p = navigator.getPoseProvider().getPose();
		p.setHeading(90);
		navigator.getPoseProvider().setPose(p);
		
		
		while (true) {
			squareCounterClock(navigator, x, y);
//			squareClock(navigator, x, y);
			navigator.followPath();
			navigator.waitForStop();
		}
	}

	static void testReverse(TrackNavigator navigator) {
		float x = (float) GridPoseProvider.LINE_SEPERATION_X;
		float y = (float) GridPoseProvider.LINE_SEPERATION_Y;

		while (true) {
			rectangleClock(navigator, x, y);
//			squareClock(navigator, x, y);
		}
	}

	
	static void rectangleClock(TrackNavigator navigator, float x, float y) {
		navigator.addWaypoint(x, 0);
		navigator.followPath();
		navigator.waitForStop();
		
		navigator.rotatePanel();

		navigator.addWaypoint(2*x, 0);
		navigator.addWaypoint(2*x, y);
		navigator.addWaypoint(x, y);
		navigator.followPath();
		navigator.waitForStop();
		
		navigator.rotatePanel();
		
		navigator.addWaypoint(0, y);
		navigator.addWaypoint(0, 0);
		navigator.followPath();
		navigator.waitForStop();
	}

	
	static void squareClock(TrackNavigator navigator, float x, float y) {
		navigator.addWaypoint(0, y);
		navigator.addWaypoint(x, y);
		navigator.addWaypoint(x, 0);
		navigator.addWaypoint(0, 0);
	}

	static void squareCounterClock(TrackNavigator navigator, float x, float y) {
		navigator.addWaypoint(0, y);
		navigator.addWaypoint(-x, y);
		navigator.addWaypoint(-x, 0);
		navigator.addWaypoint(0, 0);

	}

	static void driveLineBackAndForth(TrackNavigator navigator) {
		float x = (float) GridPoseProvider.LINE_SEPERATION_X;
		float y = (float) GridPoseProvider.LINE_SEPERATION_Y;
		
		navigator.getPoseProvider().setPose(new Pose(0,0, 0));
		
		while(true){
			navigator.addWaypoint(1* x, 0*y);
			navigator.addWaypoint(2* x, 0*y);
			navigator.addWaypoint(3* x, 0*y);
			navigator.addWaypoint(2* x, 0*y);
			navigator.addWaypoint(1* x, 0*y);
			navigator.addWaypoint(0* x, 0*y);

						
			navigator.followPath();
			navigator.waitForStop();
		}

	}
	
	static void testBWLightSensor(BlackWhiteSensor sensor) throws InterruptedException
	{
		while(true){
			LCD.clear();
			LCD.drawInt(sensor.light(), 0, 0);
			Thread.sleep(50);
		}		
	}

	static void testBWLightSensors(BlackWhiteSensor sensor1, BlackWhiteSensor sensor2) throws InterruptedException
	{
		while(true){
			LCD.clear();
			LCD.drawInt(sensor1.light(), 0, 0);
			LCD.drawInt(sensor2.light(), 0, 1);
			Thread.sleep(50);
		}		
	}
	

	public static void calibrateHeadingTest(TrackNavigator navigator) throws Exception{
		navigator.getMoveController().forward();				
		LCD.clear(7);
		LCD.drawString("wait for Line", 0, 5);
		navigator.getPoseProvider().waitForLine();
		navigator.getMoveController().stop();
		
		LCD.clear(7);
		LCD.drawString("navigator", 0, 5);
		navigator.getPoseProvider().setStartToStart();
		navigator.gridGoTo(0, 1, 0); // first intersection (no Panel)
		navigator.waitForStop();
		
		LCD.drawString("Calibrateing", 0, 5);
		//navigator.getPoseProvider().calibrateHeading();
		navigator.stop();
		LCD.drawString("Done Calibrating", 0, 5);
	}
}
