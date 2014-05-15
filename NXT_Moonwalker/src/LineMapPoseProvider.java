import lejos.nxt.LightSensor;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Pose;


public class LineMapPoseProvider implements PoseProvider {

	LightSensor ls = null;
	LineMap lm = null;
	
	public LineMapPoseProvider(LightSensor lightSensor, LineMap lineMap) {
		this.ls = lightSensor;
		this.lm = lineMap;
	}
	
	Pose pose = new Pose();
	
	@Override
	public Pose getPose() {
		return pose;
	}

	@Override
	public void setPose(Pose aPose) {
		this.pose = aPose;
	}

}
