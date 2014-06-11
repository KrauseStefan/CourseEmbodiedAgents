import java.io.IOException;

import lejos.nxt.LCD;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Move.MoveType;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
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
	private boolean allowAutoCalibration = false;
	
	public enum Direction {
		NORTH, //0 
		WEST, //1
		SOUTH, //2
		EAST //3
	};
	
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
			if (!isTurning && (bwsLeft.wasBlack() || bwsRight.wasBlack())) // use a cached value
				break;
			Thread.yield();
		}
	}
	
	public void setAutoCalibrate(boolean AC)
	{
		allowAutoCalibration = AC;
	}
	
	public void calibrateHeading() throws IOException{
		float startHeading = getPose().getHeading();
		int ms_delay = 20; 
		
		double normalTravelSpeed = pilot.getTravelSpeed();
		pilot.setTravelSpeed(1);
		
		
		PIDController pidCalibratorLeft  = new PIDController(bwsLeft .getBlackWhiteThreshold(), 0); // setPoint, sleepDelay
		PIDController pidCalibratorRight = new PIDController(bwsRight.getBlackWhiteThreshold(), ms_delay); // setPoint, sleepDelay
		
		pidCalibratorLeft.setPIDParam(PIDController.PID_KP, (float) 0.5);
		pidCalibratorRight.setPIDParam(PIDController.PID_KP, (float) 0.5);
		
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

		pidCalibratorLeft.setPIDParam(PIDController.PID_KP, (float) 1);
		pidCalibratorRight.setPIDParam(PIDController.PID_KP, (float) 1);

		pidCalibratorLeft.setPIDParam(PIDController.PID_KI, (float) 0.2);
		pidCalibratorRight.setPIDParam(PIDController.PID_KI, (float) 0.2);

		
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
		double normalTravelSpeed = pilot.getTravelSpeed();
		pilot.addMoveListener(this);
		LCD.clear(6);
		LCD.clear(7);
		LCD.drawString("L: ", 0, 6);
		LCD.drawString("R: ", 0, 7);	
		
		while (true) {
			bwsLeft.light();
			bwsRight.light();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LCD.drawInt(bwsLeft.light(), 3, 3, 6);
			LCD.drawInt(bwsRight.light(), 3, 3, 7);
			
			if(isTurning || true){
				pilot.setTravelSpeed(normalTravelSpeed);								
				continue;
			}
			if (!isTurning && (bwsLeft.wasBlack() || bwsRight.wasBlack()) && allowAutoCalibration) // use a cached value
			{
				Pose pose = getPose();
				if(getDirection(pose.getHeading()) == Direction.NORTH)
				{
					if(pilot.GetDirectionForward())
					{
						pose.setLocation((Math.round(pose.getX())) - SENSOR_LINE_OFFSET, Math.round(pose.getY()));
					}
					else
					{
						pose.setLocation((Math.round(pose.getX())) + SENSOR_LINE_OFFSET, Math.round(pose.getY()));
					}
				}
				else if(getDirection(pose.getHeading()) == Direction.WEST)
				{
					if(pilot.GetDirectionForward())
					{
						pose.setLocation((Math.round(pose.getX())), Math.round(pose.getY()) - SENSOR_LINE_OFFSET);
					}
					else
					{
						pose.setLocation(Math.round(pose.getX()), Math.round(pose.getY()) + SENSOR_LINE_OFFSET);
					}
				}
				else if(getDirection(pose.getHeading()) == Direction.SOUTH)
				{
					if(pilot.GetDirectionForward())
					{
						pose.setLocation((Math.round(pose.getX())) + SENSOR_LINE_OFFSET, Math.round(pose.getY()));
					}
					else
					{
						pose.setLocation((Math.round(pose.getX())) - SENSOR_LINE_OFFSET, Math.round(pose.getY()));
					}
				}
				else //if(getDirection(pose.getHeading()) == Direction.EAST)
				{
					if(pilot.GetDirectionForward())
					{
						pose.setLocation((Math.round(pose.getX())), Math.round(pose.getY()) + SENSOR_LINE_OFFSET);
					}
					else
					{
						pose.setLocation((Math.round(pose.getX())), Math.round(pose.getY()) - SENSOR_LINE_OFFSET);
					}
				}
			}

		}
		}
	
	private Direction getDirection(float heading)
	{
		if(heading > 315 || heading < 45) //N
		{return Direction.NORTH; }
		else if(heading > 45 || heading < 135) //W
		{return Direction.WEST; }
		else if(heading > 135 || heading < 225) //S
		{return Direction.SOUTH; }
		else //if(heading > 225 || heading > 315) //E
		{return Direction.EAST; }
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
