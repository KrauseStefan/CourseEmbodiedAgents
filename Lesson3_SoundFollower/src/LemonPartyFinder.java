import lejos.nxt.*;

public class LemonPartyFinder {

	private static SoundSensor Left = new SoundSensor(SensorPort.S1);
	private static SoundSensor Right = new SoundSensor(SensorPort.S2);
	
	private static int Threshold =20;
	
	
	public static void mainParty(String[] args) throws Exception {
		LCD.drawString("Finding party: ", 0, 0);
		LCD.refresh();
		int micL;
		int micR;
		int turn;

		while (!Button.ESCAPE.isDown()) {
			micL = Left.readValue();
			micR = Right.readValue();
			LCD.drawInt(micL, 0, 2);
			LCD.drawInt(micR, 0, 3);
			turn = 0;
			if(micL > Threshold || micR > Threshold)
			{
				Car.forward(50+(micR-Threshold)*2, 50+(micL-Threshold)*2);
				Thread.sleep(100);
				Car.stop();
			}
		}
		Car.stop();
		LCD.clear();
		LCD.drawString("Program stopped", 0, 0);
		Thread.sleep(2000);
	}
	
}
