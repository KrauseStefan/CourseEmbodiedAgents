import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

public class GridPoseProvider extends OdometryPoseProvider implements Runnable {

//	public final double LINE_SEPERATION = (35.6) /2;
//	public final double LINE_SEPERATION = (89) /3;
	
	public final float SENSOR_LINE_OFFSET = 6;
	
	public static final double LINE_SEPERATION_X = (59.5079 + 59.5318) /4;
	public static final double LINE_SEPERATION_Y = (58.3235 + 58.2657) /4;

	private Thread self = null;
	private LightSensor ls = null;
	private int white = 0;
	private int black = 0;
	private int darkThreshold = (white + black) / 2;

	private boolean waitingForBlack = true;

	
	private Pose startLocation = new Pose(); // (0 , 1)

	public GridPoseProvider(MoveProvider pilot, LightSensor lightSensor, int black, int white) {
		super(pilot);
		this.black = black;
		this.white = white;
		this.darkThreshold = (white + black) / 2;
		
		this.ls = lightSensor;
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
			if (!waitingForBlack)
				break;
			Thread.yield();
		}
	}

	@Override
	public void run() {
		int counter = 0;
		while (true) {
			int value = ls.getNormalizedLightValue();
			LCD.clear(6);
			LCD.drawInt(value, 0, 6);
			if (ls.getNormalizedLightValue() < darkThreshold) {
				waitingForBlack = false;
				counter = 0;
			} else
				counter++;

			if (counter > 25) {
				waitingForBlack = true;
			}
		}
	}

}
