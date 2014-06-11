import java.io.IOException;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Move.MoveType;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.util.Datalogger;
import lejos.util.Logger;
import lejos.util.NXTDataLogger;
import lejos.util.PIDController;

public class GridPoseProvider extends OdometryPoseProvider implements Runnable, MoveListener {
	
	public final float SENSOR_LINE_OFFSET = (float) 8.5;
	
	public static final double LINE_SEPERATION_X = (59.5079 + 59.5318) /4;
	public static final double LINE_SEPERATION_Y = (58.3235 + 58.2657) /4;

	private Thread self = null;
	private BlackWhiteSensor bwsLeft = null;
	private BlackWhiteSensor bwsRight = null;
	
	private Pose startLocation = new Pose(); // (0 , 1)
	
	private boolean isTurning = false;
	
	ReversibleDifferentialPilot pilot;

	public GridPoseProvider(ReversibleDifferentialPilot pilot, BlackWhiteSensor lightSensorLeft, BlackWhiteSensor lightSensorRight) {
		super(pilot);
		this.pilot = pilot;
		bwsLeft = lightSensorLeft;
		bwsRight = lightSensorRight;
		self = new Thread(this);
		self.start();
	}

	@Override
	public Pose getPose() {
		return super.getPose();
	}

	@Override
	public void setPose(Pose aPose) {
		super.setPose(aPose);
	}

	public void setStartToStart() {		
		startLocation = new Pose((getPose().getX() + SENSOR_LINE_OFFSET), getPose().getY(), 0);
	}

	public Pose getStartToStart() {
		return startLocation;
	}

	public Waypoint gridGoTo(int x, int y, float heading) {
		// start = ( 0, 1 )
		y -= 1;
		x *= LINE_SEPERATION_X;
		y *= LINE_SEPERATION_Y;
				
		x += startLocation.getX();
		y += startLocation.getY();

		return new Waypoint(x, y, heading);
	}

	public void waitForLine() {
		while (true) {
			if (bwsLeft.wasBlack()) // use a cached value
				break;
			Thread.yield();
		}
	}
	
	public void calibrateHeading() throws IOException{
		float startHeading = getPose().getHeading();
		int ms_delay = 20; 
		
		double normalTravelSpeed = pilot.getTravelSpeed();
		pilot.setTravelSpeed(1);

		
		PIDController pidCalibratorLeft  = new PIDController(bwsLeft .getBlackWhiteThreshold(), 0); // setPoint, sleepDelay
		PIDController pidCalibratorRight = new PIDController(bwsRight.getBlackWhiteThreshold(), ms_delay); // setPoint, sleepDelay
		
		pidCalibratorLeft.setPIDParam(PIDController.PID_KP, (float) 0.4);
		pidCalibratorRight.setPIDParam(PIDController.PID_KP, (float) 0.4);
		
//		NXTConnection connection = Bluetooth.waitForConnection(0, NXTConnection.PACKET);
		
//		NXTDataLogger logger = new NXTDataLogger();
//		logger.startRealtimeLog(connection);
//		pidCalibratorLeft.registerDataLogger(logger);
		
		int goodValuesNeeded = 3;
		
		while(goodValuesNeeded > 0){ //adjust left wheel
			int pidVal = pidCalibratorLeft.doPID(bwsLeft.light());
			float diff = pidCalibratorLeft.getPIDParam(PIDController.PID_PV) - pidCalibratorLeft.getPIDParam(PIDController.PID_SETPOINT);
			
			if(Math.abs(diff) < 2)
				goodValuesNeeded--;
			
			if(pidVal > 0)
				pilot.forward(pidVal, pidVal);
			else
				pilot.backward(pidVal, pidVal);
			
		}
		
		goodValuesNeeded = 10;
		
		while(goodValuesNeeded > 0){
			if(Math.abs(bwsLeft.light() - bwsRight.light()) < 10)
				goodValuesNeeded--;

			int valueLeft  = pidCalibratorLeft .doPID(bwsLeft .light());
			int valueRight = pidCalibratorRight.doPID(bwsRight.light());
			
//			If radius is positive, the robot arcs left, and the center of the turning circle is on the left side of the robot.
//			If radius is negative, the robot arcs right, and the center of the turning circle is on the right side of the robot.
//			If radius is zero, is zero, the robot rotates in place.
			
//			Robot will stop when the degrees it has moved along the arc equals angle.
//			If angle is positive, the robot will move travel forwards.
//			If angle is negative, the robot will move travel backwards. If angle is zero, the robot will not move and the method returns immediately. 
			
//			if(valueLeft < 0 && valueRight < 0){
//				pilot.arc(0, valueLeft);
//			}else if(valueLeft > 0 && valueRight > 0){
//				
//			}else 
			if(valueLeft < 0 && valueRight > 0){
				pilot.arc(pilot.getTrackWidth()/2, valueRight);
			}else if(valueLeft > 0 && valueRight < 0){
				pilot.arc(- pilot.getTrackWidth()/2, valueRight);				
			}
			
			
		}
		Pose pose = getPose();
		pose.setHeading(startHeading);
		setPose(pose);
		pilot.setTravelSpeed(normalTravelSpeed);

	}	
	
	@Override
	public void run() {
		Long timeBothBlack = (long) 0;
		Boolean adjustLeft = false;
		double normalTravelSpeed = pilot.getTravelSpeed();
		pilot.addMoveListener(this);
//		LCD.clear(6);
//		LCD.clear(7);
//		LCD.drawString("L: ", 0, 6);
//		LCD.drawString("R: ", 0, 7);		
		
		while (true) {
			bwsLeft.light();
			bwsRight.light();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			LCD.drawInt(bwsLeft.light(), 3, 3, 6);
//			LCD.drawInt(bwsRight.light(), 3, 3, 7);
			
			if(isTurning){
				pilot.setTravelSpeed(normalTravelSpeed);								
				continue;
			}
		}
	}
	
	@Override
	public void moveStarted(Move event, MoveProvider mp) {
		super.moveStarted(event, mp);

		if(event.getMoveType() == MoveType.ROTATE || event.getMoveType() == MoveType.ARC)
			isTurning = true;
		else
			isTurning = false;
		
	}

	@Override
	public void moveStopped(Move event, MoveProvider mp) {
		super.moveStopped(event, mp);
				
	}
	
}
