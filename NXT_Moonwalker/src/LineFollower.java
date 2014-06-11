import lejos.nxt.LCD;

public class LineFollower {

	private BlackWhiteSensor sensor;
	private ReversibleDifferentialPilot pilot;

	public LineFollower(BlackWhiteSensor _sensor, ReversibleDifferentialPilot _pilot) {
		this.sensor = _sensor;
		this.pilot = _pilot;
	}

	public void calibrate() {
		sensor.calibrate();
	}

	public void start(iStopCondition stopCond) {
		final int power = 300;
		final float Kp = 1.8f, Ki = 0, Kd = 0;
		float error, derivative, previous_error = 0, integral = 0, dt = 300;
		int sensorVal, powerDiff;

		// LCD.clear();
		LCD.drawString("Light: ", 0, 2);

		LCD.drawString("pDiff: ", 0, 4);
		// LCD.drawString("Right: ", 0, 5);

		while (true) {
			sensorVal = sensor.light();

			error = sensorVal - sensor.blackWhiteThreshold;
			integral = integral + error * dt;
			derivative = (error - previous_error) / dt; // dt??
			powerDiff = (int) (Kp * error + Ki * integral + Kd * derivative);

			LCD.drawInt(sensorVal, 4, 10, 2);
			LCD.drawInt(powerDiff, 4, 10, 4);

			LCD.refresh();
			pilot.forward(power - powerDiff, power + powerDiff);

			if(stopCond.stopLoop())
				return;
			
			Thread.yield();
		}
	}


}
