import lejos.nxt.LCD;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Move.MoveType;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

public class GridPoseProvider extends OdometryPoseProvider implements Runnable, MoveListener {
	
	public final float SENSOR_LINE_OFFSET = (float) 8.5;
	
	public static final double LINE_SEPERATION_X = (59.5079 + 59.5318) /4;
	public static final double LINE_SEPERATION_Y = (58.3235 + 58.2657) /4;

//	private Thread self = null;
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
	
	public Pose getGrid()
	{
		Pose pose = getPose();
		float x = pose.getX();
		float y = pose.getY();
		Pose p = new Pose(Math.round((float)x / (float)LINE_SEPERATION_X), Math.round((float)y / (float)LINE_SEPERATION_Y), pose.getHeading());
		return p;
		//return new Pose(x / LINE_SEPERATION_X, y / LINE_SEPERATION_Y, pose.getHeading());
		
	}
	
	public void setAutoCalibrate(boolean AC)
	{
		allowAutoCalibration = AC;
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
			Pose pose1 = getPose();
			LCD.drawString(getDirection(pose1.getHeading()).toString(), 0, 4);
			
			if(isTurning){
				pilot.setTravelSpeed(normalTravelSpeed);								
				continue;
			}
			if ((bwsLeft.wasBlack() || bwsRight.wasBlack()) && allowAutoCalibration) // use a cached value
			{
				Pose pose = getPose();
				LCD.clear();
				LCD.drawString("Auto Calibrated", 0, 0);
				LCD.drawInt(Math.round(pose.getX()),0, 1);
				LCD.drawInt(Math.round(pose.getY()),0, 2);
				if(getDirection(pose.getHeading()) == Direction.NORTH)
				{
					if(pilot.GetDirectionForward())
					{
						pose.setLocation((Math.round(getGrid().getX()))*(float)LINE_SEPERATION_X - SENSOR_LINE_OFFSET, Math.round(getGrid().getY())*(float)LINE_SEPERATION_Y );
					}
					else
					{
						pose.setLocation((Math.round(getGrid().getX()))*(float)LINE_SEPERATION_X + SENSOR_LINE_OFFSET, Math.round(getGrid().getY())*(float)LINE_SEPERATION_Y );
					}
				}
				else if(getDirection(pose.getHeading()) == Direction.WEST)
				{
					if(pilot.GetDirectionForward())
					{
						pose.setLocation((Math.round(getGrid().getX()))*(float)LINE_SEPERATION_X , Math.round(getGrid().getY())*(float)LINE_SEPERATION_Y  - SENSOR_LINE_OFFSET);
					}
					else
					{
						pose.setLocation(Math.round(getGrid().getX())*(float)LINE_SEPERATION_X , Math.round(getGrid().getY())*(float)LINE_SEPERATION_Y  + SENSOR_LINE_OFFSET);
					}
				}
				else if(getDirection(pose.getHeading()) == Direction.SOUTH)
				{
					if(pilot.GetDirectionForward())
					{
						pose.setLocation((Math.round(getGrid().getX()))*(float)LINE_SEPERATION_X  + SENSOR_LINE_OFFSET, Math.round(getGrid().getY())*(float)LINE_SEPERATION_Y );
					}
					else
					{
						pose.setLocation((Math.round(getGrid().getX()))*(float)LINE_SEPERATION_X  - SENSOR_LINE_OFFSET, Math.round(getGrid().getY())*(float)LINE_SEPERATION_Y );
					}
				}
				else //if(getDirection(pose.getHeading()) == Direction.EAST)
				{
					if(pilot.GetDirectionForward())
					{
						pose.setLocation((Math.round(getGrid().getX()))*(float)LINE_SEPERATION_X , Math.round(getGrid().getY())*(float)LINE_SEPERATION_Y  + SENSOR_LINE_OFFSET);
					}
					else
					{
						pose.setLocation((Math.round(getGrid().getX()))*(float)LINE_SEPERATION_X , Math.round(getGrid().getY())*(float)LINE_SEPERATION_Y  - SENSOR_LINE_OFFSET);
					}
				}
				allowAutoCalibration = false;
				LCD.drawInt(Math.round(pose.getX()),10, 1);
				LCD.drawInt(Math.round(pose.getY()),10, 2);

				
				
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
