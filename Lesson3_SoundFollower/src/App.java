import lejos.nxt.*;

public class App {

	private static PrjDataLogger prjLogger = new PrjDataLogger();
	
	public static void main(String[] args) throws Exception {
		SoundCtrCar.mainSoundCtrCar(args);
		
		
//		SoundSensor s = new SoundSensor(SensorPort.S2);
//		int soundLevel;
//		
//		prjLogger.connect();
//		
//		LCD.drawString("Level(dB) ", 0, 0);
//
//		while (!Button.ESCAPE.isDown()) {
//			soundLevel = s.readValue();
//			LCD.drawInt(soundLevel, 3, 9, 0);
//			prjLogger.log(soundLevel);
//			Thread.sleep(5);
//		}
//		LCD.clear();
//		LCD.drawString("Program stopped", 0, 0);
//		Thread.sleep(2000);
	}

}
