import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class FollowLightAvoidObstacles  {

	private static UltrasonicSensor uLeft = new UltrasonicSensor(SensorPort.S1);
	private static UltrasonicSensor uRight = new UltrasonicSensor(SensorPort.S4);

	private static LightSensor lLeft = new LightSensor(SensorPort.S2);
	private static LightSensor lRight = new LightSensor(SensorPort.S3);

	private static MotorPort mLeft = MotorPort.C;
	private static MotorPort mRight = MotorPort.B;

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

		lLeft.setFloodlight(false);
		lRight.setFloodlight(false);

		while (true) {
			LCD.clear();
			if (!avoidingObsticles())
				followLight();

			Thread.sleep(200);
		}
	}

	public static void followLight() {
		int diff = (lLeft.getLightValue() - lRight.getLightValue()) * 5;

		mLeft.controlMotor(65 + diff, MotorPort.FORWARD);
		mRight.controlMotor(65 - diff, MotorPort.FORWARD);

		LCD.drawInt(65 + diff, 0, 3);
	}

	public static int limit(int value, int limit) {
		return value > limit ? 0 : value;
	}

	public static boolean avoidingObsticles() {
		final int threshold = 90;
		int rRight = (int) uRight.getRange();
		int rLeft = (int) uLeft.getRange();

		int diff = (limit(rRight, threshold) - limit(rLeft, threshold));

		if (Math.abs(diff) < 2)
			return false;

		if (diff > 10)
			diff = 10;
		else if (diff < -10)
			diff = -10;

		mLeft.controlMotor(65 + diff, MotorPort.FORWARD);
		mRight.controlMotor(65 - diff, MotorPort.FORWARD);

		LCD.drawInt(65 + diff, 0, 0);
		LCD.drawInt(65 - diff, 0, 1);

		return true;
	}

}
