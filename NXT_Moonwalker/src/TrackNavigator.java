import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;


public class TrackNavigator extends Navigator {

	public TrackNavigator(ReversibleDifferentialPilot pilot, PoseProvider poseProvider) {
		super(pilot, poseProvider);
	}
	
	public ReversibleDifferentialPilot getMoveController(){
		return (ReversibleDifferentialPilot) super.getMoveController();
	}
	
	public GridPoseProvider getPoseProvider(){
		return (GridPoseProvider) super.getPoseProvider();
	}
	
	public void gridGoTo(int x, int y, float heading){
		this.goTo(this.getPoseProvider().gridGoTo(x, y, heading));
	}

	public void gridGoTo(int x, int y){
		this.goTo(this.getPoseProvider().gridGoTo(x, y, getPoseProvider().getPose().getHeading()));		
	}

	public void rotatePanel(){
		float rotateDeg = 180;
		Pose pose = getPoseProvider().getPose();
		rotateTo(pose.getHeading() + rotateDeg); //overflow is handled gracefully
		waitForStop();
		
		getMoveController().reverse();
		getPoseProvider().setPose(pose);
		
	}
	
	
}
