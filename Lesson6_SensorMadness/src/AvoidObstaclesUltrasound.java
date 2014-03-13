import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class AvoidObstaclesUltrasound {

	private static UltrasonicSensor uLeft = new UltrasonicSensor(SensorPort.S1);
	private static UltrasonicSensor uRight = new UltrasonicSensor(SensorPort.S4);

	private static MotorPort mLeft = MotorPort.C;
	private static MotorPort mRight = MotorPort.B;

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

		while(true){
			int diff = (int) (uRight.getRange() - uLeft.getRange());
			
			if(diff > 10)
				diff = 10;
			else if(diff < -10)
				diff = -10;
			
			mLeft.controlMotor(65 - diff, MotorPort.FORWARD);
			mRight.controlMotor(65 + diff, MotorPort.FORWARD);
			LCD.clear();
			LCD.drawInt(65 + diff, 0, 0);
			LCD.drawInt(65 - diff, 0, 1);

		}
		
	}

}
