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

	private Thread self = null;
	private BlackWhiteSensor bwsLeft = null;
	private BlackWhiteSensor bwsRight = null;
	
	private Pose startLocation = new Pose(); // (0 , 1)
	
	private boolean isTurning = false;
	
	ReversibleDifferentialPilot RDPilot;

	public GridPoseProvider(ReversibleDifferentialPilot pilot, BlackWhiteSensor lightSensorLeft, BlackWhiteSensor lightSensorRight) {
		super(pilot);
		RDPilot = pilot;
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
		double factor = 0.00005;
		
		Pose p = getPose();
		
		double adjustment = (diff * factor) * (adjustLeft ? 1 : -1);
		
		double heading = p.getHeading() - adjustment;

		heading %= 360;
		p.setHeading( (float) heading);
		setPose(p);
		
		LCD.clear(6);
		LCD.drawString("diff:   " + diff, 0, 6);		
		LCD.clear(7);
		LCD.drawString("Adjust: " + adjustment, 0, 7);		
	}
	
	@Override
	public void run() {
		Long timeBothBlack = (long) 0;
		Boolean adjustLeft = false;
		double normalTravelSpeed = RDPilot.getTravelSpeed();
		RDPilot.addMoveListener(this);
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
			Pose p = getPose();
			if(isTurning){
				RDPilot.setTravelSpeed(normalTravelSpeed);								
				continue;
			}

			
			if(bwsLeft.wasBlack() && bwsRight.wasBlack()){
				timeBothBlack = (timeBothBlack == 0 ? System.currentTimeMillis() : timeBothBlack);
			}else{
			}

			if(bwsLeft.wasBlack() || bwsRight.wasBlack()){				
				if(timeBothBlack > 0){
					adjustHeading(System.currentTimeMillis() - timeBothBlack, adjustLeft);					
				}
				
				adjustLeft = bwsRight.wasBlack();
				RDPilot.setTravelSpeed(2);
			}else{
				RDPilot.setTravelSpeed(normalTravelSpeed);				
				timeBothBlack = (long) 0;
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
