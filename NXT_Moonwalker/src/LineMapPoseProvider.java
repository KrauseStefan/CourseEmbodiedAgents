import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

public class LineMapPoseProvider extends OdometryPoseProvider implements Runnable {

	public final double LINE_SEPERATION = 23.2226;

	private Thread self = null;
	private LightSensor ls = null;
	private LineMap lm = null;
	private int white = 430;
	private int black = 230;
	private int darkThreshold = (white + black) / 2;

	private boolean waitingForBlack = true;

	
	private Pose startLocation = new Pose(); // (0 , 1)

	public LineMapPoseProvider(MoveProvider pilot, LightSensor lightSensor, LineMap lineMap) {
		super(pilot);
		this.ls = lightSensor;
		this.lm = lineMap;
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
		startLocation = this.getPose();
		startLocation.setHeading(0);
	}

	public Pose getStartToStart() {
		return startLocation;
	}

	public Waypoint gridGoTo(int x, int y, float heading) {
		// start = ( 0, 1 )
		y -= 1;
		x *= LINE_SEPERATION;
		y *= LINE_SEPERATION;
		
		x += startLocation.getX();
		y += startLocation.getY();

		return new Waypoint(x, y, heading);
	}

	public void waitForLine() {
		try {
			while (true) {
				if (!waitingForBlack)
					break;
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
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
