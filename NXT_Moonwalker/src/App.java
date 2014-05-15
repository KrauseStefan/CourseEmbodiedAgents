import lejos.nxt.*;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.*;

public class App {
	// CONFIGURATION
	final static double WHEEL_DIAMETER = MoveController.WHEEL_SIZE_NXT2;
	final static double TRACK_WIDTH = 17.6; // TODO: use correct values
	
	public static void main(String[] args) {
		NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(MotorPort.A), rightMotor = new NXTRegulatedMotor(MotorPort.B);

		LightSensor lightSensor = new LightSensor(SensorPort.S1); //TODO: Correct port
		LineMap lineMap = new TrackLineMap(); //TODO make the actual map
		
		DifferentialPilot dp = new DifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, leftMotor, rightMotor);
		PoseProvider lineMapPoseProvider = new LineMapPoseProvider(dp, lightSensor, lineMap);
		
		NXT_Moonwalker program = new NXT_Moonwalker(new TrackNavigator(dp, lineMapPoseProvider));

		
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}
			
			@Override
			public void buttonPressed(Button b) {
				System.exit(0);
			}
		});
		
		program.run();

	}

}
