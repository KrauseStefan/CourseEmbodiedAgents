

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.navigation.*;

public class NXT_Moonwalker{
	final int START_X = 0, START_Y = 0;
	final float START_HEADIN = 0;
	
	SolarPanelDetector solarPanelDetector = null;
	TrackNavigator navigator = null;
	ReversibleDifferentialPilot pilot = null;
	ClawController clawController = null;
	LineFollower lineFol = null;
	BlackWhiteSensor leftSensor = null;
	BlackWhiteSensor rightSensor = null;
	Thread lineFollowerThread;

	public NXT_Moonwalker(TrackNavigator n, ReversibleDifferentialPilot p, SolarPanelDetector spd, ClawController clawController, LineFollower _lineFol,
			BlackWhiteSensor leftSensor, BlackWhiteSensor rightSensor) {
		this.navigator = n;
		this.pilot = p;
		this.solarPanelDetector = spd;
		this.clawController = clawController;
		this.lineFol = _lineFol;
		this.leftSensor = leftSensor;
		this.rightSensor = rightSensor;
	}
	
	public void waitForLine(){
		
	}
		
	private void inspectPanel() throws InterruptedException{
		SolarPanelDetector.SolarPanelStates state =  solarPanelDetector.getState();
		switch (state) {
		case REVERSED:
			navigator.rotatePanel();
			break;
		case BROKEN:
			
			if(navigator.getMoveController().GetDirectionForward())
			{
				clawController.setState(ClawController.ClawPositions.LOAD);
				Pose p = navigator.getPoseProvider().getPose();
				navigator.goTo(p.getX() + 8, p.getY());
			}
			else{
				navigator.getMoveController().travel(4);
				clawController.setState(ClawController.ClawPositions.LOAD);
				navigator.getMoveController().travel(-14);
			}
				navigator.waitForStop();
				clawController.setState(ClawController.ClawPositions.CARRY);	

		
			break;
		case CORRECT:
			break;
		}

	}
		
//	private void printPose(int nr){
//		printPose(nr, new Pose());
//	}
	

	void printPose(int nr, Pose pose){
		Pose p = navigator.getPoseProvider().getPose();
		LCD.clear(0);
		LCD.clear(1);
		LCD.clear(2);
		LCD.clear(3);
		LCD.drawString("#: " + nr, 0, 0);
		LCD.drawString("x: " + ((p.getX() - pose.getX()) / GridPoseProvider.LINE_SEPERATION_X), 0, 1);
		LCD.drawString("y: " + ((p.getY() - pose.getY() + 1) / GridPoseProvider.LINE_SEPERATION_Y), 0, 2);
		LCD.drawString("h: " + (p.getHeading() - pose.getHeading()), 0, 3);
	}
	
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

	
	public void run() throws Exception{
		StopAtLine stopAtLine = new StopAtLine();

		LCD.drawString("Start" + 0, 0, 7);

		LCD.clear();
		
		while (Button.ENTER.isUp()) {
			Thread.yield();
		}
		
		leftSensor.calibrate(leftSensor.light(), -1);
		rightSensor.calibrate(rightSensor.light(), -1);
		lineFol.calibrate();
		pilot.travel(5);
		clawController.CalibrateClaw();

		LCD.clear(7);
		LCD.drawString("Follow Line", 0, 7);
		//lineFol.start(stopAtLine, pilot.GetDirectionForward());
		linefollowFor(5500);
		pilot.forward();
		navigator.getPoseProvider().waitForLine();

		
		//navigator.getPoseProvider().waitForLine();
//		navigator.getMoveController().stop();
		//lineFol.stop();
		
		LCD.clear(7);
		LCD.drawString("navigator", 0, 7);
		navigator.getPoseProvider().setStartToStart();
		navigator.gridGoTo(0, 1, 0); // first intersection (no Panel)
		navigator.getPoseProvider().setAutoCalibrate(false);
		navigator.gridGoTo(0, 2, 0);
		
		navigator.waitForStop();
		
//		navigator.getPoseProvider().calibrateHeading();
		
		navigator.gridGoTo(1, 2, 0);
		
		navigator.waitForStop();
		inspectPanel();
		navigator.gridGoTo(2, 2, 0);

		navigator.waitForStop();
		inspectPanel();
		navigator.gridGoTo(3, 2, 0);
		
		navigator.waitForStop();
		inspectPanel();

		while(true){
			
		}
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
