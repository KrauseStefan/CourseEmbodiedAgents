import lejos.nxt.*;

/**
 * A LEGO 9797 car with a sonar sensor. The sonar is used to maintain the car at
 * a constant distance to objects in front of the car.
 * 
 * The sonar sensor should be connected to port 1. The left motor should be
 * connected to port C and the right motor to port B.
 * 
 * @author Ole Caprani
 * @version 24.08.08
 */
public class WallTracker {
	public static void mainTracker(String[] aArg) throws Exception {
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		int distance, desiredDistance = 40, // cm
		powerDiff, avgPower = 65;

		final float Kp = 1.7f, Ki = 0, Kd = 0;

		float error, derivative, previous_error = 0, integral = 0, dt = 300;

		LCD.drawString("Distance: ", 0, 1);
		LCD.drawString("Power:    ", 0, 2);

		while (!Button.ESCAPE.isDown()) {

			distance = Math.min(us.getDistance(), 140);

			error = distance - desiredDistance;
			integral = integral + error * dt;
			derivative = (error - previous_error) / dt;

			powerDiff = (int) (Kp * error + Ki * integral + Kd * derivative);
			previous_error = error;

			// if ( error > 0 )
			// {
			powerDiff = Math.min(powerDiff, 7);
			Car.forward(avgPower + powerDiff, avgPower - powerDiff);
			LCD.drawString("Forward ", 0, 3);
			// }
			// else
			// {
			// powerDiff = Math.min(avgPower + Math.abs(powerDiff),100);
			// Car.backward(powerDiff, powerDiff);
			// LCD.drawString("Backward", 0, 3);
			// }

			LCD.drawInt(distance, 4, 10, 1);
			LCD.drawInt(powerDiff, 4, 10, 2);

			Thread.sleep((int) dt);
		}

		Car.stop();
		// LCD.clear();
		// LCD.drawString("Program stopped", 0, 0);
		// Thread.sleep(2000);
	}
}