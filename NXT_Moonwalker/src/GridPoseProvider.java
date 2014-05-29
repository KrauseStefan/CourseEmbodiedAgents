import lejos.nxt.LCD;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

public class GridPoseProvider extends OdometryPoseProvider implements Runnable {
	
	public final float SENSOR_LINE_OFFSET = (float) 8.5;
	
	public static final double LINE_SEPERATION_X = (59.5079 + 59.5318) /4;
	public static final double LINE_SEPERATION_Y = (58.3235 + 58.2657) /4;

	private Thread self = null;
	private BlackWhiteSensor bwsLeft = null;
	private BlackWhiteSensor bwsRight = null;
	
	private Pose startLocation = new Pose(); // (0 , 1)

	public GridPoseProvider(MoveProvider pilot, BlackWhiteSensor lightSensorLeft, BlackWhiteSensor lightSensorRight) {
		super(pilot);
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
			if (bwsLeft.wasBlack()) // use a chached value
				break;
			Thread.yield();
		}
	}

	private void adjustHeading(long diff, boolean adjustLeft){
		float factor = (float) 0.01;
		float heading = getPose().getHeading() - (diff * factor);
		getPose().setHeading( heading);
		LCD.clear(7);
		LCD.drawString("Adjust: " + heading, 0, 7);
		
	}
	
	@Override
	public void run() {
		Long timeBlackSeen = (long) 0;
		Boolean adjustLeft = false;
//		LCD.clear(6);
//		LCD.clear(7);
//		LCD.drawString("L: ", 0, 6);
//		LCD.drawString("R: ", 0, 7);		
		
		while (true) {
			bwsLeft.light();
			bwsRight.light();
//			LCD.drawInt(bwsLeft.light(), 3, 3, 6);
//			LCD.drawInt(bwsRight.light(), 3, 3, 7);
//			bwsLeft.isBlack();
//			bwsRight.isBlack();			
			
			if(bwsLeft.wasBlack() || bwsRight.wasBlack()){
				if(bwsLeft.wasBlack() && bwsRight.wasBlack()){
					adjustHeading(timeBlackSeen - System.nanoTime(), adjustLeft);
				}else{
					adjustLeft = bwsRight.wasBlack();
					timeBlackSeen = timeBlackSeen == 0 ? System.nanoTime() : timeBlackSeen;					
				}
			}else
				timeBlackSeen = (long) 0;
		}
	}
}
