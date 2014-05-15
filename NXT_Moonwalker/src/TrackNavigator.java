import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;


public class TrackNavigator extends Navigator {

	public TrackNavigator(MoveController pilot, PoseProvider poseProvider) {
		super(pilot, poseProvider);
		// TODO Auto-generated constructor stub
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

	
}
