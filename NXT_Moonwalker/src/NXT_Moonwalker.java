
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.navigation.*;
import lejos.util.PilotProps;

public class NXT_Moonwalker {
	final int START_X = 0, START_Y = 0;
	final float START_HEADIN = 0;
	final float lineFollowEndOffset = (float) 0.7;

	SolarPanelDetector solarPanelDetector = null;
	ReversibleDifferentialPilot pilot = null;
	ClawController clawController = null;
	LineFollower lineFol = null;
	BlackWhiteSensor leftSensor = null;
	BlackWhiteSensor rightSensor = null;

	public NXT_Moonwalker(ReversibleDifferentialPilot p, SolarPanelDetector spd, ClawController clawController, LineFollower _lineFol,
			BlackWhiteSensor leftSensor, BlackWhiteSensor rightSensor) {
		this.pilot = p;
		this.solarPanelDetector = spd;
		this.clawController = clawController;
		this.lineFol = _lineFol;
		this.leftSensor = leftSensor;
		this.rightSensor = rightSensor;
	}

	private void rotatePanel() {
		pilot.rotate(180);
		pilot.reverse();
	}

	private void inspectPanel() throws InterruptedException {
		SolarPanelDetector.SolarPanelStates state = solarPanelDetector.getState();
		switch (state) {
		case REVERSED:
			rotatePanel();
			break;
		case BROKEN:
			if (pilot.GetDirectionForward()) {
				clawController.setState(ClawController.ClawPositions.LOAD);
				pilot.travel(8);
			} else {
				clawController.setState(ClawController.ClawPositions.LOAD);
				pilot.travel(-10);
			}
			clawController.setState(ClawController.ClawPositions.CARRY);
			break;
		case CORRECT:
			break;
		}

	}
	
	private void printStatus(String status){
		LCD.clear(7);
		LCD.drawString(status, 0, 7);

	}

	// public void proceedToIntersection() throws Exception{	// Pose pose = new Pose(-GridPoseProvider.SENSOR_LINE_OFFSET, 0f, 4f);
	// navigator.getPoseProvider().setPose(pose);
	// Thread.sleep(10);
	// navigator.goTo(0, 0, 0);
	// navigator.waitForStop();
	// }
	public void linefollowFor(final long timeout) throws Exception {
		
		lineFol.start(new iStopCondition() {
			long stopTime = System.currentTimeMillis() + timeout;//500;
			@Override
			public boolean stopLoop() {
				return System.currentTimeMillis() > stopTime;
			}
		}, pilot.GetDirectionForward());
		
//		pilot.rotate(-4);
//		pilot.travel(GridPoseProvider.SENSOR_LINE_OFFSET);
	}

	public void proceedToSolarPanel(int dist) throws Exception {
		//pilot.travel(GridPoseProvider.SENSOR_LINE_OFFSET);
		if(pilot.GetDirectionForward()){ pilot.travel(16);}
		else {pilot.travel(dist);}
	}
	
	public void proceedToNextIntersection() throws Exception{
		if(pilot.GetDirectionForward())
		{
			linefollowFor(500);
			proceedToSolarPanel(16);
		}
		else{
			linefollowFor(200);
			proceedToSolarPanel(25);
			linefollowFor(300);
		}
	}

	public void run() throws Exception {
		StopAtLine stopAtLine = new StopAtLine();

		while (Button.ENTER.isUp()) {
			Thread.yield();
		}

		LCD.clear();
		leftSensor.calibrate(leftSensor.light(), -1);
		rightSensor.calibrate(rightSensor.light(), -1);
		lineFol.calibrate();
		clawController.CalibrateClaw();
		
		printStatus("Follow Line");
		stopAtLine.setDelayBeforeStop(5000);
		lineFol.start(stopAtLine, pilot.GetDirectionForward());

		printStatus("navigator");

		Thread.sleep(100);

		linefollowFor(500);

		Thread.sleep(1000);
		pilot.rotate(95);
		Thread.sleep(1000);
		lineFol.calibrate();
		lineFol.start(stopAtLine, pilot.GetDirectionForward());
		linefollowFor(400);

		Thread.sleep(100);
		pilot.rotate(-95);
		Thread.sleep(100);
		lineFol.calibrate();
		Thread.sleep(1000);
		linefollowFor(800);
		proceedToSolarPanel(16);
		inspectPanel();
		
		proceedToNextIntersection();
		inspectPanel();
		
		proceedToNextIntersection();
		inspectPanel();
		
		Thread.sleep(8000);
		
		//lineFol.start(stopAtLine);

		// navigator.gridGoTo(0, 2, 0);
		// navigator.gridGoTo(1, 2, 0);
		// navigator.gridGoTo(2, 2, 0);
		// navigator.gridGoTo(3, 2, 0);

		// navigator.waitForStop();
		//
		// // navigator.getPoseProvider().calibrateHeading();
		//
		// navigator.gridGoTo(1, 2, 0);
		//
		// navigator.waitForStop();
		// inspectPanel();
		// navigator.gridGoTo(2, 2, 0);
		//
		// navigator.waitForStop();
		// inspectPanel();
		// navigator.gridGoTo(3, 2, 0);
		//
		// navigator.waitForStop();
		// inspectPanel();
		//
		// while(true){
		//
		// }
	}

	class StopAtLine implements iStopCondition {

		private long minTime;

		public void setDelayBeforeStop(long timeMs) {
			minTime = timeMs + System.currentTimeMillis();
		}

		@Override
		public boolean stopLoop() {
			boolean stop = leftSensor.isBlack() || rightSensor.isBlack();
			LCD.drawString("Left : " + leftSensor.getChachedLightValue() + " " + leftSensor.getBlackWhiteThreshold(), 0, 3);
			LCD.drawString("Right: " + rightSensor.getChachedLightValue() + " " + rightSensor.getBlackWhiteThreshold(), 0, 4);
			return stop && (System.currentTimeMillis() > minTime);
		}

	}

}
