import lejos.nxt.*;
import lejos.robotics.localization.PoseProvider;

public class App {
	// CONFIGURATION
	final static double WHEEL_DIAMETER = 5.475;
//	final static double TRACK_WIDTH = 18.45; // TODO: use correct values
//	final static double TRACK_WIDTH = 18.00; // TODO: use correct values
	final static double TRACK_WIDTH = 18.90; // TODO: use correct values
	
	final static int LEFT_BLACK = 408;//458; //420;
	final static int LEFT_WHITE = 586;//643; //596;
	
	final static int RIGHT_BLACK = 417; //417; //404;
	final static int RIGHT_WHITE = 540; //592; //643; //615;
	
	final static int CENTER_BLACK = 404;
	final static int CENTER_WHITE = 615;
	
	public static void main(String[] args) throws Exception {
		NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(MotorPort.A), rightMotor = new NXTRegulatedMotor(MotorPort.B);


		BlackWhiteSensor bwsLeft = new BlackWhiteSensor(new LightSensor(SensorPort.S1), LEFT_BLACK, LEFT_WHITE);
//		bwsLeft.calibrate();
		BlackWhiteSensor bwsRight = new BlackWhiteSensor(new LightSensor(SensorPort.S2), RIGHT_BLACK, RIGHT_WHITE);
//		bwsRight.calibrate();
		BlackWhiteSensor btwCenter = new BlackWhiteSensor(new LightSensor(SensorPort.S3), CENTER_BLACK, CENTER_WHITE);
		ColorSensor colorSensor = new ColorSensor(SensorPort.S4);
		 
		SolarPanelDetector solarPanelDetector = new SolarPanelDetector(colorSensor);
		
		ReversibleDifferentialPilot dp = new ReversibleDifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, leftMotor, rightMotor);
		dp.setTravelSpeed(8);
		LineFollower lineFol = new LineFollower(btwCenter, dp);

		PoseProvider lineMapPoseProvider = new GridPoseProvider(dp, bwsLeft, bwsRight);
		ClawController clawController = new ClawController(Motor.C);
				
		dp.setAcceleration((int) (dp.getMaxTravelSpeed()));
		TrackNavigator navigator = new TrackNavigator(dp, lineMapPoseProvider);
		NXT_Moonwalker program = new NXT_Moonwalker(navigator, solarPanelDetector, clawController, lineFol);
		
		
		
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
//		UtilityScenarios.driveLineBackAndForth(navigator);

		//UtilityScenarios.testBWLightSensor(btwCenter);
//		UtilityScenarios.testBWLightSensor(btwCenter);
//		UtilityScenarios.calibrateHeadingTest(navigator);
//		UtilityScenarios.testBWLightSensors(bwsLeft, bwsRight);
		program.run();
		//while(true){}

	}

}
