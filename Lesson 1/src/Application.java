import lejos.nxt.*;

/**
 * A simple line follower for the LEGO 9797 car with a single light sensor.
 * 
 * The light sensor should be connected to port 3. The left motor should be
 * connected to port C and the right motor to port B.
 * 
 * Variables initialized with a constant string are used in the LCD.drawString
 * calls of the control loop to avoid garbage collection.
 * 
 * @author Ole Caprani
 * @version 22.08.08
 */
public class Application {

	public static void main(String[] aArg) throws Exception {
		PrjDataLogger logger = new PrjDataLogger();
		String left = "Turn left ";
		String right = "Turn right";

		LightSensor light = new LightSensor(SensorPort.S1);
		final int blackWhiteThreshold = 45;

		final int forward = 1;
		final int stop = 3;
		final int flt = 4;
		final int power = 80;

		// Use the light sensor as a reflection sensor
		light.setFloodlight(true);
		LCD.drawString("Light %: ", 0, 0);

		// Show light percent until LEFT is pressed
		LCD.drawString("Press LEFT", 0, 2);
		LCD.drawString("to start", 0, 3);
		while (!Button.LEFT.isDown()) {
			LCD.drawInt(light.readValue(), 3, 9, 0);
		}
		
		LCD.drawString("Waiting for logger to connect", 0, 2);
		logger.connect();
		
		// Follow line until ESCAPE is pressed
		LCD.drawString("Press ESCAPE", 0, 2);
		LCD.drawString("to stop ", 0, 3);
		int sensorVal = 0;
		
		while (!Button.ESCAPE.isDown()) {

			if (light.readValue() > blackWhiteThreshold) {
				// On white, turn right
				LCD.drawString(right, 0, 1);
				MotorPort.B.controlMotor(0, stop);
				MotorPort.C.controlMotor(power, forward);
				
			} else {
				// On black, turn left
				LCD.drawString(left, 0, 1);
				MotorPort.B.controlMotor(power, forward);
				MotorPort.C.controlMotor(0, stop);
			}
			
			sensorVal = light.readValue();
			LCD.drawInt(sensorVal, 3, 9, 0);
			
			logger.log(sensorVal);
			
			Thread.sleep(50);
		}

		// Stop car gently with free wheel drive
		MotorPort.B.controlMotor(0, flt);
		MotorPort.C.controlMotor(0, flt);
		LCD.clear();
		LCD.drawString("Program stopped", 0, 0);
		Thread.sleep(1000);
	}
}