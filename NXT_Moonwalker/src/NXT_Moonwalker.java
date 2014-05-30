

import lejos.nxt.LCD;
import lejos.robotics.navigation.*;

public class NXT_Moonwalker{
	final int START_X = 0, START_Y = 0;
	final float START_HEADIN = 0;
	
	SolarPanelDetector solarPanelDetector = null;
	TrackNavigator navigator = null;
	ClawController clawController = null;
	
	public NXT_Moonwalker(TrackNavigator n, SolarPanelDetector spd, ClawController clawController){
		this.navigator = n;
		this.solarPanelDetector = spd;		
		this.clawController = clawController;
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
			clawController.setState(ClawController.ClawPositions.LOAD);
			Pose p = navigator.getPoseProvider().getPose();
			navigator.goTo(p.getX() + 3, p.getY());
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
		
//		double dist = UtilityScenarios.mesureLineDistance(navigator);
//		
//		LCD.clear();
//		LCD.drawString("" + dist, 1, 1);
		navigator.getPoseProvider().setPose(new Pose(START_X, START_Y, START_HEADIN));
		
		navigator.getMoveController().forward();				
		navigator.getPoseProvider().waitForLine();
		navigator.getMoveController().stop();

		navigator.getPoseProvider().setStartToStart();
		navigator.gridGoTo(0, 1, 0); // first intersection (no Panel)
				
		navigator.gridGoTo(0, 2, 0);
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

}
