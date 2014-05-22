import lejos.nxt.LCD;
import lejos.robotics.navigation.*;

public class NXT_Moonwalker{
	final int START_X = 0, START_Y = 0;
	final float START_HEADIN = 0;

	enum PanelState {
		Broken,
		Reversed,
		Ok
	}
	
	TrackNavigator navigator = null;
	
	public NXT_Moonwalker(TrackNavigator n){
		n.getMoveController().setTravelSpeed(8);
		navigator = n;
		
	}
	
	public void waitForLine(){
		
	}
		
	PanelState inspectPanel(){
		return PanelState.Reversed; //FIXME: Dummy implementation		
	}
		
	void printPose(int nr){
		printPose(nr, new Pose());
	}
	

	void printPose(int nr, Pose pose){
		Pose p = navigator.getPoseProvider().getPose();
		LCD.clear(0);
		LCD.clear(1);
		LCD.clear(2);
		LCD.clear(3);
		LCD.drawString("#: " + nr, 0, 0);
		LCD.drawString("x: " + ((p.getX() - pose.getX()) / navigator.getPoseProvider().LINE_SEPERATION_X), 0, 1);
		LCD.drawString("y: " + ((p.getY() - pose.getY() + 1) / navigator.getPoseProvider().LINE_SEPERATION_Y), 0, 2);
		LCD.drawString("h: " + (p.getHeading() - pose.getHeading()), 0, 3);
	}

	
	public void run() throws Exception{
		
//		double dist = UtilityScenarios.mesureLineDistance(navigator);
//		
//		LCD.clear();
//		LCD.drawString("" + dist, 1, 1);
		navigator.getPoseProvider().setPose(new Pose(START_X, START_Y, START_HEADIN));
		printPose(1);
		
		navigator.getMoveController().forward();				
		navigator.getPoseProvider().waitForLine();
		navigator.getMoveController().stop();
		printPose(2);

		navigator.getPoseProvider().setStartToStart();
		navigator.gridGoTo(0, 1, 0); // first panel
				
		navigator.gridGoTo(0, 2, 0);
//		navigator.gridGoTo(1, 2, 0);
//		rotatePanel();
//		navigator.gridGoTo(2, 2, 0);
//		rotatePanel();
		navigator.gridGoTo(3, 2, 0);
//		navigator.rotatePanel();
//		while(navigator.isMoving()){
//			Thread.sleep(50);
//			printPose(3, navigator.getPoseProvider().getStartToStart());					
//		}
////		navigator.waitForStop();
//		
//		printPose(4);		
//		if(inspectPanel() == PanelState.Reversed)
//			rotatePanel();
//
//		printPose(5);		
//		gridGoTo(1, 2);
//		
//		if(inspectPanel() == PanelState.Reversed)
//			rotatePanel();
//
//		printPose(6);
//		
		while(true){
			
		}
	}

}
