import lejos.nxt.*;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.*;

public class App {
	// CONFIGURATION
	final static double WHEEL_DIAMETER = 5;
	final static double TRACK_WIDTH = 10; // TODO: use correct values
	
	public static void main(String[] args) {
		NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(MotorPort.A), rightMotor = new NXTRegulatedMotor(MotorPort.B);

		MCLPoseProvider provider;
		
		LightSensor lightSensor = new LightSensor(SensorPort.S1); //TODO: Correct port
		LineMap lineMap = new TrackLineMap(); //TODO make the actual map
		
		PoseProvider lineMapPoseProvider = new LineMapPoseProvider(lightSensor, lineMap);
		DifferentialPilot dp = new DifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, leftMotor, rightMotor);
		
		NXT_Moonwalker program = new NXT_Moonwalker(new Navigator(dp, lineMapPoseProvider));

		program.run();

	}

}
