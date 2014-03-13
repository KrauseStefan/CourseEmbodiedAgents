import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LightSensor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;

public class LightFollower {

	private static LightSensor lLeft = new LightSensor(SensorPort.S1);
	private static LightSensor lRight = new LightSensor(SensorPort.S4);

	private static MotorPort mLeft = MotorPort.C;
	private static MotorPort mRight = MotorPort.B;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

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
			int diff = lLeft.getLightValue() - lRight.getLightValue();

			mLeft.controlMotor(65 + diff, MotorPort.FORWARD);
			mRight.controlMotor(65 - diff, MotorPort.FORWARD);
		}
	}
}
