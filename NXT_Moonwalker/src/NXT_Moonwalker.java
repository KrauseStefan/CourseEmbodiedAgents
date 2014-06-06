

import lejos.nxt.LCD;
import lejos.robotics.navigation.*;

public class NXT_Moonwalker{
	final int START_X = 0, START_Y = 0;
	final float START_HEADIN = 0;
	
	SolarPanelDetector solarPanelDetector = null;
	TrackNavigator navigator = null;
	ClawController clawController = null;
	LineFollower lineFol = null;
	Thread lineFollowerThread;
	
	
	public NXT_Moonwalker(TrackNavigator n, SolarPanelDetector spd, ClawController clawController, LineFollower _lineFol){
		this.navigator = n;
		this.solarPanelDetector = spd;		
		this.clawController = clawController;
		this.lineFol = _lineFol;
		this.lineFollowerThread = new Thread(this.lineFol);
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
			if(navigator.getMoveController().GetDirectionForward())
			{
				navigator.goTo(p.getX() + 10, p.getY());
			}
			else{
				navigator.goTo(p.getX() - 15, p.getY());
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

		LCD.clear();
		lineFol.calibrate();
		lineFollowerThread.start();

//		navigator.getMoveController().forward();				
		LCD.clear(7);
		LCD.drawString("Follow Line", 0, 7);
		navigator.getPoseProvider().waitForLine();
//		navigator.getMoveController().stop();
		lineFol.stop();
		
		LCD.clear(7);
		LCD.drawString("navigator", 0, 7);
		navigator.getPoseProvider().setStartToStart();
		navigator.gridGoTo(0, 1, 0); // first intersection (no Panel)
				
		navigator.gridGoTo(0, 2, 0);
		
		navigator.waitForStop();
		
		navigator.getPoseProvider().calibrateHeading();
		
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
