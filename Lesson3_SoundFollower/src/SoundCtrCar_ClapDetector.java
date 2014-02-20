import lejos.nxt.*;

/**
 * The locomotions of a LEGO 9797 car is controlled by sound detected through a
 * microphone on port 1.
 * 
 * @author Ole Caprani
 * @version 23.08.07
 */
public class SoundCtrCar_ClapDetector {
	private static int soundThresholdHigh = 85;
	private static int soundThresholdLow = 30;
	private static SoundSensor sound = new SoundSensor(SensorPort.S1);
	// private static PrjDataLogger logger = new PrjDataLogger();

	private static boolean stop = false;

	private static int readValue() throws InterruptedException {
		int sensorVal = sound.readValue();
		// logger.log(sensorVal);
		return sensorVal;
	}

	private static boolean waitForLoudSound(long timeout) throws Exception {
		int soundLevel;
		long endTime = System.currentTimeMillis() + timeout;

		do {
			soundLevel = readValue();
			LCD.drawInt(soundLevel, 4, 10, 1);
			if (soundLevel > soundThresholdHigh || stop)
				return true;
		} while (endTime >= System.currentTimeMillis() || timeout == 0);

		return false;
	}

	private static boolean waitForLowSound(long timeout) throws Exception {
		int soundLevel;
		long endTime = System.currentTimeMillis() + timeout;
		do {
			soundLevel = readValue();
			if(timeout != 0)
				LCD.drawInt(soundLevel, 4, 10, 1);
			if (soundLevel < soundThresholdLow || stop)
				return true;
		} while (endTime >= System.currentTimeMillis() || timeout == 0);

		return false;
	}

	private static void waitForClap() throws Exception {

		boolean possibleClapDetected = false;
		do {
			LCD.drawString("dB level: ", 0, 1);
			LCD.drawString("Waiting for low ", 0, 3);
			LCD.drawString("1", 0, 4);
			waitForLowSound(0);

			LCD.drawString("Waiting for high", 0, 3);
			LCD.drawString("2", 0, 4);
			possibleClapDetected = waitForLoudSound(25);

			if (possibleClapDetected){
				LCD.drawString("Waiting for low ", 0, 3);
				LCD.drawString("3", 0, 4);
				possibleClapDetected = waitForLowSound(350);
			}
			if (possibleClapDetected){
				LCD.drawString("Clap Detected", 0, 0);
				Thread.sleep(2000);
			}
		} while (possibleClapDetected == false && stop == false);
	}

	public static void mainSoundCtrCar(String[] args) throws Exception {

		ButtonListener bl = new ButtonListener() {
			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				stop = true;
			}
		};
		Button.ESCAPE.addButtonListener(bl);
		LCD.refresh();

		// logger.connect();

		while (!stop) {
			LCD.clear();
			LCD.drawString("Waiting", 0, 0);
			waitForClap();
		}
		Car.stop();
		LCD.clear();
		LCD.drawString("Program stopped", 0, 0);
	}
}