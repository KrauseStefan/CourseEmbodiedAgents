import lejos.nxt.*;
import lejos.robotics.localization.PoseProvider;

public class App {
	// CONFIGURATION
	final static double WHEEL_DIAMETER = 5.475;
//	final static double TRACK_WIDTH = 18.45; // TODO: use correct values
//	final static double TRACK_WIDTH = 18.00; // TODO: use correct values
	final static double TRACK_WIDTH = 18.90; // TODO: use correct values
	
	final static int LEFT_BLACK = 420;
	final static int LEFT_WHITE = 596;
	
	final static int RIGHT_BLACK = 404;
	final static int RIGHT_WHITE = 615;
	
	public static void main(String[] args) throws Exception {
		NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(MotorPort.A), rightMotor = new NXTRegulatedMotor(MotorPort.B);


		BlackWhiteSensor bwsLeft = new BlackWhiteSensor(new LightSensor(SensorPort.S1), LEFT_BLACK, LEFT_WHITE);
		BlackWhiteSensor bwsRight = new BlackWhiteSensor(new LightSensor(SensorPort.S2), RIGHT_BLACK, RIGHT_WHITE);
		 
//		ColorSensor colorSensor = new ColorSensor(SensorPort.S4);

//		SolarPanelDetector colorDetector = new SolarPanelDetector(colorSensor);
		
		ReversibleDifferentialPilot dp = new ReversibleDifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, leftMotor, rightMotor);
		PoseProvider lineMapPoseProvider = new GridPoseProvider(dp, bwsLeft, bwsRight);
				
		dp.setAcceleration((int) (1.6 * dp.getMaxTravelSpeed()));
		TrackNavigator navigator = new TrackNavigator(dp, lineMapPoseProvider);
		NXT_Moonwalker program = new NXT_Moonwalker(navigator);
		
		final ClawController cg = new ClawController();
		
		
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}
			
			@Override
			public void buttonPressed(Button b) {
				System.exit(0);
			}
		});
		
		
		
//		UtilityScenarios.calibrationProgram(navigator);
		
//		UtilityScenarios.testReverse(navigator);
		program.run();

	}

}
