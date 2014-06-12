import lejos.nxt.LCD;
import lejos.util.PIDController;

public class LineFollower {

	private BlackWhiteSensor sensor;
	private BlackWhiteSensor sensorRef;
	private ReversibleDifferentialPilot pilot;

	private int whiteRef;

	private PIDController pidController;

	public LineFollower(BlackWhiteSensor _sensor, BlackWhiteSensor _sensorRef, ReversibleDifferentialPilot _pilot) {
		this.sensor = _sensor;
		this.sensorRef = _sensorRef;
		this.pilot = _pilot;
	}

	public void calibrate() {
		int calibAngle = 25;
		int white = sensor.light();

		pilot.rotate(calibAngle);

		int black = sensor.light();
		pilot.rotate(-calibAngle);
		sensor.calibrate(black, white);

		whiteRef = sensorRef.light();
		// -15 mens drive more on black less potent to follow stray lines
		// sensor.setBlackWhiteThreshold(sensor.getBlackWhiteThreshold() - 15);
	}

	public void start(iStopCondition stopCond, boolean forward) {
		final int power = 300;
		int sensorVal, powerDiff, whiteDiff = 0;

		pidController = new PIDController(sensor.blackWhiteThreshold, 0);
		pidController.setPIDParam(PIDController.PID_KP, 1.8f);
		pidController.setPIDParam(PIDController.PID_KD, 8f);

		// LCD.clear();
		LCD.drawString("Light: ", 0, 1);

		LCD.drawString("pDiff: ", 0, 2);

		while (!stopCond.stopLoop()) {
			// whiteDiff = (sensorRef.wasWhite() ?
			// sensorRef.getChachedLightValue() - whiteRef : whiteDiff);
			sensorVal = sensor.light() - whiteDiff / 2;

			powerDiff = pidController.doPID(sensorVal);
			if(forward){
			pilot.forward(power + powerDiff, power - powerDiff);}
			else{pilot.backward(power - powerDiff, power + powerDiff);}

			LCD.drawInt(sensorVal, 4, 10, 1);
			LCD.drawInt(powerDiff, 4, 10, 2);
			LCD.refresh();

			Thread.yield();
		}
		pilot.stop();
		LCD.clear();
	}

}
