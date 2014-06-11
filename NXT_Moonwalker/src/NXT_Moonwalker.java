

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.robotics.navigation.*;

public class NXT_Moonwalker{
	final int START_X = 0, START_Y = 0;
	final float START_HEADIN = 0;
	final float lineFollowEndOffset = (float) 0.7;
	
	SolarPanelDetector solarPanelDetector = null;
	TrackNavigator navigator = null;
	ClawController clawController = null;
	LineFollower lineFol = null;
	BlackWhiteSensor leftSensor = null;
	BlackWhiteSensor rightSensor = null;
	public NXT_Moonwalker(TrackNavigator n, SolarPanelDetector spd, ClawController clawController, LineFollower _lineFol, BlackWhiteSensor leftSensor, BlackWhiteSensor rightSensor){
		this.navigator = n;
		this.solarPanelDetector = spd;		
		this.clawController = clawController;
		this.lineFol = _lineFol;
		this.leftSensor = leftSensor;
		this.rightSensor = rightSensor;		
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
				clawController.setState(ClawController.ClawPositions.LOAD);
				navigator.getMoveController().travel(-10);
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

	
	public void run() throws Exception{
		LCD.drawString("Start" + 0, 0, 7);

		iStopCondition stopAtLine = new iStopCondition() {
			public boolean stopLoop() {				
				return leftSensor.isBlack() || rightSensor.isBlack();
			}
		};
		
		while(Button.ENTER.isUp()){
			Thread.yield();
		}
		
		LCD.clear();
		lineFol.calibrate();
		
		LCD.clear(7);
		LCD.drawString("Follow Line", 0, 7);
		lineFol.start(stopAtLine);
		
		navigator.getMoveController().stop();
		
		LCD.clear(7);
		LCD.drawString("navigator", 0, 7);

		Thread.sleep(1000);

		Pose pose = new Pose(-GridPoseProvider.SENSOR_LINE_OFFSET, 0f, 0f);
//		Pose pose = new Pose(, -lineFollowEndOffset, (float) -2.7);
		navigator.getPoseProvider().setPose(pose);
		Thread.sleep(10);		
		navigator.goTo(0, 0, 0);		
		navigator.waitForStop();
		navigator.getPoseProvider().setGridPosition(0, 1, 0);
		
		navigator.rotateTo(95);
		lineFol.start(stopAtLine);
		navigator.getMoveController().stop();
		
//		
//		
//		navigator.gridGoTo(0, 2, 0);
//		navigator.gridGoTo(1, 2, 0);
//		navigator.gridGoTo(2, 2, 0);
//		navigator.gridGoTo(3, 2, 0);
		
//		navigator.waitForStop();
//		
////		navigator.getPoseProvider().calibrateHeading();
//		
//		navigator.gridGoTo(1, 2, 0);
//		
//		navigator.waitForStop();
//		inspectPanel();
//		navigator.gridGoTo(2, 2, 0);
//
//		navigator.waitForStop();
//		inspectPanel();
//		navigator.gridGoTo(3, 2, 0);
//		
//		navigator.waitForStop();
//		inspectPanel();
//
//		while(true){
//			
//		}
	}

}
