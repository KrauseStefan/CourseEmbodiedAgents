import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;

public class DancingRobot {
	static SoundSensor sound = new SoundSensor(SensorPort.S1);

	static final int threshold = 30;

	private static MotorPort leftMotor = MotorPort.C;
	private static MotorPort rightMotor = MotorPort.B;

	public static void main(String[] args) throws InterruptedException {

		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				System.exit(0);
			}
		});

		while (true) {
			int soundVal = sound.readValue();

			if (soundVal > threshold) {
				dance();
				// Car.forward(soundVal, soundVal);
			} else {
				Car.stop();
			}

		}

	}

	static int intensity = 0;
	public static void dance() throws InterruptedException {
		LCD.clear();
		LCD.drawInt(intensity, 0, 0);

		intensity += Math.random()*5;
		if(intensity >= 35)
			intensity = -35;
		
		leftMotor.controlMotor(65 + Math.abs(intensity), (intensity < 0 ?  MotorPort.FORWARD : MotorPort.BACKWARD));
		rightMotor.controlMotor(65 + Math.abs(intensity), (intensity >= 0 ?  MotorPort.FORWARD : MotorPort.BACKWARD));
		Thread.sleep(50);
	}

}
