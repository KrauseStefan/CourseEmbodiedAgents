import lejos.nxt.LCD;
import lejos.util.PIDController;

public class LineFollower {

	private BlackWhiteSensor sensor;
	private ReversibleDifferentialPilot pilot;

	private PIDController pidController;
	
	public LineFollower(BlackWhiteSensor _sensor, ReversibleDifferentialPilot _pilot) {
		this.sensor = _sensor;
		this.pilot = _pilot;
		
	}

	public void calibrate() {
		
		int white = sensor.light();
		
		pilot.rotate(30);
		
		int black = sensor.light();
		pilot.rotate(-30);
		sensor.calibrate(black, white);
		
		// -15 mens drive more on black less potent to follw stray lines
		sensor.setBlackWhiteThreshold((int) (sensor.getBlackWhiteThreshold() - 15));
	}

	public void start(iStopCondition stopCond) {
		final int power = 300;
		int sensorVal, powerDiff;
		
		pidController = new PIDController(sensor.blackWhiteThreshold, 0);
		pidController.setPIDParam(PIDController.PID_KP, 1.8f);
		pidController.setPIDParam(PIDController.PID_KD, 8f);
		
		
		// LCD.clear();
		LCD.drawString("Light: ", 0, 2);

		LCD.drawString("pDiff: ", 0, 4);

		while (!stopCond.stopLoop()) {
			sensorVal = sensor.light();

			powerDiff = pidController.doPID(sensorVal);
			pilot.forward(power + powerDiff, power - powerDiff);			

			LCD.drawInt(sensorVal, 4, 10, 2);
			LCD.drawInt(powerDiff, 4, 10, 4);
			LCD.refresh();
			
			Thread.yield();
		}
	}


}
