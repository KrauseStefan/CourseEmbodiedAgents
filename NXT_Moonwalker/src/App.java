import lejos.nxt.*;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.*;

public class App {
	// CONFIGURATION
	final static double WHEEL_DIAMETER = 5.475;
//	final static double TRACK_WIDTH = 17.2; // TODO: use correct values
//	final static double TRACK_WIDTH = 17.4; // TODO: use correct values
//	final static double TRACK_WIDTH = 17.45; // TODO: use correct values
//	final static double TRACK_WIDTH = 18.45; // TODO: use correct values
//	final static double TRACK_WIDTH = 18.00; // TODO: use correct values
	final static double TRACK_WIDTH = 17.90; // TODO: use correct values
	
	final static int BLACK = 460;
	final static int WHITE = 670;
	
	public static void main(String[] args) throws Exception {
		NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(MotorPort.A), rightMotor = new NXTRegulatedMotor(MotorPort.B);

		LightSensor lightSensor = new LightSensor(SensorPort.S1); //TODO: Correct port
		ColorSensor colorSensor = new ColorSensor(SensorPort.S4);

		SolarPanelDetector colorDetector = new SolarPanelDetector(colorSensor);
		
//		DifferentialPilot dp = new DifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, leftMotor, rightMotor);
		ReversibleDifferentialPilot dp = new ReversibleDifferentialPilot(WHEEL_DIAMETER, TRACK_WIDTH, leftMotor, rightMotor);
		PoseProvider lineMapPoseProvider = new GridPoseProvider(dp, lightSensor, BLACK, WHITE);
				
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
		
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			cg.stopTurn();
			}
			
			@Override
			public void buttonPressed(Button b) {
				ClawController.TurnClaw(170, 30);
				//cg.startTurn(true);
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
				cg.stopTurn();
			}
			
			@Override
			public void buttonPressed(Button b) {
				ClawController.TurnClaw(170,30);
				//cg.startTurn(false);
			}
		});
		
		Button.ENTER.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}
			
			@Override
			public void buttonPressed(Button b) {
				cg.setNextState();
			}
		});
		
		
		
//		UtilityScenarios.calibrationProgram(navigator);
		
//		program.run();

	}

}
