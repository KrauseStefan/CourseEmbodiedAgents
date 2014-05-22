import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Navigator;


public class TrackNavigator extends Navigator {

	public TrackNavigator(ReversibleDifferentialPilot pilot, PoseProvider poseProvider) {
		super(pilot, poseProvider);
	}
	
	public ReversibleDifferentialPilot getMoveController(){
		return (ReversibleDifferentialPilot) super.getMoveController();
	}
	
	public LineMapPoseProvider getPoseProvider(){
		return (LineMapPoseProvider) super.getPoseProvider();
	}
	
	public void gridGoTo(int x, int y, float heading){
		this.goTo(this.getPoseProvider().gridGoTo(x, y, heading));
	}

	public void gridGoTo(int x, int y){
		this.goTo(this.getPoseProvider().gridGoTo(x, y, getPoseProvider().getPose().getHeading()));		
	}

	public void rotatePanel(){
		float heading = getPoseProvider().getPose().getHeading();
		rotateTo(heading + 180); //overflow is handled gracefully
		waitForStop();
		getMoveController().reverse();
				
		
	}
	
	
}
